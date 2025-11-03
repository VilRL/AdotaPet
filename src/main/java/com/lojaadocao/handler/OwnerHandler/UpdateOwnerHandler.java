package com.lojaadocao.handler.OwnerHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lojaadocao.dao.OwnerDAO;
import com.lojaadocao.handler.Handler;
import com.lojaadocao.model.Owner;
import com.lojaadocao.util.HttpUtil;
import com.lojaadocao.util.Logger;

public class UpdateOwnerHandler implements Handler<String, Boolean> {
    private final OwnerDAO dao = new OwnerDAO();
    private final ObjectMapper mapper = HttpUtil.getObjectMapper();

    @Override
    public Boolean execute(String body) {
        try {
            Owner owner = mapper.readValue(body, Owner.class);
            return dao.update(owner);
        } catch (Exception e) {
            Logger.error("Error updating owner", e);
            throw new RuntimeException("Error updating owner: " + e.getMessage(), e);
        }
    }
}
