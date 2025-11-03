package com.lojaadocao.controller;

import com.lojaadocao.handler.HandlerDispatcher;
import com.lojaadocao.handler.OwnerHandler.*;
import com.lojaadocao.util.HttpUtil;
import com.lojaadocao.util.Logger;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class OwnerController implements HttpHandler {
    private final HandlerDispatcher dispatcher = new HandlerDispatcher();

    public OwnerController() {
        dispatcher.register("GET", "/owners", new ListOwnersHandler());
        dispatcher.register("POST", "/owners", new CreateOwnerHandler());
        dispatcher.register("POST", "/owners/findById", new FindOwnerByIdHandler());
        dispatcher.register("PUT", "/owners/update", new UpdateOwnerHandler());
        dispatcher.register("POST", "/owners/delete", new DeleteOwnerHandler());
        dispatcher.register("POST", "/owners/findByCpf", new FindOwnerByCpfHandler());
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        Logger.info("Request received: " + method + " " + path);

        try {
            var handler = dispatcher.getHandler(method, path);

            if (handler == null) {
                Logger.warn("Endpoint not found: " + method + " " + path);
                HttpUtil.sendText(exchange, 404, "Endpoint not found: " + method + " " + path);
                return;
            }

            String body = HttpUtil.readBody(exchange);
            Logger.info("Request body length: " + body.length());

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