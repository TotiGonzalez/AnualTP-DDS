package ar.edu.utn.frba.dds.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.model.colaboradores.ColaboradorFisico;
import ar.edu.utn.frba.dds.model.colaboradores.ColaboradorJuridico;
import ar.edu.utn.frba.dds.model.colaboradores.RepoColaboradores;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Direccion;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.TipoJuridico;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;

public class MapaColaboradoresTestDB implements SimplePersistenceTest  {

  private final RepoColaboradores repo = RepoColaboradores.getInstance();

  @BeforeEach
  public void setUp() {
    entityManager().createQuery("delete from Colaboracion").executeUpdate();
    entityManager().createQuery("delete from MedioContacto ").executeUpdate();
    entityManager().createQuery("delete from Colaborador").executeUpdate();

    ColaboradorFisico pedro = new ColaboradorFisico(
        "Pedro", "Hernandez", LocalDate.of(2000, 9, 17));
    pedro.setDireccion(new Direccion("CABA", "Caballito", "Bogotá", 43));

    repo.registrar(pedro);

    ColaboradorFisico pablo = new ColaboradorFisico(
        "Pablo", "Gimenez", LocalDate.of(1999, 8, 18));
    pablo.setDireccion(new Direccion("CABA", "Caballito", "Bogotá", 243));

    repo.registrar(pablo);

    ColaboradorFisico jose = new ColaboradorFisico(
        "José", "Pineda", LocalDate.of(1993, 5, 3));
    jose.setDireccion(new Direccion("CABA", "Flores", "Nazca", 23));

    repo.registrar(jose);

    ColaboradorJuridico colaboradorJuridico = new ColaboradorJuridico(
        TipoJuridico.EMPRESA, "S.A.", "Alimentos");
    colaboradorJuridico.setDireccion(new Direccion("Avellaneda", "Sarandi", "Gral Ferré", 211));

    repo.registrar(colaboradorJuridico);
  }

  @Test
  public void testFiltrarListaPorLocalidad() {
    List<Colaborador> listaFiltrados = repo.filtrarPorLocalidad("Caballito");
    assertEquals(2, listaFiltrados.size());
  }


  @Test
  public void testFiltrarListaPorTipoColaborador() {
    List<ColaboradorFisico> fisicos = repo.todosLosFisicos();
    assertEquals(3, fisicos.size());

    List<ColaboradorJuridico> juridicos = repo.todosLosJuridicos();
    assertEquals(1, juridicos.size());
  }

  @Test
  public void leerBaseDeDatos(){
    assertEquals(4, repo.buscarTodos().size());
  }
}
