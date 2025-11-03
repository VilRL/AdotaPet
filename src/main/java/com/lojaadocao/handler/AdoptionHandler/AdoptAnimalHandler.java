package com.lojaadocao.handler.AdoptionHandler;

import com.lojaadocao.dao.AnimalDAO;
import com.lojaadocao.handler.Handler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lojaadocao.util.HttpUtil;
import com.lojaadocao.util.Logger;

public class AdoptAnimalHandler implements Handler<String, AdoptAnimalHandler.AdoptionResponse> {
    private final AnimalDAO dao = new AnimalDAO();
    private final ObjectMapper mapper = HttpUtil.getObjectMapper();

    @Override
    public AdoptionResponse execute(String body) {
        try {
            AdoptionRequest request = mapper.readValue(body, AdoptionRequest.class);
            boolean success = dao.adopt(request.getAnimalId(), request.getOwnerId());
            return new AdoptionResponse(success, success ? "Animal adopted successfully" : "Animal not available for adoption");
        } catch (Exception e) {
            Logger.error("Error adopting animal", e);
            throw new RuntimeException("Error adopting animal: " + e.getMessage(), e);
        }
    }

    public static class AdoptionRequest {
        private int animalId;
        private int ownerId;

        public int getAnimalId() { return animalId; }
        public void setAnimalId(int animalId) { this.animalId = animalId; }
        public int getOwnerId() { return ownerId; }
        public void setOwnerId(int ownerId) { this.ownerId = ownerId; }
    }

    public static class AdoptionResponse {
        private boolean success;
        private String message;

        public AdoptionResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }
}