package ar.edu.utn.frba.dds.db;

import ar.edu.utn.frba.dds.model.colaboradores.ColaboradorFisico;
import ar.edu.utn.frba.dds.model.colaboradores.Credencial;
import ar.edu.utn.frba.dds.model.colaboradores.medioscontacto.Mail;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Direccion;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Documento;
import ar.edu.utn.frba.dds.model.heladeras.Comida;
import ar.edu.utn.frba.dds.model.heladeras.ConfiguracionHeladera;
import ar.edu.utn.frba.dds.model.heladeras.EstadoHeladera;
import ar.edu.utn.frba.dds.model.heladeras.FallaTecnica;
import ar.edu.utn.frba.dds.model.heladeras.Heladera;
import ar.edu.utn.frba.dds.model.heladeras.MapaHeladeras;
import ar.edu.utn.frba.dds.model.heladeras.UbicacionPrecisa;
import ar.edu.utn.frba.dds.model.heladeras.Vianda;
import ar.edu.utn.frba.dds.model.reportes.MovimientoVianda;
import ar.edu.utn.frba.dds.model.reportes.ReporteFallas;
import ar.edu.utn.frba.dds.model.reportes.ReporteViandasPorColaborador;
import ar.edu.utn.frba.dds.model.reportes.ReporteViandasPorHeladera;
import ar.edu.utn.frba.dds.model.reportes.TipoMovimiento;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityTransaction;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PersistenciaReportesTest implements WithSimplePersistenceUnit {
  private Heladera heladera1;
  private Heladera heladera2;
  private Heladera heladera3;
  private Heladera heladera4;
  private ReporteViandasPorHeladera reporteViandasPorHeladera;
  private ReporteViandasPorColaborador reporteViandasPorColaborador;
  private ReporteFallas reporteFallasPorHeladera;
  private MapaHeladeras mapaHeladeras;
  private ColaboradorFisico colaborador;

  public ColaboradorFisico generarColaborador(Documento documento, Integer valorDocumento, String nombreDeColaborador,
                                              String apellidoDeColaborador, String mailDeColaborador){

    ColaboradorFisico colaborador = new ColaboradorFisico(nombreDeColaborador,apellidoDeColaborador
        , LocalDate.of(2000,1,1));
    Credencial credencial = new Credencial(valorDocumento.toString(),"!"+nombreDeColaborador
        .substring(3)+"L@Sn10"+apellidoDeColaborador.substring(2));
    colaborador.setCredencial(credencial);
    credencial.hashearContrasenia(credencial.getContrasenia());

    colaborador.setTipoDocumento(documento);
    colaborador.setNumeroDocumento(valorDocumento);
    colaborador.agregarMedioContacto(new Mail(mailDeColaborador));

    return colaborador;
  }

  @BeforeEach
  void setUp() {
    UbicacionPrecisa ubicacion1 = new UbicacionPrecisa("Ciudad1",
        new Direccion("Calle1",
            "Barrio1", "falsa",123), 10.0, 20.0);
    UbicacionPrecisa ubicacion2 = new UbicacionPrecisa("Ciudad2",
        new Direccion("Calle2",
            "Barrio2", "mas falsa", 456), 30.0, 40.0);

    ConfiguracionHeladera config1 = new ConfiguracionHeladera(5.0,
        25.0, 3);
    ConfiguracionHeladera config2 = new ConfiguracionHeladera(0.0,
        30.0, 3);

    heladera1 = new Heladera("heladera1", ubicacion1,
        EstadoHeladera.ACTIVA, 100, config1);
    heladera2 = new Heladera("heladera2", ubicacion2, EstadoHeladera.ACTIVA, 100, config2);
    heladera3 = new Heladera("heladera3", ubicacion1,
        EstadoHeladera.ACTIVA, 100, config2);
    heladera4 = new Heladera("heladera4", ubicacion2,
        EstadoHeladera.ACTIVA, 100, config1);

    mapaHeladeras = MapaHeladeras.getInstance();
    mapaHeladeras.agregarHeladera(heladera1);
    mapaHeladeras.agregarHeladera(heladera2);
    mapaHeladeras.agregarHeladera(heladera3);
    mapaHeladeras.agregarHeladera(heladera4);

    colaborador = generarColaborador(Documento.DNI,123456777,
        "Juan","Gomez","jgomez@test.com");
    colaborador.agregarMedioContacto(new Mail("jgomez@test.com"));

    Vianda vianda = new Vianda(
        new Comida("comida"),
        LocalDate.now(),
        500,
        250
    );

    vianda.setFechaDonacion(LocalDate.now().plusDays(7));
    vianda.setColaborador(colaborador);
    vianda.setHeladera(heladera1);
    vianda.fueEntregada();

    List<Vianda> viandas = new ArrayList<>();
    viandas.add(vianda);
    viandas.add(vianda);
    viandas.add(vianda);
    viandas.add(vianda);

    this.reporteViandasPorHeladera = new ReporteViandasPorHeladera();
    this.reporteViandasPorColaborador = new ReporteViandasPorColaborador();
    this.reporteFallasPorHeladera = new ReporteFallas();
  }

  @Test
  public void persistirReporteViandasPorHeladera() {
    // semana del 3/7/2024
    MovimientoVianda movimientoSemana1 = new MovimientoVianda(heladera1.getNombreHeladera(), 10,
        TipoMovimiento.RETIRO,
        LocalDateTime.of(2024, 7, 3, 13, 13));

    MovimientoVianda movimientoSemana1A = new MovimientoVianda(heladera1.getNombreHeladera(), 10,
        TipoMovimiento.INGRESO,
        LocalDateTime.of(2024, 7, 2, 12, 12));

    MovimientoVianda movimientoSemana1B = new MovimientoVianda(heladera3.getNombreHeladera(), 10,
        TipoMovimiento.RETIRO,
        LocalDateTime.of(2024, 7, 4, 14, 14));

    // semana del 26/06/2024
    MovimientoVianda movimientoSemana2 = new MovimientoVianda(heladera2.getNombreHeladera(), 2,
        TipoMovimiento.INGRESO,
        LocalDateTime.of(2024, 6, 26, 16, 16));

    MovimientoVianda movimientoSemana2B = new MovimientoVianda(heladera4.getNombreHeladera(), 5,
        TipoMovimiento.RETIRO,
        LocalDateTime.of(2024, 6, 27, 17, 17));

    heladera1.agregarMovimientoVianda(movimientoSemana1);
    heladera1.agregarMovimientoVianda(movimientoSemana1A);
    heladera3.agregarMovimientoVianda(movimientoSemana1B);
    heladera2.agregarMovimientoVianda(movimientoSemana2);
    heladera4.agregarMovimientoVianda(movimientoSemana2B);

    reporteViandasPorHeladera.generar();

    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();

    entityManager().persist(movimientoSemana1A);
    entityManager().persist(movimientoSemana2B);
    entityManager().persist(movimientoSemana1);
    entityManager().persist(movimientoSemana1B);
    entityManager().persist(movimientoSemana2);

    entityManager().persist(reporteViandasPorHeladera);
    tx.commit();
  }

  @Test
  public void persistirReporteFallasPorHeladera() {

    //esta semana
    FallaTecnica fallaTecnica1 = new FallaTecnica(heladera1,
        LocalDateTime.of(2024, 9, 10, 17, 17),
        colaborador, "descripcion","titulo", "pathFotos");

    FallaTecnica fallaTecnica2 = new FallaTecnica(heladera1,
        LocalDateTime.of(2024, 9, 11, 17, 17),
        colaborador, "descripcion","titulo", "pathFotos");

    FallaTecnica fallaTecnica3 = new FallaTecnica(heladera1,
        LocalDateTime.of(2024, 9, 12, 17, 17),
        colaborador, "descripcion","titulo", "pathFotos");

    FallaTecnica fallaTecnica4 = new FallaTecnica(heladera1,
        LocalDateTime.of(2024, 9, 13, 17, 17),
        colaborador, "descripcion","titulo", "pathFotos");

    //anteriores
    FallaTecnica fallaTecnica5 = new FallaTecnica(heladera1,
        LocalDateTime.of(2024, 2, 2, 17, 17),
        colaborador, "descripcion","titulo", "pathFotos");

    FallaTecnica fallaTecnica6 = new FallaTecnica(heladera1,
        LocalDateTime.of(2024, 2, 4, 17, 17),
        colaborador, "descripcion","titulo", "pathFotos");

    heladera1.agregarFallaTecnica(fallaTecnica1);
    heladera1.agregarFallaTecnica(fallaTecnica2);
    heladera1.agregarFallaTecnica(fallaTecnica3);
    heladera1.agregarFallaTecnica(fallaTecnica4);
    heladera1.agregarFallaTecnica(fallaTecnica5);
    heladera1.agregarFallaTecnica(fallaTecnica6);

    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();
    entityManager().persist(reporteFallasPorHeladera);

    ReporteFallas persistedReporteFallas = entityManager()
        .find(ReporteFallas.class, reporteFallasPorHeladera.getId());

    assertNotNull(persistedReporteFallas);
    assertEquals(reporteFallasPorHeladera.getFallas(),
        persistedReporteFallas.getFallas());

    tx.commit();
  }

  @Test
  public void eliminarReporteViandasPorHeladera() {

    persistirReporteViandasPorHeladera();

    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();

    ReporteViandasPorHeladera reporteAEliminar = entityManager().find(ReporteViandasPorHeladera.class,
        reporteViandasPorHeladera.getId());

    entityManager().remove(reporteAEliminar);

    tx.commit();

    assertEquals(1, entityManager()
        .createQuery("SELECT r FROM ReporteViandasPorHeladera r",
            ReporteViandasPorHeladera.class)
        .getResultList().size());
  }



}
