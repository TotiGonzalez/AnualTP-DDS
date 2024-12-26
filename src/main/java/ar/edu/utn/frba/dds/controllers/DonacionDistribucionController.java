package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.colaboraciones.MotivoDistribuicion;
import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.model.colaboradores.RepoColaboradores;
import ar.edu.utn.frba.dds.model.heladeras.Heladera;
import ar.edu.utn.frba.dds.model.heladeras.MapaHeladeras;
import ar.edu.utn.frba.dds.model.heladeras.SolicitudApertura;
import ar.edu.utn.frba.dds.model.heladeras.SolicitudAperturaService;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public class DonacionDistribucionController implements WithSimplePersistenceUnit, TransactionalOps {
  private SolicitudAperturaService solicitudAperturaService;
  private MotivoDistribuicion motivoSeleccionado;

  public Map<String, Object> index(@NotNull Context ctx) {
    String filtroParametro = ctx.queryParam("likeNombre");

    Map model = new HashMap<>();
    model.put("likeNombre", filtroParametro);

    return model;
  }

  public void saveForm(Context ctx) {
    Integer cantidadViandas;

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
    String nombreHeladeraOrigen = ctx.formParam("nombre-heladera-origen");
    String nombreHeladeraDestino = ctx.formParam("nombre-heladera-destino");
    String cantidadViandasStr = ctx.formParam("cantidad-viandas");
    String motivo = ctx.formParam("motivo");

    cantidadViandas = Integer.parseInt(cantidadViandasStr);

    Heladera heladeraOrigen = MapaHeladeras.getInstance().buscarHeladera(nombreHeladeraOrigen);
    Heladera heladeraDestino = MapaHeladeras.getInstance().buscarHeladera(nombreHeladeraDestino);

    motivoSeleccionado = MotivoDistribuicion.valueOf(motivo);

    withTransaction(() -> {
      // Guarda o actualiza el usuario en la base de datos
      usuarioColaborador.distribuirVianda(heladeraOrigen, heladeraDestino,
          cantidadViandas,motivoSeleccionado, solicitudAperturaService);
    });

    // Responde con un mensaje de éxito
    ctx.result("Formulario guardado correctamente!");
  }
}
