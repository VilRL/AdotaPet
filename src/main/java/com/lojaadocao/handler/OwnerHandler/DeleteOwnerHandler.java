package com.lojaadocao.handler.OwnerHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lojaadocao.dao.OwnerDAO;
import com.lojaadocao.handler.Handler;
import com.lojaadocao.util.HttpUtil;
import com.lojaadocao.util.Logger;

public class DeleteOwnerHandler implements Handler<String, Boolean> {
    private final OwnerDAO dao = new OwnerDAO();
    private final ObjectMapper mapper = HttpUtil.getObjectMapper();

    @Override
    public Boolean execute(String body) {
        try {
            IdRequest request = mapper.readValue(body, IdRequest.class);
            return dao.delete(request.getId());
        } catch (Exception e) {
            Logger.error("Error deleting owner", e);
            throw new RuntimeException("Error deleting owner: " + e.getMessage(), e);
        }
    }

    public static class IdRequest {
        private int id;
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
    }
}
