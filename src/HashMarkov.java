import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
public class HashMarkov implements MarkovInterface{
  protected String[] myWords;
  protected Random myRandom;
  protected int myOrder;
  protected HashMap <WordGram, List<String>> myMap;
  public HashMarkov(int order){
      myOrder = order;
      myRandom = new Random();
      myMap = new HashMap<WordGram, List<String>>();
  }
  public HashMarkov(){
   this(2);
}
 public void setTraining(String text){
    myWords = text.split("\\s+");
    myMap.clear();
    for(int i = 0; i<myWords.length - myOrder; i++){
         WordGram hold = new WordGram(myWords, i, myOrder);
         myMap.putIfAbsent(hold, new ArrayList<String>());
         List<String> listy = myMap.get(hold);
         listy.add(myWords[i + myOrder]);
         myMap.put(hold, listy);
    }
}
public List<String> getFollows(WordGram wgram){
    if(!myMap.containsKey(wgram)){
        List<String> retVal = new ArrayList<String>();
        return retVal;
    }
      return myMap.get(wgram);
}

   private String getNext(WordGram wgram) {
   List<String> follows = getFollows(wgram);
   if (follows.size() == 0) {
       int randomIndex = myRandom.nextInt(myWords.length);
       follows.add(myWords[randomIndex]);
   }
   int randomIndex = myRandom.nextInt(follows.size());
   return follows.get(randomIndex);
}
  public String getRandomText(int length){
      ArrayList<String> randomWords = new ArrayList<>(length);
      int index = myRandom.nextInt(myWords.length - myOrder + 1);
      WordGram current = new WordGram(myWords,index,myOrder);
      randomWords.add(current.toString());
      for(int k=0; k < length-myOrder; k++) {
          String nextWord = getNext(current);
          randomWords.add(nextWord);
          current = current.shiftAdd(nextWord);
}

return String.join(" ", randomWords);
  }
  public int getOrder(){
      return myOrder;
}
  public void setSeed(long seed){
      myRandom.setSeed(seed);
}
}

