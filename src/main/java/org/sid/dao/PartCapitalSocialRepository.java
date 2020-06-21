package org.sid.dao;

import org.sid.entities.PartCapitalSocial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PartCapitalSocialRepository extends JpaRepository<PartCapitalSocial,Long>{

	  @Query("select u from PartCapitalSocial u where u.idPart like :x")
	    public PartCapitalSocial findById(@Param("x") long id );
}
