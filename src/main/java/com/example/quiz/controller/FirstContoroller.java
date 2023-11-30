package com.example.quiz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FirstContoroller {
	@GetMapping("/")
	public String showHomePage() {
		return "redirect:/showHome";
	}
}
