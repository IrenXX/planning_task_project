package ru.kemova.task_planning.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.kemova.task_planning.config.security.PersonDetails;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtService {

    @Value("${jwt.token.secret}")
    private String jwtSecret;

    @Value("${jwt.token.lifetime}")
    private Duration jwtLifetime;

    public String generateToken(PersonDetails personDetails) {
        Map<String, Object> claims = new HashMap<>();

        if (personDetails instanceof UserDetails) {
            claims.put("name", personDetails.getPerson().getName());
            claims.put("email", personDetails.getUsername());
            claims.put("confirmed", personDetails.getPerson().isConfirmed());
            claims.put("roles", getRolesName(personDetails));
        }
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifetime.toMillis());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(personDetails.getUsername())
                .setIssuer("kemova")
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Получение ключа для подписи токена
     * @return ключ
     */
    private Key getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims getAllClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
        } catch (SignatureException sEx) {
            log.error("Invalid signature", sEx);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return claims;
    }

    //извлечение имени из токена
    public String getUsername(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public boolean isTokenValid(String token, PersonDetails personDetails) {

        try {
            final String username = getUsername(token);
            if (username.equals(personDetails.getUsername()) && !isTokenExpired(token)) {
                return true;
            }
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Jwt not valid -> {}", e.getMessage());
        }
        return false;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private List<String> getRolesName(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}
