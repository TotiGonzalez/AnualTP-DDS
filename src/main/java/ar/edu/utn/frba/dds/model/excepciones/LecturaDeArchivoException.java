package ar.edu.utn.frba.dds.model.excepciones;

public class LecturaDeArchivoException extends RuntimeException {
  public LecturaDeArchivoException(String mensaje) {
    super(mensaje);
  }
}
