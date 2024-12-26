package ar.edu.utn.frba.dds.model.colaboradores.medioscontacto;

import ar.edu.utn.frba.dds.db.EntidadPersistente;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 * hay 3 tipos de medio de contacto.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "medios_de_contacto")
public abstract class MedioContacto extends EntidadPersistente {

  public abstract void contactar(String mensaje);
}
