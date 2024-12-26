package ar.edu.utn.frba.dds.model.colaboradores.medioscontacto;

import ar.edu.utn.frba.dds.model.excepciones.MailException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class AdapterJavaxMail implements IAdapterMail{
  @Override
  public void enviarMail(String asunto, String direccionMail, String mensaje) {
    //String MAIL_PASSWORD = System.getenv("MAIL_PASSWORD");
    String MAIL_PASSWORD ="lpsbcufzayghwiyp";
    String remitente = "monitoreodeheladerasdds@gmail.com";

    Properties properties = System.getProperties();
    properties.put("mail.smtp.host", "smtp.gmail.com");
    properties.put("mail.smtp.port", "587");
    properties.put("mail.smtp.starttls.enable", "true");
    properties.put("mail.smtp.auth", "true");

    Session session = Session.getDefaultInstance(properties);

    try {
      MimeMessage message = new MimeMessage(session);

      message.setFrom(new InternetAddress(remitente));
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(direccionMail));

      message.setSubject(asunto);
      message.setText(mensaje);

      Transport transport = session.getTransport("smtp");
      transport.connect("smtp.gmail.com", remitente, MAIL_PASSWORD);
      transport.sendMessage(message, message.getAllRecipients());
      transport.close();

    } catch (MessagingException ex) {
      throw new MailException("El mail no ha podido ser enviado: " + ex.getMessage());
    }
  }
}
