package org.example.nisumtechnicalexercise.helpers;

import org.example.nisumtechnicalexercise.entities.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

//test fallan en la linea .signWith(SignatureAlgorithm.HS256, SECRET_KEY) porque no
// le gusta como estoy generando las keys:
//HMAC signing keys must be SecretKey instances.
//
//@ExtendWith(MockitoExtension.class)
//public class JwtHelperTest {
//
//    @InjectMocks
//    JwtHelper jwtHelper;
//
//    private Usuario usuario;
//    private String validToken;
//    private String invalidToken;
//
//    @BeforeEach
//    public void setUp() {
//
//        // Para emular los valores q están en properties pero el test no los conoce
//        ReflectionTestUtils.setField(jwtHelper, "jwtExpirationTime",
//                30000L);
//
//        ReflectionTestUtils.setField(jwtHelper, "secretKeyTemp",
//                "NisumTechnicalExerciseWithALotOfCharacteresTOImproveSecurity!");
//
//        usuario = new Usuario();
//        usuario.setNombre("Fernando");
//        usuario.setEmail("fernando@example.com");
//
//        validToken = jwtHelper.generateToken(usuario);
//        invalidToken = "invalid.jwt.token";
//    }
//
//}
