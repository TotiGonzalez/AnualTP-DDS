package ar.edu.utn.frba.dds.model.tarjetas.solicitudes;

import java.util.ArrayList;
import java.util.List;

public class RepoSolicitudesTarjetas {
  private static final RepoSolicitudesTarjetas INSTANCE = new RepoSolicitudesTarjetas();
  private List<SolicitudTarjeta> solicitudes;
  private int cantMaximaSolicitudTajetaVulnerable;

  public RepoSolicitudesTarjetas() {
    solicitudes = new ArrayList<>();
    cantMaximaSolicitudTajetaVulnerable = 20;
  }

  public static synchronized RepoSolicitudesTarjetas getInstance() {
    return INSTANCE;
  }

  public List<SolicitudTarjeta> getSolicitudes() {
    return solicitudes;
  }

  public void registrarSolicitud(SolicitudTarjeta solicitud) {
    this.solicitudes.add(solicitud);
  }

  public void removerSolicitud(SolicitudTarjeta solicitud) {
    this.solicitudes.remove(solicitud);
  }

  public void setCantMaximaSolicitudTajetaVulnerable(int cantMaximaSolicitudTajetaVulnerable) {
    this.cantMaximaSolicitudTajetaVulnerable = cantMaximaSolicitudTajetaVulnerable;
  }

  public int cantMaximaSolicitudTarjetaVulnerable() {
    return cantMaximaSolicitudTajetaVulnerable;
  }

  public void limpiarRepositorio() {
    solicitudes.clear();
  }

  public void verificacionCantidadSolicitada(int cantidad) {
    if (cantidad <= 0 || cantidad > cantMaximaSolicitudTajetaVulnerable) {
      throw new RuntimeException("La cantidad de tarjetas debe ser un valor entre 1 y "
          + cantMaximaSolicitudTajetaVulnerable + ".");
    }
  }
}
