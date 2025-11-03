package com.lojaadocao.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Dog extends Animal {
    private String breedGroup; // Ex: Sporting, Working, Terrier, etc.

    @Override
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getType() {
        return "DOG";
    }
}