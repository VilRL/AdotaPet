package com.lojaadocao.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Cat.class, name = "CAT"),
        @JsonSubTypes.Type(value = Dog.class, name = "DOG")
})
@Data
@NoArgsConstructor
public abstract class Animal {
    private Integer id;
    private String name;
    private Integer age;
    private String breed;
    private String gender;
    private String size;
    private Boolean neutered;
    private String status;
    private Integer ownerId;
    private LocalDate arrivalDate;
    private LocalDateTime adoptionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public abstract String getType();
}