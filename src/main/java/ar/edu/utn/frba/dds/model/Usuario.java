package ar.edu.utn.frba.dds.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Entity
public class Usuario extends PersistentObject {

	private String nombre;
	private String password;

	@OneToMany(cascade = CascadeType.PERSIST)
	private List<Captura> capturas = new LinkedList<>();

	protected Usuario() { }

	public List<Captura> getCapturas() {
		return capturas;
	}

	public Usuario(String nombre, String password) {
		super();
		this.nombre = nombre;
		this.password = password;
	}

	public String getNombre() {
		return nombre;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void capturar(Pokemon pokemon) {
		this.capturas.add(new Captura(pokemon, ThreadLocalRandom.current().nextInt(0, 100)));
	}

	@Override
	public String toString() {
		return "Usuario [usuario=" + nombre + ", capturas=" + capturas + "]";
	}

	public List<Captura> filtrarPorNombre(String nombre) {
		return this.capturas.stream().filter(captura -> captura.sePareceNombreA(nombre)).collect(Collectors.toList());
	}

	public Captura getPokemonCapturado(Long id) {
		return this.capturas.stream().filter(captura -> captura.getId().equals(id)).findFirst().get();
	}

}
