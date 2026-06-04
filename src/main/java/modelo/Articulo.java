/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "articulos")
public class Articulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_articulo")
    private Integer id;

    @Column(name = "codigo_id", unique = true, nullable = false, length = 50)
    private String codigoId;

    @Column(length = 100)
    private String descripcion;
      
    @Column(name = "precio_unitario")
    private BigDecimal precioVenta;

    @Column(name = "gastos_envio")
    private BigDecimal gastosEnvio;

    @Column(name = "tiempo_preparacion_min")
    private Integer tiempoPreparacion;

    private Integer stock;

    @Column(name = "fecha_baja")
    private LocalDateTime fechaBaja;

    // Constructores
    public Articulo() {}

    public Articulo(String codigoId, String descripcion, BigDecimal precioVenta, 
                    BigDecimal gastosEnvio, Integer tiempoPreparacion, Integer stock) {
        this.codigoId = codigoId;
        this.descripcion = descripcion;
        this.precioVenta = precioVenta;
        this.gastosEnvio = gastosEnvio;
        this.tiempoPreparacion = tiempoPreparacion;
        this.stock = stock;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getCodigoId() { return codigoId; }
    public void setCodigoId(String codigoId) { this.codigoId = codigoId; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }

    public BigDecimal getGastosEnvio() { return gastosEnvio; }
    public void setGastosEnvio(BigDecimal gastosEnvio) { this.gastosEnvio = gastosEnvio; }

    public Integer getTiempoPreparacion() { return tiempoPreparacion; }
    public void setTiempoPreparacion(Integer tiempoPreparacion) { this.tiempoPreparacion = tiempoPreparacion; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public LocalDateTime getFechaBaja() { return fechaBaja; }
    public void setFechaBaja(LocalDateTime fechaBaja) { this.fechaBaja = fechaBaja; }
}