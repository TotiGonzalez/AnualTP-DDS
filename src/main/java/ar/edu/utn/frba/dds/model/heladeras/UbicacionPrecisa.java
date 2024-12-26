package ar.edu.utn.frba.dds.model.heladeras;

import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Direccion;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class UbicacionPrecisa {
  @Column(name = "ciudad_heladera")
  private String ciudad;

  @Embedded
  private Direccion direccion;
  private Double latitud;
  private Double longitud;

  public UbicacionPrecisa(String ciudad, Direccion direccion, Double latitud, Double longitud) {
    this.ciudad = ciudad;
    this.direccion = direccion;
    this.latitud = latitud;
    this.longitud = longitud;
  }

  public UbicacionPrecisa() {}

  public String getCiudad() {
    return ciudad;
  }

  public Direccion getDireccion() {
    return direccion;
  }

  public Double getLatitud() {
    return latitud;
  }

  public Double getLongitud() {
    return longitud;
  }
}

