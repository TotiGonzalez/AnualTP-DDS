package ar.edu.utn.frba.dds.model.colaboraciones;

import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.model.heladeras.Vianda;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "donacion_vianda")
public class DonacionVianda extends Colaboracion {
  @OneToMany
  @JoinColumn(name = "donacion_vianda_id", referencedColumnName = "id")
  private List<Vianda> viandas = new ArrayList<>();

  public DonacionVianda(Colaborador colaborador, List<Vianda> viandas) {
    super(colaborador);
    this.viandas = viandas;
    this.fechaColaboracion = LocalDate.now();
  }

  public DonacionVianda() {

  }

  @Override
  public boolean puedeSerRealizadaPorFisico() {
    return true;
  }

  @Override
  public boolean puedeSerRealizadaPorJuridico() {
    return false;
  }

  public Integer getCantidadSemanasFrescas() {
    return Arrays.stream(this.viandas.stream()
        .mapToInt(Vianda::getCantidadSemanasfresca).toArray()).sum();
  }

  public int getCantidadViandas() {
    return viandas.size();
  }

  //Metodo para test
  public void setFechaColaboracion(LocalDate fechaColaboracion) {
    this.fechaColaboracion = fechaColaboracion;
  }

}
