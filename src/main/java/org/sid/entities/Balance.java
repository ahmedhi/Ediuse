package org.sid.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Balance implements Serializable {

    @Id
    @GeneratedValue
    private Long idBalance;
    private Long refBalance;
    private Long compteBalance;
    private String libelleBalance;
    private double soldeBalance;
    private int typeBalance;
        // typeBalance = 1 débit | 0 crédit

    public Balance() {
    }

    public Balance( Long refBalance, Long compteBalance, String libelleBalance, double soldeBalance, int typeBalance) {
        this.refBalance = refBalance;
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

    public double getSoldeBalance() {
        return soldeBalance;
    }

    public void setSoldeBalance(double soldeBalance) {
        this.soldeBalance = soldeBalance;
    }

    public int getTypeBalance() {
        return typeBalance;
    }

    public void setTypeBalance(int typeBalance) {
        this.typeBalance = typeBalance;
    }
}
