package com.protifolio.taskkiller.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.protifolio.taskkiller.service.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

	@Autowired
	private JwtFilter jwtFilter;
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	private final String[] PUBLIC_ENDPOINTS = new String[] {"/register","/login","/h2/**"};
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	AuthenticationManager authenticationManagerBean(AuthenticationConfiguration config)throws Exception{
		return config.getAuthenticationManager();
	}
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
		http.csrf(csrf->csrf.disable())
		.headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.disable())
            )
		.authorizeHttpRequests(req->req
			.requestMatchers("/admin","/admin/**").hasAuthority("ADMIN")
			.requestMatchers("/me","/me/**").hasAnyAuthority("USER","ADMIN")
				.requestMatchers(PUBLIC_ENDPOINTS).permitAll()
		.anyRequest().authenticated())
		.rememberMe(remember->remember
				.key("123")
				.tokenValiditySeconds(604800)
				.userDetailsService(customUserDetailsService))
		.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
