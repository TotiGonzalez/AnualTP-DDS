package ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * datos del documento.
 */
public enum Documento {
  CEDULA,
  DNI,
  LIBRETA_CIVICA,
  LIBRETA_ENROLAMIENTO
}
