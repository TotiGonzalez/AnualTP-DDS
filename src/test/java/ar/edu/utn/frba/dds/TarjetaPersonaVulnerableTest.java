package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.model.colaboradores.ColaboradorFisico;
import ar.edu.utn.frba.dds.model.colaboradores.Credencial;
import ar.edu.utn.frba.dds.model.colaboradores.PersonaVulnerable;
import ar.edu.utn.frba.dds.model.colaboradores.medioscontacto.Mail;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Direccion;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Documento;
import ar.edu.utn.frba.dds.model.tarjetas.RepoTarjetas;
import ar.edu.utn.frba.dds.model.tarjetas.TarjetaPersonaVulnerable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class TarjetaPersonaVulnerableTest {
  TarjetaPersonaVulnerable tarjetaPersonaVulnerable;
  RepoTarjetas repositorioTarjetas;

  @BeforeEach
  public void setUp() throws Exception {
    Direccion direccion = new Direccion("CABA","Almagro", "falsa", 123);
    ColaboradorFisico colaborador = generarColaborador(Documento.DNI,123456777,
        "Juan","Gomez","jgomez@test.com");
    colaborador.agregarMedioContacto(new Mail("jgomez@test.com"));

    PersonaVulnerable personaVulnerable = new PersonaVulnerable(
        "Juan", LocalDate.of(2000,1,1)
        ,true,direccion, Documento.DNI
        ,123111111,false,0,colaborador);

    repositorioTarjetas = RepoTarjetas.getInstance();
    tarjetaPersonaVulnerable = repositorioTarjetas.crearTarjetaPersonaVulnerable();
    tarjetaPersonaVulnerable.setTitularTarjeta(personaVulnerable);
  }

  public ColaboradorFisico generarColaborador(Documento documento, Integer valorDocumento, String nombreDeColaborador,
                                              String apellidoDeColaborador, String mailDeColaborador){

    ColaboradorFisico colaborador = new ColaboradorFisico(nombreDeColaborador,apellidoDeColaborador
        ,LocalDate.of(2000,1,1));
    Credencial credencial = new Credencial(valorDocumento.toString(),"!"+nombreDeColaborador
        .substring(3)+"L@Sn10"+apellidoDeColaborador.substring(2));
    colaborador.setCredencial(credencial);
    credencial.hashearContrasenia(credencial.getContrasenia());

    colaborador.setTipoDocumento(documento);
    colaborador.setNumeroDocumento(valorDocumento);
    colaborador.agregarMedioContacto(new Mail(mailDeColaborador));

    return colaborador;
  }

  @Test
  public void registrarUsoTest(){
    tarjetaPersonaVulnerable.registrarUso("heladera1");
    tarjetaPersonaVulnerable.registrarUso("heladera2");
    tarjetaPersonaVulnerable.registrarUso("heladera3");
    Assertions.assertEquals(3, tarjetaPersonaVulnerable.getCantUsos());
  }
}
