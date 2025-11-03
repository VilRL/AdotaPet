package com.lojaadocao.handler;

import java.util.HashMap;
import java.util.Map;

public class HandlerDispatcher {
    private final Map<String, Handler<?, ?>> handlers = new HashMap<>();

    public void register(String method, String path, Handler<?, ?> handler) {
        String key = method.toUpperCase() + " " + path;
        handlers.put(key, handler);
    }

    @SuppressWarnings("unchecked")
    public <T, R> Handler<T, R> getHandler(String method, String path) {
        return (Handler<T, R>) handlers.get(method.toUpperCase() + " " + path);
    }
}