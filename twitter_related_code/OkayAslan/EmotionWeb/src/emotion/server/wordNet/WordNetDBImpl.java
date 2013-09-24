package emotion.server.wordNet;


import java.util.StringTokenizer;

import edu.smu.tspell.wordnet.*;
import emotion.server.util.SystemPrinter;

public class WordNetDBImpl
{
   private static WordNetDBImpl instance = new WordNetDBImpl();
   private WordNetDatabase wordNetDB = null;
   private boolean logOpen = false;
   
   public static WordNetDBImpl getInstance()
   {
      return instance;
   }
   
   private WordNetDBImpl()
   {
   //   System.setProperty("wordnet.database.dir", "./src/emotion/server/wordNet/WordNet-3.0/dict");
      System.setProperty("wordnet.database.dir", "D:/Project/EclipseWorkspace/EmotionWeb/src/emotion/server/wordNet/WordNet-3.0/dict");
      
      wordNetDB = WordNetDatabase.getFileInstance();
   }
   
   /**
    * @param logOpen the logOpen to set
    */
   public void setLogOpen(boolean logOpen)
   {
      this.logOpen = logOpen;
   }

   /**
    * @return the logOpen
    */
   public boolean isLogOpen()
   {
      return logOpen;
   }
   
   public String getVerb(String wordForm)
   {   
      Synset[] synsets = wordNetDB.getSynsets(wordForm, SynsetType.VERB,true);
      return getWordResult(synsets,wordForm);
   }
   
   public String getNoun(String wordForm)
   {   
      Synset[] synsets = wordNetDB.getSynsets(wordForm, SynsetType.NOUN,true);
      return getWordResult(synsets,wordForm);
   }   
   
   public String getAdjective(String wordForm)
   {   
      Synset[] synsets = wordNetDB.getSynsets(wordForm, SynsetType.ADJECTIVE,true);
      return getWordResult(synsets,wordForm);
   }
   
   public String getAdverb(String wordForm)
   {   
      Synset[] synsets = wordNetDB.getSynsets(wordForm, SynsetType.ADVERB,true);
      return getWordResult(synsets,wordForm);
   }   
   
   
   public WordNetData getWordProperties(String word)
   {
      WordNetData wNData = new WordNetData();
      String foundFormedWord=null;
      String nounFormedWord=null;
      wNData.setWord(word);
      
      String formedWord = getNoun(word);
      if(formedWord!=null)
      {
         wNData.setNoun(true);
         foundFormedWord=formedWord;
         nounFormedWord=formedWord;
      }
      formedWord = getVerb(word);
      if(formedWord!=null)
      {
         wNData.setVerb(true);
         foundFormedWord=formedWord;
      }
      formedWord = getAdjective(word);
      if(formedWord!=null)
      {
         wNData.setAdjective(true);
         foundFormedWord=formedWord;
      }
      formedWord = getAdverb(word);
      if(formedWord!=null)
      {
         wNData.setAdverb(true);
         foundFormedWord=formedWord;
      }
      if(foundFormedWord!=null)
      {
         if(nounFormedWord!=null)
         {
            wNData.setPlainForm(normalizeForm(nounFormedWord));
         }
         else
         {
            wNData.setPlainForm(normalizeForm(foundFormedWord));
         }
         wNData.setJustNoun(!wNData.isVerb() && !wNData.isAdjective() && !wNData.isAdverb());
         return wNData;
      }
      else
      {
         return null;
      } 
   }
   
   
   
   private String normalizeForm(String formedWord)
   {
      StringTokenizer st = new StringTokenizer(formedWord);
      if(st.hasMoreTokens())
      {
         return st.nextToken();
      }
      else
      {
         return formedWord; 
      }
      
/*      
      String[] formedWord = mySynSet.getWordForms();
      for (int j = 0; j < wordForms.length; j++)
      {
         String myWord = wordForms[j];
         if(wordForm.startsWith(myWord.substring(0, 1)))
         {
            return myWord;
         }       
      }
*/      
   }

   private String getWordResult(Synset[] synsets, String wordForm)
   {
      if(isLogOpen())
      {
         SystemPrinter.printOperation(wordForm + " is querried from wordNet.");
      }
      
      if (synsets.length > 0)
      {
         for (int i = 0; i < synsets.length; i++)
         {
            Synset mySynSet = synsets[i];
            String[] wordForms = mySynSet.getWordForms();
            for (int j = 0; j < wordForms.length; j++)
            {
               String myWord = wordForms[j];
               if(wordForm.startsWith(myWord.substring(0, 1)))
               {
                  return myWord;
               }       
            }      
         }
      }
      else
      { 
         if(isLogOpen())
         {
            SystemPrinter.printException("Word = "+wordForm + " is not found.");
         }
      }
      return null;
   }

   public void queryWord(String wordForm)
   {
      Synset[] synsets = wordNetDB.getSynsets(wordForm, SynsetType.VERB,true);
      //  Display the word forms and definitions for synsets retrieved
      if (synsets.length > 0)
      {
         System.out.println("word : "+ wordForm);
         for (int i = 0; i < synsets.length; i++)
         {
            Synset mySynSet = synsets[i];
            System.out.println("   type : "+ mySynSet.getType());
            System.out.println("   definition : "+ mySynSet.getDefinition());
            String[] wordForms = mySynSet.getWordForms();
            for (int j = 0; j < wordForms.length; j++)
            {
               System.out.println("   wordForms "+j+" : "+ wordForms[j]);
            }
            WordSense[] wordSense = mySynSet.getAntonyms(wordForm);
            for (int j = 0; j < wordSense.length; j++)
            {
               System.out.println("   wordSense "+j+" : "+ wordSense[j].getWordForm());
            }
            System.out.println("");
         }
      }
      else
      {
         System.err.println("No synsets exist that contain " +
               "the word form '" + wordForm + "'");
      }
   }   
   
   
   public void getSynSet(String wordForm)
   {
      Synset[] synsets = wordNetDB.getSynsets(wordForm);
      //  Display the word forms and definitions for synsets retrieved
      if (synsets.length > 0)
      {
         System.out.println("The following synsets contain '" +
               wordForm + "' or a possible base form " +
               "of that text:");
         for (int i = 0; i < synsets.length; i++)
         {
            System.out.println("");
            String[] wordForms = synsets[i].getWordForms();
            for (int j = 0; j < wordForms.length; j++)
            {
               System.out.print((j > 0 ? ", " : "") + wordForms[j]);
            }
            System.out.println(": " + synsets[i].getDefinition());
         }
      }
      else
      {
         System.err.println("No synsets exist that contain " +
               "the word form '" + wordForm + "'");
      }

   }
   
   
   /*        
           wNData.setNoun(wordNetdbImpl.getNoun(currentWord)!=null);
           wNData.setVerb(wordNetdbImpl.getVerb(currentWord)!=null);
           wNData.setAdjective(wordNetdbImpl.getAdjective(currentWord)!=null);
           wNData.setAdverb(wordNetdbImpl.getAdverb(currentWord)!=null);
   */  
   
}
