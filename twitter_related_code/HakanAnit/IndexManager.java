import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LogMergePolicy;
import org.apache.lucene.index.MergePolicy;
import org.apache.lucene.index.SegmentInfo;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.NRTManagerReopenThread;
import org.apache.lucene.search.NRTManager.TrackingIndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;


public class IndexManager
{
	private  ArrayList<TweetStat> tweetsBuffer,tweetsBufferTemp;
	private  Directory indexDirectory1,indexDirectoryTemp;
	private  IndexWriter writer1,writerTemp;
	private  NRTManager manager1,managerTemp;
	private  TrackingIndexWriter trackIndex1,trackIndexTemp;
	private  String indexName, eventType,analysisType,startTime,endTime,tokens[],official; //irrelevant,neutral,relevant
	private  Boolean flagIndex, flagAnnotationNeutral, flagAnnotationRelevant,flagAnnotationIrrelevant,flagReadFromDB;
	private  List<String> filterWords;
	private StandardAnalyzer standardAnalyzer;
	private Thread threadIndexAnalyzer,threadReadFromDB,threadIndexer,threadShutDownHook;
	private  IndexWriterConfig writerConfig,writerConfigTemp;
	private  NRTManagerReopenThread reopenThread,reopenThreadTemp;
	private  Indexer indexer;
	private IndexAnalyzer indexAnalyzer,indexAnalyzerGeneral;
	private ReadFromDB readFromDB;
	private Boolean boolStartEndTimeParsed,boolWriteTweetCreatedTimeToFile,boolIntervalAnalyzerIndexer,boolQueryAll,
	boolQueryInterval,boolCompleteIntervalAnalysis,boolLiveFurtherAnalysis,boolOnlyUpdateDB,boolAnnotation,boolDetectSituation,boolTestRead;
	private Long annotationTime;
	private ShutdownHook shutdownHookTemp;
	private Classification classification;
	private LocationManager locMan;

	public List<String> listBatch=null;
	public List<String> listLocation;
	public List<ObjectId> listIds;
	public Boolean boolRequestBatch=false,boolStreamRead=false;
	private Double valClassificationThreshold;

	public IndexManager(String indexName, String eventType,String analysisType,Boolean flagIndex, Boolean flagAnnotationIrrelevant,
			Boolean flagAnnotationNeutral,Boolean flagAnnotationRelevant,Boolean flagReadFromDB,Boolean boolCompleteIntervalAnalysis,Boolean boolLiveFurtherAnalysis,
			Boolean boolOnlyUpdateDB,Boolean boolAnnotation,List<String> filterWords, Long annotationTime,String official,Boolean boolDetectSituation,Boolean boolTestRead,Integer valClassifier,Double valClassificationThreshold) throws InterruptedException, IOException
			{
		try {
			DBConnect.connect();
		} catch (UnknownHostException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		this.valClassificationThreshold=valClassificationThreshold;
		this.boolTestRead=boolTestRead;
		this.boolDetectSituation=boolDetectSituation;
		classification=new Classification(eventType,valClassifier);
		this.annotationTime=annotationTime;
		this.boolAnnotation=boolAnnotation;
		this.boolOnlyUpdateDB=boolOnlyUpdateDB;
		this.boolLiveFurtherAnalysis=boolLiveFurtherAnalysis;
		tokens=new String[2];
		this.official=official;
		this.boolCompleteIntervalAnalysis=boolCompleteIntervalAnalysis;
		boolStartEndTimeParsed=false;
		this.indexName=indexName;
		this.eventType=eventType;
		this.analysisType=analysisType;
		this.flagIndex=flagIndex;
		this.flagAnnotationIrrelevant=flagAnnotationIrrelevant;
		this.flagAnnotationNeutral=flagAnnotationNeutral;
		this.flagAnnotationRelevant=flagAnnotationRelevant;
		this.flagIndex=flagIndex;
		this.flagReadFromDB=flagReadFromDB;
		this.filterWords=filterWords;
		locMan=new LocationManager(eventType);
		List<String> stopWords = new ArrayList<String>();
		CharArraySet stopSet = new  CharArraySet(Version.LUCENE_36, stopWords.size(), false);
		listIds=new ArrayList<ObjectId>();
		listLocation=new ArrayList<String>();
		try
		{
			// Open the file that is the first 
			// command line parameter
			FileInputStream fstream = new FileInputStream("./stopwords.txt");
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)  
			{
				// Print the content on the console
				stopWords.add(strLine);
			}
			//Close the input stream
			in.close();
		}
		catch (Exception e)
		{//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		stopSet.addAll(stopWords);  
		standardAnalyzer =new StandardAnalyzer(Version.LUCENE_36,stopSet);

		if(boolOnlyUpdateDB)
		{
			UpdateDB.startUpdate();

		}
		else if(boolCompleteIntervalAnalysis||boolAnnotation)
		{
			manageIndexes();
		}
		else if(!boolAnnotation)
		{

			if(flagReadFromDB)
			{
				boolWriteTweetCreatedTimeToFile=true;
			}
			else
			{
				boolWriteTweetCreatedTimeToFile=false;
			}

			if(!boolCompleteIntervalAnalysis) //sadece interval listesini temizlicek
			{
				tweetsBuffer=new ArrayList<TweetStat>();

				try
				{
					writerConfig=new IndexWriterConfig(Version.LUCENE_36, standardAnalyzer);

					if(new File(indexName).exists())
						writerConfig.setOpenMode(IndexWriterConfig.OpenMode.APPEND);
					else
						writerConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

					indexDirectory1=new NIOFSDirectory(new File(indexName));
					writer1=new IndexWriter(indexDirectory1,writerConfig);

				} catch (CorruptIndexException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LockObtainFailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				trackIndex1=new TrackingIndexWriter(writer1);
				try 
				{
					manager1=new NRTManager(trackIndex1, null);
					reopenThread = new NRTManagerReopenThread(manager1, 10.0, 0.1);
					reopenThread.setName("NRT Reopen Thread");
					reopenThread.setPriority(Math.min(Thread.currentThread().getPriority()+2, Thread.MAX_PRIORITY));
					reopenThread.setDaemon(true);
					reopenThread.start();


				} 
				catch (IOException e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				if(!flagReadFromDB&&!boolTestRead)
				{
					new Thread(new StreamReader(tweetsBuffer,eventType,filterWords,flagIndex,classification,valClassificationThreshold)).start();

				}
				if(boolTestRead)
				{
					new Thread(new StreamFromDBForTest(eventType,valClassifier)).start();

				}

				new Thread(new Indexer(writer1,tweetsBuffer,manager1,trackIndex1,false)).start();
				//gidip osm ye sormaya kalkar gereksiz seyler calisir. sadece detection olacaksa calissin bunlar
				if(boolDetectSituation)
				{
					new Thread(new TweetWaitingDispatcher(locMan,this)).start();
					new Thread(new Notifier()).start();
				}

				if(flagAnnotationIrrelevant||flagAnnotationNeutral||flagAnnotationRelevant||analysisType.equals(""))
				{
					//bazen reindex yaparke ndiyelim yeni ozellik ekledim ve kod yani term bulma yada cooccurence yada baska ozellik. bunlar direk indexte duruyor ve sonucu db ye yaziyorum. sonuclari dbden okudugum icin tekrar yazmakta fayda var son sonuclari gormek icin ondan analiztype "" ekledim

					if(!flagReadFromDB)
					{
						indexAnalyzerGeneral=new IndexAnalyzer(writer1,manager1,indexDirectory1,eventType,analysisType,boolWriteTweetCreatedTimeToFile,false);
						new Thread(indexAnalyzerGeneral).start();
					}
					else
					{
						boolQueryAll=true;
						boolQueryInterval=false;
						new Thread(new ReadFromDB(tweetsBuffer,eventType,analysisType,boolQueryAll,boolQueryInterval,"","",null,official)).start();
					}
					//dbden okicak buda bi yandan interval icin 
					//
				}
				Runtime.getRuntime().addShutdownHook(new ShutdownHook(writer1,classification.getTextAnalytics())); //writer.close yapip indexi olusturmak icin. kapatmazsan indexi olusturmuyor ve Append olmuyor

			}
			if(boolLiveFurtherAnalysis)
			{
				manageIndexes();
			}
		}

			}

	private String[] readTweetCreatedAtFromCollection() throws IOException
	{
		boolStartEndTimeParsed=false;
		int size=0;
		DBObject queryInterval =QueryBuilder.start("eventType").is(eventType).get();
		size= DBConnect.getCollanalysisStartEndTime().find(queryInterval).count();
		//		System.out.println("start end time count:"+size);
		DBCursor cursor;

		cursor = DBConnect.getCollanalysisStartEndTime().find(queryInterval);

		DBObject o;
		ObjectId timeID;

		while(cursor.hasNext())
		{

			o=cursor.next();
			timeID=(ObjectId)o.get("_id");
			startTime=(String)o.get("startTime");
			endTime=(String)o.get("endTime");
			tokens[0]=startTime.toString();
			tokens[1]=endTime.toString();
			//query birsey donmuyorsa silmeyelim listeden sonra data available olursa dener.

			queryInterval = QueryBuilder.start().and(
					QueryBuilder.start("eventType").is(eventType).get(),
					QueryBuilder.start("tweet.createdAt").greaterThanEquals(Integer.valueOf(tokens[0]).intValue()).get(),
					QueryBuilder.start("tweet.createdAt").lessThan(Integer.valueOf(tokens[1]).intValue()).get()

					).get();
			size=DBConnect.getCollEvent().find(queryInterval).count();

			if(size>0)
			{
				DBConnect.getCollanalysisStartEndTime().remove(o);
				boolStartEndTimeParsed=true;
			}
			else
			{
				boolStartEndTimeParsed=false;
				try {
					Thread.sleep(Settings.logInterval);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			break;
		}
		cursor=null;
		System.gc();

		return tokens;

	}

	public void garbageClean() throws CorruptIndexException, IOException, InterruptedException
	{
		System.out.println("garbage clean");
		indexAnalyzer.clear();
		indexAnalyzer=null;
		readFromDB=null;
		indexAnalyzer=null;
		reopenThreadTemp.close();
		reopenThreadTemp.join();
		managerTemp.close();
		threadIndexAnalyzer.join();
		threadIndexer.join();
		threadReadFromDB.join();
		writerTemp.close();
		tweetsBufferTemp.clear();
		tweetsBufferTemp=null;
		writerConfigTemp=null;
		writerTemp=null;
		indexDirectoryTemp=null;
		trackIndexTemp=null;
		reopenThreadTemp=null;
		threadIndexer=null;
		threadIndexAnalyzer=null;
		threadReadFromDB=null;
		System.gc();

	}
	public void manageIndexes() throws InterruptedException
	{
		while(true)
		{
			try
			{
				tweetsBufferTemp=new ArrayList<TweetStat>();

				IndexWriterConfig writerConfigTemp=new IndexWriterConfig(Version.LUCENE_36, standardAnalyzer);
				writerConfigTemp.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
				indexDirectoryTemp=new NIOFSDirectory(new File("./temp/out_"+eventType+"/index_"+eventType+"_temp_"+analysisType+""));
				writerTemp=new IndexWriter(indexDirectoryTemp,writerConfigTemp);

			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LockObtainFailedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			trackIndexTemp=new TrackingIndexWriter(writerTemp);
			try 
			{
				managerTemp=new NRTManager(trackIndexTemp, null);
				reopenThreadTemp = new NRTManagerReopenThread(managerTemp, 10.0, 0.1);
				reopenThreadTemp.setName("NRT Reopen Thread Temp");
				reopenThreadTemp.setPriority(Math.min(Thread.currentThread().getPriority()+2, Thread.MAX_PRIORITY));
				reopenThreadTemp.setDaemon(true);
				reopenThreadTemp.start();

			} 
			catch (IOException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated method stub

			System.out.println("1");
			indexer=new Indexer(writerTemp,tweetsBufferTemp,managerTemp,trackIndexTemp,true);
			threadIndexer= new Thread(indexer);
			threadIndexer.start();

			if(flagReadFromDB==true||flagAnnotationIrrelevant==true||flagAnnotationNeutral==true||flagAnnotationRelevant==true||analysisType.equals(""))
			{
				//bazen reindex yaparke ndiyelim yeni ozellik ekledim ve kod yani term bulma yada cooccurence yada baska ozellik. bunlar direk indexte duruyor ve sonucu db ye yaziyorum. sonuclari dbden okudugum icin tekrar yazmakta fayda var son sonuclari gormek icin ondan analiztype "" ekledim

				//dbden okicak buda bi yandan interval icin 
				String[] createdIntervalsFromFile;
				try
				{
					createdIntervalsFromFile=null;
					if(analysisType.equals(""))
					{
						createdIntervalsFromFile = readTweetCreatedAtFromCollection();


						while(boolStartEndTimeParsed==false)
						{

							createdIntervalsFromFile = readTweetCreatedAtFromCollection();


							//System.out.println("file read");

						}
					}
					if(flagReadFromDB)
					{
						boolQueryAll=true;
						boolIntervalAnalyzerIndexer=false;
					}
					else
					{
						boolQueryAll=false;
						boolIntervalAnalyzerIndexer=true;
					}

					if(!analysisType.equals(""))//annotation icin
					{

						readFromDB=new ReadFromDB(tweetsBufferTemp,eventType,analysisType,boolQueryAll,boolIntervalAnalyzerIndexer,null,null,annotationTime,official);

					}
					else
					{
						readFromDB=new ReadFromDB(tweetsBufferTemp,eventType,analysisType,boolQueryAll,boolIntervalAnalyzerIndexer,createdIntervalsFromFile[0],createdIntervalsFromFile[1],null,null);

					}
					System.out.println("2a");

					threadReadFromDB=new Thread(readFromDB);
					threadReadFromDB.start();
					threadReadFromDB.join();

					Indexer.boolStopThread=true;
					threadIndexer.join();
					System.out.println("buffer size:"+tweetsBufferTemp.size());

					writerTemp.commit(); //commit etmezsen file olusturmuyor segment

					System.out.println("2b");


					indexAnalyzer=new IndexAnalyzer(writerTemp,managerTemp,indexDirectoryTemp,eventType,analysisType,boolWriteTweetCreatedTimeToFile,true);
					threadIndexAnalyzer=new Thread(indexAnalyzer);
					threadIndexAnalyzer.start();
					threadIndexAnalyzer.join(); //isi bitene kadar bekle



					System.out.println("2");
					File newFileName = new File("./temp/out_"+eventType+"/index_"+eventType+"_temp_"+analysisType+"");


					garbageClean();
					FileUtils.deleteDirectory(newFileName);
					while(newFileName.exists()){
						System.out.println("beklio");

					}
					newFileName=null;
					if(!analysisType.equals(""))
					{
						System.out.println("annotation report completed");
						break;
					}


				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

}
