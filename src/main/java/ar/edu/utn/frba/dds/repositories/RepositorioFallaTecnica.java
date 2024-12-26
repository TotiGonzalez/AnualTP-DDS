package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.Usuario;
import ar.edu.utn.frba.dds.model.heladeras.FallaTecnica;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import org.apache.commons.codec.digest.DigestUtils;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class RepositorioFallaTecnica implements WithSimplePersistenceUnit {

  public static final RepositorioFallaTecnica INSTANCE = new RepositorioFallaTecnica();

  // EntityManagerFactory for creating EntityManagers
  private final EntityManagerFactory entityManagerFactory;

  private RepositorioFallaTecnica() {
    // Initialize the EntityManagerFactory with the persistence unit name
    this.entityManagerFactory = Persistence.createEntityManagerFactory("simple-persistence-unit");
  }

  // Method to get a new EntityManager when needed
  private EntityManager getEntityManager() {
    return entityManagerFactory.createEntityManager();
  }

  public Usuario findAny() {
    EntityManager entityManager = getEntityManager();
    try {
      return (Usuario) entityManager.createQuery("from FallaTecnica")
          .getResultList().get(0);
    } finally {
      entityManager.close();
    }
  }

  public void save(FallaTecnica fallaTecnica) {
    EntityManager entityManager = getEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    try {
      transaction.begin();
      entityManager.persist(fallaTecnica);
      transaction.commit();
    } catch (Exception e) {
      if (transaction.isActive()) transaction.rollback();
      throw e;
    } finally {
      entityManager.close();
    }
  }

  public List<FallaTecnica> findFallas(int page, int pageSize, String sortField, String sortOrder) {
    EntityManager entityManager = getEntityManager();
    try {
      // Constructing the sorting clause dynamically
      String queryStr = "SELECT f FROM FallaTecnica f ORDER BY f." + sortField + " " + sortOrder;
      TypedQuery<FallaTecnica> query = entityManager.createQuery(queryStr, FallaTecnica.class);
      query.setFirstResult((page - 1) * pageSize); // Set the offset
      query.setMaxResults(pageSize); // Limit the number of results

      return query.getResultList();
    } finally {
      entityManager.close();
    }
  }

  // Count total reports
  public long countFallas() {
    TypedQuery<Long> query = this.entityManager().createQuery("SELECT COUNT(f) FROM FallaTecnica f", Long.class);
    return query.getSingleResult();
  }


  public Object findAll() {
    return this.entityManager().createQuery("from FallaTecnica").getResultList();
  }

  public FallaTecnica findById(int fallaId) {
    return this.entityManager().find(FallaTecnica.class, fallaId);
  }

  public List<FallaTecnica> findFallasWithFilter(int page, int pageSize, String sortField, String sortOrder, Boolean solucionada, String searchType, String searchQuery) {
    EntityManager em = entityManager();
    try {
      String queryStr = "SELECT f FROM FallaTecnica f ";
      List<String> conditions = new ArrayList<>();

      if (solucionada != null) {
        conditions.add("f.solucionada = :solucionada");
      }
      if (!searchQuery.isEmpty()) {
        conditions.add("LOWER(f." + searchType + ") LIKE :searchQuery");
      }
      if (!conditions.isEmpty()) {
        queryStr += "WHERE " + String.join(" AND ", conditions);
      }

      queryStr += " ORDER BY f." + sortField + " " + sortOrder;

      TypedQuery<FallaTecnica> query = em.createQuery(queryStr, FallaTecnica.class);
      if (solucionada != null) {
        query.setParameter("solucionada", solucionada);
      }
      if (!searchQuery.isEmpty()) {
        query.setParameter("searchQuery", "%" + searchQuery + "%");
      }
      query.setFirstResult((page - 1) * pageSize);
      query.setMaxResults(pageSize);

      return query.getResultList();
    } finally {
      em.close();
    }
  }

  public void update(FallaTecnica fallaTecnica) {
    EntityManager em = entityManager();
    try {
      em.getTransaction().begin();
      em.merge(fallaTecnica);
      em.getTransaction().commit();
    } catch (Exception e) {
      throw new RuntimeException("Error updating FallaTecnica", e);
    }
  }
}
