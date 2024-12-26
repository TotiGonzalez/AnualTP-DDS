package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.colaboradores.ColaboradorFisico;
import ar.edu.utn.frba.dds.model.colaboradores.RepoColaboradores;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Documento;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegistrarColaboradorFisicoController implements WithSimplePersistenceUnit, TransactionalOps {

  public static Map<String, Object> index(@NotNull Context ctx) {
    String filtroParametro = ctx.queryParam("likeRazonSocial");

    Map<String, Object> model = new HashMap<>();
    model.put("likeRazonSocial", filtroParametro);

    // Agregar más datos específicos de la vista aquí, si es necesario
    return model;
  }

  public void updateForm(Context ctx) {
    Integer usuarioId = ctx.sessionAttribute("usuario_id");

    if (usuarioId == null) {
      ctx.result("Usuario no autenticado");
      return;
    }

    // Buscar el colaborador fisico por ID
    ColaboradorFisico colaborador = RepoColaboradores.getInstance().buscarColaboradorFisicoPorID(usuarioId);

    if (colaborador == null) {
      ctx.result("Colaborador no encontrado");
      return;
    }

    // Capturar datos actualizados del formulario
    String nombre = ctx.formParam("nombre");
    String apellido = ctx.formParam("apellido");
    String fechaNacimientoStr = ctx.formParam("fechaNacimiento");

    LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoStr);  // Suponiendo formato YYYY-MM-DD

    // Actualizar el colaborador en una transacción
    withTransaction(() -> {
      colaborador.setNombre(nombre);
      colaborador.setApellido(apellido);
      colaborador.setFechaNacimiento(fechaNacimiento);
    });

    // Responder con éxito
    ctx.result("Colaborador físico actualizado correctamente!");
  }
}
