package ar.edu.utn.frba.dds.scripts;

import ar.edu.utn.frba.dds.model.Pokemon;
import ar.edu.utn.frba.dds.model.Usuario;
import ar.edu.utn.frba.dds.model.colaboradores.RepoColaboradores;
import ar.edu.utn.frba.dds.model.importador.ImportadorColaboracion;
import ar.edu.utn.frba.dds.repositories.RepoCredenciales;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bootstrap implements WithSimplePersistenceUnit {
	public void init() {

		new ImportadorColaboracion().importarColaboracion(RepoColaboradores.getInstance(), "src/main/resources/colaboraciones.csv", true);

		try {
			// Read the SQL file content into a string
			String sql = new String(Files.readAllBytes(Paths.get("src/main/resources/heladeras.sql")));

			// Execute the SQL content
			entityManager().getTransaction().begin();
			entityManager().createNativeQuery(sql).executeUpdate();
			entityManager().getTransaction().commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			entityManager().close();
		}

	}
/*
	private static List<Pokemon> pokemons() {
		/*Gson gson = new GsonBuilder().setPrettyPrinting().create();
		ClassLoader classLoader = Bootstrap.class.getClassLoader();
		List<Pokemon> pokemons = new ArrayList<>();
		try (Reader reader = new InputStreamReader(classLoader.getResourceAsStream("pokedex.json"))) {
			PokedexList pokedex = gson.fromJson(reader, PokedexList.class);

			pokemons = pokedex.getResults().stream().map(dto -> new Pokemon(
					dto.getNumber(),
					StringUtils.capitalize(dto.getName()),
					null,
					null
			)).toList();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return pokemons;

	}*/
/*
	private static List<Usuario> usuarios() {
		return Arrays.asList(new Usuario("gaston", "gaston"),
		new Usuario("lucas", "lucas"));
	}*/

}
