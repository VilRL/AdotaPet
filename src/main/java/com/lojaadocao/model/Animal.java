package com.lojaadocao.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Animal {
    private Integer id;
    private String nome;
    private Integer idade;
    private String tipo;
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
}
