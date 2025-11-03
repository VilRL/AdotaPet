package com.lojaadocao.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lojaadocao.dao.OwnerDAO;
import com.lojaadocao.model.Owner;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

public class OwnerController implements HttpHandler {

    private final OwnerDAO ownerDAO = new OwnerDAO();
    private final ObjectMapper objectMapper;

    public OwnerController() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if ("POST".equalsIgnoreCase(method) && path.equals("/owners")) {
                saveOwner(exchange);
            } else if ("GET".equalsIgnoreCase(method) && path.equals("/owners")) {
                listOwners(exchange);
            } else if ("GET".equalsIgnoreCase(method) && path.startsWith("/owners/")) {
                findOwner(exchange);
            } else if ("PUT".equalsIgnoreCase(method) && path.startsWith("/owners/")) {
                updateOwner(exchange);
            } else if ("DELETE".equalsIgnoreCase(method) && path.startsWith("/owners/")) {
                deleteOwner(exchange);
            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        } catch (Exception e) {
            String error = "Internal error: " + e.getMessage();
            exchange.sendResponseHeaders(500, error.length());
            exchange.getResponseBody().write(error.getBytes());
            exchange.close();
        }
    }

    private void saveOwner(HttpExchange exchange) throws IOException {
        Owner owner = objectMapper.readValue(exchange.getRequestBody(), Owner.class);
        Owner saved = ownerDAO.save(owner);

        String responseJson = objectMapper.writeValueAsString(saved);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(201, responseJson.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseJson.getBytes());
        }
    }

    private void listOwners(HttpExchange exchange) throws IOException {
        List<Owner> owners = ownerDAO.findAll();
        String responseJson = objectMapper.writeValueAsString(owners);

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, responseJson.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseJson.getBytes());
        }
    }

    private void findOwner(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        int id = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));

        Optional<Owner> owner = ownerDAO.findById(id);
        if (owner.isPresent()) {
            String responseJson = objectMapper.writeValueAsString(owner.get());
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, responseJson.getBytes().length);
            exchange.getResponseBody().write(responseJson.getBytes());
        } else {
            exchange.sendResponseHeaders(404, -1);
        }
        exchange.close();
    }

    private void updateOwner(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        int id = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));

        Owner owner = objectMapper.readValue(exchange.getRequestBody(), Owner.class);
        owner.setId(id);

        boolean updated = ownerDAO.update(owner);
        if (updated) {
            String responseJson = objectMapper.writeValueAsString(owner);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, responseJson.getBytes().length);
            exchange.getResponseBody().write(responseJson.getBytes());
        } else {
            exchange.sendResponseHeaders(404, -1);
        }
        exchange.close();
    }

    private void deleteOwner(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        int id = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));

        boolean deleted = ownerDAO.delete(id);
        if (deleted) {
            exchange.sendResponseHeaders(204, -1);
        } else {
            exchange.sendResponseHeaders(404, -1);
        }
        exchange.close();
    }
}