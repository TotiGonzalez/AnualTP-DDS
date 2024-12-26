package ar.edu.utn.frba.dds.model.excepciones;

public class VariableNullException extends RuntimeException {
  public VariableNullException(String mensaje) {
    super(mensaje);
  }
}
