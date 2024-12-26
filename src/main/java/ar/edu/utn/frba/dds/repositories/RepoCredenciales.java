package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.Usuario;
import ar.edu.utn.frba.dds.model.colaboradores.Credencial;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;

public class RepoCredenciales implements WithSimplePersistenceUnit  {

	public static RepoCredenciales INSTANCE = new RepoCredenciales();
	
	public Usuario findAny() {
		return (Usuario) this.entityManager().createQuery("from Usuario").getResultList().get(0);
	}
	
	public Usuario findByUsername(int username) {
		throw new RuntimeException("findByUsername aun no esta implementado");
	}

	public void registrar(Usuario usuario) {
		entityManager().persist(usuario);
	}

	public long contar() {
		return entityManager().createQuery("from Usuario").getResultStream().count();
	}
	public Credencial buscar(String usuario, String contrasenia) {
		Credencial credencial = entityManager()
				.createQuery("from Credencial where usuario = :usuario"
						, Credencial.class)
				.setParameter("usuario", usuario)
				.getResultList().get(0);

		if(credencial != null && credencial.contraseniaEsCorrecta(contrasenia)) {
			return credencial;
		}
		return null;
	}

	public Credencial buscar(int id) {
		Credencial credencial = entityManager()
				.createQuery("from Credencial where id = :id"
						, Credencial.class)
				.setParameter("id", id)
				.getResultList().get(0);

		if(credencial != null) {
			return credencial;
		}
		return null;
	}


}
