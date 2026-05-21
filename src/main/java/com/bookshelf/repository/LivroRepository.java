package com.bookshelf.repository;

import com.bookshelf.model.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    Page<Livro> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);

    Page<Livro> findByGeneroIgnoreCase(String genero, Pageable pageable);

    Page<Livro> findByAutorId(Long autorId, Pageable pageable);

    Page<Livro> findByAnoPublicacao(Integer ano, Pageable pageable);

    @Query("SELECT l FROM Livro l JOIN FETCH l.autor WHERE l.id = :id")
    Optional<Livro> findByIdWithAutor(@Param("id") Long id);

    @Query("SELECT l FROM Livro l JOIN FETCH l.autor JOIN FETCH l.resenhas WHERE l.id = :id")
    Optional<Livro> findByIdWithAutorAndResenhas(@Param("id") Long id);

    @Query("SELECT AVG(r.nota) FROM Resenha r WHERE r.livro.id = :livroId")
    Double calcularMediaNotas(@Param("livroId") Long livroId);

    @Query("SELECT COUNT(r) FROM Resenha r WHERE r.livro.id = :livroId")
    int countResenhasByLivroId(@Param("livroId") Long livroId);

    @Query("SELECT l FROM Livro l JOIN FETCH l.autor WHERE l.autor.id = :autorId")
    List<Livro> findByAutorIdWithAutor(@Param("autorId") Long autorId);
}
