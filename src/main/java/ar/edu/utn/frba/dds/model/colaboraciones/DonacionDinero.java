package ar.edu.utn.frba.dds.model.colaboraciones;

import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "donacion_dinero")
public class DonacionDinero extends Colaboracion {
  @Column(name = "monto_dinero")
  private Integer montoDinero;

  @Enumerated(EnumType.STRING)
  private Frecuencia frecuencia;

  public DonacionDinero(Colaborador colaborador, Integer montoDinero, Frecuencia frecuencia) {
    super(colaborador);
    this.montoDinero = montoDinero;
    this.frecuencia = frecuencia;
    this.fechaColaboracion = LocalDate.now();
  }

  public DonacionDinero() {

  }

  @Override
  public boolean puedeSerRealizadaPorFisico() {
    return true;
  }

  @Override
  public boolean puedeSerRealizadaPorJuridico() {
    return true;
  }

  /**
   * GETTTERS
   */

  public Integer getMontoDinero() {
    return montoDinero;
  }
}
