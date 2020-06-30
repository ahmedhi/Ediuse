package org.sid.dao;

import java.util.List;

import org.sid.entities.PartSocial;
import org.sid.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PartCapitalSocialRepository extends JpaRepository<PartSocial,Long>{

  	@Query("select u from PartSocial u where u.idPart like :x")
	public PartSocial findById(@Param("x") long id );

	@Query("select u from PartSocial u where u.company.idCompany like :x")
	public List<PartSocial> findTopByCompany(@Param("x") long id );
}
