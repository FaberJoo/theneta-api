package site.theneta.api.apiMvc.v1.auth.controller.response;

public record TokenResponse(
    String accessToken,
    String refreshToken,
    String tokenType,
    long expiresIn
) {}
