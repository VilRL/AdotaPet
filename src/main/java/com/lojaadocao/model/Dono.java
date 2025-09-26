package com.lojaadocao.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dono {
    private Integer id;
    private String nome;
    private String cpf;
    private String email;
    private LocalDateTime dataNascimento;
    private String telefone;
    private String endereco;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
