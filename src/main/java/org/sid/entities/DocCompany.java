package org.sid.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;
@Entity
public class DocCompany implements Serializable{
	@Id @GeneratedValue
	private Long idDoc ;

	@ManyToOne
	@JoinColumn(name="company")
	private Company company ;

	@ManyToOne
	@JoinColumn(name="user")
	private User user ;

	private Date dateUpload ;
	
	@Transient
	private MultipartFile file ;

	public DocCompany() {
	}

	public DocCompany(Company company, User user, Date dateUpload) {
		this.company = company;
		this.user = user;
		this.dateUpload = dateUpload;
	}

	public DocCompany(Long idDoc, Company company, User user, Date dateUpload) {
		this.idDoc = idDoc;
		this.company = company;
		this.user = user;
		this.dateUpload = dateUpload;
	}

	public Long getIdDoc() {
		return idDoc;
	}

	public void setIdDoc(Long idDoc) {
		this.idDoc = idDoc;
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

	public Date getDateUpload() {
		return dateUpload;
	}

	public void setDateUpload(Date dateUpload) {
		this.dateUpload = dateUpload;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
}
