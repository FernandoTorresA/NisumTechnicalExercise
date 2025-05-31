package org.example.nisumtechnicalexercise.services.impl;

import org.example.nisumtechnicalexercise.entities.RespuestaApi;
import org.example.nisumtechnicalexercise.entities.Telefono;
import org.example.nisumtechnicalexercise.entities.Usuario;
import org.example.nisumtechnicalexercise.helpers.JwtHelper;
import org.example.nisumtechnicalexercise.repositories.UsuarioRepository;
import org.example.nisumtechnicalexercise.services.UsuarioService;

import java.time.LocalDateTime;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.example.nisumtechnicalexercise.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<RespuestaApi> getAll() {
        try {
            List<Usuario> usuarios = usuarioRepository.findAll();

            if (usuarios.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new RespuestaApi(Constants.CODE_NOT_FOUND, Constants.MSG_USERS_NOT_FOUND, null));
            }

            return ResponseEntity.ok(new RespuestaApi(Constants.CODE_SUCCESS, Constants.MSG_SUCCESS, usuarios));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RespuestaApi(Constants.CODE_DB_ERROR, Constants.MSG_DB_ERROR
                            + ": " + e.getMessage(), null));
        }
    }

    @Override
    public ResponseEntity<RespuestaApi> createUser(Usuario usuario) {
        try {
            // Verificar si el usuario ya existe
            Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
            if (usuarioExistente.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new RespuestaApi(Constants.CODE_VALIDATION_ERROR, Constants.MSG_USER_EXISTS,
                                null));
            }

            // Validar emaill
            if (!usuario.getEmail().matches(emailValidator)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new RespuestaApi(Constants.CODE_VALIDATION_ERROR, Constants.MSG_VALIDATION_ERROR,
                                null));
            }

            // Validar contraseña...
            if (!usuario.getPassword().matches(passwordValidator)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new RespuestaApi(Constants.CODE_VALIDATION_ERROR, Constants.MSG_VALIDATION_ERROR,
                                null));
            }

            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

            usuario.setFechaCreacion(LocalDateTime.now().toString());
            usuario.setFechaModificacion(LocalDateTime.now().toString());
            usuario.setFechaUltimoLogin(LocalDateTime.now().toString());

            usuarioRepository.save(usuario);

            // Generar token JWT
            String token = jwtHelper.generateToken(usuario);

            // Crear respuesta
            Map<String, Object> data = new HashMap<>();
            data.put("usuario", usuario);
            data.put("token", "Bearer " + token);
            data.put("isActive", true);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new RespuestaApi(Constants.CODE_SUCCESS, Constants.MSG_SUCCESS, data));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RespuestaApi(Constants.CODE_DB_ERROR, Constants.MSG_DB_ERROR + ": "
                            + e.getMessage(), null));
        }
    }

    @Override
    public ResponseEntity<RespuestaApi> getUserByEmail(String email) {
        try {
            Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);

            if (usuarioOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new RespuestaApi(Constants.CODE_NOT_FOUND, Constants.MSG_USER_NOT_FOUND, null));
            }

            Usuario usuario = usuarioOptional.get();
            Map<String, Object> data = new HashMap<>();
            data.put("usuario", usuario);

            return ResponseEntity.ok(new RespuestaApi(Constants.CODE_SUCCESS, Constants.MSG_SUCCESS, data));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RespuestaApi(Constants.CODE_DB_ERROR, Constants.MSG_DB_ERROR + ": "
                            + e.getMessage(), null));
        }
    }

    @Override
    public ResponseEntity<RespuestaApi> updateUser(String email, Usuario usuarioDetails) {
        try {
            Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);

            if (usuarioOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new RespuestaApi(Constants.CODE_NOT_FOUND, Constants.MSG_USER_NOT_FOUND, null));
            }

            Usuario usuario = usuarioOptional.get();
            usuario.setNombre(usuarioDetails.getNombre());

            if (usuarioDetails.getPassword() != null && !usuarioDetails.getPassword().isEmpty()) {
                usuario.setPassword(passwordEncoder.encode(usuarioDetails.getPassword()));
            }

            if (usuarioDetails.getTelefonos() != null && !usuarioDetails.getTelefonos().isEmpty()) {
                if (usuario.getTelefonos() == null) {
                    usuario.setTelefonos(new ArrayList<>());
                } else {
                    usuario.getTelefonos().clear();
                }
                for (Telefono telefono : usuarioDetails.getTelefonos()) {
                    telefono.setUsuario(usuario);
                }
                usuario.getTelefonos().addAll(usuarioDetails.getTelefonos());
            }

            usuario.setFechaModificacion(LocalDateTime.now().toString());
            usuarioRepository.save(usuario);

            return ResponseEntity.ok(new RespuestaApi(Constants.CODE_SUCCESS, Constants.MSG_USER_UPDATED, usuario));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RespuestaApi(Constants.CODE_DB_ERROR, Constants.MSG_DB_ERROR + ": "
                            + e.getMessage(), null));
        }
    }

    @Override
    public ResponseEntity<RespuestaApi> deleteUser(String email) {
        try {
            Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);

            if (usuarioOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new RespuestaApi(Constants.CODE_NOT_FOUND, Constants.MSG_USER_NOT_FOUND, null));
            }

            usuarioRepository.delete(usuarioOptional.get());
            return ResponseEntity.ok(new RespuestaApi(Constants.CODE_SUCCESS, Constants.MSG_USER_DELETED, null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RespuestaApi(Constants.CODE_DB_ERROR, Constants.MSG_DB_ERROR + ": "
                            + e.getMessage(), null));
        }
    }

    @Override
    public ResponseEntity<RespuestaApi> validateToken(String token, String email) {
        try {
            boolean esValido = jwtHelper.validarToken(token, email);

            Map<String, Object> data = new HashMap<>();
            data.put("email", email);
            data.put("isActive", esValido);

            if (!esValido) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new RespuestaApi(Constants.CODE_VALIDATION_ERROR, "Token inválido", null));
            }

            return ResponseEntity.ok(new RespuestaApi(Constants.CODE_SUCCESS, Constants.MSG_TOKEN_VALIDATED, data));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RespuestaApi(Constants.CODE_DB_ERROR, Constants.MSG_DB_ERROR + ": "
                            + e.getMessage(), null));
        }
    }

    @Override
    public ResponseEntity<RespuestaApi> loginUser(String email, String password) {
        try {
            Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
            if (usuarioOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new RespuestaApi(Constants.CODE_NOT_FOUND, Constants.MSG_USER_NOT_FOUND, null));
            }

            Usuario usuario = usuarioOptional.get();

            if (!passwordEncoder.matches(password, usuario.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new RespuestaApi(Constants.CODE_VALIDATION_ERROR,
                                "Credenciales inválidas", null));
            }

            // Generar nuevo token jwt
            String token = jwtHelper.generateToken(usuario);

            usuario.setFechaUltimoLogin(LocalDateTime.now().toString());
            usuarioRepository.save(usuario);

            Map<String, Object> data = new HashMap<>();
            data.put("token", "Bearer " + token);
            data.put("email", email);

            return ResponseEntity.ok(new RespuestaApi(Constants.CODE_SUCCESS, Constants.MSG_TOKEN_VALIDATED, data));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RespuestaApi(Constants.CODE_DB_ERROR, Constants.MSG_DB_ERROR + ": "
                            + e.getMessage(), null));
        }
    }
}
