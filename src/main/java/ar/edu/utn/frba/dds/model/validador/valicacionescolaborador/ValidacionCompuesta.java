package ar.edu.utn.frba.dds.model.validador.valicacionescolaborador;

import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.model.excepciones.ValidarColaboradorException;
import java.util.ArrayList;
import java.util.List;

public class ValidacionCompuesta implements ValidadorColaborador {
  private List<ValidadorColaborador> validaciones = new ArrayList<>();

  public void agregarValidacion(ValidadorColaborador validador) {
    validaciones.add(validador);
  }

  @Override
  public void validar(Colaborador colaborador) throws ValidarColaboradorException {
    for (ValidadorColaborador validador : validaciones) {
      validador.validar(colaborador);
    }
  }
}
