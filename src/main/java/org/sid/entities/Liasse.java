package org.sid.entities;

import java.util.ArrayList;
import java.util.List;

public class Liasse {
    int id;
    String IF , StartDate, EndDate;
    List<Bilan> bilanActif, bilanPassif, cpc;
    List<PartSocial> partSocial;

    public Liasse(int id, String IF, String startDate, String endDate) {
        this.id = id;
        this.IF = IF;
        StartDate = startDate;
        EndDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIF() {
        return IF;
    }

    public void setIF(String IF) {
        this.IF = IF;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public List<Bilan> getBilanActif() {
        return bilanActif;
    }

    public void setBilanActif(List<Bilan> bilanActif) {
        this.bilanActif = bilanActif;
    }

    public List<Bilan> getBilanPassif() {
        return bilanPassif;
    }

    public void setBilanPassif(List<Bilan> bilanPassif) {
        this.bilanPassif = bilanPassif;
    }

    public List<Bilan> getCpc() {
        return cpc;
    }

    public void setCpc(List<Bilan> cpc) {
        this.cpc = cpc;
    }

    public List<PartSocial> getPartSocial() {
        return partSocial;
    }

    public void setPartSocial(List<PartSocial> partSocial) {
        this.partSocial = partSocial;
    }
}
