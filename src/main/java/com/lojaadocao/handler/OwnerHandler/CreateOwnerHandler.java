package com.lojaadocao.handler.OwnerHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lojaadocao.dao.OwnerDAO;
import com.lojaadocao.handler.Handler;
import com.lojaadocao.model.Owner;
import com.lojaadocao.util.HttpUtil;
import com.lojaadocao.util.Logger;

public class CreateOwnerHandler implements Handler<String, Owner> {
    private final OwnerDAO dao = new OwnerDAO();
    private final ObjectMapper mapper = HttpUtil.getObjectMapper();

    @Override
    public Owner execute(String body) {
        try {
            Owner owner = mapper.readValue(body, Owner.class);
            return dao.save(owner);
        } catch (Exception e) {
            Logger.error("Error creating owner", e);
            throw new RuntimeException("Error creating owner: " + e.getMessage(), e);
        }
    }
}
