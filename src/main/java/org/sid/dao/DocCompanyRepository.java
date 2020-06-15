package org.sid.dao;

import org.sid.entities.DocCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DocCompanyRepository extends JpaRepository<DocCompany,Long> {

    @Query("From DocCompany WHERE idDoc = ?1")
    public DocCompany findTopByIdDoc(Long _idDoc );

}
