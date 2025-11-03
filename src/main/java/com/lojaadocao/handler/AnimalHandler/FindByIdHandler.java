package com.lojaadocao.handler.AnimalHandler;

import com.lojaadocao.dao.AnimalDAO;
import com.lojaadocao.handler.Handler;
import com.lojaadocao.model.Animal;

import java.util.Optional;

public class FindByIdHandler implements Handler<Integer, Optional<Animal>> {

    private final AnimalDAO dao = new AnimalDAO();

    @Override
    public Optional<Animal> execute(Integer id) {
        return dao.findById(id);
    }
}