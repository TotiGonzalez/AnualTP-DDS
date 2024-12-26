package ar.edu.utn.frba.dds.model.reportes;

import ar.edu.utn.frba.dds.model.colaboraciones.DonacionVianda;
import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.model.colaboradores.RepoColaboradores;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


// colaborador | viandas donadas en la semana | viandas distribuidas en la semana

@Entity
@Table(name="reporte_viandas_por_colaborador")
public class ReporteViandasPorColaborador extends GeneracionReportes {

  @OneToMany
  @JoinColumn(name = "vianda_donada", referencedColumnName = "id")
  private List<ViandasDonadasPorColaborador> todasViandasDonadas = new ArrayList<>();

  public List<ViandasDonadasPorColaborador> calcularViandasDonadasUltimaSemanaDias() {
    LocalDate hoy = LocalDate.now();
    LocalDate hace7Dias = hoy.minus(7, ChronoUnit.DAYS);
    LocalDate ayer = hoy.minus(1, ChronoUnit.DAYS);

    return this.todosColaboradores().stream()
        .map(colaborador -> {
          int viandasDonadas = colaborador.getColaboraciones().stream()
              .filter(colaboracion -> colaboracion instanceof DonacionVianda)
              .filter(colaboracion -> {
                LocalDate fechaColaboracion = colaboracion.getFechaColaboracion();
                return !fechaColaboracion.isBefore(hace7Dias) && !fechaColaboracion.isAfter(ayer);
              })
              .mapToInt(colaboracion -> ((DonacionVianda) colaboracion).getCantidadViandas())
              .sum();
          return new ViandasDonadasPorColaborador(colaborador, viandasDonadas);
        })
        .collect(Collectors.toList());
  }

  private List<Colaborador> todosColaboradores() {
    return RepoColaboradores.getInstance().getColaboradores();
  }

  @Override
  public void generar() {
    this.todasViandasDonadas.clear();
    this.todasViandasDonadas = this.calcularViandasDonadasUltimaSemanaDias();
  }

  public List<ViandasDonadasPorColaborador> getTodasViandasDonadas() {
    return todasViandasDonadas;
  }
}
