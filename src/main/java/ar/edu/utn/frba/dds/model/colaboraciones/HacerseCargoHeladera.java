package ar.edu.utn.frba.dds.model.colaboraciones;

import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.model.heladeras.Heladera;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "hacerse_cargo_heladera")
public class HacerseCargoHeladera extends Colaboracion {
  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "hacerse_cargo_id", referencedColumnName = "id")
  private Heladera heladeraCargo;

  public HacerseCargoHeladera(Colaborador colaborador, Heladera heladeraCargo) {
    super(colaborador);
    this.heladeraCargo = heladeraCargo;
    this.fechaColaboracion = LocalDate.now();
  }

  public HacerseCargoHeladera() {
  }

  @Override
  public boolean puedeSerRealizadaPorFisico() {
    return false;
  }

  @Override
  public boolean puedeSerRealizadaPorJuridico() {
    return true;
  }
}
