package org.victor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.victor.model.Cartao;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, Long> {
}
