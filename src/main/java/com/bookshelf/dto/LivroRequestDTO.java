package com.bookshelf.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LivroRequestDTO {

    @NotBlank(message = "Título é obrigatório")
    @Size(min = 1, max = 300)
    private String titulo;

    @Size(max = 20)
    private String isbn;

    private Integer anoPublicacao;

    @Size(max = 100)
    private String genero;

    @Min(value = 1, message = "Número de páginas deve ser positivo")
    private Integer paginas;

    @Size(max = 2000)
    private String sinopse;

    @NotNull(message = "ID do autor é obrigatório")
    private Long autorId;
}
