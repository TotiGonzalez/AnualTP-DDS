package ar.edu.utn.frba.dds.model.sensores;

public class SensorDeViandas {
  private Lsensor apiLunch;

  public SensorDeViandas(Lsensor apiLunch) {
    this.apiLunch = apiLunch;
  }

  public void setLunchBoxAction(LunchBoxAction action) {
    if (apiLunch != null) {
      apiLunch.onLunchBoxChanged(action);
    }
  }
}
