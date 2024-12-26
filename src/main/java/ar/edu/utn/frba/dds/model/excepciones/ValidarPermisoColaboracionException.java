package ar.edu.utn.frba.dds.model.excepciones;

public class ValidarPermisoColaboracionException extends RuntimeException {
  public ValidarPermisoColaboracionException(String mensaje) {
    super(mensaje);
  }
}