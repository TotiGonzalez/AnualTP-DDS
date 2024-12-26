package ar.edu.utn.frba.dds.model.suscripciones;

import ar.edu.utn.frba.dds.model.heladeras.MapaHeladeras;
import java.util.List;

public interface RecomendadorHeladera {
  public List<SugerenciaHeladera> recomendar(MapaHeladeras mapaHeladeras);
}
