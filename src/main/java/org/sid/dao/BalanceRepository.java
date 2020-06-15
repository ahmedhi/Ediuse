package org.sid.dao;

import org.sid.entities.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<Balance , Long> {
}
