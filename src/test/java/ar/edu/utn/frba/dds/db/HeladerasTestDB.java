package ar.edu.utn.frba.dds.db;

import ar.edu.utn.frba.dds.model.colaboraciones.*;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Direccion;
import ar.edu.utn.frba.dds.model.heladeras.*;
import ar.edu.utn.frba.dds.model.tarjetas.MotivoUso;
import ar.edu.utn.frba.dds.model.tarjetas.RegistroUso;
import ar.edu.utn.frba.dds.model.tarjetas.TarjetaPersonaVulnerable;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityTransaction;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class HeladerasTestDB implements WithSimplePersistenceUnit {
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

    this.heladera1 = generarHeladera("heladera1", "A", EstadoHeladera.ACTIVA,
            22.2, 22.2, 2, 5, direccionHeladera1, 100, 3);

    this.heladera2 = generarHeladera("heladera2", "B", EstadoHeladera.ACTIVA,
            79.3, 14.2, -90, 0, direccionHeladera2, 90, 3);
    double medioPeso = heladera2.getCapacidadTotal() * 0.6 / 100;
    heladera2.setPesoActual(medioPeso);

    EntityTransaction tx = mapaHeladeras.getTransaction();
    tx.begin();

    mapaHeladeras.persist(heladera1);
    mapaHeladeras.persist(heladera2);

    Colaboracion distribucionVianda = new DistribucionVianda(null, heladera1, heladera2, 1
            , MotivoDistribuicion.FALTA_DE_VIANDAS);

    Colaboracion donacionDinero = new DonacionDinero(null, 100000, Frecuencia.DIARIA);

    Comida comida1 = new Comida("Sopa Knor");
    Vianda vianda1 = new Vianda(comida1, LocalDate.now().plusMonths(1)
            ,130,100);
    vianda1.setHeladera(heladera1);
    vianda1.setFechaDonacion(LocalDate.now());

    Comida comida2 = new Comida("Maruchan");
    Vianda vianda2 = new Vianda(comida2, LocalDate.now().plusMonths(9)
            ,130,100);
    mapaHeladeras.persist(comida1);
    mapaHeladeras.persist(comida2);
    mapaHeladeras.persist(vianda1);
    mapaHeladeras.persist(vianda2);

    Colaboracion donacionVianda = new DonacionVianda(null,new ArrayList<>(Arrays.asList(vianda1
            , vianda2)));


    Colaboracion hacerseCargoHeladera = new HacerseCargoHeladera(null,heladera2);

    TarjetaPersonaVulnerable tarjeta = new TarjetaPersonaVulnerable("1234");
    //tarjeta.registrarUso("heladera1");
    RegistroUso registroUso = new RegistroUso("heladera1", MotivoUso.INGRESO);
    mapaHeladeras.persist(tarjeta);
    mapaHeladeras.persist(registroUso);

    Colaboracion registracionPersonaVulnerable = new RegistracionPersonaVulnerable(null,tarjeta,null );

    mapaHeladeras.persist(distribucionVianda);
    mapaHeladeras.persist(donacionDinero);
    mapaHeladeras.persist(donacionVianda);
    mapaHeladeras.persist(hacerseCargoHeladera);
    mapaHeladeras.persist(registracionPersonaVulnerable);

    tx.commit();

  }

  public Heladera generarHeladera(String nombre, String ciudad, EstadoHeladera estado,
      double latitud, double longitud, double temperaturaMinima,
                              double temperaturaMaxima, Direccion direccionHeladera, int capacidadTotal, int tiempoExpiracionSolicitudes) {

    UbicacionPrecisa ubicacionHeladera = new UbicacionPrecisa(ciudad,
        direccionHeladera, latitud, longitud);
    ConfiguracionHeladera configuracionHeladera = new ConfiguracionHeladera(temperaturaMinima,
        temperaturaMaxima, tiempoExpiracionSolicitudes);
    return mapaHeladeras.crearHeladera(ubicacionHeladera,nombre, estado,
        capacidadTotal, configuracionHeladera);

  }

  //TODO SuscripcionHeladera y RecomendacionHeladera

}
