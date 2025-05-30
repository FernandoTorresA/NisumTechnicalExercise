package org.example.nisumtechnicalexercise.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "usuario")
public class Usuario {

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq")
//    @SequenceGenerator(name = "usuario_seq", sequenceName = "usuario_seq", allocationSize = 1)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Telefono> telefonos;

    // variables ocultas/internas que no van en el proceso de creación, pero sí después...

    @Column(name = "fecha_creacion")
    private String fechaCreacion;

    @Column(name = "fecha_modificacion")
    private String fechaModificacion;

    @Column(name = "fecha_ultimo_login")
    private String fechaUltimoLogin;

}
