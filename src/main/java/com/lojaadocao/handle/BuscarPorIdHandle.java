package com.lojaadocao.handle;

import com.lojaadocao.dao.AnimalDAO;
import com.lojaadocao.model.Animal;

import java.util.Optional;

public class BuscarPorIdHandle implements Handle<Integer, Optional<Animal>> {

    private final AnimalDAO dao = new AnimalDAO();

    @Override
    public Optional<Animal> executar(Integer id) {
        return dao.findById(id);
    }
}
