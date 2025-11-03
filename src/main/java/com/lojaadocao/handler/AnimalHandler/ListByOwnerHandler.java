package com.lojaadocao.handler.AnimalHandler;

import com.lojaadocao.dao.AnimalDAO;
import com.lojaadocao.handler.Handler;
import com.lojaadocao.model.Animal;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lojaadocao.util.HttpUtil;
import com.lojaadocao.util.Logger;

import java.util.List;

public class ListByOwnerHandler implements Handler<String, List<Animal>> {
    private final AnimalDAO dao = new AnimalDAO();
    private final ObjectMapper mapper = HttpUtil.getObjectMapper();

    @Override
    public List<Animal> execute(String body) {
        try {
            IdRequest request = mapper.readValue(body, IdRequest.class);
            return dao.findByOwner(request.getId());
        } catch (Exception e) {
            Logger.error("Error listing animals by owner", e);
            throw new RuntimeException("Error listing animals by owner: " + e.getMessage(), e);
        }
    }

    public static class IdRequest {
        private int id;
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
    }
}