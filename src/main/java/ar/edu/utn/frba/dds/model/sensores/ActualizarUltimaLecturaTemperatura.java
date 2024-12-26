package ar.edu.utn.frba.dds.model.sensores;

import ar.edu.utn.frba.dds.model.heladeras.EstadoHeladera;
import ar.edu.utn.frba.dds.model.heladeras.Heladera;

//ConcreteCommand : implements Execute by invoking the corresponding operation(s) on Receiver
public class ActualizarUltimaLecturaTemperatura implements Action {

  private Heladera heladera;

  public ActualizarUltimaLecturaTemperatura(Heladera heladera) {
    this.heladera = heladera;
  }

  @Override
  public void executeForTemperature(double temperature) {
    heladera.actualizarUltimasTresTemperaturas(temperature);

    if (heladera.necesitaAtencion()) {
      heladera.setEstadoHeladera(EstadoHeladera.NECESITA_ATENCION);
    }
  }

}
