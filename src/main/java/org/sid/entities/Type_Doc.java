package org.sid.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Type_Doc implements Serializable  {
	@Id @GeneratedValue
	private Long idTypeDoc ; 
	private String nomTypeDoc ; // Excel , txt ...
	private String cheminDoc;
	@OneToMany(mappedBy="typeDoc",fetch=FetchType.LAZY)
	private Collection <DocEntreprise> docEntreprises ;
	
	//Constructeur par défaut 
	public Type_Doc() {
		super();
	}	
	//Constructeur avec paramètre

	public Type_Doc(String nomTypeDoc , String cheminDoc) {
		super();
		this.nomTypeDoc = nomTypeDoc;
		this.cheminDoc = cheminDoc;
	}
	//Setters & Getters

	public Long getIdTypeDoc() {
		return idTypeDoc;
	}

	public String getCheminDoc() {
		return cheminDoc;
	}

	public void setCheminDoc(String cheminDoc) {
		this.cheminDoc = cheminDoc;
	}

	public void setIdTypeDoc(Long idTypeDoc) {
		this.idTypeDoc = idTypeDoc;
	}

	public String getNomTypeDoc() {
		return nomTypeDoc;
	}

	public void setNomTypeDoc(String nomTypeDoc) {
		this.nomTypeDoc = nomTypeDoc;
	}

	public Collection<DocEntreprise> getDocEntreprises() {
		return docEntreprises;
	}

	public void setDocEntreprises(Collection<DocEntreprise> docEntreprises) {
		this.docEntreprises = docEntreprises;
	}
	
	
	
	

}
