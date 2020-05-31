package org.sid.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Entity
public class DocCompany implements Serializable{
	@Id @GeneratedValue
	private Long idDoc ;

	private String nameFile ;

	@ManyToOne
	@JoinColumn(name="company")
	private Company company ;

	@ManyToOne
	@JoinColumn(name="user")
	private User user ;

	@ManyToOne
	@JoinColumn(name="docType")
	private DocType docType ;

	private Date dateUpload ;

	public DocCompany() {
	}

	public DocCompany(Long idDoc, String nameFile, Company company, User user, DocType docType, Date dateUpload) {
		this.idDoc = idDoc;
		this.nameFile = nameFile;
		this.company = company;
		this.user = user;
		this.docType = docType;
		this.dateUpload = dateUpload;
	}

	public Long getIdDoc() {
		return idDoc;
	}

	public void setIdDoc(Long idDoc) {
		this.idDoc = idDoc;
	}

	public String getNameFile() {
		return nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public DocType getDocType() {
		return docType;
	}

	public void setDocType(DocType docType) {
		this.docType = docType;
	}

	public Date getDateUpload() {
		return dateUpload;
	}

	public void setDateUpload(Date dateUpload) {
		this.dateUpload = dateUpload;
	}
}
