/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.Articulo;
import repositorio.ArticuloRepositorio;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/articulos")
public class ArticuloWebControlador {

    private final ArticuloRepositorio repository;

    public ArticuloWebControlador(ArticuloRepositorio repository) {
        this.repository = repository;
    }

    // GET - Lista activos
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("articulos", repository.findByFechaBajaIsNull());
        model.addAttribute("activa", "activos");
        return "articulos";
    }

    // GET - Lista baja
    @GetMapping("/baja")
    public String listarBaja(Model model) {
        model.addAttribute("articulos", repository.findByFechaBajaIsNotNull());
        model.addAttribute("activa", "baja");
        return "articulos";
    }

    // POST - Crear nuevo
    @PostMapping
    public String crear(@RequestParam String codigoId,
                    @RequestParam String descripcion,
                    @RequestParam BigDecimal precioVenta,
                    @RequestParam BigDecimal gastosEnvio,
                    @RequestParam Integer tiempoPreparacion,
                    @RequestParam Integer stock,
                    Model model) {
        
        if (repository.existsByCodigoId(codigoId)) {
            model.addAttribute("error", "Código ya existe: " + codigoId);
            model.addAttribute("articulos", repository.findByFechaBajaIsNull());
            model.addAttribute("activa", "activos");
            return "articulos";
        }

        Articulo articulo = new Articulo(codigoId, descripcion, precioVenta, 
                                       gastosEnvio, tiempoPreparacion, stock);
        repository.save(articulo);
        
        return "redirect:/articulos?mensaje=Artículo+guardado";
    }

    // POST - Modificar
    @PostMapping("/guardar-modificacion")
    public String modificar(@RequestParam Integer id,
                        @RequestParam String codigoId,
                        @RequestParam String descripcion,
                        @RequestParam BigDecimal precioVenta,
                        @RequestParam BigDecimal gastosEnvio,
                        @RequestParam Integer tiempoPreparacion,
                        @RequestParam Integer stock,
                        Model model) {

        System.out.println("===== MODIFICAR =====");
        System.out.println("ID: " + id);
        System.out.println("Stock actual: " + stock);

        // Buscar el artículo actual
        var opt = repository.findById(id);
        if (opt.isPresent()) {
            Articulo articulo = opt.get();

            // Verificar que el stock solo puede aumentar
            if (stock < articulo.getStock()) {
                model.addAttribute("error", "El stock no puede reducirse. Stock actual: " + articulo.getStock());
                model.addAttribute("articulos", repository.findByFechaBajaIsNull());
                model.addAttribute("activa", "activos");
                return "articulos";
            }

            // Actualizar
            articulo.setCodigoId(codigoId);
            articulo.setDescripcion(descripcion);
            articulo.setPrecioVenta(precioVenta);
            articulo.setGastosEnvio(gastosEnvio);
            articulo.setTiempoPreparacion(tiempoPreparacion);
            articulo.setStock(stock);
            repository.save(articulo);

            model.addAttribute("mensaje", "Artículo modificado");
        }

        return "redirect:/articulos";
    }

    // POST - Dar de baja
    @PostMapping("/eliminar")
    public String darBaja(@RequestParam Integer id, Model model) {
        repository.findById(id).ifPresent(articulo -> {
            articulo.setFechaBaja(LocalDateTime.now());
            repository.save(articulo);
        });
        return "redirect:/articulos?mensaje=Artículo+dado+de+baja";
    }

    // POST - Reactivar
    @PostMapping("/reactivar")
    public String reactivar(@RequestParam Integer id, Model model) {
        repository.findById(id).ifPresent(articulo -> {
            articulo.setFechaBaja(null);
            repository.save(articulo);
        });
        return "redirect:/articulos/baja?mensaje=Artículo+reactivado";
    }
}