package org.sid.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
@Entity
public class Company implements Serializable{
	@Id @GeneratedValue
	private Long idCompany;
	private String iceCompany;
	private String ifCompany ;
	private String NameCompany;
	@OneToMany(mappedBy="company",fetch=FetchType.LAZY)
	private Collection <DocCompany> docCompanies;

	public Company() {
	}

	public Company(Long idCompany, String iceCompany, String ifCompany, String raisonSocialCompany, Collection<DocCompany> docCompanies) {
		this.idCompany = idCompany;
		this.iceCompany = iceCompany;
		this.ifCompany = ifCompany;
		this.NameCompany = raisonSocialCompany;
		this.docCompanies = docCompanies;
	}

	public Long getIdCompany() {
		return idCompany;
	}

	public void setIdCompany(Long idCompany) {
		this.idCompany = idCompany;
	}

	public String getIceCompany() {
		return iceCompany;
	}

	public void setIceCompany(String iceCompany) {
		this.iceCompany = iceCompany;
	}

	public String getIfCompany() {
		return ifCompany;
	}

	public void setIfCompany(String ifCompany) {
		this.ifCompany = ifCompany;
	}

	public String getNameCompany() {
		return NameCompany;
	}

	public void setNameCompany(String nameCompany) {
		NameCompany = nameCompany;
	}

	public Collection<DocCompany> getDocCompanies() {
		return docCompanies;
	}

	public void setDocCompanies(Collection<DocCompany> docCompanies) {
		this.docCompanies = docCompanies;
	}
}
