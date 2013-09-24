package emotion.server.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class HashMapUtil
{
   private HashMap<String,Integer> emotionHashMapByWord = new HashMap<String,Integer>();
   private LinkedHashMap<String,Integer> sortedEmotionHashMapByWord = null;
   
   /**
    * @return the emotionHashMapByWord
    */
   public HashMap<String, Integer> getEmotionHashMapByWord()
   {
      return emotionHashMapByWord;
   }
   /**
    * @return the sortedEmotionHashMapByWord
    */
   public LinkedHashMap<String, Integer> getSortedEmotionHashMapByWord()
   {
      return sortedEmotionHashMapByWord;
   }
   /**
    * @param emotionHashMapByWord the emotionHashMapByWord to set
    */
   public void setEmotionHashMapByWord(HashMap<String, Integer> emotionHashMapByWord)
   {
      this.emotionHashMapByWord = emotionHashMapByWord;
   }
   /**
    * @param sortedEmotionHashMapByWord the sortedEmotionHashMapByWord to set
    */
   public void setSortedEmotionHashMapByWord(LinkedHashMap<String, Integer> sortedEmotionHashMapByWord)
   {
      this.sortedEmotionHashMapByWord = sortedEmotionHashMapByWord;
   }
   
   public LinkedHashMap<String, Integer> sortEmotionHashMapByValue(HashMap<String, Integer> emotionHashMapByWord)
   {
      LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
      HashMap<String, Integer> tempMap = (HashMap<String, Integer>) emotionHashMapByWord.clone();
      List<String> emotionHashMapKeys = new ArrayList<String>(emotionHashMapByWord.keySet());
      List<Integer> emotionHashMapValues = new ArrayList<Integer>(emotionHashMapByWord.values());
      Collections.sort(emotionHashMapValues);
      Collections.reverse(emotionHashMapValues);
      
      Iterator<Integer> valueIt = emotionHashMapValues.iterator();
      while (valueIt.hasNext())
      {
         Integer val = (Integer) valueIt.next();
         Iterator<String> keyIt = emotionHashMapKeys.iterator();
         while (keyIt.hasNext())
         {
            String key = (String) keyIt.next();
            if (tempMap.get(key)==val)
            {
               tempMap.remove(key);
               emotionHashMapKeys.remove(key);
               sortedMap.put(key, val);
               break;
            }
         }
      }   
      return sortedMap;
   } 
 
/*   
   public void writeToFile(LinkedHashMap<String, Integer> emotionHashMapByWord, String fileName)
   {
      FileWriter fstream;
      try
      {
         fstream = new FileWriter(fileName);
         BufferedWriter out = new BufferedWriter(fstream);
         
         Iterator<String> HashmapIterator = emotionHashMapByWord.keySet().iterator();
         while(HashmapIterator.hasNext()) 
         {
            Object key = HashmapIterator.next(); 
            Object val = emotionHashMapByWord.get(key);
            out.write(key+" ; "+ val+"\n");
         }
         out.close();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }  
*/   
   
   public Integer getFirstValue(HashMap<String, Integer> sortedEmotionHashMapByWord)
   {
      Iterator<String> HashmapIterator = sortedEmotionHashMapByWord.keySet().iterator();
      while(HashmapIterator.hasNext()) 
      {
         Object key = HashmapIterator.next(); 
         Integer val = sortedEmotionHashMapByWord.get(key);
         return val;
      }
      return null;
   } 
   
   public String getFirstKey(HashMap<String, Integer> sortedEmotionHashMapByWord)
   {
      Iterator<String> HashmapIterator = sortedEmotionHashMapByWord.keySet().iterator();
      while(HashmapIterator.hasNext()) 
      {
         String key = HashmapIterator.next(); 
         return key;
      }
      return null;
   }
   
   public Integer getTotalValue(HashMap<String, Integer> occurenceHashMapByWord)
   {
      int total = 0;
      Iterator<String> HashmapIterator = occurenceHashMapByWord.keySet().iterator();
      while(HashmapIterator.hasNext()) 
      {
         Object key = HashmapIterator.next(); 
         Integer val = occurenceHashMapByWord.get(key);
         total = total + val;
      }
      return total;
   } 
   
   public Integer copyToHashMapAndGetTotalValue(HashMap<String, Integer> fromOccurenceHashMapByWord,HashMap<String, Integer> toOccurenceHashMapByWord)
   {
      int total = 0;
      Iterator<String> HashmapIterator = fromOccurenceHashMapByWord.keySet().iterator();
      while(HashmapIterator.hasNext()) 
      {
         String key = HashmapIterator.next(); 
         int val = fromOccurenceHashMapByWord.get(key);
         checkOccurenceHashMapByValue(key,toOccurenceHashMapByWord,val);
         total = total + val;
      }
      return total;
   }
   
   
   public void printHashmap(HashMap<String, Integer> sortedEmotionHashMapByWord)
   {
      Iterator<String> hashmapIterator = sortedEmotionHashMapByWord.keySet().iterator();
      while(hashmapIterator.hasNext()) 
      {
         Object key = hashmapIterator.next(); 
         Object val = sortedEmotionHashMapByWord.get(key);
         System.out.println(key+" ; "+ val);
      }
   } 
 
   public void checkOccurenceHashMapByOne(String key, HashMap<String, Integer> valueOccurenceHashmap)
   {
      if(valueOccurenceHashmap.containsKey(key))
      {
         Integer val = valueOccurenceHashmap.get(key);
         val =val+1;
         valueOccurenceHashmap.put(key, val);
      }
      else
      {
         valueOccurenceHashmap.put(key, 1);
      }
   } 
   
   public void checkOccurenceHashMapByValue(String key, HashMap<String, Integer> valueOccurenceHashmap,int value)
   {
      if(valueOccurenceHashmap.containsKey(key))
      {
         Integer val = valueOccurenceHashmap.get(key);
         val =val+value;
         valueOccurenceHashmap.put(key, val);
      }
      else
      {
         valueOccurenceHashmap.put(key, value);
      }
   } 
   
   
   public void checkOccurenceWordHashMapByValue(String parentKey,String childKey,HashMap<String,HashMap<String,Integer>> valueOccurenceHashmap, Integer value)
   {
      if(valueOccurenceHashmap.containsKey(parentKey))
      {
         checkOccurenceHashMapByValue(childKey, valueOccurenceHashmap.get(parentKey),value); 
      }
      else
      {
         HashMap<String, Integer> valueOccHMap= new HashMap<String, Integer>();
         valueOccHMap.put(childKey, value);
         valueOccurenceHashmap.put(parentKey, valueOccHMap);
      }
   }    
   
   
}
