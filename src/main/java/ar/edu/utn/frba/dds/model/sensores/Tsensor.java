package ar.edu.utn.frba.dds.model.sensores;

public interface Tsensor {
  void connect(String serialNumber);

  void onTemperatureChange(Action action);
}

