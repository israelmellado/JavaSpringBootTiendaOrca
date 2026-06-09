package repositorio;

import modelo.LineaPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LineaPedidoRepositorio extends JpaRepository<LineaPedido, Integer> {

    List<LineaPedido> findByIdPedido(Integer idPedido);

    void deleteByIdPedido(Integer idPedido);
}
