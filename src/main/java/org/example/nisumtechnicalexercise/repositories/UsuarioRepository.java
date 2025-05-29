package org.example.nisumtechnicalexercise.repositories;

import org.example.nisumtechnicalexercise.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
