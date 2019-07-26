package com.snteam.competence;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.Span;

public class Nlp {

	
	
	public static  String [] POSTagging(String sentence) throws Exception 
	
	{	
		InputStream inputStream = new FileInputStream("/home/kakaroto/open_nlp/models/fr-pos.bin"); 
		POSModel model = new POSModel(inputStream); 
		POSTaggerME tagger = new POSTaggerME(model);
		WhitespaceTokenizer whitespaceTokenizer= WhitespaceTokenizer.INSTANCE; 
		String[] tokens = whitespaceTokenizer.tokenize(sentence); 
		String[] tags = tagger.tag(tokens); 
		POSSample sample = new POSSample(tokens, tags); 
	
		//System.out.println(sample);
		
		
		return tags;
		
		
	}
	

	
	public static ArrayList<String> readStopWord(String filename) {
		ArrayList<String> stopWords=new ArrayList<String>();
		BufferedReader br;
		
		try
		{
			br = new BufferedReader(new FileReader(filename));
			String line=br.readLine();
			
			while(line!=null) {
				stopWords.add(line);
				line=br.readLine();
			}
			
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
			
		}

		return stopWords;
		
		
		
	}
	public static boolean isStopword(String word, ArrayList<String> stopWords) {
		
		
		if(word.length() < 2) return true;
		if(word.charAt(0) >= '0' && word.charAt(0) <= '9') return true;
		//System.out.println("contain:"+word);
		if(stopWords.contains(word)) {
			//System.out.println("nana:"+word);
			return true;
		}
		else return false;
}
	public static String removeStopWords(String string ,ArrayList<String> stopWords) {
		String result = "";
		String[] words = string.split("\\s+");
		
		for(String word : words) {
			//System.out.println("word: "+word);
			if(word.isEmpty()) continue;
			if(isStopword(word,stopWords)) {
				//System.out.println("contine" + word);
				continue; //remove stopwords
			}
			result += (word+" ");
		}
		return result;
}
	public static void NER(String sentence) throws Exception
	{
		SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
	    String[] tokens = tokenizer.tokenize(sentence);
	   // System.out.println(tokens[0]);
		InputStream inputStreamNameFinder = new FileInputStream("/home/kakaroto/open_nlp/models/fr-ner-location.bin");
		
		TokenNameFinderModel model = new TokenNameFinderModel(inputStreamNameFinder);
		
		NameFinderME nameFinder = new NameFinderME(model);
		List<Span> spans = Arrays.asList(nameFinder.find(tokens));
		
		//System.out.println(spans.toString());
	}
	
	public static ArrayList<Integer>   extractNumbers(String sentence) {
		Pattern p = Pattern.compile("-?\\d+");
		Matcher m = p.matcher(sentence);
		ArrayList<Integer> numbers=new ArrayList<Integer>();
		while (m.find()) {
		 // System.out.println(m.group());
		  numbers.add(Integer.parseInt(m.group()));
		}
		return numbers;
	}
	
	
	public static void main(String[] argv)  throws Exception{
		
		//Nlp.POSTagging("Niveau d'études : Bac+5 et plus");
		//ArrayList<String> stopWords=readStopWord("/home/kakaroto/open_nlp/models/stopwords/stopwords.txt");
		//String result=Nlp.removeStopWords("Niveau d'études : Bac+5 et plus",stopWords);
		//System.out.println("result: "+  result);
		
		extractNumbers("Niveau d'expérience :\n" +"Débutant < 2 ans");
	}
}
