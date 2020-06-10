package org.sid.entities;

import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;

@Entity
public class ChartOfAccounts implements Serializable {
    @Id @GeneratedValue
    private Long idChartOfAccounts;
    private Integer refClass;
    private Integer refAccount;
    private String wording;

    @Transient
    private MultipartFile file ;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public ChartOfAccounts() {
    }

    public ChartOfAccounts(Integer refClass, Integer refAccount, String wording) {
        this.refClass = refClass;
        this.refAccount = refAccount;
        this.wording = wording;
    }

    public Long getIdChartOfAccounts() {
        return idChartOfAccounts;
    }

    public void setIdChartOfAccounts(Long idChartOfAccounts) {
        this.idChartOfAccounts = idChartOfAccounts;
    }

    public Integer getRefClass() {
        return refClass;
    }

    public void setRefClass(Integer refClass) {
        this.refClass = refClass;
    }

    public Integer getRefAccount() {
        return refAccount;
    }

    public void setRefAccount(Integer refAccount) {
        this.refAccount = refAccount;
    }

    public String getWording() {
        return wording;
    }

    public void setWording(String wording) {
        this.wording = wording;
    }
}
