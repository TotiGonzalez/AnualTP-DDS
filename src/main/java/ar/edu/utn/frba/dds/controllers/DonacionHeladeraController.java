package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.model.colaboradores.RepoColaboradores;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Direccion;
import ar.edu.utn.frba.dds.model.heladeras.ConfiguracionHeladera;
import ar.edu.utn.frba.dds.model.heladeras.EstadoHeladera;
import ar.edu.utn.frba.dds.model.heladeras.Heladera;
import ar.edu.utn.frba.dds.model.heladeras.UbicacionPrecisa;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public class DonacionHeladeraController implements WithSimplePersistenceUnit, TransactionalOps {
  private Integer altura = 0;

  public Map<String, Object> index(@NotNull Context ctx) {
    String filtroParametro = ctx.queryParam("likeNombre");

    Map model = new HashMap<>();
    model.put("likeNombre", filtroParametro);

    return model;
  }

  public void saveForm(Context ctx) {

    Integer usuarioId = ctx.sessionAttribute("usuario_id");

    // Valida que el usuario esté logueado
    if (usuarioId == null) {
      ctx.result("Usuario no autenticado");
      return;
    }

    // Verifica si el usuario es un colaborador
    Colaborador usuarioColaborador = RepoColaboradores.getInstance()
        .buscarColaboradorJuridicoPorID(usuarioId);

    if (usuarioColaborador == null) {
      ctx.result("El usuario no es un colaborador");
      return;
    }

    // Obtiene los valores de los campos del formulario
    String nombreHeladera = ctx.formParam("nombre-heladera");
    String direccionHeladera = ctx.formParam("direccion-heladera");
    String capacidadHeladera = ctx.formParam("capacidad-heladera");
    String fechaHeladera = ctx.formParam("fecha-colocacion");

    // Divide el string de la dirección en sus partes
    String[] partesDireccion = direccionHeladera.split("\\s+"); // Divide por uno o más espacios

    // Asegura que tienes al menos 4 partes para calle, ciudad, provincia y código postal
    String calle = partesDireccion.length > 0 ? partesDireccion[0] : "";
    try {
      altura = partesDireccion.length > 1 ? Integer.parseInt(partesDireccion[1]) : 0;
    } catch (NumberFormatException e) {
      System.out.println("Error al parsear altura: " + e.getMessage());
    }
    String ciudad = partesDireccion.length > 2 ? partesDireccion[2] : "";
    String localidad = partesDireccion.length > 3 ? partesDireccion[3] : "";

    withTransaction(() -> {
      // Realiza las asignaciones de los valores del formulario
      UbicacionPrecisa ubicacion = new UbicacionPrecisa(ciudad,
          new Direccion(ciudad,
              localidad, calle, altura), 0.0, 0.0);

      //generico
      ConfiguracionHeladera config = new ConfiguracionHeladera(5.0,
          25.0, 3);

      Heladera heladera = new Heladera("nombreHeladera", ubicacion,
          EstadoHeladera.ACTIVA, 100, config);

      // Guarda o actualiza el usuario en la base de datos
      usuarioColaborador.hacerseCargoDeHeladera(heladera);
    });

    // Responde con un mensaje de éxito
    ctx.result("Formulario guardado correctamente!");
  }
}
