package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.scripts.Bootstrap;
import ar.edu.utn.frba.dds.server.templates.JavalinHandlebars;
import ar.edu.utn.frba.dds.server.templates.JavalinRenderer;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;

public class Server {
    public void start() {
        new Bootstrap().init();
        var app = Javalin.create(config -> {
            initializeStaticFiles(config);
            initializeTemplating(config);
        });

        new Router().configure(app);
        app.start(8080);
    }

    private void initializeTemplating(JavalinConfig config) {
        JavalinHandlebars handlebars = new JavalinHandlebars();

        config.fileRenderer(new JavalinRenderer().register("hbs", handlebars));
    }

    private static void initializeStaticFiles(JavalinConfig config) {
        config.staticFiles.add(staticFileConfig -> {
            staticFileConfig.hostedPath = "/public";
            staticFileConfig.directory = "/public";
        });
    }
}