package org.sid.web;

import javax.validation.Valid;

import org.sid.dao.UtilisateurRepository;
import org.sid.entities.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/")
public class IndexController {

	@Autowired
	private UtilisateurRepository utilisateurRepository ;

	@RequestMapping("/")
	public String index() {
		return "connexion/login";
	}

	@GetMapping("signup")
	public String createCount() {
		return "connexion/signup";
	}
	
	
	@PostMapping("signup")
	public String createCount(@Valid Utilisateur user, BindingResult  result , Model model) {
		if(result.hasErrors()) {
			return "/connexion/singup";
		}
		this.utilisateurRepository.save(new Utilisateur(user.getLoginUser(),user.getNomUser(),user.getPrenomUser(),user.getPwdUser(),"USER"));
		return "redirect:/";
	}
	
	
	@GetMapping("/login")
	public String signin() {
		return "/connexion/login";
	}
	
	@GetMapping("/403")
	public String accessDneied() {
		return "403";
	}
	
	/*
	@GetMapping("/user")
	public String user() {
		return  ("<h1>Welcome user</h1>");
	}
	
	@GetMapping("/admin")
	public String admin() {
		return  ("<h1>Welcome admin </h1>");
	}*/
}
