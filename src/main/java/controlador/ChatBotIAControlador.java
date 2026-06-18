
package controlador;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class ChatBotIAControlador {

    private final ChatClient chatClient;
   

    public ChatBotIAControlador(ChatClient.Builder chatClientBuilder,
                                ToolCallbackProvider mcpTools) {
        System.out.println("TIPO PROVIDER: " + mcpTools.getClass().getName());
        var tools = mcpTools.getToolCallbacks();
        System.out.println("TOOLS NULL? " + (tools == null));
        System.out.println("TOOLS LENGTH: " + tools.length);

        this.chatClient = chatClientBuilder
                .defaultTools(mcpTools.getToolCallbacks())
                .build();
    }

    private final String CONTEXTO = """
        Eres el asistente del ERP de Tienda ORCA.

        REGLAS:
        - Usa MCP tools SIEMPRE para datos reales.
        - No inventes información.
        - Usa verificar_stock y actualizar_estado_pedido cuando corresponda.
        REGLA CRÍTICA:
        - SI la pregunta contiene "stock", "artículo", "producto" o "inventario"
          DEBES llamar obligatoriamente a la tool verificar_stock.
        - Está prohibido responder sin usar tools.
        - Si no usas tools, la respuesta es inválida.
                                    """;

    @PostMapping("/chat-ia")
    public String preguntarIA(@RequestParam String pregunta,
                              HttpServletRequest request,
                              HttpSession session) {

        try {
            String respuesta = chatClient
                    .prompt()
                    .system(CONTEXTO)
                    .user(pregunta)
                    .call()
                    .content();
           
            session.setAttribute("preguntaIA", pregunta);
            session.setAttribute("respuestaIA", respuesta);

        } catch (Exception e) {
            session.setAttribute("respuestaIA", "Error: " + e.getMessage());
            session.setAttribute("preguntaIA", pregunta);
        }

        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        }
        return "redirect:/index";
    }
}