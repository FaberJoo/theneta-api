package site.theneta.api.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http)
      throws Exception {
    // 헤더 보안 설정
    http
        .headers((headers) -> headers
            .contentTypeOptions(withDefaults())
            .xssProtection(withDefaults())
            .cacheControl(withDefaults())
            .httpStrictTransportSecurity(withDefaults())
            .frameOptions(withDefaults()));

    // csrf 비활성, cors 기본 설정
    http
        .csrf(AbstractHttpConfigurer::disable)
        .cors(withDefaults());

    // 세션 비활성
    // form 로그인 비활성, httpBasic 비활성
    http
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authorize -> authorize
            .anyRequest().permitAll());

    return http.build();
  }

}