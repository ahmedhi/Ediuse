package org.sid.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
@Entity
public class Entreprise implements Serializable{
	@Id @GeneratedValue
	private Long idEntreprise;
	private String iceEntreprise;
	private String ifEntreprise ;
	private String raisonSocialEntreprise;
	@OneToMany(mappedBy="entreprise",fetch=FetchType.LAZY)
	private Collection <DocEntreprise> docEntreprises;
	
	//Contructeur par defaut
	public Entreprise() {
		super();
	}
	
	//Constructeur avec paramètres 
	public Entreprise(String iceEntreprise, String ifEntreprise, String raisonSocialEntreprise) {
		super();
		this.iceEntreprise = iceEntreprise;
		this.ifEntreprise = ifEntreprise;
		this.raisonSocialEntreprise = raisonSocialEntreprise;
	}

	//Getters and Setters 
	public Long getIdEntreprise() {
		return idEntreprise;
	}
	public void setIdEntreprise(Long idEntreprise) {
		this.idEntreprise = idEntreprise;
	}
	public String getIceEntreprise() {
		return iceEntreprise;
	}
	public void setIceEntreprise(String iceEntreprise) {
		this.iceEntreprise = iceEntreprise;
	}
	public String getIfEntreprise() {
		return ifEntreprise;
	}
	public void setIfEntreprise(String ifEntreprise) {
		this.ifEntreprise = ifEntreprise;
	}
	public String getRaisonSocialEntreprise() {
		return raisonSocialEntreprise;
	}
	public void setRaisonSocialEntreprise(String raisonSocialEntreprise) {
		this.raisonSocialEntreprise = raisonSocialEntreprise;
	}

}
