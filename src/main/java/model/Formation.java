package model;

import java.io.Serializable;
import java.util.Map;

public class Formation  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String niveau_d_etude;
	private String period ;
	private String f_nom;
	private String etablissement;
	private String description;
	public String getEtablissement() {
		return etablissement;
	}
	public void setEtablissement(String etablissement) {
		this.etablissement = etablissement;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getF_nom() {
		return f_nom;
	}
	public void setF_nom(String f_nom) {
		this.f_nom = f_nom;
	}
	private String formation_description;
	public String getFormation_description() {
		return formation_description;
	}
	public void setFormation_description(String formation_description) {
		this.formation_description = formation_description;
	}
	public String getNiveau_d_etude() {
		return niveau_d_etude;
	}
	public void setNiveau_d_etude(String niveau_d_etude) {
		this.niveau_d_etude = niveau_d_etude;
	}
	
	
}
