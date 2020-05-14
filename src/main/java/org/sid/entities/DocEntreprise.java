package org.sid.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Entity
public class DocEntreprise implements Serializable{
		@Id @GeneratedValue
		private Long idDoc ;
		private Date dateUpload ;
		@ManyToOne
		@JoinColumn(name="Entreprise")
		private Entreprise entreprise ;
		@ManyToOne
		@JoinColumn(name="Utilisateur")
		private Utilisateur utilisateur ;
		@ManyToOne
		@JoinColumn(name="TypeDoc")
		private Type_Doc typeDoc ;
		
		//Constructeur par defaut
		public DocEntreprise() {
			super();
		}
		
		//Constructeur avec paramètres
		public DocEntreprise(Date dateUpload, Entreprise entreprise, Utilisateur utilisateur, Type_Doc typeDoc) {
			super();
			this.dateUpload = dateUpload;
			this.entreprise = entreprise;
			this.utilisateur = utilisateur;
			this.typeDoc = typeDoc;
		}
		//Geters and seters 

		public Long getIdDoc() {
			return idDoc;
		}

		public void setIdDoc(Long idDoc) {
			this.idDoc = idDoc;
		}

		public Date getDateUpload() {
			return dateUpload;
		}

		public void setDateUpload(Date dateUpload) {
			this.dateUpload = dateUpload;
		}

		public Entreprise getEntreprise() {
			return entreprise;
		}

		public void setEntreprise(Entreprise entreprise) {
			this.entreprise = entreprise;
		}

		public Utilisateur getUtilisateur() {
			return utilisateur;
		}

		public void setUtilisateur(Utilisateur utilisateur) {
			this.utilisateur = utilisateur;
		}

		public Type_Doc getTypeDoc() {
			return typeDoc;
		}

		public void setTypeDoc(Type_Doc typeDoc) {
			this.typeDoc = typeDoc;
		}
		
		
		
	
}
