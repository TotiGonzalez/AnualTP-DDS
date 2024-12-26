package ar.edu.utn.frba.dds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.edu.utn.frba.dds.model.colaboradores.ColaboradorFisico;
import ar.edu.utn.frba.dds.model.colaboradores.ColaboradorJuridico;
import ar.edu.utn.frba.dds.model.colaboradores.RepoColaboradores;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Direccion;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.TipoJuridico;
import ar.edu.utn.frba.dds.model.excepciones.ValidarPermisoUsoTarjetasException;
import ar.edu.utn.frba.dds.model.tarjetas.RepoTarjetas;
import ar.edu.utn.frba.dds.model.tarjetas.TarjetaColaborador;
import ar.edu.utn.frba.dds.model.tarjetas.solicitudes.RepoSolicitudesTarjetas;
import ar.edu.utn.frba.dds.model.tarjetas.solicitudes.SolicitudTarjeta;
import ar.edu.utn.frba.dds.model.tarjetas.solicitudes.TipoSolicitudTarjeta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;

public class SolicitudTarjetasTest {
  RepoTarjetas repositorioTarjetas;
  RepoSolicitudesTarjetas repoSolicitudesTarjetas;
  RepoColaboradores repoColaboradores;
  ColaboradorFisico colaboradorFisico;
  ColaboradorFisico colaboradorFisico2;
  ColaboradorJuridico colaboradorJuridico;

  @BeforeEach
  public void setUp() {
    repositorioTarjetas = RepoTarjetas.getInstance();
    repoSolicitudesTarjetas = RepoSolicitudesTarjetas.getInstance();
    repoColaboradores = RepoColaboradores.getInstance();
    colaboradorFisico = new ColaboradorFisico("Pedro", "Hernandez", LocalDate.of(2000, 9, 17));
    colaboradorFisico.setDireccion(new Direccion("CABA", "Caballito", "Bogotá", 43));
    colaboradorJuridico = new ColaboradorJuridico(TipoJuridico.EMPRESA, "S.A.", "Alimentos");
    colaboradorJuridico.setDireccion(new Direccion("Avellaneda", "Sarandi", "Gral Ferré", 211));
    colaboradorFisico2 = new ColaboradorFisico("José", "Pineda", LocalDate.of(1993, 5, 3));

    repoSolicitudesTarjetas.limpiarRepositorio();
  }

  @Test
  public void laSolicitudTarjetaPersonalSeGuardaCorrectamente() {
    colaboradorFisico.solicitarTarjetaPersonal();
    List<SolicitudTarjeta> solicitudes = repoSolicitudesTarjetas.getSolicitudes();
    assertEquals(TipoSolicitudTarjeta.COLABORADOR, solicitudes.get(0).getTipoSolicitudTarjeta());
    assertEquals(
        colaboradorFisico.getHistorialSolicitudesTarjetas().get(0).getDireccion().getCalle(),
        "Bogotá");
    assertEquals(
        solicitudes.get(0).getDireccion().getCalle(),
        "Bogotá");
  }


  @Test
  public void laSolicitudDeTarjetasDePersonasVulnerablesSeGuardaCorrectamente() {
    colaboradorFisico.solicitarTarjetasPersonasVulnerables(10);
    List<SolicitudTarjeta> solicitudes = repoSolicitudesTarjetas.getSolicitudes();
    assertEquals(1, solicitudes.size());
    assertEquals(TipoSolicitudTarjeta.PERSONA_VULNERABLE, solicitudes.get(0).getTipoSolicitudTarjeta());
    assertEquals(10, (solicitudes.get(0)).getCantidad());
  }

  @Test
  public void siColaboradorJuridicoIntentaSolicitarTarjetasLanzaException() {
    Exception exception = assertThrows(ValidarPermisoUsoTarjetasException.class, () ->
        colaboradorJuridico.solicitarTarjetaPersonal());
    assertEquals("Este colaborador no puede poseer tarjetas.", exception.getMessage());
  }

  @Test
  public void siUnColaboradorSinDireccionIntentaSolicitarTarjetasLanzaException() {
    Exception exception = assertThrows(ValidarPermisoUsoTarjetasException.class, () ->
        colaboradorFisico2.solicitarTarjetaPersonal());
    assertEquals("No ha guardado una dirección.", exception.getMessage());
  }

  @Test
  public void solicitarTarjetaPersonalYaTeniendoUnaLanzaException() {
    TarjetaColaborador tarjetaPersonal = repositorioTarjetas.crearTarjetaColaborador(colaboradorFisico);
    colaboradorFisico.recibeTarjetaPersonal(tarjetaPersonal);
    Exception exception = assertThrows(RuntimeException.class, () ->
        colaboradorFisico.solicitarTarjetaPersonal());
    assertEquals("Ya tienes una tarjeta personal.", exception.getMessage());
  }

  @Test
  public void siSeDaUnaCantidadMayorAlMaximoEnSolicitudTarjetaPersonaVulnerableLanzaException() {
    Exception exception = assertThrows(RuntimeException.class, () ->
        colaboradorFisico.solicitarTarjetasPersonasVulnerables(30));
    assertEquals("La cantidad de tarjetas debe ser un valor entre 1 y 20.",
        exception.getMessage());
  }

}
