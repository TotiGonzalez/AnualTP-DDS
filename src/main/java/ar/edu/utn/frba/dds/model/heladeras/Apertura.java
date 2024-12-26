package ar.edu.utn.frba.dds.model.heladeras;

import ar.edu.utn.frba.dds.db.convertidores.ConvertidorLocalDateTime;
import ar.edu.utn.frba.dds.db.EntidadPersistente;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class Apertura extends EntidadPersistente {

  @Column(name = "numero_tarjeta")
  private String numeroTarjeta;

  @Convert(converter = ConvertidorLocalDateTime.class)
  @Column(name = "fecha_apertura")
  private LocalDateTime fechaApertura;

  @Column(name = "heladeraId_id")
  private String heladeraId;

  public Apertura(String numeroTarjeta, LocalDateTime fechaApertura, String heladeraId) {
    this.numeroTarjeta = numeroTarjeta;
    this.fechaApertura = fechaApertura;
    this.heladeraId = heladeraId;
  }

  public Apertura() {}

  public String getNumeroTarjeta() {
    return numeroTarjeta;
  }

  public LocalDateTime getFechaApertura() {
    return fechaApertura;
  }

  public String getHeladeraId() {
    return heladeraId;
  }
}
