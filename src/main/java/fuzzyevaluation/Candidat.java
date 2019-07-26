package fuzzyevaluation;

import java.util.Map;

public class Candidat {
  
	
	private int id;
	
	private Map<Evaluateur,Map<String,Appartenance>> evaluations;
	private Map<Evaluateur,Appartenance> moyArithmetqueFloue;
	private Appartenance MoyArithmetiqueFloueByExpert ;
	private Map<Term,Double> term_distance;
	private Term nearest_term;
	

	public Term getNearest_term() {
		return nearest_term;
	}

	public void setNearest_term(Term nearest_term) {
		this.nearest_term = nearest_term;
	}

	public Map<Term, Double> getTerm_distance() {
		return term_distance;
	}

	public void setTerm_distance(Map<Term, Double> term_distance) {
		this.term_distance = term_distance;
	}

	public Map<Evaluateur, Appartenance> getMoyArithmetqueFloue() {
		return moyArithmetqueFloue;
	}

	public void setMoyArithmetqueFloue(Map<Evaluateur, Appartenance> moyArithmetqueFloue) {
		this.moyArithmetqueFloue = moyArithmetqueFloue;
	}

	public Appartenance getMoyArithmetiqueFloueByExpert() {
		return MoyArithmetiqueFloueByExpert;
	}

	public void setMoyArithmetiqueFloueByExpert(Appartenance moyArithmetiqueFloueByExpert) {
		MoyArithmetiqueFloueByExpert = moyArithmetiqueFloueByExpert;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	

	public Map<Evaluateur, Map<String, Appartenance>> getEvaluations() {
		return evaluations;
	}

	public void setEvaluations(Map<Evaluateur, Map<String, Appartenance>> evaluations) {
		this.evaluations = evaluations;
	}

	public void addEvaluation(Evaluateur e, Map<String,Appartenance> comp_app) {
		evaluations.put(e, comp_app);
	}
	
	public void addMoyArithmetiqueFloue(Evaluateur e, Appartenance app) {
		moyArithmetqueFloue.put(e, app);
	}

	public void addTermDistance(Term t, Double d) {
		term_distance.put(t, d);
	}
}
