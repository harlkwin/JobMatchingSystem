package FuzzyCompetence;

import java.util.ArrayList;
import java.util.Map;

import model.Competence;

	public class Personne { 
	
	private String id;
	private Map<String,ArrayList<Competence>> savoir_faire;
	private Map<String,ArrayList<Competence>> savoir;
	private int size;
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Map<String, ArrayList<Competence>> getSavoir_faire() {
		return savoir_faire;
	}

	private double distance;
	
	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	public void setSavoir_faire(Map<String, ArrayList<Competence>> savoir_faire) {
		this.savoir_faire = savoir_faire;
	}

	public Map<String, ArrayList<Competence>> getSavoir() {
		return savoir;
	}

	public void setSavoir(Map<String, ArrayList<Competence>> savoir) {
		this.savoir = savoir;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ArrayList<Competence> getCompetenceSet() {
		return CompetenceSet;
	}

	public void setCompetenceSet(ArrayList<Competence> competenceSet) {
		CompetenceSet = competenceSet;
	}

	private ArrayList<Competence> CompetenceSet;
	private ArrayList<Interval> FuzzySet;
	
	public ArrayList<Interval> getFuzzySet() {
		return FuzzySet;
	}

	public void setFuzzySet(ArrayList<Interval> fuzzySet) {
		FuzzySet = fuzzySet;
	}
}
