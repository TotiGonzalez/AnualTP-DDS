package ar.edu.utn.frba.dds.model.reporteinterno;

import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.db.EntidadPersistente;
import ar.edu.utn.frba.dds.db.convertidores.ConvertidorLocalDate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class ReporteInterno extends EntidadPersistente {

  @OneToMany
  @JoinColumn(name = "linea_id", referencedColumnName = "id")
  List<LineaReporte> lineasReporte = new ArrayList<>();
  @Column
  @Convert(converter = ConvertidorLocalDate.class)
  LocalDate periodoReporte;

  public void calcularPuntajeReporte() {
    lineasReporte.forEach(LineaReporte::asignarPuntaje);
  }

  public void agregarLineasReporte(List<LineaReporte> lineasReporte) {
    this.lineasReporte.addAll(lineasReporte);
  }

  public void setPeriodoReporte(LocalDate periodoReporte) {
    this.periodoReporte = periodoReporte;
  }

  public void generarReporte(List<Colaborador> colaboradores) {
    List<LineaReporte> lineasReporte = new ArrayList<>();
    for (Colaborador colaborador : colaboradores) {
      LineaReporte lineaReporte = new LineaReporte(colaborador);
      lineasReporte.add(lineaReporte);
    }
    this.setPeriodoReporte(LocalDate.now());
    this.agregarLineasReporte(lineasReporte);
    this.calcularPuntajeReporte();
  }

  public List<LineaReporte> getLineasReporte() {
    return lineasReporte;
  }

  public LocalDate getPeriodoReporte() {
    return periodoReporte;
  }
}
