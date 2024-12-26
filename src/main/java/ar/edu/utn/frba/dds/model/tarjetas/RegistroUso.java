package ar.edu.utn.frba.dds.model.tarjetas;

import ar.edu.utn.frba.dds.db.EntidadPersistente;
import ar.edu.utn.frba.dds.db.convertidores.ConvertidorLocalDateTime;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "registro_uso")
public class RegistroUso extends EntidadPersistente {
  @Column(name = "nombre_heladera")
  private String nombreHeladera;

  @Enumerated(EnumType.STRING)
  private MotivoUso motivoUso;

  @Column(name = "fecha_hora")
  @Convert(converter = ConvertidorLocalDateTime.class)
  private LocalDateTime fechaHora;

  public RegistroUso(String nombreHeladera, MotivoUso motivoUso) {
    this.nombreHeladera = nombreHeladera;
    this.motivoUso = motivoUso;
    this.fechaHora = LocalDateTime.now();
  }

  public RegistroUso() {

  }

  public LocalDateTime getFechaHora() {
    return fechaHora;
  }

  public String getNombreHeladera() {
    return nombreHeladera;
  }

  public MotivoUso getMotivoUso() {
    return motivoUso;
  }
}
