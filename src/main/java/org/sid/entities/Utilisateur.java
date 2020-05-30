package org.sid.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.Valid;
@Entity
public class Utilisateur implements Serializable{
	@Id @GeneratedValue
	private Long idUser;
	private String loginUser;
	private String nomUser;
	private String prenomUser ;
	private String pwdUser;
	private String role ; // Admin  , user 
	
	@OneToMany(mappedBy="utilisateur",fetch=FetchType.LAZY)
	private Collection<DocEntreprise> docEntreprises ;
	
	public Utilisateur() {
		super();
	}
	
	public Utilisateur(String loginUser, String nomUser, String prenomUser, String pwdUser , String type) {
		super();
		this.loginUser = loginUser;
		this.nomUser = nomUser;
		this.prenomUser = prenomUser;
		this.pwdUser = pwdUser;
		this.role = type ;
	}

	public Utilisateur( Utilisateur user ){
		super();
		this.loginUser = user.getLoginUser();
		this.nomUser = user.getNomUser();
		this.prenomUser = user.getPrenomUser();
		this.pwdUser = user.getPwdUser();
		this.role = user.getRole() ;
	}

	public Long getIdUser() {
		return idUser;
	}
	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}
	public String getLoginUser() {
		return loginUser;
	}
	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}
	public String getNomUser() {
		return nomUser;
	}
	public void setNomUser(String nomUser) {
		this.nomUser = nomUser;
	}
	public String getPrenomUser() {
		return prenomUser;
	}
	public void setPrenomUser(String prenomUser) {
		this.prenomUser = prenomUser;
	}
	public String getPwdUser() {
		return pwdUser;
	}
	public void setPwdUser(String pwdUser) {
		this.pwdUser = pwdUser;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	

}
