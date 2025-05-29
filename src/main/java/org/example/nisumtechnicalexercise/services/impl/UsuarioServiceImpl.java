package org.example.nisumtechnicalexercise.services.impl;

import org.example.nisumtechnicalexercise.entities.Usuario;
import org.example.nisumtechnicalexercise.services.UsuarioService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {
    @Override
    public List<Usuario> getAll() {
        return null;
    }
}
