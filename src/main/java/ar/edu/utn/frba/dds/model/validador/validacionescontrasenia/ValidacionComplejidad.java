package ar.edu.utn.frba.dds.model.validador.validacionescontrasenia;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidacionComplejidad implements Validacion {
  public String validar(String contrasenia) {
    // Contraseña debe contener al menos un número, una mayúscula,
    // una minúscula y un caracter especial y una longitud mínima de ocho caracteres
    String regex = "(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[!~<>,;:_=?*+#.\"&§%°()|\\[\\]\\-$^@/])";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(contrasenia);
    if (!matcher.find()) {
      return "La contraseña debe tener al menos un número,"
          + " una mayúscula, una minúscula y un caracter especial";
    }
    return "";
  }
}
