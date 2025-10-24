package org.example.userservice.services.helper.helperServices;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private  String JWT ;
    public String extractUserName(String token) {
        return extractClaim(token,Claims::getSubject);
    }

    public  String generateToken(Map<String, Object> claims,   UserDetails userDetails, long expiration ) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith( SignatureAlgorithm.HS256,getSignInKey())
                .compact();
                    }
    public  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    public  Claims extractClaims(String token) {
        return Jwts.parserBuilder().
                setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private  Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Map<String, String> extractTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        Map<String, String> tokens = new HashMap<>();
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("access_token")) {
                tokens.put(cookie.getName(), cookie.getValue());
            }
            if (cookie.getName().equals("refresh_token")) {
                tokens.put(cookie.getName(), cookie.getValue());
            }
        }
        return tokens;
    }
}
