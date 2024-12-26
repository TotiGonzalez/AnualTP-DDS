
package ar.edu.utn.frba.dds.model.colaboradores;

import ar.edu.utn.frba.dds.model.colaboraciones.Colaboracion;
import ar.edu.utn.frba.dds.model.colaboraciones.DistribucionVianda;
import ar.edu.utn.frba.dds.model.colaboraciones.DonacionDinero;
import ar.edu.utn.frba.dds.model.colaboraciones.DonacionVianda;
import ar.edu.utn.frba.dds.model.colaboraciones.Frecuencia;
import ar.edu.utn.frba.dds.model.colaboraciones.HacerseCargoHeladera;
import ar.edu.utn.frba.dds.model.colaboraciones.MotivoDistribuicion;
import ar.edu.utn.frba.dds.model.colaboraciones.RegistracionPersonaVulnerable;
import ar.edu.utn.frba.dds.model.colaboraciones.RegistracionPersonaVulnerableBuilder;
import ar.edu.utn.frba.dds.model.colaboradores.medioscontacto.MedioContacto;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Direccion;
import ar.edu.utn.frba.dds.db.EntidadPersistente;
import ar.edu.utn.frba.dds.model.excepciones.ValidarPermisoColaboracionException;
import ar.edu.utn.frba.dds.model.excepciones.ValidarPermisoUsoTarjetasException;
import ar.edu.utn.frba.dds.model.heladeras.Apertura;
import ar.edu.utn.frba.dds.model.heladeras.Heladera;
import ar.edu.utn.frba.dds.model.heladeras.SolicitudApertura;
import ar.edu.utn.frba.dds.model.heladeras.SolicitudAperturaService;
import ar.edu.utn.frba.dds.model.heladeras.Vianda;
import ar.edu.utn.frba.dds.model.tarjetas.TarjetaColaborador;
import ar.edu.utn.frba.dds.model.tarjetas.TarjetaPersonaVulnerable;
import ar.edu.utn.frba.dds.model.tarjetas.solicitudes.RepoSolicitudesTarjetas;
import ar.edu.utn.frba.dds.model.tarjetas.solicitudes.SolicitudTarjeta;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "colaboradores")
public abstract class Colaborador extends EntidadPersistente {

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "colaborador_titular", referencedColumnName = "id")
  protected List<MedioContacto> mediosContacto;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "credencial", referencedColumnName = "id")
  protected Credencial credencial;

  @OneToOne(mappedBy = "titularTarjeta")
  protected TarjetaColaborador tarjetaPersonal;

  @Embedded
  protected Direccion direccion;

  @OneToMany(mappedBy = "colaborador", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
  protected List<Colaboracion> colaboraciones = new ArrayList<>();

  @OneToMany
  @JoinColumn(name = "colaborador", referencedColumnName = "id")
  protected List<SolicitudApertura> solicitudesApertura = new ArrayList<>();

  @OneToMany
  @JoinColumn(name = "colaborador", referencedColumnName = "id")
  protected List<Apertura> aperturas = new ArrayList<>();

  @OneToMany
  @JoinColumn(name = "colaborador", referencedColumnName = "id")
  protected List<TarjetaPersonaVulnerable> tarjetasPersonaVulnerables = new ArrayList<>();

  @OneToMany(mappedBy = "colaborador")
  protected List<SolicitudTarjeta> historialSolicitudesTarjetas = new ArrayList<>();

  @Column(name= "es_admin")
  protected boolean esAdmin = false;

  public String getNombreUsuario() {
    return credencial.getUsuario();
  }

  public void agregarMedioContacto(MedioContacto medioContacto) {
    if (this.mediosContacto == null) {
      this.mediosContacto = new ArrayList<>();
    }
    this.mediosContacto.add(medioContacto);
  }

  //Metodos para realizar las colaboraciones
  public void donarDinero(Integer montoDinero, Frecuencia frecuencia) {
    Colaboracion colaboracion = new DonacionDinero(this, montoDinero, frecuencia);
    agregarColaboracionSiTienePermiso(colaboracion);
  }

  public void donarViandas(LocalDate fecha,
                           List<Vianda> viandas,
                           Heladera heladera,
                           SolicitudAperturaService solicitudAperturaService) {
    solicitudAperturaService.registrarSolicitud(this, heladera);
    registrarHeladeraColaboradorEnVianda(viandas, this, heladera);
    Colaboracion colaboracion = new DonacionVianda(this, viandas);
    agregarColaboracionSiTienePermiso(colaboracion);
    solicitudAperturaService.registrarApertura(this, heladera);
  }

  private void registrarHeladeraColaboradorEnVianda(List<Vianda> viandas,
                                                    Colaborador colaborador, Heladera heladera) {
    viandas.forEach(vianda -> {
      vianda.setColaborador(colaborador);
      vianda.setHeladera(heladera);
      vianda.setFechaDonacion(LocalDate.now());
    });
  }

  public void distribuirVianda(Heladera heladeraOrigen,
                               Heladera heladeraDestino,
                               Integer cantViandas,
                               MotivoDistribuicion motivoDistribuicion,
                               SolicitudAperturaService solicitudAperturaService) {
    solicitudAperturaService.registrarSolicitud(this, heladeraOrigen);
    Colaboracion colaboracion = new DistribucionVianda(this,
        heladeraOrigen, heladeraDestino, cantViandas, motivoDistribuicion);
    agregarColaboracionSiTienePermiso(colaboracion);
    solicitudAperturaService.registrarApertura(this, heladeraOrigen);
  }

  public void hacerseCargoDeHeladera(Heladera heladeraCargo) {
    Colaboracion colaboracion = new HacerseCargoHeladera(this, heladeraCargo);
    agregarColaboracionSiTienePermiso(colaboracion);
  }

  public void registrarPersonaVulnerable(RegistracionPersonaVulnerableBuilder builder) {
    RegistracionPersonaVulnerable registracion = builder.generarLaRegistracion();
    agregarColaboracionSiTienePermiso(registracion);
  }

  public RegistracionPersonaVulnerableBuilder iniciarPersonaVulnerableBuilder() {
    if (tarjetasPersonaVulnerables.isEmpty()) {
      throw new RuntimeException("Precisa tener tarjetas para Personas Vulnerables para "
         + "iniciar este builder");
    } else {
      validarPermisoParaBuilder(new RegistracionPersonaVulnerable(this, null, null));
      return RegistracionPersonaVulnerableBuilder.crear(this);
    }
  }

  private void agregarColaboracionSiTienePermiso(Colaboracion colaboracion) {
    validarPermisoColaboracion(colaboracion);
    colaboraciones.add(colaboracion);
  }

  protected void validarPermisoColaboracion(Colaboracion colaboracion) {
    if (noPuedeRealizarColaboracion(colaboracion)) {
      throw new ValidarPermisoColaboracionException("Este colaborador no puede realizar "
          + "esta colaboración.");
    }
  }

  protected void validarPermisoParaBuilder(Colaboracion colaboracion) {
    if (noPuedeRealizarColaboracion(colaboracion)) {
      throw new ValidarPermisoColaboracionException("Este colaborador no puede iniciar el "
          + "builder para esta colaboración.");
    }
  }

  public abstract boolean puedeRealizarColaboracion(Colaboracion colaboracion);

  public boolean noPuedeRealizarColaboracion(Colaboracion colaboracion) {
    return !puedeRealizarColaboracion(colaboracion);
  }

  public boolean tieneTarjeta(TarjetaPersonaVulnerable tarjetaPersonaVulnerable) {
    return this.tarjetasPersonaVulnerables.contains(tarjetaPersonaVulnerable);
  }

  public abstract boolean puedePoseerTarjetas();

  public void solicitarTarjetaPersonal() {
    validarPermisoTarjetas(this);
    if (tarjetaPersonal != null) {
      throw new RuntimeException("Ya tienes una tarjeta personal.");
    }
    SolicitudTarjeta solicitud =
        new SolicitudTarjeta(this, this.getDireccion());
    this.registrarSolicitudTarjeta(solicitud);
  }

  public void solicitarTarjetasPersonasVulnerables(int cantidad) {
    validarPermisoTarjetas(this);
    RepoSolicitudesTarjetas.getInstance().verificacionCantidadSolicitada(cantidad);
    SolicitudTarjeta solicitud =
        new SolicitudTarjeta(this, this.getDireccion(), cantidad);
    this.registrarSolicitudTarjeta(solicitud);
  }

  private void registrarSolicitudTarjeta(SolicitudTarjeta solicitudTarjeta) {
    RepoSolicitudesTarjetas.getInstance().registrarSolicitud(solicitudTarjeta);
    this.historialSolicitudesTarjetas.add(solicitudTarjeta);
  }

  public void recibeTarjetasPersonaVulnerable(
      List<TarjetaPersonaVulnerable> tarjetaPersonaVulnerables
  ) {
    validarPermisoTarjetas(this);
    this.tarjetasPersonaVulnerables.addAll(tarjetaPersonaVulnerables);
  }

  public void recibeTarjetaPersonal(TarjetaColaborador tarjeta) {
    validarPermisoTarjetas(this);
    this.tarjetaPersonal = tarjeta;
  }

  protected void validarPermisoTarjetas(Colaborador colaborador) {
    if (!puedePoseerTarjetas()) {
      throw new ValidarPermisoUsoTarjetasException("Este colaborador no puede poseer tarjetas.");
    } else if (colaborador.getDireccion() == null) {
      throw new ValidarPermisoUsoTarjetasException("No ha guardado una dirección.");
    }
  }

  public TarjetaPersonaVulnerable getPrimeraTarjetaDeLista() {
    if (tarjetasPersonaVulnerables.isEmpty()) {
      throw new RuntimeException("No posee tarjetas actualmente");
    } else {
      return this.tarjetasPersonaVulnerables.get(0);
    }
  }

  public void removerTarjeta(TarjetaPersonaVulnerable tarjetaPersonaVulnerable) {
    this.tarjetasPersonaVulnerables.remove(tarjetaPersonaVulnerable);
  }

  public void registrarSolicitudApertura(SolicitudApertura solicitud) {
    this.solicitudesApertura.add(solicitud);
  }

  public void registrarApertura(Apertura apertura) {
    this.aperturas.add(apertura);
  }

  //Para el reporte Interno
  public int getPesosDonados() {
    int pesosDonados = 0;
    for (Colaboracion donacionDinero : this.colaboraciones) {
      pesosDonados += ((DonacionDinero) donacionDinero).getMontoDinero();
    }
    return pesosDonados;
  }

  public Integer getCantidadViandasDistribuidas() {
    Integer viandasDistribuidas = 0;
    for (Colaboracion distribucionVianda : this.colaboraciones) {
      viandasDistribuidas += ((DistribucionVianda) distribucionVianda).getCantViandas();
    }
    return viandasDistribuidas;
  }

  public Integer getCantidadViandasDonadas() {
    int cantidadViandasDonadas = 0;
    for (Colaboracion donacionVianda : this.colaboraciones) {
      cantidadViandasDonadas += ((DonacionVianda) donacionVianda).getCantidadViandas();
    }
    return cantidadViandasDonadas;
  }

  public Integer getSemanasViandasFrescas() {
    Integer cantidadSemanasViandasFrescas = 0;
    for (Colaboracion donacionVianda : this.colaboraciones) {
      cantidadSemanasViandasFrescas += ((DonacionVianda) donacionVianda)
          .getCantidadSemanasFrescas();
    }
    return cantidadSemanasViandasFrescas;
  }

  public Integer getCantTarjetasDistribuidas() {
    return this.colaboraciones.stream()
        .map(colaboracion -> (RegistracionPersonaVulnerable) colaboracion)
        .toList().size();
  }

  public Integer getCantUsosTarjetas() {
    return this.getTarjetasPersonaVulnerable().stream()
        .mapToInt(TarjetaPersonaVulnerable::getCantUsos).sum();
  }

  public Integer getMesesTarjetasActivas() {
    return this.getTarjetasPersonaVulnerable().stream()
            .mapToInt(TarjetaPersonaVulnerable::getCantidadMesesActiva).sum();
  }

  public List<MedioContacto> getMediosContacto() {
    return mediosContacto;
  }

  public List<Colaboracion> getColaboraciones() {
    return colaboraciones;
  }

  public Credencial getCredencial() {
    return credencial;
  }

  public List<TarjetaPersonaVulnerable> getTarjetasPersonaVulnerable() {
    return tarjetasPersonaVulnerables;
  }

  public void setCredencial(Credencial credencial) {
    this.credencial = credencial;
  }

  public TarjetaColaborador getTarjetaPersonal() {
    return tarjetaPersonal;
  }

  public void setDireccion(Direccion direccion) {
    this.direccion = direccion;
  }

  public Direccion getDireccion() {
    return direccion;
  }

  public List<SolicitudTarjeta> getHistorialSolicitudesTarjetas() {
    return historialSolicitudesTarjetas;
  }

  public Double getPuntajeColaborador() {
    return this.getPesosDonados() * 0.5
        + this.getCantidadViandasDistribuidas()
        + this.getCantidadViandasDonadas() * this.getSemanasViandasFrescas() * 1.5
        + this.getCantTarjetasDistribuidas()
        * this.getMesesTarjetasActivas() * this.getCantUsosTarjetas();
  }

  public void suscribirseHeladera(Heladera heladera) {
    heladera.getSuscripciones().suscribirColaborador(this);
  }

  //Metodos para Tests
  public void agregarColaboracion(Colaboracion colaboracion) {
    this.colaboraciones.add(colaboracion);
  }

  public void setEsAdmin(boolean valor) {
    this.esAdmin = valor;
  }

  public boolean esAdmin() {
    return esAdmin;
  }
}



