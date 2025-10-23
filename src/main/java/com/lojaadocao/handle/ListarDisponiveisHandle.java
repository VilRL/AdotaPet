package com.lojaadocao.handle;

import com.lojaadocao.dao.AnimalDAO;
import com.lojaadocao.model.Animal;

import java.util.List;

public class ListarDisponiveisHandle implements Handle<Void, List<Animal>> {

    private final AnimalDAO dao = new AnimalDAO();

    @Override
    public List<Animal> executar(Void v) {
        return dao.listarDisponiveis();
    }
}
