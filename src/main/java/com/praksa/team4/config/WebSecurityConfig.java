package com.praksa.team4.config;

import javax.crypto.SecretKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {
	
	private SecretKey secretKey;

	public WebSecurityConfig() {
		super();
		this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
	}

	@Bean
	public SecretKey secretKey() {
		return this.secretKey;
	}
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }	

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors().and()
				.csrf().disable()
				.addFilterAfter(new JWTAuthorizationFilter(secretKey), UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/cookbook/login").permitAll()
				.antMatchers(HttpMethod.GET, "/project/recipe").permitAll()
				.antMatchers(HttpMethod.GET, "/project/recipe/by_name").permitAll()
				.antMatchers(HttpMethod.GET, "/project/recipe/{id}").permitAll()
				.antMatchers(HttpMethod.GET, "/project/ingredients").permitAll()
				.antMatchers(HttpMethod.GET, "/project/ingredients/recipe/{id}").permitAll()
				.antMatchers(HttpMethod.GET, "/project/allergens").permitAll()
				.antMatchers(HttpMethod.GET, "/project/ingredients/id/{id}").permitAll()
				.antMatchers(HttpMethod.POST, "/project/regularuser").permitAll()
				.anyRequest().authenticated();
		return http.build();
	}
}