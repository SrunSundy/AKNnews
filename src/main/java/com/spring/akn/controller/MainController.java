package com.spring.akn.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {
	@RequestMapping(value="/apidoc")
	public String apiDoc(){
		System.out.println("API DOC WORK");
		return"test";
	}
	@RequestMapping(value="/test")
	public String test(){
		System.out.println("API DOC WORK");
		return"test";
	}
	@RequestMapping(value="/login")
	public String login(ModelMap m){
		return "login";
	}
	
	@RequestMapping(value="/accessDeined")
	public String accessDenied(ModelMap m){
		return "accessDenied";
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/home";
	}
	
}
