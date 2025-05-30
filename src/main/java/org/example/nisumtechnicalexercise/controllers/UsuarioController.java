package org.example.nisumtechnicalexercise.controllers;

import lombok.extern.slf4j.Slf4j;
import org.example.nisumtechnicalexercise.entities.ApiResponse;
import org.example.nisumtechnicalexercise.entities.LoginRequest;
import org.example.nisumtechnicalexercise.entities.Usuario;
import org.example.nisumtechnicalexercise.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Component
@RestController
@RequestMapping("/nisum/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    //Se pidió un RESTful, por tanto, se hace el CRUD completo...

    //No solicitado, pero es útil para probar los cambios en los usuarios
    @GetMapping("/obtenerTodos")
    public ResponseEntity<ApiResponse> getAll() {
        ApiResponse response = usuarioService.getAll();
        return ResponseEntity.ok(response);
    }

    //1- Ingresar usuario
    @PostMapping("/ingresarUsuario")
    public ResponseEntity<ApiResponse> createUser(@RequestBody Usuario usuario) {
        ApiResponse response = usuarioService.createUser(usuario);
        return ResponseEntity.ok(response);
    }

    //2- Obtener usuario por email
    @GetMapping("/obtenerUsuario")
    public ResponseEntity<ApiResponse> getUserByEmail(@RequestParam("email") String email) {
        ApiResponse response = usuarioService.getUserByEmail(email);
        return ResponseEntity.ok(response);
    }

    //3- Actualizar usuario por mail
    @PutMapping("/actualizarUsuario")
    public ResponseEntity<ApiResponse> updateUser(@RequestParam("email") String email,
                                                  @RequestBody Usuario usuarioDetails) {
        ApiResponse response = usuarioService.updateUser(email, usuarioDetails);
        return ResponseEntity.ok(response);
    }

    //4- Eliminar usuario por mail
    @DeleteMapping("/borrarUsuario")
    public ResponseEntity<ApiResponse> deleteUser(@RequestParam("email") String email) {
        ApiResponse response = usuarioService.deleteUser(email);
        return ResponseEntity.ok(response);
    }

    // validador de tokens
    @GetMapping("/validaToken")
    public ResponseEntity<ApiResponse> validateToken(
            @RequestHeader("Authorization") String token,
            @RequestParam("email") String email) {
        ApiResponse response = usuarioService.validateToken(token, email);
        return ResponseEntity.ok(response);
    }

    // para generar un nuevo JWT, ya que solo se estaba generando al crear al user
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest loginRequest) {
        ApiResponse response = usuarioService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(response);
    }
}
