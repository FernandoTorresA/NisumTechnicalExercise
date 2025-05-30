package org.example.nisumtechnicalexercise.controllers;

import lombok.extern.slf4j.Slf4j;
import org.example.nisumtechnicalexercise.entities.Usuario;
import org.example.nisumtechnicalexercise.helper.JwtHelper;
import org.example.nisumtechnicalexercise.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RestController
@RequestMapping("/nisum/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    JwtHelper jwtHelper;

//    Se pidió un RESTful, por tanto, se hace el CRUD completo...

    //No solicitado, pero es útil para probar los cambios
    @GetMapping("/obtenerTodos")
    public ResponseEntity<List<Usuario>> getAll() {
        log.info("[getAllColaborador] Request recieved...");
        List<Usuario> colaboradorList = usuarioService.getAll();
        return ResponseEntity.ok(colaboradorList);
    }

    //1- Ingresar usuario
    @PostMapping("/ingresarUsuario")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody Usuario usuario) {

        log.info("[createUser] Request recieved...");
        Map<String, Object> response = new HashMap<>();

        //1- Guardar user...
        usuarioService.createUser(usuario);

        //2- Generar token jwt
        String token = jwtHelper.generateToken(usuario);

        //3- como lo acabo de crear... siempre está habilitado...
        boolean isActive = true;

        response.put("usuario", usuario);
        response.put("token", "Bearer " + token);
        response.put("isActive", isActive);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/validaToken")
    public ResponseEntity<Map<String, Object>> validateToken(
            @RequestHeader("Authorization") String token,
            @RequestParam String email) {

        log.info("[validarToken] Request received...");

        Map<String, Object> response = new HashMap<>();

        // 1. Validar el token
        boolean esValido = jwtHelper.validarToken(token, email);

        response.put("email", email);
        response.put("isActive", esValido);

        return ResponseEntity.ok(response);
    }

}
