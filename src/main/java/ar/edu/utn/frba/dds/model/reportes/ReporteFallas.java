package ar.edu.utn.frba.dds.model.reportes;

import ar.edu.utn.frba.dds.model.heladeras.FallaTecnica;
import ar.edu.utn.frba.dds.model.heladeras.MapaHeladeras;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

// Heladera 1 tuvo 4 fallas
// Heladera 2 tuvo 2 fallas
// una vez a la semana recompilo lo que paso en la semana

@Entity
@Table(name = "reporte_fallas")
public class ReporteFallas extends GeneracionReportes {

  //una falla va a estar en 1 semana, cada falla es unica, no puede estar en 2 reportes
  @OneToMany
  @JoinColumn(name = "reporte_fallas_id", referencedColumnName = "id")
  private List<FallaTecnica> fallas = new ArrayList<>();

  public List<FallaTecnica> generarReporteFallasEstaSemana() {
    MapaHeladeras mapaHeladeras = MapaHeladeras.getInstance();
    return mapaHeladeras.getHeladeras().stream()
        .flatMap(heladera -> heladera.getFallaTecnicas().stream())
        .filter(falla -> this.dentroDeEstaSemana(falla.getFechaYhora()))
        .toList();
  }

  @Override
  public void generar() {
    fallas.clear();
    this.fallas = this.generarReporteFallasEstaSemana();
  }

  public List<FallaTecnica> getFallas() {
    return fallas;
  }
}
