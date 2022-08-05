package me.marnic.jrd.jrd.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


/**
 * Copyright (c) 30.07.2022
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((a) -> {
            try {
                a.antMatchers("/login/**").permitAll().anyRequest().authenticated();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        http.userDetailsService(userDetailsService()).formLogin().loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/monitors", true).failureUrl("/login?error").permitAll().and().csrf().disable().logout().logoutSuccessUrl("/login").permitAll().and()
                .sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(true);

        return http.build();
    }

    /**
     *
     * One test user with (username: user and password: password) is currently added
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();


        return new InMemoryUserDetailsManager(user);
    }
}
