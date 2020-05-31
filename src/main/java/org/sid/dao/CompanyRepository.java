package org.sid.dao;

import org.sid.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompanyRepository extends JpaRepository<Company,Long> {

    @Query("select u from Company u where u.idCompany like :x")
    public Company findById(@Param("x") long id );

}
