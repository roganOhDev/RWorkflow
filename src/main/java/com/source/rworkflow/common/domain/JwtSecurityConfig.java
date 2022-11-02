//package com.source.rworkflow.common.domain;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.DefaultSecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//
//@Configuration
//@EnableWebMvc
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class JwtSecurityConfig {
//
//    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    @Bean
//    protected DefaultSecurityFilterChain configure(HttpSecurity http) throws Exception {
//
//        return http.cors().disable()
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/**").permitAll()
//                .and()
//                .formLogin().disable()
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .exceptionHandling()
//                .authenticationEntryPoint(((request, response, authException) -> {
//                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
//                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//                }))
//                .and().build();
//    }
//
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//}
