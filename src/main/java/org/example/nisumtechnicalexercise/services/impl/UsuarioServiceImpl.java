package org.example.nisumtechnicalexercise.services.impl;

import org.example.nisumtechnicalexercise.entities.Usuario;
import org.example.nisumtechnicalexercise.repositories.UsuarioRepository;
import org.example.nisumtechnicalexercise.services.UsuarioService;

import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {

    @Value("${password.validator}")
    protected String passwordValidator;

    @Value("${email.validator}")
    protected String emailValidator;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario createUser(Usuario usuario) {

        if (!usuario.getEmail().matches(emailValidator)) {
            throw new IllegalArgumentException("El correo no tiene un formato válido");
        }

        if (!usuario.getPassword().matches(passwordValidator)) {
            throw new IllegalArgumentException("La contraseña no cumple con los requisitos");
        }

        usuario.setFechaCreacion(LocalDateTime.now().toString());
        usuario.setFechaModificacion(LocalDateTime.now().toString());
        usuario.setFechaUltimoLogin(LocalDateTime.now().toString());

        usuarioRepository.save(usuario);
        return usuario;
    }
}
