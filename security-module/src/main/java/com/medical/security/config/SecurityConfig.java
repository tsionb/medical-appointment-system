package com.medical.security.config;

import com.medical.security.jwt.JwtAuthFilter;
import com.medical.security.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter,
                          UserDetailsServiceImpl userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http

            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/api/auth/**").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/departments/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/doctors/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/medications/**").permitAll()


                .requestMatchers(HttpMethod.POST, "/api/departments/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/departments/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/departments/**")
                    .hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST, "/api/medications/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/medications/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/medications/**")
                    .hasRole("ADMIN")

                .requestMatchers("/api/notifications/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.GET, "/api/patients").hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST, "/api/doctors/*/schedules")
                    .hasRole("DOCTOR")
                .requestMatchers(HttpMethod.DELETE, "/api/doctors/*/schedules/*")
                    .hasRole("DOCTOR")

                .requestMatchers(HttpMethod.POST, "/api/medical-records/**")
                    .hasAnyRole("DOCTOR", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/medical-records/**")
                    .hasAnyRole("DOCTOR", "ADMIN")

                .requestMatchers(HttpMethod.POST, "/api/appointments")
                    .hasRole("PATIENT")
                .requestMatchers("/api/waitlist/**").hasRole("PATIENT")

                .requestMatchers(HttpMethod.POST, "/api/appointments/*/reviews")
                    .hasRole("PATIENT")

                .anyRequest().authenticated()
            )


            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )


            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

 
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}