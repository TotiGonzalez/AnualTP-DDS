package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.db.dt.HeladeraDTO;
import ar.edu.utn.frba.dds.model.colaboradores.ColaboradorFisico;
import ar.edu.utn.frba.dds.model.colaboradores.Credencial;
import ar.edu.utn.frba.dds.model.colaboradores.RepoColaboradores;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Documento;
import ar.edu.utn.frba.dds.model.heladeras.FallaTecnica;
import ar.edu.utn.frba.dds.model.heladeras.Heladera;
import ar.edu.utn.frba.dds.repositories.RepoCredenciales;
import ar.edu.utn.frba.dds.repositories.RepositorioFallaTecnica;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import ar.edu.utn.frba.dds.repositories.RepositorioHeladeras;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import javax.persistence.EntityManager;


public class AltaFallaController implements WithSimplePersistenceUnit, TransactionalOps, Handler {
  private RepositorioFallaTecnica reporteRepository = RepositorioFallaTecnica.INSTANCE;

  public Map<String, Object> showForm(Context ctx) {
    Map<String, Object> model = new HashMap<>();

    ctx.result("Prueba");
    Integer usuarioId = ctx.sessionAttribute("usuario_id");

    // Valida que el usuario esté logueado
    if (usuarioId == null) {
      ctx.result("Usuario no autenticado");
      ctx.redirect("/login");
      return model;
    }

    // Fetch all heladeras
    List<Heladera> heladeras = RepositorioHeladeras.INSTANCE.findAll();

    // Sort heladeras alphabetically by 'nombreHeladera' (A-Z)
    heladeras = heladeras.stream()
        .sorted((h1, h2) -> h1.getNombreHeladera().compareToIgnoreCase(h2.getNombreHeladera()))
        .toList();

    // Limit to the first 10 heladeras
    List<Heladera> limitedHeladeras = heladeras.stream().limit(10).collect(Collectors.toList());

    // Add the sorted and limited list to the model
    model.put("heladeras", limitedHeladeras);

    model.put("title", "Crear Nueva Falla Tecnica");
    model.put("usuario_id",ctx.sessionAttribute("usuario_id"));
    model.put("esAdmin",ctx.sessionAttribute("es_admin"));
    return model;
  }


  public void create(Context ctx) {

    FallaTecnica nuevaFalla = ctx.bodyAsClass(FallaTecnica.class);

    try {
      reporteRepository.save(nuevaFalla);

      ctx.status(201).result("FallaTecnica report creada exitosamente.");
    } catch (Exception e) {
      ctx.status(500).result("Error creando reporte de FallaTecnica: " + e.getMessage());
    }
  }

  public void processForm(Context ctx) {
    try {
      FallaTecnica nuevaFalla = new FallaTecnica();

      // Set basic form parameters
      nuevaFalla.setTitulo(ctx.formParam("titulo"));
      nuevaFalla.setDescripcion(ctx.formParam("descripcion"));
      nuevaFalla.setEmail(ctx.formParam("email"));
      nuevaFalla.setGrave(Boolean.parseBoolean(ctx.formParam("grave")));
      nuevaFalla.setFechaYhora(LocalDateTime.now());

      // Fetch and set heladera
      Integer heladeraId = Integer.parseInt(ctx.formParam("heladeraInvolucrada"));
      EntityManager em = entityManager();
      Heladera heladera = em.find(Heladera.class, heladeraId);

      if (heladera != null) {
        nuevaFalla.setHeladeraInvolucrada(heladera);
      } else {
        throw new IllegalArgumentException("Heladera no encontrada con ID: " + heladeraId);
      }

      Integer usuarioId = ctx.sessionAttribute("usuario_id");
      if (usuarioId != null) {
        Credencial credencial = RepoCredenciales.INSTANCE.buscar(usuarioId);
        ColaboradorFisico colaboradorFisico =
            RepoColaboradores.getInstance().buscarColaboradorFisicoPorCredencialID(credencial);
        nuevaFalla.setColaboradorFisico(colaboradorFisico);
      }

      // Handle file upload
      var uploadedFile = ctx.uploadedFile("pathFotos");
      if (uploadedFile != null && uploadedFile.filename() != null && !uploadedFile.filename().isEmpty()) {
        // Save file only if it's uploaded
        String uploadDir = "src/main/resources/public/uploads"; // Place inside public directory
        String fileName = UUID.randomUUID() + "_" + uploadedFile.filename();
        Path uploadPath = Paths.get(uploadDir, fileName);

        Files.createDirectories(uploadPath.getParent()); // Ensure directories exist
        Files.copy(uploadedFile.content(), uploadPath, StandardCopyOption.REPLACE_EXISTING);

        // Save relative file path in FallaTecnica
        nuevaFalla.setPathFotos(fileName);
      } else {
        // Ensure the pathFotos is not set if no file is chosen
        nuevaFalla.setPathFotos(null);
      }

      // Save the new FallaTecnica
      withTransaction(() -> reporteRepository.save(nuevaFalla));

      ctx.status(201).result("Falla técnica reportada exitosamente.");
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(400).result("Error al procesar el formulario: " + e.getMessage());
    }

    ctx.json(Map.of("message", "Falla técnica reportada exitosamente."));
  }

  public void searchHeladeras(Context ctx) {
    String searchTerm = ctx.queryParam("search") != null ? ctx.queryParam("search") : "";

    // Fetch all heladeras
    List<Heladera> heladeras = RepositorioHeladeras.INSTANCE.findAll();

    List<HeladeraDTO> filteredHeladeras = heladeras.stream()
        .filter(h -> h.getNombreHeladera().toLowerCase().contains(searchTerm.toLowerCase()))
        .sorted((h1, h2) -> h1.getNombreHeladera().compareToIgnoreCase(h2.getNombreHeladera()))
        .limit(10)
        .map(h -> new HeladeraDTO((long) h.getId(), h.getNombreHeladera())) // Map to DTO
        .collect(Collectors.toList());

    ctx.json(filteredHeladeras);
  }

  @Override
  public void handle(@NotNull Context ctx) throws Exception  {
    FallaTecnica fallaTecnica = ctx.bodyAsClass(FallaTecnica.class);
    this.reporteRepository.save(fallaTecnica);
    ctx.status(HttpStatus.CREATED);
    ctx.result("FallaTecnica report creada exitosamente.");
  }
}
