package org.example.nisumtechnicalexercise.services.impl;

import org.example.nisumtechnicalexercise.entities.ApiResponse;
import org.example.nisumtechnicalexercise.entities.Telefono;
import org.example.nisumtechnicalexercise.entities.Usuario;
import org.example.nisumtechnicalexercise.helpers.JwtHelper;
import org.example.nisumtechnicalexercise.repositories.UsuarioRepository;
import org.example.nisumtechnicalexercise.services.UsuarioService;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.nisumtechnicalexercise.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    JwtHelper jwtHelper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ApiResponse getAll() {
        try {
            List<Usuario> usuarios = usuarioRepository.findAll();

            if (usuarios.isEmpty()) {
                return new ApiResponse(Constants.CODE_NOT_FOUND, Constants.MSG_USERS_NOT_FOUND, null);
            } else {
                return new ApiResponse(Constants.CODE_SUCCESS, Constants.MSG_SUCCESS, usuarios);
            }
        } catch (Exception e) {
            return new ApiResponse(Constants.CODE_DB_ERROR, Constants.MSG_DB_ERROR + ": " + e.getMessage(),
                    null);
        }
    }

    @Override
    public ApiResponse createUser(Usuario usuario) {
        try {
            // Verificar si el usuario ya existe
            Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
            if (usuarioExistente.isPresent()) {
                return new ApiResponse(Constants.CODE_VALIDATION_ERROR, Constants.MSG_USER_EXISTS, null);
            }

            // Validar email
            if (!usuario.getEmail().matches(emailValidator)) {
                return new ApiResponse(Constants.CODE_VALIDATION_ERROR, Constants.MSG_VALIDATION_ERROR, null);
            }

            // Validar contraseña
            if (!usuario.getPassword().matches(passwordValidator)) {
                return new ApiResponse(Constants.CODE_VALIDATION_ERROR, Constants.MSG_VALIDATION_ERROR, null);
            }

            // Nunca debería guardar passwords como texto plano, se usa spring para cifrarla antes de almacenarla
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

            // Asignar fechas
            usuario.setFechaCreacion(LocalDateTime.now().toString());
            usuario.setFechaModificacion(LocalDateTime.now().toString());
            usuario.setFechaUltimoLogin(LocalDateTime.now().toString());

            // Guardar usuario en la base de datos
            usuarioRepository.save(usuario);

            // Generar token JWT
            String token = jwtHelper.generateToken(usuario);

            // Crear respuesta
            Map<String, Object> data = new HashMap<>();
            data.put("usuario", usuario);
            data.put("token", "Bearer " + token);
            data.put("isActive", true);

            return new ApiResponse(Constants.CODE_SUCCESS, Constants.MSG_SUCCESS, data);

        } catch (Exception e) {
            return new ApiResponse(Constants.CODE_DB_ERROR, Constants.MSG_DB_ERROR + ": " + e.getMessage(),
                    null);
        }
    }

    @Override
    public ApiResponse getUserByEmail(String email) {
        try {
            Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
            if (usuarioOptional.isPresent()) {
                Usuario usuario = usuarioOptional.get();

                Map<String, Object> data = new HashMap<>();
                data.put("usuario", usuario);
                data.put("password", usuario.getPassword());

                return new ApiResponse(Constants.CODE_SUCCESS, Constants.MSG_SUCCESS, data);
            } else {
                return new ApiResponse(Constants.CODE_NOT_FOUND, Constants.MSG_USER_NOT_FOUND, null);
            }
        } catch (Exception e) {
            return new ApiResponse(Constants.CODE_DB_ERROR, Constants.MSG_DB_ERROR + ": " + e.getMessage(), null);
        }
    }

    @Override
    public ApiResponse validateToken(String token, String email) {
        try {
            boolean esValido = jwtHelper.validarToken(token, email);

            Map<String, Object> data = new HashMap<>();
            data.put("email", email);
            data.put("isActive", esValido);

            return new ApiResponse(Constants.CODE_SUCCESS, Constants.MSG_TOKEN_VALIDATED, data);
        } catch (Exception e) {
            return new ApiResponse(Constants.CODE_DB_ERROR, Constants.MSG_DB_ERROR + ": " + e.getMessage(),
                    null);
        }
    }

    @Override
    public ApiResponse updateUser(String email, Usuario usuarioDetails) {
        try {
            Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
            if (usuarioOptional.isPresent()) {
                Usuario usuario = usuarioOptional.get();
                usuario.setNombre(usuarioDetails.getNombre());

                if (usuarioDetails.getTelefonos() != null && !usuarioDetails.getTelefonos().isEmpty()) {
                    usuario.getTelefonos().clear();
                    for (Telefono telefono : usuarioDetails.getTelefonos()) {
                        telefono.setUsuario(usuario);
                    }
                    usuario.getTelefonos().addAll(usuarioDetails.getTelefonos());
                }

                usuario.setFechaModificacion(LocalDateTime.now().toString());

                usuarioRepository.save(usuario);
                return new ApiResponse(Constants.CODE_SUCCESS, Constants.MSG_USER_UPDATED, usuario);
            } else {
                return new ApiResponse(Constants.CODE_NOT_FOUND, Constants.MSG_USER_NOT_FOUND, null);
            }
        } catch (Exception e) {
            return new ApiResponse(Constants.CODE_DB_ERROR, Constants.MSG_DB_ERROR + ": " + e.getMessage(), null);
        }
    }

    @Override
    public ApiResponse deleteUser(String email) {
        try {
            Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
            if (usuarioOptional.isPresent()) {
                usuarioRepository.delete(usuarioOptional.get());
                return new ApiResponse(Constants.CODE_SUCCESS, Constants.MSG_USER_DELETED, null);
            } else {
                return new ApiResponse(Constants.CODE_NOT_FOUND, Constants.MSG_USER_NOT_FOUND, null);
            }
        } catch (Exception e) {
            return new ApiResponse(Constants.CODE_DB_ERROR, Constants.MSG_DB_ERROR + ": " + e.getMessage(),
                    null);
        }
    }

    @Override
    public ApiResponse loginUser(String email, String password) {
        try {
            Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
            if (usuarioOptional.isEmpty()) {
                return new ApiResponse(Constants.CODE_NOT_FOUND, Constants.MSG_USER_NOT_FOUND, null);
            }

            Usuario usuario = usuarioOptional.get();

            if (!passwordEncoder.matches(password, usuario.getPassword())) {
                return new ApiResponse(Constants.CODE_VALIDATION_ERROR, "Credenciales inválidas", null);
            }

            // Generar nuevo token JWT
            String token = jwtHelper.generateToken(usuario);

            // Actualizar la fecha del último login
            usuario.setFechaUltimoLogin(LocalDateTime.now().toString());
            usuarioRepository.save(usuario);

            // Construir respuesta
            Map<String, Object> data = new HashMap<>();
            data.put("token", "Bearer " + token);
            data.put("email", email);

            return new ApiResponse(Constants.CODE_SUCCESS, Constants.MSG_TOKEN_VALIDATED, data);

        } catch (Exception e) {
            return new ApiResponse(Constants.CODE_DB_ERROR, Constants.MSG_DB_ERROR + ": " + e.getMessage(),
                    null);
        }
    }
}