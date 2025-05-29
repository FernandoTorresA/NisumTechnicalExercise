package org.example.nisumtechnicalexercise.entities;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import lombok.Data;

@Entity
@Data
@Table(name = "telefono")
public class Telefono {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "numero", nullable = false)
    private Long numero;

    @Column(name = "codigo_ciudad", nullable = false)
    private Long codigo_ciudad;

    @Column(name = "codigo_pais", nullable = false)
    private Long codigo_pais;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}

