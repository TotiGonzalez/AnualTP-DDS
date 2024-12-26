package ar.edu.utn.frba.dds.model.colaboradores.medioscontacto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * .
 */
@Entity
public class Whatsapp extends MedioContacto {

  @Column
  private String celular;

  @Transient
  private IAdapterWhatsapp whatsappSender;

  public Whatsapp(String celular) {
    this.celular = celular;
  }

  public Whatsapp() {

  }

  public void setAdapter(IAdapterWhatsapp whatsappSender) {
    this.whatsappSender = whatsappSender;
  }

  @Override
  public void contactar(String mensaje) {
      this.whatsappSender.enviarWhatsapp(this.celular, mensaje);
  }
}
