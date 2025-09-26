package com.lojaadocao.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Cachorro extends Animal {
    @Override
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getTipo() {
        return "CACHORRO";
    }
}
