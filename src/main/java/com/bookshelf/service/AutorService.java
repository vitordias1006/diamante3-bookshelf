package com.bookshelf.service;

import com.bookshelf.dto.*;
import com.bookshelf.exception.ResourceNotFoundException;
import com.bookshelf.model.Autor;
import com.bookshelf.repository.AutorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutorService {

    private final AutorRepository autorRepository;

    @Cacheable(value = "autores")
    @Transactional(readOnly = true)
    public Page<AutorResponseDTO> listarTodos(Pageable pageable) {
        log.info("Buscando lista de autores - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        return autorRepository.findAll(pageable)
                .map(this::toResponseDTO);
    }

    @Cacheable(value = "autor", key = "#id")
    @Transactional(readOnly = true)
    public AutorResponseDTO buscarPorId(Long id) {
        log.info("Buscando autor por ID: {}", id);
        Autor autor = autorRepository.findByIdWithLivros(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado com ID: " + id));
        return toResponseDTO(autor);
    }

    @Transactional(readOnly = true)
    public Page<AutorResumoDTO> buscarPorNome(String nome, Pageable pageable) {
        return autorRepository.findByNomeContainingIgnoreCase(nome, pageable)
                .map(this::toResumoDTO);
    }

    @Transactional(readOnly = true)
    public Page<AutorResumoDTO> buscarPorNacionalidade(String nacionalidade, Pageable pageable) {
        return autorRepository.findByNacionalidadeIgnoreCase(nacionalidade, pageable)
                .map(this::toResumoDTO);
    }

    @CacheEvict(value = {"autores", "autor"}, allEntries = true)
    @Transactional
    public AutorResponseDTO criar(AutorRequestDTO dto) {
        log.info("Criando novo autor: {}", dto.getNome());
        Autor autor = Autor.builder()
                .nome(dto.getNome())
                .nacionalidade(dto.getNacionalidade())
                .bio(dto.getBio())
                .dataNascimento(dto.getDataNascimento())
                .build();
        return toResponseDTO(autorRepository.save(autor));
    }

    @CacheEvict(value = {"autores", "autor"}, allEntries = true)
    @Transactional
    public AutorResponseDTO atualizar(Long id, AutorRequestDTO dto) {
        log.info("Atualizando autor ID: {}", id);
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado com ID: " + id));

        autor.setNome(dto.getNome());
        autor.setNacionalidade(dto.getNacionalidade());
        autor.setBio(dto.getBio());
        autor.setDataNascimento(dto.getDataNascimento());

        return toResponseDTO(autorRepository.save(autor));
    }

    @CacheEvict(value = {"autores", "autor"}, allEntries = true)
    @Transactional
    public void deletar(Long id) {
        log.info("Deletando autor ID: {}", id);
        if (!autorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Autor não encontrado com ID: " + id);
        }
        autorRepository.deleteById(id);
    }

    private AutorResponseDTO toResponseDTO(Autor autor) {
        return AutorResponseDTO.builder()
                .id(autor.getId())
                .nome(autor.getNome())
                .nacionalidade(autor.getNacionalidade())
                .bio(autor.getBio())
                .dataNascimento(autor.getDataNascimento())
                .totalLivros(autor.getLivros() != null ? autor.getLivros().size() : 0)
                .build();
    }

    private AutorResumoDTO toResumoDTO(Autor autor) {
        return AutorResumoDTO.builder()
                .id(autor.getId())
                .nome(autor.getNome())
                .nacionalidade(autor.getNacionalidade())
                .totalLivros(autorRepository.countLivrosByAutorId(autor.getId()))
                .build();
    }
}
