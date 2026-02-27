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

    List<Compra> findByMesFaturaAndAnoFaturaAndUsuarioId(int mesFatura, int anoFatura, String usuarioId);

    List<Compra> findByGrupoParcelamento(String grupoParcelamento);

    @Query("SELECT SUM(c.valor) FROM Compra c WHERE c.cartao.id = :cartaoId AND c.mesFatura = :mes AND c.anoFatura = :ano AND c.usuarioId = :usuarioId")
    BigDecimal somarGastosPorCartao(@Param("cartaoId") Long cartaoId, @Param("mes") int mes, @Param("ano") int ano, @Param("usuarioId") String usuarioId);

    // limpa a base toda do usuario de uma vez
    void deleteByUsuarioId(String usuarioId);
}