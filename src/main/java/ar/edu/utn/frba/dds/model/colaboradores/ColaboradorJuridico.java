package ar.edu.utn.frba.dds.model.colaboradores;

import ar.edu.utn.frba.dds.model.colaboraciones.Colaboracion;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.TipoJuridico;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@SuppressWarnings("FieldMayBeFinal")
public class ColaboradorJuridico extends Colaborador {

  @Enumerated(EnumType.STRING)
  private TipoJuridico tipoJuridico;

  @Column(name = "razon_social")
  private String razonSocial;

  @Column
  private String rubro;

  public ColaboradorJuridico(TipoJuridico tipoJuridico, String razonSocial, String rubro) {
    this.tipoJuridico = tipoJuridico;
    this.razonSocial = razonSocial;
    this.rubro = rubro;
  }

  public ColaboradorJuridico() {
  }

  @Override
  public boolean puedeRealizarColaboracion(Colaboracion colaboracion) {
    return colaboracion.puedeSerRealizadaPorJuridico();
  }

  @Override
  public boolean puedePoseerTarjetas() {
    return false;
  }

  public TipoJuridico getTipoJuridico() {
    return tipoJuridico;
  }

  public String getRazonSocial() {
    return razonSocial;
  }

  public String getRubro() {
    return rubro;
  }

}
