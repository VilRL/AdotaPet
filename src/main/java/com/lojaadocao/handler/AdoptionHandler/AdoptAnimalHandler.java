package com.lojaadocao.handler.AdoptionHandler;

import com.lojaadocao.dao.AnimalDAO;
import com.lojaadocao.handler.Handler;

import java.util.Optional;

public class AdoptAnimalHandler implements Handler<AdoptAnimalHandler.AdoptionRequest, Optional<AdoptAnimalHandler.AdoptionRequest>> {

    private final AnimalDAO dao = new AnimalDAO();

    @Override
    public Optional<AdoptionRequest> execute(AdoptionRequest request) {
        boolean ok = dao.adopt(request.getAnimalId(), request.getOwnerId());
        if (ok) {
            request.setSuccess(true);
            return Optional.of(request);
        }
        return Optional.empty();
    }

    public static class AdoptionRequest {
        private int animalId;
        private int ownerId;
        private boolean success;

        public int getAnimalId() { return animalId; }
        public void setAnimalId(int animalId) { this.animalId = animalId; }
        public int getOwnerId() { return ownerId; }
        public void setOwnerId(int ownerId) { this.ownerId = ownerId; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
    }
}