package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.model.colaboraciones.DonacionVianda;
import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.model.colaboradores.ColaboradorFisico;
import ar.edu.utn.frba.dds.model.colaboradores.Credencial;
import ar.edu.utn.frba.dds.model.colaboradores.medioscontacto.Mail;
import ar.edu.utn.frba.dds.model.colaboradores.RepoColaboradores;
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
import ar.edu.utn.frba.dds.model.reportes.ViandasDonadasPorColaborador;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenaracionReporteTest {
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
    credencial.hashearContrasenia(credencial.getContrasenia());
    colaborador.setCredencial(credencial);

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
  public void testGenerarReporteDeEstaSemanaHeladeras() {
    // semana del 3/7/2024
    MovimientoVianda movimientoSemana1 = new MovimientoVianda(heladera1.getNombreHeladera(), 10,
        TipoMovimiento.RETIRO,
        LocalDateTime.of(2024, 9, 13, 13, 13));

    MovimientoVianda movimientoSemana1A = new MovimientoVianda(heladera1.getNombreHeladera(), 10,
        TipoMovimiento.INGRESO,
        LocalDateTime.of(2024, 9, 12, 12, 12));

    MovimientoVianda movimientoSemana1B = new MovimientoVianda(heladera3.getNombreHeladera(), 10,
        TipoMovimiento.RETIRO,
        LocalDateTime.of(2024, 9, 11, 14, 14));

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
    List<MovimientoVianda> movimientos = reporteViandasPorHeladera.getMovimientos();

    Assertions.assertTrue(movimientos.stream()
        .allMatch(mov -> reporteViandasPorHeladera.dentroDeEstaSemana(mov.getFechaYhora())));

    long cantidadSemana1 = movimientos.stream()
        .filter(mov -> reporteViandasPorHeladera.dentroDeEstaSemana(mov.getFechaYhora())).count();
    Assertions.assertEquals(3, cantidadSemana1);
  }

  @Test
  public void testGenerarReporteDeEstaSemanaColaboradores() {
    RepoColaboradores repoColaboradores = RepoColaboradores.getInstance();
    ColaboradorFisico colaboradorFisico = new ColaboradorFisico("Pedro", "Hernandez",
        LocalDate.of(2000, 9, 17));
    colaboradorFisico.setDireccion(
        new Direccion("CABA", "Caballito", "Bogotá", 43));

    ColaboradorFisico colaboradorFisico2 = new ColaboradorFisico("José", "Pineda",
        LocalDate.of(1993, 5, 3));
    colaboradorFisico2.setDireccion(
        new Direccion("Avellaneda", "Sarandi", "Gral Ferré", 211));

    repoColaboradores.agregarColaborador(colaboradorFisico);
    repoColaboradores.agregarColaborador(colaboradorFisico2);

    DonacionVianda donacionVianda =
        new DonacionVianda(colaboradorFisico, viandasGenericas(colaboradorFisico));
    donacionVianda.setFechaColaboracion(LocalDate.now().minusDays(6));
    colaboradorFisico.agregarColaboracion(donacionVianda);

    DonacionVianda donacionVianda1 =
        new DonacionVianda(colaboradorFisico2, viandasGenericas(colaboradorFisico2));
    donacionVianda1.setFechaColaboracion(LocalDate.now().minusDays(1));
    colaboradorFisico2.agregarColaboracion(donacionVianda1);

    DonacionVianda donacionVianda2 =
        new DonacionVianda(colaboradorFisico2, viandasGenericas(colaboradorFisico2));
    colaboradorFisico.agregarColaboracion(donacionVianda2);
    donacionVianda2.setFechaColaboracion(LocalDate.now().minusDays(9));

    reporteViandasPorColaborador.generar();


    int cantidadDonadaSemana2 =
        reporteViandasPorColaborador.getTodasViandasDonadas().stream()
            .mapToInt(ViandasDonadasPorColaborador::getCantidadViandasDonadas).sum();

    Assertions.assertEquals(6, cantidadDonadaSemana2);
  }

  private List<Vianda> viandasGenericas(Colaborador colaborador) {
    List<Vianda> viandas = new ArrayList<>();
    Vianda pan = new Vianda(new Comida("Pan"), LocalDate.now(), 200, 100);
    pan.setColaborador(colaborador);
    pan.setFechaDonacion(LocalDate.now());

    viandas.add(pan);
    viandas.add(pan);
    viandas.add(pan);
    return viandas;
  }

    @Test
public void testGenerarReporteDeEstaSemanaFallas() {
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
          colaborador, "descripcion","", "pathFotos");

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

      reporteFallasPorHeladera.generar();
      List<FallaTecnica> fallas = reporteFallasPorHeladera.getFallas();

      long cantidadSemana1 = fallas.stream()
          .filter(falla -> reporteFallasPorHeladera
              .dentroDeEstaSemana(falla.getFechaYhora())).count();
      Assertions.assertEquals(8, cantidadSemana1);
    }
}
