package com.bookshelf.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutorResponseDTO {
    private Long id;
    private String nome;
    private String nacionalidade;
    private String bio;
    private LocalDate dataNascimento;
    private int totalLivros;
}
