package site.theneta.api.domain.member.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import site.theneta.api.domain.member.entity.AuthLocal;

import java.util.UUID;

public interface AuthLocalRepository extends JpaRepository<AuthLocal, UUID> {
  boolean existsByEmail(String email);

  Optional<AuthLocal> findByEmail(String email);
}
