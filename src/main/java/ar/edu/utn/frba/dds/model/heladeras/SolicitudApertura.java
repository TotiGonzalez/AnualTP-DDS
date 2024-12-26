package ar.edu.utn.frba.dds.model.heladeras;

import ar.edu.utn.frba.dds.db.EntidadPersistente;
import ar.edu.utn.frba.dds.db.convertidores.ConvertidorLocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitud_apertura")
public class SolicitudApertura extends EntidadPersistente {
  private Long idApertura;
  @Column(name = "numero_tarjeta")
  private String numeroTarjeta;

  @Column(name = "fecha_solicitud")
  @Convert(converter = ConvertidorLocalDate.class)
  private LocalDateTime fechaSolicitud;

  private boolean completada;

  public SolicitudApertura(Long id, String numeroTarjeta, LocalDateTime fechaSolicitud) {
    this.idApertura = id;
    this.numeroTarjeta = numeroTarjeta;
    this.fechaSolicitud = fechaSolicitud;
    this.completada = false;
  }

  public SolicitudApertura() {

  }

  public Long getIdApertura() {
    return idApertura;
  }

  public String getNumeroTarjeta() {
    return numeroTarjeta;
  }

  public LocalDateTime getFechaSolicitud() {
    return fechaSolicitud;
  }

  public boolean isCompletada() {
    return completada;
  }

  public void setCompletada(boolean completada) {
    this.completada = completada;
  }
}
