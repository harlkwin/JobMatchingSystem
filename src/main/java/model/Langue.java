package model;

import java.io.Serializable;

public class Langue  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  String nom;
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getNiveau() {
		return niveau;
	}
	public void setNiveau(String niveau) {
		this.niveau = niveau;
	}
	private String niveau;
}
