package org.victor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.victor.model.Cartao;
import java.util.List;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, Long> {
    // Busca todos os cartões DESTE usuário
    List<Cartao> findByUsuarioId(String usuarioId);
}