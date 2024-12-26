package ar.edu.utn.frba.dds.model.tarjetas;

import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.db.RepositorioPersistente;
import java.util.HashSet;
import java.util.Set;

public class RepoTarjetas extends RepositorioPersistente<Tarjeta> {
  private static final RepoTarjetas INSTANCE = new RepoTarjetas();
  private Set<Tarjeta> tarjetas;
  private long codigoCounter;
  private long codigoCounter2;

  public RepoTarjetas() {
    super(Tarjeta.class);
    this.tarjetas = new HashSet<>();
    this.codigoCounter = 0L;
    this.codigoCounter2 = (long) Math.pow(36, 11) / 2;
  }

  public static synchronized RepoTarjetas getInstance() {
    return INSTANCE;
  }

  public TarjetaPersonaVulnerable crearTarjetaPersonaVulnerable() {
    String codigoUnico = generarCodigoUnico(codigoCounter);
    TarjetaPersonaVulnerable tarjetaPersonaVulnerable = new TarjetaPersonaVulnerable(codigoUnico);
    tarjetas.add(tarjetaPersonaVulnerable);
    codigoCounter++;
    return tarjetaPersonaVulnerable;
  }

  public TarjetaColaborador crearTarjetaColaborador(Colaborador colaborador) {
    String codigoUnico = generarCodigoUnico(codigoCounter2);
    TarjetaColaborador tarjetaColaborador = new TarjetaColaborador(codigoUnico, colaborador);
    tarjetas.add(tarjetaColaborador);
    codigoCounter2++;
    return tarjetaColaborador;
  }

  private String generarCodigoUnico(long contador) {
    String codigo = Long.toString(contador, 36).toUpperCase();
    return String.format("%11s", codigo).replace(' ', '0');
  }


  public Tarjeta obtenerTarjetaPorCodigo(String codigo) {
    return tarjetas.stream()
        .filter(t -> t.getCodigo().equals(codigo))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada para el código: " + codigo));
  }

  public Set<Tarjeta> getTarjetas() {
    return tarjetas;
  }

  public void eliminarTarjeta(TarjetaPersonaVulnerable tarjetaPersonaVulnerable) {
    tarjetas.remove(tarjetaPersonaVulnerable);
  }
}
