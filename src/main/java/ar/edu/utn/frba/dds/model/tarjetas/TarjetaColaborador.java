package ar.edu.utn.frba.dds.model.tarjetas;

import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;

@Entity
@DiscriminatorValue("Tarjeta_C")
public class TarjetaColaborador extends Tarjeta {

  @OneToOne
  @JoinColumn(name="colaborador_id")
  private Colaborador titularTarjeta;

  public TarjetaColaborador(String codigo, Colaborador titularTarjeta) {
    this.codigo = codigo;
    this.listaUsosTarjeta = new ArrayList<>();
    this.titularTarjeta = titularTarjeta;
    this.fechaActivacion = LocalDate.now();
  }

  public TarjetaColaborador(Colaborador titularTarjeta) {

      this.titularTarjeta = titularTarjeta;
  }

  public TarjetaColaborador() {

  }

  public Colaborador getTitularTarjeta() {
      return titularTarjeta;
    }

  public void registrarUso(String nombreHeladera, MotivoUso motivoUso) {
    listaUsosTarjeta.add(new RegistroUso(nombreHeladera, motivoUso));
  }
}
