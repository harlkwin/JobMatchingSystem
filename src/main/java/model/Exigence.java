package model;

import java.util.ArrayList;

public class Exigence {
	
	private String formation_de_base;
	private ArrayList<String> type_des_contract;
	private String experience;
	public String getFormation_de_base() {
		return formation_de_base;
	}
	public void setFormation_de_base(String formation_de_base) {
		this.formation_de_base = formation_de_base;
	}
	public ArrayList<String> getType_des_contract() {
		return type_des_contract;
	}
	public void setType_des_contract(ArrayList<String> type_des_contract) {
		this.type_des_contract = type_des_contract;
	}
	public String getExperience() {
		return experience;
	}
	public void setExperience(String experience) {
		this.experience = experience;
	}
	
	

}
