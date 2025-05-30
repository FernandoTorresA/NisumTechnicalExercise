package org.example.nisumtechnicalexercise.services;

import org.example.nisumtechnicalexercise.entities.ApiResponse;
import org.example.nisumtechnicalexercise.entities.Usuario;

public interface UsuarioService {

    ApiResponse getAll();

    ApiResponse createUser(Usuario usuario);

    ApiResponse deleteUser(String email);

    ApiResponse updateUser(String email, Usuario usuarioDetails);

    ApiResponse getUserByEmail(String email);

    ApiResponse validateToken(String token, String email);

    ApiResponse loginUser(String email, String password);
}
