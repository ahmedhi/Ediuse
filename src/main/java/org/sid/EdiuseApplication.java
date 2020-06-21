package org.sid;

import org.sid.dao.CompanyRepository;
import org.sid.dao.DocTypeRepository;
import org.sid.dao.UserRepository;
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
	private UserRepository utilisateurRepository ;
	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private DocTypeRepository typeDocRepository ;

	@Override
	public void run(String... args) throws Exception {
	}
}	
