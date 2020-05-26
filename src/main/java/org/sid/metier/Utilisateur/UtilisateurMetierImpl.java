package org.sid.metier.Utilisateur;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.sid.dao.UtilisateurRepository;
import org.sid.entities.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UtilisateurMetierImpl implements IUtilisateurMetier {

	@Override
	public Utilisateur createUser(Utilisateur user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Utilisateur updateUser(Utilisateur user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Utilisateur> getAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Utilisateur getUserById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUser(long id) {
		// TODO Auto-generated method stub
		
	}
/*
	@Autowired 
	private UtilisateurRepository utilisateurRepository ;
	@Override
	public Utilisateur createUser(Utilisateur user) {
		return utilisateurRepository.save(user);
	}

	@Override
	public Utilisateur updateUser(Utilisateur user) {
		Optional<Utilisateur> userDB = this.utilisateurRepository.findById(user.getIdUser());
		
		if(userDB.isPresent()) {
			Utilisateur userUpdate = userDB.get();
			userUpdate.setIdUser(userDB.get().getIdUser());
			userUpdate.setNomUser(userDB.get().getNomUser());
			userUpdate.setPrenomUser(userDB.get().getPrenomUser());
			userUpdate.setPwdUser(userDB.get().getPwdUser());
			userUpdate.setLoginUser(userDB.get().getLoginUser());
			utilisateurRepository.save(userUpdate);
			return userUpdate ;
		}
	}

	@Override
	public List<Utilisateur> getAllUsers() {
		return utilisateurRepository.findAll();
		}

	@Override
	public Utilisateur getUserById(long id) {
		Optional<Utilisateur> userDB = this.utilisateurRepository.findById(id);
		
		if(userDB.isPresent()) {
			return userDB.get(); 
		}
		
	}

	@Override
	public void deleteUser(long id) {
		Optional<Utilisateur> userDB = this.utilisateurRepository.findById(id);
		
		if(userDB.isPresent()) {
			this.utilisateurRepository.delete(userDB.get());
		}
		
	}
*/
}
