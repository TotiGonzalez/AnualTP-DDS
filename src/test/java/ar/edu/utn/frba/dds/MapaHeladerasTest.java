package ar.edu.utn.frba.dds;

import static org.mockito.Mockito.mock;

import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Direccion;
import ar.edu.utn.frba.dds.model.heladeras.ConfiguracionHeladera;
import ar.edu.utn.frba.dds.model.heladeras.EstadoHeladera;
import ar.edu.utn.frba.dds.model.heladeras.Heladera;
import ar.edu.utn.frba.dds.model.heladeras.MapaHeladeras;
import ar.edu.utn.frba.dds.model.heladeras.DatosHeladera;
import ar.edu.utn.frba.dds.model.heladeras.UbicacionPrecisa;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class MapaHeladerasTest {
  private MapaHeladeras mapaHeladeras;
  private Heladera heladera1;
  private Heladera heladera2;

  Direccion direccionHeladera1 = new Direccion("CABA",
      "Almagro", "falsa", 123);

  Direccion direccionHeladera2 = new Direccion("CABA",
      "Almagro", "Mas Falsa", 124);

  @BeforeEach
  void setUp() {
    mapaHeladeras = MapaHeladeras.getInstance();
    mapaHeladeras.vaciarListas(); // Para que no hayan heladeras repetidas

    this.heladera1 = generarHeladera("heladera1","A",EstadoHeladera.ACTIVA,
        22.2,22.2,2,5,direccionHeladera1,100, 3);

    this.heladera2 = generarHeladera("heladera2","B",EstadoHeladera.ACTIVA,
        79.3,14.2,-90,0,direccionHeladera2,90, 3);
    double medioPeso = heladera2.getCapacidadTotal() * 0.6 / 100;
    heladera2.setPesoActual(medioPeso);

  }

  public Heladera generarHeladera(String nombre, String ciudad, EstadoHeladera estado,
      double latitud, double longitud, double temperaturaMinima,
                              double temperaturaMaxima, Direccion direccionHeladera, int capacidadTotal, int tiempoExpiracionSolicitudes) {

    UbicacionPrecisa ubicacionPrecisa = new UbicacionPrecisa(ciudad,
        direccionHeladera, latitud, longitud);
    ConfiguracionHeladera configuracionHeladera = new ConfiguracionHeladera(temperaturaMinima,
        temperaturaMaxima, tiempoExpiracionSolicitudes);
    return mapaHeladeras.crearHeladera(ubicacionPrecisa,nombre, estado,
        capacidadTotal, configuracionHeladera);

  }

  @Test
  void obtenerListadoHeladerasTest() {
    List<DatosHeladera> datosHeladerasEsperados = List.of(
        new DatosHeladera("heladera1", heladera1.getUbicacionHeladera(), heladera1.getEstadoHeladera(), heladera1.getNivelLlenado()),
        new DatosHeladera("heladera2", heladera2.getUbicacionHeladera(), heladera2.getEstadoHeladera(), heladera2.getNivelLlenado())
    );

    List<DatosHeladera> datosHeladerasActuales = mapaHeladeras.obtenerListadoHeladeras();
    List<String> nombresDeHeladerasActuales = datosHeladerasActuales.stream()
        .map(DatosHeladera::getNombreHeladera).toList();

        Assertions.assertEquals(datosHeladerasEsperados.size(), datosHeladerasActuales.size());

    for (DatosHeladera datosEsperados : datosHeladerasEsperados) {
      Assertions.assertTrue(nombresDeHeladerasActuales.contains(datosEsperados.getNombreHeladera()));
    }
  }

  @Test
  public void eliminarHeladeraTest(){
    Assertions.assertNotNull(mapaHeladeras.buscarHeladera("heladera2"));
    mapaHeladeras.eliminarHeladera(heladera2);
    Assertions.assertNull(mapaHeladeras.buscarHeladera("heladera2"));
  }

  @Test
  public void ponerEnFuncionamientoTest(){
    heladera2.ponerEnFuncionamiento();
    Assertions.assertEquals(heladera2.getEstadoHeladera(), EstadoHeladera.ACTIVA);
  }

  @Test
  public void detenerFuncionamientoTest(){
    heladera2.detenerFuncionamiento();
    Assertions.assertEquals(heladera2.getEstadoHeladera(), EstadoHeladera.INACTIVA);
  }


}
