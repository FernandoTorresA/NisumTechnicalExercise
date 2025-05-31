package org.example.nisumtechnicalexercise.controllers;

import lombok.extern.slf4j.Slf4j;
import org.example.nisumtechnicalexercise.entities.RespuestaApi;
import org.example.nisumtechnicalexercise.entities.LoginRequest;
import org.example.nisumtechnicalexercise.entities.Usuario;
import org.example.nisumtechnicalexercise.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Tag(name = "Usuarios", description = "CRUD de usuarios con validación JWT")
@Slf4j
@Component
@RestController
@RequestMapping("/nisum/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    //Se pidió un RESTful, por tanto, se hace el CRUD completo...

    //1- No solicitado, pero es útil para probar los cambios en los usuarios
    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve la lista completa de usuarios")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida correctamente")
    @ApiResponse(responseCode = "204", description = "No hay usuarios en la base de datos")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    @GetMapping("/obtenerTodos")
    public ResponseEntity<RespuestaApi> getAll() {
        return usuarioService.getAll();
    }

    // 2- Ingresar usuario
    @Operation(summary = "Crear un usuario", description = "Registra un nuevo usuario en la base de datos")
    @ApiResponse(responseCode = "201", description = "Usuario creado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos (email o contraseña incorrectos)")
    @ApiResponse(responseCode = "409", description = "Usuario ya existe")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    @PostMapping("/ingresarUsuario")
    public ResponseEntity<RespuestaApi> createUser(@RequestBody Usuario usuario) {
        return usuarioService.createUser(usuario);
    }

    // 3- Obtener usuario por email
    @Operation(summary = "Obtener usuario por email", description = "Busca un usuario registrado por su dirección de correo")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    @GetMapping("/obtenerUsuario")
    public ResponseEntity<RespuestaApi> getUserByEmail(@RequestParam("email") String email) {
        return usuarioService.getUserByEmail(email);
    }

    // 4- Actualizar usuario por mail
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    @PutMapping("/actualizarUsuario")
    public ResponseEntity<RespuestaApi> updateUser(@RequestParam("email") String email,
                                                   @RequestBody Usuario usuarioDetails) {
        return usuarioService.updateUser(email, usuarioDetails);
    }

    // 5- Eliminar usuario por mail
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario de la base de datos por email")
    @ApiResponse(responseCode = "200", description = "Usuario eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    @DeleteMapping("/borrarUsuario")
    public ResponseEntity<RespuestaApi> deleteUser(@RequestParam("email") String email) {
        return usuarioService.deleteUser(email);
    }

    // 6- validador de tokens
    @Operation(summary = "Validar token JWT", description = "Verifica si el token JWT es válido para el usuario")
    @ApiResponse(responseCode = "200", description = "Token validado correctamente")
    @ApiResponse(responseCode = "401", description = "Token inválido")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    @GetMapping("/validaToken")
    public ResponseEntity<RespuestaApi> validateToken(
            @RequestHeader("Authorization") String token,
            @RequestParam("email") String email) {
        return usuarioService.validateToken(token, email);
    }

    // 7- para generar un nuevo JWT, ya que solo se estaba generando al crear al user
    @Operation(summary = "Login de usuario", description = "Autentica un usuario y genera un nuevo JWT")
    @ApiResponse(responseCode = "200", description = "Usuario autenticado correctamente")
    @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    @PostMapping("/login")
    public ResponseEntity<RespuestaApi> login(@RequestBody LoginRequest loginRequest) {
        return usuarioService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
    }
}
