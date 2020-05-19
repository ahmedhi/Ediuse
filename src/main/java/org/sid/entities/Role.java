package org.sid.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Role implements Serializable {
	@Id @GeneratedValue
	private Long idRole ; 
	private String nomRole ;
	
	@OneToMany(mappedBy="role",fetch=FetchType.LAZY)
	private Collection<Users_Role> users_Role ;

	public Role(String nomRole) {
		super();
		this.nomRole = nomRole;
	}
	public Role() {
		super();
	}

	public Long getIdRole() {
		return idRole;
	}

	public void setIdRole(Long idRole) {
		this.idRole = idRole;
	}

	public String getNomRole() {
		return nomRole;
	}

	public void setNomRole(String nomRole) {
		this.nomRole = nomRole;
	}

	public Collection<Users_Role> getUsers_Role() {
		return users_Role;
	}

	public void setUsers_Role(Collection<Users_Role> users_Role) {
		this.users_Role = users_Role;
	}



}
