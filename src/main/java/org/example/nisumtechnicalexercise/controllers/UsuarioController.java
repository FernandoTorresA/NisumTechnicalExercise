package org.example.nisumtechnicalexercise.controllers;

import lombok.extern.slf4j.Slf4j;
import org.example.nisumtechnicalexercise.entities.Usuario;
import org.example.nisumtechnicalexercise.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Slf4j
@Component
@RestController
@RequestMapping("/nisum/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

//    Se pidió un RESTful, por tanto, se hace el CRUD completo...

    //No solicitado, pero es útil para probar los cambios
    @GetMapping("/obtenerTodos")
    public ResponseEntity<List<Usuario>> getAll() {
        log.info("[getAllColaborador] Request recieved...");
        List<Usuario> colaboradorList = usuarioService.getAll();
        return ResponseEntity.ok(colaboradorList);
    }
}
