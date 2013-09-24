package emotion.server.util;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Pattern;

import emotion.server.constants.WordConstants;

public class WordUtil
{

   public String clearPunctuation(String word)
   {
      if(word.startsWith("@"))
      {
         return null;
      }
      int stringLen = word.length();
      if(stringLen>1)
      {
         StringBuffer buffer = new StringBuffer(stringLen);
         for (int i = 0; i < stringLen; i++)
         {
            char ch = word.charAt(i);  
            if (Character.isLetter(ch)) 
            {  
                buffer.append(ch);  
            }
         }
         String alphaString = buffer.toString(); 
         if(alphaString.length()>1)
         {
            return alphaString; 
         }
      }   
      return null;   
   }
 
   /**
    * checks if the word is a SimplifiedStopWord
    */
   public boolean isSimplifiedStopWord(String word)
   {
      String[] simplifiedStopWordsList = WordConstants.SIMPLIFIED_STOPWORDS_LIST;
      for (int i = 0; i < simplifiedStopWordsList.length; i++)
      {
         if(simplifiedStopWordsList[i].equalsIgnoreCase(word))
         {
            return true;
         }
      }
      return false;
   }
   
   public boolean isExceptionalWord(String word)
   {
      String[] exceptionalWordList = WordConstants.EXCEPTIONAL_WORDS_LIST;
      for (int i = 0; i < exceptionalWordList.length; i++)
      {
         if(exceptionalWordList[i].equalsIgnoreCase(word))
         {
            return true;
         }
      }
      return false;
   }   
   
   
   /**
    * checks if the word is a SimplifiedStopWord
    */
   public boolean isTimeWord(String word)
   {
      String[] timeWordsList = WordConstants.TIMEWORDS_LIST;
      for (int i = 0; i < timeWordsList.length; i++)
      {
         if(timeWordsList[i].equalsIgnoreCase(word))
         {
            return true;
         }
      }
      return false;
   } 
   
   /**
    * checks if the word is a SimplifiedStopWord
    */
   public boolean isProhibited(String word)
   {
      String[] prohibitedWordsList = WordConstants.PROHIBITED_WORDS_LIST;
      for (int i = 0; i < prohibitedWordsList.length; i++)
      {
         if(prohibitedWordsList[i].equalsIgnoreCase(word))
         {
            return true;
         }
      }
      return false;
   }    
   
   
   /**
    * checks if the word is a SimplifiedStopWord
    */
   public boolean isAvailable(String word)
   {
      String[] notStartWithWords = WordConstants.NOTSTARTWITH_WORDS_LIST;
      for (int i = 0; i < notStartWithWords.length; i++)
      {
         if(word.startsWith(notStartWithWords[i]))
         {
            return false;
         }
      }
      String[] notContainsWords = WordConstants.NOTCONTAINS_WORDS_LIST;
      for (int i = 0; i < notContainsWords.length; i++)
      {
         if(word.contains(notContainsWords[i]))
         {
            return false;
         }
      }
      return true;
   }
   
   
   public String covertToPlain(String key)
   {
      key = key.replace('y', 'y');
      key = key.replaceAll("ù|ú|û|ü", "u");
      key = key.replaceAll("ò|ó|ô|õ|ö", "o");
      key = key.replaceAll("ì|í|î|ï|ý", "i");
      key = key.replaceAll("è|é|ê|ë", "e");
      key = key.replace('ç', 'c');
      key = key.replaceAll("à|á|â|ã|ä|å|æ", "a");
      return key;
   }
   
   public String deAccent(String str) {
      str = covertToPlain(str.toLowerCase(Locale.US));
      String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD); 
      Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
      return pattern.matcher(nfdNormalizedString).replaceAll("");
  }
   
   public boolean checkContains(String bigString, String portionString) 
   {
      String plainedBigString = covertToPlain(bigString.toLowerCase(Locale.US));
      String plainedPortionString = covertToPlain(bigString.toLowerCase(Locale.US));
      String deAccentedBigString = deAccent(plainedBigString);
      String deAccentedPortionString = deAccent(plainedPortionString);
      
      return deAccentedBigString.contains(deAccentedPortionString);
   }
   
   public String converToUnderscored(String word)
   {
      String root = null;
      if(word!=null)
      {
         String wordArr[] = word.split(" ");
         StringBuffer strBuffer = new StringBuffer();
         strBuffer.append(wordArr[0]);
         for (int i = 1; i < wordArr.length; i++)
         {
            strBuffer.append("_" + wordArr[i]);
         }
         root = strBuffer.toString();
         
      }
      return root;
   }
   

   public boolean isAngerWord(String word)
   {
      String[] timeWordsList = WordConstants.ALL_ANGER_WORDS_LIST;
      for (int i = 0; i < timeWordsList.length; i++)
      {
         if(timeWordsList[i].equalsIgnoreCase(word))
         {
            return true;
         }
      }
      return false;
   }
   
   public boolean isDisgustWord(String word)
   {
      String[] timeWordsList = WordConstants.ALL_DISGUST_WORDS_LIST;
      for (int i = 0; i < timeWordsList.length; i++)
      {
         if(timeWordsList[i].equalsIgnoreCase(word))
         {
            return true;
         }
      }
      return false;
   }
   
   public boolean isJoyWord(String word)
   {
      String[] timeWordsList = WordConstants.ALL_JOY_WORDS_LIST;
      for (int i = 0; i < timeWordsList.length; i++)
      {
         if(timeWordsList[i].equalsIgnoreCase(word))
         {
            return true;
         }
      }
      return false;
   }   
   
   public boolean isSadnessWord(String word)
   {
      String[] timeWordsList = WordConstants.ALL_SADNESS_WORDS_LIST;
      for (int i = 0; i < timeWordsList.length; i++)
      {
         if(timeWordsList[i].equalsIgnoreCase(word))
         {
            return true;
         }
      }
      return false;
   }   
   
   public boolean isFearWord(String word)
   {
      String[] timeWordsList = WordConstants.ALL_FEAR_WORDS_LIST;
      for (int i = 0; i < timeWordsList.length; i++)
      {
         if(timeWordsList[i].equalsIgnoreCase(word))
         {
            return true;
         }
      }
      return false;
   }
   
   public boolean isSurpriseWord(String word)
   {
      String[] timeWordsList = WordConstants.ALL_SURPRISE_WORDS_LIST;
      for (int i = 0; i < timeWordsList.length; i++)
      {
         if(timeWordsList[i].equalsIgnoreCase(word))
         {
            return true;
         }
      }
      return false;
   }     
   
   public boolean isParrotAngerWord(String word)
   {
      String[] timeWordsList = WordConstants.PARROT_ANGER_WORDS_LIST;
      for (int i = 0; i < timeWordsList.length; i++)
      {
         if(timeWordsList[i].equalsIgnoreCase(word))
         {
            return true;
         }
      }
      return false;
   }
   
   public boolean isParrotLoveWord(String word)
   {
      String[] timeWordsList = WordConstants.PARROT_LOVE_WORDS_LIST;
      for (int i = 0; i < timeWordsList.length; i++)
      {
         if(timeWordsList[i].equalsIgnoreCase(word))
         {
            return true;
         }
      }
      return false;
   }
   
   public boolean isParrotJoyWord(String word)
   {
      String[] timeWordsList = WordConstants.PARROT_JOY_WORDS_LIST;
      for (int i = 0; i < timeWordsList.length; i++)
      {
         if(timeWordsList[i].equalsIgnoreCase(word))
         {
            return true;
         }
      }
      return false;
   }   
   
   public boolean isParrotSadnessWord(String word)
   {
      String[] timeWordsList = WordConstants.PARROT_SADNESS_WORDS_LIST;
      for (int i = 0; i < timeWordsList.length; i++)
      {
         if(timeWordsList[i].equalsIgnoreCase(word))
         {
            return true;
         }
      }
      return false;
   }   
   
   public boolean isParrotFearWord(String word)
   {
      String[] timeWordsList = WordConstants.PARROT_FEAR_WORDS_LIST;
      for (int i = 0; i < timeWordsList.length; i++)
      {
         if(timeWordsList[i].equalsIgnoreCase(word))
         {
            return true;
         }
      }
      return false;
   }
   
   public boolean isParrotSurpriseWord(String word)
   {
      String[] timeWordsList = WordConstants.PARROT_SURPRISE_WORDS_LIST;
      for (int i = 0; i < timeWordsList.length; i++)
      {
         if(timeWordsList[i].equalsIgnoreCase(word))
         {
            return true;
         }
      }
      return false;
   }   
   

}
