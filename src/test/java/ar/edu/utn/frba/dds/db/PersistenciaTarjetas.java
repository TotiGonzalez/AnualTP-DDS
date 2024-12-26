package ar.edu.utn.frba.dds.db;

import ar.edu.utn.frba.dds.model.colaboradores.ColaboradorFisico;
import ar.edu.utn.frba.dds.model.colaboradores.Credencial;
import ar.edu.utn.frba.dds.model.colaboradores.PersonaVulnerable;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Direccion;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Documento;
import ar.edu.utn.frba.dds.model.colaboradores.medioscontacto.Mail;
import ar.edu.utn.frba.dds.model.tarjetas.RepoTarjetas;
import ar.edu.utn.frba.dds.model.tarjetas.TarjetaPersonaVulnerable;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersistenciaTarjetas implements SimplePersistenceTest{
  TarjetaPersonaVulnerable tarjetaPersonaVulnerable;
  TarjetaPersonaVulnerable tarjeta2;
  TarjetaPersonaVulnerable tarjeta3;
  RepoTarjetas repositorioTarjetas = new RepoTarjetas();

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

    PersonaVulnerable personaVulnerable2 = new PersonaVulnerable(
        "Juana", LocalDate.of(2002,3,7)
        ,true,direccion, Documento.DNI
        ,77777777,false,0,colaborador);

    tarjetaPersonaVulnerable = repositorioTarjetas.crearTarjetaPersonaVulnerable();
    tarjetaPersonaVulnerable.setTitularTarjeta(personaVulnerable);

    tarjeta2 = repositorioTarjetas.crearTarjetaPersonaVulnerable();
    tarjeta2.setTitularTarjeta(personaVulnerable2);

    tarjeta3 = repositorioTarjetas.crearTarjetaPersonaVulnerable();
    tarjeta3.setTitularTarjeta(personaVulnerable2);


   // EntityTransaction tx = repositorioTarjetas.getTransaction();
    //tx.begin();

    repositorioTarjetas.registrar(tarjetaPersonaVulnerable);
    repositorioTarjetas.registrar(tarjeta2);
    repositorioTarjetas.registrar(tarjeta3);

    //tx.commit();
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
  public void sePersistieronLasTarjetas() {
    assertEquals(3, repositorioTarjetas.buscarTodos().size());
  }

  @Test
  public void eliminarTarjeta() {
    repositorioTarjetas.eliminar(tarjetaPersonaVulnerable);

    assertEquals(2, repositorioTarjetas.buscarTodos().size());
  }

  @Test
  public void buscarTarjeta() {
    assertEquals(tarjetaPersonaVulnerable,
        repositorioTarjetas.buscar(tarjetaPersonaVulnerable.getId()));
  }
}
