import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class HashMarkov implements MarkovInterface {
    private String myText;
    private Random myRandom;
    private int myOrder;
    private HashMap<WordGram, List<String>> myMap;

    public HashMarkov(int order) {
        myRandom = new Random();
        myOrder = order;
        myMap = new HashMap<WordGram, List<String>>();
    }

	/**
	 * Set the training text for subsequent random text generation.
	 * Should always be called prior to calling getRandomText.
	 * @param text is the training text
	 */
	public void setTraining(String text) {
        myText = text;
        String[] words = myText.split("\\s+");
        myMap.clear();
    
        for (int i = 0; i <= words.length - myOrder; i++) {
            WordGram wg = new WordGram(words, i, myOrder);
            String next = words[i + myOrder];
    
            if (!myMap.containsKey(wg)) {
                myMap.put(wg, new ArrayList<String>());
            }
            myMap.get(wg).add(next);
        }
    }
	

	/**
	 * Get a list of Strings containing all words that follow
	 * from wgram in the training text. Result may be an empty list.
	 * @param wgram is a WordGram to search for in the text
	 * @return List of words following wgram in training text.
	 * May be empty.
	 */
	public List<String> getFollows(WordGram wgram) {
        if (myMap.containsKey(wgram)) {
            return myMap.get(wgram);
        }
        return Collections.emptyList();
    }

	
	/**
	 * Get randomly generated text based on the training text.
	 * Details of how random text is generated are left to the 
	 * implementing class.
	 * @param length is the number of characters or words generated
	 * @return randomly generated text 
	 */
	public String getRandomText(int length) {
        ArrayList<String> randomWords = new ArrayList<>(length);
        String[] myWords = myText.split("\\s+");
		int index = myRandom.nextInt(myWords.length - myOrder + 1);
		WordGram current = new WordGram(myWords,index,myOrder);
		randomWords.add(current.toString());
        List<String> nextWords = getFollows(current);
        if (nextWords.size() == 0){
            for(int i = 0; i < length - 1; i++){
                int idx = myRandom.nextInt(myWords.length);
		        randomWords.add(myWords[idx]);
            }
        }else{
            for(int i = 0; i < length - 1; i++){
            int idx = myRandom.nextInt(nextWords.size());
		    randomWords.add(nextWords.get(idx));
            }
        }
        return String.join(" ", randomWords);
    }
	

	/**
	 * Returns the order of the Markov Model, typically set at construction 
	 * @return the order of this model
	 */
	public int getOrder() {
        return myOrder;
    }
	
	/**
	 * Sets the random seed and initializes the random 
	 * number generator. A given implementing class should
	 * always produce the same randomText given the same 
	 * training text and random seed.
	 * @param seed initial seed for java.util.Random
	 */
	public void setSeed(long seed) {
        myRandom.setSeed(seed);
    }
}

