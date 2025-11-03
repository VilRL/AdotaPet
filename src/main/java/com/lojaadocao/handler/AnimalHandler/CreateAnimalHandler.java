package com.lojaadocao.handler.AnimalHandler;

import com.lojaadocao.dao.AnimalDAO;
import com.lojaadocao.handler.Handler;
import com.lojaadocao.model.Animal;

public class CreateAnimalHandler implements Handler<Animal, Animal> {

    private final AnimalDAO dao = new AnimalDAO();

    @Override
    public Animal execute(Animal animal) {
        if (animal.getStatus() == null) animal.setStatus("AVAILABLE");
        return dao.save(animal);
    }
}