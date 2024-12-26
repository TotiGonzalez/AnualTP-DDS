package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.model.colaboradores.Credencial;
import ar.edu.utn.frba.dds.model.excepciones.LecturaDeArchivoException;
import ar.edu.utn.frba.dds.model.validador.validacionescontrasenia.Validacion;
import ar.edu.utn.frba.dds.model.validador.validacionescontrasenia.ValidacionComplejidad;
import ar.edu.utn.frba.dds.model.validador.validacionescontrasenia.ValidacionDeContrasenia;
import ar.edu.utn.frba.dds.model.validador.validacionescontrasenia.ValidacionFortaleza;
import ar.edu.utn.frba.dds.model.validador.validacionescontrasenia.ValidacionLongitud;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class ValidadorContraseniaTest {
  List<Validacion> validadores;

  @Test
  public void validarContrasenia_contraseniaEsValida(){
    validadores = Arrays.asList(
        new ValidacionFortaleza("src/main/resources/10k-worst-passwords.txt"),
        new ValidacionLongitud(8,64),
        new ValidacionComplejidad()
    );

    Credencial credencial = new Credencial("Juan1","9@SeAprueba9!");
    Assertions.assertEquals(0, credencial.validarContrasenia(credencial.getContrasenia()
        ,new ValidacionDeContrasenia(validadores)).size());
  }

  @Test
  public void validarContrasenia_contraseniaNoEsCompleja(){
    validadores = Arrays.asList(
        new ValidacionComplejidad()
    );
    Credencial credencial = new Credencial("Florencia1","SeAprueba!");
    Assertions.assertEquals(1, credencial.validarContrasenia(
        credencial.getContrasenia(),new ValidacionDeContrasenia(validadores)).size());
  }

  @Test
  public void validarContrasenia_contraseniaEsDebil(){
    validadores = Arrays.asList(
        new ValidacionFortaleza("src/main/resources/10k-worst-passwords.txt")
    );
    Credencial credencial = new Credencial("Juana2","123456");
    Assertions.assertEquals(1, credencial.validarContrasenia(
        credencial.getContrasenia(),new ValidacionDeContrasenia(validadores)).size());
  }

  @Test
  public void validarContrasenia_contraseniaEsCorta() {
    validadores = Arrays.asList(
        new ValidacionLongitud(8,64)
    );
    Credencial credencial = new Credencial("Juana1","Hm9!");
    Assertions.assertNotEquals("",credencial.validarContrasenia(
        credencial.getContrasenia(),new ValidacionDeContrasenia(validadores)));
    Assertions.assertEquals(1, credencial.validarContrasenia(
        credencial.getContrasenia(),new ValidacionDeContrasenia(validadores)).size());
  }

  @Test
  public void hashearContrasenia_contraseniaEsCorrecta() {
    validadores = Arrays.asList(
        new ValidacionFortaleza("src/main/resources/10k-worst-passwords.txt"),
        new ValidacionLongitud(8,64),
        new ValidacionComplejidad()
    );
    Credencial credencial = new Credencial("Juana1","Hm9!cWii@L");
    Assertions.assertEquals(0, credencial.validarContrasenia(
        credencial.getContrasenia(),new ValidacionDeContrasenia(validadores)).size());
    credencial.hashearContrasenia(credencial.getContrasenia());
    Assertions.assertNotEquals("Hm9!cWii@L",credencial.getContrasenia());
    Assertions.assertTrue(credencial.contraseniaEsCorrecta("Hm9!cWii@L"));
  }

  @Test
  public void hashearContrasenia_contraseniaEsIncorrecta() {
    validadores = Arrays.asList(
        new ValidacionFortaleza("src/main/resources/10k-worst-passwords.txt"),
        new ValidacionLongitud(8,64),
        new ValidacionComplejidad()
    );
    Credencial credencial = new Credencial("Juana1","Hm9!cWii@L");
    Assertions.assertEquals(0, credencial.validarContrasenia(
        credencial.getContrasenia(),new ValidacionDeContrasenia(validadores)).size());
    credencial.hashearContrasenia(credencial.getContrasenia());
    Assertions.assertFalse(credencial.contraseniaEsCorrecta("NoEsCorrectaa!"));
  }

  @Test
  public void validarContraseña_archivoNoExiste() {
    Assertions.assertThrows(LecturaDeArchivoException.class, ()-> new ValidacionFortaleza
        ("src/main/resources/archivo que no existe :).txt"));
  }

}
