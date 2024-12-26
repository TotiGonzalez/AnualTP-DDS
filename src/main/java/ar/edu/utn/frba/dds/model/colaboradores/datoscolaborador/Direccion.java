package ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador;

import javax.persistence.Embeddable;

@Embeddable
public class Direccion {
  private String ciudad;
  private String calle;
  private String localidad;
  private Integer altura;

  public Direccion(String ciudad, String localidad, String calle, Integer altura) {
    this.ciudad = ciudad;
    this.localidad = localidad;
    this.calle = calle;
    this.altura = altura;
  }

  public Direccion() {}

  public String getCiudad() {
    return ciudad;
  }

  public String getCalle() {
    return calle;
  }

  public String getLocalidad() {
    return localidad;
  }

  public Integer getAltura() {
    return altura;
  }
}


