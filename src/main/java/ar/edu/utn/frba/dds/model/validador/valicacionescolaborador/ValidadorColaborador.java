package ar.edu.utn.frba.dds.model.validador.valicacionescolaborador;

import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.model.excepciones.ValidarColaboradorException;

public interface ValidadorColaborador {
  void validar(Colaborador colaborador) throws ValidarColaboradorException;
}
