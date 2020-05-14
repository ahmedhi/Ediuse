package com.example.demo.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
@Entity
public class Utilisateur implements Serializable{
	@Id @GeneratedValue
	private Long idUser;
	private String loginUser;
	private String nomUser;
	private String prenomUser ;
	private String pwdUser;
	private int typeUser ; // Admin 1 , comptable 2
	@OneToMany(mappedBy="utilisateur",fetch=FetchType.LAZY)
	private Collection<DocEntreprise> docEntreprises ;
	
	public Utilisateur() {
		super();
	}
	
	public Utilisateur(String loginUser, String nomUser, String prenomUser, String pwdUser, int typeUser) {
		super();
		this.loginUser = loginUser;
		this.nomUser = nomUser;
		this.prenomUser = prenomUser;
		this.pwdUser = pwdUser;
		this.typeUser = typeUser;
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
	public int getTypeUser() {
		return typeUser;
	}
	public void setTypeUser(int typeUser) {
		this.typeUser = typeUser;
	}
	

}
