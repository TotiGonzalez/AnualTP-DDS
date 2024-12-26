package ar.edu.utn.frba.dds.model.suscripciones;

import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.model.heladeras.MapaHeladeras;
import java.util.ArrayList;
import java.util.List;

public class RecomendacionHeladeras {
  private List<RecomendadorHeladera> criterios;

  public RecomendacionHeladeras(List<RecomendadorHeladera> criterios) {
    this.criterios = criterios;
  }

  public List<SugerenciaHeladera> recomendar(Colaborador colaborador){
    List<SugerenciaHeladera> recomendacion = new ArrayList<>();
    this.criterios.forEach(c -> recomendacion.addAll(c.recomendar(MapaHeladeras.getInstance())));
    return recomendacion;
  }
}
