/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

import java.math.BigDecimal;
import java.util.List;

public class DashboardDTO {

    private int totalArticulos;
    private int pedidosPendientes;
    private int pedidosEnviados;
    private int stockTotal;
    private Integer pedidosHoy;
    private BigDecimal ingresosTotales;
    private BigDecimal ingresosHoy;
    private List<VentasDiariasDTO> ventasUltimos7Dias;

    public List<VentasDiariasDTO> getVentasUltimos7Dias() {
        return ventasUltimos7Dias;
    }

    public void setVentasUltimos7Dias(List<VentasDiariasDTO> ventasUltimos7Dias) {
        this.ventasUltimos7Dias = ventasUltimos7Dias;
    }

    public Integer getPedidosHoy() {
        return pedidosHoy;
    }

    public void setPedidosHoy(Integer pedidosHoy) {
        this.pedidosHoy = pedidosHoy;
    }

    public BigDecimal getIngresosTotales() {
        return ingresosTotales;
    }

    public void setIngresosTotales(BigDecimal ingresosTotales) {
        this.ingresosTotales = ingresosTotales;
    }

    public BigDecimal getIngresosHoy() {
        return ingresosHoy;
    }

    // getters y setters
    public void setIngresosHoy(BigDecimal ingresosHoy) {
        this.ingresosHoy = ingresosHoy;
    }

    public int getTotalArticulos() {
        return totalArticulos;
    }

    public void setTotalArticulos(int totalArticulos) {
        this.totalArticulos = totalArticulos;
    }

    public int getPedidosPendientes() {
        return pedidosPendientes;
    }

    public void setPedidosPendientes(int pedidosPendientes) {
        this.pedidosPendientes = pedidosPendientes;
    }

    public int getPedidosEnviados() {
        return pedidosEnviados;
    }

    public void setPedidosEnviados(int pedidosEnviados) {
        this.pedidosEnviados = pedidosEnviados;
    }

    public int getStockTotal() {
        return stockTotal;
    }

    public void setStockTotal(int stockTotal) {
        this.stockTotal = stockTotal;
    }
    
}
