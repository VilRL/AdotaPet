package com.lojaadocao.handle;


import com.lojaadocao.dao.AnimalDAO;
import com.lojaadocao.model.Animal;

import java.util.List;

public class ListarPorDonoHandle implements Handle<Integer, List<Animal>> {

    private final AnimalDAO dao = new AnimalDAO();

    @Override
    public List<Animal> executar(Integer donoId) {
        return dao.listarPorDono(donoId);
    }
}
