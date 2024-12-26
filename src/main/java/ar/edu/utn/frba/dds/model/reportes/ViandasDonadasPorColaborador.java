package ar.edu.utn.frba.dds.model.reportes;

import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.db.EntidadPersistente;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="viandas_donadas")
public class ViandasDonadasPorColaborador extends EntidadPersistente {

  //muchas viandas relacionadas a mismo colab. 3 colab no van a tener exactamente la misma vianda
  @ManyToOne
  @JoinColumn(name="colaborador_asociado", referencedColumnName="id")
  private Colaborador colaborador;

  @Column(name="cantidad_viandas_donadas")
  private int cantidadViandasDonadas;

  public ViandasDonadasPorColaborador(Colaborador colaborador, int cantidadViandasDonadas) {
    this.colaborador = colaborador;
    this.cantidadViandasDonadas = cantidadViandasDonadas;
  }

  public ViandasDonadasPorColaborador() {}

  public Colaborador getColaborador() {
    return colaborador;
  }

  public int getCantidadViandasDonadas() {
    return cantidadViandasDonadas;
  }
}
