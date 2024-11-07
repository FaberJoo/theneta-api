package site.theneta.api.domain.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import site.theneta.api.global.constant.Role;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private Role role;

  @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  private AuthLocal authLocal;

  @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  private Profile profile;

  @Column(nullable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(nullable = false)
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @Builder
  public Member(String username) {
    this.username = username;
    this.role = Role.GUEST;
  }

  public void verifyEmail() {
    this.role = Role.MEMBER;
  }

  public void addAuthLocal(AuthLocal authLocal) {
    this.authLocal = authLocal;
  }

  public void addProfile(Profile profile) {
    this.profile = profile;
  }
}