package com.lojaadocao.handler.AnimalHandler;

import com.lojaadocao.dao.AnimalDAO;
import com.lojaadocao.handler.Handler;
import com.lojaadocao.model.Animal;

import java.util.List;

public class ListByOwnerHandler implements Handler<Integer, List<Animal>> {

    private final AnimalDAO dao = new AnimalDAO();

    @Override
    public List<Animal> execute(Integer ownerId) {
        return dao.findByOwner(ownerId);
    }
}