package org.sid.web;

import javax.validation.Valid;

import org.sid.dao.UserRepository;
import org.sid.entities.User;
import org.sid.metier.UserMetierImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class AccountController {

	@Autowired
	private UserMetierImpl userMetier ;

	public String users( Model model ){
		model.addAttribute("users",this.userMetier.getAllUsers() );
		return "index";
	}

	@RequestMapping("/")
	public String index() {
		return "connexion/login";
	}

	@GetMapping("signup")
	public String createCount() {
		return "connexion/signup";
	}

	@PostMapping("signup")
	public String createAccount(@Valid User user, BindingResult  result , Model model) {
		if(result.hasErrors()) {
			return "/connexion/singup";
		}

		user.setRole( "USER" );
		this.userMetier.createUser( user );

		return "redirect:/";
	}
	
	
	
}
