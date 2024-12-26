package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.colaboraciones.Frecuencia;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DonacionDineroController implements WithSimplePersistenceUnit, TransactionalOps {
  public Map<String, Object> index(@NotNull Context ctx) {
    String filtroParametro = ctx.queryParam("likeNombre");

    Map model = new HashMap<>();
    model.put("likeNombre", filtroParametro);

    return model;
  }

  public void saveForm(Context ctx) {
    ctx.result("Prueba");
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
    String monto = ctx.formParam("monto");
    String frecuencia = ctx.formParam("frecuencia");
    String capacidadHeladera = ctx.formParam("capacidad-heladera");
    String fechaInicio = ctx.formParam("fecha-donacion-inicio");
    String fechaFin = ctx.formParam("fecha-donacion-fin");

    String nroTarjeta = ctx.formParam("nro-tarjeta");
    String mes = ctx.formParam("mes");
    String anio = ctx.formParam("anio");
    String cvc = ctx.formParam("cvc");

    withTransaction(() -> {

      // Guarda o actualiza el usuario en la base de datos
      usuarioColaborador.donarDinero(Integer.parseInt(monto), Frecuencia.valueOf(frecuencia));
    });

    // Responde con un mensaje de éxito
    ctx.result("Formulario guardado correctamente!");
  }
}
