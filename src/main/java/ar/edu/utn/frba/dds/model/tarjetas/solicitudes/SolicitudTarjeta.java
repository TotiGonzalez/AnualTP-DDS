package ar.edu.utn.frba.dds.model.tarjetas.solicitudes;

import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Direccion;
import ar.edu.utn.frba.dds.db.EntidadPersistente;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "solicitud_tarjeta")
public class SolicitudTarjeta extends EntidadPersistente {

  @Column(name = "fecha_solicitud")
  private LocalDate fechaSolicitud;

  @ManyToOne
  @JoinColumn(name = "colaborador", referencedColumnName = "id")
  private Colaborador colaborador;

  @Embedded
  private Direccion direccion;

  @Column
  private int cantidad;

  @Enumerated(EnumType.STRING)
  private TipoSolicitudTarjeta tipoSolicitudTarjeta;

  public SolicitudTarjeta(Colaborador colaborador, Direccion direccion) {
    this.tipoSolicitudTarjeta = TipoSolicitudTarjeta.COLABORADOR;
    this.fechaSolicitud = LocalDate.now();
    this.colaborador = colaborador;
    this.direccion = direccion;
  }

  public SolicitudTarjeta(Colaborador colaborador, Direccion direccion, int cantidad) {
    this.tipoSolicitudTarjeta = TipoSolicitudTarjeta.PERSONA_VULNERABLE;
    this.fechaSolicitud = LocalDate.now();
    this.colaborador = colaborador;
    this.direccion = direccion;
    this.cantidad = cantidad;
  }

  public SolicitudTarjeta() {
  }

  public LocalDate getFechaSolicitud() {
    return fechaSolicitud;
  }

  public Colaborador getColaborador() {
    return colaborador;
  }

  public Direccion getDireccion() {
    return direccion;
  }

  public TipoSolicitudTarjeta getTipoSolicitudTarjeta() {
    return tipoSolicitudTarjeta;
  }

  public int getCantidad() {
    return cantidad;
  }
}
