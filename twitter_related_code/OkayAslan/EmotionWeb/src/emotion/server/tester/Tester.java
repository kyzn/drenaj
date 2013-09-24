/**
 * 
 */
package emotion.server.tester;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;

import twitter4j.TwitterException;
import emotion.server.EmotionWebServiceImpl;
import emotion.server.analyzer.Analyzer;
import emotion.server.dbPedia.DBPediaImpl;
import emotion.server.libstemmer.snowball.ext.englishStemmer;
import emotion.server.mediaWiki.MediaWikiImpl;
import emotion.server.twitter.TwitterDBImpl;
import emotion.server.twitter.TwitterImpl;
import emotion.server.util.HashMapUtil;
import emotion.server.util.TweetUtil;
import emotion.server.util.WordUtil;
import emotion.server.wordNet.WordNetDBImpl;
import emotion.server.wordNet.WordNetData;
import emotion.shared.EmoNvNTriplets;
import emotion.shared.EmoTweet;
import emotion.shared.EmoTweetResultData;
import emotion.shared.EmoTweetWordData;

/**
 * @author okay
 *
 */
public class Tester
{
   private static DBPediaImpl dbpediaImpl = new DBPediaImpl();
   private static TwitterDBImpl twitterDBImpl = TwitterDBImpl.getInstance(); 
   private static TweetUtil tweetUtil = new TweetUtil();
   private static HashMapUtil hMapUtil = new HashMapUtil();
   private static MediaWikiImpl mediaWikiImpl = new MediaWikiImpl();
   private static HashMap<String,EmoTweetResultData> resultMap = new HashMap<String,EmoTweetResultData>();
   private static HashMap<String, Integer> sortedEmotionHashMapByWord = new HashMap<String, Integer>();
   private static WordNetDBImpl wordNetdbImpl = WordNetDBImpl.getInstance();
   private static TesterImpl testerImpl = new TesterImpl();
   private static WordUtil wordUtil = new WordUtil();
   private static HashMap<String,EmoTweetWordData> wordDataMap = new HashMap<String,EmoTweetWordData>();
   private static HashMap<String, Integer> emotionHashMapByWord = new HashMap<String, Integer>();
   private static englishStemmer stemmer = new englishStemmer();
   private static TesterHelper testerHelper = new TesterHelper();
   private static TwitterImpl twitterImpl = TwitterImpl.getInstance();
   private static HashMap<String,WordNetData> wordNetMap = new HashMap<String,WordNetData>();
   private static Analyzer analyzer = new Analyzer();
   private static String DATE_FORMAT_FOR_DISPLAY   = "dd-MMMMM-yyyy_hh-mm-ss";
   private static SimpleDateFormat simpleDataFormatterForDisplay = new SimpleDateFormat(DATE_FORMAT_FOR_DISPLAY,Locale.US);
   public static final String NL = System.getProperty("line.separator"); 
   private static EmotionWebServiceImpl emoWebSvcImpl = new EmotionWebServiceImpl();
   /**
    * @param args
    */
   public static void main(String[] args)
   {
   //   fire("lost_23",23,"tw_lost_tb");
    //  fire("oilspill_02",02,"tw_oilspill_tb");
      
      //(new File("resultData//"+"barca")).mkdir();
     // (new File("resultData//"+"barca")).mkdir();
     //savejobs();
  //   filerEmo();
    // emofire();
   //  fire("w3c_all",0,"tw_w3c_tb");
    // firetest();
   //  TesterAffect.startAffective();
   //  fireByDayAndHourTest("israel_May",31,00,12,"tw_israel_tb");
   //  fireByDayAndHourTest("israel_May",31,12,24,"tw_israel_tb");
     //fire("barcelona_31",31,"tw_barcelona_tb");
     //filer();
      int a = 485;
      int b = 45;
      System.out.println(  (float)b/a  );
      
    //  saveNewJobs();
    //  insertDB("israel");
      //specialFire();
     // fireByDayAndHour("oilspill_30",30,8,18,"tw_oilspill_stb");
     //specialDBInsert();
     //insertAlltoDB();
      //insertAlltoDBByTableName();
      // fire("barcelona_31",31,"tw_barcelona_tb");
      // fire("moscowMetro_30",30,"tw_moscowmetro_tb");
      // insertDB();
   }
   
   private static void saveNewJobs()
   {
      try
      {
         emoWebSvcImpl.saveJob("eduWeb"); // July 26 — 27 - 28, 2010, Chicago
         emoWebSvcImpl.saveJob("WINSYS"); // July 26 — 27, 2010, Athens 
         emoWebSvcImpl.saveJob("Balisage");//August 3 — 6, 2010, Montréal, Canada 
         emoWebSvcImpl.saveJob("The Dead Weather"); //Jul 22, 2010  8:00 pm San Fransisco
         emoWebSvcImpl.saveJob("Jazz Festival"); //Wednesday, Aug 4 7:00p to 9:00p  San Fransisco
         emoWebSvcImpl.saveJob("New York");
         emoWebSvcImpl.saveJob("San Fransisco");
         emoWebSvcImpl.saveJob("palo alto");
         emoWebSvcImpl.saveJob("unesco");
         emoWebSvcImpl.saveJob("berkeley");
         emoWebSvcImpl.saveJob("second life");
      }
      catch (Exception e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
   }

   public static void filerEmo()
   {
     // String emoWord = "Adoration, affection, love, fondness, liking, attraction, caring, tenderness, compassion, sentimentality,   Arousal, desire, lust, passion, infatuation, Longing";
    //  String emoWord = "Amusement, bliss, cheerfulness, gaiety, glee, jolliness, joviality, joy, delight, enjoyment, gladness, happiness, jubilation, elation, satisfaction, ecstasy, euphoria,    Enthusiasm, zeal, zest, excitement, thrill, exhilaration,Contentment, pleasure,Pride, triumph,  Eagerness, hope, optimism,    Enthrallment, rapture,  Relief";
   //   String emoWord = "Aggravation, irritation, agitation, annoyance, grouchiness, grumpiness,    Exasperation, frustration,Anger, rage, outrage, fury, wrath, hostility, ferocity, bitterness, hate, loathing, scorn, spite, vengefulness, dislike, resentment,Disgust, revulsion, contempt,  Envy, jealousy,Torment";
   //   String emoWord = "   Agony, suffering, hurt, anguish,Depression, despair, hopelessness, gloom, glumness, sadness, unhappiness, grief, sorrow, woe, misery, melancholy,Dismay, disappointment, displeasure,  Guilt, shame, regret, remorse,Alienation, isolation, neglect, loneliness, rejection, homesickness, defeat, dejection, insecurity, embarrassment, humiliation, insult,Pity, sympathy";
      String emoWord = "   Alarm, shock, fear, fright, horror, terror, panic, hysteria, mortification,Anxiety, nervousness, tenseness, uneasiness, apprehension, worry, distress, dread";

      
      String wordArr[] = emoWord.split(",");
      System.out.print("public final static String[] PARROT_FEAR_WORDS_LIST={ " );
      for (int i = 1; i < wordArr.length; i++)
      {
         String string = wordArr[i];
         System.out.print("\""+string.trim().toLowerCase()+"\", ");
      }
      System.out.print("};" );
   }   
   
   
   public static void filer()
   {
      FileInputStream fstream;
      BufferedReader bufferedReader;
      String strLine;
      String token;
      try
      {
         fstream = new FileInputStream("data/surprise.txt");
         DataInputStream inTest = new DataInputStream(fstream);
         bufferedReader = new BufferedReader(new InputStreamReader(inTest));
         
         System.out.print("public final static String[] ALL_SURPRISE_WORDS_LIST={ " );
         while ((strLine = bufferedReader.readLine()) != null)   
         {
            String wordArr[] = strLine.split(" ");
            for (int i = 1; i < wordArr.length; i++)
            {
               String string = wordArr[i];
               System.out.print("\""+string+"\", ");
            }
         }
         System.out.print("};" );
      }
      catch (FileNotFoundException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
   }
   
    
   private static void savejobs()
   {
      try
      {
       //  emoWebSvcImpl.saveJob("vuvuzela");
       //  emoWebSvcImpl.saveJob("michael jackson");
       //  emoWebSvcImpl.saveJob("sonisphere");
         emoWebSvcImpl.saveJob("W3C");
      //   emoWebSvcImpl.saveJob("argentina");
      //   emoWebSvcImpl.saveJob("messi");
         emoWebSvcImpl.saveJob("istanbul");
         emoWebSvcImpl.saveJob("ist2010");
       //  emoWebSvcImpl.saveJob("BBQKALBI");
      //   emoWebSvcImpl.saveJob("MoGoBBQ");
      //   emoWebSvcImpl.saveJob("brazil");
     //    emoWebSvcImpl.saveJob("germany");
    //     emoWebSvcImpl.saveJob("Last Airbender");
         emoWebSvcImpl.saveJob("New York");
         
         emoWebSvcImpl.saveJob("vuvuzela");
         emoWebSvcImpl.saveJob("ICCSIT 2010");
         emoWebSvcImpl.saveJob("Semantic");
         emoWebSvcImpl.saveJob("spain"); 
         emoWebSvcImpl.saveJob("netherlands");
         
         
         
         
         
      //   
      /*   
         emoWebSvcImpl.saveJob("wwdc");
         emoWebSvcImpl.saveJob("israel");
         emoWebSvcImpl.saveJob("flotilla");
         emoWebSvcImpl.saveJob("gaza");
         emoWebSvcImpl.saveJob("Oil Spill");
         */
      }
      catch (Exception e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }



   private static void specialDBInsert()
   {
      insertDBByTableName("oilspill");
      insertDB("Oil Spill");
   }
   
   private static void specialFire()
   {
      fireByDayAndHour("oilspill_30_8_18",30,8,18,"tw_oilspill_stb");
      fireByDayAndHour("oil_spill_30_8_18",30,8,18,"tw_oilspill_tb");
   }   

   private static void firetest()
   {  /* 
      fireByDayAndHourTest("israel_May",30,00,12,"tw_israel_tb");
      fireByDayAndHourTest("israel_May",30,12,24,"tw_israel_tb");
      fireByDayAndHourTest("israel_May",31,00,12,"tw_israel_tb");
      fireByDayAndHourTest("israel_May",31,12,24,"tw_israel_tb");
      fireByDayAndHourTest("israel_Jun",01,00,12,"tw_israel_tb");
      fireByDayAndHourTest("israel_Jun",01,12,24,"tw_israel_tb");
      fireByDayAndHourTest("israel_Jun",02,00,12,"tw_israel_tb");
      fireByDayAndHourTest("israel_Jun",02,12,24,"tw_israel_tb");
      fireByDayAndHourTest("israel_Jun",03,00,12,"tw_israel_tb");
      fireByDayAndHourTest("israel_Jun",03,12,24,"tw_israel_tb");
      fireByDayAndHourTest("israel_Jun",04,00,12,"tw_israel_tb");
      fireByDayAndHourTest("israel_Jun",04,12,24,"tw_israel_tb");
 
      fireByDayAndHourTest("gaza_May",30,00,12,"tw_gaza_tb");
      fireByDayAndHourTest("gaza_May",30,12,24,"tw_gaza_tb");      
      fireByDayAndHourTest("gaza_May",30,00,12,"tw_gaza_tb");
      fireByDayAndHourTest("gaza_May",30,12,24,"tw_gaza_tb");
      fireByDayAndHourTest("gaza_May",31,00,12,"tw_gaza_tb");
      fireByDayAndHourTest("gaza_May",31,12,24,"tw_gaza_tb");
      fireByDayAndHourTest("gaza_Jun",01,00,12,"tw_gaza_tb");
      */
      fireByDayAndHourTest("gaza_Jun",01,12,24,"tw_gaza_tb");
      fireByDayAndHourTest("gaza_Jun",02,00,12,"tw_gaza_tb");
      fireByDayAndHourTest("gaza_Jun",02,12,24,"tw_gaza_tb");
      fireByDayAndHourTest("gaza_Jun",03,00,12,"tw_gaza_tb");
      fireByDayAndHourTest("gaza_Jun",03,12,24,"tw_gaza_tb");
      fireByDayAndHourTest("gaza_Jun",04,00,12,"tw_gaza_tb");
      fireByDayAndHourTest("gaza_Jun",04,12,24,"tw_gaza_tb");
   }   

   private static void emofire()
   {
    //  fireByDayAndHourTest("wwdc_June",7,12,24,"tw_wwdc_tb");
      
      
      fireByDayAndHourTest("vuvuzela_July",1,12,24,"tw_vuvuzela_tb");
      fireByDayAndHourTest("vuvuzela_July",2,00,12,"tw_vuvuzela_tb");
      fireByDayAndHourTest("vuvuzela_July",2,12,24,"tw_vuvuzela_tb");
      fireByDayAndHourTest("vuvuzela_July",3,00,12,"tw_vuvuzela_tb");
      fireByDayAndHourTest("vuvuzela_July",3,12,24,"tw_vuvuzela_tb");
      fireByDayAndHourTest("vuvuzela_July",4,00,12,"tw_vuvuzela_tb");
      fireByDayAndHourTest("vuvuzela_July",4,12,24,"tw_vuvuzela_tb");
      fireByDayAndHourTest("vuvuzela_July",5,00,12,"tw_vuvuzela_tb");
      fireByDayAndHourTest("vuvuzela_July",5,12,24,"tw_vuvuzela_tb");
      fireByDayAndHourTest("vuvuzela_July",6,00,12,"tw_vuvuzela_tb");
      fireByDayAndHourTest("vuvuzela_July",6,12,24,"tw_vuvuzela_tb");
      fireByDayAndHourTest("vuvuzela_July",7,00,12,"tw_vuvuzela_tb");
      fireByDayAndHourTest("vuvuzela_July",7,12,24,"tw_vuvuzela_tb");
      fireByDayAndHourTest("vuvuzela_July",8,00,12,"tw_vuvuzela_tb");
      fireByDayAndHourTest("vuvuzela_July",8,12,24,"tw_vuvuzela_tb");
      
 /*     
      fireByDayAndHourTest("messi_July",1,00,12,"tw_messi_tb");
      fireByDayAndHourTest("messi_July",1,12,24,"tw_messi_tb");
      fireByDayAndHourTest("messi_July",2,00,12,"tw_messi_tb");
      fireByDayAndHourTest("messi_July",2,12,24,"tw_messi_tb");
      fireByDayAndHourTest("messi_July",3,00,12,"tw_messi_tb");
      fireByDayAndHourTest("messi_July",3,12,24,"tw_messi_tb");
      fireByDayAndHourTest("messi_July",4,00,12,"tw_messi_tb");
      fireByDayAndHourTest("messi_July",4,12,24,"tw_messi_tb");
      fireByDayAndHourTest("messi_July",5,00,12,"tw_messi_tb");
      fireByDayAndHourTest("messi_July",5,12,24,"tw_messi_tb");
      fireByDayAndHourTest("messi_July",6,00,12,"tw_messi_tb");
      fireByDayAndHourTest("messi_July",6,12,24,"tw_messi_tb");
      fireByDayAndHourTest("messi_July",7,00,12,"tw_messi_tb");
      fireByDayAndHourTest("messi_July",7,12,24,"tw_messi_tb");
      fireByDayAndHourTest("messi_July",8,00,12,"tw_messi_tb");
      fireByDayAndHourTest("messi_July",8,12,24,"tw_messi_tb");
   */   
   }   
   

   private static void fireall()
   {
      fire("joselima_23",23,"tw_joselima_tb");
      fire("joselima_24",24,"tw_joselima_tb");
      fire("joselima_25",25,"tw_joselima_tb");
      
      fire("garycoleman_29",29,"tw_garycoleman_tb");
      fire("garycoleman_30",30,"tw_garycoleman_tb");

      fire("dennishopper_30",30,"tw_dennishopper_tb");
      fire("dennishopper_31",31,"tw_dennishopper_tb");
      
       fire("barcelona_30",30,"tw_barcelona_tb");
       fire("barcelona_31",31,"tw_barcelona_tb");
       fire("barcelona_01",01,"tw_barcelona_tb");
       fire("arsenal_30",30,"tw_arsenal_tb");
       fire("arsenal_31",31,"tw_arsenal_tb");
       fire("arsenal_01",01,"tw_arsenal_tb");
       fire("eurovision_30",30,"tw_eurovision_tb");
       fire("torontoconference_all",0,"tw_torontoconference_tb");
       
       
       fire("oilspill_30",30,"tw_oilspill_stb");
       fire("oilspill_31",31,"tw_oilspill_stb");
       
       fire("israel_31",31,"tw_israel_tb");
       fire("israel_01",01,"tw_israel_tb");
       
       fire("gaza_31",31,"tw_gaza_tb");
       fire("gaza_01",01,"tw_gaza_tb");
       
       fire("flotilla_31",31,"tw_flotilla_tb");
       fire("flotilla_01",01,"tw_flotilla_tb");      
       
       fire("volcano_17",17,"tw_volcano_tb");
       fire("volcano_18",18,"tw_volcano_tb");
       fire("volcano_19",19,"tw_volcano_tb");
       
       fire("moscowmetro_29",29,"tw_moscowmetro_tb");
       fire("moscowmetro_30",30,"tw_moscowmetro_tb");
       fire("moscowmetro_31",31,"tw_moscowmetro_tb");
       fire("moscowmetro_01",01,"tw_moscowmetro_tb");
       
       fire("earthquake_05",05,"tw_earthquake_tb");
       fire("earthquake_06",06,"tw_earthquake_tb");
       fire("earthquake_14",14,"tw_earthquake_tb");
       fire("earthquake_15",15,"tw_earthquake_tb");
       
       fire("lost_24",24,"tw_lost_tb");
       fire("lost_25",25,"tw_lost_tb");
       fire("lost_26",26,"tw_lost_tb");
       fire("lost_27",27,"tw_lost_tb");
   }



   private static void NvNTripEx()
   {
      LinkedHashMap<String, Integer> verticeHashMap = new LinkedHashMap<String, Integer>();
      verticeHashMap.put("A", 5);
      verticeHashMap.put("B", 2);
      verticeHashMap.put("C", 2);
      verticeHashMap.put("D", 6);
      verticeHashMap.put("E", 8);
      verticeHashMap.put("F", 3);
      
      ArrayList<EmoNvNTriplets> emoNvNTripletsArrList = new ArrayList<EmoNvNTriplets>(3);
      EmoNvNTriplets emoNvNTriplets1 = new EmoNvNTriplets();
      EmoNvNTriplets emoNvNTriplets2 = new EmoNvNTriplets();
      EmoNvNTriplets emoNvNTriplets3 = new EmoNvNTriplets();
      EmoNvNTriplets emoNvNTriplets4 = new EmoNvNTriplets();
      EmoNvNTriplets emoNvNTriplets5 = new EmoNvNTriplets();
      EmoNvNTriplets emoNvNTriplets6 = new EmoNvNTriplets();
      
      fillNvNTriplets(emoNvNTriplets1,"A","B","verb1",25);
      fillNvNTriplets(emoNvNTriplets2,"B","C","verb2",8);
      fillNvNTriplets(emoNvNTriplets3,"D","E","verb3",15);
      fillNvNTriplets(emoNvNTriplets4,"A","E","verb4",1);
      fillNvNTriplets(emoNvNTriplets5,"C","F","verb5",5);
      fillNvNTriplets(emoNvNTriplets6,"A","C","verb6",7);
      
      emoNvNTripletsArrList.add(emoNvNTriplets1);
      emoNvNTripletsArrList.add(emoNvNTriplets2);
      emoNvNTripletsArrList.add(emoNvNTriplets3);
      emoNvNTripletsArrList.add(emoNvNTriplets4);
      emoNvNTripletsArrList.add(emoNvNTriplets5);
      emoNvNTripletsArrList.add(emoNvNTriplets6);
      
      writeToFile("test",verticeHashMap,emoNvNTripletsArrList);
   }


   private static void writeToFile(String keyName, HashMap<String,Integer> verticeHashMap, ArrayList<EmoNvNTriplets> emoNvNTripletsArrList)
   {
      Calendar cal=Calendar.getInstance(Locale.US);
      HashMap<String, Integer> verticeToKey = new HashMap<String, Integer>();
      String displayDate = simpleDataFormatterForDisplay.format(cal.getTime());
      FileWriter fstream;
      try
      {
         fstream = new FileWriter(keyName+"_"+displayDate+".net");
         BufferedWriter out = new BufferedWriter(fstream);
         out.write("*Vertices "+verticeHashMap.size()+NL);
         Iterator<String> HashmapIterator = verticeHashMap.keySet().iterator();
         int verticeCount=1;
         while(HashmapIterator.hasNext()) 
         {
            String key = HashmapIterator.next(); 
            Integer val = verticeHashMap.get(key);
            out.write(verticeCount+" \""+key+"/"+val+"\" x_fact "+((float)val)/10+" y_fact "+((float)val)/10+" ic Blue bc Black"+NL);
            verticeToKey.put(key, verticeCount);
            verticeCount++;
         }
         out.write("*Arcs"+NL);
         for (Iterator<EmoNvNTriplets> iterator = emoNvNTripletsArrList.iterator(); iterator.hasNext();)
         {
            EmoNvNTriplets emoNvNTriplets = (EmoNvNTriplets) iterator.next();
            out.write(verticeToKey.get(emoNvNTriplets.getFirstNoun())+" "+verticeToKey.get(emoNvNTriplets.getSecondNoun())+" "+((float)emoNvNTriplets.getStrength())/10+" l \""+emoNvNTriplets.getVerb()+"/"+emoNvNTriplets.getStrength()+"\" c Red lc Black"+NL); 
         }
         out.close();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }


   private static void fillNvNTriplets(EmoNvNTriplets emoNvNTriplets,String firstNoun,String secondNoun,String verb,int strength)
   {
      emoNvNTriplets.setFirstNoun(firstNoun);
      emoNvNTriplets.setSecondNoun(secondNoun);
      emoNvNTriplets.setVerb(verb);
      emoNvNTriplets.setStrength(strength);
      
   }
   
   private static void fireByDayAndHourTest(String word,int day,int beginH, int finishH, String tableName)
   {
      ArrayList<EmoTweet> allTweetArrList = twitterDBImpl.getAllTweetsFromTables(tableName);
      if(day!=0)
      {
         ArrayList<EmoTweet> chosenTweetArrList = new ArrayList<EmoTweet>();
         for (Iterator<EmoTweet> iterator = allTweetArrList.iterator(); iterator.hasNext();)
         {
            EmoTweet emoTweet = (EmoTweet) iterator.next();
            Calendar cal=Calendar.getInstance(Locale.US);
            cal.setTime(emoTweet.getTweetDate());
            if(cal.get(Calendar.DAY_OF_MONTH)==day)
            {
               cal.setTime(emoTweet.getTweetTime());
               if(cal.get(Calendar.HOUR_OF_DAY)>=beginH && finishH>cal.get(Calendar.HOUR_OF_DAY))
               {
                chosenTweetArrList.add(emoTweet);
               }
            }   
         }
         analyzer.analyze(word+"_"+day+"_"+beginH+"-"+finishH,chosenTweetArrList,true);
      }
      else
      {
         analyzer.analyze(word,allTweetArrList,true);
      } 
   }   
   
   

   private static void fireByDayAndHour(String fileName,int day,int beginH, int finishH, String tableName)
   {
      try
      {
         FileWriter fstream = new FileWriter("PajecData//tweetNum_"+fileName+".txt");
         BufferedWriter out = new BufferedWriter(fstream);
         ArrayList<EmoTweet> allTweetArrList = twitterDBImpl.getAllTweetsFromTables(tableName);
         if(day!=0)
         {
            ArrayList<EmoTweet> chosenTweetArrList = new ArrayList<EmoTweet>();
            for (Iterator<EmoTweet> iterator = allTweetArrList.iterator(); iterator.hasNext();)
            {
               EmoTweet emoTweet = (EmoTweet) iterator.next();
               Calendar cal=Calendar.getInstance(Locale.US);
               cal.setTime(emoTweet.getTweetDate());
               if(cal.get(Calendar.DAY_OF_MONTH)==day)
               {
                  cal.setTime(emoTweet.getTweetTime());
                  if(cal.get(Calendar.HOUR_OF_DAY)>beginH && finishH>cal.get(Calendar.HOUR_OF_DAY))
                  {
                   chosenTweetArrList.add(emoTweet);
                  }
               }   
            }
            out.write("Total number of Tweets:"+chosenTweetArrList.size());
            out.close();
            analyzer.analyze("PajecData//"+fileName,chosenTweetArrList,true);
         }
         else
         {
            out.write("Total number of Tweets:"+allTweetArrList.size());
            out.close();
            analyzer.analyze("PajecData//"+fileName,allTweetArrList,true);
         } 
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }  
   }    
     

   private static void fire(String fileName, int day, String tableName)
   {
      try
      {
         FileWriter fstream = new FileWriter("PajecData//tweetNum_"+fileName+".txt");
         BufferedWriter out = new BufferedWriter(fstream);
         ArrayList<EmoTweet> allTweetArrList = twitterDBImpl.getAllTweetsFromTables(tableName);
         if(day!=0)
         {
            ArrayList<EmoTweet> chosenTweetArrList = new ArrayList<EmoTweet>();
            for (Iterator<EmoTweet> iterator = allTweetArrList.iterator(); iterator.hasNext();)
            {
               EmoTweet emoTweet = (EmoTweet) iterator.next();
               Calendar cal=Calendar.getInstance(Locale.US);
               cal.setTime(emoTweet.getTweetDate());
               if(cal.get(Calendar.DAY_OF_MONTH)==day)
               {
                  chosenTweetArrList.add(emoTweet);
               }   
            }
            out.write("Total number of Tweets:"+chosenTweetArrList.size());
            out.close();
            analyzer.analyze(fileName,chosenTweetArrList,true);
         }
         else
         {
            out.write("Total number of Tweets:"+allTweetArrList.size());
            out.close();
            analyzer.analyze(fileName,allTweetArrList,true);
         } 
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }  
   }   
   
   
   
   
//   {
      //  getTweets("toronto conference");
      //    getAndAnalysisTweets("toronto conference");
       //   ArrayList<EmoTweet> emoTweetArrList= twitterDBImpl.getAllTweetsFromTables("tw_torontoconference_tb");
       //   analyzeTweets(emoTweetArrList);
      //   startAffective();
     //    startSingle("Allen and Okur get All-Star call");
//   }
   
   
   private static void insertAlltoDB()
   {
      insertDB("israel");
    //  insertDB("eurovision");
      insertDB("toronto conference");
    //  insertDB("Oil Spill");
   //   insertDB("Jose Lima");
    //  insertDB("lost");
      insertDB("iste2010");
   //   insertDB("Gary Coleman");
      insertDB("Dennis Hopper");
      
   }
   
   private static void insertAlltoDBByTableName()
   {
      insertDBByTableName("oilspill");
   }
   
   
   private static void insertDBByTableName(String string)
   {
      try
      {
         twitterImpl.insertTweetstoDBByTableName(twitterImpl.searchWord(string),string,"tw_"+string+"_stb");
      }
      catch (TwitterException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   private static void insertDB(String string)
   {
      try
      {
         twitterImpl.insertTweetstoDB(twitterImpl.searchWord(string),string);
      }
      catch (TwitterException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

}
