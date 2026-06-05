package controlador;

import modelo.*;
import repositorio.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/pedidos")
public class CabeceraPedidoWebControlador {

    private final CabeceraPedidoRepositorio repository;
    private final LineaPedidoRepositorio lineaRepository;
    private final ClienteRepositorio clienteRepository;
    private final ArticuloRepositorio articuloRepository;

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
            model.addAttribute("pedido", pedido);
            model.addAttribute("lineas", lineaRepository.findByIdPedido(id));
            model.addAttribute("articulos", articuloRepository.findByFechaBajaIsNull());
            return "pedido-detalle";
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
            
            LineaPedido linea = new LineaPedido(
                idPedido,
                idArticulo,
                cantidad,
                articulo.getPrecioVenta(),
                articulo.getGastosEnvio()
            );
            lineaRepository.save(linea);
            recalcularTotales(idPedido);
        }
        
        return "redirect:/pedidos/detalle/" + idPedido;
    }

    // POST - Finalizar pedido
    @PostMapping("/finalizar/{id}")
    public String finalizar(@PathVariable Integer id, Model model) {
        
        repository.findById(id).ifPresent(pedido -> {
            BigDecimal BigDescuento = BigDecimal.ZERO;
            var clienteOpt = clienteRepository.findById(pedido.getIdCliente());
            if (clienteOpt.isPresent() && clienteOpt.get().getEsPremium()) {
                BigDecimal descuento = pedido.getTotalProductos().multiply(BigDecimal.valueOf(0.30));
                pedido.setDescuentoAplicado(descuento);
            }
            BigDecimal descuento = null;
            
            BigDecimal subtotal = pedido.getTotalProductos().subtract(descuento);
            pedido.setSubtotalConDescuento(subtotal);
            pedido.setTotalFinal(subtotal.add(pedido.getEnvioTotal()));
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
        
        repository.findById(id).ifPresent(pedido -> {
            lineaRepository.deleteByIdPedido(id);
            repository.delete(pedido);
        });
        
        return "redirect:/pedidos";
    }

    private void recalcularTotales(Integer idPedido) {
        repository.findById(idPedido).ifPresent(pedido -> {
            List<LineaPedido> lineas = lineaRepository.findByIdPedido(idPedido);
            
            BigDecimal totalProductos = BigDecimal.ZERO;
            BigDecimal totalEnvio = BigDecimal.ZERO;
            
            for (LineaPedido linea : lineas) {
                BigDecimal totalLinea = linea.getPrecioVenta().multiply(BigDecimal.valueOf(linea.getCantidad()));
                totalProductos = totalProductos.add(totalLinea);
                totalEnvio = totalEnvio.add(linea.getGastosEnvio() != null ? linea.getGastosEnvio() : BigDecimal.ZERO);
            }
            
            pedido.setTotalProductos(totalProductos);
            pedido.setEnvioTotal(totalEnvio);
            repository.save(pedido);
        });
    }

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