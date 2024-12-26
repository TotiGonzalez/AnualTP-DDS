package ar.edu.utn.frba.dds.model.colaboradores;

import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Direccion;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Documento;
import ar.edu.utn.frba.dds.db.EntidadPersistente;
import ar.edu.utn.frba.dds.model.tarjetas.TarjetaPersonaVulnerable;

import javax.persistence.*;
import java.time.LocalDate;

@SuppressWarnings({"FieldMayBeFinal", "unused"})
@Entity
public class PersonaVulnerable extends EntidadPersistente {
  @Column
  private long id_persona_vulnerable;

  @Column
  private String nombre;

  @Column(name = "fecha_nacimiento")
  private LocalDate fechaNacimiento;

  @Column
  private boolean situacionDeCalle;

  @Embedded
  private Direccion direccion;

  @Enumerated(EnumType.STRING)
  private Documento tipoDocumento;

  @Column(name = "numero_documento")
  private Integer numeroDocumento;

  @Column
  private boolean tieneHijos;

  @Column
  private boolean tieneMenoresBajoTutela;

  @Column
  private Integer cantHijos;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "perfil_colaborador", referencedColumnName = "id")
  private Colaborador perfilColaborador;


  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "tarjeta_persona_vulnerable", referencedColumnName = "id")
  private TarjetaPersonaVulnerable tarjetaPersonaVulnerable;

  public PersonaVulnerable(String nombre,
                           LocalDate fechaNacimiento,
                           boolean situacionDeCalle,
                           Direccion direccion, Documento documento, Integer numeroDocumento,
                           boolean tieneHijos, Integer cantHijos, Colaborador perfil) {
    this.nombre = nombre;
    this.fechaNacimiento = fechaNacimiento;
    this.situacionDeCalle = situacionDeCalle;
    this.direccion = direccion;
    this.tipoDocumento = documento;
    this.numeroDocumento = numeroDocumento;
    this.tieneHijos = tieneHijos;
    this.cantHijos = cantHijos;
    this.perfilColaborador = perfil;
  }

  public PersonaVulnerable() {

  }

  public Integer getCantHijos() {
    return cantHijos;
  }

  public Colaborador getPerfilColaborador() {
    return perfilColaborador;
  }

  public String getNombre() {
    return nombre;
  }

  public LocalDate getFechaNacimiento() {
    return fechaNacimiento;
  }

  public boolean isSituacionDeCalle() {
    return situacionDeCalle;
  }

  public Direccion getDireccion() {
    return direccion;
  }

  public Documento getTipoDocumento() {
    return tipoDocumento;
  }

  public Integer getNumeroDocumento() {
    return numeroDocumento;
  }

  public boolean isTieneHijos() {
    return tieneHijos;
  }

  public TarjetaPersonaVulnerable getTarjeta() {
    return tarjetaPersonaVulnerable;
  }

  public void setTarjeta(TarjetaPersonaVulnerable tarjetaPersonaVulnerable) {
    this.tarjetaPersonaVulnerable = tarjetaPersonaVulnerable;
  }
}
