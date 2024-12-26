package ar.edu.utn.frba.dds.model.colaboradores;

import ar.edu.utn.frba.dds.model.colaboraciones.Colaboracion;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Documento;
import ar.edu.utn.frba.dds.db.convertidores.ConvertidorLocalDate;
import ar.edu.utn.frba.dds.model.heladeras.FallaTecnica;
import ar.edu.utn.frba.dds.model.heladeras.Heladera;
import java.time.LocalDate;
import java.util.EventListener;
import java.util.EventObject;
import javax.persistence.*;


@Entity
public class ColaboradorFisico extends Colaborador {

  @Column
  private String nombre;

  @Column
  private String apellido;

  @Column(name = "fecha_nacimiento")
  @Convert(converter = ConvertidorLocalDate.class)
  private LocalDate fechaNacimiento;

  @Enumerated(EnumType.STRING)
  private Documento tipoDocumento;

  @Column
  private Integer numeroDocumento;

  public ColaboradorFisico(String nombre, String apellido, LocalDate fechaNacimiento) {
    this.nombre = nombre;
    this.apellido = apellido;
    this.fechaNacimiento = fechaNacimiento;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
  }

  public ColaboradorFisico(String nombre, String apellido,
                           LocalDate fechaNacimiento, Documento tipoDocumento,
                           Integer numeroDocumento) {
    this.nombre = nombre;
    this.apellido = apellido;
    this.fechaNacimiento = fechaNacimiento;
    this.tipoDocumento = tipoDocumento;
    this.numeroDocumento = numeroDocumento;
  }

  public ColaboradorFisico() {

  }

  @Override
  public boolean puedeRealizarColaboracion(Colaboracion colaboracion) {
    return colaboracion.puedeSerRealizadaPorFisico();
  }

  @Override
  public boolean puedePoseerTarjetas() {
    return true;
  }

  public void reportarFallaTecnica(Heladera heladera, FallaTecnica fallaTecnica) {
    System.out.println("El colaborador " + this.getNombre() + " ha reportado una falla técnica en la heladera " + heladera.getNombreHeladera());
    heladera.agregarFallaTecnica(fallaTecnica);

    fallaTecnica.reportar();
  }

  public LocalDate getFechaNacimiento() {
    return fechaNacimiento;
  }

  public String getNombre() {
    return nombre;
  }

  public String getApellido() {
    return apellido;
  }

  @Override
  public Integer getCantUsosTarjetas() {
    return super.getCantUsosTarjetas();
  }

  public Integer getNumeroDocumento() {
    return numeroDocumento;
  }

  public Documento getTipoDocumento() {
    return tipoDocumento;
  }

  public void setFechaNacimiento(LocalDate fechaNacimiento) {
    this.fechaNacimiento = fechaNacimiento;
  }

  public void setNumeroDocumento(Integer numeroDocumento) {
    this.numeroDocumento = numeroDocumento;
  }

  public void setTipoDocumento(Documento tipoDocumento) {
    this.tipoDocumento = tipoDocumento;
  }

  public void alertaListener(EventListener listener) {
    EventObject escuchaEvento = new EventObject(listener);
    try {
      escuchaEvento.wait();
    } catch (InterruptedException excepcionCatcheada) {
      System.out.println(excepcionCatcheada.getMessage());
    }
  }
}
