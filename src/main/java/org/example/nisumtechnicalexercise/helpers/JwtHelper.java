package org.example.nisumtechnicalexercise.helpers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
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

    @Value("${secret.key}")
    private String secretKeyTemp;

    private static Key SECRET_KEY;

    @PostConstruct
    public void init() {
        SECRET_KEY = Keys.hmacShaKeyFor(secretKeyTemp.getBytes(StandardCharsets.UTF_8));
    }

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
