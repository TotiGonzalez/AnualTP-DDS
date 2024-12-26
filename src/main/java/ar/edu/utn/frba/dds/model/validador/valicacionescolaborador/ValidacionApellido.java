package ar.edu.utn.frba.dds.model.validador.valicacionescolaborador;

import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.model.colaboradores.ColaboradorFisico;
import ar.edu.utn.frba.dds.model.excepciones.ValidarColaboradorException;


public class ValidacionApellido implements ValidadorColaborador {

  @Override
  public void validar(Colaborador colaborador) throws ValidarColaboradorException {
    if (colaborador instanceof ColaboradorFisico colaboradorFisico) {
      if (colaboradorFisico.getApellido() == null) {
        throw new ValidarColaboradorException("El apellido es obligatorio");
      }
    }
  }
}
