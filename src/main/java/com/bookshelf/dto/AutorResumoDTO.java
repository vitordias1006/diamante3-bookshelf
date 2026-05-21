package com.bookshelf.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutorResumoDTO {
    private Long id;
    private String nome;
    private String nacionalidade;
    private int totalLivros;
}
