package org.sid.metier;

import java.util.List;

import javax.transaction.Transactional;

import org.sid.dao.UtilisateurRepository;
import org.sid.entities.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UtilisateurMetierImpl implements IUtilisateurMetier {
	@Autowired UtilisateurRepository utilisateurRepository;

	@Override
	public Utilisateur createUser(Utilisateur user) {
		return utilisateurRepository.save( user );
	}

	@Override
	public Utilisateur updateUser(Utilisateur user) {
		return this.utilisateurRepository.save(user);
	}

	@Override
	public List<Utilisateur> getAllUsers() {
		return utilisateurRepository.findAll();
	}

	@Override
	public Utilisateur getUserById(long id) {
		// TODO Auto-generated method stub
		return utilisateurRepository.findById( id );
	}

	@Override
	public void deleteUser(long id) {
		// TODO Auto-generated method stub
		
	}

}
