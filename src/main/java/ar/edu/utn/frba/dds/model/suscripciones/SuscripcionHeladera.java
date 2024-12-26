package ar.edu.utn.frba.dds.model.suscripciones;

import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.db.EntidadPersistente;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "suscripcion_heladera")
public class SuscripcionHeladera extends EntidadPersistente {
  @OneToMany
  @JoinColumn(name = "suscripcion_heladera_id")
  private List<Colaborador> colaboradores;

  public SuscripcionHeladera() {
    this.colaboradores = new ArrayList<>();
  }

  public void suscribirColaborador(Colaborador colaborador) {
    this.colaboradores.add(colaborador);
  }

  public void notificarSuscriptores(String mensaje){
    this.colaboradores.forEach(c -> this.notificarSuscriptor(c,mensaje));
  }

  public void notificarSuscriptor(Colaborador colaborador, String mensaje){
    colaborador.getMediosContacto().forEach(medioContacto -> {
      medioContacto.contactar(mensaje);
    });
  }

  public void recomendarHeladera(RecomendacionHeladeras recomendacionHeladeras){
      this.colaboradores.forEach(recomendacionHeladeras::recomendar);
  }
}
