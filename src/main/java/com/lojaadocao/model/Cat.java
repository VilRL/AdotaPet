package com.lojaadocao.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Cat extends Animal {
    private String coatType; // Ex: Short-haired, Long-haired, Hairless, etc.

    @Override
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getType() {
        return "CAT";
    }
}