package org.sid.dao;

import org.sid.entities.PartSocial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PartCapitalSocialRepository extends JpaRepository<PartSocial,Long>{

	  @Query("select u from PartSocial u where u.idPart like :x")
	    public PartSocial findById(@Param("x") long id );
}
