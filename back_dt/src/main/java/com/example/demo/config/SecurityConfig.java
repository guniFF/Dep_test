package com.example.demo.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@AllArgsConstructor
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    // BCryptPasswordEncoder 빈을 생성하여 암호화 관련 기능을 제공
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // CORS 설정을 위한 CorsConfigurationSource 빈 추가
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // HTTP 보안 구성을 위한 SecurityFilterChain 빈 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // CSRF(Cross-Site Request Forgery) 방어 기능 비활성화
                .csrf(CsrfConfigurer<HttpSecurity>::disable)

                // CORS 설정 추가
                .cors(withDefaults())

                /**
                 * X-Frame-Options 헤더 설정을 SAMEORIGIN으로 지정하여 Clickjacking 공격 방지
                 * Clickjacking : 사용자가 의도하지 않은 클릭 동작을 유도하여 악의적인 행위를 하도록 만드는 공격 기법.
                 */
                .headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin))

                // HTTP 요청에 대한 접근 제한 및 권한 설정
                .authorizeHttpRequests(requests ->
                        requests
                                // 특정 API 경로 및 스웨거 관련 리소스에 대한 요청은 모두 허용
                                .requestMatchers(
                                        "/api/v1/**/auth/**", // 인증 관련 API 경로
                                        "/api/v1/**/any/**",  // 일반 API 경로
                                        "/swagger-resources/**", // 스웨거 리소스
                                        "/configuration/ui",    // 스웨거 UI 설정
                                        "/configuration/security", // 스웨거 보안 설정
                                        "/swagger-ui/**",       // 스웨거 UI 페이지
                                        "/webjars/**",          // 웹 자원(JAR 파일)
                                        "/v3/api-docs/**"       // 스웨거 3.0 이상 API 문서 엔드포인트
                                ).permitAll() // 모든 사용자에게 허용

                                // H2 데이터베이스 콘솔 접근을 모두에게 허용
                                .requestMatchers(PathRequest.toH2Console()).permitAll()

                                // 그 외의 모든 요청은 인증 필요
                                .anyRequest().authenticated()
                )

                // 세션 관리 설정: STATELESS로 설정하여 세션을 사용하지 않음
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .build(); // 보안 설정을 빌드하여 SecurityFilterChain 반환
    }
}
