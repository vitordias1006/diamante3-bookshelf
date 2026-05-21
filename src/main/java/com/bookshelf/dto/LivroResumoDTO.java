package com.bookshelf.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LivroResumoDTO {
    private Long id;
    private String titulo;
    private String genero;
    private Integer anoPublicacao;
    private String autorNome;
    private double mediaNotas;
}
