package ar.edu.utn.frba.dds.model.heladeras;

public class ControladorDeAccesoHeladera implements ControladorDeAccesoInterno {
  private ControladorDeAcceso controladorDeAccesoExterno;

  public ControladorDeAccesoHeladera(ControladorDeAcceso controladorDeAccesoExterno) {
    this.controladorDeAccesoExterno = controladorDeAccesoExterno;
  }

  @Override
  public void notificarTarjetaHabilitada(String numeroDeTarjeta) {
    controladorDeAccesoExterno.notificarTarjetasColaboradoraHabilitada(numeroDeTarjeta);
  }
}
