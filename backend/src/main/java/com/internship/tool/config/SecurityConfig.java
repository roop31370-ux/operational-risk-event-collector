package com.internship.tool.config;

import com.internship.tool.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // ❌ Disable CSRF for REST
            .csrf(csrf -> csrf.disable())

            // 🔥 Stateless (JWT)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 🔐 Authorization rules
            .authorizeHttpRequests(auth -> auth

                // 🌐 PUBLIC (no token required)
                .requestMatchers(
                        "/api/auth/**",
                        "/h2-console/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                ).permitAll()

                // 🟢 READ APIs → USER + ADMIN
                .requestMatchers(HttpMethod.GET, "/api/risk-events/**")
                .hasAnyRole("USER", "ADMIN")

                // 🔴 CREATE → ADMIN ONLY
                .requestMatchers(HttpMethod.POST, "/api/risk-events")
                .hasRole("ADMIN")

                // 🔴 SEARCH → USER + ADMIN (POST /search)
                .requestMatchers(HttpMethod.POST, "/api/risk-events/search")
                .hasAnyRole("USER", "ADMIN")

                // 🔴 UPDATE → ADMIN ONLY
                .requestMatchers(HttpMethod.PUT, "/api/risk-events/**")
                .hasRole("ADMIN")

                // 🔴 DELETE → ADMIN ONLY
                .requestMatchers(HttpMethod.DELETE, "/api/risk-events/**")
                .hasRole("ADMIN")

                // 🔐 everything else requires auth
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