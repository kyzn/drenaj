package emotion.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import emotion.shared.EmoAnalysisDO;
import emotion.shared.EmoDO;
import emotion.shared.EmoResultDO;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("emotion")
public interface EmotionWebService extends RemoteService 
{
	
 //  public ArrayList<EmoTweet> searchWordOnTwitter(String word) throws Exception;

   public EmoDO searchWordOnTwitter(String word) throws Exception;
   
   public void saveJob(String word) throws Exception;
   
   public String[] getAllJobs() throws Exception;
   
   public EmoResultDO getJob(String word) throws Exception;
   
   public String[] getAllAnalyzedUsers() throws Exception;
   
   public EmoResultDO searchAndReturnResults(String word,boolean semanticAnalysis) throws Exception;
   
   public EmoResultDO searchUserAndReturnResults(String word,boolean semanticAnalysis) throws Exception;
   
   public EmoResultDO performAnalysis(EmoAnalysisDO emoAnalysisDO) throws Exception; 
   
}
