package org.sid.metier;

import java.util.List;

import org.sid.entities.Utilisateur;

public interface IUtilisateurMetier {
	Utilisateur createUser(Utilisateur user );
	Utilisateur updateUser(Utilisateur user);
	List<Utilisateur> getAllUsers();
	Utilisateur getUserById(long id);
	void deleteUser(Utilisateur user);

}
