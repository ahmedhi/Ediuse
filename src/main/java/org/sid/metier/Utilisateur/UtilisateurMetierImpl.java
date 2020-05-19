package org.sid.metier.Utilisateur;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.sid.dao.UtilisateurRepository;
import org.sid.entities.Utilisateur;
import org.sid.exception.RessourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UtilisateurMetierImpl implements IUtilisateurMetier {

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
		}else {
			throw new RessourceNotFoundException ("l'utilisateur introuvable, son Id : " + user.getIdUser());
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
		else {
			throw new RessourceNotFoundException ("l'utilisateur introuvable, son Id : " + id);
		}
	}

	@Override
	public void deleteUser(long id) {
		Optional<Utilisateur> userDB = this.utilisateurRepository.findById(id);
		
		if(userDB.isPresent()) {
			this.utilisateurRepository.delete(userDB.get());
		}
		else {
			throw new RessourceNotFoundException ("l'utilisateur introuvable, son Id : " + id);
		}
	}

}
