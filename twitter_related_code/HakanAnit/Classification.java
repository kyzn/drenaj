import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.FileHandler;

import org.apache.commons.lang.ArrayUtils;
import org.bson.types.ObjectId;

import weka.core.SparseInstance;

import cmu.arktweetnlp.Tagger.TaggedToken;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

import libsvm.LibSVM;
import libsvm.SelfOptimizingLinearLibSVM;
import libsvm.svm;

import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.bayes.NaiveBayesClassifier;
import net.sf.javaml.classification.evaluation.CrossValidation;
import net.sf.javaml.classification.evaluation.PerformanceMeasure;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.filter.normalize.InstanceNormalizeMidrange;
import net.sf.javaml.filter.normalize.NormalizeMean;
import net.sf.javaml.filter.normalize.NormalizeMidrange;


//SVM ile classification yapilacak train dahil hersey bunda

//Instance
//    The representation of real world sample. An instance can have any number of attributes that define it and can have at most one class label.
//Dataset
//    A dataset is a collection of instances that belong together.
//http://java-ml.sourceforge.net/content/basic-terminology

public class Classification
{
	private double[] valFeatureVectorUser,valFeatureVectorOfficial,valFeatureVector;
	private String tweetText,strEventType;
	//tweet pozitif user tweet, official tweet yada negatif olabilir
	private Dataset datasetUsertweet,datasetOfficialtweet;
	private Classifier classifierSVMUsertweet,classifierSVMOfficialtweet,classifierNaiveBayesUsertweet,classifierNaiveBayesOfficialtweet,classifierSelfOpSVMUsertweet,classifierSelfOpSVMOfficialtweet;
	private NormalizeMidrange nmrUser, nmrOfficial;
	//	private NormalizeMean nmrUser,nmrOfficial;
	private net.sf.javaml.core.DenseInstance instanceUserTweet,instanceOfficialTweet; 
	private Integer valIsRT,valGPSAttached,valUserInstanceSize;
	private TextAnalytics textAnalytics;
	private Map<ObjectId, Double> hashmapNaivePosWeights,hashmapNaiveNegWeights,hashmapSortedNaivePosWeights,hashmapSortedNaiveNegWeights,
	hashmapSVMPosWeights,hashmapSVMNegWeights,hashmapSortedSVMPosWeights,hashmapSortedSVMNegWeights,
	hashmapSVMOpPosWeights,hashmapSVMOpNegWeights,hashmapSortedSVMOpPosWeights,hashmapSortedSVMOpNegWeights;
	private List<String> listFeatureVectorItems;
	private Integer valFold=1,valSetSize=0; 
	private List<ObjectId> listAllObjectIds,listTrainIds,listTestIds;
	private Double instanceClassValuePositive=0.0;
	public Double getinstanceClassValuePositive() {
		return instanceClassValuePositive;
	}
	
	private Integer valNormalizeThreshold=51000;
	public Integer getValClassifier() {
		return valClassifier;
	}

	public void setValClassifier(Integer valClassifier) {
		this.valClassifier = valClassifier;
	}
	private Integer valClassifier;

	public TextAnalytics getTextAnalytics() {
		return textAnalytics;
	}

	public void setTextAnalytics(TextAnalytics textAnalytics) {
		this.textAnalytics = textAnalytics;
	}

	public Classification(String eventType,Integer valClassifier) throws IOException
	{
		this.strEventType=eventType;
		this.valClassifier=valClassifier;
		textAnalytics=new TextAnalytics();
	}

	//normalization http://java-ml.sourceforge.net/content/normalization

	private void loadUserDataset() throws IOException
	{
		datasetUsertweet = net.sf.javaml.tools.data.FileHandler.loadDataset(new File("userTweetDataset"+strEventType+".txt"), 0,"\t");
	}
	private void loadOfficialDataset() throws IOException
	{
		datasetOfficialtweet = net.sf.javaml.tools.data.FileHandler.loadDataset(new File("officialTweetDataset"+strEventType+".txt"), 0,"\t");
	}

	//userlarin olay hakkinda attigi positif tweetlerin dataseti
	private void createUserTweetDataset() throws IOException
	{
		datasetUsertweet= new DefaultDataset();
	}
	private void createOfficialTweetDataset() throws IOException
	{
		datasetOfficialtweet= new DefaultDataset();
	}
	private void storeUserTweetDataset() throws IOException
	{
		//dataseti sonra load etmek icin store edelim
		net.sf.javaml.tools.data.FileHandler.exportDataset(datasetUsertweet,new File("userTweetDataset"+strEventType+".txt"));
	}
	private void storeOfficialTweetDataset() throws IOException
	{
		//dataseti sonra load etmek icin store edelim
		net.sf.javaml.tools.data.FileHandler.exportDataset(datasetOfficialtweet,new File("officialTweetDataset"+strEventType+".txt"));
	}

	public String classifyTweetBySelfOpSVM(String tweetText) throws IOException
	{
		//official ve usertweet datasetlerinde classify et

		//	textAnalytics.prepareTextAnalytics(tweetText);
		//		instanceUserTweet=new DenseInstance(prepareTweetFeatureVector(false));
		prepareTweetFeatureVector(false,tweetText);


		//		instanceOfficialTweet=new DenseInstance(prepareTweetFeatureVector(true));

		prepareTweetFeatureVector(true,tweetText);

		nmrUser.filter(instanceUserTweet);

		nmrOfficial.filter(instanceOfficialTweet);
		Object predictedClassValue=classifierSelfOpSVMUsertweet.classify(instanceUserTweet);
		//once pos user tweet ile classify et. eger negative ise official ile sonuc posUser,posOfficial yada negative olur
		///user negative
		String result="";
		System.out.println(predictedClassValue);
		if(predictedClassValue.toString().equals("negative"))
		{
			Object predictedClassValue2=classifierSelfOpSVMOfficialtweet.classify(instanceOfficialTweet);
			if(predictedClassValue2.toString().equals("negative"))
			{
				result="negative";
			}
			else
			{
				result="official positive";
			}

		}
		else
		{
			result="user positive";
		}

		instanceOfficialTweet.clear();
		instanceUserTweet.clear();
		instanceOfficialTweet=null;
		instanceUserTweet=null;
		System.out.println(result);
		textAnalytics.clear();
		return result;
	}
	public String classifyTweetBySVM(String tweetText) throws IOException
	{
		//official ve usertweet datasetlerinde classify et

		//	textAnalytics.prepareTextAnalytics(tweetText);
		//		instanceUserTweet=new DenseInstance(prepareTweetFeatureVector(false));
		prepareTweetFeatureVector(false,tweetText);


		//		instanceOfficialTweet=new DenseInstance(prepareTweetFeatureVector(true));

		prepareTweetFeatureVector(true,tweetText);

		nmrUser.filter(instanceUserTweet);

		nmrOfficial.filter(instanceOfficialTweet);
		Object predictedClassValue=classifierSVMUsertweet.classify(instanceUserTweet);
		//once pos user tweet ile classify et. eger negative ise official ile sonuc posUser,posOfficial yada negative olur
		///user negative
		String result="";
		System.out.println(predictedClassValue);

		Map<Object, Double> distribution = classifierSVMUsertweet.classDistribution(instanceUserTweet);

		Set<Map.Entry<Object, Double>> entries = distribution.entrySet();
		for(Map.Entry<Object, Double> entry : entries) {
			Object key = entry.getKey();
			Double value = entry.getValue();
			System.out.println("CONF_NAIVECLASS"+key.toString()+value.toString());
		}

		if(predictedClassValue.toString().equals("negative"))
		{
			Object predictedClassValue2=classifierSVMOfficialtweet.classify(instanceOfficialTweet);
			if(predictedClassValue2.toString().equals("negative"))
			{
				result="negative";
			}
			else
			{
				result="official positive";
			}

		}
		else
		{
			result="user positive";
		}

		instanceOfficialTweet.clear();
		instanceUserTweet.clear();
		instanceOfficialTweet=null;
		instanceUserTweet=null;
		System.out.println(result);
		textAnalytics.clear();
		return result;
	}
	public String classifyTweetByNaiveBayes(String tweetText) throws IOException
	{
		//official ve usertweet datasetlerinde classify et

		//	textAnalytics.prepareTextAnalytics(tweetText);
		//		instanceUserTweet=new DenseInstance(prepareTweetFeatureVector(false));
		prepareTweetFeatureVector(false,tweetText);


		//		instanceOfficialTweet=new DenseInstance(prepareTweetFeatureVector(true));

		prepareTweetFeatureVector(true,tweetText);

		nmrUser.filter(instanceUserTweet);
		nmrOfficial.filter(instanceOfficialTweet);

		Object predictedClassValue=classifierNaiveBayesUsertweet.classify(instanceUserTweet);
		//once pos user tweet ile classify et. eger negative ise official ile sonuc posUser,posOfficial yada negative olur
		///user negative
		String result="";
		System.out.println(predictedClassValue);
		Map<Object, Double> distribution =classifierNaiveBayesUsertweet.classDistribution(instanceUserTweet);
		Set<Map.Entry<Object, Double>> entries = distribution.entrySet();
		int cntResults=0;
		Double value1=0.0,value2=0.0;
		for(Map.Entry<Object, Double> entry : entries) {

			Object key = entry.getKey();
			Double value = entry.getValue();

			if(cntResults==0)
			{
				value1=value;
				cntResults++;

			}
			else
			{
				value2=value;
			}

			System.out.println("CONF_NAIVECLASS"+key.toString()+value.toString());
		}
		
		instanceClassValuePositive=value1;
//		if(predictedClassValue.toString().equals("negative"))
		if(value1<0.99999)
		{
			Object predictedClassValue2=classifierNaiveBayesOfficialtweet.classify(instanceOfficialTweet);
			if(predictedClassValue2.toString().equals("negative"))
			{
				result="negative";
			}
			else
			{
				result="official positive";
			}

		}
		else
		{
			result="user positive";
		}

		instanceOfficialTweet.clear();
		instanceUserTweet.clear();
		instanceOfficialTweet=null;
		instanceUserTweet=null;
		System.out.println(result);
		textAnalytics.clear();
		return result;
	}

	//feature vectoru hazirlar
	private void prepareTweetFeatureVector(Boolean boolOfficial,String tweetText) throws IOException
	{

		List<Double> listVals=textAnalytics.prepareTextAnalytics(tweetText);																																																																																																											
		//		System.out.println("size instance bunda"+listVals.size());
		//		System.out.println("asdas"+listVals.size());

		if(boolOfficial)
		{
			instanceOfficialTweet=new net.sf.javaml.core.DenseInstance(listVals.size());

			//			valFeatureVectorOfficial=new double[]{0};
			for (int i = 0; i < listVals.size(); i++)
			{
				//				if(listVals.get(i)!=-1)
				//				{
				instanceOfficialTweet.put(i, listVals.get(i));
				//				}
			}

		}
		else
		{
			instanceUserTweet=new net.sf.javaml.core.DenseInstance(listVals.size());

			for (int i = 0; i < listVals.size(); i++)
			{
				//				if(listVals.get(i)!=-1)
				//				{
				instanceUserTweet.put(i, listVals.get(i));
				//				}
			}
		}


	}

	private void fetchTweetsForTrainingFromDBAndCreateDataset(Boolean boolUseFilter,Boolean boolToUserSet,Integer valTweetsFromSet,Boolean boolTrainClassAsPos, String eventType,Integer valUseFile) throws IOException
	{
		net.sf.javaml.core.DenseInstance instancePosTweet;

		//once mongodan user tweetleri cekicez. islicez. userTweetInstace olusturcaz ve data ya eklicez.

		DBConnect.connect();
		String strFilename="";
		switch (valTweetsFromSet)
		{
		//sadce train icin olanlarla train ediyoruz
		case 1:

			strFilename=eventType+valFold+"_userpos_train"+valUseFile.toString()+".txt";
			//earthquake_userpos_train0 olan dosya acilcak ondan id ler okunacak
			break;
		case 2:

			strFilename=eventType+valFold+"_officialpos_train"+valUseFile.toString()+".txt";

			break;
		case 3:
			//negativelerde hepsinde ayni

			strFilename=eventType+valFold+"_neg_train"+valUseFile.toString()+".txt";

			break;
		}

		//simdi dosyayi okumak icin ac

		FileInputStream fstream = new FileInputStream(strFilename);
		// Get the object of DataInputStream
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		//Read File Line By Line

		DBCursor cursor;
		DBObject query=null;
		while ((strLine = br.readLine()) != null)  
		{
			//event teki object id direk okudugum
			ObjectId objId=new ObjectId(strLine);
			//object id si o olani cek evetten
			query= QueryBuilder.start().and(
					QueryBuilder.start("_id").is(objId).get()
					).get();
			DBObject o;
			BasicDBList tweet1;
			cursor = DBConnect.getCollEvent().find(query);

			o=cursor.next();
			tweet1=(BasicDBList)o.get("tweet");
			if(!tweet1.equals(null))
			{

				if(((BasicDBObject)tweet1.get(0)).get("isGeoAttached")!=null) //geoAttached idi adini degistirdim ondan eldekiler gitmesin
				{
					if(((BasicDBObject)tweet1.get(0)).get("isGeoAttached").toString().equals("0"))
					{
						valGPSAttached=0;
					}
					else
						valGPSAttached=1;
				}
				else
					valGPSAttached=0;

				if(((BasicDBObject)tweet1.get(0)).get("isRT").toString().equals("1"))
					valIsRT=1;
				else
					valIsRT=0;

				String tweetText=((BasicDBObject)tweet1.get(0)).get("originalTweet").toString();

				List<Double> listVals=textAnalytics.prepareTextAnalytics(tweetText);	
				instancePosTweet=new net.sf.javaml.core.DenseInstance(listVals.size());
				instancePosTweet.setClassValue((boolTrainClassAsPos?"positive":"negative"));

				for (int i = 0; i < listVals.size(); i++)
				{
					//							if(listVals.get(i)!=-1)
					//							{
					instancePosTweet.put(i, listVals.get(i));
					//							}
				}

				if(boolToUserSet)
				{

					datasetUsertweet.add(instancePosTweet);

				}
				else
				{

					datasetOfficialtweet.add(instancePosTweet);
				}
				listVals.clear();
				listVals=null;

				textAnalytics.clear();


				//						instancePosTweet.clear();
			}

			tweet1.clear();
			tweet1=null;
			o=null;



			cursor.close();

		}
		//Close the input stream
		in.close();


	}
	private void GetWeightsClassification(net.sf.javaml.core.DenseInstance instancePosTweet,Integer valClassifier,Integer valSetType,ObjectId eventid)
	{
		Map<Object,Double> mapResult=null;
		System.out.println("CLAS:"+valClassifier+" SET:"+valSetType);
		switch (valClassifier) 
		{
		case 0:
			switch (valSetType)
			{
			case 0:
				mapResult= classifierSVMUsertweet.classDistribution(instancePosTweet);

				break;
			case 1:
				mapResult= classifierSVMOfficialtweet.classDistribution(instancePosTweet);

				break;
			}
			break;
		case 1:
			switch (valSetType)
			{
			case 0:
				mapResult= classifierNaiveBayesUsertweet.classDistribution(instancePosTweet);

				break;
			case 1:
				mapResult= classifierNaiveBayesOfficialtweet.classDistribution(instancePosTweet);

				break;
			}
			break;
		case 2:
			switch (valSetType)
			{
			case 0:
				mapResult= classifierSelfOpSVMUsertweet.classDistribution(instancePosTweet);

				break;
			case 1:
				mapResult= classifierSelfOpSVMOfficialtweet.classDistribution(instancePosTweet);

				break;
			}
			break;
		}

		System.out.println("BUNE:");

		Set<Map.Entry<Object,Double>> entries = mapResult.entrySet();
		for(Map.Entry<Object,Double> entry : entries) {
			Object key = entry.getKey();
			Double value = entry.getValue();
			System.out.printf("%s = %s%n", key, value);
			//id si key value su weight olarak map e koy

			if(key.toString().equals("positive"))
			{
				switch (valClassifier) {
				case 0:
					hashmapSVMPosWeights.put(eventid, value);

					break;
				case 1:
					hashmapNaivePosWeights.put(eventid, value);

					break;
				case 2:
					hashmapSVMOpPosWeights.put(eventid, value);

					break;
				}
			}
			else
			{
				System.out.println("asdasd:"+valClassifier);
				switch (valClassifier)
				{
				case 0:
					hashmapSVMNegWeights.put(eventid, value);

					break;
				case 1:
					hashmapNaiveNegWeights.put(eventid, value);

					break;
				case 2:
					hashmapSVMOpNegWeights.put(eventid, value);

					break;
				}

			}
		}
		//		Object predictedClassValue=classifierNaiveBayesUsertweet.classify(instancePosTweet);
		//		System.out.println(predictedClassValue);
	}
	public void topNPositiveNegative(Integer valClassifier,Integer valUser0Official1) throws IOException
	{
		hashmapNaiveNegWeights=new HashMap<ObjectId, Double>();
		hashmapNaivePosWeights=new HashMap<ObjectId, Double>();
		hashmapSVMNegWeights=new HashMap<ObjectId, Double>();
		hashmapSVMPosWeights=new HashMap<ObjectId, Double>();
		hashmapSVMOpNegWeights=new HashMap<ObjectId, Double>();
		hashmapSVMOpPosWeights=new HashMap<ObjectId, Double>();

		//verilen classifiera gore elimizdeki user positive butun orneklerin yada off pos yada neg 
		//her bir instance classify ettirilir ve weight i alinir. 

		//testte olmayan user pos olan butun hepsini cek classify ettir.
		net.sf.javaml.core.DenseInstance instancePosTweet;
		listFeatureVectorItems=new ArrayList<String>();
		listFeatureVectorItems.add("org length");
		listFeatureVectorItems.add("stop word cnt");
		listFeatureVectorItems.add("semi length");
		listFeatureVectorItems.add("emr. word pos1");
		listFeatureVectorItems.add("emr. word pos2");
		listFeatureVectorItems.add("emr. word pos3");
		listFeatureVectorItems.add("emr. word pos4");
		listFeatureVectorItems.add("hash pos1");
		listFeatureVectorItems.add("hash pos2");
		listFeatureVectorItems.add("hash pos3");
		listFeatureVectorItems.add("hash pos4");
		listFeatureVectorItems.add("loc pos1");
		listFeatureVectorItems.add("loc pos2");
		listFeatureVectorItems.add("loc pos3");
		listFeatureVectorItems.add("loc pos4");
		listFeatureVectorItems.add("mention pos1");
		listFeatureVectorItems.add("mention pos2");
		listFeatureVectorItems.add("mention pos3");
		listFeatureVectorItems.add("mention pos4");
		listFeatureVectorItems.add("emoticon pos1");
		listFeatureVectorItems.add("emoticon pos2");
		listFeatureVectorItems.add("emoticon pos3");
		listFeatureVectorItems.add("emoticon pos4");
		listFeatureVectorItems.add("url pos1");
		listFeatureVectorItems.add("url pos2");
		listFeatureVectorItems.add("url pos3");
		listFeatureVectorItems.add("url pos4");
		listFeatureVectorItems.add("punc pos1");
		listFeatureVectorItems.add("punc pos2");
		listFeatureVectorItems.add("punc pos3");
		listFeatureVectorItems.add("punc pos4");
		listFeatureVectorItems.add("_emr. word left1-1");
		listFeatureVectorItems.add("_emr. word left1-2");
		listFeatureVectorItems.add("-emr. word left1-3");
		listFeatureVectorItems.add("_emr. word left1-4");
		listFeatureVectorItems.add("_emr. word left2-1");
		listFeatureVectorItems.add("_emr. word left2-2");
		listFeatureVectorItems.add("_emr. word left2-3");
		listFeatureVectorItems.add("_emr. word left2-4");
		listFeatureVectorItems.add("_emr. word right1-1");
		listFeatureVectorItems.add("_emr. word right1-2");
		listFeatureVectorItems.add("_emr. word right1-3");
		listFeatureVectorItems.add("_emr. word right1-4");
		listFeatureVectorItems.add("_emr. word right2-1");
		listFeatureVectorItems.add("_emr. word right2-2");
		listFeatureVectorItems.add("_emr. word right2-3");
		listFeatureVectorItems.add("_emr. word right2-4");

		listFeatureVectorItems.add("_loc. word left1-1");
		listFeatureVectorItems.add("_loc. word left1-2");
		listFeatureVectorItems.add("_loc. word left1-3");
		listFeatureVectorItems.add("_loc. word left1-4");
		listFeatureVectorItems.add("_loc. word left2-1");
		listFeatureVectorItems.add("_loc. word left2-2");
		listFeatureVectorItems.add("_loc. word left2-3");
		listFeatureVectorItems.add("_loc. word left2-4");
		listFeatureVectorItems.add("_loc. word right1-1");
		listFeatureVectorItems.add("_loc. word right1-2");
		listFeatureVectorItems.add("_loc. word right1-3");
		listFeatureVectorItems.add("_loc. word right1-4");
		listFeatureVectorItems.add("_loc. word right2-1");
		listFeatureVectorItems.add("_loc. word right2-2");
		listFeatureVectorItems.add("_loc. word right2-3");
		listFeatureVectorItems.add("_loc. word right2-4");
		listFeatureVectorItems.add("_emr. word 1");
		listFeatureVectorItems.add("_emr. word 2");
		listFeatureVectorItems.add("_emr. word 3");
		listFeatureVectorItems.add("_emr. word 4");
		//once mongodan user tweetleri cekicez. islicez. userTweetInstace olusturcaz ve data ya eklicez.

		DBConnect.connect();
		DBObject query1,query2,query3;

		query1= QueryBuilder.start().and(
				QueryBuilder.start("eventType").is(strEventType).get(),
				QueryBuilder.start("inSet").is("userPosSet").get(),
				QueryBuilder.start("inTest").exists(false).get()
				).get();
		query2= QueryBuilder.start().and(
				QueryBuilder.start("eventType").is(strEventType).get(),
				QueryBuilder.start("inSet").is("officialPosSet").get(),
				QueryBuilder.start("inTest").exists(false).get()
				).get();
		query1= QueryBuilder.start().and(
				QueryBuilder.start("eventType").is(strEventType).get(),
				QueryBuilder.start("inSet").is("userPosSet").get(),
				QueryBuilder.start("inTest").exists(false).get()
				).get();
		query3= QueryBuilder.start().and(
				QueryBuilder.start("eventType").is(strEventType).get(),
				QueryBuilder.start("inSet").is("negSet").get(),
				QueryBuilder.start("inTest").exists(false).get()
				).get();


		int k=valUser0Official1.intValue();
		int tweetBuffer=30;
		DBCursor cursor;
		int size=0;

		if(k==0)
			size= DBConnect.getCollEvent().find(query1).count();
		else if(k==1)
			size= DBConnect.getCollEvent().find(query2).count();
		else if(k==2)
			size= DBConnect.getCollEvent().find(query3).count();



		System.out.println("query:"+size);

		int skip;
		DBObject o;
		BasicDBList tweet1;
		ObjectId eventid;
		DBConnect.queryCount=size;


		//hepsini cekmiyoruz kademe kademe
		for(int j=0;j<Math.ceil(size/tweetBuffer)+1;j++)
		{
			skip=tweetBuffer*j;

			cursor=null;

			if(k==0)
				cursor = DBConnect.getCollEvent().find(query1).limit(tweetBuffer).skip(skip);
			else if(k==1)
				cursor = DBConnect.getCollEvent().find(query2).limit(tweetBuffer).skip(skip);
			else if(k==2)
				cursor = DBConnect.getCollEvent().find(query3).limit(tweetBuffer).skip(skip);


			try 
			{
				while(cursor.hasNext())
				{

					o=cursor.next();
					tweet1=(BasicDBList)o.get("tweet");
					eventid=(ObjectId)o.get("_id");
					if(!tweet1.equals(null))
					{
						if(((BasicDBObject)tweet1.get(0)).get("isGeoAttached")!=null) //geoAttached idi adini degistirdim ondan eldekiler gitmesin
						{
							if(((BasicDBObject)tweet1.get(0)).get("isGeoAttached").toString().equals("0"))
							{
								valGPSAttached=0;
							}
							else
								valGPSAttached=1;
						}
						else
							valGPSAttached=0;

						if(((BasicDBObject)tweet1.get(0)).get("isRT").toString().equals("1"))
							valIsRT=1;
						else
							valIsRT=0;

						String tweetText=((BasicDBObject)tweet1.get(0)).get("originalTweet").toString();

						List<Double> listVals=textAnalytics.prepareTextAnalytics(tweetText);	
						instancePosTweet=new net.sf.javaml.core.DenseInstance(listVals.size());
						//							instancePosTweet.setClassValue("positive");

						for (int i = 0; i < listVals.size(); i++)
						{
							//							if(listVals.get(i)!=-1)
							//							{
							instancePosTweet.put(i, listVals.get(i));
							//							}
						}

						switch (k) {
						case 0:
							nmrUser.filter(instancePosTweet);

							GetWeightsClassification(instancePosTweet, valClassifier, 0,eventid);

							break;

						case 1:
							nmrOfficial.filter(instancePosTweet);

							GetWeightsClassification(instancePosTweet, valClassifier, 1,eventid);

							break;
						case 2:
							nmrUser.filter(instancePosTweet);

							GetWeightsClassification(instancePosTweet, valClassifier, 0,eventid);

							break;

						}

						tweet1.clear();
						tweet1=null;
						o=null;

						listVals.clear();
						listVals=null;

						textAnalytics.clear();

						eventid=null;
					}

				}



			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			finally 
			{

				cursor.close();

			}

		}
		//artik hashmapPos neg onlari sort etmek ve feature vektoru tekrar olusturup sonuclari file a yazmak gerekli
		//bu bize positive setimizde nasil mesajlar olursa onlarin posizif olarak gosterilecegini hangi mesajlarin ise postivie set 
		//icinde elenecegini gosterecek
		switch (valClassifier) {
		case 0:
			hashmapSortedSVMPosWeights=MapSortByValue.sortByValueDescending(hashmapSVMPosWeights);
			hashmapSortedSVMNegWeights=MapSortByValue.sortByValueDescending(hashmapSVMNegWeights);
			writeFeaturesToFileWithWeights("TopNWeights_SVM.txt", hashmapSortedSVMPosWeights);
			writeFeaturesToFileWithWeights("TopNWeights_SVM.txt", hashmapSortedSVMNegWeights);
			hashmapSortedSVMNegWeights.clear();
			hashmapSortedSVMPosWeights.clear();
			break;

		case 1:
			hashmapSortedNaivePosWeights=MapSortByValue.sortByValueDescending(hashmapNaivePosWeights);
			hashmapSortedNaiveNegWeights=MapSortByValue.sortByValueDescending(hashmapNaiveNegWeights);
			writeFeaturesToFileWithWeights("TopNWeights_Naive.txt", hashmapSortedNaivePosWeights);
			writeFeaturesToFileWithWeights("TopNWeights_Naive.txt", hashmapSortedNaiveNegWeights);
			hashmapSortedNaiveNegWeights.clear();
			hashmapSortedNaivePosWeights.clear();

			break;
		case 2:
			hashmapSortedSVMOpPosWeights=MapSortByValue.sortByValueDescending(hashmapSVMOpPosWeights);
			hashmapSortedSVMOpNegWeights=MapSortByValue.sortByValueDescending(hashmapSVMOpNegWeights);
			writeFeaturesToFileWithWeights("TopNWeights_SVMOp.txt", hashmapSortedSVMOpPosWeights);
			writeFeaturesToFileWithWeights("TopNWeights_SVMOp.txt", hashmapSortedSVMOpNegWeights);
			hashmapSortedSVMOpNegWeights.clear();
			hashmapSortedSVMOpPosWeights.clear();
			break;
		}

		hashmapSVMNegWeights.clear();
		hashmapSVMPosWeights.clear();

		hashmapNaiveNegWeights.clear();
		hashmapNaivePosWeights.clear();

		hashmapSVMOpNegWeights.clear();
		hashmapSVMOpPosWeights.clear();






	}
	private void writeFeaturesToFileWithWeights(String strFilename,Map<ObjectId, Double> hashmapWeights) throws IOException
	{
		//simdi maplerin sorted halleri var
		//ilk 10 tanesini alicam id yi alinca dbden texti cekip tekrar instance i yaaratiyoruz ve ozellikleri neyse onu file a yaziyoruz
		int valCnt=0,valCnt1=0;
		Set<Map.Entry<ObjectId,Double>> entries = hashmapWeights.entrySet();
		FileWriter outFile = new FileWriter(strFilename,true);
		PrintWriter out = new PrintWriter(outFile);
		for(Map.Entry<ObjectId,Double> entry : entries) 
		{
			ObjectId key = entry.getKey();
			Double value = entry.getValue();
			if(valCnt==30)
				break;
			valCnt++;

			//dbden idsi key olanin tweettextini cek instance olustur
			String strTweetText=(String)DBConnect.fetchTweetFieldFromDBGivenID("originalTweet", key);
			//bun instance User icinne koyacak
			try {
				prepareTweetFeatureVector(false, strTweetText);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Set<Map.Entry<Integer,Double>> entrie = instanceUserTweet.entrySet();
			valCnt1=0;

			for(Map.Entry<Integer,Double> entr : entrie) {
				Object key1 = entr.getKey();
				Double value1 = entr.getValue();
				//						System.out.printf("Feature %s = %s%n", key1, value1);
				//simdi featurelari ve weighti dosyaya yazalim
				if(valCnt1==0)
				{

					out.print(valCnt+")");
					out.print(" tweet: "+strTweetText);
					out.print(" org. length: "+value1);
					out.print(" weight:"+value);

				}
				else
				{
					//eger textinin icinde _ varsa o idye ait wordun ne oldugunu cek
					if(listFeatureVectorItems.get(valCnt1).contains("_"))
					{
						if(value1!=-1)
							out.print(" "+listFeatureVectorItems.get(valCnt1)+": "+DBConnect.fetchValueFromWordIDGivenID(value1.intValue()));

					}
					else
					{
						if(value1!=-1)
							out.print(" "+listFeatureVectorItems.get(valCnt1)+": "+value1);

					}
				}
				valCnt1++;
			}
			instanceUserTweet.clear();
			instanceUserTweet=null;
			out.println("");

		}
		out.close();

	}
	public void prepareTraining(Boolean loadDataset) throws IOException
	{

		createUserTweetDataset();
		createOfficialTweetDataset();


		nmrOfficial=new NormalizeMidrange(valNormalizeThreshold,valNormalizeThreshold);
		nmrUser= new NormalizeMidrange(valNormalizeThreshold,valNormalizeThreshold); //500 1000 di
		//nmrOfficial=new NormalizeMean();
		//nmrUser=new NormalizeMean();


		//user positive: status1 olanlar negative: officiallar status1 ve status-1ler
		//official positive: status1 official olarnlar negative: status1 official false ve status -1ler

		if(!loadDataset)
		{
			//textAnalytics.storeWordIdsToFile();

			//filter edicez artik
			System.out.println("generating dataset");

			fetchTweetsForTrainingFromDBAndCreateDataset(true, true, 1, true, strEventType,0);//sadece user ve positive
			fetchTweetsForTrainingFromDBAndCreateDataset(true, true, 3, false, strEventType,0); //negative ler
			fetchTweetsForTrainingFromDBAndCreateDataset(true, true, 2, false, strEventType,0);//sadece official ve positive


			fetchTweetsForTrainingFromDBAndCreateDataset(true, false, 2, true,strEventType,0);//sadece official ve positive
			fetchTweetsForTrainingFromDBAndCreateDataset(true, false, 3, false,strEventType,0); //negative ler
			fetchTweetsForTrainingFromDBAndCreateDataset(true, false, 1, false, strEventType,0);//sadece user ve positive



			System.out.println("storing dataset");

			storeUserTweetDataset();
			storeOfficialTweetDataset();
			System.out.println("dataset stored");
		}
		else
		{
			//textAnalytics.loadWordIdsFromFile();
			loadUserDataset();
			loadOfficialDataset();
		}

		nmrUser.build(datasetUsertweet);
		nmrOfficial.build(datasetOfficialtweet);

		nmrUser.filter(datasetUsertweet);
		nmrOfficial.filter(datasetOfficialtweet);

		//Linear SVM
		classifierSVMUsertweet=new LibSVM();
		classifierSVMOfficialtweet=new LibSVM();
		//Naive bayes
		Boolean boolUseLaplace=true;
		Boolean boolUseLogs=true;
		classifierNaiveBayesUsertweet= new NaiveBayesClassifier(boolUseLaplace, boolUseLogs, false);
		classifierNaiveBayesOfficialtweet= new NaiveBayesClassifier(boolUseLaplace, boolUseLogs, false);

		//Self Optimizing Linear SVM
		classifierSelfOpSVMUsertweet= new SelfOptimizingLinearLibSVM();
		classifierSelfOpSVMOfficialtweet= new SelfOptimizingLinearLibSVM();


		System.out.println("building classifier");

		switch (valClassifier) {
		case 0:
			classifierSVMUsertweet.buildClassifier(datasetUsertweet);
			classifierSVMOfficialtweet.buildClassifier(datasetOfficialtweet);
			break;

		case 1:
			classifierNaiveBayesUsertweet.buildClassifier(datasetUsertweet);
			classifierNaiveBayesOfficialtweet.buildClassifier(datasetOfficialtweet);
			break;
		case 2:
			classifierSelfOpSVMUsertweet.buildClassifier(datasetUsertweet);
			classifierSelfOpSVMOfficialtweet.buildClassifier(datasetOfficialtweet);

		}

		System.out.println("classifier build");

		//		/* Counters for correct and wrong predictions. */
		//		int correct = 0, wrong = 0;
		//		/* Classify all instances and check with the correct class values */
		//		for (Instance inst : datasetUsertweet) {
		//			Object predictedClassValue = classifierSVMUsertweet.classify(inst);
		//			Object realClassValue = inst.classValue();
		//			if (predictedClassValue.equals(realClassValue))
		//				correct++;
		//			else
		//				wrong++;
		//			
		//		}
		//		System.out.println("Correct predictions  " + correct);
		//		System.out.println("Wrong predictions " + wrong);
	}
	//cross validation

	//test + train hepsini al userPos olan
	//negative set ayni soyleki official+negative olanlar
	//test+train i kac fold ise ona bol diyelim total 1000 mesaj var 200luk grup olacak
	//random olarak sec 200*(n-1) tane onunla sistemi train et digerleri. 1 set 200luk test olacak
	//her set icin bunu yapacaksin ve basari orani kacsa ortalamasini sunacaksin

	public void StartCrossValidation(Integer valFold) throws IOException
	{
		this.valFold=valFold;
		GenerateTrainTestIDsAndEvaluate();


	}
	private void GenerateTrainTestIDsAndEvaluate() throws IOException
	{

		String strFilename="";

		strFilename="crossvalUser.txt";


		FileWriter outFile = new FileWriter(strFilename,true);
		PrintWriter out = new PrintWriter(outFile);

		Double valPercentage=0.0;

		for (int i = 0; i < valFold; i++)
		{

			listTestIds=new ArrayList<ObjectId>();

			createUserTweetDataset();
			nmrUser= new NormalizeMidrange(valNormalizeThreshold,valNormalizeThreshold); //7500 7500 di
			fetchTweetsForTrainingFromDBAndCreateDataset(true, true, 3, false, strEventType,i); //negative ler
			fetchTweetsForTrainingFromDBAndCreateDataset(true, true, 2,false, strEventType,i);//sadece official ve positive
			fetchTweetsForTrainingFromDBAndCreateDataset(true, true, 1,true, strEventType,i); //user positiveler


			nmrUser.build(datasetUsertweet);	
			nmrUser.filter(datasetUsertweet);

			//Linear SVM
			classifierSVMUsertweet=new LibSVM();
			//Naive bayes
			Boolean boolUseLaplace=true;
			Boolean boolUseLogs=true;
			classifierNaiveBayesUsertweet= new NaiveBayesClassifier(boolUseLaplace, boolUseLogs, false);

			//Self Optimizing Linear SVM
			classifierSelfOpSVMUsertweet= new SelfOptimizingLinearLibSVM();

			System.out.println("building classifier"+valClassifier);

			switch (valClassifier)
			{
			case 0:

				classifierSVMUsertweet.buildClassifier(datasetUsertweet);

				break;


			case 1:

				classifierNaiveBayesUsertweet.buildClassifier(datasetUsertweet);

				break;


			case 2:

				classifierSelfOpSVMUsertweet.buildClassifier(datasetUsertweet);

				break;




			}

			System.out.println("classifier build");
			//ana for bitis
			Integer cnt1=0,cnt2=0,cnt3=0;
			for (int j = 0; j < 3; j++)
			{
				//listTestIds denen listeye fold kacsa onun test idleri oku ve at
				String strFilename1="";

				switch (j) {
				case 0:
					strFilename1=strEventType+valFold+"_userpos_test"+i+".txt";
					break;

				case 1:
					strFilename1=strEventType+valFold+"_officialpos_test"+i+".txt";
					break;
				case 2:
					strFilename1=strEventType+valFold+"_neg_test"+i+".txt";
					break;
				}
				//officalpos_test ve negtest_0 i da at list test ids e

				//simdi dosyayi okumak icin ac

				FileInputStream fstream = new FileInputStream(strFilename1);
				// Get the object of DataInputStream
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String strLine;
				//Read File Line By Line
				while ((strLine = br.readLine()) != null)  
				{
					//event teki object id direk okudugum

					ObjectId objId=new ObjectId(strLine);
					listTestIds.add(objId);
					switch (j) {
					case 0:
						cnt1++;
						break;

					case 1:
						cnt2++;
						break;
					case 2:
						cnt3++;
						break;
					}

				}
				in.close();
			}

			////
			//simdi traini okuyarak classify et daha sonra butun train olanlarda inSet&classifiedAs bakicalak oranlar bulunacak
			streamTestFromTestList();
			//simdi oranlari bul ve sonucu dosyaya yaz
			List<Double> listEval=evaluateClassifierReturnPercentage();
			//fold i 
			out.println("fold "+i+"total all: "+listTestIds.size()+" precision: "+ listEval.get(0)+" recall: "+ listEval.get(1)+" fscore: "+ listEval.get(2)
					+" gercek: "+ listEval.get(3)+" bulduamadegil: "+ listEval.get(4)+" cnt user pos demis nege: "+ listEval.get(5)+" cnt user pos demis officiale: "+
					listEval.get(6)+" user pos count: "+ cnt1+" offfiaal pos count: "+ cnt2+" neg pos count: "+ cnt3);

			//fold valFold-1==i ise butun percentagelari topla valfolda bol
			//clear
			listTestIds.clear();

			listTestIds=null;
			datasetUsertweet.clear();
			datasetUsertweet=null;


			nmrUser=null;


			classifierSVMUsertweet=null;
			classifierNaiveBayesUsertweet= null;
			classifierSelfOpSVMUsertweet= null;

			System.gc();



		}
		out.close();
		out=null;

	}

	private List<Double> evaluateClassifierReturnPercentage() throws IOException
	{
		int tweetBuffer=30;
		DBCursor cursor;
		double cntGercek=0,cnt1=0,cntBulduAmaDegil=0,cntNeg=0,cntOfficial=0;
		for (ObjectId objectid : listTestIds)
		{
			DBObject query=null;
			BasicDBList tweet1;
			ObjectId eventID;

			query = QueryBuilder.start().and(
					QueryBuilder.start("_id").is(objectid).get()
					).get();

			DBObject tweetobj = DBConnect.getCollEvent().findOne(query);

			if(!tweetobj.equals(null))
			{

				String classifiedAs=(String)tweetobj.get("classifiedAs");
				String inSet=(String)tweetobj.get("inSet");

				//				else if(inSet.equals("negSet")&&classifiedAs.equals("negative"))
				//					cntGercek++;
				//				else if(inSet.equals("officialPosSet")&&classifiedAs.equals("official positive"))
				//					cntGercek++;

				if(inSet.equals("userPosSet")&&classifiedAs.equals("user positive"))
					cntGercek++;
				//user positive icin yapiyorsun
				if(inSet.equals("negSet")&&classifiedAs.equals("user positive"))
				{
					cntBulduAmaDegil++;
					cntNeg++;
				}


				if(inSet.equals("officialPosSet")&&classifiedAs.equals("user positive"))
				{
					cntBulduAmaDegil++;
					cntOfficial++;
				}

				if(inSet.equals("userPosSet"))
				{

					cnt1++;
				}


				tweetobj=null;


			}

		}
		System.out.println("CNT1="+cnt1+"CNTBZLDUAMADEGIL="+cntBulduAmaDegil);
		double valPrecision=(double)(cntGercek/(cntBulduAmaDegil+cntGercek));
		double valRecall=(double)(cntGercek/cnt1);
		double valFScore=2*((valPrecision*valRecall)/(valPrecision+valRecall));
		List<Double> list=new ArrayList<Double>();
		list.add(valPrecision);
		list.add(valRecall);
		list.add(valFScore);
		list.add(cntGercek);
		list.add(cntBulduAmaDegil);
		list.add(cntNeg);
		list.add(cntOfficial);


		return list;

	}

	private void streamTestFromTestList() throws IOException
	{
		int tweetBuffer=30;
		DBCursor cursor;
		int size=0;

		for (ObjectId objectid : listTestIds)
		{
			DBObject query=null;
			BasicDBList tweet1;
			ObjectId eventID;

			query = QueryBuilder.start().and(
					QueryBuilder.start("_id").is(objectid).get()
					).get();

			DBObject tweetobj = DBConnect.getCollEvent().findOne(query);

			if(!tweetobj.equals(null))
			{

				tweet1=(BasicDBList)tweetobj.get("tweet");


				Integer i=Integer.parseInt(((BasicDBObject)tweet1.get(0)).get("createdAt").toString());

				//TweetStat olustur ve onu buffer'a ekle
				String strOriginalTweetText=((BasicDBObject)tweet1.get(0)).get("originalTweet").toString();


				prepareTweetFeatureVector(false,strOriginalTweetText);
				nmrUser.filter(instanceUserTweet);



				Object predictedClassValue=null;
				String result="";

				switch (valClassifier) {
				case 0:


					predictedClassValue=classifierSVMUsertweet.classify(instanceUserTweet);

					if(predictedClassValue.toString().equals("negative"))
					{
						result="negative";

					}
					else
					{
						result="user positive";
					}
					instanceUserTweet.clear();
					instanceUserTweet=null;





					textAnalytics.clear();

					break;


				case 1:

					predictedClassValue=classifierNaiveBayesUsertweet.classify(instanceUserTweet);
					Map<Object, Double> distribution =classifierNaiveBayesUsertweet.classDistribution(instanceUserTweet);
					Set<Map.Entry<Object, Double>> entries = distribution.entrySet();
					for(Map.Entry<Object, Double> entry : entries) {
						//				            Integer key = entry.getKey();
						double value = entry.getValue();
						System.out.printf("%d \n",value);
					}
					if(predictedClassValue.toString().equals("negative"))
					{
						result="negative";

					}
					else
					{
						result="user positive";
					}
					instanceUserTweet.clear();
					instanceUserTweet=null;



					textAnalytics.clear();

					break;
				case 2:

					predictedClassValue=classifierSelfOpSVMUsertweet.classify(instanceUserTweet);
					if(predictedClassValue.toString().equals("negative"))
					{
						result="negative";

					}
					else
					{
						result="user positive";
					}
					instanceUserTweet.clear();
					instanceUserTweet=null;


					textAnalytics.clear();
					break;
				}

				BasicDBObject queryT = new BasicDBObject();
				queryT.put("_id", objectid);

				DBObject newObject =  DBConnect.getCollEvent().find(queryT).toArray().get(0);
				newObject.put("classifiedAs", result);
				DBConnect.getCollEvent().findAndModify(queryT, newObject);


				tweet1.clear();
				tweet1=null;
				tweetobj=null;


				getTextAnalytics().clear();

			}

		}

	}


	private void fetchTweetsForTrainingFromListAndCreateDataset(Boolean boolUseFilter,Boolean boolToUserSet,Boolean boolTrainClassAsPos, String eventType) throws IOException
	{
		net.sf.javaml.core.DenseInstance instancePosTweet;
		for (ObjectId objectid : listTrainIds)
		{
			DBObject query=null;
			BasicDBList tweet1;
			ObjectId eventID;

			query = QueryBuilder.start().and(
					QueryBuilder.start("_id").is(objectid).get()
					).get();

			DBObject tweetobj = DBConnect.getCollEvent().findOne(query);

			if(!tweetobj.equals(null))
			{

				tweet1=(BasicDBList)tweetobj.get("tweet");
				eventID=(ObjectId)tweetobj.get("_id");
				if(!tweet1.equals(null))
				{

					if(((BasicDBObject)tweet1.get(0)).get("isGeoAttached")!=null) //geoAttached idi adini degistirdim ondan eldekiler gitmesin
					{
						if(((BasicDBObject)tweet1.get(0)).get("isGeoAttached").toString().equals("0"))
						{
							valGPSAttached=0;
						}
						else
							valGPSAttached=1;
					}
					else
						valGPSAttached=0;

					if(((BasicDBObject)tweet1.get(0)).get("isRT").toString().equals("1"))
						valIsRT=1;
					else
						valIsRT=0;

					String tweetText=((BasicDBObject)tweet1.get(0)).get("originalTweet").toString();

					List<Double> listVals=textAnalytics.prepareTextAnalytics(tweetText);	
					instancePosTweet=new net.sf.javaml.core.DenseInstance(listVals.size());
					instancePosTweet.setClassValue((boolTrainClassAsPos?"positive":"negative"));

					for (int i = 0; i < listVals.size(); i++)
					{
						//							if(listVals.get(i)!=-1)
						//							{
						instancePosTweet.put(i, listVals.get(i));
						//							}
					}

					if(boolToUserSet)
					{

						datasetUsertweet.add(instancePosTweet);

					}
					else
					{

						datasetOfficialtweet.add(instancePosTweet);
					}
					listVals.clear();
					listVals=null;

					textAnalytics.clear();


					//						instancePosTweet.clear();
				}

				tweet1.clear();
				tweet1=null;
				tweetobj=null;

				eventID=null;
				//					System.gc();
			}

		}
	}


	public void PrepareTestAndTrainSetOnFile(Integer valFold) throws IOException
	{

		//oncelikle butun userpos olanlarin idlerini alip bir arraye atalim
		List<ObjectId> listAllUserPosIds=new ArrayList<ObjectId>();
		List<ObjectId> listAllOfficialPosIds=new ArrayList<ObjectId>();
		List<ObjectId> listAllNegIds=new ArrayList<ObjectId>();


		List<ObjectId> listUserPosTrainIds=new ArrayList<ObjectId>();
		List<ObjectId> listUserPosTestIds=new ArrayList<ObjectId>();

		List<ObjectId> listOfficialPosTrainIds=new ArrayList<ObjectId>();
		List<ObjectId> listOfficialPosTestIds=new ArrayList<ObjectId>();

		List<ObjectId> listNegTrainIds=new ArrayList<ObjectId>();
		List<ObjectId> listNegTestIds=new ArrayList<ObjectId>();

		DBObject query1,query2,query3;

		query1= QueryBuilder.start().and(
				QueryBuilder.start("eventType").is(strEventType).get(),
				QueryBuilder.start("inSet").is("userPosSet").get()
				).get();
		query2= QueryBuilder.start().and(
				QueryBuilder.start("eventType").is(strEventType).get(),
				QueryBuilder.start("inSet").is("officialPosSet").get()
				).get();

		query3= QueryBuilder.start().and(
				QueryBuilder.start("eventType").is(strEventType).get(),
				QueryBuilder.start("inSet").is("negSet").get()
				).get();
		String strFilename="";
		for (int m = 0; m < 3; m++)
		{
			int tweetBuffer=30;
			DBCursor cursor;
			int size=0;
			double valPercentage=0;
			switch (m) {
			case 0:
				size= DBConnect.getCollEvent().find(query1).count();

				break;

			case 1:
				size= DBConnect.getCollEvent().find(query2).count();

				break;
			case 2:
				size= DBConnect.getCollEvent().find(query3).count();

				break;
			}

			int skip;
			DBObject o;
			BasicDBList tweet1;
			ObjectId eventid;
			DBConnect.queryCount=size;

			for(int j=0;j<Math.ceil(size/tweetBuffer)+1;j++)
			{
				skip=tweetBuffer*j;
				cursor=null;
				switch (m) {
				case 0:
					cursor = DBConnect.getCollEvent().find(query1).limit(tweetBuffer).skip(skip);
					break;
				case 1:
					cursor = DBConnect.getCollEvent().find(query2).limit(tweetBuffer).skip(skip);
					break;
				case 2:
					cursor = DBConnect.getCollEvent().find(query3).limit(tweetBuffer).skip(skip);
					break;
				}
				while(cursor.hasNext())
				{
					o=cursor.next();
					eventid=(ObjectId)o.get("_id");

					switch (m) {
					case 0:
						listAllUserPosIds.add(eventid);
						break;

					case 1:
						listAllOfficialPosIds.add(eventid);

						break;
					case 2:
						listAllNegIds.add(eventid);

						break;
					}
				}
			}

			//kac tane oldugunu biliyoruz onu fold sayisina bolerek bakalim test train kac olacak
			//size kac tane oldugu total userPostan
			valSetSize=size/valFold;
			//simdi random olarak valSetSize*(fold size-1) kadar eleman alip onlari train set olacak diye koyalim
			//bu kismi fold sayisi kadar tekrar edecegiz 

			//train setinde kac eleman olacak
			int valTrainCount=valSetSize*(valFold-1);
			//simdi valTrainCount kadar eleman oluncaya kadar listenin icinde alldaneleman sec
			for (int i = 0; i < valFold; i++)
			{
				FileWriter outFileTrain,outFileTest;
				PrintWriter outTrain=null,outTest=null;
				Random rand = new Random();
				int min=0, max=0;
				int valRand=0;
				List<ObjectId> listTemp;
				switch (m) {
				case 0:
					max=listAllUserPosIds.size()-1;
					outFileTrain = new FileWriter(strEventType+valFold+"_userpos_train"+i+".txt",false);
					outTrain = new PrintWriter(outFileTrain);
					outFileTest = new FileWriter(strEventType+valFold+"_userpos_test"+i+".txt",false);
					outTest = new PrintWriter(outFileTest);

					//simdi listede valTrainCount kadar eleman olana kadar ve listede olmayan numaralari cek
					while (listUserPosTrainIds.size()<valTrainCount)
					{
						valRand= rand.nextInt(max - min + 1) + min;
						ObjectId obj=listAllUserPosIds.get(valRand);
						if(!listUserPosTrainIds.contains(obj))
						{
							listUserPosTrainIds.add(obj);
							outTrain.write(obj.toString());
							outTrain.write("\n");

						}

					}

					//listobjeler listtrainidlerkesimi disindakiler testidler
					listTemp=new ArrayList<ObjectId>();
					listTemp.addAll(listUserPosTrainIds);
					//kesimleri tempte
					listTemp.retainAll(listAllUserPosIds); 
					for (ObjectId objectId : listAllUserPosIds)
					{
						if(!listTemp.contains(objectId))
						{
							listUserPosTestIds.add(objectId);
							outTest.write(objectId.toString());
							outTest.write("\n");


						}
					}
					listTemp.clear();
					listUserPosTestIds.clear();
					listUserPosTrainIds.clear();
					outTest.close();
					outTrain.close();
					break;

				case 1:
					max=listAllOfficialPosIds.size()-1;

					outFileTrain = new FileWriter(strEventType+valFold+"_officialpos_train"+i+".txt",false);
					outTrain = new PrintWriter(outFileTrain);
					outFileTest = new FileWriter(strEventType+valFold+"_officialpos_test"+i+".txt",false);
					outTest = new PrintWriter(outFileTest);

					//simdi listede valTrainCount kadar eleman olana kadar ve listede olmayan numaralari cek
					while (listOfficialPosTrainIds.size()<valTrainCount)
					{
						valRand= rand.nextInt(max - min + 1) + min;
						ObjectId obj=listAllOfficialPosIds.get(valRand);
						if(!listOfficialPosTrainIds.contains(obj))
						{
							listOfficialPosTrainIds.add(obj);
							outTrain.write(obj.toString());
							outTrain.write("\n");

						}

					}

					//listobjeler listtrainidlerkesimi disindakiler testidler
					listTemp=new ArrayList<ObjectId>();
					listTemp.addAll(listOfficialPosTrainIds);
					//kesimleri tempte
					listTemp.retainAll(listAllOfficialPosIds); 
					for (ObjectId objectId : listAllOfficialPosIds)
					{
						if(!listTemp.contains(objectId))
						{
							listOfficialPosTestIds.add(objectId);
							outTest.write(objectId.toString());
							outTest.write("\n");


						}
					}
					listTemp.clear();

					listOfficialPosTestIds.clear();
					listOfficialPosTrainIds.clear();
					outTest.close();
					outTrain.close();
					break;

				case 2:
					max=listAllNegIds.size()-1;

					outFileTrain = new FileWriter(strEventType+valFold+"_neg_train"+i+".txt",false);
					outTrain = new PrintWriter(outFileTrain);
					outFileTest = new FileWriter(strEventType+valFold+"_neg_test"+i+".txt",false);
					outTest = new PrintWriter(outFileTest);

					//simdi listede valTrainCount kadar eleman olana kadar ve listede olmayan numaralari cek
					while (listNegTrainIds.size()<valTrainCount)
					{
						valRand= rand.nextInt(max - min + 1) + min;
						ObjectId obj=listAllNegIds.get(valRand);
						if(!listNegTrainIds.contains(obj))
						{
							listNegTrainIds.add(obj);
							outTrain.write(obj.toString());
							outTrain.write("\n");

						}

					}

					//listobjeler listtrainidlerkesimi disindakiler testidler
					listTemp=new ArrayList<ObjectId>();
					listTemp.addAll(listNegTrainIds);
					//kesimleri tempte
					listTemp.retainAll(listAllNegIds); 
					for (ObjectId objectId : listAllNegIds)
					{
						if(!listTemp.contains(objectId))
						{
							listNegTestIds.add(objectId);
							outTest.write(objectId.toString());
							outTest.write("\n");


						}
					}
					listTemp.clear();

					listNegTestIds.clear();
					listNegTrainIds.clear();
					outTest.close();
					outTrain.close();
					break;
				}

			}

			listAllUserPosIds.clear();
			listAllOfficialPosIds.clear();
			listAllNegIds.clear();


		}


	}


}
