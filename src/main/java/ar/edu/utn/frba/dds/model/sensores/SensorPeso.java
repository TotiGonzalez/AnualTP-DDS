package ar.edu.utn.frba.dds.model.sensores;

import ar.edu.utn.frba.dds.model.heladeras.Heladera;
import ar.edu.utn.frba.dds.model.heladeras.TipoIncidente;

public class SensorPeso {
  private Wsensor apiPeso;
  private Heladera heladera;
  public SensorPeso(Wsensor apiPeso) {
    this.apiPeso = apiPeso;
  }

  public Reading getReading(String serialNumber) throws Exception {
    if (serialNumber == null) {
      heladera.alertarFalloHeladera(TipoIncidente.INCIDENTE_FALLACONEXION);
      throw new Exception("El serialNumber está vacío");
    } else {
      return apiPeso.getWeight(serialNumber);
    }
  }

  public double obtenerPesoActual(String serialNumber) throws Exception {
    Reading reading = getReading(serialNumber);
    return convertiraKg(reading);
  }

  public double convertiraKg(Reading reading) {
    if (reading.getUnit().equalsIgnoreCase("lbs")) {
      return reading.getValue() * 0.453592;
    } else {
      return reading.getValue();
    }
  }

}