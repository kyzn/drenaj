package emotion.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import emotion.shared.EmoTweet;

public class EmotionWebCallBack implements AsyncCallback<ArrayList<EmoTweet>>
{
   private boolean callBackSuccess = false;
   private ArrayList<EmoTweet> emoTweetArr = null;
   
   
   /**
    * @param callBackSuccess the callBackSuccess to set
    */
   public void setCallBackSuccess(boolean callBackSuccess)
   {
      this.callBackSuccess = callBackSuccess;
   }

   /**
    * @return the callBackSuccess
    */
   public boolean isCallBackSuccess()
   {
      return callBackSuccess;
   }

   /**
    * @param emoTweetArr the emoTweetArr to set
    */
   public void setEmoTweetArr(ArrayList<EmoTweet> emoTweetArr)
   {
      this.emoTweetArr = emoTweetArr;
   }

   /**
    * @return the emoTweetArr
    */
   public ArrayList<EmoTweet> getEmoTweetArr()
   {
      return emoTweetArr;
   }

   @Override
   public void onFailure(Throwable caught)
   {
      setCallBackSuccess(false); 
      
   }

   @Override
   public void onSuccess(ArrayList<EmoTweet> result)
   {
      setCallBackSuccess(true);
      setEmoTweetArr(result);
   }

}
