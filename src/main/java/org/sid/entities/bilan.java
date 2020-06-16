package org.sid.entities;

public class bilan {
	private String libelle ;
	private double brut ; 
	private double amort ;
	private double net ;
	private  boolean type  ;
	public bilan(String libelle, double brut, double amort, double net , boolean type) {
		super();
		this.libelle = libelle;
		this.brut = brut;
		this.amort = amort;
		this.net = net;
		this.type = type ;
	}
	public bilan() {
		super();
	}

	public String getLibelle() {
		return libelle;
	}
	public boolean getType() {
		return type;
	}
	public void setType(boolean type) {
		this.type = type;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public double getBrut() {
		return brut;
	}
	public void setBrut(double brut) {
		this.brut = brut;
	}
	public double getAmort() {
		return amort;
	}
	public void setAmort(double amort) {
		this.amort = amort;
	}
	public double getNet() {
		return net;
	}
	public void setNet(double net) {
		this.net = net;
	}

}
