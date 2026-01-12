package com.sbproject.mall01.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainContoller {
	
	// 북리스트를 메인으로 보여주기 위함.
	@GetMapping("/")
	public String requestMain() {
		return "redirect:/books";
	}
}
