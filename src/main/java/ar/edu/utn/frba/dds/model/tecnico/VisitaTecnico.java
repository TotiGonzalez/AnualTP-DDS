package ar.edu.utn.frba.dds.model.tecnico;

import ar.edu.utn.frba.dds.db.EntidadPersistente;
import ar.edu.utn.frba.dds.db.convertidores.ConvertidorLocalDateTime;
import ar.edu.utn.frba.dds.model.heladeras.Heladera;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "visita_tecnico")
public class VisitaTecnico extends EntidadPersistente {
  @Convert(converter = ConvertidorLocalDateTime.class)
  @Column(name = "fecha_visita")
  private LocalDateTime fechaVisita;

  private String descripcion;
  private String foto;
  @Column(name = "incidente_resuelto")
  private boolean incidenteResuelto;

  @OneToOne
  @JoinColumn(name = "visita_tecnico_id")
  private Heladera heladeraFallida;

  public VisitaTecnico(LocalDateTime fechaVisita, Heladera heladeraInvolucrada,
                       String descripcion, String foto,
                       boolean incidenteResuelto) {
    this.fechaVisita = fechaVisita;
    this.incidenteResuelto = incidenteResuelto;
    this.heladeraFallida = heladeraInvolucrada;
  }

  public VisitaTecnico() {

  }

  public void isSolucionado() {
    if (incidenteResuelto) {
      heladeraFallida.ponerEnFuncionamiento();
    }
  }
}
