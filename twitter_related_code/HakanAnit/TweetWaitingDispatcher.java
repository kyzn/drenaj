import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.List;

import org.bson.types.ObjectId;
import org.dbpedia.spotlight.exceptions.AnnotationException;
import org.json.JSONException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

//tweetwaiting tablosundan sort ederek alacak datayi. aldigi fn adi status gibi bilgileri LocManager icindeki dispatcher
//fonksiyonuna gonderecek ve bekleyek. return edince bu tweeti silecek yenisini ekleyecek
public class TweetWaitingDispatcher implements Runnable
{
	private DBObject o;
	private DBCursor cur;
	private LocationManager locMan;
	private IndexManager im;
	public Boolean boolRequest=false;
	public Boolean boolCont=false;
	private TweetOSMDispatcher tod;

	public TweetWaitingDispatcher(LocationManager locMan,IndexManager im) throws UnknownHostException, FileNotFoundException
	{
		this.locMan=locMan;    
		DBConnect.connect();
		this.im=im;
		tod=new TweetOSMDispatcher(locMan,im,this);
		new Thread(tod).start();
	}
	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		while(true)
		{
			//System.out.println("boollllll"+im.boolRequestBatch.toString());
			synchronized (boolRequest)
			{
				if(boolRequest)
				{
					//var buralarda UNUTMA
					try
					{
						im.listBatch=locMan.batchRequestOSM(im.listIds, im.listLocation);
						//OSM hemen cvp donmeyebilir bir sure bekle yada dummyvar gerekli
						Thread.sleep(5000);

					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (AnnotationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//eger liste bossa yani OSM birsey gondermemisse tod.Update olmammali, butun liste ve dbdeki data silinmeli request
					boolRequest=false;
					if(im.listBatch!=null&&!im.listBatch.isEmpty())
					{
						System.out.println("LISTE BOS DEGIL");
						try {
							tod.updateDB();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else
					{
						//yani OSM birsey dondurmedi butun idleri listeden sil
						System.out.println("OSM BISI BULAMAMIS");

						for (ObjectId objid : im.listIds)
						{
							//hepsini sil dbden
							DBConnect.getCollTweetWaiting().remove(new BasicDBObject("tweetid",objid));
							DBConnect.getCollRequestOSM().remove(new BasicDBObject("tweetid",objid));
						}						
					}
					if(im.listBatch!=null)
						im.listBatch.clear();
					im.listBatch=null;
					if(im.listIds!=null)
						im.listIds.clear();
					//im.listIds=null;
					if(im.listLocation!=null)
						im.listLocation.clear();
					//im.listLocation=null;

				}

			}
			
			cur =DBConnect.getCollTweetWaiting().find().sort(new BasicDBObject("timestamp", 1)).limit(1);

			if (cur.hasNext())
			{
				o=cur.next();
				try
				{
					Boolean boolRequest1=false;
					if(o.get("request")!=null)
					{
						if(((String)o.get("request")).equals("1"))
							boolRequest1=true;
						else
							boolRequest1=false;
					}
					System.out.println("twaitig_"+(String)o.get("lat")+(String)o.get("long"));
					locMan.functionDispatcher((ObjectId)o.get("tweetid"),(String)o.get("tweetText"),(String)o.get("functionName"),(String)o.get("country"),(String)o.get("city"),(String)o.get("town"),(String)o.get("state"),
							(String)o.get("street"),(String)o.get("lat"),(String)o.get("long"),(String)o.get("status"),boolRequest1);
					DBConnect.getCollTweetWaiting().remove(new BasicDBObject("tweetid",(ObjectId)o.get("tweetid")));
					System.out.println("removed dispatcher id:"+((ObjectId)o.get("tweetid")).toString());
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (AnnotationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

		}

	}

}
