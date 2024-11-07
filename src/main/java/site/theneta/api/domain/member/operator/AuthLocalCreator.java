package site.theneta.api.domain.member.operator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import site.theneta.api.domain.member.entity.AuthLocal;
import site.theneta.api.domain.member.entity.Member;
import site.theneta.api.domain.member.repository.AuthLocalRepository;

@Component
@AllArgsConstructor
public class AuthLocalCreator {
  private final AuthLocalRepository authLocalRepository;

  public AuthLocal create(Member member, String email, String encodedPassword) {
    AuthLocal authLocal = AuthLocal.builder()
        .member(member)
        .email(email)
        .password(encodedPassword)
        .build();
    return authLocalRepository.save(authLocal);
  }
}
