package com.internship.tool.config;

import com.internship.tool.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // 🔐 BCrypt Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // ❌ Disable CSRF
            .csrf(csrf -> csrf.disable())

            // 🔥 Stateless (JWT)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 🔐 Authorization rules (UPDATED 👇)
            .authorizeHttpRequests(auth -> auth

                // PUBLIC
                .requestMatchers(
                        "/api/auth/**",
                        "/h2-console/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                ).permitAll()

                // 🔴 ADMIN ONLY
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // 🟡 USER + ADMIN
                .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")

                // 🔐 EVERYTHING ELSE → LOGIN REQUIRED
                .anyRequest().authenticated()
            )

            // 🧱 H2 console fix
            .headers(headers ->
                headers.frameOptions(frame -> frame.disable())
            )

            // 🚫 Disable default login
            .httpBasic(httpBasic -> httpBasic.disable())
            .formLogin(form -> form.disable());

        // 🔐 JWT filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}