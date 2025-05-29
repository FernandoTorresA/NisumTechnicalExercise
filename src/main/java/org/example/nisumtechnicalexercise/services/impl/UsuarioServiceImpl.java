package org.example.nisumtechnicalexercise.services.impl;

import org.example.nisumtechnicalexercise.entities.Usuario;
import org.example.nisumtechnicalexercise.repositories.UsuarioRepository;
import org.example.nisumtechnicalexercise.services.UsuarioService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }
}
