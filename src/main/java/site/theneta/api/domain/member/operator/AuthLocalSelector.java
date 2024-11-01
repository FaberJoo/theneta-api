package site.theneta.api.domain.member.operator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import site.theneta.api.domain.member.repository.AuthLocalRepository;

@Component
@AllArgsConstructor
public class AuthLocalSelector {
    private final AuthLocalRepository authLocalRepository;

    public boolean existsByEmail(String email) {
        return authLocalRepository.existsByEmail(email);
    }
}
