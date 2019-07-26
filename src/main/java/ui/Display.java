package ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import com.itextpdf.layout.element.Table;
import com.snteam.competence.App;
import com.snteam.competence.Nlp;
import com.snteam.competence.OntologieManipulation;

import FuzzyCompetence.Candidat;
import FuzzyCompetence.FuzzyCompetence;
import FuzzyCompetence.IdealCandidat;
import FuzzyCompetence.Personne;
import fuzzyevaluation.Term;
import model.Competence;
import model.DAF;
import model.Profil;

public class Display {

	
	public  static Object [][] convertArrayToObjectData(ArrayList<DAF> dafs) {
		Object [][] data=new Object[dafs.size()][8];
		for(int i=0;i<dafs.size()-1;i++) {
			
			data[i][0]=dafs.get(i).getId();
			data[i][1]=dafs.get(i).getDaf();
			data[i][2]=dafs.get(i).getDac();
			data[i][3]=dafs.get(i).getTotcomp();
			data[i][4]=dafs.get(i).getDae();
			data[i][5]=dafs.get(i).getTotex();
			data[i][6]=dafs.get(i).getDaq();
			data[i][7]=dafs.get(i).getTotq();
			
		}

	return data;
	
	}
	
	public  static Object [][] convertPersonneToObjectData(ArrayList<Personne> dafs) {
		Object [][] data=new Object[dafs.size()][2];
		for(int i=0;i<dafs.size();i++) {
			
			data[i][0]=dafs.get(i).getId();
			data[i][1]=dafs.get(i).getDistance();
			
			
		}

	return data;
	
	}
	public  static Object [][] convertArrayDacToObjectData(ArrayList<DAF> dafs) {
		
		
		Object [][] data=new Object[dafs.size()][2];
		for(int i=0;i<dafs.size()-1;i++) {
			
			data[i][0]=dafs.get(i).getId();
			data[i][1]=dafs.get(i).getDac();
			
			
		}

	return data;
	
	}
	
	
	public static Object [][] convertMapToObject(ArrayList<fuzzyevaluation.Candidat> candidats){
		
		Object [][] data=new Object[candidats.size()][3];
		for(int i=0;i<candidats.size()-1;i++) {
			
			data[i][0]=candidats.get(i).getId();
			 for(Map.Entry<Term, Double> value : candidats.get(i).getTerm_distance().entrySet()) {
				 
				 
			 }
			
			
		}

	return data;
		
		
	}
	public static Object [][] convertHashMapToObjectData(Map<Candidat,Double> result){
		
		Object [][] data=new Object[result.size()][2];
		 int i=0;
		for(Map.Entry<Candidat,Double> entry : result.entrySet()) {
			
			data[i][0]=entry.getKey().getId();
			data[i][1]=entry.getValue();
			i++;
			
		}
		
		
		return data;
		
	}
	public void displayProfilPersonne() throws Exception, IOException {

		String uri_base="http://www.semanticweb.org/kakaroto/ontologies/2019/3/newVersion#";
		String uri_competence="http://www.semanticweb.org/kakaroto/ontologies/2019/3/Competence#";
		String path="/home/kakaroto/Pfe/tp/test/";
		String filename="/home/kakaroto/object.txt";
		String profil_filename="/home/kakaroto/pfeprofil.txt";
		ArrayList<String> stopWords=Nlp.readStopWord("/home/kakaroto/open_nlp/models/stopwords/stopwords.txt");
		
		OntologieManipulation app=new OntologieManipulation(uri_base,path,uri_competence,stopWords);
		
		long start = System.nanoTime();

		
		long duree = System.nanoTime() - start;
		System.out.println("duree"+duree);
		
		app.appariementUtilisantPalmer("941");

	      final JFrame frame = new JFrame("Appariement Affichage");
	 
	        String[] columns = {"Id", "similarite totale"};
	        
	        Object[][] data=Display.convertPersonneToObjectData(app.app_palmer);
	 
	        JTable table = new JTable(data, columns);
	        JScrollPane scrollPane = new JScrollPane(table);
	        table.setFillsViewportHeight(true);
	 
	        JLabel lblHeading = new JLabel("Appariement CV/Offre");
	        lblHeading.setFont(new Font("Arial",Font.TRUETYPE_FONT,24));
	 
	        frame.getContentPane().setLayout(new BorderLayout());
	 
	        frame.getContentPane().add(lblHeading,BorderLayout.PAGE_START);
	        frame.getContentPane().add(scrollPane,BorderLayout.CENTER);
	 
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setSize(550, 200);
	        frame.setVisible(true);
		
		
		
		
		
	}
	public  static Object [][] convertArrayCToObjectData(ArrayList<Candidat> dafs) {
		Object [][] data=new Object[dafs.size()][dafs.get(0).getCompetenceSet().size()];

		
		for(int i=0;i<dafs.size()-1;i++) {
			
			data[i][0]=dafs.get(i).getId();
			
			for(int j=1;j<dafs.get(i).getCompetenceSet().size()-1;j++) {
				data[i][j]=dafs.get(i).getCompetenceSet().get(j).getNiveau();
			}
			
			
		}

	return data;
	
	}
	
	public static Map<String,ArrayList<Competence>> convertArrayCandidatToHashMap(ArrayList<Competence> comps ) throws Exception {
		
		ArrayList<Competence> savoir=new ArrayList<Competence>();
		ArrayList<Competence> savoir_faire=new ArrayList<Competence>();
		
		for(Competence comp : comps) {
			if(comp.getManifestation().contains("negocier des contrats de travail")) savoir_faire.add(comp);
			else if(Nlp.POSTagging(comp.getManifestation())[0].equals("VINF")) {
				//	System.out.println("i m VINF");
					savoir_faire.add(comp);
				}else {
					//System.out.println("i m not VINF");
					savoir.add(comp);
				}
		}
		
		Map<String,ArrayList<Competence>> result=new HashMap<String, ArrayList<Competence>>();
		result.put("savoir", savoir);
		result.put("savoir_faire", savoir_faire);
		return result;
		
	}
	public static Object [][] convertCandidatsToObject(ArrayList<Candidat> candidats) throws Exception {
		
		Object [][] data=new Object[candidats.size()*20][4];
	   
		
		int counter=0;
		for(Candidat candidat : candidats) {
			
			
			
			
			ArrayList<Competence> competences=candidat.getCompetenceSet();
			Map<String,ArrayList<Competence>> map_comp=convertArrayCandidatToHashMap(competences);
			 for(Map.Entry<String, ArrayList<Competence>> entry :  map_comp.entrySet()) {
				 
				  ArrayList<Competence> comps=entry.getValue();
				  for(Competence c : comps) {
					  data[counter][0]=candidat.getId();
					  data[counter][1]=entry.getKey();
					  data[counter][2]=c.getManifestation();
					  data[counter][3]=c.getNiveau();
					  counter++;
				  }
				  
			 }
			 
			
		}
		
		return data;
	}
public static Object [][] convertICandidatsToObject(IdealCandidat candidat) throws Exception {
		
		Object [][] data=new Object[20][4];
	   
		
		int counter=0;
		
			ArrayList<Competence> competences=candidat.getCompetenceSet();
			Map<String,ArrayList<Competence>> map_comp=convertArrayCandidatToHashMap(competences);
			 for(Map.Entry<String, ArrayList<Competence>> entry :  map_comp.entrySet()) {
				 
				  ArrayList<Competence> comps=entry.getValue();
				  for(Competence c : comps) {
					  data[counter][0]=candidat.getId();
					  data[counter][1]=entry.getKey();
					  data[counter][2]=c.getManifestation();
					  data[counter][3]=c.getNiveau();
					  counter++;
				  }
				  
			 }
			 
			
		
		
		return data;
	}
	
	
	public static void printPDF(JTable table) throws Exception {
			MessageFormat header=new MessageFormat("liste des profils");
			MessageFormat footer=new MessageFormat("****");
			table.print(JTable.PrintMode.NORMAL, header,footer);
	}
	public void displayProfil() throws Exception, IOException {
		FuzzyCompetence fc=new FuzzyCompetence();
		
      /*  long start = System.nanoTime();
          fc.getDataFromOntology("230",fc.candidats);
		  fc.SortCompetenceLikeIdealCandidat();
		  fc.fillFuzzySetForIdealCandidat();
		  fc.fillFuzzySetForCandidats();
		  fc.distanceBetweenIdealAndCandidat();
		  fc.getValuesOfMSF();
		
		long duree = System.nanoTime() - start;
		System.out.println("duree"+duree);*/
		  fc.getDataFromOntology("712",fc.candidats);
		  fc.SortCompetenceLikeIdealCandidat();
		  fc.fillFuzzySetForIdealCandidat();
		  fc.fillFuzzySetForCandidats();
		  long start = System.nanoTime();
		  fc.distanceBetweenIdealAndCandidat();
		  long duree_ham = System.nanoTime() - start;
			System.out.println("duree hamming "+duree_ham);
			long start_2 = System.nanoTime();
		  fc.getValuesOfMSF();
		  long duree = System.nanoTime() - start;
			System.out.println("duree indice de competence "+duree);
		  HashMap<Candidat,Double> ham=fc.SortMapByDistance(fc.competenceIndex,1);
		  
	      
	 
	        String[] columns = {"Id", "indice de competence"};
	        
	        Object[][] data=Display.convertHashMapToObjectData(ham);
		  final JFrame frame = new JFrame("Appariement Affichage");
		  
	       //Object [][] data=Display.convertCandidatsToObject(fc.candidats);
		  //Object [][] data=Display.convertICandidatsToObject(fc.Icandidat);
	      // String[] columns = {"Id", "competence","manifestation","niveau"};
	        JTable table = new JTable(data, columns);
	       //printPDF(table);
	        JScrollPane scrollPane = new JScrollPane(table);
	        table.setFillsViewportHeight(true);
	 
	        JLabel lblHeading = new JLabel("Appariement CV/Offre");
	        lblHeading.setFont(new Font("Arial",Font.TRUETYPE_FONT,24));
	 
	        frame.getContentPane().setLayout(new BorderLayout());
	 
	        frame.getContentPane().add(lblHeading,BorderLayout.PAGE_START);
	        frame.getContentPane().add(scrollPane,BorderLayout.CENTER);
	 
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setSize(550, 200);
	        frame.setVisible(true);
		
		
		
		
		
	}

	 public static void main(String[] args) throws Exception {
		String uri_base="http://www.semanticweb.org/kakaroto/ontologies/2019/3/newVersion#";
			String uri_competence="http://www.semanticweb.org/kakaroto/ontologies/2019/3/Competence#";
			String path="/home/kakaroto/Pfe/re/";
			String filename="/home/kakaroto/object.txt";
			String profil_filename="/home/kakaroto/profiles.txt";
			ArrayList<String> stopWords=Nlp.readStopWord("/home/kakaroto/open_nlp/models/stopwords/stopwords.txt");
			
			OntologieManipulation app=new OntologieManipulation(uri_base,path,uri_competence,stopWords);
			long start = System.nanoTime();
			ArrayList<Map<String,Float>> resultats=app.appariementForAllCV("712", 1, 10, 30, 5, 5);
			long duree = System.nanoTime() - start;
			System.out.println("duree"+duree);
			ArrayList<DAF> resultats_trie=app.triResultats(resultats);
			
		 
	      final JFrame frame = new JFrame("Appariement Affichage");
	 
	      /*  String[] columns = {"Id", "DAF", "DAC", "TOTAL COMPETENCE",
	                            "DAE", "TOTAL EXIGENCE", "DAQ","TOTAL QUALIFICATION"};*/
	      String[] columns = {"Id","DAC"};
	        
	        Object[][] data=Display.convertArrayDacToObjectData(resultats_trie);
	 
	        JTable table = new JTable(data, columns);
	        JScrollPane scrollPane = new JScrollPane(table);
	        table.setFillsViewportHeight(true);
	 
	        JLabel lblHeading = new JLabel("Appariement CV/Offre");
	        lblHeading.setFont(new Font("Arial",Font.TRUETYPE_FONT,24));
	 
	        frame.getContentPane().setLayout(new BorderLayout());
	 
	        frame.getContentPane().add(lblHeading,BorderLayout.PAGE_START);
	        frame.getContentPane().add(scrollPane,BorderLayout.CENTER);
	 
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setSize(550, 200);
	        frame.setVisible(true);
		 	/*Display display=new Display();
		 	display.displayProfil();*/
			
			
}}
