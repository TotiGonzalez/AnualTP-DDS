package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.model.reportes.ReporteFallas;
import ar.edu.utn.frba.dds.model.reportes.ReporteViandasPorColaborador;
import ar.edu.utn.frba.dds.model.reportes.ReporteViandasPorHeladera;
import java.time.LocalDateTime;

public class Main {
  public static void main(String[] args) {
    ReporteFallas reporteFallas = new ReporteFallas();
    ReporteViandasPorHeladera reporteViandasPorHeladera = new ReporteViandasPorHeladera();
    ReporteViandasPorColaborador reporteViandasPorColaborador = new ReporteViandasPorColaborador();

    LocalDateTime now = LocalDateTime.now();

    if (reporteFallas.dentroDeEstaSemana(now)
        && reporteViandasPorHeladera.dentroDeEstaSemana(now)
        && reporteViandasPorColaborador.dentroDeEstaSemana(now)) {
      reporteFallas.generar();
      reporteViandasPorColaborador.generar();
      reporteViandasPorHeladera.generar();
    }
  }
}
