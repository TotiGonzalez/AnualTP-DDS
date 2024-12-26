package ar.edu.utn.frba.dds.model.excepciones;

public class SolicitudNoEncontradaException extends RuntimeException {
  public SolicitudNoEncontradaException(String message) {
    super(message);
  }
}
