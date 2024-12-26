package ar.edu.utn.frba.dds.model.heladeras;

import javax.persistence.Embeddable;

@Embeddable
public class ConfiguracionHeladera {
  private double temperaturaMinima;
  private double temperaturaMaxima;
  private int tiempoExpiracionSolicitudes;

  public ConfiguracionHeladera(double temperaturaMinima,
                               double temperaturaMaxima, int tiempoExpiracionSolicitudes) {
    this.temperaturaMinima = temperaturaMinima;
    this.temperaturaMaxima = temperaturaMaxima;
    this.tiempoExpiracionSolicitudes = tiempoExpiracionSolicitudes;
  }

  public ConfiguracionHeladera() {}

  public double getTemperaturaMaxima() {
    return temperaturaMaxima;
  }

  public double getTemperaturaMinima() {
    return temperaturaMinima;
  }

  public int getTiempoExpiracionSolicitudes() {
    return tiempoExpiracionSolicitudes;
  }

  public void setTiempoExpiracionSolicitudes(int tiempoExpiracionSolicitudes) {
    this.tiempoExpiracionSolicitudes = tiempoExpiracionSolicitudes;
  }
}
