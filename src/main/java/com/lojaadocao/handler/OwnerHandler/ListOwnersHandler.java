package com.lojaadocao.handler.OwnerHandler;

import com.lojaadocao.dao.OwnerDAO;
import com.lojaadocao.handler.Handler;
import com.lojaadocao.model.Owner;

import java.util.List;

public class ListOwnersHandler implements Handler<String, List<Owner>> {
    private final OwnerDAO dao = new OwnerDAO();

    @Override
    public List<Owner> execute(String body) {
        return dao.findAll();
    }
}