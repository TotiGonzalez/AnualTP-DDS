package ar.edu.utn.frba.dds.model.colaboradores;

import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Documento;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.TipoJuridico;
import ar.edu.utn.frba.dds.db.RepositorioPersistente;
import ar.edu.utn.frba.dds.model.validador.valicacionescolaborador.ValidacionApellido;
import ar.edu.utn.frba.dds.model.validador.valicacionescolaborador.ValidacionCompuesta;
import ar.edu.utn.frba.dds.model.validador.valicacionescolaborador.ValidacionFechaNacimiento;
import ar.edu.utn.frba.dds.model.validador.valicacionescolaborador.ValidacionNombre;
import ar.edu.utn.frba.dds.model.validador.valicacionescolaborador.ValidacionRazonSocial;
import ar.edu.utn.frba.dds.model.validador.valicacionescolaborador.ValidacionRubro;
import ar.edu.utn.frba.dds.model.validador.valicacionescolaborador.ValidacionTipoJuridico;
import ar.edu.utn.frba.dds.model.validador.valicacionescolaborador.ValidadorColaborador;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class RepoColaboradores extends RepositorioPersistente<Colaborador> {

  private static RepoColaboradores INSTANCE = new RepoColaboradores();
  private final List<Colaborador> colaboradores = new ArrayList<>();
  private ValidacionCompuesta validacionCompuestaFisico = new ValidacionCompuesta();
  private ValidacionCompuesta validacionCompuestaJuridico = new ValidacionCompuesta();

  //// Inicializa los validadores por defecto para ambos colaboradores
  public RepoColaboradores() {
    super(Colaborador.class);

    validacionCompuestaFisico.agregarValidacion(new ValidacionNombre());
    validacionCompuestaFisico.agregarValidacion(new ValidacionApellido());
    validacionCompuestaFisico.agregarValidacion(new ValidacionFechaNacimiento());

    validacionCompuestaJuridico.agregarValidacion(new ValidacionTipoJuridico());
    validacionCompuestaJuridico.agregarValidacion(new ValidacionRazonSocial());
    validacionCompuestaJuridico.agregarValidacion(new ValidacionRubro());
  }

  public static RepoColaboradores getInstance() {
    return INSTANCE;
  }

  public void agregarValidacionFisico(ValidadorColaborador validador) {
    validacionCompuestaFisico.agregarValidacion(validador);
  }

  public void limpiarValidacionesFisico() {
    validacionCompuestaFisico = new ValidacionCompuesta();
  }

  public void agregarValidacionJuridico(ValidadorColaborador validador) {
    validacionCompuestaJuridico.agregarValidacion(validador);
  }

  public void limpiarValidacionesJuridico() {
    validacionCompuestaJuridico = new ValidacionCompuesta();
  }

  public Colaborador registrarColaboradorFisico(String nombre,
                                                String apellido, LocalDate fechaNacimiento) {
    ColaboradorFisico colaboradorFisico =
        new ColaboradorFisico(nombre, apellido, fechaNacimiento);
    validacionCompuestaFisico.validar(colaboradorFisico);
    this.colaboradores.add(colaboradorFisico);
    return colaboradorFisico;
  }

  public Colaborador registrarColaboradorJuridico(TipoJuridico tipoJuridico,
                                                  String razonSocial, String rubro) {
    ColaboradorJuridico colaboradorJuridico =
        new ColaboradorJuridico(tipoJuridico, razonSocial, rubro);
    validacionCompuestaJuridico.validar(colaboradorJuridico);
    this.colaboradores.add(colaboradorJuridico);
    return colaboradorJuridico;
  }

  public void eliminarColaborador(Colaborador colaborador) {
    this.colaboradores.remove(colaborador);
  }

  public ColaboradorFisico buscarColaboradorFisico(Documento tipoDocumento, Integer documento) {
    List<ColaboradorFisico> colaboradorFisicos = this.colaboradores.stream()
        .filter(c -> c instanceof ColaboradorFisico)
        .map(c -> (ColaboradorFisico) c)
        .toList();

    List<ColaboradorFisico> colaboradorFisicoList = new ArrayList<>();
    if (!colaboradorFisicos.isEmpty()) {
      colaboradorFisicoList = colaboradorFisicos.stream().filter(colaboradorFisico
          -> colaboradorFisico.getTipoDocumento().equals(tipoDocumento)
          && colaboradorFisico.getNumeroDocumento().equals(documento)).toList();
    }
    if (!colaboradorFisicoList.isEmpty()) {
      return colaboradorFisicoList.get(0);
    }
    return null;
  }

  public ColaboradorFisico buscarColaboradorFisicoPorID(int id) {
    List<ColaboradorFisico> colaboradorFisico = entityManager()
            .createQuery("from ColaboradorFisico where id = :id"
                    , ColaboradorFisico.class)
            .setParameter("id", id)
            .getResultList();

    if ( colaboradorFisico != null && !colaboradorFisico.isEmpty()) {
      return colaboradorFisico.get(0);
    }
      return null;
  }
  public ColaboradorJuridico buscarColaboradorJuridicoPorCredencialID(int credencialId) {
      List<ColaboradorJuridico> colaboradoresJuridicos = entityManager()
              .createQuery("SELECT c FROM Colaborador c JOIN ColaboradorJuridico cf " +
                              "ON c.id = cf.id WHERE c.credencial = :credencialId"
                      , ColaboradorJuridico.class)
              .setParameter("credencialId", credencialId)
              .getResultList();

      if ( colaboradoresJuridicos != null && !colaboradoresJuridicos.isEmpty()) {
        return colaboradoresJuridicos.get(0);
      }
    return null;
  }
  public ColaboradorFisico buscarColaboradorFisicoPorCredencialID(Credencial credencialId) {
    List<Integer> ids = entityManager()
            .createQuery("SELECT cf.id FROM Colaborador c " +
                    "JOIN ColaboradorFisico cf ON c.id = cf.id " +
                    "WHERE c.credencial = :credencialId", Integer.class)
            .setParameter("credencialId", credencialId)
            .getResultList();

    if ( ids != null && !ids.isEmpty()) {
      return this.buscarColaboradorFisicoPorID(ids.get(0));
    }
    return null;
  }

  public ColaboradorFisico buscarColaboradorFisicoPorDocumento(int documento) {
    List<ColaboradorFisico> colaboradorFisicos = entityManager()
            .createQuery("from ColaboradorFisico where numeroDocumento = :documento"
                    , ColaboradorFisico.class)
            .setParameter("documento", documento)
            .getResultList();
    if ( colaboradorFisicos != null && !colaboradorFisicos.isEmpty()) {
      return colaboradorFisicos.get(0);
    }
    return null;
  }

  public ColaboradorJuridico buscarColaboradorJuridicoPorID(int id) {
    return entityManager()
        .createQuery("from ColaboradorJuridico where id = :id"
            , ColaboradorJuridico.class)
        .setParameter("id", id)
        .getResultList().get(0);
  }

  public List<Colaborador> getColaboradores() {
    return colaboradores;
  }

  public ValidacionCompuesta getValidacionCompuestaJuridico() {
    return validacionCompuestaJuridico;
  }

  //Set para test
  public void agregarColaborador(Colaborador colaborador) {
    this.colaboradores.add(colaborador);
  }

  public void limpiarRepositorio() {
    this.colaboradores.clear();
  }

  public List<Colaborador> todos() {
    return entityManager()
        .createQuery("from Colaborador", Colaborador.class)
        .getResultList();
  }

  public List<ColaboradorFisico> todosLosFisicos() {
    return entityManager()
        .createQuery("from ColaboradorFisico", ColaboradorFisico.class)
        .getResultList();
  }

  public List<ColaboradorJuridico> todosLosJuridicos() {
    return entityManager()
        .createQuery("from ColaboradorJuridico", ColaboradorJuridico.class)
        .getResultList();
  }

}
