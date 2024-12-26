package ar.edu.utn.frba.dds;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import ar.edu.utn.frba.dds.model.colaboradores.ColaboradorFisico;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Direccion;
import ar.edu.utn.frba.dds.model.heladeras.ConfiguracionHeladera;
import ar.edu.utn.frba.dds.model.heladeras.EstadoHeladera;
import ar.edu.utn.frba.dds.model.heladeras.FallaTecnica;
import ar.edu.utn.frba.dds.model.heladeras.Heladera;
import ar.edu.utn.frba.dds.model.heladeras.UbicacionPrecisa;
import ar.edu.utn.frba.dds.model.tecnico.Tecnico;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class VisitaTecnicaTest {

  @Test
  public void unTecnicoVisitaHeladera(){
    UbicacionPrecisa ubicacion1 = new UbicacionPrecisa("Ciudad1",
        new Direccion("Calle1",
            "Barrio1", "falsa",123), 10.0, 20.0);

    ConfiguracionHeladera config1 = new ConfiguracionHeladera(5.0,
        25.0, 3);

    UbicacionPrecisa ubicacionTecnico = new UbicacionPrecisa("Ciudad1",
        new Direccion("Calle1",
            "Barrio1", "falsa",123), 15.0, 20.0);

    UbicacionPrecisa ubicacionTecnico1 = new UbicacionPrecisa("Ciudad1",
        new Direccion("Calle1",
            "Barrio1", "falsa",123), 19.0, 20.0);


    Tecnico tecnicoDisponible = new Tecnico("TotiGonzalez",ubicacionTecnico,"Un tecnico con todas las herramientas","./tecnicofotos/foto1");
    Tecnico tecnicoDisponible1 = new Tecnico("TomasAraujo",ubicacionTecnico1,"Se levanta tarde siempre","./tecnicofotos/foto2");

    Heladera heladera = new Heladera("heladera1", ubicacion1,
        EstadoHeladera.ACTIVA, 100, config1);
    //ColaboradorFisico colaboradorFisico = mock(ColaboradorFisico.class);
    ColaboradorFisico colaboradorFisico1 = new ColaboradorFisico("Pedro", "Hernandez", LocalDate.of(2000, 9, 17));;
    ColaboradorFisico colaboradorFisico2 = new ColaboradorFisico("José", "Pineda", LocalDate.of(1993, 5, 3));

    FallaTecnica fallaTecnica = new FallaTecnica(heladera, LocalDateTime.now(),colaboradorFisico1,"cualquier cosa", "algo", "pathfoto1");
    fallaTecnica.addObserver(tecnicoDisponible);
    fallaTecnica.addObserver(tecnicoDisponible1);
    Assertions.assertTrue(fallaTecnica.contieneTecnico(tecnicoDisponible));

    colaboradorFisico1.reportarFallaTecnica(heladera,fallaTecnica);
    Assertions.assertEquals(1,fallaTecnica.getVisitas().size());

    //FallaTecnica fallaTecnica = heladera.getfallaTecnica(0);



  }


}
