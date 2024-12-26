package ar.edu.utn.frba.dds.model.tecnico;

import ar.edu.utn.frba.dds.db.EntidadPersistente;
import ar.edu.utn.frba.dds.model.heladeras.FallaTecnica;
import ar.edu.utn.frba.dds.model.heladeras.Heladera;
import ar.edu.utn.frba.dds.model.heladeras.UbicacionPrecisa;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tecnico")
public class Tecnico extends EntidadPersistente implements TecnicoObserver {
  private String nombre;

  @Embedded
  @Column(name = "ubicacion_actual")
  private UbicacionPrecisa ubicacionActual;

  String descripcion;
  String foto;
  boolean solucionado;

  public Tecnico(String nombre, UbicacionPrecisa ubicacionActual,
                 String descripcion, String foto) {
    this.nombre = nombre;
    this.ubicacionActual = ubicacionActual;
    this.descripcion = descripcion;
    this.foto = foto;
  }

  public Tecnico() {

  }

  @Override
  public void update(FallaTecnica falla) {
      realizarVisita(falla.heladeraInvolucrada, falla);
  }

  public String getNombre() {
    return nombre;
  }

  public UbicacionPrecisa getUbicacionActual() {
    return ubicacionActual;
  }

  public void realizarVisita(Heladera heladeraInvolucrada, FallaTecnica falla) {
    LocalDateTime fechaVisita = LocalDateTime.now();
    VisitaTecnico visita = new VisitaTecnico(fechaVisita, heladeraInvolucrada, descripcion, foto, solucionado);
    falla.registrarVisita(visita);
    visita.isSolucionado();
  }

  public void recibirNotificacion(FallaTecnica fallaTecnica) {
    System.out.println("El técnico " + this.getNombre() + " ha sido notificado sobre la falla en la heladera " + fallaTecnica.getHeladeraInvolucrada().getNombreHeladera());
  }
}