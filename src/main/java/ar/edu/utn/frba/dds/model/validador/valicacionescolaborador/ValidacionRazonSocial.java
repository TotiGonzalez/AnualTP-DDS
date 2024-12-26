package ar.edu.utn.frba.dds.model.validador.valicacionescolaborador;

import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.model.colaboradores.ColaboradorJuridico;
import ar.edu.utn.frba.dds.model.excepciones.ValidarColaboradorException;

public class ValidacionRazonSocial implements ValidadorColaborador {

  @Override
  public void validar(Colaborador colaborador) throws ValidarColaboradorException {
    if (colaborador instanceof ColaboradorJuridico colaboradorJuridico) {
      if (colaboradorJuridico.getRazonSocial() == null) {
        throw new ValidarColaboradorException("La razón social es obligatoria");
      }
    }
  }
}
