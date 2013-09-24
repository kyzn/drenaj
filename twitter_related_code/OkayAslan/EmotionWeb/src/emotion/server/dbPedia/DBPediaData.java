package emotion.server.dbPedia;

import java.util.HashMap;

public class DBPediaData
{
   private String property = null;
   private String hasValue = null;
   private String isValueOf = null;
   private String propertyResourceUrl = null;
   private String hasValueResourceUrl = null;
   private String isValueOfResourceUrl = null;
   private HashMap<String,String> propertyWordHashMap = new HashMap<String, String>();;
   private HashMap<String,String> hasValueWordHashMap  = new HashMap<String, String>();;
   private HashMap<String,String> isValueOWordfHashMap  = new HashMap<String, String>();;
   
   /**
    * @return the property
    */
   public String getProperty()
   {
      return property;
   }
   /**
    * @return the hasValue
    */
   public String getHasValue()
   {
      return hasValue;
   }
   /**
    * @return the isValueOf
    */
   public String getIsValueOf()
   {
      return isValueOf;
   }
   /**
    * @return the propertyResourceUrl
    */
   public String getPropertyResourceUrl()
   {
      return propertyResourceUrl;
   }
   /**
    * @return the hasValueResourceUrl
    */
   public String getHasValueResourceUrl()
   {
      return hasValueResourceUrl;
   }
   /**
    * @return the isValueOfResourceUrl
    */
   public String getIsValueOfResourceUrl()
   {
      return isValueOfResourceUrl;
   }
   /**
    * @param property the property to set
    */
   public void setProperty(String property)
   {
      this.property = property;
   }
   /**
    * @param hasValue the hasValue to set
    */
   public void setHasValue(String hasValue)
   {
      this.hasValue = hasValue;
   }
   /**
    * @param isValueOf the isValueOf to set
    */
   public void setIsValueOf(String isValueOf)
   {
      this.isValueOf = isValueOf;
   }
   /**
    * @param propertyResourceUrl the propertyResourceUrl to set
    */
   public void setPropertyResourceUrl(String propertyResourceUrl)
   {
      this.propertyResourceUrl = propertyResourceUrl;
      setProperty(splitAndReturn(propertyResourceUrl,propertyWordHashMap));
   }
   /**
    * @param hasValueResourceUrl the hasValueResourceUrl to set
    */
   public void setHasValueResourceUrl(String hasValueResourceUrl)
   {
      this.hasValueResourceUrl = hasValueResourceUrl;
      setHasValue(splitAndReturn(hasValueResourceUrl,hasValueWordHashMap));
   }
   /**
    * @param isValueOfResourceUrl the isValueOfResourceUrl to set
    */
   public void setIsValueOfResourceUrl(String isValueOfResourceUrl)
   {
      this.isValueOfResourceUrl = isValueOfResourceUrl;
      setIsValueOf(splitAndReturn(isValueOfResourceUrl,isValueOWordfHashMap));
   }
   
   private String splitAndReturn(String resUrl, HashMap<String, String> wordHashMap)
   {
      String root = null;
      if(resUrl!=null)
      {
         String wordArr[] = resUrl.split("/");
         String lastWord = wordArr[(wordArr.length)-1];
         String meanWord[] = lastWord.split("_");
         StringBuffer strBuffer = new StringBuffer();
         strBuffer.append(meanWord[0]);
      //   wordHashMap = new HashMap<String, String>();
         wordHashMap.put(meanWord[0].toLowerCase(), meanWord[0].toLowerCase());
         for (int i = 1; i < meanWord.length; i++)
         {
            strBuffer.append(" " + meanWord[i]);
            wordHashMap.put(meanWord[i].toLowerCase(), meanWord[i].toLowerCase());
         }
         root = strBuffer.toString();
      }
      return root;
   }
   /**
    * @return the propertyWordHashMap
    */
   public HashMap<String, String> getPropertyWordHashMap()
   {
      return propertyWordHashMap;
   }
   /**
    * @return the hasValueWordHashMap
    */
   public HashMap<String, String> getHasValueWordHashMap()
   {
      return hasValueWordHashMap;
   }
   /**
    * @return the isValueOWordfHashMap
    */
   public HashMap<String, String> getIsValueOWordfHashMap()
   {
      return isValueOWordfHashMap;
   }
   /**
    * @param propertyWordHashMap the propertyWordHashMap to set
    */
   public void setPropertyWordHashMap(HashMap<String, String> propertyWordHashMap)
   {
      this.propertyWordHashMap = propertyWordHashMap;
   }
   /**
    * @param hasValueWordHashMap the hasValueWordHashMap to set
    */
   public void setHasValueWordHashMap(HashMap<String, String> hasValueWordHashMap)
   {
      this.hasValueWordHashMap = hasValueWordHashMap;
   }
   /**
    * @param isValueOWordfHashMap the isValueOWordfHashMap to set
    */
   public void setIsValueOWordfHashMap(HashMap<String, String> isValueOWordfHashMap)
   {
      this.isValueOWordfHashMap = isValueOWordfHashMap;
   }

}
