package org.sid.dao;

import org.sid.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur,Long>{

    @Query("select u from Utilisateur u where u.idUser like :x")
    public Utilisateur findById(@Param("x") long id );

}
