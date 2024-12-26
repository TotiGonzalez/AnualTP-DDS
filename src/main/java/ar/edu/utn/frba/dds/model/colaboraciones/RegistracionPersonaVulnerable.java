package ar.edu.utn.frba.dds.model.colaboraciones;

import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.model.colaboradores.PersonaVulnerable;
import ar.edu.utn.frba.dds.model.tarjetas.TarjetaPersonaVulnerable;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "registracion_persona_vulnerable")
public class RegistracionPersonaVulnerable extends Colaboracion {
  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "registracion_persona_vulnerable_id", referencedColumnName = "id")
  public TarjetaPersonaVulnerable tarjetaPersonaVulnerable;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "persona_vulnerable_id", referencedColumnName = "id")
  public PersonaVulnerable personaVulnerable;

  public RegistracionPersonaVulnerable(Colaborador colaborador,
                                       TarjetaPersonaVulnerable tarjetaPersonaVulnerable,
                                       PersonaVulnerable personaVulnerable) {
    super(colaborador);
    this.tarjetaPersonaVulnerable = tarjetaPersonaVulnerable;
    this.personaVulnerable = personaVulnerable;
    this.fechaColaboracion = LocalDate.now();
  }

  public RegistracionPersonaVulnerable() {

  }

  public PersonaVulnerable getPersonaVulnerable() {
    return personaVulnerable;
  }

  public TarjetaPersonaVulnerable getTarjeta() {
    return tarjetaPersonaVulnerable;
  }

  @Override
  public boolean puedeSerRealizadaPorFisico() {
    return true;
  }

  @Override
  public boolean puedeSerRealizadaPorJuridico() {
    return false;
  }
}
