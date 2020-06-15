package org.sid.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Balance implements Serializable {

    @Id
    @GeneratedValue
    private Long idBalance;
    private Long refBalance;
    @ManyToOne
    @JoinColumn(name="company")
    private Company companyBalance;
    private Long compteBalance;
    private String libelleBalance;
    private Long soldeBalance;
    private int typeBalance;
        // typeBalance = 0 débit | 1 crédit

    public Balance() {
    }

    public Balance( Long refBalance, Company companyBalance, Long compteBalance, String libelleBalance, Long soldeBalance, int typeBalance) {
        this.refBalance = refBalance;
        this.companyBalance = companyBalance;
        this.compteBalance = compteBalance;
        this.libelleBalance = libelleBalance;
        this.soldeBalance = soldeBalance;
        this.typeBalance = typeBalance;
    }

    public Long getIdBalance() {
        return idBalance;
    }

    public void setIdBalance(Long idBalance) {
        this.idBalance = idBalance;
    }

    public Long getRefBalance() {
        return refBalance;
    }

    public void setRefBalance(Long refBalance) {
        this.refBalance = refBalance;
    }

    public Company getCompanyBalance() {
        return companyBalance;
    }

    public void setCompanyBalance(Company companyBalance) {
        this.companyBalance = companyBalance;
    }

    public Long getCompteBalance() {
        return compteBalance;
    }

    public void setCompteBalance(Long compteBalance) {
        this.compteBalance = compteBalance;
    }

    public String getLibelleBalance() {
        return libelleBalance;
    }

    public void setLibelleBalance(String libelleBalance) {
        this.libelleBalance = libelleBalance;
    }

    public Long getSoldeBalance() {
        return soldeBalance;
    }

    public void setSoldeBalance(Long soldeBalance) {
        this.soldeBalance = soldeBalance;
    }

    public int getTypeBalance() {
        return typeBalance;
    }

    public void setTypeBalance(int typeBalance) {
        this.typeBalance = typeBalance;
    }
}
