package com.bookshelf.controller;

import com.bookshelf.dto.*;
import com.bookshelf.service.ResenhaService;
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
@RequestMapping("/api/resenhas")
@RequiredArgsConstructor
@Tag(name = "Resenhas", description = "Gerenciamento de resenhas de livros")
public class ResenhaController {

    private final ResenhaService resenhaService;

    @GetMapping
    @Operation(summary = "Listar todas as resenhas")
    public ResponseEntity<Page<ResenhaResponseDTO>> listarTodas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nota") String sort,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort.Direction dir = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        return ResponseEntity.ok(resenhaService.listarTodas(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar resenha por ID")
    public ResponseEntity<EntityModel<ResenhaResponseDTO>> buscarPorId(@PathVariable Long id) {
        ResenhaResponseDTO resenha = resenhaService.buscarPorId(id);

        EntityModel<ResenhaResponseDTO> model = EntityModel.of(resenha,
                linkTo(methodOn(ResenhaController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(ResenhaController.class).listarTodas(0, 10, "nota", "desc")).withRel("resenhas"),
                linkTo(methodOn(LivroController.class).buscarPorId(resenha.getLivroId())).withRel("livro")
        );

        return ResponseEntity.ok(model);
    }

    @GetMapping("/livro/{livroId}")
    @Operation(summary = "Listar resenhas de um livro")
    public ResponseEntity<Page<ResenhaResponseDTO>> buscarPorLivro(
            @PathVariable Long livroId,
            @Parameter @RequestParam(defaultValue = "0") int page,
            @Parameter @RequestParam(defaultValue = "10") int size,
            @Parameter @RequestParam(defaultValue = "nota") String sort,
            @Parameter @RequestParam(defaultValue = "desc") String direction) {

        Sort.Direction dir = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        return ResponseEntity.ok(resenhaService.buscarPorLivro(livroId, pageable));
    }

    @GetMapping("/livro/{livroId}/sem-spoiler")
    @Operation(summary = "Listar resenhas de um livro sem spoilers")
    public ResponseEntity<Page<ResenhaResponseDTO>> buscarPorLivroSemSpoiler(
            @PathVariable Long livroId, Pageable pageable) {
        return ResponseEntity.ok(resenhaService.buscarPorLivroSemSpoiler(livroId, pageable));
    }

    @GetMapping("/nota/{nota}")
    @Operation(summary = "Buscar resenhas por nota (1-5)")
    public ResponseEntity<Page<ResenhaResponseDTO>> buscarPorNota(
            @PathVariable Integer nota, Pageable pageable) {
        return ResponseEntity.ok(resenhaService.buscarPorNota(nota, pageable));
    }

    @PostMapping
    @Operation(summary = "Criar nova resenha")
    public ResponseEntity<EntityModel<ResenhaResponseDTO>> criar(@Valid @RequestBody ResenhaRequestDTO dto) {
        ResenhaResponseDTO resenha = resenhaService.criar(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(resenha.getId())
                .toUri();

        EntityModel<ResenhaResponseDTO> model = EntityModel.of(resenha,
                linkTo(methodOn(ResenhaController.class).buscarPorId(resenha.getId())).withSelfRel(),
                linkTo(methodOn(LivroController.class).buscarPorId(resenha.getLivroId())).withRel("livro"),
                linkTo(methodOn(ResenhaController.class).buscarPorLivro(resenha.getLivroId(), 0, 10, "nota", "desc")).withRel("resenhas-do-livro")
        );

        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar resenha")
    public ResponseEntity<EntityModel<ResenhaResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ResenhaRequestDTO dto) {

        ResenhaResponseDTO resenha = resenhaService.atualizar(id, dto);

        EntityModel<ResenhaResponseDTO> model = EntityModel.of(resenha,
                linkTo(methodOn(ResenhaController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(LivroController.class).buscarPorId(resenha.getLivroId())).withRel("livro")
        );

        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar resenha")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        resenhaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
