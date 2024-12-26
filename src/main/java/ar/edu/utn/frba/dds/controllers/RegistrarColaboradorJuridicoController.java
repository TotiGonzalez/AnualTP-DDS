package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.colaboradores.ColaboradorJuridico;
import ar.edu.utn.frba.dds.model.colaboradores.RepoColaboradores;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.TipoJuridico;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegistrarColaboradorJuridicoController implements WithSimplePersistenceUnit, TransactionalOps {

  public static Map<String, Object> index(@NotNull Context ctx) {
    String filtroParametro = ctx.queryParam("likeRazonSocial");

    Map<String, Object> model = new HashMap<>();
    model.put("likeRazonSocial", filtroParametro);

    // Agregar más datos específicos de la vista aquí, si es necesario
    return model;
  }

  public void saveForm(Context ctx) {

    Integer usuarioId = ctx.sessionAttribute("usuario_id");

    // Verificar autenticación
    if (usuarioId == null) {
      ctx.result("Usuario no autenticado");
      return;
    }

    // Verificar si el usuario es colaborador (en este caso, debería ser un usuario administrativo)
    ColaboradorJuridico usuarioColaborador = RepoColaboradores.getInstance()
        .buscarColaboradorJuridicoPorID(usuarioId);

    if (usuarioColaborador == null) {
      ctx.result("El usuario no es un colaborador jurídico");
      return;
    }

    // Capturar datos del formulario
    String razonSocial = ctx.formParam("razonSocial");
    String tipoJuridicoParam = ctx.formParam("tipoJuridico");
    String rubro = ctx.formParam("rubro");

    // Validar tipo de colaborador jurídico
    TipoJuridico tipoJuridico;
    try {
      tipoJuridico = TipoJuridico.valueOf(Objects.requireNonNull(tipoJuridicoParam).toUpperCase());
    } catch (IllegalArgumentException e) {
      ctx.result("Tipo jurídico no válido.");
      return;
    }

    // Crear y registrar el colaborador jurídico dentro de una transacción
    withTransaction(() -> {
      ColaboradorJuridico colaboradorJuridico = (ColaboradorJuridico) RepoColaboradores.getInstance()
          .registrarColaboradorJuridico(tipoJuridico, razonSocial, rubro);
      RepoColaboradores.getInstance().agregarColaborador(colaboradorJuridico);
    });

    // Responder con éxito
    ctx.result("Colaborador jurídico registrado correctamente!");
  }
}
