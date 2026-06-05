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

    @Column(name = "precio_venta")
    private BigDecimal precioVenta;

    @Column(name = "gastos_envio")
    private BigDecimal gastosEnvio;

    // Constructores
    public LineaPedido() {}

    public LineaPedido(Integer idPedido, Integer idArticulo, Integer cantidad, 
                      BigDecimal precioVenta, BigDecimal gastosEnvio) {
        this.idPedido = idPedido;
        this.idArticulo = idArticulo;
        this.cantidad = cantidad;
        this.precioVenta = precioVenta;
        this.gastosEnvio = gastosEnvio;
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

    public BigDecimal getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }

    public BigDecimal getGastosEnvio() { return gastosEnvio; }
    public void setGastosEnvio(BigDecimal gastosEnvio) { this.gastosEnvio = gastosEnvio; }

    // Calculado: total línea
    public BigDecimal getTotal() {
        if (precioVenta == null || cantidad == null) return BigDecimal.ZERO;
        return precioVenta.multiply(BigDecimal.valueOf(cantidad))
            .add(gastosEnvio != null ? gastosEnvio : BigDecimal.ZERO);
    }
}
