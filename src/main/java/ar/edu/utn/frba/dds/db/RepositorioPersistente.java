package ar.edu.utn.frba.dds.db;

import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;

public abstract class RepositorioPersistente<T> implements WithSimplePersistenceUnit {
  private Class<T> entityClass;

  public RepositorioPersistente(Class<T> entityClass) { this.entityClass = entityClass; }

  public void registrar(T entity) {
    entityManager().persist(entity);
  }

  public void eliminar(T entity) {
    entityManager().remove(entity);
  }

  public T buscar(Integer id) {
    return entityManager().find(entityClass, id);
  }

  public List<T> buscarTodos() {
    return entityManager().createQuery("from "
        + entityClass.getSimpleName(), entityClass).getResultList(); }

  public List<T> buscarPorTipo(Class<T> type) {
    String jpql = "SELECT e FROM " + type.getSimpleName() + " e";
    return entityManager().createQuery("SELECT e FROM " + type.getSimpleName() + " e", type)
        .getResultList();
  }

  public List<Colaborador> filtrarPorLocalidad(String localidad) {
    return entityManager()
        .createQuery("from Colaborador localidad join localidad.direccion "
            + "direccion where direccion.localidad = :localidad", Colaborador.class)
        .setParameter("localidad", localidad)
        .getResultList();
  }

  public void clean() {
    entityManager().createQuery("from "
        + entityClass.getSimpleName()).executeUpdate();
  }
}