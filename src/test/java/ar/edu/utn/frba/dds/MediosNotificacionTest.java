package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.model.colaboradores.medioscontacto.AdapterJavaxMail;
import ar.edu.utn.frba.dds.model.colaboradores.medioscontacto.AdapterTelegramBot;
import ar.edu.utn.frba.dds.model.colaboradores.medioscontacto.AdapterTwilio;
import ar.edu.utn.frba.dds.model.colaboradores.medioscontacto.Mail;
import ar.edu.utn.frba.dds.model.colaboradores.medioscontacto.Telegram;
import ar.edu.utn.frba.dds.model.colaboradores.medioscontacto.Whatsapp;
import ar.edu.utn.frba.dds.model.excepciones.MailException;
import ar.edu.utn.frba.dds.model.excepciones.TelegramException;
import ar.edu.utn.frba.dds.model.excepciones.TwilioNotificacionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class MediosNotificacionTest {
  @Disabled
  @Test
  public void enviarNotificacionTelegram_ChatIdEsValido() {
    Telegram telegram = new Telegram("1001309744");
    telegram.setAdapter(new AdapterTelegramBot());
    Assertions.assertDoesNotThrow(() -> telegram.contactar(
        "Hola! Soy MonitoreoHeladerasDDS_bot! Hermosa tarde, verdad?"));
  }
  @Disabled
  @Test
  public void enviarNotificacionTelegram_ChatIdEsInvalido() {
    Telegram telegram = new Telegram("testWiiixxxx");
    telegram.setAdapter(new AdapterTelegramBot());
    Assertions.assertThrows(TelegramException.class,() -> telegram.contactar(
        "Hola! Soy MonitoreoHeladerasDDS_bot! Hermosa tarde, verdad?"));
  }
  @Disabled
  @Test
  public void enviarNotificacionMail_MailEsValido() {
    Mail mail = new Mail("larasnr@gmail.com");
    mail.setAdapter(new AdapterJavaxMail());
    mail.setAsunto("MediosNotificacionTest");
    Assertions.assertDoesNotThrow(() -> mail.contactar(
        "Hola! Soy MonitoreoHeladerasDDS! Hermosa tarde, verdad?"));
  }
  @Disabled
  @Test
  public void enviarNotificacionMail_MailEsInvalido() {
    Mail mail = new Mail("larasfesfsnr@");
    mail.setAdapter(new AdapterJavaxMail());
    mail.setAsunto("MediosNotificacionTest");
    Assertions.assertThrows(MailException.class,() -> mail.contactar(
        "Hola! Soy MonitoreoHeladerasDDS! Hermosa tarde, verdad?"));
  }
/*
  @Test
  public void enviarNotificacionWhatsapp_NumeroTelefonoEsValido() {
    Whatsapp whatsapp = new Whatsapp("+5491154914487");
    whatsapp.setAdapter(new AdapterTwilio());
    Assertions.assertDoesNotThrow(() -> whatsapp.contactar(
        "Hola! Soy MonitoreoHeladerasDDS! Hermosa tarde, verdad?"));
  }
*/
@Disabled
  @Test
  public void enviarNotificacionWhatsapp_NumeroTelefonoEsInvalido() {
    Whatsapp whatsapp = new Whatsapp("+++5491154914487");
    whatsapp.setAdapter(new AdapterTwilio());
    Assertions.assertThrows(TwilioNotificacionException.class,() -> whatsapp.contactar(
        "Hola! Soy MonitoreoHeladerasDDS! Hermosa tarde, verdad?"));
  }

}
