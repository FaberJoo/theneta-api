package site.theneta.api.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.theneta.api.domain.member.entity.Profile;

import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {

  boolean existsByName(String name);
}