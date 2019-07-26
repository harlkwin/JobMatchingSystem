package com.snteam.competence;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import org.json.simple.parser.ParseException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import model.Experience;
import model.Formation;
import model.Langue;
import model.Other;
import model.Profil;

public class Scrapper {

	private WebClient client;
	private Vector<Profil> profils;
	static int counter = 0;

	public void initConfig() {
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);

	}

	public HtmlPage extractPage(String url) {
		HtmlPage page = null;
		try {
			page = client.getPage(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page;
	}

	public Experience extractExperience(HtmlPage page) {
		Experience experience = new Experience();
		List<HtmlElement> items = (List<HtmlElement>) page
				.getByXPath("//div[@class='candidate-professional-experience']");
		for (HtmlElement item : items) {

			HtmlElement fields = item.getFirstByXPath(".//div[@class='field-items']");
			HtmlElement niveau_d_experience = item.getFirstByXPath(".//div[@class='field-item']");
			experience.setNiveau_experience(niveau_d_experience.asText());
			List<HtmlElement> exps = (List<HtmlElement>) fields.getByXPath(".//div[@class='industries-item']  ");
			ArrayList<String> description = new ArrayList<String>();
			for (HtmlElement exp : exps) {

				description.add(exp.asText());

				/*
				 * System.out.println("description: "+experience.getDescription());
				 * System.out.println("niveau: "+experience.getNiveau_experience());
				 */

			}
			if (description.size() == 0) {
				description.add("non affecte");
			}
			experience.setDescription(description);

		}

		return experience;

	}

	public boolean checkIfExistsInArray(ArrayList<Langue> array, String value) {
		boolean exist = false;
		for (Langue lng : array) {
			if (lng.getNom().equals(value)) {
				exist = true;
			}
		}
		return exist;
	}

	public Other extractOther(HtmlPage page) {

		Other others = new Other();
		HtmlElement items = (HtmlElement) page.getFirstByXPath("//div[@class='candidate-more-info']");
		
		
			
			HtmlElement fields = items.getFirstByXPath(".//div[@class='candidate-more-info']");
			
			HtmlElement fmrs = (HtmlElement) fields.getFirstByXPath(".//div[@class='field-items']");
			List<HtmlElement> fms=(List<HtmlElement>) page.getByXPath("//div[@class='field-item']");
			
			for (HtmlElement fm : fms) {
				System.out.println("fm:" + fm.asText());
				HtmlElement type = fm.getFirstByXPath(".//span[@class='job-type-items']");
				
				if (type != null) {
					others.setType_contract(type.asText());
				/*	System.out.println("***************************");
					System.out.println("type:" + type.asText());
					System.out.println("***************************");*/
				} else {
					others.setType_contract("non affecte");
				}

			}

		
		return others;
	}

	public ArrayList<Langue> extractLangue(HtmlPage page) {

		ArrayList<Langue> langues = new ArrayList<Langue>();
		List<HtmlElement> items = (List<HtmlElement>) page.getByXPath("//div[@class='candidate-languages']");
		for (HtmlElement item : items) {

			HtmlElement fields = item.getFirstByXPath(".//div[@class='field-items']");
			List<HtmlElement> lngs = (List<HtmlElement>) fields
					.getByXPath(".//div[@class='field-item even'] | .//div[@class='field-item odd'] ");
			// System.out.println("size:"+lngs.size());
			for (HtmlElement lng : lngs) {
				Langue lang = new Langue();
				String[] values = lng.asText().split("›");
				String nom = values[0];
				String niveau = "";
				if(values.length!=2) {
					niveau="courante";
				}else {
					niveau=values[1];
				}
				
				lang.setNom(nom);
				lang.setNiveau(niveau);
				if (checkIfExistsInArray(langues, lang.getNom())) {
					// System.out.println("im here ");

				} else {
					langues.add(lang);
				}

				// System.out.println("langues:"+ langues);
			}

		}

		return langues;

	}

	public ArrayList<Formation> extractFormation(HtmlPage page) {

		ArrayList<Formation> formations = new ArrayList<Formation>();
		@SuppressWarnings("unchecked")
		List<HtmlElement> items = (List<HtmlElement>) page.getByXPath("//div[@class='candidate-education-info']");
		for (HtmlElement item : items) {

			HtmlElement niveau_d_etude = item.getFirstByXPath(".//div[@class='field-item']");
			HtmlElement fields = item.getFirstByXPath(".//div[@class='field-items']");
			@SuppressWarnings("unchecked")
			List<HtmlElement> fms = (List<HtmlElement>) fields
					.getByXPath(".//div[@class='field-item even'] | .//div[@class='field-item odd'] ");

			System.out.println(niveau_d_etude.asText());
			for (HtmlElement fm : fms) {
				Formation form = new Formation();
				HtmlElement period = fm.getFirstByXPath(".//div[@class='period']");
				HtmlElement etablissement = fm.getFirstByXPath(".//div[@class='establishment']");
				HtmlElement nom = fm.getFirstByXPath(".//div[@class='title']");
				HtmlElement description = fm.getFirstByXPath(".//div[@class='description']");
				if(period!=null) {
					form.setPeriod(period.asText());
				}else {
					form.setPeriod("0");;
				}
				
				if(etablissement!=null) {
					form.setEtablissement(etablissement.asText());
				}else {
					form.setEtablissement("non affecte");
				}
				
				form.setF_nom(nom.asText());
				if (description != null) {
					form.setDescription(description.asText());
				} else {
					form.setDescription("non affecte");
				}

				form.setNiveau_d_etude(niveau_d_etude.asText());
				formations.add(form);
				
				 
				 
				// System.out.println("*********");
			}

		}

		return formations;

	}

	public Profil getProfil(HtmlPage page) {
		Profil profile = new Profil();
		ArrayList<Formation> formations = extractFormation(page);
		ArrayList<Langue> langues = extractLangue(page);
		Other other=extractOther(page);
		Experience e = extractExperience(page);
		profile.setExperience(e);
		profile.setLangues(langues);
		profile.setFormations(formations);
		profile.setOther(other);
		return profile;
	}

	public ArrayList<String> getUrls(HtmlPage page) {
		DomElement de = page.getElementById("content");
		HtmlElement de1 = (HtmlElement) de.getFirstByXPath(".//div[@class='content-inner inner']");
		ArrayList<String> urls = new ArrayList<String>();

		List<HtmlElement> cvs = ((List<HtmlElement>) de1.getByXPath(".//div[@class='content']"));
		for (HtmlElement cv : cvs) {

			List<HtmlElement> profils = ((List<HtmlElement>) cv.getByXPath(".//div[@class='cv-description-wrapper']"));
			System.out.println("size: " + profils.size());
			for (HtmlElement profil : profils) {
				/*
				 * System.out.println("profil  :****************");
				 * System.out.println(profil.asText());
				 * System.out.println("profil  :****************");
				 */
				String url = profil.getAttribute("data-href");
				urls.add(url);

			}

		}

		return urls;
	}

	public void scrapProfiles(String start) throws ParseException, IOException {
		String pa = "page";
		String beg = start + "";
		while (counter < 20 ) {
			HtmlPage pag = null;
			if (counter == 0) {
				pag = extractPage(String.format("%s", start));
				System.out.println("i m page 1 ***********************************");
			} else {
				//System.out.println("value:" + String.format("%s?%s=%d", start, pa, counter));
				System.out.println("i m more than one page***********************************");
				pag = extractPage(String.format("%s?%s=%d", start, pa, counter));
			}

			ArrayList<String> urls = getUrls(pag);
			System.out.println("counter: " + counter);
			for (String url : urls) {
				HtmlPage page = extractPage(url);
				HtmlElement numero = (HtmlElement) page.getFirstByXPath("//h1[@class='title']");
				// System.out.println("numero de profil :"+numero.asText().replace("Profil CV N°", ""));
				Profil p = getProfil(page);
				p.setNumero_cv(numero.asText().replace("Profil CV N°", ""));
				profils.add(p);
			}

			counter++;

		}

		App.serializeArray("/home/kakaroto/pfeprofil.txt", profils);
	}

	public Vector<Profil> getProfils() {
		return profils;
	}

	public void setProfils(Vector<Profil> profils) {
		this.profils = profils;
	}

	public static int getCounter() {
		return counter;
	}

	public static void setCounter(int counter) {
		Scrapper.counter = counter;
	}

	public WebClient getClient() {
		return client;
	}

	public void setClient(WebClient client) {
		this.client = client;
	}

	public static void main(String[] argv) throws ParseException, IOException, ClassNotFoundException {
		WebClient wc = new WebClient();
		Scrapper sc = new Scrapper();
		sc.setClient(wc);
		Vector<Profil> profils = new Vector<Profil>();
		sc.initConfig();
		sc.setProfils(profils);
	/*	HtmlPage page=sc.extractPage("https://www.emploi.ma/recrutement-maroc-cv/4311567");
		ArrayList<Formation> formations=sc.extractFormation(page);
		for(Formation d:formations) {
			System.out.println("formation: ");
			System.out.println("nom : "+d.getF_nom());
			System.out.println("period : "+d.getPeriod());
			 System.out.println("etablissement : "+d.getEtablissement());
			 
			 
		}*/
		
	//	Other o=sc.extractOther(page);
		//System.out.println("type de contract : "+ o.getType_contract());
		// ArrayList<Langue> e=sc.extractLangue(page);
		// ArrayList<String> url=sc.getUrls(page);
		// System.out.println("urls : "+url);
		//sc.scrapProfiles("https://www.emploi.ma/recherche-base-donnees-cv/ressources%2520humaines");
		sc.scrapProfiles("https://www.emploi.ma/recherche-base-donnees-cv/ressources%2520humaines");
		//System.out.println(sc.getProfils());
		ArrayList<Profil> pro = App.readObjectFromTxt("/home/kakaroto/pfeprofil.txt");
		int k=0;
		for (Profil p : pro) {
			System.out.println("numero cv : " + p.getNumero_cv());
			//System.out.println("experience: " + p.getExperience().getDescription());
			k++;
		}
		System.out.println("k " +k);
	}

}
