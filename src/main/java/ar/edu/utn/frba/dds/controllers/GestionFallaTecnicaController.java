package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.Usuario;
import ar.edu.utn.frba.dds.model.heladeras.FallaTecnica;
import ar.edu.utn.frba.dds.repositories.RepositorioFallaTecnica;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GestionFallaTecnicaController implements WithSimplePersistenceUnit, TransactionalOps {
  Map<String, Object> model = new HashMap<>();
  private final RepositorioFallaTecnica reporteRepository = RepositorioFallaTecnica.INSTANCE;

  public Map<String, Object> index(Context ctx) {
    ctx.result("Prueba");
    Integer usuarioId = ctx.sessionAttribute("usuario_id");

    // Valida que el usuario esté logueado
    if (usuarioId == null) {
      ctx.result("Usuario no autenticado");
      ctx.redirect("/login");
      return model;
    }

    int page = ctx.queryParamAsClass("page", Integer.class).getOrDefault(1);
    int pageSize = 10;

    // Sorting Parameters
    String sortField = ctx.queryParam("sortField") != null ? ctx.queryParam("sortField") : "fechaYhora";
    String sortOrder = ctx.queryParam("sortOrder") != null ? ctx.queryParam("sortOrder") : "desc";

// Search Parameters
    String searchType = ctx.queryParam("searchType") != null ? ctx.queryParam("searchType") : "nombreHeladera";
    String searchQuery = ctx.queryParam("search") != null ? Objects.requireNonNull(ctx.queryParam("search")).toLowerCase() : "";

    Boolean solucionada = null; // Default: No filter
    if ("solucionada".equals(sortField)) {
      solucionada = "asc".equals(sortOrder); // asc = true (solved), desc = false (pending)
    }

    List<FallaTecnica> fallaTecnicaList;
    long totalReportes;

    try {
      // Fetch reports using dynamic filters
      fallaTecnicaList = reporteRepository.findFallasWithFilter(page, pageSize, sortField, sortOrder, solucionada, searchType, searchQuery);
      totalReportes = reporteRepository.countFallas(); // Adjust if total count needs filtering
    } catch (Exception e) {
      fallaTecnicaList = Collections.emptyList();
      totalReportes = 0;
      model.put("emptyMessage", "No hay reportes de fallas");
    }

    // Pagination Metadata
    int totalPages = (int) Math.ceil((double) totalReportes / pageSize);
    boolean isFirstPage = page <= 1;
    boolean isLastPage = page >= totalPages;

    // Format Data
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    List<Map<String, Object>> transformedReportes = fallaTecnicaList.stream().map(falla -> {
      Map<String, Object> reporteData = new HashMap<>();
      reporteData.put("id", falla.getId());
      reporteData.put("titulo", falla.getTitulo());
      reporteData.put("fechaYhora", falla.getFechaYhora()
          .atZone(ZoneId.of("UTC"))
          .format(formatter));
      reporteData.put("solucionada", falla.getSolucionada());
      reporteData.put("nombreHeladera", falla.getHeladeraInvolucrada() != null
          ? falla.getHeladeraInvolucrada().getNombreHeladera()
          : "Sin asignar");
      reporteData.put("ubicacion", falla.getHeladeraInvolucrada() != null
          ? falla.getHeladeraInvolucrada().getCiudadYLocalidad()
          : "Sin ubicación");
      return reporteData;
    }).toList();

    // Prepare Model
    model.put("reportes", transformedReportes);
    model.put("totalReportes", totalReportes);
    model.put("currentPage", page);
    model.put("totalPages", totalPages);
    model.put("paginationStart", (page - 1) * pageSize + 1);
    model.put("paginationEnd", Math.min(page * pageSize, (int) totalReportes));
    model.put("isFirstPage", isFirstPage);
    model.put("isLastPage", isLastPage);
    model.put("previousPage", Math.max(1, page - 1));
    model.put("nextPage", Math.min(totalPages, page + 1));
    model.put("sortField", sortField);
    model.put("sortOrder", sortOrder);

    return model;
  }

  public Map<String, Object> show(Context ctx) {
    String idParam = ctx.pathParam("id");
    if (idParam == null || idParam.isEmpty()) {
      ctx.status(400).result("ID is missing or invalid");
      return null;
    }

    int id;
    try {
      id = Integer.parseInt(idParam);
    } catch (NumberFormatException e) {
      ctx.status(400).result("Invalid ID format");
      return null;
    }

    FallaTecnica fallaTecnica = reporteRepository.findById(id);
    if (fallaTecnica == null) {
      ctx.status(404).result("FallaTecnica not found");
      return null;
    }

    String imagePath = fallaTecnica.getPathFotos();
    String imageUrl = (imagePath != null && !imagePath.isEmpty())
        ? "src/main/resources/public/uploads/" + imagePath
        : "../public/img/imagen-camara.png"; // Default fallback image

    model.put("pathFotos", imageUrl);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    model.put("titulo", fallaTecnica.getTitulo());
    model.put("fechaYhora", fallaTecnica.getFechaYhora().atZone(ZoneId.of("UTC")).format(formatter));
    model.put("nombreHeladera", fallaTecnica.getHeladeraInvolucrada() != null
        ? fallaTecnica.getHeladeraInvolucrada().getNombreHeladera()
        : "N/A");
    model.put("colaborador", fallaTecnica.getColaboradorFisico() != null
        ? fallaTecnica.getColaboradorFisico().getNombre()
        : "N/A");
    model.put("ubicacion", fallaTecnica.getHeladeraInvolucrada() != null
        ? fallaTecnica.getHeladeraInvolucrada().getCiudadYLocalidad()
        : "N/A");
    model.put("direccion", fallaTecnica.getHeladeraInvolucrada() != null
        ? fallaTecnica.getHeladeraInvolucrada().getDireccion()
        : "N/A");
    model.put("pathFotos", fallaTecnica.getPathFotos() != null && !fallaTecnica.getPathFotos().isEmpty()
        ? imageUrl
        : "../public/img/imagen-camara.png");
    model.put("email", fallaTecnica.getEmail());
    model.put("descripcion", fallaTecnica.getDescripcion());
    model.put("isGrave", fallaTecnica.isGrave());
    model.put("isLeve", !fallaTecnica.isGrave());
    model.put("usuario_id", ctx.sessionAttribute("usuario_id"));
    model.put("esAdmin", ctx.sessionAttribute("es_admin"));
    model.put("id", fallaTecnica.getId());
    model.put("solucionada", fallaTecnica.getSolucionada());

    return model;
  }

  public void toggleFallaState(Context ctx) {
    String idParam = ctx.pathParam("id");
    String action = ctx.pathParam("action"); // "resolve" or "reopen"

    // Ensure the user is an admin
    /*Boolean isAdmin = ctx.sessionAttribute("es_admin");
    if (isAdmin == null || !isAdmin) {
      ctx.status(403).result("Acceso denegado: sólo los administradores pueden realizar esta acción.");
      return;
    }*/

    int id;
    try {
      id = Integer.parseInt(idParam);
    } catch (NumberFormatException e) {
      ctx.status(400).result("Invalid ID format");
      return;
    }

    FallaTecnica fallaTecnica = reporteRepository.findById(id);
    if (fallaTecnica == null) {
      ctx.status(404).result("Falla Técnica no encontrada");
      return;
    }

    if ("resolve".equals(action)) {
      fallaTecnica.establecerSolucionada();
    } else if ("reopen".equals(action)) {
      fallaTecnica.establecerNoSolucionado();
    } else {
      ctx.status(400).result("Acción no válida");
      return;
    }

    try {
      reporteRepository.update(fallaTecnica);
      ctx.json(Map.of("message", "Estado actualizado correctamente"));
    } catch (Exception e) {
      ctx.status(500).result("Hubo un error al actualizar la falla");
    }
  }
}
