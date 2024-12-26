package ar.edu.utn.frba.dds.model.colaboraciones;

import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.model.colaboradores.PersonaVulnerable;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Direccion;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Documento;
import ar.edu.utn.frba.dds.model.excepciones.VariableNullException;
import ar.edu.utn.frba.dds.model.tarjetas.TarjetaPersonaVulnerable;

import javax.persistence.Embedded;
import java.time.LocalDate;

public class RegistracionPersonaVulnerableBuilder {
  //Datos de la Persona
  private String nombre;
  private LocalDate fechaNacimiento;
  private boolean situacionDeCalle = true;

  @Embedded
  private Direccion direccion;
  private Documento tipoDocumento;
  private Integer numeroDocumento;
  private boolean tieneMenoresBajoTutela = false;
  private Integer cantMenoresBajoTutela;
  //Colaborador
  private final Colaborador responsableInscripcion;
  //Tajeta
  private TarjetaPersonaVulnerable tarjetaPersonaVulnerable;

  private RegistracionPersonaVulnerableBuilder(Colaborador responsableInscripcion) {
    this.responsableInscripcion = responsableInscripcion;
  }

  public static RegistracionPersonaVulnerableBuilder crear(Colaborador responsableInscripcion) {
    return new RegistracionPersonaVulnerableBuilder(responsableInscripcion);
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public void setFechaNacimiento(LocalDate fechaNacimiento) {
    this.fechaNacimiento = fechaNacimiento;
  }

  public void setSituacionDeCalle(boolean situacionDeCalle) {
    this.situacionDeCalle = situacionDeCalle;
  }

  public void setDireccion(Direccion direccion) {
    if (situacionDeCalle) {
      throw new RuntimeException("Previamente debe declarar que la persona no está en situación "
          + "de calle");
    } else {
      this.direccion = direccion;
    }
  }

  public void setTipoDocumento(Documento tipoDocumento) {
    this.tipoDocumento = tipoDocumento;
  }

  public void setNumeroDocumento(Integer numeroDocumento) {
    if (tipoDocumento == null) {
      throw new RuntimeException("Previamente debe declarar que el tipo de documento ");
    } else {
      this.numeroDocumento = numeroDocumento;
    }
  }

  public void setTieneMenoresBajoTutela(boolean tieneMenoresBajoTutela) {
    this.tieneMenoresBajoTutela = tieneMenoresBajoTutela;
  }

  public void setCantMenoresBajoTutela(Integer cantMenores) {
    if (!tieneMenoresBajoTutela) {
      throw new RuntimeException("Previamente debe declarar que esta persona tiene menores bajo"
          + "su tutela");
    } else if (cantMenores < 1) {
      throw new RuntimeException("La cantidad debe ser 1 o mayor");
    } else {
      this.cantMenoresBajoTutela = cantMenores;
    }
  }

  public void setPrimeraTarjetaDisponible() {
    this.tarjetaPersonaVulnerable = responsableInscripcion.getPrimeraTarjetaDeLista();
  }


  public void setTarjeta(TarjetaPersonaVulnerable tarjeta) {
    if (this.responsableInscripcion.tieneTarjeta(tarjeta)) {
      this.tarjetaPersonaVulnerable = tarjeta;
    } else {
      throw new RuntimeException("La tarjeta debe estar en posesión del colaborador");
    }
  }

  public RegistracionPersonaVulnerable generarLaRegistracion() {

    String caracteristicasNecesariasError = "Para generar la Persona Vulnerable es Necesario"
        + " que especifique: Nombre, Fecha de Nacimiento, Situacion de Calle, Tipo de Documento,"
        + " Numero de Documento y si Tiene Menores Bajo su Tutela";

    if (this.nombre == null) {
      throw new VariableNullException(
          "No especificó el Nombre. " + caracteristicasNecesariasError);
    }
    if (this.fechaNacimiento == null) {
      throw new VariableNullException(
          "No especificó la Fecha de Nacimiento. " + caracteristicasNecesariasError);
    }
    if (this.tipoDocumento == null) {
      throw new VariableNullException(
        "No especificó el Tipo de Documento. " + caracteristicasNecesariasError);
    }
    if (this.numeroDocumento == null) {
      throw new VariableNullException(
        "No especificó el Numero de Documento. " + caracteristicasNecesariasError);
    }
    if (!situacionDeCalle && this.direccion == null) {
      throw new RuntimeException(
          "Para una Persona Vulnerable que no está en situación de calle se debe especificar "
              + "una dirección.");
    }
    if (tieneMenoresBajoTutela && cantMenoresBajoTutela == null) {
      throw new RuntimeException(
          "Para una Persona Vulnerable que tiene hijos debe especificar la cantidad que tiene");
    }

    PersonaVulnerable personaVulnerable = new PersonaVulnerable(nombre, fechaNacimiento,
        situacionDeCalle, direccion, tipoDocumento, numeroDocumento, tieneMenoresBajoTutela,
        cantMenoresBajoTutela, responsableInscripcion);

    this.tarjetaPersonaVulnerable.setTitularTarjeta(personaVulnerable);

    this.responsableInscripcion.removerTarjeta(tarjetaPersonaVulnerable);

    personaVulnerable.setTarjeta(tarjetaPersonaVulnerable);

    return new RegistracionPersonaVulnerable(
        responsableInscripcion, tarjetaPersonaVulnerable, personaVulnerable);
  }
}

