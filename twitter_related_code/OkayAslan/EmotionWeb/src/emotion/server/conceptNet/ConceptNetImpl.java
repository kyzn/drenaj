package emotion.server.conceptNet;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ConceptNetImpl
{
   private String CONCEPTNET_SERVER_URL = "http://openmind.media.mit.edu";
   private String CONCEPTNET_API_URL = "http://openmind.media.mit.edu/api/";
   private String CLIENT_VERSION = "1";

   public ConceptNetImpl()
   {
   }

   public Concept getConcept(String word)
   {
      HttpClient httpClient = new DefaultHttpClient();
      HttpGet httpRequest = new HttpGet(prepareUrl(word));
      ResponseHandler<String> responseHandler = new BasicResponseHandler();
      String result;
      Concept concept = null;
      try
      {
         result = httpClient.execute(httpRequest, responseHandler);
         Gson gson = new Gson();
         concept = gson.fromJson(result, Concept.class); 
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
      
      return concept;
   }

   private String prepareUrl(String word)
   {
      return CONCEPTNET_API_URL+"/en/concept/"+word+"/";
   }

}
