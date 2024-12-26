package ar.edu.utn.frba.dds.model.colaboraciones;

import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.model.heladeras.Heladera;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "distribucion_vianda")
public class DistribucionVianda extends Colaboracion {
  @OneToOne
  @JoinColumn(name = "heladera_origen_id",  referencedColumnName = "id")
  private Heladera heladeraOrigen;

  @OneToOne
  @JoinColumn(name = "heladera_destino_id",  referencedColumnName = "id")
  private Heladera heladeraDestino;

  @Column(name = "cant_viandas")
  private Integer cantViandas;

  @Enumerated(EnumType.STRING)
  private MotivoDistribuicion motivo;

  public DistribucionVianda(Colaborador colaborador,
                            Heladera heladeraOrigen,
                            Heladera heladeraDestino,
                            Integer cantViandas,
                            MotivoDistribuicion motivoDistribuicion) {
    super(colaborador);
    this.heladeraOrigen = heladeraOrigen;
    this.heladeraDestino = heladeraDestino;
    this.cantViandas = cantViandas;
    this.motivo = motivoDistribuicion;
    this.fechaColaboracion = LocalDate.now();
  }

  public DistribucionVianda() {

  }

  @Override
  public boolean puedeSerRealizadaPorFisico() {
    return true;
  }

  @Override
  public boolean puedeSerRealizadaPorJuridico() {
    return false;
  }

  public Integer getCantViandas() {
    return cantViandas;
  }
}
