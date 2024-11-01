package site.theneta.api.apiRest.v1.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import site.theneta.api.apiRest.v1.member.controller.request.SignupRequest;
import site.theneta.api.domain.member.entity.AuthLocal;
import site.theneta.api.domain.member.entity.Member;
import site.theneta.api.domain.member.entity.Profile;
import site.theneta.api.domain.member.operator.*;
import site.theneta.api.global.error.ErrorCode;
import site.theneta.api.global.error.exception.BusinessBaseException;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
  private final MemberSelector memberSelector;
  private final MemberCreator memberCreator;
  private final AuthLocalSelector authLocalSelector;
  private final AuthLocalCreator authLocalCreator;
  private final ProfileCreator profileCreator;
  private final ProfileSelector profileSelector;
  private final PasswordEncoder passwordEncoder;

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
}