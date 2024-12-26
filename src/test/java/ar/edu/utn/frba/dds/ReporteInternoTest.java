package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.model.colaboraciones.Colaboracion;
import ar.edu.utn.frba.dds.model.colaboraciones.DonacionDinero;
import ar.edu.utn.frba.dds.model.colaboraciones.Frecuencia;
import ar.edu.utn.frba.dds.model.colaboradores.ColaboradorFisico;
import ar.edu.utn.frba.dds.model.colaboradores.Credencial;
import ar.edu.utn.frba.dds.model.colaboradores.medioscontacto.Mail;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Documento;
import ar.edu.utn.frba.dds.model.reporteinterno.LineaReporte;
import ar.edu.utn.frba.dds.model.reporteinterno.ReporteInterno;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Arrays;

public class

ReporteInternoTest {

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

  public Colaboracion crearColaboracion(ColaboradorFisico colaborador, LocalDate fechaColaboracion,
                                        LocalDate fechaNacimiento) {
    Colaboracion colaboracion;
    colaboracion = new DonacionDinero(colaborador, 10000, Frecuencia.DIARIA);
    colaboracion.setFechaColaboracion(fechaColaboracion);
    colaborador.setFechaNacimiento(fechaNacimiento);
    return colaboracion;
  }

  @Test
  public void getPuntaje_colaboradorNoTieneColaboraciones(){
    ColaboradorFisico colaborador = generarColaborador(Documento.DNI,123456777,
        "Juan","Gomez","jgomez@test.com");
    colaborador.agregarMedioContacto(new Mail("jgomez@test.com"));
    LineaReporte linea = new LineaReporte(colaborador);
    linea.asignarPuntaje();
    Assertions.assertEquals(0.0,linea.getPuntaje());
  }

  @Test
  public void generarReporte_colaboradorTieneColaboraciones(){
    ColaboradorFisico colaborador = generarColaborador(Documento.DNI,123456777,
        "Juan","Gomez","jgomez@test.com");
    colaborador.agregarMedioContacto(new Mail("jgomez@test.com"));

    Colaboracion donacionDinero = crearColaboracion(colaborador,LocalDate.now()
        ,LocalDate.of(2000,01,01));

    ReporteInterno reporteInterno = new ReporteInterno();
    reporteInterno.generarReporte(Arrays.asList(colaborador));
  }

}
