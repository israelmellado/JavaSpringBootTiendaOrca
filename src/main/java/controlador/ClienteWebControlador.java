package controlador;

import modelo.Cliente;
import repositorio.ClienteRepositorio;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/clientes")
public class ClienteWebControlador {

    private final ClienteRepositorio repository;

    public ClienteWebControlador(ClienteRepositorio repository) {
        this.repository = repository;
    }

    // GET - Lista clientes activos
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", repository.findByFechaBajaIsNull());
        model.addAttribute("activa", "todos");
        return "clientes";
    }

    // GET - Lista premium
    @GetMapping("/premium")
    public String listarPremium(Model model) {
        model.addAttribute("clientes", repository.findByEsPremiumTrueAndFechaBajaIsNull());
        model.addAttribute("activa", "premium");
        return "clientes";
    }

    // GET - Lista estándar
    @GetMapping("/estandar")
    public String listarEstandar(Model model) {
        model.addAttribute("clientes", repository.findByEsPremiumFalseAndFechaBajaIsNull());
        model.addAttribute("activa", "estandar");
        return "clientes";
    }

    // GET - Lista baja
    @GetMapping("/baja")
    public String listarBaja(Model model) {
        model.addAttribute("clientes", repository.findByFechaBajaIsNotNull());
        model.addAttribute("activa", "baja");
        return "clientes";
    }

    // POST - Crear cliente (estandar o premium)
    @PostMapping
    public String crear(@RequestParam String email,
                    @RequestParam String nombre,
                    @RequestParam String domicilio,
                    @RequestParam String nif,
                    @RequestParam(required = false) Boolean esPremium,
                    Model model) {

        // Verificar email único
        if (repository.existsByEmail(email)) {
            model.addAttribute("error", "Email ya existe: " + email);
            model.addAttribute("clientes", repository.findByFechaBajaIsNull());
            return "clientes";
        }

        // Verificar NIF único
        if (repository.existsByNif(nif)) {
            model.addAttribute("error", "NIF ya existe: " + nif);
            model.addAttribute("clientes", repository.findByFechaBajaIsNull());
            return "clientes";
        }

        Cliente cliente = new Cliente(email, nombre, domicilio, nif);

        // Si es premium: 30€ cuota + 30% descuento
        if (esPremium != null && esPremium) {
            cliente.setEsPremium(true);
            cliente.setCuotaAnual(new BigDecimal("30.00"));
        } else {
            cliente.setEsPremium(false);
            cliente.setCuotaAnual(BigDecimal.ZERO);
        }

        repository.save(cliente);

        return "redirect:/clientes?mensaje=Cliente+guardado";
    }



    // POST - Guardar modificación
    @PostMapping("/guardar-modificacion")
    public String modificar(@RequestParam Integer id,
                        @RequestParam String email,
                        @RequestParam String nombre,
                        @RequestParam String domicilio,
                        @RequestParam String nif,
                        Model model) {
        
        repository.findById(id).ifPresent(cliente -> {
            // Verificar si email ya existe en otro cliente
            if (!cliente.getEmail().equals(email) && repository.existsByEmail(email)) {
                model.addAttribute("error", "Email ya existe: " + email);
                return;
            }
            
            // Verificar si NIF ya existe en otro cliente
            if (!cliente.getNif().equals(nif) && repository.existsByNif(nif)) {
                model.addAttribute("error", "NIF ya existe: " + nif);
                return;
            }
            
            cliente.setEmail(email);
            cliente.setNombre(nombre);
            cliente.setDomicilio(domicilio);
            cliente.setNif(nif);
            repository.save(cliente);
            
            model.addAttribute("mensaje", "Cliente modificado");
        });
        
        return "redirect:/clientes";
    }

    // POST - Escalar a premium
    @PostMapping("/escalar")
    public String escalarPremium(@RequestParam Integer id,
                        @RequestParam BigDecimal cuotaBase,
                        Model model) {
        
        repository.findById(id).ifPresent(cliente -> {
            if (cliente.getEsPremium()) {
                model.addAttribute("error", "Ya es premium");
                return;
            }
            
            // Escalar a premium con 30% descuento
            cliente.setEsPremium(true);
            cliente.setCuotaAnual(cuotaBase.multiply(BigDecimal.valueOf(0.70)));
            repository.save(cliente);
            
            model.addAttribute("mensaje", "Escalado a premium");
        });
        
        return "redirect:/clientes";
    }

    // POST - Rebajar a estándar
    @PostMapping("/reducir")
    public String reducirPremium(@RequestParam Integer id, Model model) {
        
        repository.findById(id).ifPresent(cliente -> {
            if (!cliente.getEsPremium()) {
                model.addAttribute("error", "Ya es estándar");
                return;
            }
            
            cliente.setEsPremium(false);
            cliente.setCuotaAnual(BigDecimal.ZERO);
            repository.save(cliente);
            
            model.addAttribute("mensaje", "Rebajado a estándar");
        });
        
        return "redirect:/clientes";
    }

    // POST - Dar de baja
    @PostMapping("/eliminar")
    public String darBaja(@RequestParam Integer id, Model model) {
        
        repository.findById(id).ifPresent(cliente -> {
            cliente.setFechaBaja(LocalDateTime.now());
            repository.save(cliente);
        });
        
        return "redirect:/clientes?mensaje=Cliente+dado+de+baja";
    }

    // POST - Reactivar
    @PostMapping("/reactivar")
    public String reactivar(@RequestParam Integer id, Model model) {
        
        repository.findById(id).ifPresent(cliente -> {
            cliente.setFechaBaja(null);
            repository.save(cliente);
        });
        
        return "redirect:/clientes/baja?mensaje=Cliente+reactivado";
    }
}