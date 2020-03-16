package com.emeq.counter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	
	private static final String template = "Hello, %s!";

	@GetMapping("/hello")
	public Hello hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Hello(String.format(template, name));
	}
//    http://localhost:4000/hello?name=emek
}
