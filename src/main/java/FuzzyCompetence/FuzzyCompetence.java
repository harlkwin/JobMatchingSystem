package FuzzyCompetence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.naming.spi.DirStateFactory.Result;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import com.snteam.competence.Nlp;
import com.snteam.competence.OntologieManipulation;

import fuzzyevaluation.Term;
import model.Competence;


class sortByDistance implements Comparator<Candidat>{

	public  int compare(Candidat arg0, Candidat arg1) {
		
		if(arg0.getDistance()>arg1.getDistance()) return 1;
		if(arg0.getDistance()<arg1.getDistance()) return -1;
		return 0;
	}
	
}

class sortByAlphabet implements Comparator<Competence>{

	public int compare(Competence arg0, Competence arg1) {
		// TODO Auto-generated method stub
		return arg0.getManifestation().compareToIgnoreCase(arg1.getManifestation());
	}

	
	
}
public class FuzzyCompetence {
	
	public ArrayList<Candidat> candidats;
	public IdealCandidat Icandidat;
	public Map<Candidat,Double> distance;
	public Map<Candidat,Double> competenceIndex;
	public FuzzyCompetence() {
		
		  this.candidats=new ArrayList<Candidat>();
            this.Icandidat=new IdealCandidat();
            this.distance=new HashMap<Candidat, Double>();
            this.competenceIndex=new HashMap<Candidat, Double>();
	}
	
	public  static  double HammingDistance(ArrayList<Interval> arr1, ArrayList<Interval> arr2) {
		
		double somme=0;
		
		for(int i=0;i<arr1.size()-1;i++) {
			
				somme=somme+(Math.abs(arr1.get(i).getMin()-arr2.get(i).getMin())+Math.abs(arr1.get(i).getMax()-arr2.get(i).getMax()));
			
		}
		int size=2*arr1.size();
		//System.out.println(" size : "+ size +" somme "+somme);
		double result=somme/size;
		return result;
		
	}
	
	public static  void sortByDistance(ArrayList<Candidat> candidats){
		   
		
		Collections.sort(candidats, new sortByDistance());
		
	}
	
	public static  Interval transformInt(Interval i, Interval j) {
		
		 if(!i.isInterval() && j.isInterval()) {
			 
			 
			 if(i.getMin()>j.getMin()) {
					
					return new Interval(j.getMin(), i.getMin(),true);
					
				}else {
					
					return  new Interval(i.getMin(), i.getMin(), true);
				}
		 }else if(i.isInterval() && !j.isInterval()) {
			    
			 if(j.getMin()>i.getMin()) {
				 
				 return new Interval(i.getMin(),j.getMin(),true);
			 }
			 else {
				 return  new Interval(i.getMin(),i.getMin(),false);
			 }
			 
		 }
	return null;
		
}
	public HashMap<Candidat,Double> SortMapByDistance(Map<Candidat,Double> distance,int type) {
		
		 List<Map.Entry<Candidat,Double> > list = 
	              new LinkedList<Map.Entry<Candidat,Double> >(distance.entrySet());
		 if(type==0) {
			 Collections.sort(list, new Comparator<Map.Entry<Candidat,Double> >() {

					public int compare(Entry<Candidat, Double> arg0, Entry<Candidat, Double> arg1) {
						// TODO Auto-generated method stub
						if(arg0.getValue()>arg1.getValue()) return 1;
						if(arg0.getValue()<arg1.getValue()) return -1;
						return 0;
					} 
			          

						 
			      }); 
		 }else {
			 Collections.sort(list, new Comparator<Map.Entry<Candidat,Double> >() {

					public int compare(Entry<Candidat, Double> arg0, Entry<Candidat, Double> arg1) {
						// TODO Auto-generated method stub
						if(arg0.getValue()<arg1.getValue()) return 1;
						if(arg0.getValue()>arg1.getValue()) return -1;
						return 0;
					} 
			          

						 
			      }); 
		 }
		  
		  
		  HashMap<Candidat, Double> temp = new LinkedHashMap<Candidat,Double>(); 
	        for (Entry<Candidat, Double> aa : list) { 
	            temp.put(aa.getKey(), aa.getValue()); 
	        } 
	        
	       return temp;
		 
		
	}
	
	public  void getDataFromOntology(String postecode, ArrayList<Candidat> candidats) throws Exception {
		String uri_base="http://www.semanticweb.org/kakaroto/ontologies/2019/3/newVersion#";
		String uri_competence="http://www.semanticweb.org/kakaroto/ontologies/2019/3/Competence#";
		String path="/home/kakaroto/Pfe/re/";
		String filename="/home/kakaroto/object.txt";
		String profil_filename="/home/kakaroto/pfeprofil.txt";
		ArrayList<String> stopWords=Nlp.readStopWord("/home/kakaroto/open_nlp/models/stopwords/stopwords.txt");
		OntologieManipulation om =new OntologieManipulation(uri_base, path, uri_competence, stopWords);
		OWLClass Personne=om.getDf().getOWLClass(IRI.create(uri_base+"Personne"));
		ArrayList<OWLNamedIndividual> personnes=om.getAllIndividualsOfAClass(Personne);
		
		ArrayList<OWLNamedIndividual> CompPoste= new ArrayList<OWLNamedIndividual>();
		
		ArrayList<OWLNamedIndividual> Poste= new ArrayList<OWLNamedIndividual>();
		
		
		OWLNamedIndividual poste=om.getDf().getOWLNamedIndividual(IRI.create(uri_base+postecode));
		Poste.add(poste.asOWLNamedIndividual());
		om.extractCompetences(Poste,CompPoste,"Manifestation_Niveau");
		
		ArrayList<Competence> IcandidatComp=om.convertToComp(CompPoste);
		System.out.println("array " + IcandidatComp);
		Icandidat.setCompetenceSet(IcandidatComp);
		Icandidat.setId(postecode);
		
		for(OWLNamedIndividual personne : personnes ) {
			ArrayList<OWLNamedIndividual> CompCV=new ArrayList<OWLNamedIndividual>();
			ArrayList<OWLNamedIndividual> CV=new ArrayList<OWLNamedIndividual>();
			Candidat c =new Candidat();
			CV.add(personne.asOWLNamedIndividual());
			om.extractCompetences(CV, CompCV,"Manifestation_Niveau");
			String id=om.getValueOfAssertionAxiom(personne, "Personne_id");
			c.setId(id);
			ArrayList<Competence>CompetenceCV=om.convertToComp(CompCV);
			
			c.setCompetenceSet(CompetenceCV);
			candidats.add(c);
		}
		
		
		
		
	}
	
	
	public void  fillFuzzySetForIdealCandidat() {
		 ArrayList<Competence> competences=Icandidat.getCompetenceSet();
		  ArrayList<Interval> fuzzySet=new ArrayList<Interval>();
		 for(Competence c:competences) {
			 double niveau =c.getNiveau();
			 double normalisedNiveau=(niveau-1)/3;
			// System.out.println("normalized niveau :" + normalisedNiveau);
			 Interval niv=new Interval(normalisedNiveau,1,true);
			 fuzzySet.add(niv);
		 }
		
		 Icandidat.setFuzzySet(fuzzySet);
		
	}
	
	public void fillFuzzySetForCandidats() {
		
		for(Candidat c:candidats) {
			//System.out.println("****************");
			ArrayList<Interval> fuzzySet=new ArrayList<Interval>();
			int i=0;
			for(Competence co:c.getCompetenceSet()) {
				
				double niveau =co.getNiveau();
				double normalisedNiveau=(niveau-1)/3;
				 Interval niv=new Interval(normalisedNiveau,2,false);
				 Interval rectified=transformInt(niv, Icandidat.getFuzzySet().get(i));
				 //System.out.println(" rectified  i "+i +" "+ rectified.toString());
				 fuzzySet.add(rectified);
				 i++;
				 
				
			}
			//System.out.println("****************");
			
			c.setFuzzySet(fuzzySet);
		}
	}
	
	
	public void  SortCompetenceLikeIdealCandidat() {
		    
	  for(Candidat c:candidats) {
		   ArrayList<Competence> arr=c.getCompetenceSet();
			Collections.sort(arr, new sortByAlphabet());
		  
	  }
	  
	 Collections.sort(Icandidat.getCompetenceSet(), new sortByAlphabet());
		
		 	
	}
	
	
	public void distanceBetweenIdealAndCandidat() {
		
		
		for(int i=0;i<candidats.size();i++) {
			ArrayList<Interval> fs=new ArrayList<Interval>();
			
			double somme=HammingDistance(candidats.get(i).getFuzzySet(), Icandidat.getFuzzySet());
          //  System.out.println("somme :" + somme);
			distance.put(candidats.get(i), somme);
			
			
		}
		
		
	}
	
	public double getLengthInterval(Interval i) {
		
		return i.getMax()-i.getMin();		
	}
	
	
	public double getValueOfMemberShipFunction(Interval i , Interval j) {
		double msf=0;
		if(getLengthInterval(i.getIntersection(j))==0 && getLengthInterval(i.getUnion(j))==0) {
			msf=0;
		}else {
			 msf=getLengthInterval(i.getIntersection(j))/getLengthInterval(i.getUnion(j));
		}
		
		
		        
		        return msf;
	}
	
	public void getValuesOfMSF() {
		
		
		for(int i=0;i<candidats.size();i++) {
			
			double somme=0;
			for(int j=0;j<candidats.get(i).getFuzzySet().size();j++) {
				//System.out.println("candidate : "+ candidats.get(i).getFuzzySet().get(j).toString()+ " Ideal one : "+ Icandidat.getFuzzySet().get(j).toString());
				
				double result=getValueOfMemberShipFunction(candidats.get(i).getFuzzySet().get(j), Icandidat.getFuzzySet().get(j));
				
			//	System.out.println("result 9 " + result);
				somme=somme+result;
			}
			
			//System.out.println("somme :" +somme );
			double rs=somme/Icandidat.getCompetenceSet().size();
		//	System.out.println("resultat :" +rs );
			//System.out.println("candidat :" + candidats.get(i).getId());
			Candidat e=candidats.get(i);
			competenceIndex.put(e, rs);
		}
		
		
	}
    
	public static void main (String [] argv) throws Exception {
		
	FuzzyCompetence fc=new FuzzyCompetence();
		
	  fc.getDataFromOntology("894",fc.candidats);
	  fc.SortCompetenceLikeIdealCandidat();
	  fc.fillFuzzySetForIdealCandidat();
	  fc.fillFuzzySetForCandidats();
	  fc.distanceBetweenIdealAndCandidat();
	  fc.getValuesOfMSF();
	  HashMap<Candidat,Double> ham=fc.SortMapByDistance(fc.distance,0);
	  HashMap<Candidat,Double> di=fc.SortMapByDistance(fc.competenceIndex,1);
	  
	  
	 
	  for(Map.Entry<Candidat, Double> entry : ham.entrySet()) {
		  System.out.println("***********"); 
		  System.out.println(" id : " + entry.getKey().getId() + " distance from ideal with hamming distance: "+ entry.getValue());
		   
		  System.out.println("***********"); 
	  }
	  
	  for(Map.Entry<Candidat, Double> entry : di.entrySet()) {
		  System.out.println("***********"); 
		  System.out.println(" id : " + entry.getKey().getId()+ " distance from ideal candidat with competence Index : "+ entry.getValue());
		 
		  System.out.println("***********"); 
	  }
	  
	}
}
