package com.nbk.delivery_order.global.config;

import com.nbk.delivery_order.global.security.JwtAuthenticationFilter;
import com.nbk.delivery_order.global.security.JwtProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
public class SecurityConfig {
    private final JwtProvider jwtProvider;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public SecurityConfig(JwtProvider jwtProvider,
                          @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtProvider = jwtProvider;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

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
                // 필터에서 발생한 401·403도 GlobalExceptionHandler에서 같은 형식으로 응답하도록 위임
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, e) ->
                                handlerExceptionResolver.resolveException(request, response, null, e))
                        .accessDeniedHandler((request, response, e) ->
                                handlerExceptionResolver.resolveException(request, response, null, e)))
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
