package org.sid.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class DocType implements Serializable  {
	@Id @GeneratedValue
	private Long idTypeDoc ; 
	private String nameTypeDoc ; // Excel , txt ...
	private String pathDoc;

	public DocType() {
	}

	public DocType(Long idTypeDoc, String nameTypeDoc, String pathDoc) {
		this.idTypeDoc = idTypeDoc;
		this.nameTypeDoc = nameTypeDoc;
		this.pathDoc = pathDoc;
	}

	public Long getIdTypeDoc() {
		return idTypeDoc;
	}

	public void setIdTypeDoc(Long idTypeDoc) {
		this.idTypeDoc = idTypeDoc;
	}

	public String getNameTypeDoc() {
		return nameTypeDoc;
	}

	public void setNameTypeDoc(String nameTypeDoc) {
		this.nameTypeDoc = nameTypeDoc;
	}

	public String getPathDoc() {
		return pathDoc;
	}

	public void setPathDoc(String pathDoc) {
		this.pathDoc = pathDoc;
	}
}
