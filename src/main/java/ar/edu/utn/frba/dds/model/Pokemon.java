package ar.edu.utn.frba.dds.model;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
@Entity
public class Pokemon extends PersistentObject{

	private int pokedexNumber;
	private TipoPokemon tipo;
	private String nombre;
	@OneToOne
	private Pokemon evolucion;
	@ElementCollection
	private List<String> fotos;

	public Pokemon(int pokedexNumber, String nombre, TipoPokemon tipo, Pokemon evolucion) {
		super();
		this.tipo = tipo;
		this.nombre = nombre;
		this.evolucion = evolucion;
		this.pokedexNumber = pokedexNumber;
		this.fotos = new ArrayList<>();
	}

	protected Pokemon() {	}

	public TipoPokemon getTipo() {
		return tipo;
	}

	public String getNombre() {
		return nombre;
	}

	public Pokemon getEvolucion() {
		return evolucion;
	}

	public String principalImage() {
		return String.format("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/dream-world/%d.svg", this.pokedexNumber);
	}

	@Override
	public String toString() {
		return "Pokemon [tipo=" + tipo + ", nombre=" + nombre + ", fotos=" + fotos + "]";
	}

}
