package fuzzyevaluation;


import java.util.Map;

public class Evaluateur {
	private  int id;
	private Map<Candidat,Map<String,Appartenance>> evaluations;
	private Map<Candidat,Appartenance> moyArithmetqueFloue;
	
	
	
	
	
	
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Map<Candidat, Appartenance> getMoyArithmetqueFloue() {
		return moyArithmetqueFloue;
	}

	public void setMoyArithmetqueFloue(Map<Candidat, Appartenance> moyArithmetqueFloue) {
		this.moyArithmetqueFloue = moyArithmetqueFloue;
	}

	public Map<Candidat,Map<String, Appartenance>> getEvaluations() {
		return evaluations;
	}

	public void setEvaluations(Map<Candidat, Map<String, Appartenance>> evaluations) {
		this.evaluations = evaluations;
	}



	public void addEvaluation(Candidat c, Map<String,Appartenance> comp_appartenance) {
		
		evaluations.put(c, comp_appartenance);
	}
	
	
	
	public Map<String,Appartenance> getAppartenance(Candidat c) {
		
	return	 evaluations.get(c);		
		
	}
	
	
}
