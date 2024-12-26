package ar.edu.utn.frba.dds.model.excepciones;

public class SolicitudExpiradaException extends RuntimeException {
  public SolicitudExpiradaException(String message) {
    super(message);
  }
}
