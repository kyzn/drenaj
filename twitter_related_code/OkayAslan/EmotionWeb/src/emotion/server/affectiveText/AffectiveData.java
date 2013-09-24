package emotion.server.affectiveText;

import java.util.HashMap;

public class AffectiveData
{
   private int id;
   private String statement;
   private int anger;
   private int disgust; 
   private int fear;
   private int joy;
   private int sadness;
   private int surprise;
   private HashMap<String,Integer> adjectiveOccurrenceHashMap = new HashMap<String,Integer>();
   private HashMap<String,Integer> verbOccurrenceHashMap = new HashMap<String,Integer>();
   
   /**
    * @return the id
    */
   public int getId()
   {
      return id;
   }
   /**
    * @param id the id to set
    */
   public void setId(int id)
   {
      this.id = id;
   }
   /**
    * @return the statement
    */
   public String getStatement()
   {
      return statement;
   }
   /**
    * @param statement the statement to set
    */
   public void setStatement(String statement)
   {
      this.statement = statement;
   }
   /**
    * @param anger the anger to set
    */
   public void setAnger(int anger)
   {
      this.anger = anger;
   }
   /**
    * @return the anger
    */
   public int getAnger()
   {
      return anger;
   }
   /**
    * @return the disgust
    */
   public int getDisgust()
   {
      return disgust;
   }
   /**
    * @param disgust the disgust to set
    */
   public void setDisgust(int disgust)
   {
      this.disgust = disgust;
   }
   /**
    * @return the fear
    */
   public int getFear()
   {
      return fear;
   }
   /**
    * @param fear the fear to set
    */
   public void setFear(int fear)
   {
      this.fear = fear;
   }
   /**
    * @return the joy
    */
   public int getJoy()
   {
      return joy;
   }
   /**
    * @param joy the joy to set
    */
   public void setJoy(int joy)
   {
      this.joy = joy;
   }
   /**
    * @return the sadness
    */
   public int getSadness()
   {
      return sadness;
   }
   /**
    * @param sadness the sadness to set
    */
   public void setSadness(int sadness)
   {
      this.sadness = sadness;
   }
   /**
    * @return the surprise
    */
   public int getSurprise()
   {
      return surprise;
   }
   /**
    * @param surprise the surprise to set
    */
   public void setSurprise(int surprise)
   {
      this.surprise = surprise;
   }
   /**
    * @return the adjectiveOccurrenceHashMap
    */
   public HashMap<String, Integer> getAdjectiveOccurrenceHashMap()
   {
      return adjectiveOccurrenceHashMap;
   }
   /**
    * @param adjectiveOccurrenceHashMap the adjectiveOccurrenceHashMap to set
    */
   public void setAdjectiveOccurrenceHashMap(HashMap<String, Integer> adjectiveOccurrenceHashMap)
   {
      this.adjectiveOccurrenceHashMap = adjectiveOccurrenceHashMap;
   }
   /**
    * @return the verbOccurrenceHashMap
    */
   public HashMap<String, Integer> getVerbOccurrenceHashMap()
   {
      return verbOccurrenceHashMap;
   }
   /**
    * @param verbOccurrenceHashMap the verbOccurrenceHashMap to set
    */
   public void setVerbOccurrenceHashMap(HashMap<String, Integer> verbOccurrenceHashMap)
   {
      this.verbOccurrenceHashMap = verbOccurrenceHashMap;
   }
   
}
