
package site.theneta.api.global.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProviderImpl implements JwtTokenProvider {
  private SecretKey key = Jwts.SIG.HS256.key().build();

  @Value("${jwt.at-expiry}")
  private long atExpiry;

  @Value("${jwt.rt-expiry}")
  private long rtExpiry;

  @Override
  public String generateAccessToken(Map<String, Object> claims) {
    return Jwts.builder()
        .claims(claims)
        .issuedAt(new Date())
        .expiration(generateTokenExpiry(atExpiry))
        .signWith(key)
        .compact();
  }

  @Override
  public String generateRefreshToken() {
    return Jwts.builder()
        .issuedAt(new Date())
        .expiration(generateTokenExpiry(rtExpiry))
        .signWith(key)
        .compact();
  }

  @Override
  public Map<String, Object> getClaimsFromToken(String token) {
    Jws<Claims> claims = Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token);
    return claims.getPayload().entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  @Override
  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .decryptWith(key)
          .build()
          .parse(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      log.error("Invalid JWT token", e);
      return false;
    }
  }

  @Override
  public Date generateTokenExpiry(long expiry) {
    return new Date(System.currentTimeMillis() + atExpiry);
  }

  public long getAcessTokenExpiry() {
    return atExpiry;
  }
}