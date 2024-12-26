package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.colaboradores.ColaboradorFisico;
import ar.edu.utn.frba.dds.model.colaboradores.Credencial;
import ar.edu.utn.frba.dds.model.colaboradores.RepoColaboradores;
import ar.edu.utn.frba.dds.model.colaboradores.datoscolaborador.Documento;
import ar.edu.utn.frba.dds.repositories.RepoCredenciales;
import io.javalin.http.Context;

import javax.persistence.EntityTransaction;
import java.util.*;

public class AdminController {
    public void show(Context ctx){
        Map<String, Object> model = new HashMap<>();
        model.put("usuario_id",ctx.sessionAttribute("usuario_id"));
        model.put("esAdmin",ctx.sessionAttribute("es_admin"));

        int offset = 0;
        int limite = 4;

        String offsetString = ctx.queryParam("offset");
        if(offsetString != null){
            offset = Integer.parseInt(offsetString);
        }

        List<ColaboradorFisico> colaboradorFisicos = RepoColaboradores.getInstance().todosLosFisicos();
        List<ColaboradorFisico> colaboradoresParaMostrar = new ArrayList<>();

        if (colaboradorFisicos != null && !colaboradorFisicos.isEmpty()) {
            for (int i = offset; i < offset + limite && i < colaboradorFisicos.size(); i++) {
                colaboradoresParaMostrar.add(colaboradorFisicos.get(i));
            }
            model.put("colaboradores", colaboradoresParaMostrar);
        }

        int offsetSiguiente = offset + limite;
        int offsetAnterior = offset - limite;

        if (offsetSiguiente > colaboradorFisicos.size()) {
            model.put("anterior", "/admin/index");
        }
        else {
            model.put("siguiente", "/admin/index?offset=" + offsetSiguiente);
        }

        if (offsetAnterior > 0){
            model.put("anterior",  "/admin/index?offset=" +offsetAnterior);
        }
        else {
            model.put("anterior", "/admin/index");
        }

        ctx.render("admin-rol.hbs", model);
}

public void create(Context ctx) {
    try {
        ColaboradorFisico colaboradorFisico = RepoColaboradores.getInstance()
                .buscarColaboradorFisicoPorID(Integer.parseInt(ctx.formParams("colaborador").get(0)));

        if (colaboradorFisico != null) {
            colaboradorFisico.setEsAdmin(true);
            ctx.sessionAttribute("es_admin", true);
            EntityTransaction tx = RepoColaboradores.getInstance().getTransaction();
            tx.begin();

            RepoColaboradores.getInstance().persist(colaboradorFisico);
            tx.commit();
        }

        Map<String, Object> model = new HashMap<>();

        ctx.redirect("/admin/index");

    } catch (Exception e) {
        Map<String, Object> model = new HashMap<>();
        e.printStackTrace();
        ctx.redirect("/");

    }
}
    public void delete(Context ctx) {
        try {
            ColaboradorFisico colaboradorFisico = RepoColaboradores.getInstance()
                    .buscarColaboradorFisicoPorID(Integer.parseInt(ctx.formParams("colaborador").get(0)));

            if (colaboradorFisico != null && colaboradorFisico.esAdmin()) {
                colaboradorFisico.setEsAdmin(false);
                ctx.sessionAttribute("es_admin", false);
                EntityTransaction tx = RepoColaboradores.getInstance().getTransaction();
                tx.begin();

                RepoColaboradores.getInstance().persist(colaboradorFisico);
                tx.commit();
            }

            Map<String, Object> model = new HashMap<>();

            ctx.redirect("/admin/index");

        } catch (Exception e) {
            Map<String, Object> model = new HashMap<>();
            e.printStackTrace();
            ctx.redirect("/");

        }
    }
}
