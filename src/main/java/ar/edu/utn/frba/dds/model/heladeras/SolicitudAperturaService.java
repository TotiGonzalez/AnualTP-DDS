package ar.edu.utn.frba.dds.model.heladeras;

import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.model.excepciones.AperturaInvalidaException;
import ar.edu.utn.frba.dds.model.excepciones.SolicitudExpiradaException;
import ar.edu.utn.frba.dds.model.excepciones.SolicitudNoEncontradaException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class SolicitudAperturaService {
  private Map<Long, SolicitudApertura> solicitudes = new HashMap<>();
  private Map<Long, Apertura> aperturas = new HashMap<>();
  private long solicitudIdCounter = 0;
  private long aperturaIdCounter = 0;
  private ControladorDeAccesoInterno controladorDeAcceso;

  public SolicitudAperturaService(ControladorDeAccesoInterno controladorDeAcceso) {
    this.controladorDeAcceso = controladorDeAcceso;
  }

  public SolicitudApertura registrarSolicitud(Colaborador colaborador, Heladera heladera) {
    if (!heladera.estaActiva()) {
      throw new AperturaInvalidaException("El heladera no esta activa");
    }

    SolicitudApertura solicitud = new SolicitudApertura(++solicitudIdCounter,
        colaborador.getCredencial().getUsuario(), LocalDateTime.now());
    solicitudes.put(solicitud.getIdApertura(), solicitud);

    heladera.registrarSolicitudApertura(solicitud);
    colaborador.registrarSolicitudApertura(solicitud);

    controladorDeAcceso.notificarTarjetaHabilitada(colaborador.getCredencial().getUsuario());

    return solicitud;
  }

  public Apertura registrarApertura(Colaborador colaborador, Heladera heladera) {
    Optional<SolicitudApertura> solicitudOpt = solicitudes.values().stream()
        .filter(solicitud -> solicitud.getNumeroTarjeta()
            .equals(colaborador.getCredencial().getUsuario()) && !solicitud.isCompletada())
        .findFirst();

    if (solicitudOpt.isPresent()) {
      SolicitudApertura solicitud = solicitudOpt.get();
      LocalDateTime now = LocalDateTime.now();
      int tiempoExpiracion = heladera.getTiempoExpiracionSolicitudes();
      if (now.isBefore(solicitud.getFechaSolicitud().plusHours(tiempoExpiracion))) {
        Apertura apertura = new Apertura(
            colaborador.getCredencial().getUsuario(), now,  heladera.getNombreHeladera());
        aperturas.put(solicitud.getIdApertura(), apertura);

        solicitud.setCompletada(true);
        solicitudes.put(solicitud.getIdApertura(), solicitud);

        heladera.registrarApertura(apertura);
        colaborador.registrarApertura(apertura);

        return apertura;
      } else {
        throw new SolicitudExpiradaException("La solicitud ha expirado.");
      }
    } else {
      throw new SolicitudNoEncontradaException("No se encontró una solicitud activa .");
    }
  }

  public void agregarSolicitud(SolicitudApertura solicitud) {
    solicitudes.put(solicitud.getIdApertura(), solicitud);
  }

  public int cantidadDeSolicitudes() {
    return solicitudes.size();
  }

  public int cantidadDeAperturas() {
    return aperturas.size();
  }

}
