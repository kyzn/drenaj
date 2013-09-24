package emotion.server.mediaWiki;

import java.io.IOException;
import java.net.MalformedURLException;

import net.sourceforge.jwbf.actions.mediawiki.MediaWiki;
import net.sourceforge.jwbf.actions.mediawiki.queries.AllPageTitles;
import net.sourceforge.jwbf.actions.mediawiki.util.RedirectFilter;
import net.sourceforge.jwbf.actions.mediawiki.util.VersionException;
import net.sourceforge.jwbf.bots.MediaWikiBot;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


public class MediaWikiImpl
{ 
   private String MEDIAWIKI_SERVER_URL = "http://en.wikipedia.org/w/";
   private String MEDIAWIKI_QUERY_STRING = "api.php?action=query&list=search&format=xml&srsearch=";
   private String MEDIAWIKI_SINGLE_DETAILED_QUERY_STRING = "api.php?action=opensearch&format=xml&limit=1&search=";
   private String MEDIAWIKI_DETAILED_QUERY_STRING = "api.php?action=opensearch&format=xml&search=";
   private String MEDIAWIKI_SUGGESTED_TITLE_QUERY_STRING = "api.php?action=query&list=search&format=xml&sroffset=0&srlimit=20&srprop=&srsearch=";
   
   public void queryWikiPedia (String word)
   {
      MediaWikiBot mediaWikiBot = null;
      try
      {
        mediaWikiBot = new MediaWikiBot(MEDIAWIKI_SERVER_URL);
      }
      catch (MalformedURLException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      AllPageTitles apt = null;
      try
      {
         apt = new AllPageTitles(mediaWikiBot, null, word,RedirectFilter.nonredirects, MediaWiki.NS_MAIN);
         for (String articleName : apt) {
            System.out.println(articleName);
         }
         
      }
      catch (VersionException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
   
   public MWApi getWikiPediaTitleInformation(String word)
   {
      HttpClient httpClient = new DefaultHttpClient();
      
      HttpGet httpRequest = new HttpGet(prepareUrl(MEDIAWIKI_SUGGESTED_TITLE_QUERY_STRING,word));
      ResponseHandler<String> responseHandler = new BasicResponseHandler();
      String result;
      MWApi mediaWikiApi = null;
      try
      {
         result = httpClient.execute(httpRequest, responseHandler);
         XStream xstream = new XStream(new DomDriver());
         xstream.alias("api", MWApi.class);
         xstream.alias("query", MWQuery.class);
         xstream.aliasField("query", MWApi.class, "mwQuery");
         xstream.aliasField("query-continue", MWApi.class, "queryCont");
         xstream.alias("search", MWSearch.class);
         xstream.aliasField("search", MWQuery.class, "mwSearch");
         xstream.aliasField("searchinfo", MWQuery.class, "searchinfo");
         xstream.alias("p", MWTitle.class);
         xstream.aliasAttribute(MWTitle.class, "title", "title");
         xstream.addImplicitCollection(MWSearch.class, "mwTitles", MWTitle.class);
         mediaWikiApi = (MWApi)xstream.fromXML(result); 

      }
      catch (ClientProtocolException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      return mediaWikiApi;
   }  
   
   public MediaWikiData getWikiInformation(String word)
   {
      HttpClient httpClient = new DefaultHttpClient();
      
      HttpGet httpRequest = new HttpGet(prepareUrl(MEDIAWIKI_DETAILED_QUERY_STRING,word));
      ResponseHandler<String> responseHandler = new BasicResponseHandler();
      String result;
      MediaWikiData mediaWikiData = null;
      try
      {
         result = httpClient.execute(httpRequest, responseHandler);
         XStream xstream = new XStream(new DomDriver());
         xstream.alias("SearchSuggestion", MediaWikiData.class);
         xstream.aliasField("Query", MediaWikiData.class, "query");
         xstream.alias("Section", Section.class);
         xstream.aliasField("Section", MediaWikiData.class, "section");
         xstream.alias("Item", Item.class);
         xstream.addImplicitCollection(Section.class, "items", Item.class);
         xstream.aliasField("Item", Section.class, "items");
         xstream.aliasField("Text", Item.class, "text");
         xstream.aliasField("Description", Item.class, "description");
         xstream.aliasField("Url", Item.class, "wikiUrl");
         xstream.alias("Image", Image.class);
         xstream.aliasField("Image", Item.class, "image");
         xstream.aliasAttribute(Image.class, "source", "source");
         xstream.aliasAttribute(Image.class, "width", "width");
         xstream.aliasAttribute(Image.class, "height", "height");

         mediaWikiData = (MediaWikiData)xstream.fromXML(result); 

      }
      catch (ClientProtocolException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      return mediaWikiData;
   }
   
   public MediaWikiData getSingleWikiInformation(String word)
   {
      HttpClient httpClient = new DefaultHttpClient();
      word = word.replaceAll("`", "");
      HttpGet httpRequest = new HttpGet(prepareUrl(MEDIAWIKI_SINGLE_DETAILED_QUERY_STRING,word));
      ResponseHandler<String> responseHandler = new BasicResponseHandler();
      String result;
      MediaWikiData mediaWikiData = null;
      try
      {
         result = httpClient.execute(httpRequest, responseHandler);
         XStream xstream = new XStream(new DomDriver());
         xstream.alias("SearchSuggestion", MediaWikiData.class);
         xstream.aliasField("Query", MediaWikiData.class, "query");
         xstream.alias("Section", Section.class);
         xstream.aliasField("Section", MediaWikiData.class, "section");
         xstream.alias("Item", Item.class);
         xstream.addImplicitCollection(Section.class, "items", Item.class);
         xstream.aliasField("Item", Section.class, "items");
         xstream.aliasField("Text", Item.class, "text");
         xstream.aliasField("Description", Item.class, "description");
         xstream.aliasField("Url", Item.class, "wikiUrl");
         xstream.alias("Image", Image.class);
         xstream.aliasField("Image", Item.class, "image");
         xstream.aliasAttribute(Image.class, "source", "source");
         xstream.aliasAttribute(Image.class, "width", "width");
         xstream.aliasAttribute(Image.class, "height", "height");

         mediaWikiData = (MediaWikiData)xstream.fromXML(result); 
         

      }
      catch (ClientProtocolException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      return mediaWikiData;
   }   
   
   private String prepareUrl(String queryString, String word)
   {
      return MEDIAWIKI_SERVER_URL+queryString+word;
   }
   
 }
