package com.lojaadocao.handle;

import com.lojaadocao.dao.AnimalDAO;
import com.lojaadocao.model.Animal;

public class CriarAnimalHandle implements Handle<Animal, Animal> {

    private final AnimalDAO dao = new AnimalDAO();

    @Override
    public Animal executar(Animal animal) {
        if (animal.getStatus() == null) animal.setStatus("DISPONIVEL");
        return dao.salvar(animal);
    }
}
