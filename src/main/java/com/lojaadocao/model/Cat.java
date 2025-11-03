package com.lojaadocao.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Cat extends Animal {
    @Override
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getType() {
        return "CAT";
    }
}