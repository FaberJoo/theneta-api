package site.theneta.api.domain.member.operator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import site.theneta.api.domain.member.repository.ProfileRepository;

@Component
@AllArgsConstructor
public class ProfileSelector {
    private final ProfileRepository profileRepository;

    public boolean existsByName(String name) {
        return profileRepository.existsByName(name);
    }
}
