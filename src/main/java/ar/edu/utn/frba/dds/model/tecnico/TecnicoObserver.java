package ar.edu.utn.frba.dds.model.tecnico;

import ar.edu.utn.frba.dds.model.heladeras.FallaTecnica;

public interface TecnicoObserver {
  void update(FallaTecnica falla);//notificar
}


//