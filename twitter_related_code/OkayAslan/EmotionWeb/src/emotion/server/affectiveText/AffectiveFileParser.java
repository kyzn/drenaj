package emotion.server.affectiveText;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AffectiveFileParser
{
   private Document affectiveTrialDoc;
   private BufferedReader bufferedTrialReader;
   private Document affectiveTestDoc;
   private BufferedReader bufferedTestReader;

   public AffectiveFileParser()
   {
      init();
   }
   
   private void init()
   {
      // get the factory
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

      try
      {
         // Using factory get an instance of document builder
         DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();

         // parse using builder to get DOM representation of the XML file
         affectiveTestDoc = db.parse("data/affectivetext_test.xml");
         FileInputStream fstreamTest = new FileInputStream("data/affectivetext_test.emotions.gold");
         DataInputStream inTest = new DataInputStream(fstreamTest);
         bufferedTestReader = new BufferedReader(new InputStreamReader(inTest));
         
         affectiveTrialDoc = db.parse("data/affectivetext_trial.xml");
         FileInputStream fstream = new FileInputStream("data/affectivetext_trial.emotions.gold");
         DataInputStream in = new DataInputStream(fstream);
         bufferedTrialReader = new BufferedReader(new InputStreamReader(in));


      }
      catch (ParserConfigurationException pce)
      {
         pce.printStackTrace();
      }
      catch (SAXException se)
      {
         se.printStackTrace();
      }
      catch (IOException ioe)
      {
         ioe.printStackTrace();
      }
   }

   public ArrayList<AffectiveData> readStatements()
   {
      ArrayList<AffectiveData> affectiveDataArrayList = new ArrayList<AffectiveData>();
      ArrayList<AffectiveData> tempAffectiveDataArrList = fillStatementAndID();
      affectiveDataArrayList = fillEmotions(tempAffectiveDataArrList);
      return affectiveDataArrayList;
   }
      
   public ArrayList<AffectiveData> fillStatementAndID()
   {
      ArrayList<AffectiveData> affectiveDataArrList = new ArrayList<AffectiveData>();
      
      // get the root element
      Element docEle = affectiveTrialDoc.getDocumentElement();

      // get a nodelist of elements
      NodeList nl = docEle.getElementsByTagName("instance");
      
      if (nl != null && nl.getLength() > 0)
      {
         for (int i = 0; i < nl.getLength(); i++)
         {
            Element statementEl = (Element) nl.item(i);
            affectiveDataArrList.add(fillAffectiveData(statementEl));
         }
      }
      
      // get the root element
      Element docEle2 = affectiveTestDoc.getDocumentElement();

      // get a nodelist of elements
      NodeList nl2 = docEle2.getElementsByTagName("instance");
      
      if (nl2 != null && nl2.getLength() > 0)
      {
         for (int i = 0; i < nl2.getLength(); i++)
         {
            Element statementEl = (Element) nl2.item(i);
            affectiveDataArrList.add(fillAffectiveData(statementEl));
         }
      } 
      
      return affectiveDataArrList;
   }
   
   //id  anger disgust fear joy sadness surprise
   
   public ArrayList<AffectiveData> fillEmotions(ArrayList<AffectiveData> tempAffectiveDataArrList)
   {
      ArrayList<AffectiveData> affectiveDataArrList = new ArrayList<AffectiveData>();
      String strLine;
      AffectiveData tempAffectiveData;
      try
      {
         while ((strLine = bufferedTrialReader.readLine()) != null)   
         {
            StringTokenizer st = new StringTokenizer(strLine);
            tempAffectiveData = getAffectiveData(tempAffectiveDataArrList,Integer.parseInt(st.nextToken()));

            tempAffectiveData.setAnger(Integer.parseInt(st.nextToken()));
            tempAffectiveData.setDisgust(Integer.parseInt(st.nextToken()));
            tempAffectiveData.setFear(Integer.parseInt(st.nextToken()));
            tempAffectiveData.setJoy(Integer.parseInt(st.nextToken()));
            tempAffectiveData.setSadness(Integer.parseInt(st.nextToken()));
            tempAffectiveData.setSurprise(Integer.parseInt(st.nextToken()));
            affectiveDataArrList.add(tempAffectiveData);
         }
         while ((strLine = bufferedTestReader.readLine()) != null)   
         {
            StringTokenizer st = new StringTokenizer(strLine);
            tempAffectiveData = getAffectiveData(tempAffectiveDataArrList,Integer.parseInt(st.nextToken()));

            tempAffectiveData.setAnger(Integer.parseInt(st.nextToken()));
            tempAffectiveData.setDisgust(Integer.parseInt(st.nextToken()));
            tempAffectiveData.setFear(Integer.parseInt(st.nextToken()));
            tempAffectiveData.setJoy(Integer.parseInt(st.nextToken()));
            tempAffectiveData.setSadness(Integer.parseInt(st.nextToken()));
            tempAffectiveData.setSurprise(Integer.parseInt(st.nextToken()));
            affectiveDataArrList.add(tempAffectiveData);
         }
         
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
      
      return affectiveDataArrList;
   }   
   

   private AffectiveData getAffectiveData(ArrayList<AffectiveData> tempAffectiveDataArrList, int id)
   {
      for (Iterator<AffectiveData> iterator = tempAffectiveDataArrList.iterator(); iterator.hasNext();)
      {
         AffectiveData affectiveData = (AffectiveData) iterator.next();
         if(affectiveData.getId()==id)
         {
            return affectiveData;
         }
      }
      return null;
   }

   /**
    * I take an employee element and read the values in, create an Employee
    * object and return it
    */
   private AffectiveData fillAffectiveData(Element stateEl)
   {
      AffectiveData affectiveData = new AffectiveData();
      affectiveData.setId(Integer.parseInt(stateEl.getAttribute("id")));
      affectiveData.setStatement( stateEl.getFirstChild().getNodeValue());
      return affectiveData;
   }

}
