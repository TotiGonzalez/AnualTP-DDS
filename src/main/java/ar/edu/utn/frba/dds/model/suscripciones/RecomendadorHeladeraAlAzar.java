package ar.edu.utn.frba.dds.model.suscripciones;

import ar.edu.utn.frba.dds.model.heladeras.Heladera;
import ar.edu.utn.frba.dds.model.heladeras.MapaHeladeras;

import java.util.List;
import java.util.Random;

public class RecomendadorHeladeraAlAzar implements RecomendadorHeladera {
  @Override
  public List<SugerenciaHeladera> recomendar(MapaHeladeras mapaHeladeras) {
    List<Heladera> heladeras = mapaHeladeras.getHeladeras();
    Random rand = new Random();
    int indiceAleatorio = rand.nextInt(heladeras.size());
    Heladera heladeraAleatoria = heladeras.get(indiceAleatorio);
    SugerenciaHeladera sugerenciaHeladera = new SugerenciaHeladera(heladeraAleatoria);

    return List.of(sugerenciaHeladera);
  }
}
