package org.victor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.victor.model.Pessoa;
import java.util.List;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    // Busca todas as pessoas DESTE usuário
    List<Pessoa> findByUsuarioId(String usuarioId);
}