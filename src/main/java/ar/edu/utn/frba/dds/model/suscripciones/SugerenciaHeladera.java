package ar.edu.utn.frba.dds.model.suscripciones;

import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.db.EntidadPersistente;
import ar.edu.utn.frba.dds.model.heladeras.Heladera;

import javax.persistence.*;

@Entity
@Table(name = "sugerencia_heladera")
public class SugerenciaHeladera extends EntidadPersistente {
  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "heladera_id", referencedColumnName = "id")
  private Heladera heladera;

  private boolean aceptado;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "colaborador", referencedColumnName = "id")
  private Colaborador colaborador;

  public SugerenciaHeladera() {

  }

  public void aceptarSugerencia(){
    this.aceptado = true;
  }

  public SugerenciaHeladera(Heladera heladera) {
    this.heladera = heladera;
    this.aceptado = false;
  }

  public void setColaborador(Colaborador colaborador) {
    this.colaborador = colaborador;
  }
}
