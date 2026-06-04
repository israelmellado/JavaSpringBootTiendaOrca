package controlador;

import modelo.Usuario;
import repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @GetMapping("/test")
    @ResponseBody
    public String test() {
        Usuario u = usuarioRepositorio.findByUsername("admin").orElse(null);
        if (u == null) {
            return "Admin NO encontrado en la base de datos";
        }
        return "Admin encontrado: " + u.getUsername() + 
               ", Password: " + u.getPassword() + 
               ", Roles: " + u.getRoles();
    }
}