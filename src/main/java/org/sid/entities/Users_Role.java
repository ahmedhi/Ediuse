package org.sid.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Users_Role implements Serializable {
	@Id @GeneratedValue
	private Long idUsersRole ;
	@ManyToOne
	@JoinColumn(name="Utilisateur")
	private Utilisateur utilisateur ;
	@ManyToOne
	@JoinColumn(name="Role")
	private Role role ;
	
	
	
	public Users_Role() {
		super();
	}
	
	
	public Users_Role(Utilisateur utilisateur, Role role) {
		super();
		this.utilisateur = utilisateur;
		this.role = role;
	}


	public Long getIdUsersRole() {
		return idUsersRole;
	}
	public void setIdUsersRole(Long idUsersRole) {
		this.idUsersRole = idUsersRole;
	}
	public Utilisateur getUtilisateur() {
		return utilisateur;
	}
	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}

	
}
