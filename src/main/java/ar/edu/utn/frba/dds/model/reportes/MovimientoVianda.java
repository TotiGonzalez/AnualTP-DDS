package ar.edu.utn.frba.dds.model.reportes;

import ar.edu.utn.frba.dds.db.EntidadPersistente;
import ar.edu.utn.frba.dds.db.convertidores.ConvertidorLocalDateTime;

import javax.persistence.*;
import java.time.LocalDateTime;

// es una clase para mostrar el movimiento de las viandas ingreso/retiro
@Entity
@Table(name = "movimiento_vianda")
public class MovimientoVianda extends EntidadPersistente {

  @Column(name = "nombre_heladera_asociada")
  private String heladeraAsociada;

  @Column(name = "cantidad_viandas")
  private Integer cantidadViandas;

  @Column(name = "tipo_movimiento")
  @Enumerated(EnumType.STRING)
  private TipoMovimiento tipoMovimiento;

  @Convert(converter = ConvertidorLocalDateTime.class)
  @Column(name = "fecha_y_hora")
  private LocalDateTime fechaYhora;

  public MovimientoVianda(String heladeraAsociada,
                          Integer cantidadViandas, TipoMovimiento tipoMovimiento,
                          LocalDateTime fechaYhora) {
    this.heladeraAsociada = heladeraAsociada;
    this.cantidadViandas = cantidadViandas;
    this.tipoMovimiento = tipoMovimiento;
    this.fechaYhora = fechaYhora;
  }

  public MovimientoVianda() {

  }

  public LocalDateTime getFechaYhora() {
    return this.fechaYhora;
  }
}
