package ar.edu.utn.frba.dds.repositories;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Direccion;
import ar.edu.utn.frba.dds.model.heladeras.ConfiguracionHeladera;
import ar.edu.utn.frba.dds.model.heladeras.EstadoHeladera;
import ar.edu.utn.frba.dds.model.heladeras.Heladera;
import ar.edu.utn.frba.dds.model.heladeras.UbicacionPrecisa;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import java.util.List;

public class RepositorioHeladeras implements WithSimplePersistenceUnit {

  public static final RepositorioHeladeras INSTANCE = new RepositorioHeladeras();

  private EntityManagerFactory entityManagerFactory;

  private RepositorioHeladeras() {
    this.entityManagerFactory = Persistence.createEntityManagerFactory("simple-persistence-unit");
  }

  public List<Heladera> findAll() {
    EntityManager entityManager = getEntityManager();
    try {
      return entityManager.createQuery("from Heladera", Heladera.class).getResultList();
    } finally {
      entityManager.close();
    }
  }

  public Heladera findById(int id) {
    EntityManager entityManager = getEntityManager();
    try {
      return entityManager.find(Heladera.class, id);
    } finally {
      entityManager.close();
    }
  }

  public Heladera findByName(String name) {
    EntityManager entityManager = getEntityManager();
    try {
      return entityManager
          .createQuery("SELECT h FROM Heladera h WHERE h.nombreHeladera = :name", Heladera.class)
          .setParameter("name", name)
          .getSingleResult();
    } catch (NoResultException e) {
      return null;
    } finally {
      entityManager.close();
    }
  }

  private EntityManager getEntityManager() {
    return entityManagerFactory.createEntityManager();
  }

}
