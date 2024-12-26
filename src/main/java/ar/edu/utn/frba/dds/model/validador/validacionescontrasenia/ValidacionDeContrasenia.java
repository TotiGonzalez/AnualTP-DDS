package ar.edu.utn.frba.dds.model.validador.validacionescontrasenia;

import java.util.ArrayList;
import java.util.List;

public class ValidacionDeContrasenia {
  private final List<Validacion> validadores;

  public ValidacionDeContrasenia(List<Validacion> validadores) {
    this.validadores = validadores;
  }

  public List<String> validarContrasenia(String contrasenia) {
    List<String> errores = new ArrayList<>();
    if (validadores != null) {
      validadores.forEach(validador -> errores.add(validador.validar(contrasenia)));
    } else {
      throw new RuntimeException("Inicializar la lista de validadores");
    }
    return errores;
  }

}
