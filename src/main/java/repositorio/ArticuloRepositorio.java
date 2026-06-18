/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositorio;


import  modelo.Articulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface ArticuloRepositorio extends JpaRepository<Articulo, Integer> {

    // Buscar por código único
    Optional<Articulo> findByCodigoId(String codigoId);

    // Verificar si existe código
    boolean existsByCodigoId(String codigoId);

    // Artículos activos (sin fecha de baja)
    List<Articulo> findByFechaBajaIsNull();

    // Artículos dados de baja
    List<Articulo> findByFechaBajaIsNotNull();
    
    //--------------------DASHBOARD----------------------------------------------//
    // 📦 Sumar stock total de todos los artículos
        @Query("SELECT COALESCE(SUM(a.stock), 0) FROM Articulo a WHERE a.fechaBaja IS NULL")
    Integer sumarStockTotal();
}