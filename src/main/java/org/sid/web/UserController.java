package org.sid.web;

import java.util.List;

import org.sid.entities.Utilisateur;
import org.sid.metier.Utilisateur.IUtilisateurMetier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	
	@Autowired
	private IUtilisateurMetier utilisateurMetier ;
	
	@GetMapping("/user/users")
	public ResponseEntity<List<Utilisateur>> getAllUsers(){
		return ResponseEntity.ok().body(utilisateurMetier.getAllUsers());
	}
	
	@GetMapping("/user/{id}")
	public ResponseEntity<Utilisateur> getUserById(@PathVariable long id ){
		return ResponseEntity.ok().body(utilisateurMetier.getUserById(id));
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
	}
}
