package kr.co.dimillion.lcapp.infrastructure.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.dimillion.lcapp.application.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Configuration(proxyBeanMethods = false)
public class SecurityConfig {
    @Bean
    UserDetailsService userDetailsService() {
        UserDetails worker = User.withDefaultPasswordEncoder()
                .username("worker")
                .password("worker")
                .roles(Role.WORKER.name())
                .build();

        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles(Role.ADMIN.name())
                .build();

        return new InMemoryUserDetailsManager(worker, admin);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/output.css").permitAll()
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/admin/**").hasRole(Role.ADMIN.name())
                        .anyRequest().hasRole(Role.WORKER.name()))
                .formLogin(login -> login
                        .loginPage("/login").permitAll()
                        .loginProcessingUrl("/login/proc")
                        .successHandler(new RoleBasedAuthenticationSuccessHandler()))
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((AccessDeniedHandler) (request, response, accessDeniedException) -> {
                            response.sendRedirect("/login?denied");
                        }));
        return http.build();
    }
}
