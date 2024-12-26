package ar.edu.utn.frba.dds.model.reporteinterno;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.db.EntidadPersistente;
import ar.edu.utn.frba.dds.model.heladeras.MapaHeladeras;

@Entity
public class LineaReporte extends EntidadPersistente {

  @ManyToOne
  @JoinColumn(name = "colaborador_id", referencedColumnName = "id")
  private Colaborador colaborador;
  @Column
  private Double puntaje;

  public LineaReporte(Colaborador colaborador) {
    this.colaborador = colaborador;
  }

  public LineaReporte() {}

  public void asignarPuntaje() {
    // Calculo del puntaje por persona
    MapaHeladeras mapaHeladeras = new MapaHeladeras();
    this.puntaje = colaborador.getPuntajeColaborador() + mapaHeladeras.getPuntajeHeladera();
  }

  public Double getPuntaje() {
    return puntaje;
  }
}
