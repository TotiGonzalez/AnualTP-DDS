package ar.edu.utn.frba.dds.db;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class EntidadPersistente {
  @Id
  @GeneratedValue
  private int id;
  public int getId(){
    return id;}
}