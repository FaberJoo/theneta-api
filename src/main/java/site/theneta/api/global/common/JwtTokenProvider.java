package site.theneta.api.global.common;

import java.util.Date;
import java.util.Map;

public interface JwtTokenProvider {

  String generateAccessToken(Map<String, Object> claims);

  String generateRefreshToken();

  Map<String, Object> getClaimsFromToken(String token);

  boolean validateToken(String token);

  Date generateTokenExpiry(long expiry);
}
