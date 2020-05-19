package org.sid.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class IndexController {

	
	@RequestMapping("/")
	public String index() {
		return "/index";
	}
	@RequestMapping("/login")
	public String signin() {
		return "/login";
	}
	
	@RequestMapping("/403")
	public String accessDneied() {
		return "403";
	}
	
	/*@GetMapping("/")
	public String home() {
		return ("<h1>Welcome </h1>");
	}
*/
	@GetMapping("/user")
	public String user() {
		return  ("<h1>Welcome user</h1>");
	}
	
	@GetMapping("/admin")
	public String admin() {
		return  ("<h1>Welcome admin </h1>");
	}
}
