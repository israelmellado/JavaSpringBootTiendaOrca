/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ChatBotIAControlador {

    private final String API_URL = "http://localhost:11434/api/chat";
    private final String MODEL = "llama3.2:1b";
    
    private final String CONTEXTO = """
        Eres un asistente útil de Tienda ORCA.
        Vendemos: móviles (Samsung, iPhone, Xiaomi), portátiles (MacBook, Gaming), 
        cables USB y HDMI, material de oficina (papel, bolígrafos, carpetas),
        rotuladores Copic, calculadoras.
        Precios: desde 1.50€ hasta 1854€
        Descuento: 30% para clientes premium
        Envío: gratis a partir de 50€
        Estados pedido: PENDIENTE, ENVIAR, CANCELADO
        """;
    
    @PostMapping("/chat-ia")
    public String preguntarIA(@RequestParam String pregunta, 
                              HttpServletRequest request, 
                              HttpSession session) {
        try {
            RestTemplate rt = new RestTemplate();
            
            List<Map<String, String>> mensajes = new ArrayList<>();
            mensajes.add(Map.of("role", "system", "content", CONTEXTO));
            mensajes.add(Map.of("role", "user", "content", pregunta));
            
            Map<String, Object> body = new HashMap<>();
            body.put("model", MODEL);
            body.put("messages", mensajes);
            body.put("stream", false);
            body.put("options", Map.of(
                "temperature", 0.3,       
                "num_predict", 80,        
                "num_thread", 4           
            ));
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> petition = new HttpEntity<>(body, headers);
            ResponseEntity<Map> resposta = rt.postForEntity(API_URL, petition, Map.class);
            
            String respostaIA = extractMessage(resposta.getBody());
            
            // GUARDAR EN SESIÓN: Almacenamos los datos para que persistan tras la redirección
            session.setAttribute("preguntaIA", pregunta);
            session.setAttribute("respuestaIA", respostaIA);
            
        } catch (Exception e) {
            session.setAttribute("respuestaIA", "Error: " + e.getMessage());
            session.setAttribute("preguntaIA", pregunta);
        }
        
        // REDIRECCIÓN SEGURA: Volvemos a la URL exacta desde la que el usuario preguntó
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        }
        
        return "redirect:/index"; 
    }
    
    private String extractMessage(Map body) {
        if (body != null && body.containsKey("message")) {
            Map message = (Map) body.get("message");
            return (String) message.get("content");
        }
        return "Sense resposta";
    }
}