package com.springBoot.Project.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import com.springBoot.Project.Service.Userservice;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private Userservice userservice;
	@Bean
	public PasswordEncoder passwordencoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationprovider() {
		DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
		provider.setUserDetailsService(userservice);
		provider.setPasswordEncoder(passwordencoder());
		return provider;
	}
	
	@Bean
	public SecurityFilterChain filerchain(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeHttpRequests().requestMatchers("/register").permitAll().requestMatchers("/verify").permitAll().requestMatchers("/").permitAll().anyRequest().authenticated()
		.and().formLogin();
		return http.build();
	}
	
}
