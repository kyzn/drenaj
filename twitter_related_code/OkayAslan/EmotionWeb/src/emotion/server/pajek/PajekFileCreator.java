package emotion.server.pajek;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import emotion.shared.EmoNvNTriplets;

public class PajekFileCreator
{
   private static String DATE_FORMAT_FOR_DISPLAY   = "dd-MMMMM-yyyy_HH-mm-ss";
   private static SimpleDateFormat simpleDataFormatterForDisplay = new SimpleDateFormat(DATE_FORMAT_FOR_DISPLAY,Locale.US);
   private Calendar cal=Calendar.getInstance(Locale.US);
   private static final String NL = System.getProperty("line.separator");   
   private static float diamondFactor;
   private static float ellipseFactor;
   private static float relationFactor;
   private static float ellipseConstant = 100;
   private static float diamondConstant = 50;

   
   private String path = null;

   public PajekFileCreator(String fileName, float ellipseSize, float diamondSize)
   {
      ellipseFactor = (float)ellipseConstant/ellipseSize;
      diamondFactor = diamondConstant/diamondSize;
      relationFactor = diamondFactor/5;
      
      this.path="resultData//"+fileName;
      boolean success = (new File(path)).mkdir();
      if (success) {
        System.out.println("Directory: " + path + " created");
      }
   }
   
   public void writeToFileWithEdges(String fileName, HashMap<String,Integer> verticeEllipseValueHashMap,HashMap<String, Integer> verticeDiamondValueHashMap, ArrayList<EmoNvNTriplets> emoNvNTripletsArrList)
   {
      HashMap<String, Integer> verticeToKey = new HashMap<String, Integer>();
      String displayDate = simpleDataFormatterForDisplay.format(cal.getTime());
      int verticeCount=1;
      int verticeSize= verticeEllipseValueHashMap.size()+verticeDiamondValueHashMap.size();
      
      FileWriter fstream;
      try
      {
         fstream = new FileWriter(path+"//"+fileName+"_"+displayDate+".net");
         BufferedWriter out = new BufferedWriter(fstream);
         
         out.write("*Vertices "+verticeSize+NL);
         
         Iterator<String> verticeEllipseValueHashMapIterator = verticeEllipseValueHashMap.keySet().iterator();
         while(verticeEllipseValueHashMapIterator.hasNext()) 
         {
            String key = verticeEllipseValueHashMapIterator.next(); 
            Integer val = verticeEllipseValueHashMap.get(key);
            out.write(verticeCount+" \""+key+"/"+val+"\" x_fact "+(((float)val)*ellipseFactor)+" y_fact "+(((float)val)*ellipseFactor)+" ic Blue bc Black"+NL);
            verticeToKey.put(key, verticeCount);
            verticeCount++;
         }
         
         Iterator<String> verticeDiamondValueHashMapIterator = verticeDiamondValueHashMap.keySet().iterator();
         while(verticeDiamondValueHashMapIterator.hasNext()) 
         {
            String key = verticeDiamondValueHashMapIterator.next(); 
            Integer val = verticeDiamondValueHashMap.get(key);
            out.write(verticeCount+" \""+key+"/"+val+"\" diamond x_fact "+(((float)val)*diamondFactor)+" y_fact "+(((float)val)*diamondFactor)+" ic Green bc Black"+NL);
            verticeToKey.put(key, verticeCount);
            verticeCount++;
         }
         
         out.write("*Arcs"+NL);
         for (Iterator<EmoNvNTriplets> iterator = emoNvNTripletsArrList.iterator(); iterator.hasNext();)
         {
            EmoNvNTriplets emoNvNTriplets = (EmoNvNTriplets) iterator.next();
            out.write(verticeToKey.get(emoNvNTriplets.getFirstNoun())+" "+verticeToKey.get(emoNvNTriplets.getVerb())+" "+(((float)verticeDiamondValueHashMap.get(emoNvNTriplets.getVerb()))*relationFactor)+" l \""+(float)verticeDiamondValueHashMap.get(emoNvNTriplets.getVerb())+"\" c Red lc Black"+NL);
            out.write(verticeToKey.get(emoNvNTriplets.getVerb())+" "+verticeToKey.get(emoNvNTriplets.getSecondNoun())+" "+(((float)verticeDiamondValueHashMap.get(emoNvNTriplets.getVerb()))*relationFactor)+" l \""+(float)verticeDiamondValueHashMap.get(emoNvNTriplets.getVerb())+"\" c Red lc Black"+NL);
         }
         out.close();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }
 
 /*
   private void createFile(String fileName, HashMap<String,Integer> verticeEllipseValueHashMap,HashMap<String, Integer> verticeDiamondValueHashMap, ArrayList<EmoNvNTriplets> emoNvNTripletsArrList)
   {
      HashMap<String, Integer> verticeToKey = new HashMap<String, Integer>();
      String displayDate = simpleDataFormatterForDisplay.format(cal.getTime());
      int verticeCount=1;
      int verticeSize= verticeEllipseValueHashMap.size()+verticeDiamondValueHashMap.size();
      
      FileWriter fstream;
      try
      {
         fstream = new FileWriter(fileName+"_"+displayDate+".net");
         BufferedWriter out = new BufferedWriter(fstream);
         
         out.write("*Vertices "+verticeSize+NL);
         
         Iterator<String> verticeEllipseValueHashMapIterator = verticeEllipseValueHashMap.keySet().iterator();
         while(verticeEllipseValueHashMapIterator.hasNext()) 
         {
            String key = verticeEllipseValueHashMapIterator.next(); 
            Integer val = verticeEllipseValueHashMap.get(key);
            out.write(verticeCount+" \""+key+"/"+val+"\" x_fact "+((float)val)/10+" y_fact "+((float)val)/10+" ic Blue bc Black"+NL);
            verticeToKey.put(key, verticeCount);
            verticeCount++;
         }
         
         Iterator<String> verticeDiamondValueHashMapIterator = verticeDiamondValueHashMap.keySet().iterator();
         while(verticeDiamondValueHashMapIterator.hasNext()) 
         {
            String key = verticeDiamondValueHashMapIterator.next(); 
            Integer val = verticeDiamondValueHashMap.get(key);
            out.write(verticeCount+" \""+key+"/"+val+"\" diamond x_fact "+((float)val)/500+" y_fact "+((float)val)/500+" ic Green bc Black"+NL);
            verticeToKey.put(key, verticeCount);
            verticeCount++;
         }
         
         out.write("*Arcs"+NL);
         for (Iterator<EmoNvNTriplets> iterator = emoNvNTripletsArrList.iterator(); iterator.hasNext();)
         {
            EmoNvNTriplets emoNvNTriplets = (EmoNvNTriplets) iterator.next();
            out.write(verticeToKey.get(emoNvNTriplets.getFirstNoun())+" "+verticeToKey.get(emoNvNTriplets.getVerb())+" "+((float)verticeDiamondValueHashMap.get(emoNvNTriplets.toString()))/10+" l \""+(float)verticeDiamondValueHashMap.get(emoNvNTriplets.toString())+"\" c Red lc Black"+NL);
            out.write(verticeToKey.get(emoNvNTriplets.getVerb())+" "+verticeToKey.get(emoNvNTriplets.getSecondNoun())+" "+((float)verticeDiamondValueHashMap.get(emoNvNTriplets.toString()))/10+" l \""+(float)verticeDiamondValueHashMap.get(emoNvNTriplets.toString())+"\" c Red lc Black"+NL);
         }
         out.close();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }   
   
 */  
   
   
/*
   private void writeToFile(String keyName, HashMap<String,Integer> verticeHashMap, ArrayList<EmoNvNTriplets> emoNvNTripletsArrList, HashMap<String, Integer> verbStreghtHasMap)
   {
      
      HashMap<String, Integer> verticeToKey = new HashMap<String, Integer>();
      String displayDate = simpleDataFormatterForDisplay.format(cal.getTime());
      FileWriter fstream;
      try
      {
         fstream = new FileWriter(keyName+"_"+displayDate+".net");
         BufferedWriter out = new BufferedWriter(fstream);
         out.write("*Vertices "+ getVerticeSize(verticeHashMap,alreadyFoundWords,emoNvNTripletsArrList)+NL);
         Iterator<String> HashmapIterator = verticeHashMap.keySet().iterator();
         int verticeCount=1;
         while(HashmapIterator.hasNext()) 
         {
            String key = HashmapIterator.next(); 
            Integer val = verticeHashMap.get(key);
            if(!alreadyFoundWords.containsKey(key))
            {
               out.write(verticeCount+" \""+keyValHashMap.get(key)+"/"+val+"\" x_fact "+((float)val)/10+" y_fact "+((float)val)/10+" ic Blue bc Black"+NL);
               verticeToKey.put(keyValHashMap.get(key), verticeCount);
               verticeCount++;
            }
         }
         out.write("*Arcs"+NL);
         for (Iterator<EmoNvNTriplets> iterator = emoNvNTripletsArrList.iterator(); iterator.hasNext();)
         {
            EmoNvNTriplets emoNvNTriplets = (EmoNvNTriplets) iterator.next();
            out.write(verticeToKey.get(keyValHashMap.get(emoNvNTriplets.getFirstNoun()))+" "+verticeToKey.get(keyValHashMap.get(emoNvNTriplets.getSecondNoun()))+" "+((float)verbStreghtHasMap.get(emoNvNTriplets.toString()))/10+" l \""+emoNvNTriplets.getVerb()+"/"+(float)verbStreghtHasMap.get(emoNvNTriplets.toString())+"\" c Red lc Black"+NL); 
         }
         out.close();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }  
   
*/
   
   
}
