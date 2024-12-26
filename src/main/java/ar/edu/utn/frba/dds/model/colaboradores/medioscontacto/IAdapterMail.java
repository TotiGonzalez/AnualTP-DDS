package ar.edu.utn.frba.dds.model.colaboradores.medioscontacto;

public interface IAdapterMail {
  void enviarMail(String asunto, String direccionMail, String mensaje);
}
