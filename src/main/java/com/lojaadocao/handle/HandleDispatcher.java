package com.lojaadocao.handle;

import java.util.HashMap;
import java.util.Map;

public class HandleDispatcher {
    private final Map<String, Handle<?, ?>> handlers = new HashMap<>();

    public void registrar(String metodo, String caminho, Handle<?, ?> handle) {
        String chave = metodo.toUpperCase() + " " + caminho;
        handlers.put(chave, handle);
    }

    @SuppressWarnings("unchecked")
    public <T, R> Handle<T, R> getHandle(String metodo, String caminho) {
        return (Handle<T, R>) handlers.get(metodo.toUpperCase() + " " + caminho);
    }
}