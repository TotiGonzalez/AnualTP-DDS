package ar.edu.utn.frba.dds.model.validador.validacionescontrasenia;


public class ValidacionLongitud implements Validacion {
  private final int longitudMinima;
  private final int longitudMaxima;

  public ValidacionLongitud(int longitudMinima, int longitudMaxima) {
    this.longitudMinima = longitudMinima;
    this.longitudMaxima = longitudMaxima;
  }

  public String validar(String contrasenia) {

    if (contrasenia.length() < longitudMinima) {
      return ("La contraseña debe tener más de "
          + longitudMinima + " caracteres");
    } else if (contrasenia.length() > longitudMaxima) {
      return ("La contraseña debe tener menos  de "
          + longitudMaxima + " caracteres");
    }
    return "";
  }

}
