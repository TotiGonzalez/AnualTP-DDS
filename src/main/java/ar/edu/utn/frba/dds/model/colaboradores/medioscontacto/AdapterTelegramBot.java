package ar.edu.utn.frba.dds.model.colaboradores.medioscontacto;


import ar.edu.utn.frba.dds.model.excepciones.TelegramException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.Objects;

public class AdapterTelegramBot  implements IAdapterTelegram {

  public void enviarTelegram(String chatId, String mensaje) {
    //String BOT_TOKEN = System.getenv("TELEGRAM_TOKEN");
    String BOT_TOKEN = "7405990102:AAGIvoaUQPBtHr4OEcIb6mHZNHGgIOVqgug";
    if(Objects.isNull(BOT_TOKEN)) {
      throw new TelegramException("El token del bot de Telegram está vacío");
    }
    Request enviarMensajeRequest = this.crearSendMessageRequest("https://api.telegram.org/bot",
        BOT_TOKEN,"sendMessage",chatId,mensaje);

    Response response = this.ejecutarRequest(enviarMensajeRequest);
    if(!response.isSuccessful()) {
      throw new TelegramException("No se ha podido enviar el mensaje de Telegram");
    }
  }

  public Request crearSendMessageRequest(String baseUrl, String token, String metodo, String chatId, String mensaje) {
    HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl
            .parse(baseUrl + token + "/" + metodo))
        .newBuilder();
    urlBuilder.addQueryParameter("chat_id", chatId);
    urlBuilder.addQueryParameter("text", mensaje);
    String url = urlBuilder.build().toString();

    return new Request.Builder().url(url).build();
  }

  public Response ejecutarRequest(Request request) {
    OkHttpClient client = new OkHttpClient();
    Response response;
    try{
      response = client.newCall(request).execute();
    }
    catch (IOException e){
      throw new TelegramException("No se ha podido enviar el mensaje de Telegram: " + e.getMessage());
    }
    return response;
  }

  }
