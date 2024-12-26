package ar.edu.utn.frba.dds.db;

import ar.edu.utn.frba.dds.model.colaboradores.ColaboradorFisico;
import ar.edu.utn.frba.dds.model.colaboradores.Credencial;
import ar.edu.utn.frba.dds.model.colaboradores.medioscontacto.Mail;
import ar.edu.utn.frba.dds.model.colaboradores.RepoColaboradores;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Documento;
import ar.edu.utn.frba.dds.model.importador.ImportadorColaboracion;
import ar.edu.utn.frba.dds.model.validador.validacionescontrasenia.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ImportadorTestDB {
  ValidacionDeContrasenia validadorDeContrasenia;
  RepoColaboradores repoColaboradores;
  Map<String,List<String>> resultado;
  ImportadorColaboracion importador;

  @BeforeEach
  public void init() {
    List<Validacion> validadores = Arrays.asList(
        new ValidacionFortaleza("src/main/resources/10k-worst-passwords.txt"),
        new ValidacionLongitud(8, 64),
        new ValidacionComplejidad()
    );
    validadorDeContrasenia = new ValidacionDeContrasenia(validadores);

    importador = new ImportadorColaboracion();
    repoColaboradores =  RepoColaboradores.getInstance();

  }

  public ColaboradorFisico generarColaborador(Documento documento, Integer valorDocumento, String nombreDeColaborador,
                                              String apellidoDeColaborador, String mailDeColaborador, RepoColaboradores repoColaboradores){

    ColaboradorFisico colaborador = (ColaboradorFisico) repoColaboradores.registrarColaboradorFisico("Juan","Gomez",LocalDate.of(2000,1,1));
    Credencial credencial = new Credencial(valorDocumento.toString(),"!"+nombreDeColaborador.substring(3)+"L@Sn10"+apellidoDeColaborador.substring(2));
    System.out.println(credencial.getContrasenia());

    credencial.hashearContrasenia(credencial.getContrasenia());
    colaborador.setCredencial(credencial);

    colaborador.setTipoDocumento(documento);
    colaborador.setNumeroDocumento(valorDocumento);
    colaborador.agregarMedioContacto(new Mail(mailDeColaborador));

    return colaborador;
  }

  @Test
  public void importarColaboracionesUsuarioExiste() {
    generarColaborador(Documento.DNI,41710110,"Juan","Gomez","jgomez@test.com",repoColaboradores);
    resultado = importador.importarColaboracion(repoColaboradores, "src/main/resources/colaboraciones.csv", true);
    /*Assertions.assertNotNull(repoColaboradores.buscarColaboradorFisico(Documento.DNI,41710110));
    Assertions.assertEquals(0, resultado.get("Errores").size());
    Assertions.assertEquals(3, resultado.get("Colaboraciones").size());*/
  }
}
