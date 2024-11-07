package site.theneta.api.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.theneta.api.domain.member.entity.Member;

import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
  boolean existsByUsername(String username);
}
