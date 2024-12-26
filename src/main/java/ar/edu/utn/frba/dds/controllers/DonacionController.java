package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.Captura;
import ar.edu.utn.frba.dds.model.Usuario;
import ar.edu.utn.frba.dds.repositories.RepoCredenciales;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DonacionController implements WithSimplePersistenceUnit, TransactionalOps {
    public Map<String, Object> index(@NotNull Context ctx) {
        Map model = new HashMap<>();
        model.put("usuario_id",ctx.sessionAttribute("usuario_id"));
        model.put("esAdmin",ctx.sessionAttribute("es_admin"));
        return model;
    }

    public Map<String, Object> show(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        Usuario usuario = RepoCredenciales.INSTANCE.findAny();

        Map model = showPokemon(usuario, id);
        return model;
    }

    public void save(Context ctx) {
        String apodo = ctx.formParam("apodo");
        Long id = Long.parseLong(ctx.pathParam("id"));
        Usuario usuario = RepoCredenciales.INSTANCE.findAny();

        withTransaction(() -> {
            usuario.getPokemonCapturado(id).setApodo(apodo);
        });

        ctx.result("Guardado!!");
    }

    @NotNull
    private static Map showPokemon(Usuario usuario, Long id) {
        Map model = new HashMap<>();
        model.put("usuario", usuario);
        model.put("captura", usuario.getPokemonCapturado(id));
        return model;
    }
}
