package modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "linea_pedidos")
public class LineaPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_linea")
    private Integer id;

    @Column(name = "id_pedido")
    private Integer idPedido;

    @Column(name = "id_articulo")
    private Integer idArticulo;

    @Column(name = "cantidad")
    private Integer cantidad;
    
    @Column(name = "subtotal")
    private BigDecimal subtotal;
    
    @Column(name = "gastos_envio_linea")
    private BigDecimal gastosEnvioLinea;


    // Constructores
    public LineaPedido() {}

    public LineaPedido(Integer idPedido, Integer idArticulo, Integer cantidad) {
        this.idPedido = idPedido;
        this.idArticulo = idArticulo;
        this.cantidad = cantidad;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getIdPedido() { return idPedido; }
    public void setIdPedido(Integer idPedido) { this.idPedido = idPedido; }

    public Integer getIdArticulo() { return idArticulo; }
    public void setIdArticulo(Integer idArticulo) { this.idArticulo = idArticulo; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    
    public void setGastosEnvioLinea(BigDecimal gastosEnvioLinea) {
        this.gastosEnvioLinea = gastosEnvioLinea;
    }
    public BigDecimal getGastosEnvioLinea() {
        return gastosEnvioLinea;
    }
}
