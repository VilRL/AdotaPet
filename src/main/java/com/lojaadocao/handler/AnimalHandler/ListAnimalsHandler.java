package com.lojaadocao.handler.AnimalHandler;

import com.lojaadocao.dao.AnimalDAO;
import com.lojaadocao.handler.Handler;
import com.lojaadocao.model.Animal;

import java.util.List;

public class ListAnimalsHandler implements Handler<String, List<Animal>> {
    private final AnimalDAO dao = new AnimalDAO();

    @Override
    public List<Animal> execute(String body) {
        return dao.findAll();
    }
}
