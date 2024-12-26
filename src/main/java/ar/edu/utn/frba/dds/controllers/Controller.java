package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.Usuario;
import ar.edu.utn.frba.dds.model.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.model.colaboradores.ColaboradorFisico;
import ar.edu.utn.frba.dds.repositories.RepoCredenciales;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class Controller implements WithSimplePersistenceUnit, TransactionalOps {
    protected Colaborador colaborador(Context ctx) {
        if(ctx.sessionAttribute("usuario_id") == null) {
            return null;
        }
        return entityManager()
                .find(Colaborador.class, ctx.sessionAttribute("usuario_id"));
    }
}
