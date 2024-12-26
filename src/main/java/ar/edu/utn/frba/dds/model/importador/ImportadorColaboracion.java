package ar.edu.utn.frba.dds.model.importador;

import ar.edu.utn.frba.dds.model.colaboraciones.Colaboracion;
import ar.edu.utn.frba.dds.model.colaboraciones.DistribucionVianda;
import ar.edu.utn.frba.dds.model.colaboraciones.DonacionDinero;
import ar.edu.utn.frba.dds.model.colaboraciones.DonacionVianda;
import ar.edu.utn.frba.dds.model.colaboraciones.RegistracionPersonaVulnerable;
import ar.edu.utn.frba.dds.model.colaboradores.ColaboradorFisico;
import ar.edu.utn.frba.dds.model.colaboradores.Credencial;
import ar.edu.utn.frba.dds.model.colaboradores.RepoColaboradores;
import ar.edu.utn.frba.dds.model.colaboradores.medioscontacto.Mail;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Documento;
import ar.edu.utn.frba.dds.model.excepciones.ImportadorColaboracionExeception;
import ar.edu.utn.frba.dds.model.tarjetas.TarjetaPersonaVulnerable;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityTransaction;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class ImportadorColaboracion implements WithSimplePersistenceUnit {

  public void mostrarColaboracion(Colaboracion colaboracion, ColaboradorFisico colaborador, int i) {
    System.out.println("******** " + "Datos importados de Colaboracion " + i + " ********");
    System.out.println("Tipo Doc: " + colaborador.getTipoDocumento());
    System.out.println("Doc: " + colaborador.getNumeroDocumento());
    System.out.println("Nombre de colaborador: " + colaborador.getNombre());
    System.out.println("Apellido de colaborador: " + colaborador.getApellido());
    Mail mail = (Mail) colaborador.getMediosContacto().get(0);
    System.out.println("Mail: " + mail.getDireccionCorreo());
    System.out.println("Fecha de nacimiento de colaborador: " + colaborador.getFechaNacimiento());
    System.out.println("Fecha de colaboracion: " + colaboracion.getFechaColaboracion());
    System.out.println("Forma de colaboracion: " + colaboracion.getClass());
    System.out.println("Usuario de colaborador: " + colaborador.getCredencial().getUsuario());
    System.out.println("Contraseña de colaborador: "
        + colaborador.getCredencial().getContrasenia());

    System.out.println("********" + "Fin de datos importados" + "********\n");
  }

  public Map<String, List<String>> importarColaboracion(RepoColaboradores repoColaboradores,
                                                        String urlArchivo, boolean conPersistencia) {
    List<String> mensajesError = new ArrayList<>();
    List<Colaboracion> colaboraciones = new ArrayList<>();
    Map<String, List<String>> resultado = new HashMap<>();

    try (BufferedReader br = new BufferedReader(new InputStreamReader(
        new FileInputStream(urlArchivo), StandardCharsets.UTF_8))) {
      String line;
      // Saltea el header
      br.readLine();

      int contadorLinea = 0;
      while ((line = br.readLine()) != null) {
        contadorLinea++;
        try {
          colaboraciones.addAll(procesarLinea(repoColaboradores,
              line, contadorLinea, mensajesError, conPersistencia));
        } catch (ImportadorColaboracionExeception e) {
          resultado.put("Errores", mensajesError);
        }
      }
    } catch (IOException e) {
      mensajesError.add("ERROR: Fallo al leer el archivo: ");
      resultado.put("Errores", mensajesError);
    }
    resultado.put("Colaboraciones", colaboraciones.stream().map(colaboracion
        -> colaboracion.getClass().toString()).collect(Collectors.toList()));

    resultado.put("Errores", mensajesError);

    return resultado;
  }

  private List<Colaboracion> procesarLinea(RepoColaboradores repoColaboradores,
                                           String line, int contadorLinea,
                                           List<String> erroresTotales, boolean conPersistencia) {
    String[] values = line.split(";");
    int i = 0;

    List<String> errores = new ArrayList<>();

    String tipoDoc = values[i++];
    validarTipoDocumento(tipoDoc, contadorLinea, errores);

    String valorDocumentoString = values[i++];
    final Integer valorDocumento = validarValorDocumento(
        valorDocumentoString, contadorLinea, errores);

    String nombreDeColaborador = values[i++];
    validarNombreColaborador(nombreDeColaborador, contadorLinea, errores);

    String apellidoDeColaborador = values[i++];
    validarApellidoColaborador(apellidoDeColaborador, contadorLinea, errores);

    String mailDeColaborador = values[i++];
    validarMailColaborador(mailDeColaborador, contadorLinea, errores);

    String fechaDeColaboracionString = values[i++];
    LocalDate fechaDeColaboracion = validarFecha(fechaDeColaboracionString,
        contadorLinea, "fecha de colaboracion", errores);

    String fechaDeNacimientoString = values[i++];
    LocalDate fechaDeNacimiento = validarFecha(fechaDeNacimientoString,
        contadorLinea, "fecha de nacimiento", errores);

    String tipoColaboracion = values[i++];
    validarTipoColaboracion(tipoColaboracion, contadorLinea, errores);

    Integer cantidadDeColaboraciones = validarCantidadDeColaboraciones(
        values, i, contadorLinea, errores);

    if (!errores.isEmpty()) {
      erroresTotales.addAll(errores);
      throw new ImportadorColaboracionExeception(erroresTotales.toString());
    }

    Documento documento = convertirTipoDocumento(tipoDoc, errores);

    ColaboradorFisico colaborador = obtenerColaborador(repoColaboradores,
        documento, valorDocumento,
        nombreDeColaborador, apellidoDeColaborador,
        fechaDeNacimiento, mailDeColaborador, conPersistencia);

    List<Colaboracion> colaboraciones = obtenerColaboraciones(colaborador, tipoColaboracion, fechaDeColaboracion,
        fechaDeNacimiento, cantidadDeColaboraciones);

    if(conPersistencia) {
      EntityTransaction tx = entityManager().getTransaction();
      tx.begin();

      entityManager().persist(colaborador);

      colaboraciones.forEach( c -> entityManager().persist(c));

      tx.commit();

    }

    return colaboraciones;

  }

  private void validarTipoDocumento(String tipoDoc, int contadorLinea, List<String> errores) {
    if (!tipoDoc.equals("LE") && !tipoDoc.equals("LC") && !tipoDoc.equals("DNI")) {
      errores.add("ERROR linea " + contadorLinea + ": El tipo de documento es incorrecto."
          + " Ingresar 'LE' (LIBRETA_ENROLAMIENTO) o 'LC' (LIBRETA_CIVICA) o 'DNI'");
    }
  }

  private Integer validarValorDocumento(String valorDocumentoString,
                                        int contadorLinea, List<String> errores) {

    if (valorDocumentoString.length() > 10 || valorDocumentoString.isBlank()) {
      errores.add("ERROR linea " + contadorLinea + ": Ingresar valor del documento."
          + " Validar que sean 10 caracteres");
    }
    try {
      return Integer.parseInt(valorDocumentoString);
    } catch (NumberFormatException e) {
      errores.add("ERROR linea " + contadorLinea + ": Ingresar valor numérico para el documento");
    }
    return null;
  }

  private void validarNombreColaborador(String nombreDeColaborador,
                                        int contadorLinea, List<String> errores) {
    if (nombreDeColaborador.length() > 51 || nombreDeColaborador.isBlank()) {
      errores.add("ERROR linea " + contadorLinea + ": Ingresar nombre de colaborador."
          + " Debe tener 50 caracteres como máximo");

    }
  }

  private void validarApellidoColaborador(String apellidoDeColaborador,
                                          int contadorLinea, List<String> errores) {
    if (apellidoDeColaborador.length() > 51 || apellidoDeColaborador.isBlank()) {
      errores.add("ERROR linea " + contadorLinea + ": Ingresar apellido de colaborador."
          + " Debe tener 50 caracteres como máximo");
    }
  }

  private void validarMailColaborador(String mailDeColaborador,
                                      int contadorLinea, List<String> errores) {
    if (mailDeColaborador.length() > 51 || mailDeColaborador.isBlank()) {
      errores.add("ERROR linea " + contadorLinea + ": Ingresar mail de colaborador."
          + " Debe tener 50 caracteres como máximo");
    }
  }

  private LocalDate validarFecha(String fechaString, int contadorLinea,
                                 String tipoFecha, List<String> errores) {
    if (fechaString.length() > 10 || fechaString.isBlank()) {
      errores.add("ERROR linea " + contadorLinea + ": Ingresar " + tipoFecha + ". "
          + "Debe tener 10 caracteres como máximo");
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    return LocalDate.parse(fechaString, formatter);
  }

  private void validarTipoColaboracion(String tipoColaboracion,
                                       int contadorLinea, List<String> errores) {
    if (!tipoColaboracion.equals("ENTREGA_TARJETAS") && !tipoColaboracion
        .equals("REDISTRIBUCION_VIANDAS") && !tipoColaboracion.equals("DONACION_VIANDAS")
        && !tipoColaboracion.equals("DINERO")) {
      errores.add("ERROR linea " + contadorLinea + ": El tipo de colaboracion"
          + " es incorrecto. Ingresar ENTREGA_TARJETAS o REDISTRIBUCION_VIANDAS"
          + " o DONACION_VIANDAS o DINERO");
    }
  }

  private Integer validarCantidadDeColaboraciones(String[] values, int index,
                                                  int contadorLinea, List<String> errores) {
    String cantidadDeColaboracionesString = "";
    try {
      cantidadDeColaboracionesString = values[index];
      if (cantidadDeColaboracionesString == null || cantidadDeColaboracionesString.length() > 7) {
        errores.add("ERROR linea "
            + contadorLinea + ": Validar que la cantidad de colaboraciones sean 7 caracteres"
            + " como máximo");
      } else {
        return Integer.parseInt(cantidadDeColaboracionesString);
      }
    } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
      errores.add("ERROR linea "
          + contadorLinea + ": Ingresar una cantidad de colaboraciones válida");
    }
    return null;

  }

  private Documento convertirTipoDocumento(String tipoDoc, List<String> errores) {
    switch (tipoDoc) {
      case "DNI" -> {
        return Documento.DNI;
      }
      case "LC" -> {
        return Documento.LIBRETA_CIVICA;
      }
      case "LE" -> {
        return Documento.LIBRETA_ENROLAMIENTO;
      }
      case "CEDULA" -> {
        return Documento.CEDULA;
      }
      default -> {
        errores.add("ERROR - Tipo de Documento inválido");
      }
    }
    return null;
  }

  private ColaboradorFisico obtenerColaborador(RepoColaboradores repoColaboradores,
                                               Documento documento, Integer valorDocumento,
                                               String nombre, String apellido,
                                               LocalDate fechaNacimiento, String mail, boolean conPersistencia) {
    ColaboradorFisico colaborador;

    if (conPersistencia) {
      colaborador = repoColaboradores.buscarColaboradorFisicoPorDocumento(valorDocumento);
    }
    else {
      colaborador = repoColaboradores
              .buscarColaboradorFisico(documento, valorDocumento);
    }
    System.out.println("colaborador = " + colaborador);
    if (colaborador == null) {
      colaborador = (ColaboradorFisico) repoColaboradores
          .registrarColaboradorFisico(nombre, apellido, fechaNacimiento);
      Credencial credencial = new Credencial(valorDocumento.toString(),
          "!" + nombre.substring(3) + "L@Sn10" + apellido.substring(2));
      colaborador.setCredencial(credencial);
      credencial.hashearContrasenia(credencial.getContrasenia());
    }
    colaborador.setTipoDocumento(documento);
    colaborador.setNumeroDocumento(valorDocumento);
    colaborador.agregarMedioContacto(new Mail(mail));
    return colaborador;
  }

  private List<Colaboracion> obtenerColaboraciones(ColaboradorFisico colaborador,
                                                   String tipoColaboracion,
                                                   LocalDate fechaColaboracion,
                                                   LocalDate fechaNacimiento,
                                                   int cantidadDeColaboraciones) {
    List<Colaboracion> colaboraciones = new ArrayList<>();
    for (int j = 0; j < cantidadDeColaboraciones; j++) {
      Colaboracion colaboracion = this.crearColaboracion(colaborador, tipoColaboracion,
          fechaColaboracion, fechaNacimiento);
      colaboraciones.add(colaboracion);
    }
    return colaboraciones;
  }

  public Colaboracion crearColaboracion(ColaboradorFisico colaborador,
                                        String tipoColaboracion,
                                        LocalDate fechaColaboracion, LocalDate fechaNacimiento) {
    Colaboracion colaboracion;
    colaboracion = switch (tipoColaboracion) {
      case "REDISTRIBUCION_VIANDAS" -> new DistribucionVianda(colaborador,
          null, null, null, null);
      case "DONACION_VIANDAS" -> new DonacionVianda(colaborador, null);
      case "DINERO" -> new DonacionDinero(colaborador, null, null);
      case "ENTREGA_TARJETAS" ->
          new RegistracionPersonaVulnerable(colaborador, new TarjetaPersonaVulnerable(null), null);
      default -> throw new ImportadorColaboracionExeception("Tipo de colaboración no válido");
    };
    colaboracion.setFechaColaboracion(fechaColaboracion);
    colaborador.setFechaNacimiento(fechaNacimiento);
    return colaboracion;
  }
}
