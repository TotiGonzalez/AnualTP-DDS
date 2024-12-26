package ar.edu.utn.frba.dds.db.convertidores;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;

@Converter(autoApply = true)
public class ConvertidorLocalDate implements AttributeConverter<LocalDate, Date> {
  @Override
  public Date convertToDatabaseColumn(LocalDate localDate) {
    return localDate == null? null: Date.valueOf(localDate);
  }

  @Override
  public LocalDate convertToEntityAttribute(Date sqlDate) {
    return sqlDate == null? null : sqlDate.toLocalDate();
  }
}