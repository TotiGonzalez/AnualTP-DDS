package ar.edu.utn.frba.dds.model.heladeras;
import ar.edu.utn.frba.dds.db.convertidores.ConvertidorLocalDateTime;
import ar.edu.utn.frba.dds.db.EntidadPersistente;

import javax.persistence.*;
import java.time.LocalDateTime;

//no es embbeded porque si tengo 2 alertas en el mismo horario para la misma heladera
//no significa que las 2 sean iguales. Son 2 alertas distintas con la misma fecha y hora
@Entity
public class AlertaHeladera extends EntidadPersistente {

  @Convert(converter = ConvertidorLocalDateTime.class)
  @Column(name = "fecha_y_hora")
  private LocalDateTime fechaYhora;

  @Enumerated(EnumType.STRING)
  private TipoIncidente tipoIncidente;

  public AlertaHeladera(LocalDateTime fechaYhora, TipoIncidente tipoIncidente) {
    this.fechaYhora = fechaYhora;
    this.tipoIncidente = tipoIncidente;
  }

  public AlertaHeladera() {}
}
