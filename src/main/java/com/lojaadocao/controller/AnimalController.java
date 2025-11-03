package com.lojaadocao.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lojaadocao.handler.*;
import com.lojaadocao.handler.AdoptionHandler.AdoptAnimalHandler;
import com.lojaadocao.handler.AdoptionHandler.ListAvailableHandler;
import com.lojaadocao.handler.AnimalHandler.CreateAnimalHandler;
import com.lojaadocao.handler.AnimalHandler.ListAnimalsHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class AnimalController implements HttpHandler {
    private final HandlerDispatcher dispatcher = new HandlerDispatcher();
    private final ObjectMapper objectMapper;

    public AnimalController() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        dispatcher.register("GET", "/animals", new ListAnimalsHandler());
        dispatcher.register("POST", "/animals", new CreateAnimalHandler());
        dispatcher.register("GET", "/animals/available", new ListAvailableHandler());
        dispatcher.register("POST", "/animals/adopt", new AdoptAnimalHandler());
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        Handler<String, ?> handler = dispatcher.getHandler(method, path);

        if (handler == null) {
            sendText(exchange, 404, "Endpoint not found: " + method + " " + path);
            return;
        }

        String body = readBody(exchange);

        try {
            Object result = handler.execute(body);
            sendJson(exchange, 200, result);
        } catch (Exception e) {
            e.printStackTrace();
            sendText(exchange, 500, "Error processing request: " + e.getMessage());
        }
    }

    private String readBody(HttpExchange exchange) throws IOException {
        try (InputStream inputStream = exchange.getRequestBody()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private void sendJson(HttpExchange exchange, int statusCode, Object data) throws IOException {
        String json = objectMapper.writeValueAsString(data);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

    private void sendText(HttpExchange exchange, int statusCode, String text) throws IOException {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }
}