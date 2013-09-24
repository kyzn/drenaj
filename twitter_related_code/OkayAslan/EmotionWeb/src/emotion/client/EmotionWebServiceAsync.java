package emotion.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import emotion.shared.EmoAnalysisDO;
import emotion.shared.EmoDO;
import emotion.shared.EmoResultDO;

/**
 * The async counterpart of <code>EmotionWebService</code>.
 */
public interface EmotionWebServiceAsync {

   void searchWordOnTwitter(String word, AsyncCallback<EmoDO> callback);

   void saveJob(String word, AsyncCallback<Void> callback);

   void getJob(String word, AsyncCallback<EmoResultDO> callback);

   void getAllJobs(AsyncCallback<String[]> callback);

   void performAnalysis(EmoAnalysisDO emoAnalysisDO, AsyncCallback<EmoResultDO> callback);

   void searchAndReturnResults(String word, boolean semanticAnalysis, AsyncCallback<EmoResultDO> callback);

   void searchUserAndReturnResults(String word, boolean semanticAnalysis, AsyncCallback<EmoResultDO> callback);

   void getAllAnalyzedUsers(AsyncCallback<String[]> callback);

}
