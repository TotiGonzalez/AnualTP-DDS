package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.colaboraciones.RegistracionPersonaVulnerable;
import ar.edu.utn.frba.dds.model.colaboraciones.RegistracionPersonaVulnerableBuilder;
import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.model.colaboradores.RepoColaboradores;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.*;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegistrarPersonaVulnerableController implements WithSimplePersistenceUnit, TransactionalOps {

  public static Map<String, Object> index(@NotNull Context ctx) {
    String filtroParametro = ctx.queryParam("likeNombre");

    Map<String, Object> model = new HashMap<>();
    model.put("likeNombre", filtroParametro);

    // Podrías agregar más datos específicos de la vista aquí
    return model;
  }

  public void saveForm(Context ctx) {

    Integer usuarioId = ctx.sessionAttribute("usuario_id");

    // Verifica si el usuario está autenticado
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

    // Captura los datos del formulario
    String nombre = ctx.formParam("nombre");
    String apellido = ctx.formParam("apellido");
    String sexo = ctx.formParam("sexo");
    String nombreCompleto = nombre + " " + apellido;
    int edad = Integer.parseInt(Objects.requireNonNull(ctx.formParam("edad")));
    String direccionParam = ctx.formParam("direccion");
    String tipoDocumentoParam = ctx.formParam("tipoDocumento");
    String numeroDocumentoParam = ctx.formParam("numeroDocumento");
    boolean situacionCalle = ctx.formParam("situacionCalle") != null;
    boolean tieneHijos = ctx.formParam("tieneHijos") != null;

    // Divide el string de la dirección en sus partes
    String[] partesDireccion = direccionParam.split(",\\s*"); // Separador por coma y espacios opcionales

    // Asegura que tienes al menos 4 partes para calle, ciudad, provincia y código postal
    String calle = partesDireccion.length > 0 ? partesDireccion[0] : "";
    Integer altura = partesDireccion.length > 1 ? Integer.parseInt(partesDireccion[1].trim()) : 0;
    String ciudad = partesDireccion.length > 3 ? partesDireccion[2] : "";
    String localidad = partesDireccion.length > 2 ? partesDireccion[3] : "";

    // Crear el builder para la registración
    RegistracionPersonaVulnerableBuilder builder = RegistracionPersonaVulnerableBuilder.crear(usuarioColaborador);

    // Configura el builder con los datos capturados
    builder.setNombre(nombreCompleto);
    builder.setFechaNacimiento(LocalDate.now().minusYears(edad)); // suponiendo edad en años
    builder.setSituacionDeCalle(situacionCalle);

    // Configura dirección solo si no está en situación de calle
    if (!situacionCalle) {
      builder.setDireccion(new Direccion(ciudad,
          localidad, calle, altura));  // Asumiendo que `Direccion` tiene un constructor con String
    }

    // Configura tipo de documento y número de documento
    try {
      Documento tipoDocumento = Documento.valueOf(tipoDocumentoParam.toUpperCase()); // Convertimos el String al enum
      builder.setTipoDocumento(tipoDocumento); // Seteamos el tipo de documento en el builder
    } catch (IllegalArgumentException e) {
      ctx.result("Tipo de documento no válido.");
      return;
    }
    builder.setNumeroDocumento(Integer.parseInt(numeroDocumentoParam));

    // Configura si tiene menores bajo tutela y la cantidad
    builder.setTieneMenoresBajoTutela(tieneHijos);
    if (tieneHijos) {
      int cantidadHijos = Integer.parseInt(Objects.requireNonNull(ctx.formParam("cantidadHijos")));
      builder.setCantMenoresBajoTutela(cantidadHijos);
    }

    // Configura la primera tarjeta disponible del colaborador
    builder.setPrimeraTarjetaDisponible();

    // Genera la registración dentro de una transacción
    withTransaction(() -> {
      RegistracionPersonaVulnerable registracion = builder.generarLaRegistracion();

    });

    // Responde con un mensaje de éxito
    ctx.result("Información de la persona vulnerable guardada correctamente!");
  }
}