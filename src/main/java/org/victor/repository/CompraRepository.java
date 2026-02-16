package org.victor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.victor.model.Compra;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    List<Compra> findByMesFaturaAndAnoFatura(Integer mes, Integer ano);

    @Query("SELECT SUM(c.valor) FROM Compra c WHERE c.cartao.id = :cartaoId AND c.mesFatura = :mes AND c.anoFatura = :ano")
    BigDecimal somarGastosPorCartao(@Param("cartaoId") Long cartaoId, @Param("mes") Integer mes, @Param("ano") Integer ano);
}
