package fuzzyevaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.apache.http.nio.entity.NByteArrayEntity;

public class FuzzyEvaluation {
	
	int nbEvaluateur;
	int nbCompetence;
	double p1;
	double p2;
	double p3;
	ArrayList<Evaluateur> evaluateurs;
	ArrayList<Candidat> candidats;
	ArrayList<Term> linguistiquesystem;
	ArrayList<String> competences;
	
	     public FuzzyEvaluation(int nbe, int nbc, double p1, double p2, double p3) {
			
	    	 nbEvaluateur=nbe;
	    	 nbCompetence=nbc;
	    	 this.p1=p1;
	    	 this.p2=p2;
	    	 this.p3=p3;
	    	 evaluateurs=new ArrayList<Evaluateur>();
	    	 candidats=new ArrayList<Candidat>();
	    	 linguistiquesystem=new ArrayList<Term>();
	    	 competences=new ArrayList<String>();
		}
	
	public Appartenance calculateMoyArithmetiqueFloueForCandidat(Candidat C , Evaluateur E) {
		
		Map<String,Appartenance> competence_appartenance=E.getAppartenance(C);
		Appartenance moyenne=new Appartenance();
		double a=0;
		double b=0;
		double c=0;
		for(Map.Entry<String, Appartenance> entry : competence_appartenance.entrySet()) {
			       
				a=a+entry.getValue().getA();
				b=b+entry.getValue().getB();
				c=c+entry.getValue().getC();
			       
		}
		
		moyenne.setA(a/nbCompetence);
		moyenne.setB(b/nbCompetence);
		moyenne.setC(c/nbCompetence);
		
		return moyenne;
		
	}
	
	public Appartenance calculateMoyArithmetiqueFloueByExpert(Candidat ca) {
		
		Map<Evaluateur,Appartenance> eval_app=ca.getMoyArithmetqueFloue();
		double a=0;
		double b=0;
		double c=0;
		Appartenance moyenne=new Appartenance();
		for(Map.Entry<Evaluateur, Appartenance> entry :eval_app.entrySet()) {
			
			a=a+entry.getValue().getA();
			b=b+entry.getValue().getB();
			c=c+entry.getValue().getC();
		}
		
		moyenne.setA(a/nbEvaluateur);
		moyenne.setB(b/nbEvaluateur);
		moyenne.setC(c/nbEvaluateur);
		
		return moyenne;
		}
	
	
	public Map<Term,Double> calculateTermDistanceCandidate(Candidat c){
		   
		Appartenance app=c.getMoyArithmetiqueFloueByExpert();
		Map<Term,Double> term_distance=new HashMap<Term,Double>();
		System.out.println("****************************");
		System.out.println("candidat  "+ c.getId());
		System.out.println("****************************");
		for(Term term:linguistiquesystem) {
			
		
			double a_diff=Math.pow(term.getAppartenance().getA()-app.getA(),2)*p1;
			//System.out.println("valeur de a_diff" +a_diff +" a candidat: " +app.getA());
			double b_diff=Math.pow(term.getAppartenance().getB()-app.getB(),2)*p2;
			//System.out.println("valeur de b_diff" +b_diff +" a candidat: " +app.getB());
			double c_diff=Math.pow(term.getAppartenance().getC()-app.getC(),2)*p3;
			//System.out.println("valeur de c_diff" +c_diff +" a candidat: " +app.getC());
			
			double somme=a_diff+b_diff+c_diff;
			
			
			term_distance.put(term, Math.sqrt(somme));
			//System.out.println("somme "+term_distance.get(term));
		}
		//System.out.println("****************************");
	//	System.out.println("****************************");
		
		return term_distance;
		
		
	}
	
	public static HashMap<Term,Double> sortByValue(HashMap<Term,Double> hm) 
    { 
        
        List<Map.Entry<Term,Double> > list = 
               new LinkedList<Map.Entry<Term,Double> >(hm.entrySet()); 
  
       
        Collections.sort(list, new Comparator<Map.Entry<Term,Double> >() { 
            

			public int compare(Entry<Term, Double> arg0, Entry<Term, Double> arg1) {
				// TODO Auto-generated method stub
				
				return arg0.getValue().compareTo(arg1.getValue());
			} 
        }); 
          
       
        HashMap<Term, Double> temp = new LinkedHashMap<Term,Double>(); 
        for (Entry<Term, Double> aa : list) { 
            temp.put(aa.getKey(), aa.getValue()); 
        } 
        
        return temp; 
    } 
	
	public Term getNearestTerm(Candidat c) {
		
		
		
		Term nearest_term=null;
		Map<Term,Double> term_distance=sortByValue((HashMap<Term, Double>) c.getTerm_distance());
		
		for(Map.Entry<Term, Double> term : term_distance.entrySet()) {
			      double tmp =term.getValue();
			   // System.out.println("term : "+ term.getKey().getTerm() + " distance "+ tmp);
			      
			      nearest_term=term.getKey();
			      break;
			      
		}
		c.setNearest_term(nearest_term);
		System.out.println("term le plus proche : " + nearest_term.getTerm());
		return nearest_term;
	}
	
	public static Appartenance getAppartenance() {
		Scanner in=new Scanner(System.in);
		Appartenance app=new Appartenance();
		System.out.println("enter a : \n");
		app.setA(in.nextDouble());
		System.out.println("enter b : \n");
		app.setB(in.nextDouble());
		System.out.println("enter c : \n");
		app.setC(in.nextDouble());
		return app;
	}
	public   Appartenance getAppartenanceByValue(String value) {
		
		for(Term term : linguistiquesystem) {
			
			if(term.getTerm().equals(value)) {
				//System.out.println(term.getAppartenance().toString());
				return term.getAppartenance();
			}
		}
		
		
		return null;
	}
	
	public  Map<String,Appartenance> getCompetenceAppartenance(int nb) {
		Map<String, Appartenance> competence_appartenance=new HashMap<String, Appartenance>();
		Scanner in=new Scanner(System.in);
		
		for(int i=0;i<nb;i++) {
			System.out.println("entrer term du competence: " +competences.get(i));
			
			//System.out.println("entrer Term: \n");
			String term =in.next();
			competence_appartenance.put(competences.get(i), getAppartenanceByValue(term));
		}
		
		
		return competence_appartenance;
	}
	
	public void addCandidat(Candidat c) {
		candidats.add(c);
	}
	public void addEvaluateur(Evaluateur e) {
		evaluateurs.add(e);
	}
	public void addTerm(Term e) {
		linguistiquesystem.add(e);
	}
	
	
	public void addAllEvaluateur() {
		Scanner in=new Scanner(System.in);
		for(int i=0;i<nbEvaluateur;i++) {
			Evaluateur e=new Evaluateur();
			 Map<Candidat,Map<String,Appartenance>> evaluations=new HashMap<Candidat,Map<String,Appartenance>>();
			 Map<Candidat,Appartenance> moyArithmetqueFloue=new HashMap<Candidat,Appartenance>();
			System.out.println("enter Evaluateur ID :");
			int id=in.nextInt();
			e.setId(id);
			e.setEvaluations(evaluations);
			e.setMoyArithmetqueFloue(moyArithmetqueFloue);
			addEvaluateur(e);
			
		}
	}
	public void addAllCompetences() {
		Scanner in=new Scanner(System.in);
		for(int i=0;i<nbCompetence;i++) {
			System.out.println("entrer nom competence \n");
			String comp=in.next();
			competences.add(comp);
		}
	}
	public void addAllCandidat(int nbCandidat) {
		Scanner in=new Scanner(System.in);
		for(int i=0;i<nbCandidat;i++) {
			Candidat e=new Candidat();
			 Map<Evaluateur,Map<String,Appartenance>> evaluations=new HashMap<Evaluateur,Map<String,Appartenance>>();
			 Map<Evaluateur,Appartenance> moyArithmetqueFloue=new HashMap<Evaluateur,Appartenance>();
			 Map<Term,Double> term_distance=new HashMap<Term, Double>();
			System.out.println("enter Candidat ID :");
			int id=in.nextInt();
			e.setId(id);
			e.setTerm_distance(term_distance);
			e.setMoyArithmetqueFloue(moyArithmetqueFloue);
			e.setEvaluations(evaluations);
			addCandidat(e);
			
		}
	}
	
	public void addAllLinguicTerm(int nbTerm) {
		Scanner in=new Scanner(System.in);
		for(int i=0;i<nbTerm;i++) {
			Term e=new Term();
			System.out.println("enter term nom :");
			String term=in.next();
			e.setTerm(term);
			Appartenance app=getAppartenance();
			e.setAppartenance(app);
			addTerm(e);
			
		}
	}
	
	
	public  void printEvaluateurs() {
		for(Evaluateur e:evaluateurs) {
			System.out.println("****************************");
			System.out.println("evauateur key :  "+e.getId());
			System.out.println("****************************");
			System.out.println("****************************");
			for(Map.Entry<Candidat, Map<String,Appartenance>> entry : e.getEvaluations().entrySet() ) {
				 
				System.out.println("candidat   "+entry.getKey().getId());
				
				for(Map.Entry<String,Appartenance> ent : entry.getValue().entrySet()) {
					System.out.println("value a :  "+ent.getValue().getA() +" value b: "+ent.getValue().getB() +"value c: " +ent.getValue().getC() );
					
				}
				
				
			}
			System.out.println("****************************");
			System.out.println("****************************");
			for(Map.Entry<Candidat,Appartenance> ent:e.getMoyArithmetqueFloue().entrySet()) {
				
				System.out.println("candidat:  "+ ent.getKey().getId() + " value a :  "+ent.getValue().getA() +" value b: "+ent.getValue().getB() +" value c: " +ent.getValue().getC() );
			}
			System.out.println("****************************");
			System.out.println("****************************");
		}
	}
	public void affectEvaluationToCandidat() {
		
		
		for(Evaluateur e:evaluateurs) {
			System.out.println("evaluateur n: " +e.getId());
			for(Candidat c : candidats) {
				System.out.println("evaluation candiat "+ c.getId()+"\n");
				Map<String,Appartenance> comp_app=getCompetenceAppartenance(nbCompetence);
				e.addEvaluation(c, comp_app);
				c.addEvaluation(e, comp_app);
				//System.out.println("A : " +e.getAppartenance(c).get("se").getA());
				Appartenance moyFloue=calculateMoyArithmetiqueFloueForCandidat(c, e);
				c.getMoyArithmetqueFloue().put(e, moyFloue);
				e.getMoyArithmetqueFloue().put(c, moyFloue);
				
			}
		}
	}
	
	public void affectDistanceAndMAFE() {
		for(Candidat ca:candidats) {
			
			Appartenance app=calculateMoyArithmetiqueFloueByExpert(ca);
			
			ca.setMoyArithmetiqueFloueByExpert(app);
			System.out.println("candidat : " + ca.getId() +"\n");
			Map<Term,Double> term_distance=calculateTermDistanceCandidate(ca);
			
			for(Map.Entry<Term, Double> entry :term_distance.entrySet()) {
				System.out.println("le  terme lingustique : " + entry.getKey().getTerm() + " distance : "+entry.getValue()+"\n");
			}
			ca.setTerm_distance(term_distance);
		
			Term term=getNearestTerm(ca);
			
			ca.setNearest_term(term);
		}
	}
	public void display() {
		for(Candidat ca:candidats) {
			System.out.println("nearest term to Candiat N: "+ ca.getId());
			System.out.println("term : "+ca.getNearest_term().getTerm());
		}
	}
	public ArrayList<Term> getLinguistiquesystem() {
		return linguistiquesystem;
	}

	public void setLinguistiquesystem(ArrayList<Term> linguistiquesystem) {
		this.linguistiquesystem = linguistiquesystem;
	}

	public static void main(String [] argc) {
		FuzzyEvaluation fe= new FuzzyEvaluation(3,3,0.2,0.2,0.6);
		
		fe.addAllEvaluateur();
		fe.addAllCandidat(3);
		fe.addAllLinguicTerm(3);
		fe.addAllCompetences();
		fe.affectEvaluationToCandidat();
		//fe.printEvaluateurs();
		fe.affectDistanceAndMAFE();
		fe.display();
		
	}

	public ArrayList<Candidat> getCandidats() {
		return candidats;
	}

	public void setCandidats(ArrayList<Candidat> candidats) {
		this.candidats = candidats;
	}
	
}
