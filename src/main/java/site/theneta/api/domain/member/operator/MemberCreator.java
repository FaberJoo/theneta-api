package site.theneta.api.domain.member.operator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import site.theneta.api.domain.member.entity.Member;
import site.theneta.api.domain.member.repository.MemberRepository;

@Component
@AllArgsConstructor
public class MemberCreator {
    private final MemberRepository memberRepository;

    public Member create(String username) {
        Member member = Member.builder()
                .username(username)
                .build();
        return memberRepository.save(member);
    }
}
