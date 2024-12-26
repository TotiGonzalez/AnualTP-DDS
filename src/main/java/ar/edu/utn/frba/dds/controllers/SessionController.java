package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.colaboradores.ColaboradorFisico;
import ar.edu.utn.frba.dds.model.colaboradores.Credencial;
import ar.edu.utn.frba.dds.model.colaboradores.RepoColaboradores;
import ar.edu.utn.frba.dds.repositories.RepoCredenciales;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class SessionController {
    public void show(Context ctx) {
        Map<String, Object> model = new HashMap<>();
        model.put("usuario_id",ctx.sessionAttribute("usuario_id"));
        model.put("esAdmin",ctx.sessionAttribute("es_admin"));
        ctx.render("login.hbs", model);
    }

    public void create(Context ctx) {
        try {
            Credencial credencial = RepoCredenciales.INSTANCE.buscar(
                    ctx.formParam("nombre"),
                    ctx.formParam("password")
            );

            ctx.sessionAttribute("usuario_id", credencial.getId());
            ColaboradorFisico colaboradorFisico =
                    RepoColaboradores.getInstance().buscarColaboradorFisicoPorCredencialID(credencial);
            if (colaboradorFisico != null) {
                ctx.sessionAttribute("es_admin", colaboradorFisico.esAdmin());
            }
            ctx.redirect("/");

        } catch (Exception e) {
            Map<String, Object> model = new HashMap<>();
            e.printStackTrace();
            model.put("error", "El nombre de usuario y/o contraseña es inválido");
            model.put("usuario_id",ctx.sessionAttribute("usuario_id"));
            ctx.render("login.hbs", model);

        }
    }

    public void logout(Context ctx) {
        ctx.sessionAttribute("usuario_id", null);
        ctx.sessionAttribute("es_admin",null);

        ctx.render("home.hbs");
    }

}