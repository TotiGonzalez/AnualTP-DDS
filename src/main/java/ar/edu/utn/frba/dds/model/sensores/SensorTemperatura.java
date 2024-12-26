package ar.edu.utn.frba.dds.model.sensores;

//invoker : asks the command to carry out the request.
public class SensorTemperatura {
  private Tsensor apiTemperatura;
  public SensorTemperatura(Tsensor apiTemperatura) {
    this.apiTemperatura = apiTemperatura;

  }

  public void connect(String serialNumber) throws Exception {
    if (serialNumber == null) {
      throw new Exception("El serialNumber está vacío");
    } else {
      apiTemperatura.connect(serialNumber);
    }
  }

  public void onTemperatureChange(Action action) {
    apiTemperatura.onTemperatureChange(action);
  }
}