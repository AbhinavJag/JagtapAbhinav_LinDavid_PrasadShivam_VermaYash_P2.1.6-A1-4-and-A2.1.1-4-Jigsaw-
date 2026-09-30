import java.util.Scanner;
import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Class that contains helper methods for the Review Lab
 **/
public class Review {
  
  private static HashMap<String, Double> sentiment = new HashMap<String, Double>();
  private static ArrayList<String> posAdjectives = new ArrayList<String>();
  private static ArrayList<String> negAdjectives = new ArrayList<String>();
  
  static{
    try {
      Scanner input = new Scanner(new File("CSA_216_ConsumerLab_Starter/ConsumerLab_Code/cleanSentiment.csv"));
      while(input.hasNextLine()){
        String[] temp = input.nextLine().split(",");
        sentiment.put(temp[0],Double.parseDouble(temp[1]));
        // System.out.println("added "+ temp[0]+", "+temp[1]);
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing cleanSentiment.csv");
    }
  
  
  //read in the positive adjectives in postiveAdjectives.txt
     try {
      Scanner input = new Scanner(new File("CSA_216_ConsumerLab_Starter/ConsumerLab_Code/positiveAdjectives.txt"));
      while(input.hasNextLine()){
        // The supplied list is a CSV export (word,value); random adjective
        // methods should return just the word.
        posAdjectives.add(input.nextLine().split(",")[0].trim());
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing positiveAdjectives.txt\n" + e);
    }   
 
  //read in the negative adjectives in negativeAdjectives.txt
     try {
      Scanner input = new Scanner(new File("CSA_216_ConsumerLab_Starter/ConsumerLab_Code/negativeAdjectives.txt"));

      while(input.hasNextLine()){
        negAdjectives.add(input.nextLine().split(",")[0].trim());
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing negativeAdjectives.txt");
    }   
  }
  
  /** 
   * returns a string containing all of the text in fileName (including punctuation), 
   * with words separated by a single space 
   */
  public static String textToString( String fileName )
  {  
    String temp = "";
    try {
      Scanner input = new Scanner(new File(fileName));
      
      //add 'words' in the file to the string, separated by a single space
      while(input.hasNext()){
        temp = temp + input.next() + " ";
      }
      input.close();
      
    }
    catch(Exception e){
      System.out.println("Unable to locate " + fileName);
    }
    // remove any additional space that may have been added at the end of the string
    return temp.trim();
  }
  
  /**
   * @returns the sentiment value of word as a number between -1 (very negative) to 1 (very positive sentiment) 
   */
  public static double sentimentVal( String word )
  {
    try
    {
      return sentiment.get(word.toLowerCase());
    }
    catch(Exception e)
    {
      return 0;
    }
  }
  
  /**
   * Returns the ending punctuation of a string, or the empty string if there is none 
   */
  public static String getPunctuation( String word )
  { 
    String punc = "";
    for(int i=word.length()-1; i >= 0; i--){
      if(!Character.isLetterOrDigit(word.charAt(i))){
        punc = punc + word.charAt(i);
      } else {
        return punc;
      }
    }
    return punc;
  }

      /**
   * Returns the word after removing any beginning or ending punctuation
   */
  public static String removePunctuation( String word )
  {
    while(word.length() > 0 && !Character.isAlphabetic(word.charAt(0)))
    {
      word = word.substring(1);
    }
    while(word.length() > 0 && !Character.isAlphabetic(word.charAt(word.length()-1)))
    {
      word = word.substring(0, word.length()-1);
    }
    
    return word;
  }
 
  /** 
   * Randomly picks a positive adjective from the positiveAdjectives.txt file and returns it.
   */
  public static String randomPositiveAdj()
  {
    int index = (int)(Math.random() * posAdjectives.size());
    return posAdjectives.get(index);
  }
  
  /** 
   * Randomly picks a negative adjective from the negativeAdjectives.txt file and returns it.
   */
  public static String randomNegativeAdj()
  {
    int index = (int)(Math.random() * negAdjectives.size());
    return negAdjectives.get(index);
    
  }
  
  /** 
   * Randomly picks a positive or negative adjective and returns it.
   */
  public static String randomAdjective()
  {
    boolean positive = Math.random() < .5;
    if(positive){
      return randomPositiveAdj();
    } else {
      return randomNegativeAdj();
    }
  }

  /**
   * Returns the sum of the sentiment values of the words in fileName.
   */
  public static double totalSentiment(String fileName)
  {
    String reviewText = textToString(fileName);
    double sum = 0.0;
    for (String word : reviewText.split("\\s+"))
    {
      System.out.println(word);
      sum += sentimentVal(removePunctuation(word));
    }
    
    return sum;
  }

  /**
   * Converts a review's total sentiment to a rating from 0 through 4.
   */
  public static int starRating(String fileName)
  {
    double total = totalSentiment(fileName);

    if (total < -10) return 0;
    if (total < 0) return 1;
    if (total < 10) return 2;
    if (total < 20) return 3;
    return 4;
  }

  /**
   * Replaces every *-marked adjective with an adjective that has stronger
   * sentiment in the same direction, while retaining the original punctuation.
   */
  public static String fakeReview(String fileName)
  {
    String reviewText = textToString(fileName);
    StringBuilder result = new StringBuilder();

    for (String word : reviewText.split("\\s+"))
    {
      if (word.startsWith("*"))
      {
        String punctuation = getPunctuation(word);
        String original = removePunctuation(word.substring(1));
        result.append(strongerAdjective(original)).append(punctuation);
      }
      else
      {
        result.append(word);
      }
      result.append(" ");
    }

    return result.toString().trim();
  }

  /** Returns a random adjective that is stronger than original when possible. */
  private static String strongerAdjective(String original)
  {
    double originalValue = sentimentVal(original);
    boolean positive = originalValue > 0;
    String replacement = positive ? randomPositiveAdj() : randomNegativeAdj();

    // A finite limit prevents a rare endless search at an extreme value.
    for (int attempts = 0; attempts < 1000000000; attempts++)
    {
      double replacementValue = sentimentVal(replacement);
      if ((positive && replacementValue > originalValue) ||
          (!positive && replacementValue < originalValue))
      {
        System.out.println("replacement");
        return replacement;
      }
      replacement = positive ? randomPositiveAdj() : randomNegativeAdj(); //If positive, return randompositive; else negative
    }

    return replacement;
  }
}
