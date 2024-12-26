package ar.edu.utn.frba.dds.model.tarjetas;

import ar.edu.utn.frba.dds.db.EntidadPersistente;
import ar.edu.utn.frba.dds.db.convertidores.ConvertidorLocalDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
@Table(name = "tarjetas")
public abstract class Tarjeta extends EntidadPersistente {
    @Column(name = "codigo_tarjeta")
    protected String codigo;
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "tarjeta_id",  referencedColumnName = "id")
  protected List<RegistroUso> listaUsosTarjeta;

  @Column(name = "is_titular_tarjeta_set")
  protected boolean isTitularTarjetaSet = false;

  @Column(name = "fecha_activacion")
  @Convert(converter = ConvertidorLocalDate.class)
  protected LocalDate fechaActivacion;

  public String getCodigo() {
    return codigo;
  }

  public boolean isTitularTarjetaSet() {
    return isTitularTarjetaSet;
  }

  public List<RegistroUso> getListaUsosTarjeta() {
    return listaUsosTarjeta;
  }

  public Integer getCantidadMesesActiva() {
    return Math.toIntExact(ChronoUnit.MONTHS.between(fechaActivacion, LocalDate.now()));
  }
}
