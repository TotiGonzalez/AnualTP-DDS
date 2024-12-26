package ar.edu.utn.frba.dds.model;

import ar.edu.utn.frba.dds.utils.RandomDateGenerator;
import com.github.jknack.handlebars.internal.lang3.StringUtils;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Entity
public class Captura extends PersistentObject {

	private String apodo;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Pokemon pokemon;
	private int nivel;
	private LocalDateTime fechaCaptura;

	public Captura(Pokemon pokemon, int nivel) {
		super();
		this.pokemon = pokemon;
		this.nivel = nivel;
		this.fechaCaptura = RandomDateGenerator.get();
	}

	protected Captura() { }

	public String getApodo() {
		return apodo;
	}

	public Pokemon getPokemon() {
		return pokemon;
	}

	public int getNivel() {
		return nivel;
	}

	public LocalDateTime getFechaCaptura() {
		return fechaCaptura;
	}

	public boolean getNuevo(){ return Period.between(fechaCaptura.toLocalDate(), LocalDate.now()).getDays() <= 1;  }

	@Override
	public String toString() {
		return "PokemonCapturado [nombre=" + apodo + ", pokemon=" + pokemon + ", nivel=" + nivel + ", fechaCaptura="
				+ fechaCaptura + "]";
	}

	public boolean sePareceNombreA(String nombre) {
		return StringUtils.startsWithIgnoreCase(this.apodo, nombre);
	}

	public void setApodo(String apodo) {
		this.apodo = apodo;
	}
}
