package org.sid.dao;

import org.sid.entities.ChartOfAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public interface ChartOfAccountsRepository extends JpaRepository<ChartOfAccounts,Long>{

    @Query("From ChartOfAccounts WHERE refClass = ?1")
    public ChartOfAccounts findByRefClass( Integer _refClass );

    @Query("From ChartOfAccounts WHERE refAccount = ?1")
    public ChartOfAccounts findByRefAccount( Integer _refAccount );

    @Query("From ChartOfAccounts WHERE refClass = ?1 AND refAccount = ?2 ")
    public ChartOfAccounts findByRefClassAndRefAccount( Integer _refClass, Integer _refAccount );

}
