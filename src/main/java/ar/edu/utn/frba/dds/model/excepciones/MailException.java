package ar.edu.utn.frba.dds.model.excepciones;

public class MailException extends RuntimeException {
  public MailException(String message) {
    super(message);
  }
}
