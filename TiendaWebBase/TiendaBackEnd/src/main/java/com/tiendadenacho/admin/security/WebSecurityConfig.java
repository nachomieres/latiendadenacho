package com.tiendadenacho.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	public UserDetailsService userDetailsService () {
		return new TiendaUserDetailsService();
	}
	
	@Bean
	public PasswordEncoder PasswordEncoder () {
		return new BCryptPasswordEncoder();
	}
	
	public DaoAuthenticationProvider authenticationProvider () {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(PasswordEncoder());
		
		return authProvider;
	}
	
	

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/states/list_by_country/**")
				.hasAnyAuthority("Admin", "Gestor")
				
			.antMatchers("/users/**", "/settings/**", "/countries/**", "/states/**")
				.hasAuthority("Admin")
				
			.antMatchers("/categories/**", "/brands/**")
				.hasAnyAuthority("Admin", "Editor")
				
			.antMatchers("/products/new", "/products/delete/**")
				.hasAnyAuthority("Admin", "Editor")		
				
			.antMatchers("/products/edit/**", "/products/save", "/products/check_unique")
				.hasAnyAuthority("Admin", "Editor", "Gestor")			
				
			.antMatchers("/products", "/products/", "/products/detail/**", "/products/page/**")
				.hasAnyAuthority("Admin", "Editor", "Gestor", "Expedidor")	
				
			.antMatchers("/products/**")
				.hasAnyAuthority("Admin", "Editor")		
				
			.antMatchers("/orders", "/orders/", "/orders/page/**", "/orders/detail/**")
					.hasAnyAuthority("Admin", "Gestor", "Expedidor")
				
			.antMatchers("/customers/**", "/orders/**", "/get_shipping_cost")
				.hasAnyAuthority("Admin", "Gestor")
				
			.antMatchers("/orders_shipper/update/**")
				.hasAuthority("Expedidor")
				
			.anyRequest().authenticated()
			.and()
			.formLogin()
				.loginPage("/login")
				.usernameParameter("email")
				.permitAll()
			.and().logout().permitAll()
			.and().rememberMe().key("MiClAvEsEcReTa2021");
		http.headers().frameOptions().sameOrigin();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**", "/*.css");
	}

}
