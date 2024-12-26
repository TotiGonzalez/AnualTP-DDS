package ar.edu.utn.frba.dds.model.validador.valicacionescolaborador;

import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.model.colaboradores.ColaboradorJuridico;
import ar.edu.utn.frba.dds.model.excepciones.ValidarColaboradorException;

public class ValidacionRubro implements ValidadorColaborador {

  @Override
  public void validar(Colaborador colaborador) throws ValidarColaboradorException {
    if (colaborador instanceof ColaboradorJuridico colaboradorJuridico) {
      if (colaboradorJuridico.getRubro() == null) {
        throw new ValidarColaboradorException("El rubro es obligatorio");
      }
    }
  }
}


