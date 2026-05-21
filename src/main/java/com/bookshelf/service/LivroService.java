package com.bookshelf.service;

import com.bookshelf.dto.*;
import com.bookshelf.exception.ResourceNotFoundException;
import com.bookshelf.model.Autor;
import com.bookshelf.model.Livro;
import com.bookshelf.repository.AutorRepository;
import com.bookshelf.repository.LivroRepository;
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
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;

    @Cacheable(value = "livros")
    @Transactional(readOnly = true)
    public Page<LivroResponseDTO> listarTodos(Pageable pageable) {
        log.info("Listando livros - page: {}", pageable.getPageNumber());
        return livroRepository.findAll(pageable)
                .map(this::toResponseDTO);
    }

    @Cacheable(value = "livro", key = "#id")
    @Transactional(readOnly = true)
    public LivroResponseDTO buscarPorId(Long id) {
        log.info("Buscando livro ID: {}", id);
        Livro livro = livroRepository.findByIdWithAutor(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado com ID: " + id));
        return toResponseDTO(livro);
    }

    @Transactional(readOnly = true)
    public Page<LivroResumoDTO> buscarPorTitulo(String titulo, Pageable pageable) {
        return livroRepository.findByTituloContainingIgnoreCase(titulo, pageable)
                .map(this::toResumoDTO);
    }

    @Transactional(readOnly = true)
    public Page<LivroResumoDTO> buscarPorGenero(String genero, Pageable pageable) {
        return livroRepository.findByGeneroIgnoreCase(genero, pageable)
                .map(this::toResumoDTO);
    }

    @Transactional(readOnly = true)
    public Page<LivroResponseDTO> buscarPorAutor(Long autorId, Pageable pageable) {
        if (!autorRepository.existsById(autorId)) {
            throw new ResourceNotFoundException("Autor não encontrado com ID: " + autorId);
        }
        return livroRepository.findByAutorId(autorId, pageable)
                .map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<LivroResumoDTO> buscarPorAno(Integer ano, Pageable pageable) {
        return livroRepository.findByAnoPublicacao(ano, pageable)
                .map(this::toResumoDTO);
    }

    @CacheEvict(value = {"livros", "livro", "autores", "autor"}, allEntries = true)
    @Transactional
    public LivroResponseDTO criar(LivroRequestDTO dto) {
        log.info("Criando livro: {}", dto.getTitulo());
        Autor autor = autorRepository.findById(dto.getAutorId())
                .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado com ID: " + dto.getAutorId()));

        Livro livro = Livro.builder()
                .titulo(dto.getTitulo())
                .isbn(dto.getIsbn())
                .anoPublicacao(dto.getAnoPublicacao())
                .genero(dto.getGenero())
                .paginas(dto.getPaginas())
                .sinopse(dto.getSinopse())
                .autor(autor)
                .build();

        return toResponseDTO(livroRepository.save(livro));
    }

    @CacheEvict(value = {"livros", "livro"}, allEntries = true)
    @Transactional
    public LivroResponseDTO atualizar(Long id, LivroRequestDTO dto) {
        log.info("Atualizando livro ID: {}", id);
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado com ID: " + id));

        Autor autor = autorRepository.findById(dto.getAutorId())
                .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado com ID: " + dto.getAutorId()));

        livro.setTitulo(dto.getTitulo());
        livro.setIsbn(dto.getIsbn());
        livro.setAnoPublicacao(dto.getAnoPublicacao());
        livro.setGenero(dto.getGenero());
        livro.setPaginas(dto.getPaginas());
        livro.setSinopse(dto.getSinopse());
        livro.setAutor(autor);

        return toResponseDTO(livroRepository.save(livro));
    }

    @CacheEvict(value = {"livros", "livro"}, allEntries = true)
    @Transactional
    public void deletar(Long id) {
        log.info("Deletando livro ID: {}", id);
        if (!livroRepository.existsById(id)) {
            throw new ResourceNotFoundException("Livro não encontrado com ID: " + id);
        }
        livroRepository.deleteById(id);
    }

    private LivroResponseDTO toResponseDTO(Livro livro) {
        Double media = livroRepository.calcularMediaNotas(livro.getId());
        int total = livroRepository.countResenhasByLivroId(livro.getId());

        return LivroResponseDTO.builder()
                .id(livro.getId())
                .titulo(livro.getTitulo())
                .isbn(livro.getIsbn())
                .anoPublicacao(livro.getAnoPublicacao())
                .genero(livro.getGenero())
                .paginas(livro.getPaginas())
                .sinopse(livro.getSinopse())
                .autorNome(livro.getAutor() != null ? livro.getAutor().getNome() : null)
                .autorId(livro.getAutor() != null ? livro.getAutor().getId() : null)
                .mediaNotas(media != null ? Math.round(media * 10.0) / 10.0 : 0.0)
                .totalResenhas(total)
                .build();
    }

    private LivroResumoDTO toResumoDTO(Livro livro) {
        Double media = livroRepository.calcularMediaNotas(livro.getId());
        return LivroResumoDTO.builder()
                .id(livro.getId())
                .titulo(livro.getTitulo())
                .genero(livro.getGenero())
                .anoPublicacao(livro.getAnoPublicacao())
                .autorNome(livro.getAutor() != null ? livro.getAutor().getNome() : null)
                .mediaNotas(media != null ? Math.round(media * 10.0) / 10.0 : 0.0)
                .build();
    }
}
