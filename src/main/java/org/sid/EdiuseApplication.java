package org.sid;

import org.sid.dao.EntrepriseRepository;
import org.sid.dao.TypeDocRepository;
import org.sid.dao.UtilisateurRepository;
import org.sid.entities.Entreprise;
import org.sid.entities.Type_Doc;
import org.sid.entities.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import nz.net.ultraq.thymeleaf.LayoutDialect;

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

	@Override
	public void run(String... args) throws Exception {
		 
		
		Utilisateur u1 = utilisateurRepository.save(new Utilisateur("user1","ASKOUR","Hamza","password","ADMIN"));
		Utilisateur u2 = utilisateurRepository.save(new Utilisateur("user2","HILALI","Ahmed","password","USER"));
		
		
		Entreprise e1 = entrepriseRepository.save(new Entreprise("120000570","34567890000","Sygar"));
		Entreprise e2 = entrepriseRepository.save(new Entreprise("123456789","12345678910","Koutoubia"));
		
		Type_Doc t1 = typeDocRepository.save(new Type_Doc("Excel","/tmp/Excel"));

	}

}
