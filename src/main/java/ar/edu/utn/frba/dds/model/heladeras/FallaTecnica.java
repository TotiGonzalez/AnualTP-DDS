package ar.edu.utn.frba.dds.model.heladeras;

import ar.edu.utn.frba.dds.model.colaboradores.ColaboradorFisico;
import ar.edu.utn.frba.dds.db.EntidadPersistente;
import ar.edu.utn.frba.dds.db.convertidores.ConvertidorLocalDateTime;
import ar.edu.utn.frba.dds.model.tecnico.Tecnico;
import ar.edu.utn.frba.dds.model.tecnico.TecnicoObserver;
import ar.edu.utn.frba.dds.model.tecnico.VisitaTecnico;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "falla_tecnica")
public class FallaTecnica extends EntidadPersistente implements FallaTecnicaObservable {

  @ManyToOne
  @JoinColumn(name = "heladera_involucrada_id", referencedColumnName = "id")
  public Heladera heladeraInvolucrada;

  @OneToMany
  @JoinColumn(name = "falla_tecnica_id", referencedColumnName = "id")
  private List<Tecnico> tecnicosDisponibles;

  @ManyToOne
  @JoinColumn(name = "colaborador_fisico_id", referencedColumnName = "id")
  ColaboradorFisico colaboradorFisico;

  @Column(name = "incidente_solucionado")
  private boolean solucionada = false;

  @Column(name = "incidente_grave")
  private boolean grave;

  String email;
  String titulo;
  String descripcion;
  String pathFotos;

  @OneToMany
  @JoinColumn(name = "falla_tecnica_id", referencedColumnName = "id")
  private List<VisitaTecnico> visitas;

  @Convert(converter = ConvertidorLocalDateTime.class)
  @Column(name = "fecha_y_hora")
  LocalDateTime fechaYhora;

  public ColaboradorFisico getColaboradorFisico() {
    return colaboradorFisico;
  }

  public void setColaboradorFisico(ColaboradorFisico colaboradorFisico) {
    this.colaboradorFisico = colaboradorFisico;
  }

  public Heladera getHeladeraInvolucrada() {
    return heladeraInvolucrada;
  }

  public void setHeladeraInvolucrada(Heladera heladeraInvolucrada) {
    this.heladeraInvolucrada = heladeraInvolucrada;
  }

  public FallaTecnica(Heladera heladeraInvolucrada,
                      LocalDateTime fechaYhora, ColaboradorFisico colaboradorFisico,
                      String descripcion, String titulo, String pathFotos) {
    this.heladeraInvolucrada = heladeraInvolucrada;
    this.fechaYhora = fechaYhora;
    this.colaboradorFisico = colaboradorFisico;
    this.descripcion = descripcion;
    this.titulo = titulo;
    this.pathFotos = pathFotos;
    this.visitas = new ArrayList<>();
    this.tecnicosDisponibles = new ArrayList<>();
  }

  public FallaTecnica() {

  }

  @Override
  public void addObserver(TecnicoObserver observer) {
    if (observer instanceof Tecnico) {
      tecnicosDisponibles.add((Tecnico) observer);
      System.out.println("Técnico " + ((Tecnico) observer).getNombre() + " añadido a la lista de técnicos disponibles.");
    } else {
      System.out.println("El observador no es un técnico.");
    }
  }

  @Override
  public void removeObserver(TecnicoObserver observer) {
    if (observer instanceof Tecnico) {
      tecnicosDisponibles.remove((Tecnico) observer);
    }
  }//arreglar observer y tecnico

  @Override
  public void notifyObservers() {
    System.out.println("Intentando notificar. Cantidad de técnicos disponibles: " + tecnicosDisponibles.size());
    if (tecnicosDisponibles.isEmpty()) {
      System.out.println("No hay técnicos para notificar.");
    } else {
      System.out.println("Notificando a los técnicos disponibles...");
      Tecnico tecnicoMasCercano = encontrarTecnicoMasCercano(heladeraInvolucrada,
          tecnicosDisponibles);
      if (tecnicoMasCercano != null) {
        tecnicoMasCercano.update(this);
        System.out.println("El tecnico que atenderá la falla es " + tecnicoMasCercano.getNombre());
      }
    }
  }//notificar automaticamente
  public String getDescripcion() {
    return descripcion;
  }
  public List<VisitaTecnico> getVisitas() {
    return visitas;
  }
  public String getPathFotos() {
    return pathFotos;
  }

  public void setPathFotos(String pathFotos) {
    this.pathFotos = pathFotos;
  }

  public String getTitulo() {
    return titulo;
  }

  public void reportar() {
    notifyObservers();
  }

  public boolean contieneTecnico(Tecnico tecnico) {
    return tecnicosDisponibles.contains(tecnico);
  }

  private Tecnico encontrarTecnicoMasCercano(Heladera heladera, List<Tecnico> tecnicosDisponibles) {
    return tecnicosDisponibles.stream()
        .min((t1, t2) -> Double.compare(
            calcularDistancia(heladera.getUbicacionHeladera(), t1.getUbicacionActual()),
            calcularDistancia(heladera.getUbicacionHeladera(), t2.getUbicacionActual())
        ))
        .orElseThrow(() -> new RuntimeException("No se encontró ningún técnico disponible."));
  }

  public double calcularDistancia(UbicacionPrecisa ubiHeladera, UbicacionPrecisa ubiActual) {
    double lat1 = ubiHeladera.getLatitud();
    double lon1 = ubiHeladera.getLongitud();
    double lat2 = ubiActual.getLatitud();
    double lon2 = ubiActual.getLongitud();

    final double R = 6378.14; // Radio de la tierra en km
    double latDistance = Math.toRadians(lat2 - lat1);
    double lonDistance = Math.toRadians(lon2 - lon1);
    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
        + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
        * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return R * c;
  }

  public void registrarVisita(VisitaTecnico visita) {
    visitas.add(visita);
  }

  public void establecerSolucionada() {
    this.solucionada = true;
  }

  public void establecerNoSolucionado() {
    this.solucionada = false;
  }
  public LocalDateTime getFechaYhora() {
    return fechaYhora;
  }

  public boolean getSolucionada() {
    return solucionada;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public void setGrave(boolean value) {
    this.grave = value;
  }

  public boolean isGrave() {
    return grave;
  }

  public void setFechaYhora(LocalDateTime fechaYhora) {
    this.fechaYhora = fechaYhora;
  }

    public LocalDateTime getFechaYHora() {
      return fechaYhora;
    }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }
}