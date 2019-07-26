package model;

import java.io.Serializable;
import java.util.Vector;

public class Occupation implements Serializable {
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String nom;
private String Descritption;
private Vector<String> savoir;
private Vector<String> savoir_faire;





public String getNom() {
	return nom;
}
public void setNom(String nom) {
	this.nom = nom;
}
public Vector<String> getSavoir() {
	return savoir;
}
public void setSavoir(Vector<String> savoir) {
	this.savoir = savoir;
}
public Vector<String> getSavoir_faire() {
	return savoir_faire;
}
public void setSavoir_faire(Vector<String> savoir_faire) {
	this.savoir_faire = savoir_faire;
}
public String getDescritption() {
	return Descritption;
}
public void setDescritption(String descritption) {
	Descritption = descritption;
}

}
