package controlador;

import modelo.*;
import repositorio.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/pedidos")
public class CabeceraPedidoWebControlador {
    
    // Variables
    private final CabeceraPedidoRepositorio repository;
    private final LineaPedidoRepositorio lineaRepository;
    private final ClienteRepositorio clienteRepository;
    private final ArticuloRepositorio articuloRepository;
    
    //Constructor
    public CabeceraPedidoWebControlador(CabeceraPedidoRepositorio repository,
                                    LineaPedidoRepositorio lineaRepository,
                                    ClienteRepositorio clienteRepository,
                                    ArticuloRepositorio articuloRepository) {
        this.repository = repository;
        this.lineaRepository = lineaRepository;
        this.clienteRepository = clienteRepository;
        this.articuloRepository = articuloRepository;
    }

    // GET - Lista pedidos
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pedidos", repository.findAll());
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("activa", "todos");
        return "pedidos";
    }

    // GET - Pedidos por estado
    @GetMapping("/estado/{estado}")
    public String listarPorEstado(@PathVariable String estado, Model model) {
        model.addAttribute("pedidos", repository.findByEstado(estado));
        model.addAttribute("clientes", clienteRepository.findAll()); 
        model.addAttribute("activa", estado);
        return "pedidos";
    }

    // GET - Nuevo pedido (formulario)
    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        model.addAttribute("clientes", clienteRepository.findByFechaBajaIsNull());
        return "pedidos-nuevo";
    }

    // POST - Crear pedido
    @PostMapping("/crear")
    public String crear(@RequestParam Integer idCliente, Model model) {
        if (!clienteRepository.existsById(idCliente)) {
            model.addAttribute("error", "Cliente no existe");
            return "redirect:/pedidos";
        }

        String numPedido = generarNumeroPedido();
        CabeceraPedido pedido = new CabeceraPedido(numPedido, idCliente);
        repository.save(pedido);

        return "redirect:/pedidos/detalle/" + pedido.getId();
    }

    // GET - Ver detalle pedido
    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Integer id, Model model) {
        return repository.findById(id).map(pedido -> {
            List<LineaPedido> lineas = lineaRepository.findByIdPedido(id);

            Map<Integer, Articulo> articulosMap = new HashMap<>();
            for (Articulo a : articuloRepository.findAll()) {
                articulosMap.put(a.getId(), a);
            }

            // Verificar si cliente es premium
            boolean esPremium = false;
            var cliente = clienteRepository.findById(pedido.getIdCliente());
            if (cliente.isPresent() && cliente.get().getEsPremium()) {
                esPremium = true;
            }

            model.addAttribute("pedido", pedido);
            model.addAttribute("lineas", lineas);
            model.addAttribute("articulosMap", articulosMap);
            model.addAttribute("articulos", articuloRepository.findByFechaBajaIsNull());
            model.addAttribute("esPremium", esPremium);
            return "pedidos-detalle";
        }).orElse("redirect:/pedidos");
    }
    // POST - Añadir línea
    @PostMapping("/añadir-linea")
    public String añadirLinea(@RequestParam Integer idPedido,
                           @RequestParam Integer idArticulo,
                           @RequestParam Integer cantidad,
                           Model model) {

        var pedidoOpt = repository.findById(idPedido);
        var articuloOpt = articuloRepository.findById(idArticulo);

        if (pedidoOpt.isPresent() && articuloOpt.isPresent()) {
            Articulo articulo = articuloOpt.get();

            // Controlar stock
            if (articulo.getStock() != null && cantidad > articulo.getStock()) {
                model.addAttribute("error", "Stock insuficiente. Disponible: " + articulo.getStock());
                return "redirect:/pedidos/detalle/" + idPedido;
            }

            // Calcular subtotal (precio * cantidad)
            BigDecimal precio = articulo.getPrecioVenta() != null ? articulo.getPrecioVenta() : BigDecimal.ZERO;
            BigDecimal subtotal = precio.multiply(BigDecimal.valueOf(cantidad));
            BigDecimal gastosEnvio = articulo.getGastosEnvio() != null ? articulo.getGastosEnvio() : BigDecimal.ZERO;
            // Crear línea con subtotal
            LineaPedido linea = new LineaPedido(idPedido, idArticulo, cantidad);
            linea.setSubtotal(subtotal);
            linea.setGastosEnvioLinea(gastosEnvio);
            lineaRepository.save(linea);

            recalcularTotales(idPedido);
        }

        return "redirect:/pedidos/detalle/" + idPedido;
    }

    // POST - Finalizar pedido
    @PostMapping("/finalizar/{id}")
    public String finalizar(@PathVariable Integer id, Model model) {

        repository.findById(id).ifPresent(pedido -> {
            BigDecimal totalBase = pedido.getTotalProductos() != null ? pedido.getTotalProductos() : BigDecimal.ZERO;
            BigDecimal envio = pedido.getEnvioTotal() != null ? pedido.getEnvioTotal() : BigDecimal.ZERO;

            BigDecimal descuento = BigDecimal.ZERO;
            var clienteOpt = clienteRepository.findById(pedido.getIdCliente());
            if (clienteOpt.isPresent() && clienteOpt.get().getEsPremium()) {
                descuento = totalBase.multiply(BigDecimal.valueOf(0.30));
            }

            pedido.setDescuentoAplicado(descuento);

            BigDecimal subtotal = totalBase.subtract(descuento);
            pedido.setSubtotalConDescuento(subtotal);
            pedido.setTotalFinal(subtotal.add(envio));
            pedido.setEstado("PENDIENTE");
            repository.save(pedido);
        });

        return "redirect:/pedidos";
    }

    // POST - Enviar pedido
    @PostMapping("/enviar/{id}")
    public String enviar(@PathVariable Integer id, Model model) {
        
        repository.findById(id).ifPresent(pedido -> {
            pedido.setEstado("ENVIADO");
            repository.save(pedido);
        });
        
        return "redirect:/pedidos";
    }
    // POST - Cancelar pedido
    @PostMapping("/cancelar/{id}")
    public String cancelar(@PathVariable Integer id, Model model) {
        return repository.findById(id).map(pedido -> {
            pedido.setEstado("CANCELADO");
            repository.save(pedido);
            return "redirect:/pedidos";
        }).orElse("redirect:/pedidos");
    }
    
    //POST - Cancelar linea pedido
    @PostMapping("/eliminar-linea/{id}")
    public String eliminarLinea(@PathVariable Integer id, Model model) {
        var lineaOpt = lineaRepository.findById(id);
        if (lineaOpt.isPresent()) {
            LineaPedido linea = lineaOpt.get();
            Integer idPedido = linea.getIdPedido();
            Integer cantidad = linea.getCantidad();

            // Devolver stock al artículo
            var articuloOpt = articuloRepository.findById(linea.getIdArticulo());
            if (articuloOpt.isPresent()) {
                Articulo articulo = articuloOpt.get();
                Integer stockActual = articulo.getStock() != null ? articulo.getStock() : 0;
                articulo.setStock(stockActual + cantidad);
                articuloRepository.save(articulo);
            }

            lineaRepository.delete(linea);

            // Recalcular totales
            recalcularTotales(idPedido);

            return "redirect:/pedidos/detalle/" + idPedido;
        }
        return "redirect:/pedidos";
    }
    //Recalculo de los totales
    private void recalcularTotales(Integer idPedido) {
        repository.findById(idPedido).ifPresent(pedido -> {
            List<LineaPedido> lineas = lineaRepository.findByIdPedido(idPedido);

            BigDecimal totalProductos = BigDecimal.ZERO;

            for (LineaPedido linea : lineas) {
                var articulo = articuloRepository.findById(linea.getIdArticulo());
                if (articulo.isPresent()) {
                    BigDecimal precio = articulo.get().getPrecioVenta();
                    totalProductos = totalProductos.add(precio.multiply(BigDecimal.valueOf(linea.getCantidad())));
                }
            }

            pedido.setTotalProductos(totalProductos);
            // null safety
            if (pedido.getTotalProductos() == null) pedido.setTotalProductos(BigDecimal.ZERO);
            
            // Recalcular descuento si es premium
            BigDecimal descuento = BigDecimal.ZERO;
            var cliente = clienteRepository.findById(pedido.getIdCliente());
            if (cliente.isPresent() && cliente.get().getEsPremium()) {
                descuento = totalProductos.multiply(BigDecimal.valueOf(0.30));
            }

            pedido.setDescuentoAplicado(descuento);
            // Subtotal
            BigDecimal subtotal = totalProductos.subtract(descuento);
            pedido.setSubtotalConDescuento(subtotal);

            repository.save(pedido);
        });
    }
    //Crear número de pedido nuevo, único y consecutivo   
    private String generarNumeroPedido() {
        Random random = new Random();
        String numPedido;
        do {
            int num = random.nextInt(900000) + 100000;
            numPedido = "P" + num;
        } while (repository.existsByNumPedido(numPedido));
        return numPedido;
    }
}