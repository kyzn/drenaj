package emotion.server.dbPedia;

import java.util.ArrayList;
import java.util.Iterator;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class DBPediaImpl
{

   private String DBPEDIA_SERVER_URL = "http://dbpedia.org/sparql";

   public void queryDBPedia(String word)
   {
      String sparqlQueryString = "select distinct ?Concept where {[] a ?Concept } LIMIT 10";
      Query query = QueryFactory.create(sparqlQueryString);
      QueryExecution qexec = QueryExecutionFactory.sparqlService(DBPEDIA_SERVER_URL, query);

      try
      {
         ResultSet results = qexec.execSelect();
         for (; results.hasNext();)
         {
            QuerySolution soln = results.nextSolution();
            String x = soln.get("Concept").toString();
            System.out.print(x + "\n");
         }
      }
      finally
      {
         qexec.close();
      }
   }

   public ArrayList<DBPediaData> searchDBPedia()
   {
      ArrayList<DBPediaData> dbPediaDataArrList = null;
      String sparqlQueryString = "SELECT ?property ?hasValue ?isValueOf WHERE {{ <http://dbpedia.org/resource/FC_Barcelona> ?property ?hasValue } UNION"
            + "{ ?isValueOf ?property <http://dbpedia.org/resource/FC_Barcelona> }}";
      Query query = QueryFactory.create(sparqlQueryString);
      QueryExecution qexec = QueryExecutionFactory.sparqlService(DBPEDIA_SERVER_URL, query);

      try
      {
         dbPediaDataArrList = populateData(qexec.execSelect());
      }
      finally
      {
         qexec.close();
      }
      return dbPediaDataArrList;
   }
   
   public ArrayList<DBPediaData> getDBPediaContent(String word)
   {
      ArrayList<DBPediaData> dbPediaDataArrList = null;
      String sparqlQueryString = "SELECT ?property ?hasValue ?isValueOf WHERE { { <http://dbpedia.org/resource/"+word+"> ?property ?hasValue } UNION { ?isValueOf ?property <http://dbpedia.org/resource/"+word+"> }}";

      Query query = null;

      QueryExecution qexec = null;

      try
      {
         query = QueryFactory.create(sparqlQueryString);
         qexec = QueryExecutionFactory.sparqlService(DBPEDIA_SERVER_URL, query);
         dbPediaDataArrList = populateData(qexec.execSelect());
      }
      catch(Exception e)
      {
         System.out.println("querried word : " +word);
         System.out.println(e.getMessage());
      }
      finally
      {
         if(qexec!=null)
         {
            qexec.close(); 
         }
         
      }
      return dbPediaDataArrList;
   }   
   

   private ArrayList<DBPediaData> populateData(ResultSet results)
   {
      ArrayList<DBPediaData> resultData = null;
      if (results != null)
      {
         resultData = new ArrayList<DBPediaData>(results.getRowNumber());
         for (; results.hasNext();)
         {
            QuerySolution soln = results.nextSolution();
            resultData.add(getRowData(soln));
         }
      }
      return resultData;
   }

   private DBPediaData getRowData(QuerySolution soln)
   {
      DBPediaData dbPediaData = new DBPediaData();
      dbPediaData.setPropertyResourceUrl(soln.get("property").toString());
      if (soln.get("hasValue") != null)
      {
         dbPediaData.setHasValueResourceUrl(soln.get("hasValue").toString());
      }
      if (soln.get("isValueOf") != null)
      {
         dbPediaData.setIsValueOfResourceUrl(soln.get("isValueOf").toString());
      }

      return dbPediaData;
   }

   public void checkValue(ArrayList<DBPediaData> dbPediaDataArrList, String word)
   {
      if(word.length()>3)
      {
         for (Iterator<DBPediaData> iterator = dbPediaDataArrList.iterator(); iterator.hasNext();)
         {
            DBPediaData dbPediaData = (DBPediaData) iterator.next();
            if (!dbPediaData.getProperty().equalsIgnoreCase("abstract"))
            {
               if (dbPediaData.getHasValue() != null && dbPediaData.getHasValueWordHashMap().containsKey(word.toLowerCase()))
               {
                  System.out.println(dbPediaData.getProperty() + " has value " + dbPediaData.getHasValue());
               }
               if (dbPediaData.getIsValueOf() != null && dbPediaData.getIsValueOWordfHashMap().containsKey(word.toLowerCase()))
               {
                  System.out.println(dbPediaData.getProperty() + " is value of  " + dbPediaData.getIsValueOf());
               }
            }
         }
      }
   }

}
