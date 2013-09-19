package indexer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.collections15.Bag;
import org.apache.commons.collections15.bag.HashBag;
import org.apache.commons.math.linear.OpenMapRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import tokenizer.Label;
import tokenizer.Token;
import twitter.MyUser;
import util.Util;

public class UserVectors {

	private Map<Integer, String> wordIdValueMap = new HashMap<Integer, String>();
	private Map<Integer, String> documentIdNameMap = new HashMap<Integer, String>();
	private RealMatrix matrix;

	public UserVectors (List<MyUser> users) throws Exception {
		StopWatch stopWatch = new Log4JStopWatch("occurrenceMatrix");
		generateVector(users);
		stopWatch.stop();
	}
	
	/**
	 * 
	 * @param users
	 * @throws Exception
	 */
	private void generateVector(List<MyUser> users) throws Exception {

		Map<String, Bag<String>> documentWordFrequencyMap = new HashMap<String, Bag<String>>();
		SortedSet<String> wordSet = new TreeSet<String>();

		for (int docId = 0; docId < users.size(); docId++) {

			MyUser myUser = users.get(docId);
			// String text = myUser.generateUserStr();
			String screenName = myUser.getScreenName();

			Bag<String> wordFrequencies = myUser.getTermBag(); // getWordFrequenciesNew(text);
			wordSet.addAll(wordFrequencies.uniqueSet());
			documentWordFrequencyMap.put(screenName, wordFrequencies);
			
			documentIdNameMap.put(docId, screenName);
		}

		// create a Map of ids to words from the wordSet
		int wordId = 0;
		for (String word : wordSet) {
			wordIdValueMap.put(wordId, word);
			wordId++;
		}

		/*
		for(Map.Entry<Integer, String> e : wordIdValueMap.entrySet()) {
			if (e.getValue().startsWith("#")) {
				System.out.println(e.getKey() + ": " + e.getValue());
			}
		}
		*/
		
		// we need a documents.keySet().size() x wordSet.size() matrix to hold
		// this info
		int numDocs = users.size();
		int numWords = wordSet.size();
		matrix = new OpenMapRealMatrix(numWords, numDocs);
		for (int i = 0; i < matrix.getRowDimension(); i++) {
			for (int j = 0; j < matrix.getColumnDimension(); j++) {
				String docName = documentIdNameMap.get(j);
				Bag<String> wordFrequencies = documentWordFrequencyMap.get(docName);
				String word = wordIdValueMap.get(i);
				int count = wordFrequencies.getCount(word);
				matrix.setEntry(i, j, count);
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public String[] getDocumentNames() {
		
		String[] documentNames = new String[documentIdNameMap.keySet().size()];
		
		for (int i = 0; i < documentNames.length; i++) {
			documentNames[i] = documentIdNameMap.get(i);
		}
		
		return documentNames;
	}

	/**
	 * 
	 * @return
	 */
	public String[] getWords() {
		
		String[] words = new String[wordIdValueMap.keySet().size()];
		
		for (int i = 0; i < words.length; i++) {
			
			String word = wordIdValueMap.get(i);
			if (word.contains("|||")) {
				// phrases are stored with length for other purposes, strip it off for this report.
				word = word.substring(0, word.indexOf("|||"));
			}
			words[i] = word;
		}
		return words;
	}

	private Bag<String> getWordFrequenciesNew(String text) throws Exception {
		
		Bag<String> wordBag = new HashBag<String>();
		
		for (String string : text.split("\\s")) {
			wordBag.add(string);
		}
		
		return wordBag;
		
	}
	
	
	/**
	 * 
	 * @param text
	 * @return
	 * @throws Exception
	 */
	private Bag<String> getWordFrequencies(String text) throws Exception {

		Bag<String> wordBag = new HashBag<String>();

		List<Token> labeledTokens = Util.toLabeledTokens(text);
		
		String wordToIndex = null;
		for (Token labeledToken : labeledTokens) {
			
			if ( eligible(labeledToken) ) {
				
				wordToIndex = labeledToken.getValue();
				/*
				if(labeledToken.getLabel() == Label.HASHTAG) {
					wordToIndex = wordToIndex.substring(1);
				}
				*/
				wordBag.add(Util.lowerCase(wordToIndex));
			}
		}

		return wordBag;
	}

	/**
	 * 
	 * @param labeledToken
	 * @return
	 */
	private boolean eligible(Token labeledToken) {
		return (labeledToken.getLabel() == Label.WORD || labeledToken.getLabel() == Label.HASHTAG)
				&& 
			   !labeledToken.getValue().equals("");
	}
	
	/**
	 * 
	 * @return
	 */
	public RealMatrix getMatrix() {
		return matrix;
	}
}
