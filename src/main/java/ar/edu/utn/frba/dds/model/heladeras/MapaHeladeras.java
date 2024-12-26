package ar.edu.utn.frba.dds.model.heladeras;

import ar.edu.utn.frba.dds.db.RepositorioPersistente;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Direccion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class MapaHeladeras extends RepositorioPersistente {
  private static final MapaHeladeras INSTANCE = new MapaHeladeras();
  private List<Heladera> heladeras = new ArrayList<Heladera>();
  private List<DatosHeladera> mapa = new ArrayList<DatosHeladera>();

  public MapaHeladeras() {
    super(Heladera.class);
  }

  public static MapaHeladeras getInstance() {
    return INSTANCE;
  }

  public Heladera crearHeladera(UbicacionPrecisa ubicacion,
                                String nombre,
                                EstadoHeladera estadoHeladera,
                                double capacidadTotal,
                                ConfiguracionHeladera configuracionHeladera) {
    Heladera heladera =
        new Heladera(nombre, ubicacion,
            EstadoHeladera.INACTIVA, capacidadTotal, configuracionHeladera);
    this.agregarHeladera(heladera);
    return heladera;
  }

  public void agregarHeladera(Heladera heladera) {
    this.heladeras.add(heladera);
    this.agregarDatosHeladera(heladera);
  }

  public void eliminarHeladera(Heladera heladera) {
    this.heladeras.remove(heladera);
  }

  public Heladera buscarHeladera(String nombre) {
    List<Heladera> heladerasList = new ArrayList<>();
    if (!heladeras.isEmpty()) {
      heladerasList = heladeras.stream().filter(heladera
          -> heladera.getNombreHeladera().equals(nombre)).collect(Collectors.toList());
    }
    if (heladerasList.isEmpty()) {
      return null;
    }
    return heladerasList.get(0);
  }

  public List<UbicacionPrecisa> ubicacionesHeladeras() {
    if (heladeras.isEmpty()) {
      throw new RuntimeException("la lista esta vacia");
    } else {
      return this.heladeras.stream()
          .map(Heladera::getUbicacionHeladera).collect(Collectors.toList());
    }
  }

  public void agregarDatosHeladera(Heladera heladera) {
    String nombreHeladera = heladera.getNombreHeladera();
    UbicacionPrecisa ubicacion = heladera.getUbicacionHeladera();
    EstadoHeladera estado = heladera.getEstadoHeladera();
    NivelLlenado nivelLlenado = heladera.getNivelLlenado();

    this.mapa.add(new DatosHeladera(
        nombreHeladera, ubicacion, estado, nivelLlenado
    ));
  }

  public List<DatosHeladera> obtenerListadoHeladeras() {
    return mapa;
  }

  public Integer cantidadHeladerasActiva() {
    return Math.toIntExact(this.heladeras.stream()
        .filter(Heladera::estaActiva).count());
  }

  public Integer getCantidadMesesActivas() {
    return Arrays.stream(this.heladeras.stream()
                        .mapToInt(Heladera::getCantidadMesesActiva)
                        .toArray()).sum();
  }

  public Integer getCantidadUsos() {
    return Arrays.stream(this.heladeras.stream()
        .mapToInt(Heladera::getCantUsos)
        .toArray()).sum();
  }

  public double getPuntajeHeladera() {
    return this.cantidadHeladerasActiva()
        * this.getCantidadMesesActivas()
        * this.getCantidadUsos()
        * 5;
  }

  public List<Heladera> getHeladeras() {
    return heladeras;
  }

  public void vaciarListas() {
    this.heladeras.clear();
    this.mapa.clear();
  }

  public Heladera generarHeladera(String nombre, String ciudad, EstadoHeladera estado,
                                  double latitud, double longitud, double temperaturaMinima,
                                  double temperaturaMaxima, Direccion direccionHeladera, int capacidadTotal, int tiempoExpiracionSolicitudes) {

    UbicacionPrecisa ubicacionHeladera = new UbicacionPrecisa(ciudad,
        direccionHeladera, latitud, longitud);
    ConfiguracionHeladera configuracionHeladera = new ConfiguracionHeladera(temperaturaMinima,
        temperaturaMaxima, tiempoExpiracionSolicitudes);
    return this.crearHeladera(ubicacionHeladera,nombre, estado,
        capacidadTotal, configuracionHeladera);
  }
}
