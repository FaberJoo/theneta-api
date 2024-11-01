package site.theneta.api.domain.member.operator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import site.theneta.api.domain.member.entity.Member;
import site.theneta.api.domain.member.entity.Profile;
import site.theneta.api.domain.member.repository.ProfileRepository;

@Component
@AllArgsConstructor
public class ProfileCreator {
    private final ProfileRepository profileRepository;

    public Profile create(Member member, String name) {
        Profile profile = Profile.builder()
                .member(member)
                .name(name)
                .build();
        return profileRepository.save(profile);
    }
}
