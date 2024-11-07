package site.theneta.api.apiRest.v1.auth.service;

import java.util.Map;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import site.theneta.api.apiRest.v1.auth.controller.request.LoginRequest;
import site.theneta.api.apiRest.v1.auth.controller.response.TokenResponse;
import site.theneta.api.apiRest.v1.auth.controller.request.SignupRequest;
import site.theneta.api.domain.member.entity.AuthLocal;
import site.theneta.api.domain.member.entity.Member;
import site.theneta.api.domain.member.entity.Profile;
import site.theneta.api.domain.member.operator.*;
import site.theneta.api.global.common.JwtTokenProviderImpl;
import site.theneta.api.global.error.ErrorCode;
import site.theneta.api.global.error.exception.BusinessBaseException;
import site.theneta.api.global.utils.either.Either;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
  private final MemberSelector memberSelector;
  private final MemberCreator memberCreator;
  private final AuthLocalSelector authLocalSelector;
  private final AuthLocalCreator authLocalCreator;
  private final ProfileCreator profileCreator;
  private final ProfileSelector profileSelector;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProviderImpl jwtTokenProviderImpl;

  public void signup(SignupRequest request) {
    // 이메일 중복 검사
    if (authLocalSelector.existsByEmail(request.email())) {
      throw new BusinessBaseException(ErrorCode.CONFLICT, null, "email");
    }

    // 사용자명 중복 검사
    if (memberSelector.existsByUsername(request.username())) {
      throw new BusinessBaseException(ErrorCode.CONFLICT, null, "username");
    }

    // 이름 중복 검사
    if (profileSelector.existsByName(request.name())) {
      throw new BusinessBaseException(ErrorCode.CONFLICT, null, "name");
    }

    // Member 생성
    Member member = memberCreator.create(request.username());

    // AuthLocal 생성
    AuthLocal authLocal = authLocalCreator.create(
        member,
        request.email(),
        passwordEncoder.encode(request.password()));

    // Profile 생성
    Profile profile = profileCreator.create(
        member,
        request.name());

    member.addAuthLocal(authLocal);
    member.addProfile(profile);
  }

  public TokenResponse login(LoginRequest request) {
    // 이메일로 회원 조회
    Either<BusinessBaseException, AuthLocal> result = authLocalSelector.findByEmail(request.email());
    if (result.isLeft()) {
      throw result.getLeft().get();
    }

    AuthLocal authLocal = result.getRight().get();

    // 비밀번호 검증
    if (!passwordEncoder.matches(request.password(), authLocal.getPassword())) {
      throw new BusinessBaseException(ErrorCode.INVALID_CERTIFICATION);
    }

    Member member = authLocal.getMember();

    Map<String, Object> claims = Map.of(
        "id", member.getId(),
        "username", member.getUsername(),
        "role", member.getRole()
    );

    // 토큰 생성
    String accessToken = jwtTokenProviderImpl.generateAccessToken(claims);
    String refreshToken = jwtTokenProviderImpl.generateRefreshToken();

    return new TokenResponse(
        accessToken,
        refreshToken,
        "Bearer",
        jwtTokenProviderImpl.getAcessTokenExpiry()
    );
  }
}