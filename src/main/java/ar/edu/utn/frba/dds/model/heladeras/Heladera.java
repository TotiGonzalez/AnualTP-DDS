package ar.edu.utn.frba.dds.model.heladeras;

import static java.lang.Math.abs;

import ar.edu.utn.frba.dds.db.convertidores.ConvertidorLocalDate;
import ar.edu.utn.frba.dds.db.EntidadPersistente;
import ar.edu.utn.frba.dds.model.reportes.MovimientoVianda;
import ar.edu.utn.frba.dds.model.sensores.ActualizarCantidadViandasAction;
import ar.edu.utn.frba.dds.model.sensores.ActualizarUltimaLecturaTemperatura;
import ar.edu.utn.frba.dds.model.sensores.SensorDeViandas;
import ar.edu.utn.frba.dds.model.sensores.SensorPeso;
import ar.edu.utn.frba.dds.model.sensores.SensorTemperatura;
import ar.edu.utn.frba.dds.model.suscripciones.RecomendacionHeladeras;
import ar.edu.utn.frba.dds.model.suscripciones.SuscripcionHeladera;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.lang.Math;

//Receiver : knows how to perform the operations associated with carrying out a request
@Entity
@Table(name = "heladeras")
public class Heladera extends EntidadPersistente {

  @Embedded
  private UbicacionPrecisa ubicacionPrecisa;

  @Column(name = "nombre_heladera")
  private String nombreHeladera;

  @Convert(converter = ConvertidorLocalDate.class)
  @Column(name = "fecha_inicio")
  private LocalDate fechaInicio;

  @Enumerated(EnumType.STRING)
  private EstadoHeladera estadoHeladera;

  @Column(name = "capacidad_total")
  private double capacidadTotal;

  @Column(name = "peso_actual")
  private double pesoActual;

  @Column(name = "cant_usos")
  private Integer cantUsos;

  @Column(name = "numero_serie")
  private String numeroSerie;


  @Column(name = "temperatura_actual")
  private double temperaturaActual;

  @Column(name = "cantidad_viandas")
  private Integer cantidadViandas = 0;

  @OneToMany(mappedBy = "heladera", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
  private List<Vianda> viandas;

  @Embedded
  private ConfiguracionHeladera configuracionHeladera;

  @OneToMany
  @JoinColumn(name = "heladera_id",  referencedColumnName = "id")
  private List<Apertura> aperturas;

  @ElementCollection
  private List<Double> ultimasTresTemperaturas;

  @OneToMany
  @JoinColumn(name = "heladera_id",  referencedColumnName = "id")
  private List<SolicitudApertura> solicitudesApertura;

  @OneToMany
  @JoinColumn(name = "heladera_id",  referencedColumnName = "id")
  private List<MovimientoVianda> registroMovimientos = new ArrayList<>();

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "heladera_id", referencedColumnName = "id")
  private SuscripcionHeladera suscripciones = new SuscripcionHeladera();

  @OneToMany(mappedBy = "heladeraInvolucrada", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
  private List<FallaTecnica> fallaTecnicas;

  @OneToMany
  @JoinColumn(name = "heladera_id",  referencedColumnName = "id")
  private List<AlertaHeladera> alertasHeladera;

  public void setSuscripciones(SuscripcionHeladera suscripciones) {
    this.suscripciones = suscripciones;
  }

  public Heladera(String nombreHeladera, UbicacionPrecisa ubicacionPrecisa,
                  EstadoHeladera estadoHeladera, double capacidadTotal,
                  ConfiguracionHeladera configuracionHeladera) {
    this.nombreHeladera = nombreHeladera;
    this.ubicacionPrecisa = ubicacionPrecisa;
    this.estadoHeladera = estadoHeladera;
    this.viandas = new ArrayList<>();
    this.ultimasTresTemperaturas = new ArrayList<>();
    this.capacidadTotal = capacidadTotal;
    this.configuracionHeladera = configuracionHeladera;
    this.temperaturaActual = configuracionHeladera.getTemperaturaMaxima();
    this.cantUsos = 0;
    this.numeroSerie = String.valueOf(ThreadLocalRandom.current().nextInt(1000));
    this.solicitudesApertura = new ArrayList<>();
    this.aperturas = new ArrayList<>();
    fallaTecnicas = new ArrayList<>();
  }

  public Heladera() {}

  public void agregarViandas(List<Vianda> viandas) {
    this.viandas.addAll(viandas);
  }

  public void agregarMovimientoVianda(MovimientoVianda movimiento) {
    this.registroMovimientos.add(movimiento);
  }

  public void ponerEnFuncionamiento() {
    this.estadoHeladera = EstadoHeladera.ACTIVA;
    this.fechaInicio = LocalDate.now();
    this.fallaTecnicas.forEach(FallaTecnica::establecerSolucionada);
  }
  public FallaTecnica getfallaTecnica(Integer index){
    return this.fallaTecnicas.get(index);
  }
  public void detenerFuncionamiento() {
    this.estadoHeladera = EstadoHeladera.INACTIVA;
  }

  public boolean estaActiva() {
    return this.getEstadoHeladera().equals(EstadoHeladera.ACTIVA);
  }

  public Integer getCantidadMesesActiva() {
    return Math.toIntExact(ChronoUnit.MONTHS.between(this.getFechaInicio(), LocalDate.now()));
  }

  public boolean necesitaAtencion() {
    double minTemperature = configuracionHeladera.getTemperaturaMinima();
    return ultimasTresTemperaturas.size() == 3
        && ultimasTresTemperaturas.stream().allMatch(temp -> temp < minTemperature);
  }

  public void accionAnteCambioTemperatura(SensorTemperatura sensorTemperatura) {
    sensorTemperatura.onTemperatureChange(new ActualizarUltimaLecturaTemperatura(this));
    if (configuracionHeladera.getTemperaturaMaxima() < getTemperaturaActual() ||
        configuracionHeladera.getTemperaturaMinima()> getTemperaturaActual() ){
        this.alertarFalloHeladera(TipoIncidente.INCIDENTE_TEMPERATURA);
    }
  }

  public void actualizarUltimasTresTemperaturas(double temperatura) {
    if (ultimasTresTemperaturas.size() == 3) {
      ultimasTresTemperaturas.remove(0);
    }
    ultimasTresTemperaturas.add(temperatura);
  }

  public NivelLlenado getNivelLlenado() {
    double pesoActual = getPesoActual();
    double nivel = pesoActual * 100 / capacidadTotal;
    if (nivel <= 0.3) {
      return NivelLlenado.BAJO;
    } else if (nivel <= 0.7) {
      return NivelLlenado.MEDIO;
    } else {
      return NivelLlenado.ALTO;
    }
  }

  public Integer getCantidadViandas() {
    return this.cantidadViandas;
  }

  public void agregarFallaTecnica(FallaTecnica fallaTecnica) {
    this.fallaTecnicas.add(fallaTecnica);
  }

  public void actualizarPesoActual(SensorPeso sensorPeso) throws Exception {
    this.pesoActual = sensorPeso.obtenerPesoActual(this.getNumeroSerie());
  }
  public void setPesoActual(double pesoActual) {
    this.pesoActual = pesoActual;
  }

  public void setTemperaturaActual(double temperaturaActual) {
    this.temperaturaActual = temperaturaActual;
  }

  public void setEstadoHeladera(EstadoHeladera estadoHeladera) {
    this.estadoHeladera = estadoHeladera;
  }

  public double getCapacidadTotal() {
    return capacidadTotal;
  }

  public UbicacionPrecisa getUbicacionHeladera() {
    return ubicacionPrecisa;
  }

  public String getNombreHeladera() {
    return nombreHeladera;
  }

  public void setNombreHeladera(String nombreHeladera) {
    this.nombreHeladera = nombreHeladera;
  }

  public double getPesoActual() {
    return pesoActual;
  }

  public Integer getCantUsos() {
    return cantUsos;
  }

  public List<FallaTecnica> getFallaTecnicas() {
    return fallaTecnicas;
  }

  public EstadoHeladera getEstadoHeladera() {
    return estadoHeladera;
  }

  public LocalDate getFechaInicio() {
    return fechaInicio;
  }

  public String getNumeroSerie() {
    return numeroSerie;
  }

  public double getTemperaturaActual() {
    return temperaturaActual;
  }

  public List<MovimientoVianda> getMovimientos() {
    return registroMovimientos;
  }

  public SuscripcionHeladera getSuscripciones(){
    return suscripciones;
  }

  public String getCiudad() {
    return this.ubicacionPrecisa.getDireccion().getCiudad();
  }

  public String getLocalidad() {
    return this.ubicacionPrecisa.getDireccion().getLocalidad();
  }

  public String getCiudadYLocalidad() {
    return this.getCiudad() + ", " + this.getLocalidad();
  }

  public String getDireccion() {
    String calle = this.ubicacionPrecisa.getDireccion().getCalle();
    String altura = this.ubicacionPrecisa.getDireccion().getAltura().toString();

    return calle + " " + altura;
  }


  public void registrarSolicitudApertura(SolicitudApertura solicitud) {
    this.solicitudesApertura.add(solicitud);
  }

  public void registrarApertura(Apertura apertura) {
    this.aperturas.add(apertura);
  }

  public int getTiempoExpiracionSolicitudes() {
    return configuracionHeladera.getTiempoExpiracionSolicitudes();
  }

  public void accionAnteCambioCantidadViandas(SensorDeViandas sensorDeViandas) {
    sensorDeViandas.setLunchBoxAction(new ActualizarCantidadViandasAction(this));
  }

  public void faltanViandas(int cantidad){
    if(this.viandas.size() <= cantidad){
      this.suscripciones.notificarSuscriptores("Faltan viandas");
    }
  }

  public void incidente(RecomendacionHeladeras recomendacionHeladeras){
      this.suscripciones.notificarSuscriptores("Hubo un incidente");
      this.suscripciones.recomendarHeladera(recomendacionHeladeras);
  }

  public void alertarFalloHeladera(TipoIncidente tipoincidente){
    alertasHeladera.add(new AlertaHeladera(LocalDateTime.now(),tipoincidente));
    setEstadoHeladera(EstadoHeladera.INACTIVA);
    this.suscripciones.notificarSuscriptores("Hubo un incidente de tipo:" + tipoincidente);
  }

}