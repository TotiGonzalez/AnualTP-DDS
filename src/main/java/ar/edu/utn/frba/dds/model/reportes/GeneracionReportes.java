package ar.edu.utn.frba.dds.model.reportes;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import ar.edu.utn.frba.dds.db.EntidadPersistente;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

//no va a ser joined porque no comparten atributo
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class GeneracionReportes extends EntidadPersistente {

  abstract void generar();

  public boolean dentroDeEstaSemana(LocalDateTime fecha) {
    LocalDateTime inicioDeSemana = LocalDateTime.now()
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    LocalDateTime finalDeSemana = LocalDateTime.now()
        .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

    return ((fecha.isAfter(inicioDeSemana)
        && fecha.isBefore(finalDeSemana)
        || this.esElMismoDia(inicioDeSemana, fecha)
        || this.esElMismoDia(finalDeSemana, fecha)));
  }

  //si no esta entre semana, es el lunes o domingo de esta semana
  public boolean esElMismoDia(LocalDateTime lunesOdomingo, LocalDateTime fecha) {
    return lunesOdomingo.getDayOfMonth() == fecha.getDayOfMonth()
        && lunesOdomingo.getMonth() == fecha.getMonth()
        && lunesOdomingo.getYear() == fecha.getYear();
  }
}
