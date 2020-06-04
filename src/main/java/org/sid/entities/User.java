package org.sid.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

@Entity
public class User implements Serializable{
	@Id @GeneratedValue
	private Long idUser;
	private String loginUser;
	private String lastNameUser;
	private String firstNameUser ;
	private String pwdUser;
	private String role ; // Admin  , user 
	private String fileType ;
	
	
	
	@OneToMany(mappedBy="user",fetch=FetchType.LAZY)
	private Collection<DocCompany> docCompanies ;
	
	@Transient
	private MultipartFile file ;

	public User() {
	}

	public User(String loginUser, String lastNameUser, String firstNameUser, String pwdUser, String role, String fileType ) {
		this.loginUser = loginUser;
		this.lastNameUser = lastNameUser;
		this.firstNameUser = firstNameUser;
		this.pwdUser = pwdUser;
		this.role = role;
		this.fileType = fileType;
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

	public String getLastNameUser() {
		return lastNameUser;
	}

	public void setLastNameUser(String lastNameUser) {
		this.lastNameUser = lastNameUser;
	}

	public String getFirstNameUser() {
		return firstNameUser;
	}

	public void setFirstNameUser(String firstNameUser) {
		this.firstNameUser = firstNameUser;
	}

	public String getPwdUser() {
		return pwdUser;
	}

	public void setPwdUser(String pwdUser) {
		this.pwdUser = pwdUser;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Collection<DocCompany> getDocCompanies() {
		return docCompanies;
	}

	public void setDocCompanies(Collection<DocCompany> docCompanies) {
		this.docCompanies = docCompanies;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
}
