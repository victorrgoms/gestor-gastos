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

    // Busca compras por data E por usuário
    List<Compra> findByMesFaturaAndAnoFaturaAndUsuarioId(Integer mes, Integer ano, String usuarioId);

    // Soma gastos filtrando também pelo ID do usuário para segurança
    @Query("SELECT SUM(c.valor) FROM Compra c WHERE c.cartao.id = :cartaoId AND c.mesFatura = :mes AND c.anoFatura = :ano AND c.usuarioId = :usuarioId")
    BigDecimal somarGastosPorCartao(@Param("cartaoId") Long cartaoId,
                                    @Param("mes") Integer mes,
                                    @Param("ano") Integer ano,
                                    @Param("usuarioId") String usuarioId);
}