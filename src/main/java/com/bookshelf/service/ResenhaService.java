package com.bookshelf.service;

import com.bookshelf.dto.*;
import com.bookshelf.exception.ResourceNotFoundException;
import com.bookshelf.model.Livro;
import com.bookshelf.model.Resenha;
import com.bookshelf.repository.LivroRepository;
import com.bookshelf.repository.ResenhaRepository;
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
public class ResenhaService {

    private final ResenhaRepository resenhaRepository;
    private final LivroRepository livroRepository;

    @Cacheable(value = "resenhas")
    @Transactional(readOnly = true)
    public Page<ResenhaResponseDTO> listarTodas(Pageable pageable) {
        log.info("Listando todas as resenhas");
        return resenhaRepository.findAll(pageable)
                .map(this::toResponseDTO);
    }

    @Cacheable(value = "resenha", key = "#id")
    @Transactional(readOnly = true)
    public ResenhaResponseDTO buscarPorId(Long id) {
        log.info("Buscando resenha ID: {}", id);
        Resenha resenha = resenhaRepository.findByIdWithLivro(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resenha não encontrada com ID: " + id));
        return toResponseDTO(resenha);
    }

    @Transactional(readOnly = true)
    public Page<ResenhaResponseDTO> buscarPorLivro(Long livroId, Pageable pageable) {
        if (!livroRepository.existsById(livroId)) {
            throw new ResourceNotFoundException("Livro não encontrado com ID: " + livroId);
        }
        return resenhaRepository.findByLivroId(livroId, pageable)
                .map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<ResenhaResponseDTO> buscarPorNota(Integer nota, Pageable pageable) {
        return resenhaRepository.findByNota(nota, pageable)
                .map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<ResenhaResponseDTO> buscarPorLivroSemSpoiler(Long livroId, Pageable pageable) {
        return resenhaRepository.findByLivroIdAndTemSpoiler(livroId, false, pageable)
                .map(this::toResponseDTO);
    }

    @CacheEvict(value = {"resenhas", "resenha", "livros", "livro"}, allEntries = true)
    @Transactional
    public ResenhaResponseDTO criar(ResenhaRequestDTO dto) {
        log.info("Criando resenha para livro ID: {}", dto.getLivroId());
        Livro livro = livroRepository.findById(dto.getLivroId())
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado com ID: " + dto.getLivroId()));

        Resenha resenha = Resenha.builder()
                .titulo(dto.getTitulo())
                .conteudo(dto.getConteudo())
                .nota(dto.getNota())
                .temSpoiler(dto.getTemSpoiler() != null ? dto.getTemSpoiler() : false)
                .livro(livro)
                .build();

        return toResponseDTO(resenhaRepository.save(resenha));
    }

    @CacheEvict(value = {"resenhas", "resenha", "livros", "livro"}, allEntries = true)
    @Transactional
    public ResenhaResponseDTO atualizar(Long id, ResenhaRequestDTO dto) {
        log.info("Atualizando resenha ID: {}", id);
        Resenha resenha = resenhaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resenha não encontrada com ID: " + id));

        Livro livro = livroRepository.findById(dto.getLivroId())
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado com ID: " + dto.getLivroId()));

        resenha.setTitulo(dto.getTitulo());
        resenha.setConteudo(dto.getConteudo());
        resenha.setNota(dto.getNota());
        resenha.setTemSpoiler(dto.getTemSpoiler() != null ? dto.getTemSpoiler() : false);
        resenha.setLivro(livro);

        return toResponseDTO(resenhaRepository.save(resenha));
    }

    @CacheEvict(value = {"resenhas", "resenha", "livros", "livro"}, allEntries = true)
    @Transactional
    public void deletar(Long id) {
        log.info("Deletando resenha ID: {}", id);
        if (!resenhaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resenha não encontrada com ID: " + id);
        }
        resenhaRepository.deleteById(id);
    }

    private ResenhaResponseDTO toResponseDTO(Resenha resenha) {
        return ResenhaResponseDTO.builder()
                .id(resenha.getId())
                .titulo(resenha.getTitulo())
                .conteudo(resenha.getConteudo())
                .nota(resenha.getNota())
                .temSpoiler(resenha.getTemSpoiler())
                .livroId(resenha.getLivro() != null ? resenha.getLivro().getId() : null)
                .livroTitulo(resenha.getLivro() != null ? resenha.getLivro().getTitulo() : null)
                .build();
    }
}
