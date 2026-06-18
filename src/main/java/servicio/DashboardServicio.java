/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import repositorio.ArticuloRepositorio;
import repositorio.CabeceraPedidoRepositorio;
import DTO.DashboardDTO;
import DTO.VentasDiariasDTO;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardServicio {

    private final ArticuloRepositorio articuloRepositorio;
    private final CabeceraPedidoRepositorio pedidoRepositorio;

    public DashboardServicio(ArticuloRepositorio articuloRepositorio,
                             CabeceraPedidoRepositorio pedidoRepositorio) {
        this.articuloRepositorio = articuloRepositorio;
        this.pedidoRepositorio = pedidoRepositorio;
    }

    public DashboardDTO obtenerDashboard() {

        DashboardDTO dto = new DashboardDTO();

        // 📦 Artículos
        dto.setTotalArticulos((int) articuloRepositorio.count());

        // 📦 Pedidos por estado
        dto.setPedidosPendientes(
                (int) pedidoRepositorio.countByEstado("PENDIENTE")
        );

        dto.setPedidosEnviados(
                (int) pedidoRepositorio.countByEstado("ENVIADO")
        );

        // 📅 Rango de hoy
        LocalDate hoy = LocalDate.now();
        LocalDateTime inicio = hoy.atStartOfDay();
        LocalDateTime fin = hoy.plusDays(1).atStartOfDay();

        // 📅 Pedidos de hoy
        dto.setPedidosHoy(
                (int) pedidoRepositorio.countByFechaHoraBetween(inicio, fin)
        );

        // 💰 Ingresos totales
        BigDecimal ingresosTotales = pedidoRepositorio.sumarIngresosTotales();
        dto.setIngresosTotales(ingresosTotales != null ? ingresosTotales : BigDecimal.ZERO);

        // 💰 Ingresos hoy
        BigDecimal ingresosHoy = pedidoRepositorio.sumarIngresosHoy(inicio, fin);
        dto.setIngresosHoy(ingresosHoy != null ? ingresosHoy : BigDecimal.ZERO);

        // 📊 Ventas últimos 7 días
        List<VentasDiariasDTO> ventasUltimos7Dias = obtenerVentasUltimos7Dias();
        dto.setVentasUltimos7Dias(ventasUltimos7Dias);

        // 📦 Stock total (asumiendo que tienes un campo stock en Articulo)
        Integer stockTotal = articuloRepositorio.sumarStockTotal();
        dto.setStockTotal(stockTotal != null ? stockTotal : 0);

        return dto;
    }

    private List<VentasDiariasDTO> obtenerVentasUltimos7Dias() {
        List<VentasDiariasDTO> ventas = new ArrayList<>();
        
        for (int i = 6; i >= 0; i--) {
            LocalDate fecha = LocalDate.now().minusDays(i);
            LocalDateTime inicio = fecha.atStartOfDay();
            LocalDateTime fin = fecha.plusDays(1).atStartOfDay();
            
            BigDecimal total = pedidoRepositorio.sumarIngresosHoy(inicio, fin);
            ventas.add(new VentasDiariasDTO(fecha, total != null ? total : BigDecimal.ZERO));
        }
        
        return ventas;
    }
}