package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.AboutController;
import ar.edu.utn.frba.dds.controllers.DonacionController;
import ar.edu.utn.frba.dds.controllers.DonacionDineroController;
import ar.edu.utn.frba.dds.controllers.DonacionDistribucionController;
import ar.edu.utn.frba.dds.controllers.DonacionHeladeraController;
import ar.edu.utn.frba.dds.controllers.DonacionViandaController;
import ar.edu.utn.frba.dds.controllers.HomeController;
import ar.edu.utn.frba.dds.controllers.SessionController;
import ar.edu.utn.frba.dds.controllers.*;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class Router implements SimplePersistenceTest {
    public void configure(Javalin app) {
        HomeController homeController = new HomeController();
        AboutController aboutController = new AboutController();
        SessionController sessionController = new SessionController();
        DonacionController donacionController = new DonacionController();
        AdminController adminController = new AdminController();
        DonacionHeladeraController donacionheladeraController = new DonacionHeladeraController();
        DonacionDineroController donaciondineroController = new DonacionDineroController();
        DonacionViandaController donacionviandaController = new DonacionViandaController();
        DonacionDistribucionController donaciondistribucionController = new DonacionDistribucionController();


        AltaFallaController altaFallaController = new AltaFallaController();
        GestionFallaTecnicaController gestionFallaTecnicaController = new GestionFallaTecnicaController();

        GestionHeladerasController gestionHeladerasController = new GestionHeladerasController();

        app.before(ctx -> {
            entityManager().clear();
        });

        //TODO limpiar clases de pokemon y código extra; las dejé de guía
        app.get("/", context -> context.redirect("/home"));
        app.get("/home", ctx -> ctx.render("home.hbs", homeController.index(ctx)));
        app.get("/about", ctx -> ctx.render("about.hbs", aboutController.index(ctx)));
        app.get("/donacion/index", ctx -> ctx.render("donacion-index.hbs", donacionController.index(ctx)));
        app.get("/donacion/index/donacionheladera", ctx -> ctx.render("donacionheladera.hbs", donacionheladeraController.index(ctx)));
        app.get("/donacion/index/donaciondinero", ctx -> ctx.render("donaciondinero.hbs", donaciondineroController.index(ctx)));
        app.get("/donacion/index/donacionvianda", ctx -> ctx.render("donacionvianda.hbs", donacionviandaController.index(ctx)));
        app.get("/donacion/index/donaciondistribucion", ctx -> ctx.render("donaciondistribucion.hbs", donaciondistribucionController.index(ctx)));


        //TODO limpiar clases de pokemon y código extra; las dejé de guía
        app.get("/capturas/{id}", ctx -> ctx.render("pokemon.hbs", homeController.show(ctx)));
        app.put("/capturas/{id}", ctx -> homeController.save(ctx));
        app.get("/login", sessionController::show);
        app.post("/login", sessionController::create);
        app.get("/logout", sessionController::logout);
        app.get("/admin/index", adminController::show);

        app.post("/admin/index", ctx -> {
            String action = ctx.formParam("action");
            if ("asignar".equals(action)) {
                adminController.create(ctx);
            } else if ("remover".equals(action)) {
                adminController.delete(ctx);
            }
        });

        app.post("/donacion/index/donacionheladera", donacionheladeraController::saveForm);

        app.get("/heladeras/search", new AltaFallaController()::searchHeladeras);
        app.get("/gestionheladeras", ctx -> ctx.render("gestion-heladeras.hbs", gestionHeladerasController.index(ctx)));

        app.get("/fallas", ctx -> ctx.render("gestion-reportes-index.hbs", gestionFallaTecnicaController.index(ctx)));
        app.get("/fallas/new", ctx -> ctx.render("reportefalla.hbs", altaFallaController.showForm(ctx)));
        app.post("/altaFalla", altaFallaController::processForm);

        app.post("/fallas", altaFallaController::create);
        app.get("/fallas/{id}", ctx -> ctx.render("falla-detalles.hbs", gestionFallaTecnicaController.show(ctx)));
        app.post("/fallas/{id}/{action}", gestionFallaTecnicaController::toggleFallaState);


        app.get("/personavulnerable/new",ctx -> ctx.render("registrarpv.hbs", RegistrarPersonaVulnerableController.index(ctx)));
        app.get("/colaboradorfisico/new",ctx -> ctx.render("colafisico.hbs", RegistrarColaboradorFisicoController.index(ctx)));
        app.get("/colaboradorjuridico/new",ctx -> ctx.render("colajuridico.hbs", RegistrarColaboradorJuridicoController.index(ctx)));

        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace(); // Log the full stack trace for debugging
            ctx.status(500);
            ctx.result("Server encountered an error: " + e.getMessage());
        });

    }
}