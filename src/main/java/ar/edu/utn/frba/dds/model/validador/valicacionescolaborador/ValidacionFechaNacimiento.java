package ar.edu.utn.frba.dds.model.validador.valicacionescolaborador;

import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.model.colaboradores.ColaboradorFisico;
import ar.edu.utn.frba.dds.model.excepciones.ValidarColaboradorException;

public class ValidacionFechaNacimiento implements ValidadorColaborador {

  @Override
  public void validar(Colaborador colaborador) throws ValidarColaboradorException {
    if (colaborador instanceof ColaboradorFisico colaboradorFisico) {
      if (colaboradorFisico.getFechaNacimiento() == null) {
        throw new ValidarColaboradorException("La fecha de nacimiento es obligatoria");
      }
    }
  }
}
