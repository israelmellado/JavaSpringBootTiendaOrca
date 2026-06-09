package modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "domicilio")
    private String domicilio;

    @Column(unique = true, nullable = false, length = 10)
    private String nif;

    @Column(name = "es_premium")
    private Boolean esPremium;

    @Column(name = "cuota_anual")
    private BigDecimal cuotaAnual;

    @Column(name = "fecha_alta")
    private LocalDateTime fechaAlta;

    @Column(name = "fecha_baja")
    private LocalDateTime fechaBaja;

    // Constructores
    public Cliente() {}

    public Cliente(String email, String nombre, String domicilio, String nif) {
        this.email = email;
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.nif = nif;
        this.esPremium = false;
        this.cuotaAnual = BigDecimal.ZERO;
        this.fechaAlta = LocalDateTime.now();
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDomicilio() { return domicilio; }
    public void setDomicilio(String domicilio) { this.domicilio = domicilio; }

    public String getNif() { return nif; }
    public void setNif(String nif) { this.nif = nif; }

    public Boolean getEsPremium() { return esPremium; }
    public void setEsPremium(Boolean esPremium) { this.esPremium = esPremium; }

    public BigDecimal getCuotaAnual() { return cuotaAnual; }
    public void setCuotaAnual(BigDecimal cuotaAnual) { this.cuotaAnual = cuotaAnual; }

    public LocalDateTime getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDateTime fechaAlta) { this.fechaAlta = fechaAlta; }

    public LocalDateTime getFechaBaja() { return fechaBaja; }
    public void setFechaBaja(LocalDateTime fechaBaja) { this.fechaBaja = fechaBaja; }
}