package com.synclyplatform.synclyprojectbackend.config;

import com.synclyplatform.synclyprojectbackend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests.requestMatchers(
                                "/api/v1/authentication/**",
                                "/ws/**",
                                "/api/v1/generate-data/**",
                                "/api/v1/tags/popular",
                                "/api/v1/tags/trending",
                                "/api/v1/friends/android-app/**",
                                "/api/v1/friends/accept/request/**",
                                "/api/v1/friends/decline/request/**",
                                "/api/v1/friends/remove/friend/**",
                                "/api/v1/users-profiles/android-app/**",
                                "/api/v1/users/search/**",
                                "/api/v1/posts/search/**",
                                "/api/v1/tags/search/**",
                                "/api/v1/follows/**",
                                "/api/v1/posts/android-app/**",
                                "/api/v1/likes/android-app/**",
                                "/api/v1/likes/user/liked-profiles/**",
                                "/api/v1/shared-posts/android-app/**",
                                "/api/v1/tags/android-app/**",
                                "/api/v1/comments/**",
                                "/api/v1/post-collections/all-by-user/**",
                                "/api/v1/post-collections/android-app/**",
                                "/api/v1/post-collections/save-post/**",
                                "/api/v1/post-collections/unsave-post/by-post-collection/**",
                                "/api/v1/media/**"
                        ).permitAll().anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
