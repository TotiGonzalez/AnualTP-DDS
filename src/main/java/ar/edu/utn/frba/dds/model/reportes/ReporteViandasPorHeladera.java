package ar.edu.utn.frba.dds.model.reportes;

import ar.edu.utn.frba.dds.model.heladeras.MapaHeladeras;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

//heladera | viandas colocadas en la semana | viandas retiradas en la semana

@Entity
@Table(name = "reporte_viandas_por_heladera")
public class ReporteViandasPorHeladera extends GeneracionReportes {

  //un movimiento va a estar relacionado a un solo reporte semanal, van a ser distintos
  @OneToMany
  @JoinColumn(name = "movimientos_por_heladera_id", referencedColumnName = "id")
  private List<MovimientoVianda> movimientoViandas = new ArrayList<>();

  // uso el repo de heladeras
  public List<MovimientoVianda> movimientosViandasTodasHeladeras() {
    MapaHeladeras mapaHeladeras = MapaHeladeras.getInstance();
    return mapaHeladeras.getHeladeras().stream()
        .flatMap(heladera -> heladera.getMovimientos().stream()).toList();
  }

  @Override
  public void generar() {
    this.movimientoViandas.clear();
    this.movimientoViandas = this.movimientosViandasTodasHeladeras().stream()
        .filter(mov -> this.dentroDeEstaSemana(mov.getFechaYhora()))
        .toList();
  }

  public List<MovimientoVianda> getMovimientos() {
    return this.movimientoViandas;
  }
}
