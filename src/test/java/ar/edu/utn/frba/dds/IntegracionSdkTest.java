package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Direccion;
import ar.edu.utn.frba.dds.model.heladeras.ConfiguracionHeladera;
import ar.edu.utn.frba.dds.model.heladeras.EstadoHeladera;
import ar.edu.utn.frba.dds.model.heladeras.Heladera;
import ar.edu.utn.frba.dds.model.heladeras.NivelLlenado;
import ar.edu.utn.frba.dds.model.heladeras.UbicacionPrecisa;
import ar.edu.utn.frba.dds.model.sensores.Reading;
import ar.edu.utn.frba.dds.model.sensores.SensorPeso;
import ar.edu.utn.frba.dds.model.sensores.SensorTemperatura;
import ar.edu.utn.frba.dds.model.sensores.Tsensor;
import ar.edu.utn.frba.dds.model.sensores.Wsensor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IntegracionSdkTest {

  private Wsensor wSensor;
  private Tsensor tSensor;
  private SensorPeso sensorPeso;
  private SensorTemperatura sensorTemperatura;
  private Heladera heladera;
  private UbicacionPrecisa ubicacionPrecisa;

  @BeforeEach
  void setUp() {
    wSensor = mock(Wsensor.class);
    sensorPeso = new SensorPeso(wSensor);

    tSensor = mock(Tsensor.class);
    sensorTemperatura = new SensorTemperatura(tSensor);

    Direccion direccionHeladera1 = new Direccion("CABA",
        "Almagro","falsa", 123);

    UbicacionPrecisa ubicacionPrecisa = new UbicacionPrecisa("A",
        direccionHeladera1, 23.3, 22.2);

    EstadoHeladera estadoHeladera = EstadoHeladera.ACTIVA;

    ConfiguracionHeladera configuracionHeladera = new ConfiguracionHeladera(2,
        5, 3);

    this.heladera = new Heladera("heladera", ubicacionPrecisa, estadoHeladera,
        100, configuracionHeladera);

  }

  @Test
  public void instanciaReadingCorrectamente() {
    Reading reading = mock(Reading.class);
    when(reading.getUnit()).thenReturn("kg");
    when(reading.getValue()).thenReturn(90.0);

    String numeroSerie = heladera.getNumeroSerie();

    when(wSensor.getWeight(numeroSerie)).thenReturn(reading);

    Assertions.assertEquals(reading.getUnit(), "kg");
    Assertions.assertEquals(reading.getValue(), 90.0);
  }

  @Test
  public void obtenerPesoHeladeraKg() throws Exception {
    Reading reading = mock(Reading.class);
    when(reading.getUnit()).thenReturn("kg");
    when(reading.getValue()).thenReturn(90.0);

    String numeroSerie = heladera.getNumeroSerie();

    when(wSensor.getWeight(numeroSerie)).thenReturn(reading);

    //modifica el peso de la heladera
    heladera.actualizarPesoActual(sensorPeso);
    double pesoActualHeladera = heladera.getPesoActual();

    Assertions.assertEquals(sensorPeso.obtenerPesoActual(numeroSerie), pesoActualHeladera);
    Assertions.assertEquals(sensorPeso.getReading(numeroSerie), reading);
  }

  @Test
  public void obtenerPesoHeladeraLbs() throws Exception {
    Reading reading = mock(Reading.class);
    when(reading.getUnit()).thenReturn("lbs");
    when(reading.getValue()).thenReturn(800.0);

    String numeroSerie = heladera.getNumeroSerie();
    when(wSensor.getWeight(numeroSerie)).thenReturn(reading);

    //modifica el peso de la heladera
    try {heladera.actualizarPesoActual(sensorPeso);}
    catch (Exception e) {e.printStackTrace();}
    double pesoActualHeladera2 = heladera.getPesoActual();

    Assertions.assertEquals(sensorPeso.obtenerPesoActual(numeroSerie), pesoActualHeladera2);
    Assertions.assertEquals(sensorPeso.getReading(numeroSerie), reading);
  }

  @Test
  public void obtenerNivelLlenadoHeladeraMedio() {
    double medioPeso = heladera.getCapacidadTotal() * 0.4 / 100;
    heladera.setPesoActual(medioPeso);

    NivelLlenado nivelLlenado = heladera.getNivelLlenado();
    Assertions.assertEquals(NivelLlenado.MEDIO, nivelLlenado);
  }

  @Test
  public void obtenerNivelLlenadoHeladeraAlto() {
    double medioPeso = heladera.getCapacidadTotal() * 0.75 / 100;
    heladera.setPesoActual(medioPeso);

    NivelLlenado nivelLlenado = heladera.getNivelLlenado();
    Assertions.assertEquals(NivelLlenado.ALTO, nivelLlenado);
  }

  @Disabled
  public void heladeraNecesitaAtencion() {

    for(int i = 1; i <= 3; i++) {
      heladera.setTemperaturaActual(-80.0);
      heladera.accionAnteCambioTemperatura(sensorTemperatura);
    }

   // verify(sensorTemperatura, times(3)).configurarAccionCambioTemperatura(heladera.getNumeroSerie(),
     //   heladera.actualizarUltimaLecturaTemperatura());

    Assertions.assertEquals(EstadoHeladera.NECESITA_ATENCION, heladera.getEstadoHeladera());
  }

  @Disabled
  public void heladeraNoNecesitaAtencion() {
    heladera.setTemperaturaActual(-80.0);
    heladera.accionAnteCambioTemperatura(sensorTemperatura);

    //verify(sensorTemperatura).configurarAccionCambioTemperatura(heladera.getNumeroSerie(),
      //  heladera.actualizarUltimaLecturaTemperatura());

    Assertions.assertEquals(EstadoHeladera.ACTIVA, heladera.getEstadoHeladera());
  }

  @Test
  public void connect_serialNumberEsNulo() {
    String nullSerialNumber = null;
    Assertions.assertThrows(Exception.class,()-> sensorTemperatura.connect(nullSerialNumber));
  }
}
