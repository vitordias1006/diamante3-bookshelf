package com.bookshelf.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LivroResponseDTO {
    private Long id;
    private String titulo;
    private String isbn;
    private Integer anoPublicacao;
    private String genero;
    private Integer paginas;
    private String sinopse;
    private String autorNome;
    private Long autorId;
    private double mediaNotas;
    private int totalResenhas;
}
