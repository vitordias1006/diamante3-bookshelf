package com.bookshelf.controller;

import com.bookshelf.dto.*;
import com.bookshelf.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/livros")
@RequiredArgsConstructor
@Tag(name = "Livros", description = "Gerenciamento de livros")
public class LivroController {

    private final LivroService livroService;

    @GetMapping
    @Operation(summary = "Listar todos os livros", description = "Retorna lista paginada com média de notas e total de resenhas")
    public ResponseEntity<Page<LivroResponseDTO>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "titulo") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction dir = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        return ResponseEntity.ok(livroService.listarTodos(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar livro por ID")
    public ResponseEntity<EntityModel<LivroResponseDTO>> buscarPorId(@PathVariable Long id) {
        LivroResponseDTO livro = livroService.buscarPorId(id);

        EntityModel<LivroResponseDTO> model = EntityModel.of(livro,
                linkTo(methodOn(LivroController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(LivroController.class).listarTodos(0, 10, "titulo", "asc")).withRel("livros"),
                linkTo(methodOn(AutorController.class).buscarPorId(livro.getAutorId())).withRel("autor"),
                linkTo(methodOn(ResenhaController.class).buscarPorLivro(id, 0, 10, "nota", "desc")).withRel("resenhas")
        );

        return ResponseEntity.ok(model);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar livros por título")
    public ResponseEntity<Page<LivroResumoDTO>> buscarPorTitulo(
            @RequestParam String titulo, Pageable pageable) {
        return ResponseEntity.ok(livroService.buscarPorTitulo(titulo, pageable));
    }

    @GetMapping("/genero/{genero}")
    @Operation(summary = "Buscar livros por gênero")
    public ResponseEntity<Page<LivroResumoDTO>> buscarPorGenero(
            @PathVariable String genero, Pageable pageable) {
        return ResponseEntity.ok(livroService.buscarPorGenero(genero, pageable));
    }

    @GetMapping("/autor/{autorId}")
    @Operation(summary = "Listar livros de um autor")
    public ResponseEntity<Page<LivroResponseDTO>> listarPorAutor(
            @PathVariable Long autorId,
            @Parameter @RequestParam(defaultValue = "0") int page,
            @Parameter @RequestParam(defaultValue = "10") int size,
            @Parameter @RequestParam(defaultValue = "titulo") String sort,
            @Parameter @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction dir = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        return ResponseEntity.ok(livroService.buscarPorAutor(autorId, pageable));
    }

    @GetMapping("/ano/{ano}")
    @Operation(summary = "Buscar livros por ano de publicação")
    public ResponseEntity<Page<LivroResumoDTO>> buscarPorAno(
            @PathVariable Integer ano, Pageable pageable) {
        return ResponseEntity.ok(livroService.buscarPorAno(ano, pageable));
    }

    @PostMapping
    @Operation(summary = "Criar novo livro")
    public ResponseEntity<EntityModel<LivroResponseDTO>> criar(@Valid @RequestBody LivroRequestDTO dto) {
        LivroResponseDTO livro = livroService.criar(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(livro.getId())
                .toUri();

        EntityModel<LivroResponseDTO> model = EntityModel.of(livro,
                linkTo(methodOn(LivroController.class).buscarPorId(livro.getId())).withSelfRel(),
                linkTo(methodOn(LivroController.class).listarTodos(0, 10, "titulo", "asc")).withRel("livros"),
                linkTo(methodOn(AutorController.class).buscarPorId(livro.getAutorId())).withRel("autor")
        );

        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar livro")
    public ResponseEntity<EntityModel<LivroResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody LivroRequestDTO dto) {

        LivroResponseDTO livro = livroService.atualizar(id, dto);

        EntityModel<LivroResponseDTO> model = EntityModel.of(livro,
                linkTo(methodOn(LivroController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(LivroController.class).listarTodos(0, 10, "titulo", "asc")).withRel("livros")
        );

        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar livro")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        livroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
