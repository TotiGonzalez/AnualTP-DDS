package ar.edu.utn.frba.dds;

import static org.mockito.Mockito.mock;

import ar.edu.utn.frba.dds.model.colaboradores.ColaboradorFisico;
import ar.edu.utn.frba.dds.model.colaboradores.RepoColaboradores;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Direccion;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.TipoJuridico;
import ar.edu.utn.frba.dds.model.heladeras.ConfiguracionHeladera;
import ar.edu.utn.frba.dds.model.heladeras.EstadoHeladera;
import ar.edu.utn.frba.dds.model.heladeras.Heladera;
import ar.edu.utn.frba.dds.model.heladeras.UbicacionPrecisa;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RepoColaboradoresTest {
  private final RepoColaboradores repo = RepoColaboradores.getInstance();

  @Test
  public void eliminarColaborador_correcto() {
    repo.registrarColaboradorJuridico(
        TipoJuridico.EMPRESA,"Razon Social","Rubro");
    repo.eliminarColaborador(repo.getColaboradores().get(0));
    Assertions.assertNotNull(repo.getColaboradores());
  }

  @Test
  public void limpiarValidacionesJuridico_correcto() {
    repo.limpiarValidacionesJuridico();
    Assertions.assertNotNull(repo.getValidacionCompuestaJuridico());
  }

  @Test
  public void unColaboradorEscuchaNotificaciones() throws InterruptedException {
    ColaboradorFisico colaborador = mock(ColaboradorFisico.class);
    //EventListener evento = new AlertaHeladera();
    //colaborador.alertaListener(evento);
   // Mockito.verify(colaborador,Mockito.atLeast(1)).alertaListener(evento);
  }
  @Test
  public void UnColaboradorRecibeNotificacionHeladera(){

    ColaboradorFisico colaborador = mock(ColaboradorFisico.class);
    //EventListener evento = new AlertaHeladera();
    //colaborador.alertaListener(evento);
    UbicacionPrecisa ubicacion1 = new UbicacionPrecisa("Ciudad1",
         new Direccion("Calle1",
            "Barrio1", "falsa",123), 10.0, 20.0);

    ConfiguracionHeladera config1 = new ConfiguracionHeladera(5.0,
        25.0, 3);


    Heladera heladera = new Heladera("heladera1", ubicacion1,
        EstadoHeladera.ACTIVA, 100, config1);
    //heladera.getAlertaHeladera().notificar();
    //Mockito.verify(heladera,Mockito.atLeast(1)).getAlertaHeladera().notificar();
    //Mockito.verify(colaborador,Mockito.atLeast(1)).alertaListener(evento);
  }

}
