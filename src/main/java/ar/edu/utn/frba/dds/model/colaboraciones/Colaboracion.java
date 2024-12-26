package ar.edu.utn.frba.dds.model.colaboraciones;

import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.db.EntidadPersistente;
import ar.edu.utn.frba.dds.db.convertidores.ConvertidorLocalDate;

import javax.persistence.*;
import java.time.LocalDate;
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Colaboracion extends EntidadPersistente {
  @ManyToOne
  @JoinColumn(name = "colaborador_id", referencedColumnName = "id")
  protected Colaborador colaborador;

  @Column(name = "fecha_colaboracion")
  @Convert(converter = ConvertidorLocalDate.class)
  protected LocalDate fechaColaboracion;

  public Colaboracion(Colaborador colaborador) {
    this.fechaColaboracion = LocalDate.now();
    this.colaborador = colaborador;
  }

  public Colaboracion() {

  }

  public abstract boolean puedeSerRealizadaPorFisico();

  public abstract boolean puedeSerRealizadaPorJuridico();

  /**
   * GETTERS Y SETTERS
   */

  public LocalDate getFechaColaboracion() {
    return fechaColaboracion;
  }

  public Colaborador getColaborador() {
    return colaborador;
  }

  public void setFechaColaboracion(LocalDate fechaColaboracion) {
    this.fechaColaboracion = fechaColaboracion;
  }
}