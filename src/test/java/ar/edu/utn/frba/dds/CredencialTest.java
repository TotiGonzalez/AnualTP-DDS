package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.model.colaboradores.Credencial;
import ar.edu.utn.frba.dds.model.excepciones.ValidarRotacionException;
import ar.edu.utn.frba.dds.model.validador.validacionescontrasenia.ValidacionDeContrasenia;
import ar.edu.utn.frba.dds.model.validador.validacionescontrasenia.ValidacionComplejidad;
import ar.edu.utn.frba.dds.model.validador.validacionescontrasenia.Validacion;
import ar.edu.utn.frba.dds.model.validador.validacionescontrasenia.ValidacionFortaleza;
import ar.edu.utn.frba.dds.model.validador.validacionescontrasenia.ValidacionLongitud;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class CredencialTest {
  ValidacionDeContrasenia validadorDeContrasenia;
  Credencial credencial;
  @BeforeEach
  public void init() {
    List<Validacion> validadores = Arrays.asList(
        new ValidacionFortaleza("src/main/resources/10k-worst-passwords.txt"),
        new ValidacionLongitud(8,64),
        new ValidacionComplejidad()
        );
   validadorDeContrasenia = new ValidacionDeContrasenia(validadores);
   credencial = new Credencial("Juan1","9@SeAprueba9!");

  }

  @Test
  public void esExpirada_contraseniaEsAntigua() {
    credencial.setFechaCambioContrasenia(LocalDate.now().minusDays(365));
    Assertions.assertThrows(ValidarRotacionException.class,() -> credencial.esExpirada(30)
       );
  }
  @Test
  public void esExpirada_contraseniaNoEsAntigua() {
    Assertions.assertDoesNotThrow(() -> credencial.esExpirada(30)
    );
  }

  @Test
  public void cambiarContrasenia_contraseniaSeRota() {
    credencial.cambiarContrasenia("!Exit05Muchos.",validadorDeContrasenia);

    Assertions.assertTrue(credencial.contraseniaEsCorrecta("!Exit05Muchos."));
  }

  @Test
  public void cambiarContrasenia_contraseniaSeRota_esIncorrecta() {
    credencial.cambiarContrasenia("!Exit05Muchos.",validadorDeContrasenia);

    Assertions.assertFalse(credencial.contraseniaEsCorrecta("!Exit05Muchosss."));
  }

}
