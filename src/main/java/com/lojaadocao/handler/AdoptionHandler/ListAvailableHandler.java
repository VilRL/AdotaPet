package com.lojaadocao.handler.AdoptionHandler;

import com.lojaadocao.dao.AnimalDAO;
import com.lojaadocao.handler.Handler;
import com.lojaadocao.model.Animal;

import java.util.List;

public class ListAvailableHandler implements Handler<Void, List<Animal>> {

    private final AnimalDAO dao = new AnimalDAO();

    @Override
    public List<Animal> execute(Void v) {
        return dao.findAvailable();
    }
}