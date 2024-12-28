package com.ibat.myblog.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

import com.ibat.myblog.Model.TokenValidationResult;

@Component
public class TokenValidator {
    
    @Value("${jwt.secret}")
    private String jwtSecret;

    private SecretKey getSigningKey() {
       
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenValidationResult validateToken(String token) {
        TokenValidationResult result = new TokenValidationResult();
        
        try {
            // 使用安全的密钥
            SecretKey key = getSigningKey();
            
            // 解析JWT token
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            // 检查是否过期
            if (claims.getExpiration().before(new Date())) {
                result.setValid(false);
                result.setError("Token has expired");
                return result;
            }
            
            // token有效
            result.setValid(true);
            result.setUsername(claims.getSubject());
            result.setExpirationDate(claims.getExpiration());
            result.setIssuedAt(claims.getIssuedAt());
            
        } catch (ExpiredJwtException e) {
            result.setValid(false);
            result.setError("Token expired");
        } catch (SignatureException e) {
            result.setValid(false);
            result.setError("Invalid token signature");
        } catch (MalformedJwtException e) {
            result.setValid(false);
            result.setError("Malformed token");
        } catch (UnsupportedJwtException e) {
            result.setValid(false);
            result.setError("Unsupported token");
        } catch (IllegalArgumentException e) {
            result.setValid(false);
            result.setError("Token claims string is empty");
        }
        
        return result;
    }
}
