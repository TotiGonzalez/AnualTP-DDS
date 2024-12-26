package ar.edu.utn.frba.dds.model.colaboradores.medioscontacto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * .
 */
@Entity
public class Mail extends MedioContacto {

  @Column(name = "direccion_mail")
  private String direccionMail;

  @Transient
  private IAdapterMail mailSender;

  @Column
  private String asunto = "[Monitoreo de Heladeras DDS] Notificacion";

  public Mail(String direccionMail) {
    this.direccionMail = direccionMail;
  }

  public Mail() {

  }

  public void setAdapter(IAdapterMail mailSender) {
    this.mailSender = mailSender;
  }

  public void setAsunto(String asunto) {
    this.asunto = asunto;
  }

  @Override
  public void contactar(String mensaje) {
    this.mailSender.enviarMail(this.asunto, this.direccionMail, mensaje);
  }

  public String getDireccionCorreo() {
    return this.direccionMail;
  }
}
