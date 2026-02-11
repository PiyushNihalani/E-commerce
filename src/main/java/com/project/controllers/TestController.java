package com.project.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.locale.MessageByLocaleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

	private final MessageByLocaleService messageByLocaleService;
	
	@GetMapping
	public String TestApi() {
		return messageByLocaleService.getMessage("greeting", null);

	}
}
