package ar.edu.utn.frba.dds.model.heladeras;

import ar.edu.utn.frba.dds.model.tecnico.TecnicoObserver;

public interface FallaTecnicaObservable {
  void addObserver(TecnicoObserver observer);

  void removeObserver(TecnicoObserver observer);

  void notifyObservers();
}
