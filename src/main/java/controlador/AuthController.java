package controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login-page")
    public String loginForm() {
        return "login";
    }
    
    @GetMapping("/menu")
    public String menu() {
        System.out.println("=== CARGANDO /MENU ===");
        return "index";
    }
    
    @GetMapping("/gestion")
    public String admin() {
        return "admin";
    }
    
    @GetMapping("/mi-usuario")
    public String usuario() {
        return "usuario";
    }
    
    @GetMapping("/lista-articulos")
    public String articulos() {
        return "articulos";
    }
    
    @GetMapping("/lista-clientes")
    public String clientes() {
        return "clientes";
    }
    
    @GetMapping("/lista-pedidos")
    public String pedidos() {
        return "pedidos";
    }
}