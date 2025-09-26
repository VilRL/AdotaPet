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
        property = "tipo",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Gato.class, name = "GATO"),
        @JsonSubTypes.Type(value = Cachorro.class, name = "CACHORRO")
})
@Data
@NoArgsConstructor
public abstract class Animal {
    private Integer id;
    private String nome;
    private Integer idade;
    private String raca;
    private String sexo;
    private String porte;
    private Boolean castrado;
    private String status;
    private Integer donoId;

    private LocalDate chegadaDate;
    private LocalDateTime dataAdocao;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public abstract String getTipo();
}
