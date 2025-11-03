package com.lojaadocao.controller;

import com.lojaadocao.handler.HandlerDispatcher;
import com.lojaadocao.handler.AdoptionHandler.AdoptAnimalHandler;
import com.lojaadocao.handler.AdoptionHandler.ListAvailableHandler;
import com.lojaadocao.handler.AnimalHandler.CreateAnimalHandler;
import com.lojaadocao.handler.AnimalHandler.ListAnimalsHandler;
import com.lojaadocao.util.HttpUtil;
import com.lojaadocao.util.Logger;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class AnimalController implements HttpHandler {
    private final HandlerDispatcher dispatcher = new HandlerDispatcher();

    public AnimalController() {
        dispatcher.register("GET", "/animals", new ListAnimalsHandler());
        dispatcher.register("POST", "/animals", new CreateAnimalHandler());
        dispatcher.register("GET", "/animals/available", new ListAvailableHandler());
        dispatcher.register("POST", "/animals/adopt", new AdoptAnimalHandler());
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        Logger.info("Request received: " + method + " " + path);

        var handler = dispatcher.getHandler(method, path);

        if (handler == null) {
            Logger.warn("Endpoint not found: " + method + " " + path);
            HttpUtil.sendText(exchange, 404, "Endpoint not found: " + method + " " + path);
            return;
        }

        String body = HttpUtil.readBody(exchange);

        try {
            Object result = handler.execute(body);
            if (result != null) {
                HttpUtil.sendJson(exchange, 200, result);
            } else {
                HttpUtil.sendText(exchange, 404, "Resource not found");
            }
        } catch (Exception e) {
            Logger.error("Error processing request: " + method + " " + path, e);
            HttpUtil.sendText(exchange, 500, "Error processing request: " + e.getMessage());
        }
    }
}