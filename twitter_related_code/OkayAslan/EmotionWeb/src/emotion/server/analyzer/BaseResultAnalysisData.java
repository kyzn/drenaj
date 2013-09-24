package emotion.server.analyzer;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseResultAnalysisData
{
   private ArrayList<ResultAnalysisData> resultAnalysisDataArraylist  = new ArrayList<ResultAnalysisData>();
   private HashMap<String, Integer> allVerbOccHashMap = new HashMap<String, Integer>();
   private HashMap<String, Integer> allNounOccHashMap = new HashMap<String, Integer>();
   private HashMap<String, ResultAnalysisData> resultAnalysisDataHashMap = new HashMap<String, ResultAnalysisData>();
   
   /**
    * @return the resultAnalysisDataArraylist
    */
   public ArrayList<ResultAnalysisData> getResultAnalysisDataArraylist()
   {
      return resultAnalysisDataArraylist;
   }
   /**
    * @param resultAnalysisDataArraylist the resultAnalysisDataArraylist to set
    */
   public void setResultAnalysisDataArraylist(ArrayList<ResultAnalysisData> resultAnalysisDataArraylist)
   {
      this.resultAnalysisDataArraylist = resultAnalysisDataArraylist;
   }
   /**
    * @return the allVerbOccHashMap
    */
   public HashMap<String, Integer> getAllVerbOccHashMap()
   {
      return allVerbOccHashMap;
   }
   /**
    * @param allVerbOccHashMap the allVerbOccHashMap to set
    */
   public void setAllVerbOccHashMap(HashMap<String, Integer> allVerbOccHashMap)
   {
      this.allVerbOccHashMap = allVerbOccHashMap;
   }
   /**
    * @param allNounOccHashMap the allNounOccHashMap to set
    */
   public void setAllNounOccHashMap(HashMap<String, Integer> allNounOccHashMap)
   {
      this.allNounOccHashMap = allNounOccHashMap;
   }
   /**
    * @return the allNounOccHashMap
    */
   public HashMap<String, Integer> getAllNounOccHashMap()
   {
      return allNounOccHashMap;
   }
   /**
    * @param resultAnalysisDataHashMap the resultAnalysisDataHashMap to set
    */
   public void setResultAnalysisDataHashMap(HashMap<String, ResultAnalysisData> resultAnalysisDataHashMap)
   {
      this.resultAnalysisDataHashMap = resultAnalysisDataHashMap;
   }
   /**
    * @return the resultAnalysisDataHashMap
    */
   public HashMap<String, ResultAnalysisData> getResultAnalysisDataHashMap()
   {
      return resultAnalysisDataHashMap;
   } 
}
