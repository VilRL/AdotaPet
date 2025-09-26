package com.lojaadocao;

import com.lojaadocao.controller.AnimalController;
import com.lojaadocao.controller.DonoController;
import com.lojaadocao.util.DatabaseSetup;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws Exception {

        DatabaseSetup.criarTabelas();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/animais", new AnimalController());
        server.createContext("/donos", new DonoController());

        server.setExecutor(null);
        server.start();
        System.out.println("Servidor rodando em http://localhost:8080");
    }
}
