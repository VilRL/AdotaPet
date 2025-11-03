package com.lojaadocao.handler.AnimalHandler;

import com.lojaadocao.dao.AnimalDAO;
import com.lojaadocao.handler.Handler;
import com.lojaadocao.model.Animal;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lojaadocao.util.HttpUtil;
import com.lojaadocao.util.Logger;

public class FindByIdHandler implements Handler<String, Animal> {
    private final AnimalDAO dao = new AnimalDAO();
    private final ObjectMapper mapper = HttpUtil.getObjectMapper();

    @Override
    public Animal execute(String body) {
        try {
            IdRequest request = mapper.readValue(body, IdRequest.class);
            return dao.findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("Animal not found with id: " + request.getId()));
        } catch (Exception e) {
            Logger.error("Error finding animal by id", e);
            throw new RuntimeException("Error finding animal by id: " + e.getMessage(), e);
        }
    }

    public static class IdRequest {
        private int id;
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
    }
}