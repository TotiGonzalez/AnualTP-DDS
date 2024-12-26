package ar.edu.utn.frba.dds.model.heladeras;

public class DatosHeladera {
  private String nombreHeladera;
  private UbicacionPrecisa ubicacion;
  private EstadoHeladera estado;
  private NivelLlenado nivelLlenado;

  public DatosHeladera(String nombreHeladera, UbicacionPrecisa ubicacion,
                       EstadoHeladera estado, NivelLlenado nivelLlenado) {
    this.ubicacion = ubicacion;
    this.nombreHeladera = nombreHeladera;
    this.estado = estado;
    this.nivelLlenado = nivelLlenado;
  }

  public String getNombreHeladera() {
    return nombreHeladera;
  }

  public UbicacionPrecisa getUbicacion() {
    return ubicacion;
  }

  public EstadoHeladera getEstado() {
    return estado;
  }

  public NivelLlenado getNivelLlenado() {
    return nivelLlenado;
  }
}
