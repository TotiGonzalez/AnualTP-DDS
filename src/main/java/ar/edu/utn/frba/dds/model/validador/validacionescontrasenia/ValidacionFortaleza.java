package ar.edu.utn.frba.dds.model.validador.validacionescontrasenia;

import ar.edu.utn.frba.dds.model.excepciones.LecturaDeArchivoException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ValidacionFortaleza implements Validacion {
  private final List<String> contraseniasComunes = new ArrayList<>();

  public ValidacionFortaleza(String rutaTopContraseniasComunes) {
    this.obtenerContraseniasComunes(rutaTopContraseniasComunes);
  }

  public void obtenerContraseniasComunes(String rutaTopContraseniasComunes) {
    Scanner leido;
    try {
      leido = new Scanner(new InputStreamReader(new FileInputStream(
          rutaTopContraseniasComunes), StandardCharsets.UTF_8));
    } catch (FileNotFoundException e) {
      throw new LecturaDeArchivoException("No se ha podido leer el archivo de contraseñas");
    }
    while (leido.hasNextLine()) {
      contraseniasComunes.add(leido.nextLine());
    }
    leido.close();
  }

  public String validar(String contrasenia) {
    if (contraseniasComunes.contains(contrasenia)) {
      return "La contraseña es débil";
    }
    return "";
  }
}
