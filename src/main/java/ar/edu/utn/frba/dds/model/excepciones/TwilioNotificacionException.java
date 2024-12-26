package ar.edu.utn.frba.dds.model.excepciones;

public class TwilioNotificacionException extends RuntimeException{
  public TwilioNotificacionException(String message) {
    super(message);
  }
}
