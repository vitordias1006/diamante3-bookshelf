package com.bookshelf.repository;

import com.bookshelf.model.Resenha;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResenhaRepository extends JpaRepository<Resenha, Long> {

    Page<Resenha> findByLivroId(Long livroId, Pageable pageable);

    Page<Resenha> findByNota(Integer nota, Pageable pageable);

    Page<Resenha> findByTemSpoiler(Boolean temSpoiler, Pageable pageable);

    Page<Resenha> findByLivroIdAndTemSpoiler(Long livroId, Boolean temSpoiler, Pageable pageable);

    @Query("SELECT r FROM Resenha r JOIN FETCH r.livro WHERE r.id = :id")
    java.util.Optional<Resenha> findByIdWithLivro(@Param("id") Long id);

    @Query("SELECT AVG(r.nota) FROM Resenha r WHERE r.livro.id = :livroId")
    Double mediaNotasByLivroId(@Param("livroId") Long livroId);

    boolean existsByLivroId(Long livroId);
}
