package org.example.nisumtechnicalexercise.services;

import org.example.nisumtechnicalexercise.entities.RespuestaApi;
import org.example.nisumtechnicalexercise.entities.Usuario;
import org.springframework.http.ResponseEntity;

public interface UsuarioService {

    ResponseEntity<RespuestaApi> getAll();

    ResponseEntity<RespuestaApi> createUser(Usuario usuario);

    ResponseEntity<RespuestaApi> getUserByEmail(String email);

    ResponseEntity<RespuestaApi> updateUser(String email, Usuario usuarioDetails);

    ResponseEntity<RespuestaApi> deleteUser(String email);

    ResponseEntity<RespuestaApi> validateToken(String token, String email);

    ResponseEntity<RespuestaApi> loginUser(String email, String password);
}
