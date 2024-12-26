package ar.edu.utn.frba.dds.db;

import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Direccion;
import ar.edu.utn.frba.dds.model.heladeras.*;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityTransaction;

public class MapaHeladerasTestDB implements WithSimplePersistenceUnit {
  private MapaHeladeras mapaHeladeras;
  private Heladera heladera1;
  private Heladera heladera2;

  Direccion direccionHeladera1 = new Direccion("CABA",
      "Almagro", "falsa", 123);

  Direccion direccionHeladera2 = new Direccion("CABA",
      "Almagro", "Mas Falsa", 124);

  @Test
  public void testHeladeras() {
    mapaHeladeras = MapaHeladeras.getInstance();
    mapaHeladeras.vaciarListas(); // Para que no hayan heladeras repetidas

    this.heladera1 = generarHeladera("heladera1","A", EstadoHeladera.ACTIVA,
        22.2,22.2,2,5,direccionHeladera1,100, 3);

    this.heladera2 = generarHeladera("heladera2","B",EstadoHeladera.ACTIVA,
        79.3,14.2,-90,0,direccionHeladera2,90, 3);
    double medioPeso = heladera2.getCapacidadTotal() * 0.6 / 100;
    heladera2.setPesoActual(medioPeso);

    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();

    entityManager().persist(heladera1);
    entityManager().persist(heladera2);

    tx.commit();

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


}
