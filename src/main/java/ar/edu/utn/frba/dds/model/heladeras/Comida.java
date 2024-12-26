package ar.edu.utn.frba.dds.model.heladeras;

import ar.edu.utn.frba.dds.db.EntidadPersistente;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * clase comida para el molde de las comidas que se van a meter.
 */

@Entity
@Table(name = "comida")
public class Comida extends EntidadPersistente {

  @Column(name = "nombre_comida")
  private String nombre;

  public Comida(String nombre) {
    this.nombre = nombre;
  }

  public Comida() {}

  public String getNombre() {
    return nombre;
  }
}
