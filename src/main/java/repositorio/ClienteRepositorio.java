/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package repositorio;

import modelo.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ClienteRepositorio extends JpaRepository<Cliente, Integer> {
    
    Optional<Cliente> findByEmail(String email);
    
    Optional<Cliente> findByNif(String nif);
    
    boolean existsByEmail(String email);
    
    boolean existsByNif(String nif);
    
    List<Cliente> findByFechaBajaIsNull();
    
    List<Cliente> findByFechaBajaIsNotNull();
    
    List<Cliente> findByEsPremiumTrueAndFechaBajaIsNull();
    
    List<Cliente> findByEsPremiumFalseAndFechaBajaIsNull();
}