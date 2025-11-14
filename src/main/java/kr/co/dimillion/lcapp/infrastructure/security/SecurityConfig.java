package kr.co.dimillion.lcapp.infrastructure.security;

import kr.co.dimillion.lcapp.application.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration(proxyBeanMethods = false)
public class SecurityConfig {
    @Bean
    UserDetailsService userDetailsService() {
        UserDetails worker = User.withDefaultPasswordEncoder()
                .username("worker")
                .password("1234")
                .roles(Role.WORKER.name())
                .build();

        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("1234")
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
                        .anyRequest().authenticated())
                .formLogin(login -> login
                        .loginPage("/login").permitAll()
                        .loginProcessingUrl("/login").permitAll()
                        .successHandler(new RoleBasedAuthenticationSuccessHandler()));
        return http.build();
    }
}
