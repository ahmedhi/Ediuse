package org.sid.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.*;

@Entity
public class PartSocial  implements Serializable  {
	@Id  @GeneratedValue
	private Long idPart ; 
	private String fullName ;
	private String cin ;
	private String adress  ; 
	private Double exercicePrec ; 
	private Double exerciceActuel ;
	private double partSocial ; 
	private double montantCapitalSouscrit ;
	private double montantCapitalAppele ;
	private double montantCapitalLibere ;
	@ManyToOne
	@JoinColumn(name="company")
	private Company company ;
	
	
	public PartSocial() {
		super();
	}
	public PartSocial(String fullName , String cIN, String adress,
			Double exercicePrec, Double exerciceActuel, double partSocial, double montantCapitalSouscrit,
			double montantCapitalAppele, double montantCapitalLibere ) {
		super();
		this.fullName =  fullName ;
		this.cin = cIN;
		this.adress = adress;
		this.exercicePrec = exercicePrec;
		this.exerciceActuel = exerciceActuel;
		this.partSocial = partSocial;
		this.montantCapitalSouscrit = montantCapitalSouscrit;
		this.montantCapitalAppele = montantCapitalAppele;
		this.montantCapitalLibere = montantCapitalLibere;
	}
	public Long getIdPart() {
		return idPart;
	}
	public void setIdPart(Long idCapital) {
		this.idPart = idCapital;
	}
	
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getCin() {
		return cin;
	}
	public void setCin(String cIN) {
		this.cin = cIN;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	public Double getExercicePrec() {
		return exercicePrec;
	}
	public void setExercicePrec(Double exercicePrec) {
		this.exercicePrec = exercicePrec;
	}
	
	public Double getExerciceActuel() {
		return exerciceActuel;
	}
	public void setExerciceActuel(Double exerciceActuel) {
		this.exerciceActuel = exerciceActuel;
	}
	public double getPartSocial() {
		return partSocial;
	}
	public void setPartSocial(double partSocial) {
		this.partSocial = partSocial;
	}
	public double getMontantCapitalSouscrit() {
		return montantCapitalSouscrit;
	}
	public void setMontantCapitalSouscrit(double montantCapitalSouscrit) {
		this.montantCapitalSouscrit = montantCapitalSouscrit;
	}
	public double getMontantCapitalAppele() {
		return montantCapitalAppele;
	}
	public void setMontantCapitalAppele(double montantCapitalAppele) {
		this.montantCapitalAppele = montantCapitalAppele;
	}
	public double getMontantCapitalLibere() {
		return montantCapitalLibere;
	}
	public void setMontantCapitalLibere(double montantCapitalLibere) {
		this.montantCapitalLibere = montantCapitalLibere;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	

}
