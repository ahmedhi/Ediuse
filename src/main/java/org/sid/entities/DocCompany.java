package org.sid.entities;

import java.io.Serializable;
import java.time.Year;
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

	private Year yearDoc;
	
	@Transient
	private MultipartFile file ;

	public DocCompany() {
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

	public Year getYearDoc() {
		return yearDoc;
	}

	public void setYearDoc(Year yearDoc) {
		this.yearDoc = yearDoc;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public DocCompany( Company company, User user, Date dateUpload, Year yearDoc ) {
		this.company = company;
		this.user = user;
		this.dateUpload = dateUpload;
		this.yearDoc = yearDoc;
	}
}
