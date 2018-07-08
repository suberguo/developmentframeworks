package com.dds.ssjh.security;

import java.util.Arrays;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;


@EnableWebSecurity
public class DdsSecurityConfigurer extends WebSecurityConfigurerAdapter {

	@Autowired
	private DdsAuthenticationProvider authenticationProvider;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
		.antMatchers("/login.xhtml").permitAll()
		.antMatchers("/javax.faces.resource/**").permitAll()
		.antMatchers("/resources/**").permitAll()
		.anyRequest().permitAll()
		.and().formLogin().usernameParameter("d_Username").passwordParameter("d_Password").loginProcessingUrl("/j_spring_security_check")
		.successForwardUrl("/views/mainpage.xhtml")
		.loginPage("/login.jsf").permitAll()
		.failureUrl("/login.xhtml?error=1")
		.and().logout().logoutSuccessUrl("/j_spring_security_logout").permitAll();
		http.csrf().disable();
	}

	@Bean
	public AuthenticationProvider createDaoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(new BCryptPasswordEncoder());
		return provider;
	}

	@Bean
	public AuthenticationManager createAuthenticationManager() {
		return new ProviderManager(Arrays.asList(authenticationProvider, createDaoAuthenticationProvider()));
	}
	
	private SavedRequestAwareAuthenticationSuccessHandler createSuccessHandler() {
		SavedRequestAwareAuthenticationSuccessHandler h = new SavedRequestAwareAuthenticationSuccessHandler();
		//String path = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext()).getContextPath();
		h.setDefaultTargetUrl("/views/mainpage.xhtml");///views/mainpage.xhtml
		//h.setAlwaysUseDefaultTargetUrl(true);
		return h;
	}

}
