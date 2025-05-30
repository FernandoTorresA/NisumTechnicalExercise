package org.example.nisumtechnicalexercise.helper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.nisumtechnicalexercise.entities.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtHelper {

    @Value("${jwt.expiration.time}")
    protected Long jwtExpirationTime;

    //cambiar a property
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor("NisumTechnicalExerciseWithALotOfCharacteresTOImproveSecurity!".getBytes(StandardCharsets.UTF_8));

    public String generateToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean validarToken(String token, String email) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody();

            String emailEnToken = claims.getSubject();
            Date fechaExpiracion = claims.getExpiration();

            return email.equals(emailEnToken) && fechaExpiracion.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

}
