package com.bookshelf.repository;

import com.bookshelf.model.Autor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    Page<Autor> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    Page<Autor> findByNacionalidadeIgnoreCase(String nacionalidade, Pageable pageable);

    boolean existsByNomeIgnoreCase(String nome);

    @Query("SELECT a FROM Autor a LEFT JOIN FETCH a.livros WHERE a.id = :id")
    Optional<Autor> findByIdWithLivros(@Param("id") Long id);

    @Query("SELECT COUNT(l) FROM Livro l WHERE l.autor.id = :autorId")
    int countLivrosByAutorId(@Param("autorId") Long autorId);
}
