package com.lojaadocao.handler.AnimalHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lojaadocao.dao.AnimalDAO;
import com.lojaadocao.handler.Handler;
import com.lojaadocao.model.Animal;
import com.lojaadocao.util.HttpUtil;
import com.lojaadocao.util.Logger;

public class CreateAnimalHandler implements Handler<String, Animal> {
    private final AnimalDAO dao = new AnimalDAO();
    private final ObjectMapper mapper = HttpUtil.getObjectMapper();

    @Override
    public Animal execute(String body) {
        try {
            Animal animal = mapper.readValue(body, Animal.class);
            if (animal.getStatus() == null) animal.setStatus("AVAILABLE");
            return dao.save(animal);
        } catch (Exception e) {
            Logger.error("Error creating animal", e);
            throw new RuntimeException("Error creating animal: " + e.getMessage(), e);
        }
    }
}