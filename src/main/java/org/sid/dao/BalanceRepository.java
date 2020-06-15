package org.sid.dao;

import org.sid.entities.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BalanceRepository extends JpaRepository<Balance , Long> {

    @Query("From Balance WHERE refBalance = ?1 ")
    public List<Balance> findByRefBalance(Long _refBalance );

}
