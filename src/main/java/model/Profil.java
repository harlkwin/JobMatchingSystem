package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Profil implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String numero_cv;
	private ArrayList<Formation> formations;
	private ArrayList<Langue> langues;
	private Experience experience;
	private Other other;
	public Other getOther() {
		return other;
	}
	public void setOther(Other other) {
		this.other = other;
	}
	public ArrayList<Formation> getFormations() {
		return formations;
	}
	public void setFormations(ArrayList<Formation> formations) {
		this.formations = formations;
	}
	public Experience getExperience() {
		return experience;
	}
	public void setExperience(Experience experience) {
		this.experience = experience;
	}
	public String getNumero_cv() {
		return numero_cv;
	}
	public void setNumero_cv(String numero_cv) {
		this.numero_cv = numero_cv;
	}

	public ArrayList<Langue> getLangues() {
		return langues;
	}
	public void setLangues(ArrayList<Langue> langues) {
		this.langues = langues;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	

}
