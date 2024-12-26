package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.Captura;
import ar.edu.utn.frba.dds.model.Usuario;
import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.model.colaboradores.RepoColaboradores;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Direccion;
import ar.edu.utn.frba.dds.model.heladeras.Comida;
import ar.edu.utn.frba.dds.model.heladeras.ConfiguracionHeladera;
import ar.edu.utn.frba.dds.model.heladeras.EstadoHeladera;
import ar.edu.utn.frba.dds.model.heladeras.Heladera;
import ar.edu.utn.frba.dds.model.heladeras.MapaHeladeras;
import ar.edu.utn.frba.dds.model.heladeras.SolicitudAperturaService;
import ar.edu.utn.frba.dds.model.heladeras.UbicacionPrecisa;
import ar.edu.utn.frba.dds.model.heladeras.Vianda;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DonacionViandaController implements WithSimplePersistenceUnit, TransactionalOps {
  private SolicitudAperturaService solicitudAperturaService;

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
    String nombreComida = ctx.formParam("nombre-comida");
    String nombreHeladera = ctx.formParam("nombre-heladera");
    String calorias = ctx.formParam("calorias");
    String peso = ctx.formParam("peso");
    String fechaCaducidad = ctx.formParam("fecha-cadu");

    withTransaction(() -> {

      Heladera heladera = MapaHeladeras.getInstance().buscarHeladera(nombreHeladera);

      Comida comida = new Comida(nombreComida);

      Vianda vianda = new Vianda(comida, LocalDate.parse(fechaCaducidad),
          Integer.parseInt(calorias), Integer.parseInt(peso));

      // Guarda o actualiza el usuario en la base de datos
      usuarioColaborador.donarViandas(LocalDate.parse(fechaCaducidad), (List<Vianda>) vianda, heladera, solicitudAperturaService);
    });

    // Responde con un mensaje de éxito
    ctx.result("Formulario guardado correctamente!");
  }
}
