package ar.edu.utn.frba.dds.db.dt;

public class HeladeraDTO {
  private Long id;
  private String nombreHeladera;

  public HeladeraDTO(Long id, String nombreHeladera) {
    this.id = id;
    this.nombreHeladera = nombreHeladera;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNombreHeladera() {
    return nombreHeladera;
  }

  public void setNombreHeladera(String nombreHeladera) {
    this.nombreHeladera = nombreHeladera;
  }
}
