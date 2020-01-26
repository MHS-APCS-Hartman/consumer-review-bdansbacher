import java.util.Scanner;
import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;
import java.io.*;

/**
 * Class that contains helper methods for the Review Lab
 **/
public class Review {
  
  private static HashMap<String, Double> sentiment = new HashMap<String, Double>();
  private static ArrayList<String> posAdjectives = new ArrayList<String>();
  private static ArrayList<String> negAdjectives = new ArrayList<String>();
 
  
  private static final String SPACE = " ";
  
  static{
    try {
      Scanner input = new Scanner(new File("cleanSentiment.csv"));
      while(input.hasNextLine()){
        String[] temp = input.nextLine().split(",");
        sentiment.put(temp[0],Double.parseDouble(temp[1]));
        //System.out.println("added "+ temp[0]+", "+temp[1]);
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing cleanSentiment.csv");
    }
  
  
  //read in the positive adjectives in postiveAdjectives.txt
     try {
      Scanner input = new Scanner(new File("positiveAdjectives.txt"));
      while(input.hasNextLine()){
        String temp = input.nextLine().trim();
        System.out.println(temp);
        posAdjectives.add(temp);
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing postitiveAdjectives.txt\n" + e);
    }   
 
  //read in the negative adjectives in negativeAdjectives.txt
     try {
      Scanner input = new Scanner(new File("negativeAdjectives.txt"));
      while(input.hasNextLine()){
        negAdjectives.add(input.nextLine().trim());
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
    //make sure to remove any additional space that may have been added at the end of the string.
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
  
  public static int starRating(String fileName)
  {
     double totalSentiment = totalSentiment(fileName);
    //Goes through and determines which range it is in
    //Ranges are listed below
    //Else is returns 0 for an empty review or unlisted words
     if(totalSentiment >= 10.0)
     {
        return 5;
     }
     
     else if(totalSentiment >= 5.0 && totalSentiment < 10.0)
     {
        return 4;
     }
     else if(totalSentiment >= 3.0 && totalSentiment < 5.0)
     {
        return 3;
      }
     else if(totalSentiment >= 0.0 && totalSentiment < 3.0)
     {
        return 2;
     }
     else if(totalSentiment < 0.0)
     {
        return 1;
     }
      
     else
     {
        return 0;
     }

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
  
  public static double totalSentiment (String fileName)
  {
      //open file
      String word = textToString(fileName);
      String place = "";
      double sum = 0;
      //pick out word
      for(int i = 0; i < word.length(); i++)
      {
         if(word.substring(i, i+1).equals(" "))
         {
            place = removePunctuation(place);
            sum += sentimentVal(place);
            place = "";
            
         }
         else
         {
            place += word.substring(i, i+1);
            
         }
      }
      return sum;          
            
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

  public static String fakeReview(String fileName)
  {
    String review = textToString(fileName);
    String place = "";
    boolean asterisk = false;
    //String traversal
    for(int i = 0; i < review.length(); i++)
    {
      //Checking for asterisk
      if(review.substring(i, i+1).equals("*"))
      {
         asterisk = true;
      }
      //Adds random adjective to the placeholder
      else if(review.substring(i, i+1).equals(" ") && asterisk)
      {
         while(true)
         {
            String newAdj = randomAdjective();
            if(newAdj.equals("") != true)
            {
               place += randomAdjective() + " ";
               asterisk = false;
               break;
            }
         }
      }
      //Continues to traverse the String
      else if(asterisk == false)
      {
         place += review.substring(i, i+1);
      }
    }
    return place;
    
    
  }
    
  public static String fakeReviewStronger(String fileName)
  {
   String review = textToString(fileName);
   String place = "";
   String adjective = "";
   String newAdj = ""; 
   
   //This variable determines when and where an adjective occurs
   boolean asterisk = false;
   
   //Traverses the string
   for (int i = 0; i < review.length(); i++)
   {
      if (review.substring(i, i+1).equals("*"))
      {
         asterisk = true;
      }
      
      else if (review.substring(i, i+1).equals(" ") && asterisk)
      { 
        //Makes sure the adjective added is more negative or more positive than the previous
        //Generates new adjective and compares it to the located adjective
        while (true)
         {
            newAdj = randomAdjective();
            if(newAdj.equals("") != true)
            {
               if ( (sentimentVal(adjective) > 0) && (sentimentVal(newAdj) > sentimentVal(adjective)) )
               {
                  break;
               }
               else if ( (sentimentVal(adjective) < 0) && (sentimentVal(newAdj) < sentimentVal(adjective)) )
               {
                  break;
               }
               else if (sentimentVal(adjective) == 0)
               {
                  break;
               }
            }
         }
         
         //Adds the new adjective to the placeholder and resets
         place += newAdj + " ";
         adjective = "";
         asterisk = false;

      }
      
      //Location of adjective
      else if (asterisk)
      {
         adjective += review.substring(i, i+1);
      }
      //Adding letters to adjective placeholder
      else if (asterisk == false)
      {
         place += review.substring(i, i+1);
      }
   }
   return place;
  }
}



