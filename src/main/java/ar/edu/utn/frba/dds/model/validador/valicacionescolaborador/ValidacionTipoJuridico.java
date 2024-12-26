package ar.edu.utn.frba.dds.model.validador.valicacionescolaborador;

import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.model.colaboradores.ColaboradorJuridico;
import ar.edu.utn.frba.dds.model.excepciones.ValidarColaboradorException;

public class ValidacionTipoJuridico implements ValidadorColaborador {
  @Override
  public void validar(Colaborador colaborador) throws ValidarColaboradorException {
    if (colaborador instanceof ColaboradorJuridico colaboradorJuridico) {
      if (colaboradorJuridico.getTipoJuridico() == null) {
        throw new ValidarColaboradorException("El tipo jurídico es obligatorio");
      }
    }
  }
}
