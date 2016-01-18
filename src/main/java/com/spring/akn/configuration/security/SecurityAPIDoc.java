package com.spring.akn.configuration.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
@EnableWebSecurity
@Order(2)
public class SecurityAPIDoc extends WebSecurityConfigurerAdapter {
   
	@Autowired
	@Qualifier("CustomUserDetailService")
	private UserDetailsService userDetailsService;
	
	@Autowired
	@Qualifier(value="ajaxAuthenticationSuccessHandler")
	private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;
	
	@Autowired
	@Qualifier(value="ajaxAuthenticationFailureHandler")
	private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(userDetailsService)
		.passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers("/").hasRole("APIDOC");	
		http
		.formLogin()
		.permitAll()
		.loginPage("/login")
		.usernameParameter("username")
		.passwordParameter("password")
		.successHandler(ajaxAuthenticationSuccessHandler)
		.failureHandler(ajaxAuthenticationFailureHandler);
	http
		.sessionManagement()
		.sessionAuthenticationErrorUrl("/login")
		.maximumSessions(1)
		.expiredUrl("/login");
	http
		.logout()
		.logoutUrl("/logout")
		.logoutSuccessUrl("/login?logout")
		.invalidateHttpSession(true)
		.deleteCookies("JESSIONID")
		.permitAll();
	http.csrf().disable();
	http.exceptionHandling().accessDeniedPage("/accessDenied");	
	}
	

	
	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
}
