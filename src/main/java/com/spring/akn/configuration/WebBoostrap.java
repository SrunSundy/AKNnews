package com.spring.akn.configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class WebBoostrap implements WebApplicationInitializer {

	public void onStartup(ServletContext container) throws ServletException {
		// 1. Create AnnotationConfigWebApplicationContext object
		AnnotationConfigWebApplicationContext servletContext = new AnnotationConfigWebApplicationContext();
		// 2. Add the Configuration Class
		servletContext.register(WebConfiguration.class);

		// 3. Create DispatcherServlet, add it to the container
		// and assign it to the servletRegistration
		ServletRegistration.Dynamic dispatcherServlet = container.addServlet("springDispatcher",
				new DispatcherServlet(servletContext));

		dispatcherServlet.addMapping("/");
		dispatcherServlet.setLoadOnStartup(1);
	}

}
