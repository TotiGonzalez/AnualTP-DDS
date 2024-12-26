package ar.edu.utn.frba.dds.model.suscripciones;

import ar.edu.utn.frba.dds.model.heladeras.MapaHeladeras;
import java.util.List;

public class RecomendadorHeladeraMasVacia implements RecomendadorHeladera {
  @Override
  public List<SugerenciaHeladera> recomendar(MapaHeladeras mapaHeladeras) {
    return null;
  }
}
