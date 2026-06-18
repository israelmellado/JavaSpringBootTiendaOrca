package com.tiendaOrcaSpring.TiendaVirtualOrmSpringBoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.boot.persistence.autoconfigure.EntityScan;
//import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
@EnableJpaRepositories("repositorio")
@EntityScan("modelo")
@ComponentScan({
    "controlador",
    "servicio",
    "repositorio",
    "config"
})

public class TiendaVirtualOrmSpringBootApplication {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("INICIANDO APP...");
        System.out.println("========================================");
        System.out.println("=== DEBUG MCP START ===");
        
        System.out.println("JAVA VERSION: " + System.getProperty("java.version"));
        System.out.println("PATH: " + System.getenv("PATH"));
        System.out.println("PYTHON: " + System.getenv("PYTHON"));
        System.out.println("=== END ENV ===");
        
        SpringApplication.run(TiendaVirtualOrmSpringBootApplication.class, args);
        
        System.out.println("========================================");
        System.out.println("APP INICIADA");
        System.out.println("========================================");
    }
}

	
		/*BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                System.out.println("admin: " + encoder.encode("admin"));
                System.out.println("usuario: " + encoder.encode("usuario"));*/
    

