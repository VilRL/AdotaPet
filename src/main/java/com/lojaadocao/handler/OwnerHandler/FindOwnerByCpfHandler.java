package com.lojaadocao.handler.OwnerHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lojaadocao.dao.OwnerDAO;
import com.lojaadocao.handler.Handler;
import com.lojaadocao.model.Owner;
import com.lojaadocao.util.HttpUtil;
import com.lojaadocao.util.Logger;

import java.util.List;
import java.util.Optional;

public class FindOwnerByCpfHandler implements Handler<String, Optional<Owner>> {
    private final OwnerDAO dao = new OwnerDAO();
    private final ObjectMapper mapper = HttpUtil.getObjectMapper();

    @Override
    public Optional<Owner> execute(String body) {
        try {
            CpfRequest request = mapper.readValue(body, CpfRequest.class);
            return dao.findByCpf(request.getCpf());
        } catch (Exception e) {
            Logger.error("Error finding owner by CPF", e);
            throw new RuntimeException("Error finding owner by CPF: " + e.getMessage(), e);
        }
    }

    public static class CpfRequest {
        private String cpf;
        public String getCpf() { return cpf; }
        public void setCpf(String cpf) { this.cpf = cpf; }
    }
}