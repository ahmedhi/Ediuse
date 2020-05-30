package org.sid;

import org.sid.dao.EntrepriseRepository;
import org.sid.dao.TypeDocRepository;
import org.sid.dao.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class EdiuseApplication implements CommandLineRunner{

	public static void main(String[] args) {

		ApplicationContext ctx = SpringApplication.run(EdiuseApplication.class, args);
	}
	

	@Autowired
	private UtilisateurRepository utilisateurRepository ;
	@Autowired
	private EntrepriseRepository entrepriseRepository ;
	@Autowired
	private TypeDocRepository typeDocRepository ;

	@Override
	public void run(String... args) throws Exception {

	}

}
