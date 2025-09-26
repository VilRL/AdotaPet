package com.lojaadocao.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lojaadocao.dao.AnimalDAO;
import com.lojaadocao.model.Animal;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AnimalController implements HttpHandler {

    private final AnimalDAO animalDAO = new AnimalDAO();
    private final ObjectMapper objectMapper;

    public AnimalController() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String rawPath = exchange.getRequestURI().getPath();
        System.out.println("REQ: " + method + " " + rawPath);

        try {
            if ("GET".equalsIgnoreCase(method) && "/animais".equals(rawPath)) {
                handleListAll(exchange);
                return;
            }
            if ("POST".equalsIgnoreCase(method) && "/animais".equals(rawPath)) {
                handlePost(exchange);
                return;
            }

            if ("GET".equalsIgnoreCase(method) && "/animais/disponiveis".equals(rawPath)) {
                handleListDisponiveis(exchange);
                return;
            }

            if ("GET".equalsIgnoreCase(method) && rawPath.startsWith("/animais/dono/")) {
                handleListPorDono(exchange, rawPath);
                return;
            }

            if ("POST".equalsIgnoreCase(method) && rawPath.matches("^/animais/\\d+/adotar$")) {
                handleAdotar(exchange, rawPath);
                return;
            }

            if (rawPath.matches("^/animais/\\d+$")) {
                if ("GET".equalsIgnoreCase(method)) {
                    handleGetById(exchange, rawPath);
                    return;
                } else if ("PUT".equalsIgnoreCase(method)) {
                    handlePut(exchange, rawPath);
                    return;
                } else if ("DELETE".equalsIgnoreCase(method)) {
                    handleDelete(exchange, rawPath);
                    return;
                }
            }

            if ("GET".equalsIgnoreCase(method) && "/".equals(rawPath)) {
                sendText(exchange, 200, "Servidor ativo!");
                return;
            }

            exchange.sendResponseHeaders(404, -1);
        } catch (Exception ex) {
            ex.printStackTrace();
            sendText(exchange, 500, "Erro interno: " + ex.getMessage());
        }
    }

    private void handleListAll(HttpExchange exchange) throws IOException {
        List<Animal> lista = animalDAO.findAll();
        sendJson(exchange, 200, lista);
    }

    private void handleListDisponiveis(HttpExchange exchange) throws IOException {
        List<Animal> lista = animalDAO.listarDisponiveis();
        sendJson(exchange, 200, lista);
    }

    private void handleListPorDono(HttpExchange exchange, String path) throws IOException {
        String[] parts = path.split("/");
        if (parts.length < 4) { sendText(exchange, 400, "donoId ausente"); return; }
        String donoIdStr = parts[3];
        int donoId;
        try { donoId = Integer.parseInt(donoIdStr); }
        catch (NumberFormatException e) { sendText(exchange, 400, "donoId inválido"); return; }

        List<Animal> lista = animalDAO.listarPorDono(donoId);
        sendJson(exchange, 200, lista);
    }

    private void handleGetById(HttpExchange exchange, String path) throws IOException {
        String[] parts = path.split("/");
        int id = Integer.parseInt(parts[2]);
        Optional<Animal> opt = animalDAO.findById(id);
        if (opt.isPresent()) sendJson(exchange, 200, opt.get());
        else sendText(exchange, 404, "Animal não encontrado: id=" + id);
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String body = readBody(exchange);
        System.out.println("Body POST: " + body);
        try {
            Animal animal = objectMapper.readValue(body, Animal.class);
            if (animal.getStatus() == null) animal.setStatus("DISPONIVEL");
            Animal salvo = animalDAO.salvar(animal);
            sendJson(exchange, 201, salvo);
        } catch (JsonProcessingException jpe) {
            jpe.printStackTrace();
            sendText(exchange, 400, "JSON inválido: " + jpe.getOriginalMessage());
        }
    }

    private void handlePut(HttpExchange exchange, String path) throws IOException {
        String[] parts = path.split("/");
        int id = Integer.parseInt(parts[2]);

        String body = readBody(exchange);
        try {
            Animal incoming = objectMapper.readValue(body, Animal.class);
            incoming.setId(id);
            boolean ok = animalDAO.atualizar(incoming);
            if (ok) {
                Optional<Animal> updated = animalDAO.findById(id);
                if (updated.isPresent()) sendJson(exchange, 200, updated.get());
                else sendText(exchange, 500, "Atualizado, mas não foi possível recuperar o registro");
            } else {
                sendText(exchange, 404, "Animal não encontrado: id=" + id);
            }
        } catch (JsonProcessingException jpe) {
            jpe.printStackTrace();
            sendText(exchange, 400, "JSON inválido: " + jpe.getOriginalMessage());
        }
    }

    private void handleDelete(HttpExchange exchange, String path) throws IOException {
        String[] parts = path.split("/");
        int id = Integer.parseInt(parts[2]);

        boolean removed = animalDAO.deletar(id);
        if (removed) exchange.sendResponseHeaders(204, -1);
        else sendText(exchange, 404, "Animal não encontrado: id=" + id);
    }

    private void handleAdotar(HttpExchange exchange, String path) throws IOException {
        String[] parts = path.split("/");
        int id = Integer.parseInt(parts[2]);

        String body = readBody(exchange);
        System.out.println("Body adotar: " + body);
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = objectMapper.readValue(body, Map.class);
            Object donoObj = map.get("donoId");
            if (donoObj == null) { sendText(exchange, 400, "donoId ausente no body"); return; }

            int donoId;
            if (donoObj instanceof Number) donoId = ((Number) donoObj).intValue();
            else {
                try { donoId = Integer.parseInt(donoObj.toString()); }
                catch (NumberFormatException nfe) { sendText(exchange, 400, "donoId inválido"); return; }
            }

            boolean ok = animalDAO.adotar(id, donoId);
            if (ok) {
                Optional<Animal> updated = animalDAO.findById(id);
                if (updated.isPresent()) sendJson(exchange, 200, updated.get());
                else sendText(exchange, 200, "Animal adotado (não foi possível buscar depois)");
            } else {
                sendText(exchange, 409, "Não foi possível adotar (talvez já adotado ou não existe).");
            }
        } catch (JsonProcessingException jpe) {
            jpe.printStackTrace();
            sendText(exchange, 400, "JSON inválido: " + jpe.getOriginalMessage());
        }
    }

    private String readBody(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody();
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            return sb.toString();
        }
    }

    private void sendJson(HttpExchange exchange, int statusCode, Object obj) throws IOException {
        String json = objectMapper.writeValueAsString(obj);
        byte[] out = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, out.length);
        try (OutputStream os = exchange.getResponseBody()) { os.write(out); }
    }

    private void sendText(HttpExchange exchange, int statusCode, String msg) throws IOException {
        byte[] out = msg.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, out.length);
        try (OutputStream os = exchange.getResponseBody()) { os.write(out); }
    }
}
