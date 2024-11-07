package site.theneta.api.domain.member.operator;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import site.theneta.api.domain.member.entity.AuthLocal;
import site.theneta.api.domain.member.repository.AuthLocalRepository;
import site.theneta.api.global.error.ErrorCode;
import site.theneta.api.global.error.exception.BusinessBaseException;
import site.theneta.api.global.utils.either.Either;

@Component
@AllArgsConstructor
public class AuthLocalSelector {

  private final AuthLocalRepository authLocalRepository;

  public boolean existsByEmail(String email) {
    return authLocalRepository.existsByEmail(email);
  }

  public Either<BusinessBaseException, AuthLocal> findByEmail(String email) {
    Optional<AuthLocal> authLocal = authLocalRepository.findByEmail(email);
    if (authLocal.isPresent()) {
      return Either.right(authLocal.get());
    } else {
      return Either.left(
          new BusinessBaseException(ErrorCode.INVALID_CERTIFICATION));
    }
  }
}
