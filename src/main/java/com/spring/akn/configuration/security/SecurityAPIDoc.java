package com.spring.akn.configuration.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@Order(2)
public class SecurityAPIDoc extends WebSecurityConfigurerAdapter {
	@Autowired
	DataSource dataSource;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("SELECT user_name, user_password, enabled FROM tbuser WHERE user_name=?")
				.authoritiesByUsernameQuery("SELECT tbrole.role_name,tbuser.user_name FROM tbrole INNER JOIN tbuser_role ON tbuser_role.role_id= tbrole.role_id INNER JOIN tbuser ON tbuser.user_id=tbuser_role.user_id WHERE tbuser.user_name=?");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/apidoc").hasRole("APIDOC");
		http.formLogin()
		.loginPage("/login")
		.usernameParameter("usernameKSHRD")
		.passwordParameter("passwordKSHRD")
		.defaultSuccessUrl("/test")
		.failureUrl("/login?error");
		http.csrf().disable();
	}

}
