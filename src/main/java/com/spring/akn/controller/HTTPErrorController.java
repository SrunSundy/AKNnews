package com.spring.akn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mangofactory.swagger.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class HTTPErrorController {

	@RequestMapping(value="/500")
	public String error500(ModelMap m){
		m.addAttribute("msg","Error 500 | Internal Server Error.");
		return "errors/500";
	}
	
	@RequestMapping(value="/404")
	public String error404(ModelMap m){
		m.addAttribute("msg","Error 404 | Page not found");
		return "errors/404";
	}
	
	@RequestMapping(value="/401")
	public String error401(ModelMap m){
//		m.addAttribute("msg","Error 404 | Page not found");
		return "errors/401";
	}
	
	@RequestMapping(value={"/403" , "/accessDenied"})
	public String error403(ModelMap m){
		m.addAttribute("msg","Error 403 | Access Denied Page");
		return "errors/403";
	}
}
