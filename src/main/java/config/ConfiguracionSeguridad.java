package config;

import modelo.Usuario;
import repositorio.UsuarioRepositorio;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridad {

    private final UsuarioRepositorio usuarioRepositorio;
    
    public ConfiguracionSeguridad(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/test", "/login", "/logout").permitAll()
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .defaultSuccessUrl("/menu", true)
            .permitAll()
        )
        .logout(logout -> logout
            .logoutSuccessUrl("/login?logout")
            .permitAll()
        );

    return http.build();
}
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        System.out.println("=== PasswordEncoder bean created ===");
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }

    
    @Bean
    public UserDetailsService userDetailsService() {
        System.out.println("=== UserDetailsService bean created ===");
        
        return username -> {
            System.out.println("=== Buscando en MySQL: " + username + " ===");
            
            // CARGAR DE MYSQL
            Usuario usuario = usuarioRepositorio.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("=== NO ENCONTRADO ===");
                    return new UsernameNotFoundException("No: " + username);
                });
            
            System.out.println("=== ENCONTRADO: " + usuario.getUsername() + " ===");
            
            String roles = usuario.getRoles();
            if (roles == null || roles.isEmpty()) roles = "USER";

            return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(roles.split(","))
                .build();
        };
    }
    
    @Bean
public DaoAuthenticationProvider daoAuthenticationProvider() {
    System.out.println("=== DaoAuthenticationProvider bean created ===");
    
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService());
    provider.setPasswordEncoder(passwordEncoder());  // <-- IMPORTANTE
    
    return provider;
}
    
    @Bean
    public AuthenticationManager authenticationManager() {
        System.out.println("=== AuthenticationManager bean created ===");
        
        return new ProviderManager(List.of(daoAuthenticationProvider()));
    }
}