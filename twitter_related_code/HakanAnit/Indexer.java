import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
//import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
//import org.apache.lucene.index.IndexableField;
import org.apache.lucene.misc.HighFreqTerms;
//import org.apache.lucene.misc.TermStats;

import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.NRTManager.TrackingIndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import com.mongodb.BasicDBObject;

//buna hangi bufferi verirsen ordan indexliyor verilen indexdirectorye
public class Indexer implements Runnable
{
	private ArrayList<TweetStat>tweetsBuffer;
	private int cnt=0, numTerms=0;
	private Directory indexDirectory;
	private NRTManager manager;
	private TrackingIndexWriter trackIndex;
	private Document doc;
	private TweetStat temp;
	public static Boolean boolStopThread;
	private Boolean boolPartialIndexer;
	private IndexWriter writer;

	public Indexer(IndexWriter writer,ArrayList<TweetStat> tweetsBuffer, NRTManager manager,TrackingIndexWriter trackIndex,Boolean boolPartialIndexer)
	{
		this.tweetsBuffer=tweetsBuffer;		
		this.manager=manager;
		this.trackIndex=trackIndex;
		boolStopThread=false;
		this.boolPartialIndexer=boolPartialIndexer;
		this.writer=writer;
	}

	@Override
	public void run() 
	{
		System.out.println("indexer started");
		
		while(boolStopThread==false&&boolPartialIndexer==true||boolPartialIndexer==false||boolStopThread==true&&boolPartialIndexer==true&&tweetsBuffer.size()>0)
		{
			synchronized (tweetsBuffer)
			{

				cnt=0;
				
				while(cnt<tweetsBuffer.size()&&tweetsBuffer.size()>0)
				{
					
					doc = new Document();

					
					//TweetStat i alacak ve ona gore indexleyecek
					try
					{
						temp=tweetsBuffer.get(cnt);
						doc.add(new Field("tweetLength", temp.GetTweetLength().toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));
						doc.add(new Field("gpsAttached", temp.GetGpsAttached(), Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));
						doc.add(new Field("isRT", temp.GetIsRT(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));
						doc.add(new Field("userName", temp.GetUserName(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));
						doc.add(new Field("originalTweet",temp.GetOriginalTweet(),Field.Store.YES,Field.Index.ANALYZED,Field.TermVector.YES));
						doc.add(new Field("tweetLength", temp.GetTweetLength().toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));

						
						doc.add(new Field("filteredTweet",temp.GetFilteredTweet(),Field.Store.YES,Field.Index.ANALYZED,Field.TermVector.YES));
						doc.add(new Field("tweetLengthFiltered", temp.GetFilteredTweetLength().toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));
						doc.add(new Field("tweetLengthSemiFiltered", temp.GetSemiFilteredTweetLength().toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));

						//Irrelevantlar hangi saatte digerleri hangi saatte gelmis en cok onu anlamak icin
						Date time=new Date(temp.GetTweetTime()*1000); //bi sorun var
						doc.add(new Field("createdAt_DateHour",new SimpleDateFormat("yyyy-MM-dd HH").format(time),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));         

						//arindirilmis stemmed tweetteki kelimelerde onemli olabilir
						//her satir index farkli yapida olabilir.
						//fieldname location olacak 1:token herneyse
						//eger #,E yada ,(punctuation) ise terslerinide indexliyorum yani E:token #:token 
						//bu sayede en cok hangi hashtag kullanilmis gorulebilir
						String tempS;
						
						for(Integer i=0;i<temp.GetTokens().size();i++)  //duplicate field name olmasi sorun degil ayni documentte farkli farkli indexlenecek valuelari
						{
							tempS=temp.GetTokens().get(i);
							//Emoticon arrayinden cek FIFO
							if(tempS.equals("Emoticon")) 
							{
								doc.add(new Field("Emoticon", temp.GetEmoticonQueuePoll(), Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));
								doc.add(new Field("EmoticonLocation",i.toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));

							}
							else if(tempS.equals("Hashtag"))
							{
								doc.add(new Field("Hashtag",temp.GetHashQueuePoll(), Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));
								doc.add(new Field("HashLocation",i.toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));

							}
							else if(tempS.equals("Punctuation"))
							{
								doc.add(new Field("Punctuation",temp.GetPunctuationQueuePoll(), Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));
								doc.add(new Field("PuncLocation",i.toString() ,Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));

							}
							else if(tempS.equals("URL"))
							{
								doc.add(new Field("URL","1", Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));
								doc.add(new Field("URLLocation",i.toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));

							}
							else if(tempS.equals("Mention"))
							{
								doc.add(new Field("Mention","1", Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));
								doc.add(new Field("MentionLocation",i.toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));

							}

							if(!tempS.equals("Punctuation")) //lokasyon bazinda bbakinca hep punc. cikiyordu ondan eklemiyorum
								doc.add(new Field(i.toString(), tempS,Field.Store.YES,Field.Index.ANALYZED,Field.TermVector.YES)); //hangi lokasyonda ne var icin

						}
						tempS="";
						
						for(Integer i=0;i<temp.GetTokensFiltered().size();i++)  //duplicate field name olmasi sorun degil ayni documentte farkli farkli indexlenecek valuelari
						{
							tempS=temp.GetTokensFiltered().get(i);

							//Emoticon arrayinden cek FIFO
							if(tempS.equals("emoticon")) 
							{
								doc.add(new Field("EmoticonLocationFiltered",i.toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));

							}
							else if(tempS.equals("hashtag"))
							{
								doc.add(new Field("HashLocationFiltered",i.toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));

							}
							else if(tempS.equals("punctuation"))
							{
								doc.add(new Field("PuncLocationFiltered",i.toString() ,Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));

							}
							else if(tempS.equals("url"))
							{
								doc.add(new Field("URLLocationFiltered",i.toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));

							}
							else if(tempS.equals("mention"))
							{
								doc.add(new Field("MentionLocationFiltered",i.toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));

							}

							

						}
						
						
						if(temp.getOutGPS()!=null)
							doc.add(new Field("outGPS", temp.getOutGPS().toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));
						if(temp.getOutAbb()!=null)
							doc.add(new Field("outAbb", temp.getOutAbb().toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));
						if(temp.getOutAddress()!=null)
							doc.add(new Field("outAddress", temp.getOutAddress().toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));
						if(temp.getOutCity()!=null)
							doc.add(new Field("outCity", temp.getOutCity().toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));
						if(temp.getOutTown()!=null)
							doc.add(new Field("outTown", temp.getOutTown().toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));
						if(temp.getOutCountry()!=null)
							doc.add(new Field("outCountry", temp.getOutCountry().toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));
						if(temp.getOutSarcasticLocation()!=null)
							doc.add(new Field("outSarcasticLocation", temp.getOutSarcasticLocation().toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));
						if(temp.getOutFakeLocation()!=null)
							doc.add(new Field("outFakeLocation", temp.getOutFakeLocation().toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));
						if(temp.getInGPS()!=null)
							doc.add(new Field("inGPS", temp.getInGPS().toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));
						if(temp.getInAbb()!=null)
							doc.add(new Field("inAbb", temp.getInAbb().toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));
						if(temp.getInAddress()!=null)
							doc.add(new Field("inAddress", temp.getInAddress().toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));
						if(temp.getInCity()!=null)
							doc.add(new Field("inCity", temp.getInCity().toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));
						if(temp.getInTown()!=null)
							doc.add(new Field("inTown", temp.getInTown().toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));
						if(temp.getInCountry()!=null)
							doc.add(new Field("inCountry", temp.getInCountry().toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));
						if(temp.getInSarcasticLocation()!=null)
							doc.add(new Field("inSarcasticLocation", temp.getInSarcasticLocation().toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));
						if(temp.getInFakeLocation()!=null)
							doc.add(new Field("inFakeLocation", temp.getInFakeLocation().toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));
						if(temp.getOrganization()!=null)
							doc.add(new Field("organization", temp.getOrganization().toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));
						if(temp.getDueToAnotherEvent()!=null)
							doc.add(new Field("dueToAnotherEvent", temp.getDueToAnotherEvent().toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));
						if(temp.getTimeEndInfo()!=null)
							doc.add(new Field("timeEndInfo", temp.getTimeEndInfo().toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));
						if(temp.getTimeStartInfo()!=null)
							doc.add(new Field("timeStartInfo", temp.getTimeStartInfo().toString(),Field.Store.YES,Field.Index.NOT_ANALYZED,Field.TermVector.YES));

						//simdi burda tweetle ilgili lokasyon islerini yaptiralim. nasilsa
						//db idside var artik db de onun yerini de update edebiliriz.
						
				
						
					}
					catch(Exception e)
					{
						System.out.println("Bir value null gelmis");

					}
					try {

						temp.clear();
						tweetsBuffer.remove(cnt);	
						
						trackIndex.addDocument(doc); //3.6
//						writer.commit();
						//manager.maybeRefresh();

					} catch (CorruptIndexException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block

						e.printStackTrace();
					}

				}

			}	
		

		}
		
		System.out.println("indexer stoppped");



	}

}
