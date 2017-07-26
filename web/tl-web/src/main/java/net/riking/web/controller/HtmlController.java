package net.riking.web.controller;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HtmlController {

	@RequestMapping(value = "/index")
	public String index(ModelMap map) {
		return "/index";
	}
	
}
