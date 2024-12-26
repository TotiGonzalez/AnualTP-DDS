package ar.edu.utn.frba.dds.model.colaboradores.medioscontacto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * .
 */
@Entity
public class Telegram extends MedioContacto {

  @Column(name = "usuario_telegram")
  private String chatId;

  @Transient
  private IAdapterTelegram telegramSender;

  public Telegram(String chatId) {
    this.chatId = chatId;
  }

  public Telegram() {

  }

  public void setAdapter(IAdapterTelegram telegramSender) {
    this.telegramSender = telegramSender;
  }

  @Override
  public void contactar(String mensaje) {
        this.telegramSender.enviarTelegram(this.chatId, mensaje);
  }
}
