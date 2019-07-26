package com.snteam.competence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import model.Occupation;

/**
 * Hello world!
 *
 */
public class App 
{			
	public static int counter=0;
	
	
	public static void  createJsonFileFromApi(String api,String  filename) throws IOException, ParseException {
		JSONParser parser=new JSONParser();
		FileWriter file=new FileWriter(filename);
			URL es=new URL(api);
			URLConnection esCon = es.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(esCon.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				Object p = parser.parse(line);
				JSONObject jo = (JSONObject) p;
				JSONObject embedded = (JSONObject) jo.get("_embedded");
				JSONArray arr = (JSONArray) embedded.get("results");
				file.write(arr.toJSONString());
				file.flush();
			
			}
		file.close();
	
	}
	
	public static String getTypeOfSkill(String uri) throws IOException, ParseException {
			String base="https://ec.europa.eu/esco/api/resource/skill?uri=";
			String full_url=base+uri;
			String type="";
			JSONParser parser=new JSONParser();
			URL url=new URL(full_url);
			URLConnection url_con=url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(url_con.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				Object p = parser.parse(line);
				JSONObject jo = (JSONObject) p;
				JSONObject links = (JSONObject) jo.get("_links");
				JSONArray skill_type=(JSONArray) links.get("hasSkillType");
				for(Object o : skill_type) {
					JSONObject jp=(JSONObject)o;
					type=(String) jp.get("title");
				}
				
				
				
		
			}	
			
		return type;
	}
	public static String getFather(String uri) throws IOException, ParseException {
		String base="https://ec.europa.eu/esco/api/resource/skill?uri=";
		String full_url=base+uri;
		String type="";
		JSONParser parser=new JSONParser();
		URL url=new URL(full_url);
		URLConnection url_con=url.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(url_con.getInputStream()));
		String line;
		while ((line = br.readLine()) != null) {
			Object p = parser.parse(line);
			JSONObject jo = (JSONObject) p;
			JSONObject links = (JSONObject) jo.get("_links");
			JSONArray skill_type=(JSONArray) links.get("broaderSkills");
			for(Object o : skill_type) {
				JSONObject jp=(JSONObject)o;
				type=(String) jp.get("title");
			}
			
			
			
	
		}	
		
	return type;
}
	
	public static  Vector<Occupation> readJsonFileAndExtract(String filename) throws ParseException, IOException {
		  
		Vector<Occupation> occs=new Vector<Occupation>();
		
		JSONParser parser=new JSONParser();
        Object obj = parser.parse(new InputStreamReader(new FileInputStream(filename)));
        JSONArray arr = (JSONArray) obj;
       // System.out.println(arr);
        Vector<String> savoir=new Vector<String>();
    	Vector<String> savoir_faire=new Vector<String>();
        for(Object o:arr) {
        	Occupation occ=new Occupation();
        	JSONObject data = (JSONObject)o;
        	
        	JSONObject links=(JSONObject) data.get("_links");
        	JSONArray arr1 =(JSONArray)links.get("hasEssentialSkill");
        	System.out.println("essential skills :"+arr1);
        	occ.setNom((String)data.get("title"));
        	System.out.println("nom :"+(String)data.get("title"));
        	JSONObject des=(JSONObject)data.get("description");

        	JSONObject en=(JSONObject)des.get("en");
        	occ.setDescritption((String)en.get("literal"));
        	System.out.println("description :"+(String)en.get("literal"));
        	for(Object or : arr1) {
        		
        		JSONObject op=(JSONObject)or;
        		System.out.println("op: "+getTypeOfSkill((String)op.get("uri")));
        		
        		if(getTypeOfSkill((String)op.get("uri")).equals("knowledge")) {
        			savoir.add((String)op.get("title"));
        			
        			
        		}else {
        			savoir_faire.add((String)op.get("title"));
        			
        		}
        		
        	}
        	occ.setSavoir(savoir);
        	occ.setSavoir_faire(savoir_faire);
        	occs.add(occ);
        }
      
			return occs;
		
	}
	public static <T> void serializeArray(String filename,Vector<T> occs) throws ParseException, IOException {
		 
		 FileOutputStream f = new FileOutputStream(new File(filename));
			ObjectOutputStream o = new ObjectOutputStream(f);
			
			for(T occ:occs) {
				o.writeObject(occ);
			}
		 
		 
		 
	}
	@SuppressWarnings("unchecked")
	public static  <T> ArrayList<T> readObjectFromTxt(String filename) throws IOException, ClassNotFoundException {
		FileInputStream fi = new FileInputStream(new File(filename));
		ObjectInputStream oi = new ObjectInputStream(fi);
		ArrayList<T> arr=new ArrayList<T>();
		
		
		
		while(fi.available()>0) {
			
			T o=(T)oi.readObject();
			arr.add(o);
			
		}
		return (ArrayList<T>) arr;
	}
	
	public static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
    public static void main( String[] args ) throws IOException, ParseException, ClassNotFoundException
    {
    	String api="https://ec.europa.eu/esco/api/search?text=ressources&language=fr&type=occupation&type=concept&facet=type&facet=isInScheme&limit=2&offset=2&full=true";
        String filename="/home/kakaroto/data.json";
        String file="/home/kakaroto/object.txt";
       // createJsonFileFromApi(api,filename);
       // Vector<Occupation> occs=readJsonFileAndExtract(filename);
    	//serializeArray(file,occs);
    	
       // readJsonFileAndExtract(filename);
       //System.out.println("i m here :"+readJsonFileAndExtract(filename).get(0).getSavoir());
       // System.out.println(getTypeOfSkill("http://data.europa.eu/esco/skill/a59708e3-e654-4e37-8b8a-741c3b756eee"));
      // System.out.println(readObjectFromTxt("/home/kakaroto/object.txt"));
       ArrayList<Occupation> arr=new ArrayList<Occupation>();
		arr=App.readObjectFromTxt(file);
		System.out.println("occupation"+arr);
		for(Occupation occ:arr) {
			System.out.println(occ.getSavoir());
			System.out.println(occ.getSavoir_faire());
			}
		
    	System.out.println("ended");
    }
}
