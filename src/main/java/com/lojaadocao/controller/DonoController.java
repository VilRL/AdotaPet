package com.lojaadocao.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lojaadocao.dao.DonoDAO;
import com.lojaadocao.model.Dono;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

public class DonoController implements HttpHandler {

    private final DonoDAO donoDAO = new DonoDAO();
    private final ObjectMapper objectMapper;

    public DonoController() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // suporte a LocalDateTime
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if ("POST".equalsIgnoreCase(method) && path.equals("/donos")) {
                salvarDono(exchange);
            } else if ("GET".equalsIgnoreCase(method) && path.equals("/donos")) {
                listarDonos(exchange);
            } else if ("GET".equalsIgnoreCase(method) && path.startsWith("/donos/")) {
                buscarDono(exchange);
            } else if ("PUT".equalsIgnoreCase(method) && path.startsWith("/donos/")) {
                atualizarDono(exchange);
            } else if ("DELETE".equalsIgnoreCase(method) && path.startsWith("/donos/")) {
                deletarDono(exchange);
            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        } catch (Exception e) {
            String error = "Erro interno: " + e.getMessage();
            exchange.sendResponseHeaders(500, error.length());
            exchange.getResponseBody().write(error.getBytes());
            exchange.close();
        }
    }

    private void salvarDono(HttpExchange exchange) throws IOException {
        Dono dono = objectMapper.readValue(exchange.getRequestBody(), Dono.class);
        Dono salvo = donoDAO.salvar(dono);

        String responseJson = objectMapper.writeValueAsString(salvo);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(201, responseJson.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseJson.getBytes());
        }
    }

    private void listarDonos(HttpExchange exchange) throws IOException {
        List<Dono> donos = donoDAO.findAll();
        String responseJson = objectMapper.writeValueAsString(donos);

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, responseJson.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseJson.getBytes());
        }
    }

    private void buscarDono(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        int id = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));

        Optional<Dono> dono = donoDAO.findById(id);
        if (dono.isPresent()) {
            String responseJson = objectMapper.writeValueAsString(dono.get());
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, responseJson.getBytes().length);
            exchange.getResponseBody().write(responseJson.getBytes());
        } else {
            exchange.sendResponseHeaders(404, -1);
        }
        exchange.close();
    }

    private void atualizarDono(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        int id = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));

        Dono dono = objectMapper.readValue(exchange.getRequestBody(), Dono.class);
        dono.setId(id);

        boolean atualizado = donoDAO.atualizar(dono);
        if (atualizado) {
            String responseJson = objectMapper.writeValueAsString(dono);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, responseJson.getBytes().length);
            exchange.getResponseBody().write(responseJson.getBytes());
        } else {
            exchange.sendResponseHeaders(404, -1);
        }
        exchange.close();
    }

    private void deletarDono(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        int id = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));

        boolean deletado = donoDAO.deletar(id);
        if (deletado) {
            exchange.sendResponseHeaders(204, -1);
        } else {
            exchange.sendResponseHeaders(404, -1);
        }
        exchange.close();
    }
}
