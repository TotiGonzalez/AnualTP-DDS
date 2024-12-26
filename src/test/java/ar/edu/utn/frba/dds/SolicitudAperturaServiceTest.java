package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.model.colaboradores.Credencial;
import ar.edu.utn.frba.dds.model.excepciones.AperturaInvalidaException;
import ar.edu.utn.frba.dds.model.excepciones.SolicitudExpiradaException;
import ar.edu.utn.frba.dds.model.excepciones.SolicitudNoEncontradaException;
import ar.edu.utn.frba.dds.model.heladeras.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SolicitudAperturaServiceTest {

  private SolicitudAperturaService solicitudAperturaService;
  private ControladorDeAccesoInterno controladorDeAccesoInterno;
  private Heladera heladera;
  private Colaborador colaborador;
  private Credencial credencial;

  @BeforeEach
  void setUp() {
    controladorDeAccesoInterno = mock(ControladorDeAccesoInterno.class);
    solicitudAperturaService = new SolicitudAperturaService(controladorDeAccesoInterno);

    heladera = mock(Heladera.class);
    colaborador = mock(Colaborador.class);
    credencial = mock(Credencial.class);

    when(heladera.estaActiva()).thenReturn(true);
    when(colaborador.getCredencial()).thenReturn(credencial);
    when(credencial.getUsuario()).thenReturn("testUsuario");
  }

  @Test
  void registrarSolicitudHeladeraActiva() {
    SolicitudApertura solicitud = solicitudAperturaService.registrarSolicitud(colaborador, heladera);

    assertNotNull(solicitud);
    assertEquals("testUsuario", solicitud.getNumeroTarjeta());
    assertFalse(solicitud.isCompletada());
    assertEquals(1, solicitudAperturaService.cantidadDeSolicitudes());
    verify(heladera).registrarSolicitudApertura(solicitud);
    verify(colaborador).registrarSolicitudApertura(solicitud);
    verify(controladorDeAccesoInterno).notificarTarjetaHabilitada("testUsuario");
  }

  @Test
  void registrarSolicitudHeladeraInactiva() {
    when(heladera.estaActiva()).thenReturn(false);

    assertThrows(AperturaInvalidaException.class, () -> {
      solicitudAperturaService.registrarSolicitud(colaborador, heladera);
    });

    assertEquals(0, solicitudAperturaService.cantidadDeSolicitudes());
  }

  @Test
  void registrarAperturaSolicitudValida() {
    SolicitudApertura solicitudApertura = new SolicitudApertura(1L, "testUsuario", LocalDateTime.now().minusHours(1));
    solicitudAperturaService.agregarSolicitud(solicitudApertura);

    when(heladera.getTiempoExpiracionSolicitudes()).thenReturn(3);

    Apertura apertura = solicitudAperturaService.registrarApertura(colaborador, heladera);

    assertNotNull(apertura);
    assertEquals("testUsuario", apertura.getNumeroTarjeta());
    assertEquals(1, solicitudAperturaService.cantidadDeAperturas());
    assertTrue(solicitudApertura.isCompletada());
    verify(heladera).registrarApertura(apertura);
    verify(colaborador).registrarApertura(apertura);
  }

  @Test
  void registrarAperturaSolicitudExpirada() {
    SolicitudApertura solicitudApertura = new SolicitudApertura(1L, "testUsuario", LocalDateTime.now().minusHours(4));
    solicitudAperturaService.agregarSolicitud(solicitudApertura);

    when(heladera.getTiempoExpiracionSolicitudes()).thenReturn(3);

    assertThrows(SolicitudExpiradaException.class, () -> {
      solicitudAperturaService.registrarApertura(colaborador, heladera);
    });

    assertEquals(0, solicitudAperturaService.cantidadDeAperturas());
  }

  @Test
  void registrarAperturaSolicitudNoEncontrada() {
    assertThrows(SolicitudNoEncontradaException.class, () -> {
      solicitudAperturaService.registrarApertura(colaborador, heladera);
    });

    assertEquals(0, solicitudAperturaService.cantidadDeAperturas());
  }
}
