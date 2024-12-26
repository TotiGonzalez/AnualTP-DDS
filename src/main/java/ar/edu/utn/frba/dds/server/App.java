package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.scripts.Bootstrap;

public class App {

    public static void main(String[] args) {
        new Bootstrap().init();
        new Server().start();


    }
}

