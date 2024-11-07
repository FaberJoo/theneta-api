package site.theneta.api.domain.member.operator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import site.theneta.api.domain.member.repository.MemberRepository;

@Component
@AllArgsConstructor
public class MemberSelector {
  private final MemberRepository memberRepository;

  public boolean existsByUsername(String username) {
    return memberRepository.existsByUsername(username);
  }
}
