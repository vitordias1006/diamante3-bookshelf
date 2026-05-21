package com.bookshelf.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResenhaRequestDTO {

    @NotBlank(message = "Título é obrigatório")
    @Size(min = 3, max = 200)
    private String titulo;

    @NotBlank(message = "Conteúdo é obrigatório")
    @Size(min = 10, max = 5000)
    private String conteudo;

    @NotNull(message = "Nota é obrigatória")
    @Min(value = 1, message = "Nota mínima é 1")
    @Max(value = 5, message = "Nota máxima é 5")
    private Integer nota;

    private Boolean temSpoiler = false;

    @NotNull(message = "ID do livro é obrigatório")
    private Long livroId;
}
