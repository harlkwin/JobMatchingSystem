package com.snteam.competence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import FuzzyCompetence.Candidat;
import FuzzyCompetence.Personne;
import fuzzyevaluation.Appartenance;
import model.Competence;


public class Test {

	
	
	public static void main(String [] argv) throws Exception {
		
		String uri_base="http://www.semanticweb.org/kakaroto/ontologies/2019/3/newVersion#";
		String uri_competence="http://www.semanticweb.org/kakaroto/ontologies/2019/3/Competence#";
		String path="/home/kakaroto/Pfe/tp/test/";
		String filename="/home/kakaroto/object.txt";
		String profil_filename="/home/kakaroto/pfeprofil.txt";
		ArrayList<String> stopWords=Nlp.readStopWord("/home/kakaroto/open_nlp/models/stopwords/stopwords.txt");
		
		OntologieManipulation app=new OntologieManipulation(uri_base,path,uri_competence,stopWords);
		
		OWLNamedIndividual cv=app.df.getOWLNamedIndividual(IRI.create(uri_base+"2887976"));
		
		ArrayList<OWLNamedIndividual> candidat=new ArrayList<OWLNamedIndividual>();
		//Map<Personne,Map<String,ArrayList<Competence>>> C=new HashMap<Personne, Map<String,ArrayList<Competence>>>();
		//candidat.add(cv);
		//ArrayList<Personne> p=app.getCompetencesByType(candidat);
		
		
		/*OWLNamedIndividual root=app.df.getOWLNamedIndividual(IRI.create(uri_base+"gestion_des_ressources_humaines"));
		OWLNamedIndividual concept1=app.df.getOWLNamedIndividual(IRI.create(uri_base+"negocier_des_compromis"));
		OWLNamedIndividual concept2=app.df.getOWLNamedIndividual(IRI.create(uri_base+"rediger_des_rapports_sur_travail"));
		OWLNamedIndividual pps=app.getPPS(concept1, concept2);
		System.out.println()*/
		//double distance=app.Distance_WUPalmer(concept1, concept2);
		//System.out.println("distance : " +distance);
		app.appariementUtilisantPalmer("397");
		//System.out.println("result "+ app.app_palmer);
		
		for(Personne c:app.app_palmer) {
			
			System.out.println("id "+ c.getId());
			System.out.println("similarite "+ c.getDistance());
		}
		
	}
}
