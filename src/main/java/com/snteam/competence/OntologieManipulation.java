package com.snteam.competence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.providers.DataAssertionProvider;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

import FuzzyCompetence.Candidat;
import FuzzyCompetence.Personne;
import model.Competence;
import model.DAF;
import model.Exigence;
import model.Experience;
import model.Formation;
import model.Langue;
import model.Occupation;
import model.Other;
import model.Profil;
import model.Qualification;


class sortByDistance implements Comparator<Personne>{

	public  int compare(Personne arg0, Personne arg1) {
		
		if(arg0.getDistance()<arg1.getDistance()) return 1;
		if(arg0.getDistance()>arg1.getDistance()) return -1;
		return 0;
	}
	
}

public class OntologieManipulation {
	Map<String,Integer> Niveau=new HashMap<String, Integer>();
	Map<Personne,Map<String,Double>> appariement_WuPalmer=new HashMap<Personne, Map<String,Double>>();
    public ArrayList<Personne> app_palmer =new ArrayList<Personne>();
	OWLOntologyManager om;
	
	public OWLDataFactory df;
	OWLOntology onto;
	String uri_base;
	String uri_competence;
	String path;
	ArrayList<String> stopWords;
	
	public OntologieManipulation(String uri_base,String path,String uri_competence,ArrayList<String> stopWords) throws OWLOntologyCreationException {
		this.df=OWLManager.getOWLDataFactory();
		this.uri_competence=uri_competence;
		this.uri_base=uri_base;
		this.path=path;
		this.om = OWLManager.createOWLOntologyManager();
		this.onto=om.loadOntologyFromOntologyDocument(new File(path+"fv.owl"));
		this.stopWords=stopWords;
		Niveau.put("Novice", 1);
		Niveau.put("Intermediare", 2);
		Niveau.put("Competent", 3);
		Niveau.put("Expert", 4);
	}
	
	
	
	
	public   AddAxiom relateIndividu(OWLIndividual individu1, OWLIndividual individu2 , OWLObjectProperty relation  ) {
					
			
			OWLAxiom rel_axm =df.getOWLObjectPropertyAssertionAxiom(relation, individu1, individu2);
			AddAxiom axiom=new AddAxiom(onto, rel_axm);
				
		return axiom;
		
	}
	
	public AddAxiom relateDataPropertyToValue(OWLIndividual individu,OWLDataProperty relation,String value) {
		
		OWLAxiom axm=df.getOWLDataPropertyAssertionAxiom(relation, individu,value);
		AddAxiom addaxiomchange=new AddAxiom(onto, axm);
		
		return addaxiomchange;
	}
	
	public OWLClassAssertionAxiom relateIndividuToType(OWLClass cl,OWLIndividual individu1) {
		
		
		OWLClassAssertionAxiom asser=df.getOWLClassAssertionAxiom(cl, individu1);
		
		return asser;
		
	}
	
	public boolean  checkIndividualIfExists(OWLClass cls,OWLNamedIndividual e) {
		OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(onto);
	
		NodeSet<OWLNamedIndividual> nodeset=reasoner.getInstances(cls,true);
		boolean found=false;
		if(nodeset.containsEntity(e)) {
			found=true;
			System.out.println("found");
		}
		System.out.println("not found");
		return found;
	}
	
	public ArrayList<OWLNamedIndividual> getAllIndividualsOfAClass(OWLClass cls){
		
		OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(onto);
		ArrayList<OWLNamedIndividual> arr=new ArrayList<OWLNamedIndividual>();
		NodeSet<OWLNamedIndividual> nodeset=reasoner.getInstances(cls,true);
		for(OWLNamedIndividual i :nodeset.getFlattened()) {
			arr.add(i);
		}
		return arr;
	}
	public OWLNamedIndividual getIndividualOfAClass(OWLClass cls, int value){
		
		
		OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(onto);
		ArrayList<OWLNamedIndividual> arr=new ArrayList<OWLNamedIndividual>();
		NodeSet<OWLNamedIndividual> nodeset=reasoner.getInstances(cls,true);
		for(OWLNamedIndividual i :nodeset.getFlattened()) {
			arr.add(i);
		}
		
		return arr.get(value-1);
	}
	
	public void insertOccupationIntoOntology(Occupation occ) {
		String nom=occ.getNom();
		nom=nom.replaceAll(","," ");
		nom=nom.replaceAll(" ", "_");
		int code=App.getRandomNumberInRange(0, 1000);
		String cod=code+"";
		OWLNamedIndividual p1=df.getOWLNamedIndividual(IRI.create(uri_base+cod));
		OWLClass poste=df.getOWLClass(IRI.create(uri_base+"Poste"));
		OWLClassAssertionAxiom asser_axm1= relateIndividuToType(poste, p1);
		OWLDataProperty Poste_fonction=df.getOWLDataProperty(IRI.create(uri_base+"Poste_Fonction"));
		OWLDataProperty Poste_code=df.getOWLDataProperty(IRI.create(uri_base+"Poste_Code"));
		OWLDataProperty Poste_age=df.getOWLDataProperty(IRI.create(uri_base+"Poste_age_minimum"));
		
		AddAxiom prop_axm_poste_fon=relateDataPropertyToValue(p1, Poste_fonction, nom.replace("_", " "));
		AddAxiom prop_axm_poste_code=relateDataPropertyToValue(p1, Poste_code,cod+"" );
		OWLAxiom axm=df.getOWLDataPropertyAssertionAxiom(Poste_age, p1, App.getRandomNumberInRange(26, 38));
		
		AddAxiom prop_axm_poste_age=new AddAxiom(onto, axm);
		
		om.addAxiom(onto, asser_axm1);
		om.applyChange(prop_axm_poste_fon);
		om.applyChange(prop_axm_poste_code);
		om.applyChange(prop_axm_poste_age);
		String nom_requis="requis_"+cod;
	//	System.out.println(nom_requis);
		
		OWLNamedIndividual r1=df.getOWLNamedIndividual(IRI.create(uri_base+nom_requis));
		OWLClass requis=df.getOWLClass(IRI.create(uri_base+"Requis"));
		OWLClassAssertionAxiom asser_axm2=relateIndividuToType(requis, r1);
		OWLObjectProperty relation=df.getOWLObjectProperty(uri_base+"a_requis");
		AddAxiom add_axm1=relateIndividu(p1, r1,relation);
		
		OWLNamedIndividual formation=df.getOWLNamedIndividual(IRI.create(uri_base+code+"_formation"));
		OWLClass Formation=df.getOWLClass(IRI.create(uri_base+"Formation"));
		OWLClassAssertionAxiom asser_form=relateIndividuToType(Formation, formation);
		OWLObjectProperty a_partie=df.getOWLObjectProperty(uri_base+"a_partie");
		AddAxiom add_requis_formation=relateIndividu(r1, formation,a_partie);
		OWLDataProperty formation_niveau_d_etude=df.getOWLDataProperty(IRI.create(uri_base+"Formation_niveau_d_etude"));
		AddAxiom prop_axm_formation_niveau=relateDataPropertyToValue(formation,formation_niveau_d_etude , "Bac+5");
		
		OWLNamedIndividual contrat=df.getOWLNamedIndividual(IRI.create(uri_base+code+"_contrat"));
		OWLClass Contrat=df.getOWLClass(IRI.create(uri_base+"Contrat"));
		OWLClassAssertionAxiom asser_contrat=relateIndividuToType(Contrat, contrat);
		AddAxiom add_requis_contrat=relateIndividu(r1, contrat,a_partie);
		
		OWLNamedIndividual experience=df.getOWLNamedIndividual(IRI.create(uri_base+code+"_experience"));
		OWLClass Experience=df.getOWLClass(IRI.create(uri_base+"Experience_Professionelle"));
		OWLClassAssertionAxiom asser_experience=relateIndividuToType(Experience, experience);
		AddAxiom add_requis_experience=relateIndividu(r1, experience,a_partie);
		OWLDataProperty experience_duree=df.getOWLDataProperty(IRI.create(uri_base+"Experience_duree"));
		//AddAxiom prop_axm_experience_duree=relateDataPropertyToValue(experience, experience_duree,App.getRandomNumberInRange(3,5));
		OWLAxiom axm_exp_duree=df.getOWLDataPropertyAssertionAxiom(experience_duree, experience,App.getRandomNumberInRange(0, 15));
		AddAxiom prop_axm_experience_duree= new AddAxiom(onto, axm_exp_duree);
		
		om.addAxiom(onto, asser_axm2);
		om.addAxiom(onto, asser_form);
		om.addAxiom(onto, asser_experience);
		om.addAxiom(onto, asser_contrat);
		om.applyChange(add_axm1);
		om.applyChange(add_requis_experience);
		om.applyChange(prop_axm_experience_duree);
		om.applyChange(add_requis_formation);
		om.applyChange(add_requis_contrat);
		om.applyChange(prop_axm_formation_niveau);
		String combined_name="savoir_"+cod;
	//	System.out.println(combined_name);
		OWLNamedIndividual s=df.getOWLNamedIndividual(uri_competence+combined_name);
		OWLClass savoir=df.getOWLClass(IRI.create(uri_competence+"Savoir"));
		OWLClassAssertionAxiom asser_savoir_axm1=relateIndividuToType(savoir, s);
		OWLObjectProperty relation_r_s=df.getOWLObjectProperty(uri_base+"a_partie");
		AddAxiom relation_axm_r_s=relateIndividu(r1, s, relation_r_s);
		om.addAxiom(onto, asser_savoir_axm1);
		om.applyChange(relation_axm_r_s);
		int j=0;
		for(String man:occ.getSavoir()) {
			String ma="M"+j;
			String M_N="M_N"+j;
			 man=man.replace("é", "e").replace("è","e").replace("'", "");
			OWLNamedIndividual m=df.getOWLNamedIndividual(uri_base+man.replace(" ", "_"));
			OWLClass manifestation=df.getOWLClass(IRI.create(uri_competence+"Manifestation"));
			OWLNamedIndividual m_n=df.getOWLNamedIndividual(uri_competence+M_N);
			OWLClass mani_nive=df.getOWLClass(IRI.create(uri_competence+"Manifestation_Niveau"));
			if(!checkIndividualIfExists(manifestation,m)) {
				OWLClassAssertionAxiom asser_manifestation_axm1=relateIndividuToType(manifestation, m);
				OWLDataProperty dp=df.getOWLDataProperty(uri_competence+"Manifestation_Contenu");
				String modified=man.replace("é", "e").replace("è","e").replace("'", "");
				AddAxiom prop_axm=relateDataPropertyToValue(m, dp, modified);
				OWLClassAssertionAxiom asser_m_n=relateIndividuToType(mani_nive, m_n);
				OWLObjectProperty relation_s_m=df.getOWLObjectProperty(uri_competence+"a");
				AddAxiom relation_axm_s_m=relateIndividu(s, m_n, relation_s_m);
				OWLObjectProperty relation_mn_m=df.getOWLObjectProperty(uri_competence+"a_manifestation");
				AddAxiom relation_axm_mn_m=relateIndividu(m_n, m, relation_mn_m);
				OWLObjectProperty a_niveau=df.getOWLObjectProperty(IRI.create(uri_competence+"a_niveau"));
				int value=App.getRandomNumberInRange(1, 4);
				
				OWLClass cls=df.getOWLClass(IRI.create(uri_competence+"Niveau"));
				OWLNamedIndividual nodeset=getIndividualOfAClass(cls,value);
				
				AddAxiom niv_mn=relateIndividu(m_n,nodeset, a_niveau);
				
				om.addAxiom(onto, asser_m_n);
				om.addAxiom(onto, asser_manifestation_axm1);
				om.applyChange(prop_axm);
				om.applyChange(relation_axm_s_m);
				om.applyChange(relation_axm_mn_m);
				om.applyChange(niv_mn);
				j++;
			}else {
				OWLClassAssertionAxiom asser_m_n=relateIndividuToType(mani_nive, m_n);
				OWLObjectProperty relation_s_m=df.getOWLObjectProperty(uri_competence+"a");
				AddAxiom relation_axm_s_m=relateIndividu(s, m_n, relation_s_m);
				OWLObjectProperty relation_mn_m=df.getOWLObjectProperty(uri_competence+"a_manifestation");
				AddAxiom relation_axm_mn_m=relateIndividu(m_n, m, relation_mn_m);
				OWLObjectProperty a_niveau=df.getOWLObjectProperty(IRI.create(uri_competence+"a_niveau"));
				int value=App.getRandomNumberInRange(3, 4);
				OWLClass cls=df.getOWLClass(IRI.create(uri_competence+"Niveau"));
				OWLNamedIndividual nodeset=getIndividualOfAClass(cls,value);
				AddAxiom niv_mn=relateIndividu(m_n, nodeset, a_niveau);
				om.addAxiom(onto, asser_m_n);
				om.applyChange(relation_axm_s_m);
				om.applyChange(relation_axm_mn_m);
				om.applyChange(niv_mn);
				j++;
			}
			
			
		}
		String combined_name_sf="savoirfaire_"+cod;
		//System.out.println(combined_name_sf);
		OWLNamedIndividual sf=df.getOWLNamedIndividual(uri_competence+combined_name_sf);
		OWLClass savoirf=df.getOWLClass(IRI.create(uri_competence+"Savoir_faire"));
		OWLClassAssertionAxiom asser_savoirfaire_axm1=relateIndividuToType(savoirf, sf);
		OWLObjectProperty relation_r_sf=df.getOWLObjectProperty(uri_base+"a_partie");
		AddAxiom relation_axm_r_sf=relateIndividu(r1, sf, relation_r_sf);
		om.addAxiom(onto, asser_savoirfaire_axm1);
		om.applyChange(relation_axm_r_sf);
		for(int q=0;q<10;q++) {
			
			String man=occ.getSavoir_faire().get(q);
			String ma="M"+j;
			String M_N="M_N"+j;
			man=man.replace("é", "e").replace("è","e").replace("'", "");
			OWLNamedIndividual m=df.getOWLNamedIndividual(uri_base+man.replace(" ", "_"));
			OWLClass manifestation=df.getOWLClass(IRI.create(uri_competence+"Manifestation"));
			OWLNamedIndividual m_n=df.getOWLNamedIndividual(uri_competence+M_N);
			OWLClass mani_nive=df.getOWLClass(IRI.create(uri_competence+"Manifestation_Niveau"));
			if(!checkIndividualIfExists(manifestation,m)) {
				OWLClassAssertionAxiom asser_manifestation_axm1=relateIndividuToType(manifestation, m);
				OWLDataProperty dp=df.getOWLDataProperty(uri_competence+"Manifestation_Contenu");
				String modified=man.replace("é", "e").replace("è","e").replace("'", "");
				AddAxiom prop_axm=relateDataPropertyToValue(m, dp, modified);
				OWLClassAssertionAxiom asser_m_n=relateIndividuToType(mani_nive, m_n);
				OWLObjectProperty relation_s_m=df.getOWLObjectProperty(uri_competence+"a");
				AddAxiom relation_axm_s_m=relateIndividu(sf, m_n, relation_s_m);
				OWLObjectProperty relation_mn_m=df.getOWLObjectProperty(uri_competence+"a_manifestation");
				AddAxiom relation_axm_mn_m=relateIndividu(m_n, m, relation_mn_m);
				OWLObjectProperty a_niveau=df.getOWLObjectProperty(IRI.create(uri_competence+"a_niveau"));
				int value=App.getRandomNumberInRange(3, 4);
				OWLClass cls=df.getOWLClass(IRI.create(uri_competence+"Niveau"));
				OWLNamedIndividual nodeset=getIndividualOfAClass(cls,value);
				
				AddAxiom niv_mn=relateIndividu(m_n, nodeset, a_niveau);
				
				om.addAxiom(onto, asser_m_n);
				om.addAxiom(onto, asser_manifestation_axm1);
				om.applyChange(prop_axm);
				om.applyChange(relation_axm_s_m);
				om.applyChange(relation_axm_mn_m);
				om.applyChange(niv_mn);
				j++;
			}else {
				OWLClassAssertionAxiom asser_m_n=relateIndividuToType(mani_nive, m_n);
				OWLObjectProperty relation_s_m=df.getOWLObjectProperty(uri_competence+"a");
				AddAxiom relation_axm_s_m=relateIndividu(sf, m_n, relation_s_m);
				OWLObjectProperty relation_mn_m=df.getOWLObjectProperty(uri_competence+"a_manifestation");
				AddAxiom relation_axm_mn_m=relateIndividu(m_n, m, relation_mn_m);
				OWLObjectProperty a_niveau=df.getOWLObjectProperty(IRI.create(uri_competence+"a_niveau"));
				int value=App.getRandomNumberInRange(1, 4);
				OWLClass cls=df.getOWLClass(IRI.create(uri_competence+"Niveau"));
				OWLNamedIndividual nodeset=getIndividualOfAClass(cls,value);
				AddAxiom niv_mn=relateIndividu(m_n, nodeset, a_niveau);
				om.addAxiom(onto, asser_m_n);
				om.applyChange(relation_axm_s_m);
				om.applyChange(relation_axm_mn_m);
				om.applyChange(niv_mn);
				j++;
			}
			
			
		}
		
		
	}
	
	public void insertIndividuals(String filename) throws ClassNotFoundException, IOException, OWLOntologyStorageException, OWLOntologyCreationException {
		ArrayList<Occupation> arr=new ArrayList<Occupation>();
		arr=App.readObjectFromTxt(filename);
		System.out.println("occupation"+arr.size());
		int k=0;
		for(Occupation occ:arr) {
			k++;
			insertOccupationIntoOntology(occ);
			}
		
		//System.out.println("format: "+om.getOntologyFormat(onto));
	    onto.saveOntology();
	    System.out.println("ontology saved : "+k+"   emmmmmm");
	}
	
	
	void relateFormationToAcquis(ArrayList<Formation> formations,OWLNamedIndividual a1,String numero_profil) {
		String nom_formation="formation_"+numero_profil;
		OWLIndividual f1 =df.getOWLNamedIndividual(IRI.create(uri_base+nom_formation));
		OWLClass formation=df.getOWLClass(IRI.create(uri_base+"Formation"));
		OWLClassAssertionAxiom asser_axm_form_f1=relateIndividuToType(formation, f1);
		OWLDataProperty Form_niveau_d_etude=df.getOWLDataProperty(IRI.create(uri_base+"Formation_niveau_d_etude"));
		String []nv=formations.get(0).getNiveau_d_etude().split(":");
		String newn=Nlp.removeStopWords(nv[1], stopWords);
		String v=newn.replace("b", "B");
		AddAxiom prop_axm1=relateDataPropertyToValue(f1, Form_niveau_d_etude,v);
		
		OWLObjectProperty a_partie=df.getOWLObjectProperty(IRI.create(uri_base+"a_partie"));
		AddAxiom acquis_form=relateIndividu(a1, f1, a_partie);
		om.addAxiom(onto, asser_axm_form_f1);
		om.applyChange(prop_axm1);
		om.applyChange(acquis_form);
		int j=0;
		for(Formation f :formations) {
			String nom_dip="dip_"+j+"_"+numero_profil;
			OWLIndividual d1=df.getOWLNamedIndividual(IRI.create(uri_base+nom_dip));
			OWLClass diplome =df.getOWLClass(IRI.create(uri_base+"Diplome"));
			OWLClassAssertionAxiom asser_dipl_d1=relateIndividuToType(diplome, d1);
			om.addAxiom(onto, asser_dipl_d1);
			OWLDataProperty dip_nom=df.getOWLDataProperty(IRI.create(uri_base+"Diplome_Appelation"));
			OWLDataProperty dip_desc=df.getOWLDataProperty(IRI.create(uri_base+"Diplome_Description"));
			OWLDataProperty dip_duree=df.getOWLDataProperty(IRI.create(uri_base+"Diplome_duree"));
		//	String nom_etab="etab_"+j+"_"+numero_profil;
			OWLNamedIndividual e1=df.getOWLNamedIndividual(IRI.create(uri_base+f.getEtablissement().replace(" ", "_")));
			OWLClass etablissement=df.getOWLClass(IRI.create(uri_base+"Etablissement"));
			OWLClassAssertionAxiom etab_e1=relateIndividuToType(etablissement, e1);
			if(!checkIndividualIfExists(etablissement, e1)) {
				om.addAxiom(onto, etab_e1);
				OWLDataProperty etablissement_nom=df.getOWLDataProperty(IRI.create(uri_base+"Etablissement_Nom"));
				String newf=f.getEtablissement().replace("|", "");
				AddAxiom etb_val=relateDataPropertyToValue(e1, etablissement_nom, newf);
				om.applyChange(etb_val);
			}
			
			
		
			
			OWLObjectProperty a_etablissement=df.getOWLObjectProperty(IRI.create(uri_base+"a_etablissement"));
			AddAxiom etab_dip=relateIndividu(d1, e1, a_etablissement);
			om.applyChange(etab_dip);
			
			AddAxiom dip_axm_nom=relateDataPropertyToValue(d1, dip_nom, f.getF_nom());
			//System.out.println("description: "+f.getDescription());
			String newd=f.getDescription().replace("&", "");
			AddAxiom dip_axm_desc=relateDataPropertyToValue(d1, dip_desc, newd);
			AddAxiom dip_axm_duree=relateDataPropertyToValue(d1, dip_duree, f.getPeriod());
			
			OWLObjectProperty a_diplome=df.getOWLObjectProperty(IRI.create(uri_base+"a_diplome"));
			AddAxiom form_dip=relateIndividu(f1, d1, a_diplome);
			om.applyChange(dip_axm_nom);
			om.applyChange(dip_axm_duree);
			om.applyChange(dip_axm_desc);
			om.applyChange(form_dip);
			j++;
	
		}
		
	}
	
	public void relateContratToacquis(OWLIndividual a1,Other con,String numero_profil) {
		String nom_contrat="Contrat_"+numero_profil;
		OWLObjectProperty a_partie=df.getOWLObjectProperty(IRI.create(uri_base+"a_partie"));
		OWLIndividual con1 =df.getOWLNamedIndividual(IRI.create(uri_base+nom_contrat));
		OWLClass contrat=df.getOWLClass(IRI.create(uri_base+"Contrat"));
		OWLClassAssertionAxiom asser_axm_form_f1=relateIndividuToType(contrat, con1);
		om.addAxiom(onto, asser_axm_form_f1);
		AddAxiom acquis_divers=relateIndividu(a1, con1, a_partie);
		om.applyChange(acquis_divers);
		OWLClass cn=df.getOWLClass(IRI.create(uri_base+"ContratType"));
		OWLObjectProperty a_contrat=df.getOWLObjectProperty(IRI.create(uri_base+"a_contrat"));
		ArrayList<OWLNamedIndividual> Contrattype=getAllIndividualsOfAClass(cn);
		String [] types=con.getType_contract().split("-");
		System.out.println("size of types  "+types.length +"size of  individu : "+Contrattype.size() );
		AddAxiom axm=relateIndividu(con1, Contrattype.get(App.getRandomNumberInRange(0, 2)), a_contrat);
		om.applyChange(axm);
	/*	for(String type :types) {
			String modified=type.replace(" ", "").replace("é", "e");
			System.out.println("type:" + modified);
			for(OWLNamedIndividual ctype:Contrattype) {
				
				ArrayList<OWLNamedIndividual> arr=new ArrayList<OWLNamedIndividual>();
				arr.add(ctype);
				ArrayList<String> values=getAllValuesOfAssertionAxiom(arr, "Contrat_type");
				
			
				if(values.get(0).equals(modified)) {
					//System.out.println("value: "+ values.get(0)+ " type :"+ type);
					AddAxiom axm=relateIndividu(con1, ctype, a_contrat);
					om.applyChange(axm);
				
			}
			
			
		}
		
		
		
	}*/
	}
	public void relateLangueToacquis(OWLIndividual a1,ArrayList<Langue> langues,String numero_profil) {
		String nom_divers="Divers_"+numero_profil;
		OWLObjectProperty a_partie=df.getOWLObjectProperty(IRI.create(uri_base+"a_partie"));
		OWLIndividual div1 =df.getOWLNamedIndividual(IRI.create(uri_base+nom_divers));
		OWLClass divers=df.getOWLClass(IRI.create(uri_base+"Divers"));
		OWLClassAssertionAxiom asser_axm_form_f1=relateIndividuToType(divers, div1);
		om.addAxiom(onto, asser_axm_form_f1);
		AddAxiom acquis_divers=relateIndividu(a1, div1, a_partie);
		om.applyChange(acquis_divers);
		
		for(Langue langue:langues) {
			String l=langue.getNom().toLowerCase()+numero_profil;
			OWLObjectProperty a_langue=df.getOWLObjectProperty(IRI.create(uri_base+"a_langue"));
			
			OWLNamedIndividual l1=df.getOWLNamedIndividual(IRI.create(uri_base+l));
			
			OWLClass langu=df.getOWLClass(IRI.create(uri_base+"Langue"));
			OWLClassAssertionAxiom langue_l1=relateIndividuToType(langu, l1);
			
			AddAxiom div_langue=relateIndividu(div1,l1, a_langue);
			om.applyChange(div_langue);
			
				om.addAxiom(onto,langue_l1);
				OWLDataProperty nom=df.getOWLDataProperty(IRI.create(uri_base+"Langue_nom"));
				OWLDataProperty niveau=df.getOWLDataProperty(IRI.create(uri_base+"Langue_niveau"));
				AddAxiom axm_nom=relateDataPropertyToValue(l1, nom, langue.getNom().toLowerCase());
				AddAxiom axm_niveau=relateDataPropertyToValue(l1, niveau,langue.getNiveau());
				om.applyChange(axm_nom);
				om.applyChange(axm_niveau);
		
		}
		
		
		
	}
	
	public void relateExperienceToacquis(Experience exp, String numero_profil , OWLIndividual a1 ) {
		OWLObjectProperty a_partie=df.getOWLObjectProperty(IRI.create(uri_base+"a_partie"));
		String exp_nom="exp_"+numero_profil;
		OWLIndividual exp1 =df.getOWLNamedIndividual(IRI.create(uri_base+exp_nom));
		OWLClass experience=df.getOWLClass(IRI.create(uri_base+"Experience_Professionelle"));
		OWLClassAssertionAxiom asser_axm_exp_exp1=relateIndividuToType(experience, exp1);
		om.addAxiom(onto, asser_axm_exp_exp1);
		AddAxiom acquis_divers=relateIndividu(a1, exp1, a_partie);
		int j=0;
		String exp1_nom="exper_"+j+"_"+numero_profil;
		
		OWLDataProperty duree=df.getOWLDataProperty(IRI.create(uri_base+"Experience_duree"));
		
		OWLDataProperty desc=df.getOWLDataProperty(IRI.create(uri_base+"Experience_description"));
		ArrayList<Integer> numbers=Nlp.extractNumbers(exp.getNiveau_experience());
		if(numbers.isEmpty()) {
			numbers.add(0);
		}
		OWLAxiom axm=df.getOWLDataPropertyAssertionAxiom(duree, exp1,App.getRandomNumberInRange(0, 15));
		AddAxiom axm_duree=new AddAxiom(onto, axm);
		//System.out.println("description: "+exp.getDescription());
		
		OWLObjectProperty a_experience=df.getOWLObjectProperty(IRI.create(uri_base+"a_experienc"));
		for(String descr:exp.getDescription()) {
			exp1_nom="exper_"+j+"_"+numero_profil;
			OWLIndividual exp2 =df.getOWLNamedIndividual(IRI.create(uri_base+exp1_nom));
			OWLClass experience1=df.getOWLClass(IRI.create(uri_base+"Experience"));
			OWLClassAssertionAxiom asser_axm_exp2_exp1=relateIndividuToType(experience1, exp2);
			AddAxiom axm_desc=relateDataPropertyToValue(exp2, desc,descr);
			AddAxiom expf_exp=relateIndividu(exp1, exp2, a_experience);
			j++;
			om.addAxiom(onto, asser_axm_exp2_exp1);
			om.applyChange(axm_desc);
			om.applyChange(expf_exp);
		}
		
		om.applyChange(axm_duree);
		
		
		om.applyChange(acquis_divers); 
	}
	public void inserProfilIntoOntology(Profil profil) {
		
		OWLIndividual p1=df.getOWLNamedIndividual(IRI.create(uri_base+profil.getNumero_cv()));
		OWLClass personne=df.getOWLClass(IRI.create(uri_base+"Personne"));
		OWLClassAssertionAxiom asser_pers_p=relateIndividuToType(personne, p1);
		om.addAxiom(onto, asser_pers_p);
		OWLDataProperty pers_id=df.getOWLDataProperty(IRI.create(uri_base+"Personne_id"));
		AddAxiom pers_id_axm=relateDataPropertyToValue(p1,pers_id,profil.getNumero_cv());
		om.applyChange(pers_id_axm);
		OWLDataProperty pers_age=df.getOWLDataProperty(IRI.create(uri_base+"Personne_age"));
		OWLAxiom axm=df.getOWLDataPropertyAssertionAxiom(pers_age, p1, App.getRandomNumberInRange(26, 38));
		AddAxiom pers_id_age=new AddAxiom(onto, axm);
		om.applyChange(pers_id_age);
		String nom_acquis="acquis_"+profil.getNumero_cv();
		//System.out.println(nom_acquis);
		
		OWLNamedIndividual a1=df.getOWLNamedIndividual(IRI.create(uri_base+nom_acquis));
		OWLClass acquis=df.getOWLClass(IRI.create(uri_base+"Acquis"));
		OWLClassAssertionAxiom asser_acquis_a1=relateIndividuToType(acquis, a1);
		om.addAxiom(onto, asser_acquis_a1);
		OWLObjectProperty relation=df.getOWLObjectProperty(uri_base+"a_acquis");
		AddAxiom add_axm1=relateIndividu(p1, a1,relation);
		om.applyChanges(add_axm1);
		
		
		ArrayList<Formation> formations=profil.getFormations();
		
		relateFormationToAcquis(formations, a1, profil.getNumero_cv());
		relateLangueToacquis(a1, profil.getLangues(), profil.getNumero_cv());
		relateExperienceToacquis(profil.getExperience(),profil.getNumero_cv(), a1);
		relateContratToacquis(a1, profil.getOther(), profil.getNumero_cv());
		
	}
	
	public void inserProfilsIntoOntology(String filename) throws ClassNotFoundException, IOException, OWLOntologyStorageException {
		ArrayList<Profil> profils=new ArrayList<Profil>();
		profils=App.readObjectFromTxt(filename);
		int k=0;
		for(int i=0;i< 40;i++) {
			k++;
			
			inserProfilIntoOntology(profils.get(i));
			
		}
		System.out.println("format: "+om.getOntologyFormat(onto));
	   onto.saveOntology();
		
	   //onto.saveOntology(outputStream);
	    System.out.println("ontology saved : "+ k);
	}
	
	
	public void getManByType(ArrayList<OWLNamedIndividual> savoir,ArrayList<OWLNamedIndividual> savoir_faire) throws Exception {
		
		OWLClass manifestations=df.getOWLClass(IRI.create(uri_competence+"Manifestation"));
		ArrayList<OWLNamedIndividual> man_individu=getAllIndividualsOfAClass(manifestations);
		
		for(OWLNamedIndividual i :man_individu) {
			String [] div_uri=i.toString().split("#");
			String  sentence=div_uri[1].replace("_", " ");
		//	System.out.println("words"+ sentence);
			if(Nlp.POSTagging(sentence)[0].equals("VINF")) {
			//	System.out.println("i m VINF");
				savoir_faire.add(i);
			}else {
				//System.out.println("i m not VINF");
				savoir.add(i);
			}
			
			
		}
} 
	
	public void relatePersonWithMan() throws Exception {
		OWLObjectProperty a_partie=df.getOWLObjectProperty(IRI.create(uri_base+"a_partie"));
		OWLClass acquis =df.getOWLClass(IRI.create(uri_base+"Acquis"));
		ArrayList<OWLNamedIndividual> acquises=getAllIndividualsOfAClass(acquis);
		ArrayList<OWLNamedIndividual> savoir=new ArrayList<OWLNamedIndividual>();
		ArrayList<OWLNamedIndividual> savoir_faire=new ArrayList<OWLNamedIndividual>();
		getManByType(savoir, savoir_faire);
		//System.out.println("savoir :"+savoir);
		//System.out.println("savoir :"+savoir_faire);
		//System.out.println("im here: "+ acquises);
		for(OWLNamedIndividual i : acquises) {
			//System.out.println("im here 2");
			String num_profil =i.toString().split("#")[1];
			String ps="savoir_"+num_profil;
			ps=ps.replace(">", "");
		//	System.out.println("ps : "+ ps);
			OWLIndividual sav=df.getOWLNamedIndividual(IRI.create(uri_competence+ps));
			OWLClass s=df.getOWLClass(IRI.create(uri_competence+"Savoir"));
			OWLClassAssertionAxiom s_sav=relateIndividuToType(s, sav);
			OWLClass sf=df.getOWLClass(IRI.create(uri_competence+"Savoir_faire"));
			String psf="savoir_faire"+num_profil;
			psf=psf.replace(">", "");
			OWLIndividual savfai=df.getOWLNamedIndividual(IRI.create(uri_competence+psf));
			OWLClassAssertionAxiom sf_savfai=relateIndividuToType(sf, savfai);
			om.addAxiom(onto, s_sav);
			om.addAxiom(onto, sf_savfai);
			
			AddAxiom acquis_sav=relateIndividu(i, sav, a_partie);
			AddAxiom acquis_savfai=relateIndividu(i, savfai, a_partie);
			om.applyChange(acquis_savfai);
			om.applyChange(acquis_sav);
			int j=0;
			//System.out.println("savoir :"+savoir);
			//System.out.println("savoir :"+savoir_faire);
			int zeta=App.getRandomNumberInRange(1, savoir.size()-1);
			for(int q=0;q<zeta;q++) {
				OWLIndividual sr=savoir.get(q);
				String M_N="M_N_"+j+"_"+num_profil.replace(">", "");
				//System.out.println(M_N);
				OWLNamedIndividual m_n=df.getOWLNamedIndividual(uri_competence+M_N);
				OWLClass mani_nive=df.getOWLClass(IRI.create(uri_competence+"Manifestation_Niveau"));
				OWLClassAssertionAxiom man_niv_axm=relateIndividuToType(mani_nive, m_n);
				om.addAxiom(onto, man_niv_axm);
				OWLObjectProperty a=df.getOWLObjectProperty(IRI.create(uri_competence+"a"));
				AddAxiom m_n_s=relateIndividu(sav, m_n, a);
				om.applyChange(m_n_s);
				OWLObjectProperty a_manifestation=df.getOWLObjectProperty(IRI.create(uri_competence+"a_manifestation"));
				AddAxiom m_n_s_axm=relateIndividu(m_n, sr, a_manifestation);
				om.applyChange(m_n_s_axm);
				OWLObjectProperty a_niveau=df.getOWLObjectProperty(IRI.create(uri_competence+"a_niveau"));
				int value=App.getRandomNumberInRange(1, 4);
				OWLClass cls=df.getOWLClass(IRI.create(uri_competence+"Niveau"));
				OWLNamedIndividual nodeset=getIndividualOfAClass(cls,value);
				AddAxiom niv_mn=relateIndividu(m_n, nodeset, a_niveau);
				om.applyChange(niv_mn);
				j++;
				
			}
			int alpha =App.getRandomNumberInRange(1, savoir_faire.size()-1);
			for(int q=0;q<alpha;q++) {
				OWLIndividual sr=savoir_faire.get(q);
				String M_N="M_N_"+j+"_"+num_profil.replace(">", "");
				OWLNamedIndividual m_n=df.getOWLNamedIndividual(uri_competence+M_N);
				OWLClass mani_nive=df.getOWLClass(IRI.create(uri_competence+"Manifestation_Niveau"));
				OWLClassAssertionAxiom man_niv_axm=relateIndividuToType(mani_nive, m_n);
				om.addAxiom(onto, man_niv_axm);
				OWLObjectProperty a=df.getOWLObjectProperty(IRI.create(uri_competence+"a"));
				AddAxiom m_n_s=relateIndividu(savfai, m_n, a);
				om.applyChange(m_n_s);
				OWLObjectProperty a_manifestation=df.getOWLObjectProperty(IRI.create(uri_competence+"a_manifestation"));
				AddAxiom m_n_s_axm=relateIndividu(m_n, sr, a_manifestation);
				om.applyChange(m_n_s_axm);
				OWLObjectProperty a_niveau=df.getOWLObjectProperty(IRI.create(uri_competence+"a_niveau"));
				int value=App.getRandomNumberInRange(1, 4);
				OWLClass cls=df.getOWLClass(IRI.create(uri_competence+"Niveau"));
				OWLNamedIndividual nodeset=getIndividualOfAClass(cls,value);
				AddAxiom niv_mn=relateIndividu(m_n, nodeset, a_niveau);
				om.applyChange(niv_mn);
				j++;
			}
			
			
		}
		om.saveOntology(onto);
		
	}

	public void relatePersonWithOneMan() throws Exception {
		OWLObjectProperty a_partie=df.getOWLObjectProperty(IRI.create(uri_base+"a_partie"));
		OWLClass acquis =df.getOWLClass(IRI.create(uri_base+"Acquis"));
		ArrayList<OWLNamedIndividual> acquises=getAllIndividualsOfAClass(acquis);
		ArrayList<OWLNamedIndividual> savoir=new ArrayList<OWLNamedIndividual>();
		ArrayList<OWLNamedIndividual> savoir_faire=new ArrayList<OWLNamedIndividual>();
		getManByType(savoir, savoir_faire);
		//System.out.println("savoir :"+savoir);
		//System.out.println("savoir :"+savoir_faire);
		//System.out.println("im here: "+ acquises);
		int j=0;
		for(OWLNamedIndividual i : acquises) {
			//System.out.println("im here 2");
			String num_profil =i.toString().split("#")[1];
			String ps="savoir_"+num_profil;
			ps=ps.replace(">", "");
		//	System.out.println("ps : "+ ps);
			
			OWLClass sf=df.getOWLClass(IRI.create(uri_competence+"Savoir_faire"));
			String psf="savoir_faire"+num_profil;
			psf=psf.replace(">", "");
			OWLIndividual savfai=df.getOWLNamedIndividual(IRI.create(uri_competence+psf));
			OWLClassAssertionAxiom sf_savfai=relateIndividuToType(sf, savfai);
			
			om.addAxiom(onto, sf_savfai);
			
			
			AddAxiom acquis_savfai=relateIndividu(i, savfai, a_partie);
			om.applyChange(acquis_savfai);
			
			
			//System.out.println("savoir :"+savoir);
			//System.out.println("savoir :"+savoir_faire);
			OWLIndividual 
				 sr =null;
			
			if(App.counter<25) {
				sr=savoir_faire.get(App.getRandomNumberInRange(0,1));
				App.counter++;
			}else {
				sr=savoir_faire.get(App.getRandomNumberInRange(0,4));
				App.counter++;
			}
				
				String M_N="M_N_"+j+"_"+num_profil.replace(">", "");
				OWLNamedIndividual m_n=df.getOWLNamedIndividual(uri_competence+M_N);
				OWLClass mani_nive=df.getOWLClass(IRI.create(uri_competence+"Manifestation_Niveau"));
				OWLClassAssertionAxiom man_niv_axm=relateIndividuToType(mani_nive, m_n);
				om.addAxiom(onto, man_niv_axm);
				OWLObjectProperty a=df.getOWLObjectProperty(IRI.create(uri_competence+"a"));
				AddAxiom m_n_s=relateIndividu(savfai, m_n, a);
				om.applyChange(m_n_s);
				OWLObjectProperty a_manifestation=df.getOWLObjectProperty(IRI.create(uri_competence+"a_manifestation"));
				AddAxiom m_n_s_axm=relateIndividu(m_n, sr, a_manifestation);
				om.applyChange(m_n_s_axm);
				OWLObjectProperty a_niveau=df.getOWLObjectProperty(IRI.create(uri_competence+"a_niveau"));
				int value=App.getRandomNumberInRange(1, 4);
				OWLClass cls=df.getOWLClass(IRI.create(uri_competence+"Niveau"));
				OWLNamedIndividual nodeset=getIndividualOfAClass(cls,value);
				AddAxiom niv_mn=relateIndividu(m_n, nodeset, a_niveau);
				om.applyChange(niv_mn);
			
			
			j++;
			
		}
		onto.saveOntology();;
		
	}
	public ArrayList<OWLNamedIndividual> getObject(OWLIndividual Poste) {
		ArrayList<OWLNamedIndividual> objects=new ArrayList<OWLNamedIndividual>();
		Set<OWLObjectPropertyAssertionAxiom> properties=onto.getObjectPropertyAssertionAxioms(Poste);
		
		for (OWLObjectPropertyAssertionAxiom ax: properties) {
			
			//System.out.println("type :"+ax.toString());
		    objects.add(ax.getObject().asOWLNamedIndividual());
		    
		}
		return objects;
	}
	
	public ArrayList<OWLNamedIndividual> getPathToRoot(OWLNamedIndividual concept){
		
		ArrayList<OWLNamedIndividual> path=new ArrayList<OWLNamedIndividual>();
		//System.out.println("individual "+ getObject(concept).get(0).toString());
		OWLIndividual node=concept;
		
		while(node!=null) {
			
			if(!getObject(node).isEmpty()) {
				ArrayList<OWLNamedIndividual> tmp= getObject(node);
				node=tmp.get(0);
			//	System.out.println("individual "+ node.toString());
				
				path.add(node.asOWLNamedIndividual());
			}else {
				node=null;
			}
			
			
			
		}
	
		return path;
		
	}
	public OWLNamedIndividual getPPS(OWLNamedIndividual concept1 , OWLNamedIndividual concept2) {
		
		ArrayList<OWLNamedIndividual> c1=null;
		ArrayList<OWLNamedIndividual> c2=null;
		//System.out.println("size c1: "+ getPathToRoot(concept1).size()+ " c2: "+ getPathToRoot(concept2).size());
		if(getPathToRoot(concept1).size()<getPathToRoot(concept2).size()) {
		//	System.out.println("size c1: "+ getPathToRoot(concept1).size()+ " c2: "+ getPathToRoot(concept2).size());
			c1=getPathToRoot(concept2);
			c2=getPathToRoot(concept1);
		}else {
		//	System.out.println("size c1: "+ getPathToRoot(concept1).size()+ " c2: "+ getPathToRoot(concept2).size());
			c1=getPathToRoot(concept1);
			c2=getPathToRoot(concept2);
		}
		
		
		
		
		OWLNamedIndividual pps=null;
		for(OWLNamedIndividual c11: c1) {
			for(OWLNamedIndividual c22:c2) {
				if(c11.equals(c22)) {
					
					return c22;
					
					
				}
				
			}
			
		}
		
		return pps;
	}
	
	
	public int DistanceBetweenTwoConcept(OWLNamedIndividual concept1 , OWLNamedIndividual pps ) {
		    int counter=0;
		    boolean state=true;
		  //  System.out.println("concept 1 "+ concept1.toString() +"concept  pps"+ pps.toString() );
		    if(pps==null || concept1==null || concept1.equals(pps)) return 0;
		    
		    while(state) {
		    	
		    	ArrayList<OWLNamedIndividual> c1=new ArrayList<OWLNamedIndividual>();
				
				  c1=getObject(concept1);
				  counter++;
				  
				  if(c1.get(0).equals(pps)) {
					  state=false;
				  }
				  
				  concept1=c1.get(0);
				  
		    }
		    return counter;
		
	}
	
	
	public double Distance_WUPalmer(OWLNamedIndividual concept1, OWLNamedIndividual concept2) {
		int d=0,d1=0,d2=0;
		OWLNamedIndividual pps=getPPS(concept1, concept2);
		//System.out.println("pps" +pps.toString());
		OWLNamedIndividual root=df.getOWLNamedIndividual(IRI.create(uri_base+"gestion_des_ressources_humaines"));
		d=DistanceBetweenTwoConcept(pps, root);
		d1=DistanceBetweenTwoConcept(concept1, pps);
		d2=DistanceBetweenTwoConcept(concept2, pps);
		
		double distancePalmer=(double)(2*d)/(d1+d2+2*d);
		
		return distancePalmer;
	}
public void calculateWeigthsAndSort() {
	
	
		 double distance=0;
		
		for(Map.Entry<Personne,Map<String,Double>> entry :appariement_WuPalmer.entrySet()) {
			
			Personne cand=entry.getKey();
			System.out.println("Personne id :" +cand.getId());
			
			Map<String,Double> name_distance=entry.getValue();
			
			for(Map.Entry<String, Double> en : name_distance.entrySet()) {
				System.out.println("type " + en.getKey());
				System.out.println("value :" +en.getValue());
				if(en.getKey().equals("savoir")) {
					distance=distance+0.4*en.getValue();
				}else {
					distance=distance+ 0.6*en.getValue();
				}
				   
				   
				   
			}
		//	System.out.println("distance : "+ distance);
			cand.setDistance(distance*0.2);
			app_palmer.add(cand);	
		}
		
		Collections.sort(app_palmer, new sortByDistance());
	}
	public ArrayList<Personne>  getCompetencesByType(ArrayList<OWLNamedIndividual> candidats ) throws Exception {
		ArrayList<Personne> C=new ArrayList<Personne>();
		for(OWLNamedIndividual can : candidats) {
			ArrayList<OWLNamedIndividual>  man_niv=new ArrayList<OWLNamedIndividual>();
			ArrayList<OWLNamedIndividual>  candidat=new ArrayList<OWLNamedIndividual>();
			candidat.add(can);
			extractCompetences(candidat, man_niv, "Manifestation_Niveau");
			
			ArrayList<Competence> competences=convertToComp(man_niv);
			Map<String,ArrayList<Competence>> savoir=new HashMap<String, ArrayList<Competence>>();
			Map<String,ArrayList<Competence>> savoir_faire=new HashMap<String, ArrayList<Competence>>();
			String id=getValueOfAssertionAxiom(candidat.get(0), "Personne_id");
			ArrayList<Competence> s=new ArrayList<Competence>();
			ArrayList<Competence> sf=new ArrayList<Competence>();
			
		//System.out.println("************************	size 		*"+competences.size());
			for(Competence comp : competences) {
			
				if(Nlp.POSTagging(comp.getManifestation())[0].equals("VINF")) {
					//System.out.println("savoir  faire: "+ comp.getManifestation());
						sf.add(comp);
					
						
					}else if(Nlp.POSTagging(comp.getManifestation())[0].equals("NC")){
						
						//System.out.println("savoir : "+ comp.getManifestation());
						s.add(comp);
						
						
					}else {
						//System.out.println("other : "+ comp.getManifestation());
						sf.add(comp);
					}
				
				
			}
			savoir.put("savoir", s);
			savoir_faire.put("savoir_faire", sf);
			//System.out.println("savoir faire : " + savoir_faire.get("savoir_faire"));
			Personne p=new Personne();
			p.setId(id);
			p.setSize(man_niv.size());
			p.setSavoir_faire(savoir_faire);
			p.setSavoir(savoir);
			C.add(p);
			
			//System.out.println("savoir faire :" + C.get(c).get("savoir"));
		}
		
		
		
		
		
	return C;	
		
	}
   
	
	public void appariementUtilisantPalmer(String postCode) throws Exception {
		
		OWLNamedIndividual poste=df.getOWLNamedIndividual(IRI.create(uri_base+postCode));
		 
		ArrayList<OWLNamedIndividual> p=new ArrayList<OWLNamedIndividual>();
		p.add(poste);
		ArrayList<Personne> pos=getCompetencesByType(p);
		System.out.println("poste savoir  "+ pos.get(0).getSavoir().get("savoir"));
		OWLClass Personne=df.getOWLClass(IRI.create(uri_base+"Personne"));
		ArrayList<OWLNamedIndividual> personnes=getAllIndividualsOfAClass(Personne);
		ArrayList<Personne> cans=getCompetencesByType(personnes);
		
		
		                    
		                 for(int i=0;i<cans.size()-1;i++) {
		                	 System.out.println("**********************" + "personne id "+ cans.get(i).getId());
		                	  //  System.out.println("savoir faire : "+ cans.get(i).getSavoir_faire());
		                	   // System.out.println("savoir  : "+ cans.get(i).getSavoir());
		                	    Map <String, ArrayList<Competence>> emm1 = new HashMap<String, ArrayList<Competence>>() ;
		                	    emm1.put("savoir", cans.get(i).getSavoir().get("savoir"));
								emm1.put("savoir_faire", cans.get(i).getSavoir_faire().get("savoir_faire"));
								
							//	System.out.println("**********************");
							//	System.out.println("savoir  : hash "+ emm1.get("savoir"));
			                //	System.out.println("savoir  faire :  hash map "+ emm1.get("savoir_faire"));
			                //	System.out.println("**********************");
			                //	System.out.println("**********************");
			                	
		                		 
			                	for(Map.Entry<String, ArrayList<Competence>> entry : emm1.entrySet()) {
			                		double sim1=0;
			                		 double sim2=0;
			                		  ArrayList<Competence>  candidat_compertence=entry.getValue();
			                		     System.out.println("key : " + entry.getKey() + " size of competence "+ candidat_compertence.size());
			                		  int size=0;
			                		  for(Competence c: candidat_compertence) {
			                			  
			                			//  System.out.println("competence : "+ c.getManifestation());
			                			  boolean found=false;
			                			  
			                			  Map <String, ArrayList<Competence>> emm2 = new HashMap<String, ArrayList<Competence>>() ;
					                	    emm2.put("savoir", pos.get(0).getSavoir().get("savoir"));
											emm2.put("savoir_faire", pos.get(0).getSavoir_faire().get("savoir_faire"));
			                			  //System.out.println("competence du candidat :" + candidat_compertence.get(j).getManifestation());
			                			     
			                			  ArrayList<Competence> poste_competence=emm2.get(entry.getKey());
			                			  
			                			 size= poste_competence.size();
			                			 //System.out.println("type : "+ entry.getKey() +" size "+ poste_competence.size());
			                			  for(Competence cp : poste_competence) {
			                				  
			                				 // System.out.println("competence du poste :" + poste_competence.get(z).getManifestation());
			                				  
			                				  if(cp.getManifestation().equals(c.getManifestation())) {
			                					  found=true;
			                					  sim1=sim1+1;
			                					 // System.out.println("sim1 :" + sim1);
			                					  
			                					  
			                				  }
			                				  
			                			  }
			                			  
			                			  if(!found) {
			  								String concept1=c.getManifestation().replace(" ","_");
			  								double max=0;
			  								OWLNamedIndividual con1=df.getOWLNamedIndividual(IRI.create(uri_base+concept1));
			  								for(Competence cp : poste_competence) {
			  									
			  									String concept2=cp.getManifestation().replace(" ","_");
			  								//	System.out.println("concept2 " + concept2 +" concept 1 "+ concept1);
			  									OWLNamedIndividual con2=df.getOWLNamedIndividual(IRI.create(uri_base+concept2));
			  									if(Distance_WUPalmer(con1, con2)>max) {
			  										max=Distance_WUPalmer(con1, con2);
			  										//System.out.println("sim2 :" + sim2);
			  									}
			  								}
			  								sim2=sim2+max;
			  								
			  								
			  							}
			                			
			                			  
			                		  }
			                		
			                		 sim1=sim1/size;
			                		//System.out.println("v1 : "+ v1);
			                		// System.out.println("sim 2 : "+ sim2);
			                		 sim2=sim2/(entry.getValue().size()+size);
			                		// System.out.println("sim 1 "+ sim1 +" sim 2 : "+ sim2 +" size competence  candidat "+ entry.getValue().size() +" size comp poste " + entry.getValue().size());
			                		  double sim_palmer=0.6*sim1 +0.4*sim2;
			                		  System.out.println("similarite total"+ sim_palmer);
					    				Map<String,Double> arr=new HashMap<String,Double>(); 
					    				arr.put(entry.getKey(), sim_palmer);
					    				appariement_WuPalmer.put(cans.get(i), arr);
			                	}
		                	 
			                	
		                 }
		                 
		                
		                 	calculateWeigthsAndSort();
	}
public  void extractCompetences(ArrayList<OWLNamedIndividual> subjects,ArrayList<OWLNamedIndividual> man_niv, String className ){
	
	
	ArrayList<OWLNamedIndividual> objects=new ArrayList<OWLNamedIndividual>();
	
		
	for(OWLNamedIndividual s:subjects) {
		Set<OWLClassAssertionAxiom> axioms=onto.getClassAssertionAxioms(s);
		//System.out.println("axioms: "+ axioms);
		String type=null;
		for(OWLClassAssertionAxiom as:axioms) {
				
			type=as.getClassExpression().toString().split("#")[1].replace(">","");
			
		}
		if(type.equals(className)) {
			//System.out.println("type : "+type);
			man_niv.add(s);
		}
		objects=getObject(s);
		extractCompetences(objects,man_niv,className);
	}
	
}

public ArrayList<Competence> convertToComp(ArrayList<OWLNamedIndividual> man_niveau){
	
	ArrayList<Competence> competences=new ArrayList<Competence>();
	for(OWLNamedIndividual m_n:man_niveau) {
				ArrayList<OWLNamedIndividual> objects=getObject(m_n);
	
				Competence c=new Competence();
				for(OWLNamedIndividual object:objects) {
					
					
					//System.out.println("objects "+object);
					String det=object.toString().split("#")[1].replace(">", "");
					Set<OWLDataPropertyAssertionAxiom> axiom=onto.getDataPropertyAssertionAxioms(object);
					if(det.contains("Niveau")) {
						
						for(OWLDataPropertyAssertionAxiom axm :axiom) {
							
							
							String niveau=axm.getObject().toString();
							niveau=niveau.replace("^","_");
							String [] arr=niveau.split("__");
							//System.out.println("niveau string: "+ arr[0]);
							int niv=Niveau.get(arr[0].replace("\"", ""));
						//	System.out.println("niveau : "+ niv );
							c.setNiveau(niv);
							
							
						}
					}
					else {
						
						for(OWLDataPropertyAssertionAxiom axm :axiom) {
							
							String manifestation=axm.getObject().toString();
							manifestation=manifestation.replace("^", "_");
							String [] arr=manifestation.split("__");
							String man=arr[0].replace("\"", "");
							//System.out.println("manifestation: "+man);
							c.setManifestation(man);
							
							
						}
					}
					
				}
				competences.add(c);
	}
	return competences;
	
}

public int searchForCompetenceInarray(String manifestation,ArrayList<Competence> CompetenceCV) {
	  for(int i=0;i<CompetenceCV.size();i++) {
		//System.out.println("competencecv : "+ CompetenceCV.get(i).getManifestation());
		//System.out.println("niveau cv : "+ CompetenceCV.get(i).getNiveau());
		  if(CompetenceCV.get(i).getManifestation().equals(manifestation)) {
			  //System.out.println("index : "+i);
				return i;
			}
	  }
	return 0;
	
}


public boolean searchForCompetenceInArray(String manifestation,ArrayList<Competence> CompetenceCV) {

	for(Competence c: CompetenceCV) {
		//System.out.println("competencecv : "+ c.getManifestation());
		if(c.getManifestation().equals(manifestation)) {
			//System.out.println("found it : ");
			return true;
		}
		
	}
	return false;
	
}

public String getValueOfAssertionAxiom(OWLNamedIndividual individual,String nameOfDataProperty) {
	Set<OWLDataPropertyAssertionAxiom> axiom=onto.getDataPropertyAssertionAxioms(individual);
	String value="";
	for(OWLDataPropertyAssertionAxiom axm :axiom) {
		
		String dataproperty=axm.getProperty().toString().split("#")[1].replace(">", "");
		
		
		if(dataproperty.equals(nameOfDataProperty)) {
			String niveau=axm.getObject().toString();
			niveau=niveau.replace("^","_");
			String [] arr=niveau.split("__");
			value=arr[0].replace("\"", "");
		//	System.out.println("value : "+value);
			
		}
		
		
		
	}
	return value;
}
public ArrayList<String> getAllValuesOfAssertionAxiom(ArrayList<OWLNamedIndividual> individuals,String AssertionAxiom){
	ArrayList<String> values=new ArrayList<String>();
	for(OWLNamedIndividual individual:individuals) {
		values.add(getValueOfAssertionAxiom(individual, AssertionAxiom));
	}
	
	return values;
	
}

public Qualification ExtractQualification(OWLNamedIndividual thing,String type,String attribut) {
	Qualification qualification=new Qualification();
	ArrayList<OWLNamedIndividual> things=new ArrayList<OWLNamedIndividual>();
	things.add(thing);
	ArrayList<OWLNamedIndividual> personne=new ArrayList<OWLNamedIndividual>();
	extractCompetences(things, personne, type);
	//System.out.println("personne "+ personne);
	ArrayList<String> personne_contenu=getAllValuesOfAssertionAxiom(personne, attribut);
	qualification.setAge(Integer.parseInt(personne_contenu.get(0)));
	
	
	return qualification;
}
public Exigence ExtractExigence(OWLNamedIndividual thing) {
	Exigence exigence=new Exigence();
	ArrayList<OWLNamedIndividual> things=new ArrayList<OWLNamedIndividual>();
	things.add(thing);
	ArrayList<OWLNamedIndividual> formation=new ArrayList<OWLNamedIndividual>();
	ArrayList<OWLNamedIndividual> contrat=new ArrayList<OWLNamedIndividual>();
	ArrayList<OWLNamedIndividual> experience=new ArrayList<OWLNamedIndividual>();
	
	extractCompetences(things, formation, "Formation");
	extractCompetences(things, contrat, "ContratType");
	extractCompetences(things, experience, "Experience_Professionelle");
	ArrayList<String> formation_contenu=getAllValuesOfAssertionAxiom(formation, "Formation_niveau_d_etude");
	ArrayList<String> experience_contenu=getAllValuesOfAssertionAxiom(experience, "Experience_duree");
	ArrayList<String> contrat_contenu=getAllValuesOfAssertionAxiom(contrat, "Contrat_type");
	//System.out.println("formation: "+ formation_contenu);
	//System.out.println("experience: "+ experience_contenu);
	//System.out.println("contrat: "+ contrat_contenu);
	
	exigence.setFormation_de_base(formation_contenu.get(0));
	exigence.setType_des_contract(contrat_contenu);
	exigence.setExperience(experience_contenu.get(0));
	
	return exigence;
	
}
public boolean compareExperience(String posteExp,String cvExp) {
	int posteexp=Integer.parseInt(posteExp);
	int cvexp=Integer.parseInt(cvExp);
	if(cvexp>=posteexp) {
		return true;
	}
	return false;

}
public boolean compareFormationNiveau(String formationPoste, String formationCV) {
	ArrayList<Integer> poste=Nlp.extractNumbers(formationPoste);
	ArrayList<Integer> cv=Nlp.extractNumbers(formationCV);
	if(cv.isEmpty()) {
		if(formationCV.equals(formationPoste)) {
			return true;
		}
	}else {
		if(cv.get(0)>=poste.get(0)) {
			return true;
		}
	}
	
	return false;
}

public boolean compareContrat(ArrayList<String> contratPoste , ArrayList<String> contratCv ) {
	
	for(String contratposte : contratPoste) {
		for(String contrat : contratCv) {
			if(contratposte.equals(contrat)) {
				return true;
			}
		}
	}
	
	
	
	return false;
	
}
public boolean compareAge(int agePost,int ageCV) {
	
	if(ageCV>=agePost) {
		return true;
	}
	
	return false;
}
public float [] qualificationAppariement(OWLNamedIndividual poste , OWLNamedIndividual cv, int PAge) {
	Qualification QPoste=new Qualification();
	Qualification QCV=new Qualification();
	QPoste=ExtractQualification(poste,"Poste","Poste_age_minimum");
	QCV=ExtractQualification(cv,"Personne","Personne_age");
	float PTotal=PAge;
	float PQualification=0;
//	System.out.println("age poste: "+QPoste.getAge()+ " cv age :"+ QCV.getAge());
	if(compareAge(QPoste.getAge(), QCV.getAge())) {
		PQualification=PQualification+PAge;
	}
	float[] DAQ_PTOTAL=new float[2];
	DAQ_PTOTAL[0]=PQualification/PTotal;
	DAQ_PTOTAL[1]=PTotal;
	return DAQ_PTOTAL;
	
}
public float [] exigenceAppariement(OWLNamedIndividual poste,OWLNamedIndividual cv,int PExperience,int PFNiveau, int PContrat) {
	
	Exigence ExPoste= new Exigence();
	Exigence ExCV=new Exigence();
	ExPoste=ExtractExigence(poste);
	
	ExCV=ExtractExigence(cv);
	
	float PTotal=PExperience+PFNiveau+PContrat;
	float PExigence=0;
	//System.out.println("****************");
	
	if(compareExperience(ExPoste.getExperience(), ExCV.getExperience())) {
		
		PExigence=PExigence+PExperience;
		//System.out.println("P exigence: "+ PExigence);
		
	}
	//System.out.println(" formation poste: "+ ExPoste.getFormation_de_base() +" Formation de cv"+  ExCV.getFormation_de_base());
	if(compareFormationNiveau(ExPoste.getFormation_de_base(), ExCV.getFormation_de_base())) {
		
		PExigence=PExigence+PFNiveau;
		//System.out.println("P exigence: "+ PExigence);
	}
	
	if(compareContrat(ExPoste.getType_des_contract(),ExCV.getType_des_contract())) {
		
		PExigence=PExigence+PContrat;
		//System.out.println("P exigence: "+ PExigence);
		
	}
//	System.out.println("****************");
	float [] DAE_PTot=new float[2];
	DAE_PTot[0]=PExigence/PTotal;
	
	DAE_PTot[1]=PTotal;
//	System.out.println("value : "+ DAE_PTot[0] +" --Pexigence : "+ PExigence +" --Ptotal : "+DAE_PTot[1]);
	return DAE_PTot;
}
public float [] CompetenceExact(ArrayList<OWLNamedIndividual> poste, ArrayList<OWLNamedIndividual> cv) {
	ArrayList<OWLNamedIndividual> CompPoste= new ArrayList<OWLNamedIndividual>();
	ArrayList<OWLNamedIndividual> CompCV=new ArrayList<OWLNamedIndividual>();
	int pcomp=0;
	int totalComp=0;
	
	extractCompetences(poste,CompPoste,"Manifestation_Niveau");
	extractCompetences(cv, CompCV,"Manifestation_Niveau");
	ArrayList<Competence>CompetencePoste=convertToComp(CompPoste);
	ArrayList<Competence>CompetenceCV=convertToComp(CompCV);
	
	for(Competence competenceposte:CompetencePoste) {
		totalComp=totalComp+1;
		// System.out.println("competence : "+ competenceposte.getManifestation());
		// System.out.println("niveau : "+ competenceposte.getNiveau());
		if(searchForCompetenceInArray(competenceposte.getManifestation(), CompetenceCV)) {
			int index=searchForCompetenceInarray(competenceposte.getManifestation(), CompetenceCV);
			int niveau=CompetenceCV.get(index).getNiveau();
			//System.out.println("niveau cv: "+ niveau);
			 if(niveau>=competenceposte.getNiveau()) {
				 pcomp=pcomp+1;
				// System.out.println("pcomp : "+ pcomp);
			 }
		}
		
		
		
	}
	float percentage = ((float) pcomp) / totalComp;
	float [] DAC_TOTALCOMP=new float[2];
	
	DAC_TOTALCOMP[0]=percentage;
	DAC_TOTALCOMP[1]=(float)totalComp;
	return DAC_TOTALCOMP;
	
}

public Map<String, Float> appariementCvOffres(OWLNamedIndividual poste,OWLNamedIndividual cv, int choix ,int PoidsExperience,int PoidsFormation,int PoidsContrat,int PAge) {
	float [] DAC_TOTALCOMP=new float[2];
	float [] DAE_TOTALEX=new float[2];
	float [] DAQ_TOTALQ=new float[2];
 	Map<String, Float> DAF=new HashMap<String, Float>();
	
	
	ArrayList<OWLNamedIndividual> Poste= new ArrayList<OWLNamedIndividual>();
	Poste.add(poste.asOWLNamedIndividual());
	ArrayList<OWLNamedIndividual> CV=new ArrayList<OWLNamedIndividual>();
	CV.add(cv.asOWLNamedIndividual());
	DAE_TOTALEX=exigenceAppariement(poste.asOWLNamedIndividual(), cv.asOWLNamedIndividual(), PoidsExperience, PoidsFormation, PoidsContrat);
	DAQ_TOTALQ=qualificationAppariement(poste.asOWLNamedIndividual(), cv.asOWLNamedIndividual(), PAge);
	switch(choix) {
	case 1:DAC_TOTALCOMP=CompetenceExact(Poste, CV);
	case 2 :
		
	}
	String id=getValueOfAssertionAxiom(cv, "Personne_id");
	
	DAF.put("Id",Float.parseFloat(id));
	DAF.put("DAC",DAC_TOTALCOMP[0]);
	DAF.put("TotalCompetence",DAC_TOTALCOMP[1]);
	DAF.put("DAE",DAE_TOTALEX[0]);
	DAF.put("TotalExigence", DAE_TOTALEX[1]);
	DAF.put("DAQ", DAQ_TOTALQ[0]);
	DAF.put("TotalQ", DAQ_TOTALQ[1]);
	float daf=(DAF.get("DAC")*DAF.get("TotalCompetence")+DAF.get("DAE")*DAF.get("TotalExigence")+DAF.get("DAQ")*DAF.get("TotalQ"))/(DAF.get("TotalCompetence")+DAF.get("TotalExigence")+DAF.get("TotalQ"));
	DAF.put("DAF",daf);
	
	
	return DAF;
	
}

public ArrayList<Map<String,Float>>  appariementForAllCV(String PosteCode,int choix ,int PoidsExperience,int PoidsFormation,int PoidsContrat,int PAge) {
	
	OWLNamedIndividual poste=df.getOWLNamedIndividual(IRI.create(uri_base+PosteCode));
	OWLClass Personne=df.getOWLClass(IRI.create(uri_base+"Personne"));
	ArrayList<OWLNamedIndividual> personnes=getAllIndividualsOfAClass(Personne);
	ArrayList<Map<String,Float>> resultats=new ArrayList<Map<String,Float>>(); 
	for(OWLNamedIndividual personne :personnes) {
		 
		resultats.add(appariementCvOffres(poste, personne, choix, PoidsExperience, PoidsFormation, PoidsContrat, PAge));
		
	}
	
	return resultats;
	
}

public OWLDataFactory getDf() {
	return df;
}




public void setDf(OWLDataFactory df) {
	this.df = df;
}




public ArrayList<DAF>  getDAFArray(ArrayList<Map<String,Float>> resultats){
	ArrayList<DAF> personnes=new ArrayList<DAF>();
	for(Map<String,Float> r:resultats) {
		DAF p=new DAF();
		p.setId(Math.round(r.get("Id")));
		p.setDaf(r.get("DAF"));
		p.setDac(r.get("DAC"));
		p.setTotcomp(r.get("TotalCompetence"));
		p.setDae(r.get("DAE"));
		p.setTotex(r.get("TotalExigence"));
		p.setDaq(r.get("DAQ"));
		p.setTotq(r.get("TotalQ"));
		personnes.add(p);
	
	}
	
	return personnes;
	
}

public ArrayList<DAF> triResultats(ArrayList<Map<String,Float>> resultats) {
	
	ArrayList<DAF> results=getDAFArray(resultats);
	int n=results.size();
    for (int i = 0; i < n-1; i++) 
        for (int j = 0; j < n-i-1; j++) 
            if (results.get(j).getDac() < results.get(j+1).getDac()) 
            { 
                
                DAF temp = results.get(j); 
                results.set(j, results.get(j+1)); 
                results.set(j+1, temp); 
            } 
	return results;
}
public void affichage_resultat(ArrayList<DAF> resultats) {
	System.out.format("%16s%16s%16s%16s%16s%16s%16s%16s", "Id", "DAF", "DAC","TOTAL COMPETENCE", "DAE" , "TOTAL EXIGENCE" , "DAQ" ,"TOTAL QUALIFICATION" );
	for(DAF resultat:resultats) {
		System.out.println("\n");
		System.out.format("%16d%16f%16f%16f%16f%16f%16f%16f", resultat.getId(), resultat.getDaf(), resultat.getDac(),resultat.getTotcomp(),resultat.getDae(),resultat.getTotex(),resultat.getDaq(),resultat.getTotq());
	}
	
}
	public static void main(String [] argv) throws Exception {
		String uri_base="http://www.semanticweb.org/kakaroto/ontologies/2019/3/newVersion#";
		String uri_competence="http://www.semanticweb.org/kakaroto/ontologies/2019/3/Competence#";
		String path="/home/kakaroto/Pfe/tp/test/";
		String filename="/home/kakaroto/object.txt";
		String profil_filename="/home/kakaroto/pfeprofil.txt";
		ArrayList<String> stopWords=Nlp.readStopWord("/home/kakaroto/open_nlp/models/stopwords/stopwords.txt");
		
		OntologieManipulation app=new OntologieManipulation(uri_base,path,uri_competence,stopWords);
		/*ArrayList<Map<String,Float>> resultats=app.appariementForAllCV("685", 1, 10, 30, 5, 5);
		ArrayList<DAF> resultats_trie=app.triResultats(resultats);
		app.affichage_resultat(resultats_trie);*/
		//app.convertToComp(m_ns);
		
		/*Map<String, Float> DAF;
		DAF=app.appariementCvOffres("843", "1052739", 1,10,20,10,20);
		System.out.println("DAF: "+DAF );*/
		OWLNamedIndividual cv=app.df.getOWLNamedIndividual(IRI.create(uri_competence+""));
		OWLClass cls=app.df.getOWLClass(IRI.create(uri_competence+"Manifestation"));
		//OWLNamedIndividual poste=app.df.getOWLNamedIndividual(IRI.create(uri_base+"directeur des ressources humaines/directrice des ressources humaines -DRH-".replace(" ", "_")));
		/*float [] result=app.exigenceAppariement(poste, cv, 5, 20, 2);
		System.out.println("Total: " + result[1] +" pexig : "+ result[0]);*/
				//app.ExtractExigence(cv);
		app.insertIndividuals(filename);
		app.inserProfilsIntoOntology(profil_filename);
		//app.relatePersonWithOneMan();
		app.relatePersonWithMan();
		
		System.out.println("saved");
	}
}
