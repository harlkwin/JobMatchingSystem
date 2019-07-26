package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Experience  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String niveau_experience;
	private ArrayList<String> description;
	public String getNiveau_experience() {
		return niveau_experience;
	}
	public void setNiveau_experience(String niveau_experience) {
		this.niveau_experience = niveau_experience;
	}
	public ArrayList<String> getDescription() {
		return description;
	}
	public void setDescription(ArrayList<String> description) {
		this.description = description;
	}
	

	
}
