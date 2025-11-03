package com.lojaadocao.handler.AdoptionHandler;

import com.lojaadocao.dao.AnimalDAO;
import com.lojaadocao.handler.Handler;
import com.lojaadocao.model.Animal;

import java.util.List;

public class ListAvailableHandler implements Handler<String, java.util.List<com.lojaadocao.model.Animal>> {
    private final AnimalDAO dao = new AnimalDAO();

    @Override
    public java.util.List<com.lojaadocao.model.Animal> execute(String body) {
        return dao.findAvailable();
    }
}