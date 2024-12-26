package ar.edu.utn.frba.dds.model.heladeras;

import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.db.convertidores.ConvertidorLocalDate;
import ar.edu.utn.frba.dds.db.EntidadPersistente;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * viandas se relacionan con la heladera y el contribuidor.
 */
@Entity
@Table(name = "viandas")
public class Vianda extends EntidadPersistente {

  //una vianda tiene 1 sola comida
  @ManyToOne
  @JoinColumn(name = "comida_id", referencedColumnName = "id")
  private Comida comida;

  @ManyToOne
  @JoinColumn(name = "heladera_id", referencedColumnName = "id")
  private Heladera heladera;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "colaborador", referencedColumnName = "id")
  private Colaborador colaborador;

  @Enumerated(EnumType.STRING)
  private EstadoVianda estadoVianda;

  @Convert(converter = ConvertidorLocalDate.class)
  @Column(name = "fecha_caducidad")
  private LocalDate fechaCaducidad;

  @Convert(converter = ConvertidorLocalDate.class)
  @Column(name = "fecha_donacion")
  private LocalDate fechaDonacion;

  @Column(name = "calorias")
  private Integer calorias;

  @Column(name = "peso")
  private Integer peso;

  public Vianda(Comida comida, LocalDate fechaCaducidad,
                Integer calorias, Integer peso) {
    this.comida = comida;
    this.fechaCaducidad = fechaCaducidad;
    this.calorias = calorias;
    this.peso = peso;
    this.estadoVianda = EstadoVianda.NO_ENTREGADA;
  }

  public Vianda() {}

  public Comida getComida() {
    return comida;
  }

  public LocalDate getFechaCaducidad() {
    return fechaCaducidad;
  }

  public LocalDate getFechaDonacion() {
    return fechaDonacion;
  }

  public Integer getCalorias() {
    return calorias;
  }

  public Integer getPeso() {
    return peso;
  }

  public EstadoVianda getEstadoVianda() {
    return estadoVianda;
  }

  public Integer getCantidadSemanasfresca() {
    return Math.toIntExact(ChronoUnit.WEEKS.between(fechaDonacion, fechaCaducidad));
  }

  public Heladera getHeladera() {
    return this.heladera;
  }

  public String nombreHeladera() {
    return this.heladera.getNombreHeladera();
  }

  public void setColaborador(Colaborador colaborador) {
    this.colaborador = colaborador;
  }

  public String getColaborador() {
    return colaborador.getNombreUsuario();
  }

  public void setHeladera(Heladera heladera) {
    this.heladera = heladera;
  }

  public void setFechaDonacion(LocalDate fechaDonacion) {
    this.fechaDonacion = fechaDonacion;
  }

  public void fueEntregada() {
    this.estadoVianda = EstadoVianda.ENTREGADA;
  }
}