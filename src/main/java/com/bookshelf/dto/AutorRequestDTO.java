package com.bookshelf.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutorRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 150, message = "Nome deve ter entre 2 e 150 caracteres")
    private String nome;

    @Size(max = 100)
    private String nacionalidade;

    @Size(max = 2000)
    private String bio;

    private LocalDate dataNascimento;
}
