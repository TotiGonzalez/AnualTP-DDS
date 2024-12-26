package ar.edu.utn.frba.dds.model.tarjetas;

import ar.edu.utn.frba.dds.model.colaboradores.PersonaVulnerable;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.persistence.*;


@Entity
@DiscriminatorValue("Tarjeta_V")
public class TarjetaPersonaVulnerable extends Tarjeta {
  @OneToOne(mappedBy = "tarjetaPersonaVulnerable", cascade = CascadeType.ALL)
  private PersonaVulnerable titularTarjeta;

  public TarjetaPersonaVulnerable(String codigo) {
    this.codigo = codigo;
    this.listaUsosTarjeta = new ArrayList<>();
  }

  public TarjetaPersonaVulnerable() {

  }

  public void setTitularTarjeta(PersonaVulnerable personaVulnerable) {
    if (isTitularTarjetaSet) {
      throw new RuntimeException("Esta tarjeta ya tiene titular asignado");
    } else {
      this.titularTarjeta = personaVulnerable;
      isTitularTarjetaSet = true;
      fechaActivacion = LocalDate.now();
    }
  }

  public void registrarUso(String nombreHeladera) {
    if (puedeUsarseHoy()) {
      listaUsosTarjeta.add(new RegistroUso(nombreHeladera, MotivoUso.RETIRO));
    } else {
      throw new RuntimeException("Limite de usos diarios alcanzado");
    }
  }

  private boolean puedeUsarseHoy() {
    long usosDiaDeLaFecha = listaUsosTarjeta.stream()
        .filter(uso -> uso.getFechaHora().toLocalDate().equals(LocalDate.now()))
        .count();
    return usosDiaDeLaFecha < cantMaxUsos();
  }

  public Integer getCantUsos() {
    return this.listaUsosTarjeta.size();
  }

  private int cantMaxUsos() {
    return 4 + (2 * titularTarjeta.getCantHijos());
  }

  public String getCodigo() {
    return codigo;
  }

  public PersonaVulnerable getTitularTarjeta() {
    return titularTarjeta;
  }
}
