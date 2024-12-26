package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.model.colaboraciones.RegistracionPersonaVulnerable;
import ar.edu.utn.frba.dds.model.colaboraciones.RegistracionPersonaVulnerableBuilder;
import ar.edu.utn.frba.dds.model.colaboradores.ColaboradorFisico;
import ar.edu.utn.frba.dds.model.colaboradores.ColaboradorJuridico;

import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Direccion;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Documento;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.TipoJuridico;

import ar.edu.utn.frba.dds.model.excepciones.VariableNullException;
import ar.edu.utn.frba.dds.model.tarjetas.RepoTarjetas;
import ar.edu.utn.frba.dds.model.tarjetas.TarjetaPersonaVulnerable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class RegistracionPersonaVulnerableTest {
  ColaboradorFisico colaboradorFisico;
  ColaboradorJuridico colaboradorJuridico;
  RepoTarjetas repositorioTarjetas;

  @BeforeEach
  public void setUp() {
    repositorioTarjetas = RepoTarjetas.getInstance();
    colaboradorFisico = new ColaboradorFisico("Pedro", "Hernandez", LocalDate.of(2000, 9, 17));
    colaboradorJuridico = new ColaboradorJuridico(TipoJuridico.EMPRESA, "S.A.", "Alimentos");
  }


  @Test
  public void siColaboradorJuridicoIntentaRecibirTarjetasUnaExcepcionEsLanzada() {
    Assertions.assertThrows(RuntimeException.class, () -> colaboradorJuridico.recibeTarjetasPersonaVulnerable(listaDeTarjetas()));
  }

  @Test
  public void siUnColaboradorIntentaIniciarUnBuilderSinPoseerTarjetasUnaExcepcionEsLanzada() {
    Assertions.assertThrows(RuntimeException.class, () -> colaboradorFisico.iniciarPersonaVulnerableBuilder());
    Assertions.assertThrows(RuntimeException.class, () -> colaboradorJuridico.iniciarPersonaVulnerableBuilder());
  }

  @Test
  public void sePuedeInstanciarPersonaVulnerableConBuilder() {
    colaboradorFisico.setDireccion(unaDireaccionCualquiera());
    colaboradorFisico.recibeTarjetasPersonaVulnerable(listaDeTarjetas());

    RegistracionPersonaVulnerableBuilder builder = colaboradorFisico.iniciarPersonaVulnerableBuilder();

    builder.setNombre("Julio");
    builder.setFechaNacimiento(LocalDate.of(1997, 1, 1));
    builder.setTieneMenoresBajoTutela(false);
    builder.setSituacionDeCalle(true);
    builder.setTipoDocumento(Documento.DNI);
    builder.setNumeroDocumento(39484231);
    builder.setPrimeraTarjetaDisponible();
    RegistracionPersonaVulnerable registracion = builder.generarLaRegistracion();

    Assertions.assertEquals("Julio", registracion.personaVulnerable.getNombre());
  }

  @Test
  public void sePuedeInstanciarPersonaVulnerableConHijosAdemasDeDireccion() {
    colaboradorFisico.setDireccion(unaDireaccionCualquiera());
    colaboradorFisico.recibeTarjetasPersonaVulnerable(listaDeTarjetas());

    RegistracionPersonaVulnerableBuilder builder = colaboradorFisico.iniciarPersonaVulnerableBuilder();

    builder.setNombre("Julio");
    builder.setFechaNacimiento(LocalDate.of(1997, 1, 1));
    builder.setTieneMenoresBajoTutela(true);
    builder.setCantMenoresBajoTutela(2);
    builder.setSituacionDeCalle(false);
    builder.setDireccion(otraDireaccionCualquiera());
    builder.setTipoDocumento(Documento.DNI);
    builder.setNumeroDocumento(39484231);
    builder.setPrimeraTarjetaDisponible();
    RegistracionPersonaVulnerable registracion = builder.generarLaRegistracion();

    Assertions.assertEquals(2 , registracion.personaVulnerable.getCantHijos());
    Assertions.assertEquals("Gral Ferré", registracion.personaVulnerable.getDireccion().getCalle());
    Assertions.assertNotNull(registracion.personaVulnerable.getDireccion().getAltura());
    Assertions.assertNotNull(registracion.personaVulnerable.getDireccion().getLocalidad());
    Assertions.assertNotNull( registracion.personaVulnerable.getDireccion().getCiudad());
  }

  @Test
  public void siSeIntentaSettearNumeroDocumentoSinSettearPreviamenteElTipoUnaExcepcionEsLanzada() {
    colaboradorFisico.setDireccion(unaDireaccionCualquiera());
    colaboradorFisico.recibeTarjetasPersonaVulnerable(listaDeTarjetas());

    RegistracionPersonaVulnerableBuilder builder = colaboradorFisico.iniciarPersonaVulnerableBuilder();

    Assertions.assertThrows(RuntimeException.class, () -> builder.setNumeroDocumento(12345678));
  }

  @Test
  public void siLaGeneracionDeLaRegistracionFallaPorFaltaDeNombreUnaExcepcionEsLanzada() {
    colaboradorFisico.setDireccion(unaDireaccionCualquiera());
    colaboradorFisico.recibeTarjetasPersonaVulnerable(listaDeTarjetas());

    RegistracionPersonaVulnerableBuilder builder = colaboradorFisico.iniciarPersonaVulnerableBuilder();

    builder.setFechaNacimiento(LocalDate.of(1997, 1, 1));
    builder.setTieneMenoresBajoTutela(false);
    builder.setSituacionDeCalle(true);
    builder.setTipoDocumento(Documento.DNI);
    builder.setNumeroDocumento(39484231);
    builder.setPrimeraTarjetaDisponible();

    Assertions.assertThrows(VariableNullException.class, builder::generarLaRegistracion);
  }

  @Test
  public void elNombreEnLaRegistracionPersonaVulnerableEnColaboracionesDelColaboradorSeraElEsperado() {
    colaboradorFisico.setDireccion(unaDireaccionCualquiera());
    colaboradorFisico.recibeTarjetasPersonaVulnerable(listaDeTarjetas());

    RegistracionPersonaVulnerableBuilder builder = colaboradorFisico.iniciarPersonaVulnerableBuilder();

    builder.setNombre("Julio");
    builder.setFechaNacimiento(LocalDate.of(1997, 1, 1));
    builder.setTieneMenoresBajoTutela(false);
    builder.setSituacionDeCalle(true);
    builder.setTipoDocumento(Documento.DNI);
    builder.setNumeroDocumento(39484231);
    builder.setPrimeraTarjetaDisponible();

    colaboradorFisico.registrarPersonaVulnerable(builder);

    Assertions.assertTrue(colaboradorFisico.getColaboraciones().stream()
        .anyMatch(colaboracion -> colaboracion instanceof RegistracionPersonaVulnerable &&
            ((RegistracionPersonaVulnerable) colaboracion).getPersonaVulnerable().getNombre().equals("Julio")));
  }


  private List<TarjetaPersonaVulnerable> listaDeTarjetas() {
    TarjetaPersonaVulnerable tarjetaPersonaVulnerable1 = repositorioTarjetas.crearTarjetaPersonaVulnerable();
    TarjetaPersonaVulnerable tarjetaPersonaVulnerable2 = repositorioTarjetas.crearTarjetaPersonaVulnerable();
    TarjetaPersonaVulnerable tarjetaPersonaVulnerable3 = repositorioTarjetas.crearTarjetaPersonaVulnerable();

    List<TarjetaPersonaVulnerable> tarjetaPersonaVulnerables = new ArrayList<>();
    tarjetaPersonaVulnerables.add(tarjetaPersonaVulnerable1);
    tarjetaPersonaVulnerables.add(tarjetaPersonaVulnerable2);
    tarjetaPersonaVulnerables.add(tarjetaPersonaVulnerable3);

    return tarjetaPersonaVulnerables;
  }

  private Direccion unaDireaccionCualquiera() {
    return new Direccion("CABA", "Caballito", "Bogotá", 43);
  }

  private Direccion otraDireaccionCualquiera() {
    return new Direccion("Avellaneda", "Sarandi", "Gral Ferré", 211);
  }
}