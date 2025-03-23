package com.ppg.ems_server_side_v0.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    public static final String  SECRET_KEY = "5d6de10accaf42fc99b75e1cc10522f7403a2bb4391123aa5a7ffcdecb499709";

    public String extractUsername(String jwt) {
        return extractClaim(jwt,Claims::getSubject );
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(),userDetails);
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver)
    {
        final Claims claims = extactAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(Map<String,Object> extraClaims, UserDetails userDetails)
    {
        return Jwts.builder().
                setClaims(extraClaims).
                setSubject(userDetails.getUsername()).
                setIssuedAt(new Date(System.currentTimeMillis())).
                setExpiration(new Date(System.currentTimeMillis()+ 1000*60*24)).signWith(getSigninKey(), SignatureAlgorithm.HS256).compact();
    }

    Claims extactAllClaims(String jwt) {
        return Jwts.parserBuilder().setSigningKey(getSigninKey()).build().parseClaimsJws(jwt).getBody();
    }

    private Key getSigninKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token,UserDetails userDetails)
    {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }
}
