package org.sid;

import org.sid.dao.EntrepriseRepository;
import org.sid.dao.RoleRepository;
import org.sid.dao.TypeDocRepository;
import org.sid.dao.Users_RoleRepository;
import org.sid.dao.UtilisateurRepository;
import org.sid.entities.Entreprise;
import org.sid.entities.Role;
import org.sid.entities.Type_Doc;
import org.sid.entities.Users_Role;
import org.sid.entities.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
public class EdiuseApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(EdiuseApplication.class, args);
	}
	
	@Autowired
	private UtilisateurRepository utilisateurRepository ;
	@Autowired
	private EntrepriseRepository entrepriseRepository ;
	@Autowired
	private TypeDocRepository typeDocRepository ;
	@Autowired
	private RoleRepository roleRepository ;
	@Autowired
	private Users_RoleRepository usersRoleRepository ;
	

	@Override
	public void run(String... args) throws Exception {
		 
		Role r1 = roleRepository.save(new Role("ADMIN"));
		Role r2 = roleRepository.save(new Role("USER"));
		
		Utilisateur u1 = utilisateurRepository.save(new Utilisateur("user1","ASKOUR","Hamza","password","ADMIN"));
		Utilisateur u2 = utilisateurRepository.save(new Utilisateur("user2","HILALI","Ahmed","password","USER"));
		
		Users_Role ur1= usersRoleRepository.save(new Users_Role(u1,r1));
		Users_Role ur2= usersRoleRepository.save(new Users_Role(u2,r2));
		
		Entreprise e1 = entrepriseRepository.save(new Entreprise("120000570","34567890000","Sygar"));
		Entreprise e2 = entrepriseRepository.save(new Entreprise("123456789","12345678910","Koutoubia"));
		
		Type_Doc t1 = typeDocRepository.save(new Type_Doc("Excel","/tmp/Excel"));

	}

}
