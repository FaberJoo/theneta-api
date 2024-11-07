package site.theneta.api.apiRest.v1.auth.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.theneta.api.apiRest.v1.auth.controller.request.LoginRequest;
import site.theneta.api.apiRest.v1.auth.controller.response.TokenResponse;
import site.theneta.api.apiRest.v1.auth.service.AuthService;
import site.theneta.api.apiRest.v1.auth.controller.request.SignupRequest;

@Slf4j
@Tag(name = "Auth API", description = "인증 API")
@RestController
@RequestMapping("v1/members")
@Validated
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("/signUp")
  public ResponseEntity<?> signUp(
      @RequestBody @Valid SignupRequest req
  ) {
    authService.signup(req);

    return ResponseEntity.created(URI.create("/")).build();
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
    TokenResponse token = authService.login(request);
    return ResponseEntity.ok(token);
  }
}