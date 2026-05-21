package com.bookshelf.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResenhaResponseDTO {
    private Long id;
    private String titulo;
    private String conteudo;
    private Integer nota;
    private Boolean temSpoiler;
    private Long livroId;
    private String livroTitulo;
}
