package com.nbk.delivery_order.global.config;

import com.nbk.delivery_order.global.security.JwtAuthenticationFilter;
import com.nbk.delivery_order.global.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtProvider jwtProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 토큰이 없거나 유효하지 않으면 401, 역할이 맞지 않으면 403
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .authorizeHttpRequests(auth -> auth
                        // 누구나
                        .requestMatchers(HttpMethod.POST, "/api/members", "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/menus", "/api/menus/*").permitAll()
                        .requestMatchers("/error").permitAll()
                        // 사장님만
                        .requestMatchers(HttpMethod.POST, "/api/menus").hasRole("OWNER")
                        .requestMatchers(HttpMethod.PATCH, "/api/menus/*", "/api/orders/*/status").hasRole("OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/api/menus/*").hasRole("OWNER")
                        // 고객만
                        .requestMatchers(HttpMethod.POST, "/api/orders", "/api/payments").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.PATCH, "/api/orders/*/cancel", "/api/payments/*/cancel").hasRole("CUSTOMER")
                        // 그 외(회원 조회, 주문·결제 조회)는 로그인한 사용자
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
