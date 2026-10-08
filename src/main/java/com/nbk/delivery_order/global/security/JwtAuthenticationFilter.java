package com.nbk.delivery_order.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// @Component로 등록하면 서블릿 필터로도 한 번 더 등록되므로 SecurityConfig에서 직접 생성
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 토큰이 없거나 유효하지 않으면 인증 없이 통과 → 인증이 필요한 API는 401
        if (header != null && header.startsWith(BEARER_PREFIX)) {
            jwtProvider.parse(header.substring(BEARER_PREFIX.length()))
                    .ifPresent(authUser -> SecurityContextHolder.getContext().setAuthentication(
                            UsernamePasswordAuthenticationToken.authenticated(
                                    authUser,
                                    null,
                                    List.of(new SimpleGrantedAuthority("ROLE_" + authUser.role().name()))
                            )
                    ));
        }

        filterChain.doFilter(request, response);
    }
}
