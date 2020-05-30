package org.sid.web;

import org.sid.dao.UtilisateurRepository;
import org.sid.metier.UtilisateurMetierImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/")
public class UserController {

	@Autowired
	private UtilisateurMetierImpl utilisateurMetier ;

	private UtilisateurRepository utilisateurRepository ;

}
