package org.example.nisumtechnicalexercise.services;

import org.example.nisumtechnicalexercise.entities.ApiResponse;
import org.example.nisumtechnicalexercise.entities.Telefono;
import org.example.nisumtechnicalexercise.entities.Usuario;
import org.example.nisumtechnicalexercise.helpers.JwtHelper;
import org.example.nisumtechnicalexercise.repositories.UsuarioRepository;
import org.example.nisumtechnicalexercise.services.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceImplTest {

    @InjectMocks
    UsuarioServiceImpl usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JwtHelper jwtHelper;

    @Mock
    private PasswordEncoder passwordEncoder;

    private List<Usuario> usuarios;
    private Usuario usuario;
    private Usuario usuarioActualizado;
    private String email;
    private String validPassword;
    private String invalidPassword;
    private String validToken;
    private String invalidToken;

    @BeforeEach
    public void setUp() {

        email = "fernando@example.com";
        validPassword = "A123!sdfsdf";
        invalidPassword = "WrongPass";
        validToken = "valid-jwt-token";
        invalidToken = "invalid-jwt-token";

        usuario = new Usuario();
        usuario.setNombre("Fernando");
        usuario.setEmail("fernando@example.com");
        usuario.setPassword("A123!sdfsdf");

        Usuario usuario2 = new Usuario();
        usuario2.setNombre("Alan");
        usuario2.setEmail("alan@example.com");
        usuario2.setPassword("A123!sdfsdf");

        usuarios = Arrays.asList(usuario, usuario2);

        Telefono telefono1 = new Telefono();
        telefono1.setNumero(987654321L);
        telefono1.setCodigoCiudad(2L);
        telefono1.setCodigoPais(591L);

        Telefono telefono2 = new Telefono();
        telefono2.setNumero(987654322L);
        telefono2.setCodigoCiudad(3L);
        telefono2.setCodigoPais(591L);

        usuario.setTelefonos(new ArrayList<>(Arrays.asList(telefono1, telefono2)));

        usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre("Fernando Torres");
        usuarioActualizado.setEmail("fernando@example.com");
        usuarioActualizado.setPassword("NuevoPass123!");
        usuarioActualizado.setTelefonos(new ArrayList<>(Arrays.asList(telefono2)));

        // Para emular los valores q están en properties pero el test no los conoce
        ReflectionTestUtils.setField(usuarioService, "emailValidator",
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

        ReflectionTestUtils.setField(usuarioService, "passwordValidator",
                "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    }

    // TESTS para getAll() ----------------------------------------------------

    @Test
    public void testGetAllUsuariosFound() {
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        ApiResponse response = usuarioService.getAll();

        assertEquals("00", response.getCodigo());
        assertEquals("Operación exitosa", response.getMensaje());
        assertEquals(usuarios, response.getData());
    }

    @Test
    public void testGetAllUsuariosNotFound() {
        when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());

        ApiResponse response = usuarioService.getAll();

        assertEquals("03", response.getCodigo());
        assertEquals("No se encontraron usuarios", response.getMensaje());
        assertEquals(null, response.getData());
    }

    @Test
    public void testGetAllDatabaseError() {
        when(usuarioRepository.findAll()).thenThrow(new RuntimeException("DB connection failed"));

        ApiResponse response = usuarioService.getAll();

        assertEquals("01", response.getCodigo());
        assertEquals("Error en la base de datos: DB connection failed", response.getMensaje());
        assertEquals(null, response.getData());
    }

    // TESTS para createUser() ----------------------------------------------------

    @Test
    public void testCreateUser_UserAlreadyExists() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        ApiResponse response = usuarioService.createUser(usuario);

        assertEquals("02", response.getCodigo());
        assertEquals("El usuario ya existe", response.getMensaje());
        assertEquals(null, response.getData());
    }

    @Test
    public void testCreateUser_InvalidEmail() {
        usuario.setEmail("invalid-email");

        ApiResponse response = usuarioService.createUser(usuario);

        assertEquals("02", response.getCodigo());
        assertEquals("Error de validación", response.getMensaje());
        assertEquals(null, response.getData());
    }

    @Test
    public void testCreateUser_InvalidPassword() {
        usuario.setPassword("short");

        ApiResponse response = usuarioService.createUser(usuario);

        assertEquals("02", response.getCodigo());
        assertEquals("Error de validación", response.getMensaje());
        assertEquals(null, response.getData());
    }

    @Test
    public void testCreateUser_Success() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(usuario.getPassword())).thenReturn("encryptedPassword");
        when(jwtHelper.generateToken(usuario)).thenReturn("fake-jwt-token");

        ApiResponse response = usuarioService.createUser(usuario);

        assertEquals("00", response.getCodigo());
        assertEquals("Operación exitosa", response.getMensaje());
    }

    @Test
    public void testCreateUser_DatabaseError() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenThrow(new RuntimeException("DB failed"));

        ApiResponse response = usuarioService.createUser(usuario);

        assertEquals("01", response.getCodigo());
        assertEquals("Error en la base de datos: DB failed", response.getMensaje());
        assertEquals(null, response.getData());
    }

    // TESTS para getUserByEmail() ----------------------------------------------------

    @Test
    public void testGetUserByEmail_UserFound() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        ApiResponse response = usuarioService.getUserByEmail(usuario.getEmail());

        assertEquals("00", response.getCodigo());
        assertEquals("Operación exitosa", response.getMensaje());
    }

    @Test
    public void testGetUserByEmail_UserNotFound() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.empty());

        ApiResponse response = usuarioService.getUserByEmail(usuario.getEmail());

        assertEquals("03", response.getCodigo());
        assertEquals("Usuario no encontrado", response.getMensaje());
        assertEquals(null, response.getData());
    }

    @Test
    public void testGetUserByEmail_DatabaseError() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenThrow(new RuntimeException("DB failed"));

        ApiResponse response = usuarioService.getUserByEmail(usuario.getEmail());

        assertEquals("01", response.getCodigo());
        assertEquals("Error en la base de datos: DB failed", response.getMensaje());
        assertEquals(null, response.getData());
    }

    // TEST para updateUser() ----------------------------------------------------

    @Test
    public void testUpdateUser_Success() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode(usuarioActualizado.getPassword())).thenReturn("hashedPassword");

        ApiResponse response = usuarioService.updateUser(usuario.getEmail(), usuarioActualizado);

        assertEquals("00", response.getCodigo());
        assertEquals("Usuario actualizado correctamente", response.getMensaje());
        Usuario updatedUser = (Usuario) response.getData();
        assertEquals("Fernando Torres", updatedUser.getNombre());
        assertEquals("hashedPassword", updatedUser.getPassword());
        assertEquals(1, updatedUser.getTelefonos().size());
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.empty());

        ApiResponse response = usuarioService.updateUser(usuario.getEmail(), usuarioActualizado);

        assertEquals("03", response.getCodigo());
        assertEquals("Usuario no encontrado", response.getMensaje());
        assertEquals(null, response.getData());
    }

    @Test
    public void testUpdateUser_DatabaseError() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenThrow(new RuntimeException("DB error"));

        ApiResponse response = usuarioService.updateUser(usuario.getEmail(), usuarioActualizado);

        assertEquals("01", response.getCodigo());
        assertEquals("Error en la base de datos: DB error", response.getMensaje());
        assertEquals(null, response.getData());
    }

    @Test
    public void testUpdateUser_NoPhoneUpdate() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        usuarioActualizado.setTelefonos(null);

        ApiResponse response = usuarioService.updateUser(usuario.getEmail(), usuarioActualizado);

        assertEquals("00", response.getCodigo());
        assertEquals("Usuario actualizado correctamente", response.getMensaje());
        Usuario updatedUser = (Usuario) response.getData();
        assertEquals(2, updatedUser.getTelefonos().size());
    }

    @Test
    public void testUpdateUser_NoPasswordUpdate() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        usuarioActualizado.setPassword(null);

        ApiResponse response = usuarioService.updateUser(usuario.getEmail(), usuarioActualizado);

        assertEquals("00", response.getCodigo());
        assertEquals("Usuario actualizado correctamente", response.getMensaje());

        Usuario updatedUser = (Usuario) response.getData();

        assertEquals(usuario.getPassword(), updatedUser.getPassword());
    }

    // TEST para deleteUser() ----------------------------------------------------

    @Test
    public void testDeleteUser_Success() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).delete(usuario);

        ApiResponse response = usuarioService.deleteUser(usuario.getEmail());

        assertEquals("00", response.getCodigo());
        assertEquals("Usuario eliminado correctamente", response.getMensaje());
        assertEquals(null, response.getData());

        verify(usuarioRepository, times(1)).delete(usuario);
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.empty());

        ApiResponse response = usuarioService.deleteUser(usuario.getEmail());

        assertEquals("03", response.getCodigo());
        assertEquals("Usuario no encontrado", response.getMensaje());
        assertEquals(null, response.getData());

        verify(usuarioRepository, never()).delete(any());
    }

    @Test
    public void testDeleteUser_DatabaseError() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenThrow(new RuntimeException("DB error"));

        ApiResponse response = usuarioService.deleteUser(usuario.getEmail());

        assertEquals("01", response.getCodigo());
        assertEquals("Error en la base de datos: DB error", response.getMensaje());
        assertEquals(null, response.getData());

        verify(usuarioRepository, never()).delete(any());
    }

    // TEST para loginUser() ----------------------------------------------------

    @Test
    public void testLoginUser_Success() {
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(validPassword, usuario.getPassword())).thenReturn(true);
        when(jwtHelper.generateToken(usuario)).thenReturn("fake-jwt-token");

        ApiResponse response = usuarioService.loginUser(email, validPassword);

        assertEquals("00", response.getCodigo());
        assertEquals("Validación de token completada", response.getMensaje());

        verify(usuarioRepository, times(1)).save(usuario); // Verificar actualización del último login
    }

    @Test
    public void testLoginUser_UserNotFound() {
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        ApiResponse response = usuarioService.loginUser(email, validPassword);

        assertEquals("03", response.getCodigo());
        assertEquals("Usuario no encontrado", response.getMensaje());
        assertEquals(null, response.getData());

        verify(usuarioRepository, never()).save(any()); // No debe actualizar el usuario
    }

    @Test
    public void testLoginUser_InvalidPassword() {
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(invalidPassword, usuario.getPassword())).thenReturn(false);

        ApiResponse response = usuarioService.loginUser(email, invalidPassword);

        assertEquals("02", response.getCodigo());
        assertEquals("Credenciales inválidas", response.getMensaje());
        assertEquals(null, response.getData());

        verify(usuarioRepository, never()).save(any()); // No debe actualizar el usuario
    }

    @Test
    public void testLoginUser_DatabaseError() {
        when(usuarioRepository.findByEmail(email)).thenThrow(new RuntimeException("DB error"));

        ApiResponse response = usuarioService.loginUser(email, validPassword);

        assertEquals("01", response.getCodigo());
        assertEquals("Error en la base de datos: DB error", response.getMensaje());
        assertEquals(null, response.getData());

        verify(usuarioRepository, never()).save(any()); // No debe actualizar el usuario
    }

    // TEST para validateToken() ----------------------------------------------------

    @Test
    public void testValidateToken_Success() {
        when(jwtHelper.validarToken(validToken, email)).thenReturn(true);

        ApiResponse response = usuarioService.validateToken(validToken, email);

        assertEquals("00", response.getCodigo());
        assertEquals("Validación de token completada", response.getMensaje());
    }

    @Test
    public void testValidateToken_InvalidToken() {
        when(jwtHelper.validarToken(invalidToken, email)).thenReturn(false);

        ApiResponse response = usuarioService.validateToken(invalidToken, email);

        assertEquals("00", response.getCodigo());
        assertEquals("Validación de token completada", response.getMensaje());
    }

    @Test
    public void testValidateToken_DatabaseError() {
        when(jwtHelper.validarToken(validToken, email)).thenThrow(new RuntimeException("DB error"));

        ApiResponse response = usuarioService.validateToken(validToken, email);

        assertEquals("01", response.getCodigo());
        assertEquals("Error en la base de datos: DB error", response.getMensaje());
        assertEquals(null, response.getData());
    }
}
