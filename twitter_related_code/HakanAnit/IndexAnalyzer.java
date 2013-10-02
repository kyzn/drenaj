import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.math.util.MathUtils;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
//import org.apache.lucene.misc.HighFreqTerms;
//import org.apache.lucene.misc.TermStats;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
//import org.apache.lucene.util.MathUtil;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;

//analizi yapacak module real time
//verilen indexdirectoryden analiz yapip sonuclari db ye yaziyor

public class IndexAnalyzer implements Runnable
{
	//baslangic ve bitis tarihini dosyaya yaz
	private NRTManager manager;
	private DBConnect dbConnect;
	private IndexSearcher searcher;

	private int numTerms=10;
	private Directory indexDirectory;
	private Integer tweetCountOld=0,tweetCountNew=0; //5*60 ti
	private Analysis analysis;
	private String eventType,analysisType;
	private Long threadStartTime,threadEndTime;
	private Boolean boolWriteTweetCreatedTimeToFile,boolIntervalAnalyzerIndexer,boolFileWriteCompleted;
	public static Boolean boolIndexCompleted,boolStopThread;
	public static long tweetTime;
	private File theDir,newFileName;
	private BasicDBObject temp;
	private Long time1,time2;

	private IndexWriter writer;

	private void closeIndexSearcher(IndexSearcher is) throws IOException 
	{ 
		IndexReader[] rl = is.getSubReaders(); 
		for(IndexReader r : rl) { 
			r.close(); 
		} 
		is.close(); 
	} 
	public void clear()
	{
		theDir=null;
		newFileName=null;
		analysis=null;

		temp=null;
		System.gc();

	}

	public void generateCollocationsAndWriteToFile() throws IOException, InterruptedException
	{


		System.out.println("exec bas");

		Runtime r = Runtime.getRuntime();
		Process p = r.exec("sh ./scripts/lucene2seq.sh lucene2seq --dir ./temp/out_"+eventType+"/index_"+eventType+"_temp_"+analysisType+" --fields filteredTweet --output ./temp/out_"+eventType+"_seq_"+analysisType+" --idField userName");

		p.waitFor();
		//		BufferedReader err=
		//	             new BufferedReader(new InputStreamReader(p.getErrorStream()));		// And print each line
		//		String s = null;
		//		while ((s = err.readLine()) != null) 
		//		{
		//			System.out.println(s);
		//		}
		System.out.println("exec end1");


		System.out.println("exec bas2");

		p=r.exec("sh ./scripts/seq2sparse.sh seq2sparse --input ./temp/out_"+eventType+"_seq_"+analysisType+" --output ./temp/out_"+eventType+"_sparse_"+analysisType+"");
		p.waitFor();
		//		 err=
		//	             new BufferedReader(new InputStreamReader(p.getErrorStream()));		// And print each line
		//		 s = null;
		//		while ((s = err.readLine()) != null) 
		//		{
		//			System.out.println(s);
		//		}

		System.out.println("exec end2");


		System.out.println("exec bas3");

		p=r.exec("sh ./scripts/collocation.sh org.apache.mahout.vectorizer.collocations.llr.CollocDriver --input ./temp/out_"+eventType+"_sparse_"+analysisType+"/tokenized-documents/ --output ./temp/out_"+eventType+"_collocation_"+analysisType+" --maxNGramSize 7");
		p.waitFor();
		//		 err=
		//	             new BufferedReader(new InputStreamReader(p.getErrorStream()));		// And print each line
		//		s = null;
		//		while ((s = err.readLine()) != null) 
		//		{
		//			System.out.println(s);
		//		}

		System.out.println("exec end3");

		System.out.println("exec bas4");
		theDir = new File("./collocations");

		// if the directory does not exist, create it
		if (!theDir.exists())
		{

			boolean result = theDir.mkdir();  
			if(result){    
				System.out.println("DIR created");  
			}

		}
		p=r.exec("sh ./scripts/seqdumper.sh seqdumper --input ./temp/out_"+eventType+"_collocation_"+analysisType+"/ngrams >> ./collocations/"+eventType+"_"+threadEndTime+"_"+analysisType+".txt");
		p.waitFor();
		//		 err=
		//	             new BufferedReader(new InputStreamReader(p.getErrorStream()));		// And print each line
		//		s = null;
		//		while ((s = err.readLine()) != null) 
		//		{
		//			System.out.println(s);
		//		}

		System.out.println("exec end4");

		newFileName = new File("./temp/out_"+eventType+"_seq_"+analysisType+"");


		FileUtils.deleteDirectory(newFileName);
		while(newFileName.exists());
		newFileName= new File("./temp/out_"+eventType+"_sparse_"+analysisType+"");
		FileUtils.deleteDirectory(newFileName);
		while(newFileName.exists());

		newFileName= new File("./temp/out_"+eventType+"_collocation_"+analysisType+"");
		FileUtils.deleteDirectory(newFileName);
		while(newFileName.exists());

		temp=new BasicDBObject();

		temp.put("endTime",threadEndTime);
		temp.put("eventType", eventType);
		temp.put("analysisType",analysisType);
		DBConnect.getCollanalysisIntervalReadyReports().insert(temp);

	}

	public Boolean getFileWriteCompleted()
	{
		return boolFileWriteCompleted;
	}
	public Boolean getIndexCompleted()
	{
		return boolIndexCompleted;
	}

	public static void BoolSetStopThread(Boolean val)
	{
		boolStopThread=val;
	}
	public IndexAnalyzer(IndexWriter writer,NRTManager manager,Directory indexDirectory, String eventType, String analysisType,
			Boolean boolWriteTweetCreatedTimeToFile, Boolean boolIntervalAnalyzerIndexer )
	{	
		this.writer=writer;
		boolIndexCompleted=false;
		boolFileWriteCompleted=false;
		this.manager=manager;
		this.indexDirectory=indexDirectory;
		analysis=new Analysis();
		this.eventType=eventType;
		this.analysisType=analysisType;
		boolStopThread=false;
		this.boolWriteTweetCreatedTimeToFile=boolWriteTweetCreatedTimeToFile;
		this.boolIntervalAnalyzerIndexer=boolIntervalAnalyzerIndexer;
		//		try 
		//		{
		//			dbConnect=new DBConnect();
		//
		//
		//		} catch (UnknownHostException e1) {
		//			// TODO Auto-generated catch block
		//			e1.printStackTrace();
		//		}
		try {

			manager.maybeRefresh();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		searcher=manager.acquire();

	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		int cnt=0;
		while(boolStopThread==false&&boolIntervalAnalyzerIndexer==true||boolIntervalAnalyzerIndexer==false)
		{



			try 
			{

				time1=new Long(System.currentTimeMillis()/1000);
				threadStartTime=time1;




				tweetCountNew=analysis.GetTweetsCount();

				if(analysisType.equals("")&&!boolIntervalAnalyzerIndexer)
				{


					Thread.sleep(Settings.logInterval);

				}
				//indexleme hizi daha yavas olabilir dbye ekleme hizindan zaman farki bundan olabil r tweet count konusunda





				tweetCountOld=analysis.GetTweetsCount();



				time2=new Long(System.currentTimeMillis()/1000);
				threadEndTime=time2;

				temp=new BasicDBObject();
				if(!boolIntervalAnalyzerIndexer)//genel olan araliklari yazicak
				{
					if(boolWriteTweetCreatedTimeToFile)
					{

						threadStartTime=IndexAnalyzer.tweetTime;
						threadEndTime=IndexAnalyzer.tweetTime+Settings.logInterval/1000;


					}

					//php de long koyamiyorsun diye reequestte sorun oluyor o yuzden sadece bu string koyalim degeri
					temp.put("startTime",String.valueOf(threadStartTime.longValue()));
					temp.put("endTime",String.valueOf(threadEndTime.longValue()));
					temp.put("eventType", eventType);
					DBConnect.getCollanalysisStartEndTime().insert(temp);
					//BufferedWriter out = new BufferedWriter(new FileWriter("./"+threadEndTime+".txt", true));

					//					System.out.println(threadStartTime+"-"+threadEndTime);


				}


				if(!boolIntervalAnalyzerIndexer)
					analysis.SetTweetCountInterval(tweetCountOld-tweetCountNew);
				else
				{

					analysis.SetTweetCountInterval(tweetCountOld);
				}

				analysis.addResultsToDB();
				analysis.clear();
				cnt++;
				temp=null;
				System.gc();
				if(boolIntervalAnalyzerIndexer)
				{

					if(DBConnect.queryCount>0) //mongoda write ederken diger instance calisirken insertler gec geliyor dolayisiyla bos rapor olusturuyor ve query 0 donuyor.
					{
						generateCollocationsAndWriteToFile();
					}
					boolStopThread=true;

					System.out.println("analysis index completed");

				}
				if(!analysisType.equals("")&&analysis.tweetCountInterval.equals(0)&&cnt!=0)
				{
					System.out.println("analysis index completed");
					System.exit(0);
				}


			}
			catch (Exception e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}


			//			finally
			//			{
			//				try
			//				{
			////					closeIndexSearcher(searcher);
			//					
			////					manager.release(searcher);
			//
			//				} catch (IOException e) {
			//					// TODO Auto-generated catch block
			//					e.printStackTrace();
			//				}
			//			}

		}


	}
	private class Analysis
	{ 
		private TermStats[] terms;
		private TermStats[] termsTemp;

		private Integer tweetCountInterval=0,a,b; //Intervalde yeni kac tane tweet gelmis
		private List<Integer> values;
		private double valInGps,valInTown,valInCity,valInCountry,valInAddress,valInAbb;

		public Analysis()
		{
			values=new ArrayList<Integer>();
		}
		public void clear()
		{
			values.clear();
		}
		private Integer ComputeMedian()
		{
			PrepareTermStatsForMedian();
			Collections.sort(values);

			if (values.size() % 2 == 1)
				return values.get((values.size()+1)/2-1);
			else
			{
				Integer lower = values.get(values.size()/2-1);
				Integer upper = values.get(values.size()/2);

				return (lower + upper) / 2;
			}	
		}
		private Integer ComputeAverage()
		{
			int average=0;
			for (int i = 0; i < terms.length; i++)
			{
				a=Integer.parseInt(terms[i].term.text()); //bundada utf8string seysi vardi
				b=Integer.valueOf(terms[i].docFreq); //freq ile carpmiyorum simdlik
				average+=a.intValue();

			}

			return Integer.valueOf(average/terms.length);
		}
		private void PrepareTermStatsForMedian()
		{
			for (int i = 0; i < terms.length; i++)
			{
				a=Integer.parseInt(terms[i].term.text());
				b=Integer.valueOf(terms[i].docFreq);
				values.add(a.intValue());
			}
		}

		private BasicDBObject GetTermStats()
		{
			BasicDBObject temp=new BasicDBObject();
			for (int i = 0; i < terms.length; i++)
			{

				temp.put(String.valueOf(terms[i].docFreq),terms[i].term.text()); //lucene 4.0. da boyle yazmisim stringParse(terms[i].term.toString())

			}
			if(terms.equals(null))
			{
				temp.put("0", "");
			}

			return temp;
		}
		public void SetTweetCountInterval(Integer _t)
		{
			tweetCountInterval=_t;
		}
		public Integer TweetCountInterval()
		{

			Integer i=tweetCountInterval;
			//System.out.println("NEW TWEETS:"+ i.toString());
			return i;
		}
		public void addResultsToDB() throws Exception
		{
			temp=new BasicDBObject();
			//			Date time=new Date();

			temp.put("analysisType", analysisType);
			temp.put("eventType",eventType);
			DBConnect.getCollEvent().createIndex(new BasicDBObject("eventType",1)); //guya bunun uzerinden indexlemis oldum

			//	temp.put("createdAt",(System.currentTimeMillis()+3*60*6)/1000); //gmt ekledim
			temp.put("createdAt",threadEndTime.longValue());
			if(boolIntervalAnalyzerIndexer)
				temp.put("intervalAnalysis", String.valueOf(1));
			temp.put("averageTweetLength",AverageTweetLength().toString());
			temp.put("averageTweetLengthFiltered",AverageTweetLengthFiltered().toString());
			temp.put("averageTweetLengthSemiFiltered",AverageTweetLengthSemiFiltered().toString());


			temp.put("mostActiveUsers",GetMostActiveUsers());
			temp.put("topNHour", GetTopHour());

			temp.put("topNTermsFiltered", GetTopFilteredTerms());

			temp.put("topNTerms", GetTopOriginalTerms());
			temp.put("topNEmoticons", GetTopEmoticon());
			temp.put("topNHashtag", GetTopHashtag());
			temp.put("topNPunctuation", GetTopPunctuation());
			temp.put("percentageURL", String.valueOf(PercentageURL()));
			temp.put("percentageRT", String.valueOf(PercentageRT()));
			temp.put("percentageGPS", String.valueOf(PercentageGpsAttached()));
			temp.put("percentageMention",String.valueOf(PercentageMention()));
			temp.put("totalTweets",GetTweetsCount().toString());
			temp.put("averageHashLocation",HashLocation().toString());
			temp.put("averageURLLocation",URLLocation().toString());
			temp.put("averagePunctuationLocation",PuncLocation().toString());
			temp.put("averageMentionLocation",MentionLocation().toString());
			temp.put("averageEmoticonLocation",EmoticonLocation().toString());
			temp.put("topNTermLocation", TopNTermLocation());
			temp.put("newTweetsInterval",TweetCountInterval().toString());


			temp.put("averageHashLocationFiltered",HashLocationFiltered().toString());
			temp.put("averageURLLocationFiltered",URLLocationFiltered().toString());
			temp.put("averagePunctuationLocationFiltered",PuncLocationFiltered().toString());
			temp.put("averageMentionLocationFiltered",MentionLocationFiltered().toString());
			temp.put("averageEmoticonLocationFiltered",EmoticonLocationFiltered().toString());

			temp.put("percentageTimeStart", String.valueOf(PercentageTimeStart()));
			temp.put("percentageTimeEnd", String.valueOf(PercentageTimeEnd()));
			temp.put("percentageDueToAnotherEvent", String.valueOf(PercentageDueToAnotherEvent()));
			temp.put("percentageOrganization", String.valueOf(PercentageOrganization()));
			temp.put("percentageInFakeLocation", String.valueOf(PercentageInFakeLocation()));
			temp.put("percentageInSarcasticLocation", String.valueOf(PercentageInSarcasticLocation()));
			temp.put("percentageInCountry", String.valueOf(PercentageInCountry()));
			temp.put("percentageInTown", String.valueOf(PercentageInTown()));
			temp.put("percentageInCity", String.valueOf(PercentageInCity()));
			temp.put("percentageInAddress", String.valueOf(PercentageInAddress()));
			temp.put("percentageInAbb", String.valueOf(PercentageinAbb()));
			temp.put("percentageInGPS", String.valueOf(PercentageinGPS()));
			temp.put("percentageOutFakeLocation", String.valueOf(PercentageOutFakeLocation()));
			temp.put("percentageOutSarcasticLocation", String.valueOf(PercentageOutSarcasticLocation()));
			temp.put("percentageOutCountry", String.valueOf(PercentageOutCountry()));
			temp.put("percentageOutTown", String.valueOf(PercentageOutTown()));
			temp.put("percentageOutCity", String.valueOf(PercentageOutCity()));
			temp.put("percentageOutAddress", String.valueOf(PercentageOutAddress()));
			temp.put("percentageOutAbb", String.valueOf(PercentageOutAbb()));
			temp.put("percentageOutGPS", String.valueOf(PercentageOutGPS()));

//			temp.put("percentageLocation", String.valueOf(PercentageInLocation()));



			DBConnect.getCollAnalysis().insert(temp);

		}
		public BasicDBObject TopNTermLocation() 
		{
			//average tweet length i bul ona gore baklim her lokasyon icin
			try {
				terms=HighFreqTerms.getHighFreqTerms(searcher.getIndexReader(), numTerms, "tweetLength");
				Integer average=ComputeAverage();
				BasicDBObject temp1=new BasicDBObject();

				for(Integer i=0;i<average;i++)
				{
					termsTemp=HighFreqTerms.getHighFreqTerms(searcher.getIndexReader(), 1, i.toString());

					//o pozisyon icin kullanilan top 10 kelime ve frequencyleri
					//System.out.printf("%s:%s %,d \n",termsTemp[0].field, termsTemp[0].term.toString(), termsTemp[0].docFreq);
					temp1.put(termsTemp[0].term.field(),  termsTemp[0].term.text());


				}
				return temp1;
			} catch (Exception e) 
			{
				// TODO Auto-generated catch block
				return null;
			}

		}

		public BasicDBObject GetMostActiveUsers()
		{

			try {
				terms=HighFreqTerms.getHighFreqTerms(searcher.getIndexReader(), numTerms, "userName");

			} catch (Exception e) {
				// TODO Auto-generated catch block
				return null;
			}
			//PrintTermStats();
			return GetTermStats();

		}
		public BasicDBObject GetTopFilteredTerms()
		{
			try {
				terms=HighFreqTerms.getHighFreqTerms(searcher.getIndexReader(), numTerms+1000, "filteredTweet");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return null;
			}
			//PrintTermStats();
			return GetTermStats();

		}
		public BasicDBObject GetTopOriginalTerms()
		{
			try {
				terms=HighFreqTerms.getHighFreqTerms(searcher.getIndexReader(), numTerms+1000, "originalTweet");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return null;
			}
			//PrintTermStats();
			return GetTermStats();

		}
		public BasicDBObject GetTopPunctuation()
		{
			try {
				terms=HighFreqTerms.getHighFreqTerms(searcher.getIndexReader(), numTerms, "Punctuation");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return null;

			}
			//PrintTermStats();
			return GetTermStats();

		}
		public BasicDBObject GetTopEmoticon() 
		{
			try
			{
				terms=HighFreqTerms.getHighFreqTerms(searcher.getIndexReader(), numTerms, "Emoticon");
			} catch (Exception e) 
			{
				return null;

			}
			//PrintTermStats();
			return GetTermStats();

		}
		public BasicDBObject GetTopHour()  ///irrelevant vs en cok hangi saatte atilmis
		{
			try {
				terms=HighFreqTerms.getHighFreqTerms(searcher.getIndexReader(), numTerms, "createdAt_DateHour");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return null;
			}
			//PrintTermStats();
			return GetTermStats();

		}
		public BasicDBObject GetTopHashtag()
		{
			try {
				terms=HighFreqTerms.getHighFreqTerms(searcher.getIndexReader(), 20, "Hashtag");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return null;
			}
			//PrintTermStats();
			return GetTermStats();

		}
		public double PercentageOutGPS()
		{

			Query query = new TermQuery(new Term("outGPS","1"));
			TopDocs docs;
			try {
				manager.maybeRefresh();
				searcher=manager.acquire();
				docs = searcher.search(query, 1);

				//System.out.println("HITS:"+docs.totalHits);
				Integer hits=docs.totalHits;
				//System.out.println("HITS:"+hits);
				double d=hits.doubleValue()/GetTweetsCount().doubleValue();

				return d*100;	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			}

		}
		public double PercentageOutAbb()
		{

			Query query = new TermQuery(new Term("outAbb","1"));
			TopDocs docs;
			try {
				manager.maybeRefresh();
				searcher=manager.acquire();
				docs = searcher.search(query, 1);

				//System.out.println("HITS:"+docs.totalHits);
				Integer hits=docs.totalHits;
				//System.out.println("HITS:"+hits);
				double d=hits.doubleValue()/GetTweetsCount().doubleValue();

				return d*100;	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			}

		}
		public double PercentageOutAddress()
		{

			Query query = new TermQuery(new Term("outAddress","1"));
			TopDocs docs;
			try {
				manager.maybeRefresh();
				searcher=manager.acquire();
				docs = searcher.search(query, 1);

				//System.out.println("HITS:"+docs.totalHits);
				Integer hits=docs.totalHits;
				//System.out.println("HITS:"+hits);
				double d=hits.doubleValue()/GetTweetsCount().doubleValue();

				return d*100;	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			}

		}
		public double PercentageOutCity()
		{

			Query query = new TermQuery(new Term("outCity","1"));
			TopDocs docs;
			try {
				manager.maybeRefresh();
				searcher=manager.acquire();
				docs = searcher.search(query, 1);

				//System.out.println("HITS:"+docs.totalHits);
				Integer hits=docs.totalHits;
				//System.out.println("HITS:"+hits);
				double d=hits.doubleValue()/GetTweetsCount().doubleValue();

				return d*100;	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			}

		}
		public double PercentageOutTown()
		{

			Query query = new TermQuery(new Term("outTown","1"));
			TopDocs docs;
			try {
				manager.maybeRefresh();
				searcher=manager.acquire();
				docs = searcher.search(query, 1);

				//System.out.println("HITS:"+docs.totalHits);
				Integer hits=docs.totalHits;
				//System.out.println("HITS:"+hits);
				double d=hits.doubleValue()/GetTweetsCount().doubleValue();

				return d*100;	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			}

		}
		public double PercentageOutCountry()
		{

			Query query = new TermQuery(new Term("outCountry","1"));
			TopDocs docs;
			try {
				manager.maybeRefresh();
				searcher=manager.acquire();
				docs = searcher.search(query, 1);

				//System.out.println("HITS:"+docs.totalHits);
				Integer hits=docs.totalHits;
				//System.out.println("HITS:"+hits);
				double d=hits.doubleValue()/GetTweetsCount().doubleValue();

				return d*100;	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			}

		}
		public double PercentageOutSarcasticLocation()
		{

			Query query = new TermQuery(new Term("outSarcasticLocation","1"));
			TopDocs docs;
			try {
				manager.maybeRefresh();
				searcher=manager.acquire();
				docs = searcher.search(query, 1);

				//System.out.println("HITS:"+docs.totalHits);
				Integer hits=docs.totalHits;
				//System.out.println("HITS:"+hits);
				double d=hits.doubleValue()/GetTweetsCount().doubleValue();

				return d*100;	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			}

		}
		public double PercentageOutFakeLocation()
		{

			Query query = new TermQuery(new Term("outFakeLocation","1"));
			TopDocs docs;
			try {
				manager.maybeRefresh();
				searcher=manager.acquire();
				docs = searcher.search(query, 1);

				//System.out.println("HITS:"+docs.totalHits);
				Integer hits=docs.totalHits;
				//System.out.println("HITS:"+hits);
				double d=hits.doubleValue()/GetTweetsCount().doubleValue();

				return d*100;	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			}

		}
		
		public double PercentageinGPS()
		{

			Query query = new TermQuery(new Term("inGPS","1"));
			TopDocs docs;
			try {
				manager.maybeRefresh();
				searcher=manager.acquire();
				docs = searcher.search(query, 1);

				//System.out.println("HITS:"+docs.totalHits);
				Integer hits=docs.totalHits;
				//System.out.println("HITS:"+hits);
				double d=hits.doubleValue()/GetTweetsCount().doubleValue();
				valInGps=hits.doubleValue();
				return d*100;	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			}

		}
		public double PercentageinAbb()
		{

			Query query = new TermQuery(new Term("inAbb","1"));
			TopDocs docs;
			try {
				manager.maybeRefresh();
				searcher=manager.acquire();
				docs = searcher.search(query, 1);

				//System.out.println("HITS:"+docs.totalHits);
				Integer hits=docs.totalHits;
				//System.out.println("HITS:"+hits);
				double d=hits.doubleValue()/GetTweetsCount().doubleValue();
				valInAbb=hits.doubleValue();
				return d*100;	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			}

		}
		public double PercentageInAddress()
		{

			Query query = new TermQuery(new Term("inAddress","1"));
			TopDocs docs;
			try {
				manager.maybeRefresh();
				searcher=manager.acquire();
				docs = searcher.search(query, 1);

				//System.out.println("HITS:"+docs.totalHits);
				Integer hits=docs.totalHits;
				//System.out.println("HITS:"+hits);
				double d=hits.doubleValue()/GetTweetsCount().doubleValue();
				valInAddress=hits.doubleValue();
				return d*100;	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			}

		}
		public double PercentageInCity()
		{

			Query query = new TermQuery(new Term("inCity","1"));
			TopDocs docs;
			try {
				manager.maybeRefresh();
				searcher=manager.acquire();
				docs = searcher.search(query, 1);

				//System.out.println("HITS:"+docs.totalHits);
				Integer hits=docs.totalHits;
				//System.out.println("HITS:"+hits);
				double d=hits.doubleValue()/GetTweetsCount().doubleValue();
				valInCity=hits.doubleValue();
				return d*100;	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			}

		}
		public double PercentageInTown()
		{

			Query query = new TermQuery(new Term("inTown","1"));
			TopDocs docs;
			try {
				manager.maybeRefresh();
				searcher=manager.acquire();
				docs = searcher.search(query, 1);

				Integer hits=docs.totalHits;
				//				System.out.println("HITS in town:"+docs.totalHits);
				//
				//				System.out.println("tw count:"+GetTweetsCount().doubleValue());
				double d=hits.doubleValue()/GetTweetsCount().doubleValue();
				valInTown=hits.doubleValue();
				return d*100;	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			}

		}
		public double PercentageInCountry()
		{

			Query query = new TermQuery(new Term("inCountry","1"));
			TopDocs docs;
			try {
				manager.maybeRefresh();
				searcher=manager.acquire();
				docs = searcher.search(query, 1);

				//System.out.println("HITS:"+docs.totalHits);
				Integer hits=docs.totalHits;
				//System.out.println("HITS:"+hits);
				double d=hits.doubleValue()/GetTweetsCount().doubleValue();
				valInCountry=hits.doubleValue();
				return d*100;	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			}

		}
		public double PercentageInSarcasticLocation()
		{

			Query query = new TermQuery(new Term("inSarcasticLocation","1"));
			TopDocs docs;
			try {
				manager.maybeRefresh();
				searcher=manager.acquire();
				docs = searcher.search(query, 1);

				//System.out.println("HITS:"+docs.totalHits);
				Integer hits=docs.totalHits;
				//System.out.println("HITS:"+hits);
				double d=hits.doubleValue()/GetTweetsCount().doubleValue();

				return d*100;	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			}

		}
		public double PercentageInFakeLocation()
		{

			Query query = new TermQuery(new Term("inFakeLocation","1"));
			TopDocs docs;
			try {
				manager.maybeRefresh();
				searcher=manager.acquire();
				docs = searcher.search(query, 1);

				//System.out.println("HITS:"+docs.totalHits);
				Integer hits=docs.totalHits;
				//System.out.println("HITS:"+hits);
				double d=hits.doubleValue()/GetTweetsCount().doubleValue();

				return d*100;	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			}

		}
		public double PercentageOrganization()
		{

			Query query = new TermQuery(new Term("organization","1"));
			TopDocs docs;
			try {
				manager.maybeRefresh();
				searcher=manager.acquire();
				docs = searcher.search(query, 1);

				//System.out.println("HITS:"+docs.totalHits);
				Integer hits=docs.totalHits;
				//System.out.println("HITS:"+hits);

				//				System.out.println("HITS in organization:"+docs.totalHits);
				//
				//				System.out.println("tw count:"+GetTweetsCount().doubleValue());
				double d=hits.doubleValue()/GetTweetsCount().doubleValue();

				return d*100;	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			}

		}
		public double PercentageDueToAnotherEvent()
		{

			Query query = new TermQuery(new Term("dueToAnotherEvent","1"));
			TopDocs docs;
			try {
				manager.maybeRefresh();
				searcher=manager.acquire();
				docs = searcher.search(query, 1);

				//System.out.println("HITS:"+docs.totalHits);
				Integer hits=docs.totalHits;
				//System.out.println("HITS:"+hits);
				double d=hits.doubleValue()/GetTweetsCount().doubleValue();

				return d*100;	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			}

		}
		public double PercentageTimeEnd()
		{

			Query query = new TermQuery(new Term("timeEndInfo","1"));
			TopDocs docs;
			try {
				manager.maybeRefresh();
				searcher=manager.acquire();
				docs = searcher.search(query, 1);

				//System.out.println("HITS:"+docs.totalHits);
				Integer hits=docs.totalHits;
				//System.out.println("HITS:"+hits);
				double d=hits.doubleValue()/GetTweetsCount().doubleValue();

				return d*100;	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			}

		}
		public double PercentageTimeStart()
		{

			Query query = new TermQuery(new Term("timeStartInfo","1"));
			TopDocs docs;
			try {
				manager.maybeRefresh();
				searcher=manager.acquire();
				docs = searcher.search(query, 1);

				//System.out.println("HITS:"+docs.totalHits);
				Integer hits=docs.totalHits;
				//System.out.println("HITS:"+hits);
				double d=hits.doubleValue()/GetTweetsCount().doubleValue();

				return d*100;	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			}

		}
		public double PercentageURL()
		{

			Query query = new TermQuery(new Term("URL","1"));
			TopDocs docs;
			try {
				manager.maybeRefresh();
				searcher=manager.acquire();
				docs = searcher.search(query, 1);

				//System.out.println("HITS:"+docs.totalHits);
				Integer hits=docs.totalHits;
				//System.out.println("HITS:"+hits);
				double d=hits.doubleValue()/GetTweetsCount().doubleValue();

				return d*100;	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);
			}

		}
		public double PercentageMention()
		{

			Query query = new TermQuery(new Term("Mention","1"));
			TopDocs docs;
			try {
				manager.maybeRefresh();
				searcher=manager.acquire();
				docs = searcher.search(query, 1);

				Integer hits=docs.totalHits;
				//System.out.println("HITS:"+hits);
				double d=hits.doubleValue()/GetTweetsCount().doubleValue();

				return d*100;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);			} catch (Exception e) {
					// TODO Auto-generated catch block
					return Double.valueOf(0);			}

		}
		public double PercentageRT()
		{

			Query query = new TermQuery(new Term("isRT","1"));
			TopDocs docs;
			try {
				manager.maybeRefresh();
				searcher=manager.acquire();
				docs = searcher.search(query, 1);

				Integer hits=docs.totalHits;
				//System.out.println("HITS:"+hits);
				double d=hits.doubleValue()/GetTweetsCount().doubleValue();

				return d*100;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);			} catch (Exception e) {
					// TODO Auto-generated catch block
					return Double.valueOf(0);			}
		}
		//		public Integer TweetsCount() throws Exception
		//		{
		//
		//			manager.maybeRefresh();
		//			searcher=manager.acquire();
		//			Integer num=searcher.getIndexReader().numDocs();
		//			//System.out.println("Total Tweets:"+num);
		//			return num;
		//		}
		public Integer GetTweetsCount() throws Exception
		{
			manager.maybeRefresh();
			searcher=manager.acquire();
			return searcher.getIndexReader().numDocs();

		}
		//BURAYA MOBILE PERCENTAGE GELEBILIR
		public double PercentageGpsAttached()
		{

			Query query = new TermQuery(new Term("gpsAttached","1"));
			TopDocs docs;
			try {
				manager.maybeRefresh();
				searcher=manager.acquire();
				docs = searcher.search(query, 1);

				Integer hits=docs.totalHits;
				//System.out.println("HITS:"+hits);
				double d=hits.doubleValue()/GetTweetsCount().doubleValue();

				return d*100;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return Double.valueOf(0);			} catch (Exception e) {
					// TODO Auto-generated catch block
					return Double.valueOf(0);			}



		}
		public Integer HashLocation()
		{

			try {
				terms=HighFreqTerms.getHighFreqTerms(searcher.getIndexReader(), numTerms, "HashLocation");

				Integer median=ComputeMedian();
				Integer average=ComputeAverage();
				//System.out.println("HASH MEDIAN:"+median+"AVERAGE:"+average);
				return average;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Integer.valueOf(0);			}


		}
		public Integer URLLocation()
		{

			try {
				terms=HighFreqTerms.getHighFreqTerms(searcher.getIndexReader(), numTerms, "URLLocation");

				Integer median=ComputeMedian();
				Integer average=ComputeAverage();
				//System.out.println("URL MEDIAN:"+median+"AVERAGE:"+average);
				return average;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Integer.valueOf(0);
			}
		}
		public Integer PuncLocation()
		{

			try {
				terms=HighFreqTerms.getHighFreqTerms(searcher.getIndexReader(), numTerms, "PuncLocation");

				Integer median=ComputeMedian();
				Integer average=ComputeAverage();
				//System.out.println("PUNC MEDIAN:"+median+"AVERAGE:"+average);
				return average;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Integer.valueOf(0);
			}
		}
		public Integer MentionLocation()
		{

			try {
				terms=HighFreqTerms.getHighFreqTerms(searcher.getIndexReader(), numTerms, "MentionLocation");

				Integer median=ComputeMedian();
				Integer average=ComputeAverage();
				//System.out.println("MENTION MEDIAN:"+median+"AVERAGE:"+average);
				return average;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Integer.valueOf(0);			}
		}
		public Integer EmoticonLocation()
		{

			try {
				terms=HighFreqTerms.getHighFreqTerms(searcher.getIndexReader(), numTerms, "EmoticonLocation");

				Integer median=ComputeMedian();
				Integer average=ComputeAverage();
				//System.out.println("EMOTICON MEDIAN:"+median+"AVERAGE:"+average);
				return average;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Integer.valueOf(0);			}
		}
		public Integer AverageTweetLength()
		{

			try {
				terms=HighFreqTerms.getHighFreqTerms(searcher.getIndexReader(), numTerms, "tweetLength");

				//tlenght:1 tlenght:5 en cok hangisinden varsa onlari getiricek ve onu bolucez N'e
				Integer median=ComputeMedian();
				Integer average=ComputeAverage();
				//System.out.println("TWEET LENGTH MEDIAN:"+median+"AVERAGE:"+average);
				return average;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Integer.valueOf(0);
			}

		}
		public Integer AverageTweetLengthFiltered()
		{

			try {
				terms=HighFreqTerms.getHighFreqTerms(searcher.getIndexReader(), numTerms, "tweetLengthFiltered");

				//tlenght:1 tlenght:5 en cok hangisinden varsa onlari getiricek ve onu bolucez N'e
				Integer median=ComputeMedian();
				Integer average=ComputeAverage();
				//System.out.println("TWEET LENGTH MEDIAN:"+median+"AVERAGE:"+average);
				return average;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Integer.valueOf(0);
			}

		}

		public Integer AverageTweetLengthSemiFiltered()
		{

			try {
				terms=HighFreqTerms.getHighFreqTerms(searcher.getIndexReader(), numTerms, "tweetLengthSemiFiltered");

				//tlenght:1 tlenght:5 en cok hangisinden varsa onlari getiricek ve onu bolucez N'e
				Integer median=ComputeMedian();
				Integer average=ComputeAverage();
				//System.out.println("TWEET LENGTH MEDIAN:"+median+"AVERAGE:"+average);
				return average;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Integer.valueOf(0);
			}

		}
		public Integer HashLocationFiltered()
		{

			try {
				terms=HighFreqTerms.getHighFreqTerms(searcher.getIndexReader(), numTerms, "HashLocationFiltered");

				Integer median=ComputeMedian();
				Integer average=ComputeAverage();
				//System.out.println("HASH MEDIAN:"+median+"AVERAGE:"+average);
				return average;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Integer.valueOf(0);			}


		}
		public Integer URLLocationFiltered()
		{

			try {
				terms=HighFreqTerms.getHighFreqTerms(searcher.getIndexReader(), numTerms, "URLLocationFiltered");

				Integer median=ComputeMedian();
				Integer average=ComputeAverage();
				//System.out.println("URL MEDIAN:"+median+"AVERAGE:"+average);
				return average;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Integer.valueOf(0);
			}
		}
		public Integer PuncLocationFiltered()
		{

			try {
				terms=HighFreqTerms.getHighFreqTerms(searcher.getIndexReader(), numTerms, "PuncLocationFiltered");

				Integer median=ComputeMedian();
				Integer average=ComputeAverage();
				//System.out.println("PUNC MEDIAN:"+median+"AVERAGE:"+average);
				return average;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Integer.valueOf(0);
			}
		}
		public Integer MentionLocationFiltered()
		{

			try {
				terms=HighFreqTerms.getHighFreqTerms(searcher.getIndexReader(), numTerms, "MentionLocationFiltered");

				Integer median=ComputeMedian();
				Integer average=ComputeAverage();
				//System.out.println("MENTION MEDIAN:"+median+"AVERAGE:"+average);
				return average;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Integer.valueOf(0);			}
		}
		public Integer EmoticonLocationFiltered()
		{

			try {
				terms=HighFreqTerms.getHighFreqTerms(searcher.getIndexReader(), numTerms, "EmoticonLocationFiltered");

				Integer median=ComputeMedian();
				Integer average=ComputeAverage();
				//System.out.println("EMOTICON MEDIAN:"+median+"AVERAGE:"+average);
				return average;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Integer.valueOf(0);			}
		}


	}

}
