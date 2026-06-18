package modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cabecera_pedidos")
public class CabeceraPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Integer id;

    @Column(name = "num_pedido", unique = true)
    private String numPedido;  // String, no Integer

    @Column(name = "id_cliente")
    private Integer idCliente;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    @Column(name = "total_productos")
    private BigDecimal totalProductos;

    @Column(name = "descuento_aplicado")
    private BigDecimal descuentoAplicado;

    @Column(name = "subtotal_con_descuento")
    private BigDecimal subtotalConDescuento;

    @Column(name = "envio_total")
    private BigDecimal envioTotal;

    @Column(name = "total_final")
    private BigDecimal totalFinal;

    @Column(name = "estado")
    private String estado;
    //---DASHBOARD-------------------
    

    // Constructores
    public CabeceraPedido() {}

    public CabeceraPedido(String numPedido, Integer idCliente) {
        this.numPedido = numPedido;
        this.idCliente = idCliente;
        this.fechaHora = LocalDateTime.now();
        this.totalProductos = BigDecimal.ZERO;
        this.descuentoAplicado = BigDecimal.ZERO;
        this.subtotalConDescuento = BigDecimal.ZERO;
        this.envioTotal = BigDecimal.ZERO;
        this.totalFinal = BigDecimal.ZERO;
        this.estado = "PENDIENTE";
        
    }
    // Getters y Setters
 

    public Integer getId() {return id;}
    public void setId(Integer id) { this.id = id; }

    public String getNumPedido() { return numPedido; }
    public void setNumPedido(String numPedido) { this.numPedido = numPedido; }

    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public BigDecimal getTotalProductos() { return totalProductos; }
    public void setTotalProductos(BigDecimal totalProductos) { this.totalProductos = totalProductos; }

    public BigDecimal getDescuentoAplicado() { return descuentoAplicado; }
    public void setDescuentoAplicado(BigDecimal descuentoAplicado) { this.descuentoAplicado = descuentoAplicado; }

    public BigDecimal getSubtotalConDescuento() { return subtotalConDescuento; }
    public void setSubtotalConDescuento(BigDecimal subtotalConDescuento) { this.subtotalConDescuento = subtotalConDescuento; }

    public BigDecimal getEnvioTotal() { return envioTotal; }
    public void setEnvioTotal(BigDecimal envioTotal) { this.envioTotal = envioTotal; }

    public BigDecimal getTotalFinal() { return totalFinal; }
    public void setTotalFinal(BigDecimal totalFinal) { this.totalFinal = totalFinal; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}