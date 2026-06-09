package repositorio;

import modelo.CabeceraPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CabeceraPedidoRepositorio extends JpaRepository<CabeceraPedido, Integer> {

    Optional<CabeceraPedido> findByNumPedido(String numPedido);

    List<CabeceraPedido> findByIdCliente(Integer idCliente);

    List<CabeceraPedido> findByEstado(String estado);

    List<CabeceraPedido> findByIdClienteAndEstado(Integer idCliente, String estado);

    boolean existsByNumPedido(String numPedido);

    Optional<CabeceraPedido> findFirstByOrderByNumPedidoDesc();
}
