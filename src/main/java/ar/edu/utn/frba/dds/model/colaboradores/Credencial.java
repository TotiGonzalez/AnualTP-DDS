package ar.edu.utn.frba.dds.model.colaboradores;

import ar.edu.utn.frba.dds.db.EntidadPersistente;
import ar.edu.utn.frba.dds.db.convertidores.ConvertidorLocalDate;
import ar.edu.utn.frba.dds.model.excepciones.ValidarRotacionException;
import ar.edu.utn.frba.dds.model.validador.validacionescontrasenia.ValidacionDeContrasenia;
import java.time.LocalDate;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;

/**
 * .
 */
@Entity
public class Credencial extends EntidadPersistente {
  @Column
  private String usuario;
  @Column
  private String contrasenia;

  @Column(name = "fecha_cambio_contrasenia")
  @Convert(converter = ConvertidorLocalDate.class)
  private LocalDate fechaCambioContrasenia;

  @Column
  private Boolean expirada;

  @OneToOne(mappedBy = "credencial")
  private Colaborador id_colaborador;
  /**
   * constructor.
   */
  public Credencial(String usuario, String contrasenia) {
    this.usuario = usuario;
    this.contrasenia = contrasenia;
    this.fechaCambioContrasenia = LocalDate.now();
    this.expirada = false;
  }

  public Credencial() {}

  /**
   * .
   */
  public List<String> validarContrasenia(String nuevaContrasenia, ValidacionDeContrasenia
      validadorDeContrasenia) {
    List<String> errores = validadorDeContrasenia.validarContrasenia(nuevaContrasenia);
    errores.removeIf(String::isEmpty);
    return errores;
  }

  /**
   * .
   */
  public void cambiarContrasenia(String nuevaContrasenia, ValidacionDeContrasenia
      validadorDeContrasenia) {
    this.validarContrasenia(nuevaContrasenia,
        validadorDeContrasenia);
    hashearContrasenia(nuevaContrasenia);
    this.fechaCambioContrasenia = LocalDate.now();
  }

  /**
   * .
   */
  public void esExpirada(int antiguedadMaxima) {
    LocalDate today = LocalDate.now();
    boolean credencialExpirada = false;
    if (!this.expirada && today.isAfter(fechaCambioContrasenia.plusDays(antiguedadMaxima))) {
      throw new ValidarRotacionException(
          "Las credenciales han expirado. Por favor, cambiar la contraseña");
    }
    this.expirada = credencialExpirada;
  }

  /**
   * .
   */
  public void setFechaCambioContrasenia(LocalDate fechaCambioContrasenia) {
    this.fechaCambioContrasenia = fechaCambioContrasenia;
  }

  /**
   * .
   */
  public String getContrasenia() {
    return this.contrasenia;
  }

  /**
   * .
   */
  public LocalDate getFechaCambioContrasenia() {
    return fechaCambioContrasenia;
  }

  /**
   * .
   */
  public Boolean getExpirada() {
    return expirada;
  }

  public String getUsuario() {
    return usuario;
  }

  public void hashearContrasenia(String valor) {
    this.contrasenia = BCrypt.hashpw(valor, BCrypt.gensalt());
  }

  public boolean contraseniaEsCorrecta(String contrasenia) {
    return BCrypt.checkpw(contrasenia, this.contrasenia);
  }

}

