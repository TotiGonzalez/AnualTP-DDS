package ar.edu.utn.frba.dds.model.excepciones;

public class TelegramException extends RuntimeException{
  public TelegramException(String message) {
    super(message);
  }
}
