package com.bookshelf.controller;

import com.bookshelf.dto.*;
import com.bookshelf.service.AutorService;
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
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/autores")
@RequiredArgsConstructor
@Tag(name = "Autores", description = "Gerenciamento de autores")
public class AutorController {

    private final AutorService autorService;

    @GetMapping
    @Operation(summary = "Listar todos os autores", description = "Retorna uma lista paginada de todos os autores")
    public ResponseEntity<Page<AutorResponseDTO>> listarTodos(
            @Parameter(description = "Número da página (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo de ordenação") @RequestParam(defaultValue = "nome") String sort,
            @Parameter(description = "Direção da ordenação") @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction dir = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        return ResponseEntity.ok(autorService.listarTodos(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar autor por ID")
    public ResponseEntity<EntityModel<AutorResponseDTO>> buscarPorId(@PathVariable Long id) {
        AutorResponseDTO autor = autorService.buscarPorId(id);

        EntityModel<AutorResponseDTO> model = EntityModel.of(autor,
                linkTo(methodOn(AutorController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(AutorController.class).listarTodos(0, 10, "nome", "asc")).withRel("autores"),
                linkTo(methodOn(LivroController.class).listarPorAutor(id, 0, 10, "titulo", "asc")).withRel("livros")
        );

        return ResponseEntity.ok(model);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar autores por nome")
    public ResponseEntity<Page<AutorResumoDTO>> buscarPorNome(
            @RequestParam String nome,
            Pageable pageable) {
        return ResponseEntity.ok(autorService.buscarPorNome(nome, pageable));
    }

    @GetMapping("/nacionalidade/{nacionalidade}")
    @Operation(summary = "Buscar autores por nacionalidade")
    public ResponseEntity<Page<AutorResumoDTO>> buscarPorNacionalidade(
            @PathVariable String nacionalidade,
            Pageable pageable) {
        return ResponseEntity.ok(autorService.buscarPorNacionalidade(nacionalidade, pageable));
    }

    @PostMapping
    @Operation(summary = "Criar novo autor")
    public ResponseEntity<EntityModel<AutorResponseDTO>> criar(@Valid @RequestBody AutorRequestDTO dto) {
        AutorResponseDTO autor = autorService.criar(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(autor.getId())
                .toUri();

        EntityModel<AutorResponseDTO> model = EntityModel.of(autor,
                linkTo(methodOn(AutorController.class).buscarPorId(autor.getId())).withSelfRel(),
                linkTo(methodOn(AutorController.class).listarTodos(0, 10, "nome", "asc")).withRel("autores")
        );

        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar autor")
    public ResponseEntity<EntityModel<AutorResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AutorRequestDTO dto) {

        AutorResponseDTO autor = autorService.atualizar(id, dto);

        EntityModel<AutorResponseDTO> model = EntityModel.of(autor,
                linkTo(methodOn(AutorController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(AutorController.class).listarTodos(0, 10, "nome", "asc")).withRel("autores")
        );

        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar autor")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        autorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
