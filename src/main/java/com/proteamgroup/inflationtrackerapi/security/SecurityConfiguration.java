package com.proteamgroup.inflationtrackerapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration{
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) 
	  throws Exception {
	    return http.getSharedObject(AuthenticationManagerBuilder.class)
	      .userDetailsService(userDetailsService)
	      .passwordEncoder(passwordEncoder)
	      .and()
	      .build();
	}
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
     
        http
        
        /* Login configuration */
        .formLogin()
        	.loginPage("/login")
        	.defaultSuccessUrl("/dashboard") // user's home page, it can be any URL
        	.permitAll() // Anyone can go to the login page
        /* Logout configuration */
        .and()
        	.logout()
        	.logoutSuccessUrl("/index")
        	.permitAll()
        .and()
        	.authorizeHttpRequests()
        	/* Anonymous directories to reach static files*/
        	/* Needs to double checked to see if this cause any security problems or bugs */
        	.requestMatchers("/","/css/**", "/js/**", "/images/**","/plugins/**").permitAll()
        	/* Anonymous pages */
        	.requestMatchers("/welcome","/index","/register").permitAll()
        	/* Restricted pages only registered users */
        	.requestMatchers("/dashboard").hasAnyRole("USER")
        	/* Only for admins */
        	.requestMatchers("/adminpanel" ).hasRole("ADMIN")
        	.anyRequest()
        .authenticated();
 
        return http.build();
    }
}
