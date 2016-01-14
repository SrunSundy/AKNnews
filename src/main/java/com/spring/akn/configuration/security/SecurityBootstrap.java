package com.spring.akn.configuration.security;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import com.spring.akn.configuration.WebConfiguration;

public class SecurityBootstrap extends AbstractSecurityWebApplicationInitializer {
	public SecurityBootstrap() {
	     super(SecurityConfiguration.class,SecurityAPIDoc.class,RESTAuthenticationEntryPoint.class,WebConfiguration.class);
	}
}
