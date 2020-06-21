package org.sid.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class PartCapitalSocial  implements Serializable {

	@Id  @GeneratedValue
	private Long idPart ; 
	private String firstNameAssocie ; 
	private String lastNameAssocie ; 
	private String identifiantFiscal ; 
	private String cin ;
	private String adress  ; 
	private Double exercicePrec ; 
	private Double exerciceActuel ;
	private double partSocial ; 
	private double montantCapitalSouscrit ;
	private double montantCapitalAppele ;
	private double montantCapitalLibere ;
	@OneToMany(mappedBy="capitalSocial",fetch=FetchType.LAZY)
	private Collection<Company> company ;
	
	
	public PartCapitalSocial() {
		super();
	}
	public PartCapitalSocial(String fistNameAssocie, String lastNameAssocie, String numIF, String cIN, String adress,
			Double exercicePrec, Double exerciceActuel, double partSocial, double montantCapitalSouscrit,
			double montantCapitalAppele, double montantCapitalLibere ) {
		super();
		this.firstNameAssocie = fistNameAssocie;
		this.lastNameAssocie = lastNameAssocie;
		this.identifiantFiscal = numIF;
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
	public String getFirstNameAssocie() {
		return firstNameAssocie;
	}
	public void setFisrNameAssocie(String fisrNameAssocie) {
		this.firstNameAssocie = fisrNameAssocie;
	}
	public String getLastNameAssocie() {
		return lastNameAssocie;
	}
	public void setLastNameAssocie(String lastNameAssocie) {
		this.lastNameAssocie = lastNameAssocie;
	}
	public String getIf() {
		return identifiantFiscal;
	}
	public void setIf(String numIF) {
		this.identifiantFiscal = numIF;
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
	public Collection<Company> getCompany() {
		return company;
	}
	public void setCompany(Collection<Company> company) {
		this.company = company;
	}
	
	
}
