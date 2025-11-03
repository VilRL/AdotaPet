package com.lojaadocao.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Dog extends Animal {
    @Override
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getType() {
        return "DOG";
    }
}