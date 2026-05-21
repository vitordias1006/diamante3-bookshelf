package com.bookshelf.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "resenhas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resenha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Título da resenha é obrigatório")
    @Size(min = 3, max = 200)
    @Column(nullable = false)
    private String titulo;

    @NotBlank(message = "Conteúdo é obrigatório")
    @Size(min = 10, max = 5000)
    @Column(columnDefinition = "TEXT", nullable = false)
    private String conteudo;

    @NotNull(message = "Nota é obrigatória")
    @Min(value = 1, message = "Nota mínima é 1")
    @Max(value = 5, message = "Nota máxima é 5")
    @Column(nullable = false)
    private Integer nota;

    @Column(name = "tem_spoiler", nullable = false)
    @Builder.Default
    private Boolean temSpoiler = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livro_id", nullable = false)
    @JsonBackReference
    private Livro livro;
}
