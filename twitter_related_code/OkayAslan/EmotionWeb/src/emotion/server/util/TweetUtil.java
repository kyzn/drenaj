package emotion.server.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import twitter4j.Status;
import twitter4j.Tweet;

import emotion.server.constants.WordConstants;
import emotion.server.libstemmer.snowball.ext.englishStemmer;
import emotion.server.wordNet.WordNetDBImpl;
import emotion.shared.EmoTweet;

public class TweetUtil
{
   private static WordNetDBImpl wordNetdbImpl = WordNetDBImpl.getInstance();
   
   public ArrayList<EmoTweet> convertTweetToEmotweet(List<twitter4j.Tweet> tweetList)
   {
      ArrayList<EmoTweet> tweetArrList = null;

      if (tweetList != null)
      {
         tweetArrList = new ArrayList<EmoTweet>(tweetList.size());
         
         for (int j = 0; j < tweetList.size(); j++)
         {
            twitter4j.Tweet incomingTweet = tweetList.get(j);
            EmoTweet newTweet = new EmoTweet();
            newTweet.setTweetID(incomingTweet.getId());
            newTweet.setSource(incomingTweet.getSource());
            newTweet.setTweetDate(incomingTweet.getCreatedAt());
            newTweet.setTweetText(incomingTweet.getText());
            newTweet.setUserName(incomingTweet.getFromUser());
            newTweet.setUserID(incomingTweet.getFromUserId());
            newTweet.setImageUrl(incomingTweet.getProfileImageUrl());
            tweetArrList.add(newTweet);
         }
      }
      return tweetArrList;
   }
  
   public HashMap<String,Integer> fillAndGetEmotionHashMap(ArrayList<EmoTweet> emoTweetArrayList)
   {
      HashMap<String,Integer> emotionHashMapByWord = new HashMap<String,Integer>();
      for (int i = 0; i < emoTweetArrayList.size(); i++)
      {
         processStatementByOmittingStopWordsAndStem(emoTweetArrayList.get(i).getTweetText(),emotionHashMapByWord);
      }
      return emotionHashMapByWord;
   }   
   
   public void processStatementByOmittingStopWordsAndStem(String sentence,HashMap<String, Integer> emotionHashMapByWord)
   {   
      StringTokenizer st = new StringTokenizer(sentence);
      while (st.hasMoreTokens()) {
         String currentWord = clearPunctuation(st.nextToken().toLowerCase());
       //  System.out.println("Incoming word : "+currentWord);
         if(currentWord!=null)
         {
            if(!isSimplifiedStopWord(currentWord))
            {
               if(emotionHashMapByWord.containsKey(currentWord))
               {
                  int frequency = emotionHashMapByWord.get(currentWord);
                  frequency=frequency+1;
                  emotionHashMapByWord.put(currentWord,frequency);
               //   System.out.println("Inserted word : "+currentWord);
               }
               else
               {
                  englishStemmer stemmer = new englishStemmer();
                  stemmer.setCurrent(currentWord);
                  stemmer.stem();
                  String stemmedWord = stemmer.getCurrent();
                  if(emotionHashMapByWord.containsKey(stemmedWord))
                  {
                     int frequency = emotionHashMapByWord.get(stemmedWord);
                     frequency=frequency+1;
                     emotionHashMapByWord.put(currentWord,frequency); 
                  }
                  else
                  {
                     emotionHashMapByWord.put(currentWord, 1);
                //     System.out.println("Inserted word : "+currentWord);
                  }
                     
               }   
            }  
         }   
      }
   }
   
   public void processStatementByOmittingStopWords(String sentence,HashMap<String, Integer> emotionHashMapByWord)
   {
      StringTokenizer st = new StringTokenizer(sentence);
      while (st.hasMoreTokens()) {
         String currentWord = clearPunctuation(st.nextToken().toLowerCase());
         if(currentWord!=null)
         {
            if(!isSimplifiedStopWord(currentWord))
            {
               if(emotionHashMapByWord.containsKey(currentWord))
               {
                  int frequency = emotionHashMapByWord.get(currentWord);
                  frequency=frequency+1;
                  emotionHashMapByWord.put(currentWord,frequency);
               }
               else
               {
                  String verbWord = wordNetdbImpl.getVerb(currentWord);
                  if(verbWord!=null)
                  {
                     putWordtoEmotionHashmap(emotionHashMapByWord,verbWord);   
                  }
                  else
                  {
                     String nounWord = wordNetdbImpl.getNoun(currentWord);
                     if(nounWord!=null)
                     {
                        putWordtoEmotionHashmap(emotionHashMapByWord,nounWord);
                     }
                     else
                     {
                        String adjWord = wordNetdbImpl.getAdjective(currentWord);
                        if(nounWord!=null)
                        {
                           putWordtoEmotionHashmap(emotionHashMapByWord,adjWord);
                        }
                        else
                        {
                           String advWord = wordNetdbImpl.getAdverb(currentWord);
                           if(nounWord!=null)
                           {
                              putWordtoEmotionHashmap(emotionHashMapByWord,advWord);
                           }
                           else
                           {
                              emotionHashMapByWord.put(currentWord, 1);
                           }   
                        }
                     }     
                  }      
               }   
            }  
         }   
      }
   }
   
   private void putWordtoEmotionHashmap(HashMap<String, Integer> emotionHashMapByWord, String wordForm)
   {
      if(!isSimplifiedStopWord(wordForm))
      {
         if(emotionHashMapByWord.containsKey(wordForm))
         {
            int frequency = emotionHashMapByWord.get(wordForm);
            frequency=frequency+1;
            emotionHashMapByWord.put(wordForm,frequency);
         }
         else
         {
            emotionHashMapByWord.put(wordForm, 1);
         }
      }
   }

   private String clearPunctuation(String word)
   {
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

   public ArrayList<EmoTweet> convertStatusToEmoTweet(ArrayList<Status> userStatusArrList)
   {
      ArrayList<EmoTweet> tweetArrList = null;

      if (userStatusArrList != null)
      {
         tweetArrList = new ArrayList<EmoTweet>(userStatusArrList.size());
         
         for (int j = 0; j < userStatusArrList.size(); j++)
         {
            Status userStatus = userStatusArrList.get(j);

            EmoTweet newTweet = new EmoTweet();
            newTweet.setTweetID(userStatus.getId());
            newTweet.setSource(userStatus.getSource());
            newTweet.setTweetDate(userStatus.getCreatedAt());
            newTweet.setTweetText(userStatus.getText());
            newTweet.setUserName(userStatus.getUser().getName());
            newTweet.setUserID(userStatus.getUser().getId());
            newTweet.setImageUrl(userStatus.getUser().getProfileBackgroundImageUrl());
            tweetArrList.add(newTweet);
         }
      }
      return tweetArrList;
   }
   


}
