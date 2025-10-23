package com.lojaadocao.handle;

import com.lojaadocao.dao.AnimalDAO;
import com.lojaadocao.handle.Handle;

import java.util.Optional;

public class AdotarAnimalHandle implements Handle<AdotarAnimalHandle.AdocaoRequest, Optional<AdotarAnimalHandle.AdocaoRequest>> {

    private final AnimalDAO dao = new AnimalDAO();

    @Override
    public Optional<AdocaoRequest> executar(AdocaoRequest request) {
        boolean ok = dao.adotar(request.getAnimalId(), request.getDonoId());
        if (ok) {
            request.setSucesso(true);
            return Optional.of(request);
        }
        return Optional.empty();
    }

    public static class AdocaoRequest {
        private int animalId;
        private int donoId;
        private boolean sucesso;

        public int getAnimalId() { return animalId; }
        public void setAnimalId(int animalId) { this.animalId = animalId; }
        public int getDonoId() { return donoId; }
        public void setDonoId(int donoId) { this.donoId = donoId; }
        public boolean isSucesso() { return sucesso; }
        public void setSucesso(boolean sucesso) { this.sucesso = sucesso; }
    }
}

