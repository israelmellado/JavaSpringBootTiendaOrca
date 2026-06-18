package repositorio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import modelo.CabeceraPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface CabeceraPedidoRepositorio extends JpaRepository<CabeceraPedido, Integer> {

    Optional<CabeceraPedido> findByNumPedido(String numPedido);

    List<CabeceraPedido> findByIdCliente(Integer idCliente);

    List<CabeceraPedido> findByEstado(String estado);

    List<CabeceraPedido> findByIdClienteAndEstado(Integer idCliente, String estado);

    boolean existsByNumPedido(String numPedido);

    Optional<CabeceraPedido> findFirstByOrderByNumPedidoDesc();
//dashboard
    long countByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
    long countByEstado(String estado);
    
    @Query("SELECT COALESCE(SUM(p.totalFinal), 0) FROM CabeceraPedido p")
    BigDecimal sumarIngresosTotales();

    @Query("""
    SELECT COALESCE(SUM(p.totalFinal), 0)
    FROM CabeceraPedido p
    WHERE p.fechaHora BETWEEN :inicio AND :fin
    """)
    BigDecimal sumarIngresosHoy(LocalDateTime inicio, LocalDateTime fin);
}
