package com.example.springbootrestsecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
	
	@GetMapping("/")
	public String rootRedirect() {
		return "redirect:swagger-ui.html";
	}
	
}
