package org.dreambig.swearwords;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/***
 * Singletone class to find SwearWords out of sentences.
 * 
 * @author Npatodi
 *
 */

public class SwearWordsFilter {

	public static final SwearWordsFilter instance = new SwearWordsFilter();

	private final CopyOnWriteArrayList<String> swearWords;

	private SwearWordsFilter() {
		try {
			// Get file from resources folder
			List<String> words = new ArrayList<>();
			String line = null;
			try (BufferedReader br = new BufferedReader(new InputStreamReader(SwearWordsFilter.class
					.getResourceAsStream("/bagOfWords.properties")))) {
				while ((line = br.readLine()) != null)
					words.add(line);
			}
			swearWords = new CopyOnWriteArrayList<String>(words);
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	/**
	 * Only Initialize method
	 * 
	 * @return
	 */
	public static SwearWordsFilter getInstance() {
		return instance;
	}

	/***
	 * Method to check if sentence have swear words
	 * 
	 * @param sentence
	 *            : String
	 * @return true if contain swear words
	 */
	public boolean haveSwearWords(String sentence) {
		List<String> sentenceWithStopWords = StopWords.removeStopWords(sentence);
		long swearWordsCnt = sentenceWithStopWords.stream().filter(swearWords::contains).count();
		return swearWordsCnt != 0;
	}

	public static void main(String[] args) {
		if (args.length != 1)
			return;
		SwearWordsFilter swf = getInstance();
	}
}
