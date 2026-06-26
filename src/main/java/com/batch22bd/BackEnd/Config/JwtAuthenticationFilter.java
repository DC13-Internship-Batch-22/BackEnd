package com.batch22bd.BackEnd.Config;

import com.batch22bd.BackEnd.Exception.JwtHandle.JwtAuthenticationEntryPoint;
import com.batch22bd.BackEnd.Service.CustomUserDetailsService;
import com.batch22bd.BackEnd.Service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader =
                request.getHeader("Authorization");

        if (authHeader == null
                || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {

            if (!jwtService.isValid(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            String username = jwtService.extractUsername(token);

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (ExpiredJwtException e) {

            jwtAuthenticationEntryPoint.commence(
                    request,
                    response,
                    new InsufficientAuthenticationException("Token expired", e)
            );
            return;

        } catch (JwtException e) {

            jwtAuthenticationEntryPoint.commence(
                    request,
                    response,
                    new InsufficientAuthenticationException("Invalid token", e)
            );
            return;

        } catch (UsernameNotFoundException e) {

            jwtAuthenticationEntryPoint.commence(
                    request,
                    response,
                    new InsufficientAuthenticationException("User not found", e)
            );
            return;
        }

        filterChain.doFilter(request, response);
    }
}