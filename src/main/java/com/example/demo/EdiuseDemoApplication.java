package com.example.demo;

import com.example.demo.dao.EntrepriseRepository;
import com.example.demo.dao.TypeDocRepository;
import com.example.demo.dao.UtilisateurRepository;
import com.example.demo.entities.Entreprise;
import com.example.demo.entities.Type_Doc;
import com.example.demo.entities.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EdiuseDemoApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(EdiuseDemoApplication.class, args);
	}
	
	@Autowired
	private UtilisateurRepository utilisateurRepository ;
	@Autowired
	private EntrepriseRepository entrepriseRepository ;
	@Autowired
	private TypeDocRepository typeDocRepository ;


	@Override
	public void run(String... args) throws Exception {
		
		Utilisateur u1 = utilisateurRepository.save(new Utilisateur("user1","ASKOUR","Hamza","password",1));
		Utilisateur u2 = utilisateurRepository.save(new Utilisateur("user2","HILALI","Ahmed","password",2));
		
		Entreprise e1 = entrepriseRepository.save(new Entreprise("120000570","34567890000","Sygar"));
		Entreprise e2 = entrepriseRepository.save(new Entreprise("123456789","12345678910","Koutoubia"));
		
		Type_Doc t1 = typeDocRepository.save(new Type_Doc("Excel","/tmp/Excel"));

	}

}
