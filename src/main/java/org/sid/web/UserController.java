package org.sid.web;

import java.util.List;
import java.util.function.Supplier;

import javax.validation.Valid;

import org.sid.dao.UtilisateurRepository;
import org.sid.entities.Utilisateur;
import org.sid.metier.Utilisateur.IUtilisateurMetier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/user/")
public class UserController {
	
	@Autowired
	private UtilisateurRepository utilisateurRepository ;
	//private IUtilisateurMetier utilisateurMetier ;
	
	/*@GetMapping("login")
	public  String login() {
		return "/connexion/login";
	}
	@RequestMapping("signup")
	public  String signup() {
		return "/connexion/signup";
	}
	*/
	@RequestMapping("list")
	public String utilisateurs(Model model) {
		model.addAttribute("users",this.utilisateurRepository.findAll());
		return "index";
	}
	
	@PostMapping("signup")
	public String createCount(@Valid Utilisateur user, BindingResult  result , Model model) {
		if(result.hasErrors()) {
			return "/connexion/singup";
		}
		user.setRole("USER");
		this.utilisateurRepository.save(user);
		return "redirect:list";
	}
	@GetMapping("update/{id}")
	public String updateUser(@PathVariable("id") long id,@Valid Utilisateur user, BindingResult  result , Model model) {
		if(result.hasErrors()) {
			user.setIdUser(id);
			return "/updateStudent";
		}
		//update user
		this.utilisateurRepository.save(user);
		//get all users with update
		model.addAttribute("users",this.utilisateurRepository.findAll());
		return "index";
	}
	@GetMapping("edit/{id}")
	public String showupdateForm(@PathVariable("id") long id, Model model) {
		Utilisateur user = this.utilisateurRepository.findById(id)
				.orElseThrow(()-> new  IllegalArgumentException("Invalid utilisateur id : "+id));	
		model.addAttribute("user",user);
		return "index";
		
	}
	
	@GetMapping("delete/{id}")
	public String DeleteUser(@PathVariable("id") long id, Model model) {
		Utilisateur user = this.utilisateurRepository.findById(id)
				.orElseThrow(()-> new  IllegalArgumentException("Invalid utilisateur id : "+id));	
		this.utilisateurRepository.delete(user);
		model.addAttribute("user",this.utilisateurRepository.findAll());		
		return "";
	}
	/*@GetMapping("users")
	public ResponseEntity<List<Utilisateur>> getAllUsers(){
		return ResponseEntity.ok().body(utilisateurMetier.getAllUsers());
	}
	
	@GetMapping("/user/{id}")
	public ResponseEntity<Utilisateur> getUserById(@PathVariable long id ){
		return ResponseEntity.ok().body(utilisateurMetier.getUserById(id));
	}
	
	@GetMapping("/createCount")
	public ResponseEntity<Utilisateur> createCount(@RequestBody Utilisateur user){
		Utilisateur comptab = user;
		comptab.setRole("USER");
		return ResponseEntity.ok().body(utilisateurMetier.createUser(comptab));
	}
	
	@GetMapping("/user/createUser")
	public ResponseEntity<Utilisateur> createUser(@RequestBody Utilisateur user){
		return ResponseEntity.ok().body(utilisateurMetier.createUser(user));
	}
	
	@GetMapping("/user/update/{id}")
	public ResponseEntity<Utilisateur> updateUser(@PathVariable long id , Utilisateur user){
		user.setIdUser(id);
		return ResponseEntity.ok().body(utilisateurMetier.updateUser(user));
	}
	
	@GetMapping("/user/delete/{id}")
	public HttpStatus deleteUser(@PathVariable long id){
		this.utilisateurMetier.deleteUser(id);
		return HttpStatus.OK;
	}*/
}
