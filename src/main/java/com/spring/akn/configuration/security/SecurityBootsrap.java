package com.spring.akn.configuration.security;


import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;


public class SecurityBootsrap extends AbstractSecurityWebApplicationInitializer {

	public SecurityBootsrap(){
		super(SecurityConfiguration.class);
	}
}
