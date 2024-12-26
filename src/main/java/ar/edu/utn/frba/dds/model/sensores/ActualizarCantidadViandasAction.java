package ar.edu.utn.frba.dds.model.sensores;

import ar.edu.utn.frba.dds.model.heladeras.Heladera;
import ar.edu.utn.frba.dds.model.reportes.MovimientoVianda;
import ar.edu.utn.frba.dds.model.reportes.TipoMovimiento;
import java.time.LocalDateTime;

public class ActualizarCantidadViandasAction implements LunchBoxAction {
  private Heladera heladera;

  public ActualizarCantidadViandasAction(Heladera heladera) {
    this.heladera = heladera;
  }

  @Override
  public void onLunchBoxChanged(int nuevaCantidad) {
    int cantidadAnterior = heladera.getCantidadViandas();
    if (nuevaCantidad > cantidadAnterior) {
      int diferencia = nuevaCantidad - cantidadAnterior;
      MovimientoVianda movimiento = new MovimientoVianda(heladera.getNombreHeladera(),
          diferencia, TipoMovimiento.INGRESO, LocalDateTime.now());
      heladera.agregarMovimientoVianda(movimiento);
    } else if (nuevaCantidad < cantidadAnterior) {
      int diferencia = cantidadAnterior - nuevaCantidad;
      MovimientoVianda movimiento = new MovimientoVianda(heladera.getNombreHeladera(),
          diferencia, TipoMovimiento.RETIRO, LocalDateTime.now());
      heladera.agregarMovimientoVianda(movimiento);
    }
  }
}
