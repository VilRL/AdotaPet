package com.lojaadocao;

import com.lojaadocao.controller.AnimalController;
import com.lojaadocao.controller.OwnerController;
import com.lojaadocao.util.DatabaseSetup;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws Exception {

        DatabaseSetup.createTables();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/animals", new AnimalController());
        server.createContext("/owners", new OwnerController());

        server.setExecutor(null);
        server.start();
        System.out.println("Server running at http://localhost:8080");
    }
}