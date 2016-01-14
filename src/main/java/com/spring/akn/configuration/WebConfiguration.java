package com.spring.akn.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.mangofactory.swagger.plugin.EnableSwagger;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.spring.akn")
@EnableSwagger
@PropertySource("classpath:application.properties")
public class WebConfiguration extends WebMvcConfigurerAdapter {
	
	@Autowired
	private Environment environment;
	

	// For set Resource folder path
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}
    
	@Bean
	public ViewResolver getViewResolver(){
		InternalResourceViewResolver resolver=new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/pages/");
		resolver.setSuffix(".jsp");
		return resolver;
	}
	

	// Data Source For Using DataBase

		@Bean
		public DataSource dataSource() {
			DriverManagerDataSource dataSource = new DriverManagerDataSource();
			dataSource.setUrl(environment.getProperty("AKN.datasource.url"));
			dataSource.setDriverClassName(environment.getProperty("AKN.datasource.driver"));
			dataSource.setUsername(environment.getProperty("AKN.datasource.username"));
			dataSource.setPassword(environment.getProperty("AKN.datasource.password"));
			return dataSource;
		}

		@Bean
		public JdbcTemplate getJdbcTemplate() {
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
			return jdbcTemplate;
		}


	@Bean
	public MultipartResolver multipartResolver() {
		org.springframework.web.multipart.commons.CommonsMultipartResolver multipartResolver = new org.springframework.web.multipart.commons.CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(2097152);
		return multipartResolver;
	}
}
