import java.io.BufferedReader;
import java.io.Console;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.print.attribute.standard.DateTimeAtCompleted;

import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.notification.PushedNotification;
import javapns.notification.ResponsePacket;


import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderCrossValidator;

import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.eval.FMeasure;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.standard.ClassicAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.NRTManager.TrackingIndexWriter;
import org.apache.lucene.search.NRTManagerReopenThread;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockFactory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;
import org.bson.NewBSONDecoder;
import org.bson.types.ObjectId;
import org.dbpedia.spotlight.exceptions.AnnotationException;
import org.json.JSONException;

import twitter4j.GeoLocation;
import twitter4j.TwitterException;

//Argumanlar: index dosyasi adi, eventType (db ve analiz kayit icin), twitter filter keyworler
public class Main 
{
	private static String indexName, eventType,analysisType,choice,official; //irrelevant,neutral,relevant
	private static Boolean flagIndex, flagAnnotationNeutral, flagAnnotationRelevant,flagAnnotationIrrelevant,flagReadFromDB,
	boolCompleteIntervalAnalysis,boolLiveFurtherAnalysis,boolOnlyUpdateDB,boolAnnotation,boolDetectSituation,boolTestRead;
	public static List<String> filterWords;
	private static Long annotationTime;
	private static Integer valClassifier=0;
	private static Classification classification;
	private static Double valClassificationThreshold;

	public static void main(String[] args) throws InterruptedException, IOException, AnnotationException, JSONException
	{

		System.out.println("MAX MEM:"+java.lang.Runtime.getRuntime().maxMemory()); 
		Console consol=System.console();
		//		//ne yapacaksin live stream okuma, all data reindex, detayli rapor icin calistirma, annotationlari index, db eventte update
		//
		choice=consol.readLine("Live stream okuma 1, all data reindex 2, detayli rapor icin calistirma 3, annotationlari index, sonuclar dbye 4, db eventte yeni ozellikler gelecekse 5, belli zaman icin annotation analiz 6, dosyadan tweet idleri oku ve dbye at 7, status1 olan tweettextleri dosyaya yaz 8, live streamden oku ve detect et 9, training dosyalarini olustur once 12 yi yapman lazim 10, testleri oku ve detect et 11, train test seti hazirlar 12, topN user, official pos, yada neg featurelar 13, cross validation 14, NER 5fold cross performance 15, Notification test gonder 16, GPS dosyasindan mesafe hesaplar yazar dosyaya 17, verdigin incident tipinin gpsi varsa texte yazar 18: ");
		String s;
		boolDetectSituation=false;


		if(choice.equals("1")) //live stream icin
		{

			indexName=consol.readLine("Index folder name (sadece analiz indexlenecekse girme): ");
			eventType=consol.readLine("EventType (her kosulda gireceksin): ");

			s=consol.readLine("Indexlendi olarak isaretlensin mi? (Ana bilgisayarda stream okuyorsan 1 yap): ");
			analysisType="";
			if(s.equals("1"))
				flagIndex=true;
			else
				flagIndex=false;
			s=consol.readLine("Live interval analiz mi? (1/0)(annotationda onemi yok): ");

			if(s.equals("1"))
				boolLiveFurtherAnalysis=true;
			else
				boolLiveFurtherAnalysis=false;
			System.out.println("Filter wordleri gir, arada bosluk birakarak eger live stream ise: ");

			filterWords = Arrays.asList(consol.readLine().split("\\s+"));
			flagReadFromDB=false;
			flagAnnotationIrrelevant=flagAnnotationNeutral=flagAnnotationRelevant=false;
			boolOnlyUpdateDB=false;
			boolCompleteIntervalAnalysis=false;
			boolAnnotation=false;
			boolTestRead=false;//sadece streamreader calismasi icin
			valClassifier=1;

		}
		else if(choice.equals("2")) //all data reindex
		{
			flagReadFromDB=true;
			indexName=consol.readLine("Index folder name: ");
			eventType=consol.readLine("EventType: ");

			s=consol.readLine("Indexlendi olarak isaretlensin mi? (Ana bilgisayarda stream okuyorsan 1 yap): ");
			analysisType="";
			if(s.equals("1"))
				flagIndex=true;
			else
				flagIndex=false;
			boolLiveFurtherAnalysis=false;
			flagAnnotationIrrelevant=flagAnnotationNeutral=flagAnnotationRelevant=false;
			boolOnlyUpdateDB=false;
			boolCompleteIntervalAnalysis=false;
			boolAnnotation=false;


		}
		else if(choice.equals("3"))//detayli rapor icin calisma
		{
			boolOnlyUpdateDB=false;
			indexName=consol.readLine("Index folder name: ");
			eventType=consol.readLine("EventType: ");
			boolCompleteIntervalAnalysis=true;
			boolAnnotation=false;

		}
		else if(choice.equals("4")) //annotationlari index sonuclar dbye
		{
			boolOnlyUpdateDB=false;
			boolCompleteIntervalAnalysis=true;
			boolAnnotation=true;
			indexName=consol.readLine("Index folder name: ");
			eventType=consol.readLine("EventType: ");
			s=consol.readLine("Annotation neutral mi? (1/0): ");
			if(s.equals("1"))
			{
				flagAnnotationNeutral=true;
				indexName="index_analysisNeutral_"+eventType;
				analysisType="2";//neutral
			}
			else
				flagAnnotationNeutral=false;

			s=consol.readLine("Annotation relevant mi? (1/0): ");

			if(s.equals("1"))
			{
				flagAnnotationRelevant=true;
				indexName="index_analysisRelevant_"+eventType;
				analysisType="1"; //relevant


			}
			else
				flagAnnotationRelevant=false;

			s=consol.readLine("Annotation irrelevant mi? (1/0): ");


			if(s.equals("1"))
			{
				flagAnnotationIrrelevant=true;
				indexName="index_analysisIrrelevant_"+eventType;
				analysisType="-1"; //irrelevant
			}
			else
				flagAnnotationIrrelevant=false;

			flagReadFromDB=false;
			s=consol.readLine("Sadece officiallermi: ");
			official=s;

		}
		else if(choice.equals("5")) //db eventle yeni ozellikler gelecekse
		{
			boolAnnotation=false;

			s=consol.readLine("Event DB'si sadece yeni ozellikler icin update mi edilecek? (1/0): ");

			if(s.equals("1"))
			{
				boolOnlyUpdateDB=true;
			}
			else
				boolOnlyUpdateDB=false;
		}
		else if(choice.equals("6")) //annotationlari index sonuclar dbye belli zamandakileri analiz edicek
		{
			boolOnlyUpdateDB=false;
			boolCompleteIntervalAnalysis=true;
			boolAnnotation=true;
			indexName=consol.readLine("Index folder name: ");
			eventType=consol.readLine("EventType: ");
			s=consol.readLine("Annotation neutral mi? (1/0): ");
			if(s.equals("1"))
			{
				flagAnnotationNeutral=true;
				indexName="index_analysisNeutral_"+eventType;
				analysisType="2";//neutral
			}
			else
				flagAnnotationNeutral=false;

			s=consol.readLine("Annotation relevant mi? (1/0): ");

			if(s.equals("1"))
			{
				flagAnnotationRelevant=true;
				indexName="index_analysisRelevant_"+eventType;
				analysisType="1"; //relevant


			}
			else
				flagAnnotationRelevant=false;

			s=consol.readLine("Annotation irrelevant mi? (1/0): ");


			if(s.equals("1"))
			{
				flagAnnotationIrrelevant=true;
				indexName="index_analysisIrrelevant_"+eventType;
				analysisType="-1"; //irrelevant
			}
			else
				flagAnnotationIrrelevant=false;

			flagReadFromDB=false;

			s=consol.readLine("Zaman gir 1dk oncesi 3dk sonrasi hesaplacanak: ");
			annotationTime=Long.parseLong(s);
			s=consol.readLine("Sadece officiallermi: ");
			official=s;

		}
		else if(choice.equals("7")) //dosyadan Tweetidleri oku ve db ye at manual olarak aticak
		{
			FetchTweetByID ft=new FetchTweetByID();
			try {
				ft.fetchTweetByIDAddToDB();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(choice.equals("8")) //status 1 olan tweetleri dosyaya yazior her event icin
		{
			TweetToFile tn=new TweetToFile();
		}
		else if(choice.equals("9")) //live streamden oku ve detect et
		{

			indexName=consol.readLine("Index folder name (sadece analiz indexlenecekse girme): ");
			eventType=consol.readLine("EventType (her kosulda gireceksin): ");

			s=consol.readLine("Indexlendi olarak isaretlensin mi? (Ana bilgisayarda stream okuyorsan 1 yap): ");
			analysisType="";
			if(s.equals("1"))
				flagIndex=true;
			else
				flagIndex=false;
			s=consol.readLine("Live interval analiz mi? (1/0)(annotationda onemi yok): ");

			if(s.equals("1"))
				boolLiveFurtherAnalysis=true;
			else
				boolLiveFurtherAnalysis=false;
			System.out.println("Filter wordleri gir, arada bosluk birakarak eger live stream ise: ");

			filterWords = Arrays.asList(consol.readLine().split("\\s+"));
			flagReadFromDB=false;
			flagAnnotationIrrelevant=flagAnnotationNeutral=flagAnnotationRelevant=false;
			boolOnlyUpdateDB=false;
			boolCompleteIntervalAnalysis=false;
			boolAnnotation=false;
			boolDetectSituation=true;
			s=consol.readLine("Classifier SVM-0 Naive Bayes-1 Self Op SVM-2:");
			boolTestRead=false;
			valClassifier=Integer.parseInt(s);


		}
		else if(choice.equals("10")) //sistemi verilen eventtype icin training dosyasini hazirlar
		{
			eventType=consol.readLine("EventType (her kosulda gireceksin): ");
			System.out.println("Filter wordleri gir, arada bosluk birakarak eger live stream ise: ");

			filterWords = Arrays.asList(consol.readLine().split("\\s+"));
			s=consol.readLine("Classifier SVM-0 Naive Bayes-1 Self Op SVM-2:");

			valClassifier=Integer.parseInt(s);
			Classification classification =new Classification(eventType,valClassifier);
			classification.prepareTraining(false);
		}
		else if(choice.equals("11")) //testteki leri oku ve detect ettir
		{
			indexName=consol.readLine("Index folder name: ");

			eventType=consol.readLine("EventType (her kosulda gireceksin): ");
			System.out.println("Filter wordleri gir, arada bosluk birakarak: ");

			filterWords = Arrays.asList(consol.readLine().split("\\s+"));
			analysisType="";
			flagIndex=true;
			boolLiveFurtherAnalysis=false;
			flagReadFromDB=false;
			flagAnnotationIrrelevant=flagAnnotationNeutral=flagAnnotationRelevant=false;
			boolOnlyUpdateDB=false;
			boolCompleteIntervalAnalysis=false;
			boolAnnotation=false;
			boolDetectSituation=true;
			boolTestRead=true;//sadece streamreader calismasi icin

			s=consol.readLine("Classifier SVM-0 Naive Bayes-1 Self Op SVM-2:");

			valClassifier=Integer.parseInt(s);
		}
		else if(choice.equals("12")) //train test set olsuturma
		{

			eventType=consol.readLine("EventType (her kosulda gireceksin): ");
			//TweetTestGenerator ttg=new TweetTestGenerator(eventType);
			classification=new Classification(eventType,0); //hangi classifier oldugu onemli degil
			s=consol.readLine("Fold no:");
			classification.PrepareTestAndTrainSetOnFile(Integer.parseInt(s));//kac fold istiyorsan
		}
		else if(choice.equals("13")) //top N user, official positive negative featurelari dosyaya yazdirir 
		{
			s=consol.readLine("Classifier SVM-0 Naive Bayes-1 Self Op SVM-2:");

			valClassifier=Integer.parseInt(s);
			eventType=consol.readLine("EventType (her kosulda gireceksin): ");
			System.out.println("Filter wordleri gir, arada bosluk birakarak: ");

			filterWords = Arrays.asList(consol.readLine().split("\\s+"));
			s=consol.readLine("User 0 Official 1 Negative 2:");
			Integer valUser0Official1=Integer.parseInt(s);
			//kac fold icinde classification classinda onu degistir
			classification=new Classification(eventType,valClassifier);
			classification.prepareTraining(false);


			classification.topNPositiveNegative(valClassifier,valUser0Official1);

		}
		else if(choice.equals("14")) //cross validation
		{

			s=consol.readLine("Classifier SVM-0 Naive Bayes-1 Self Op SVM-2:");

			valClassifier=Integer.parseInt(s);
			eventType=consol.readLine("EventType (her kosulda gireceksin): ");
			System.out.println("Filter wordleri gir, arada bosluk birakarak: ");

			filterWords = Arrays.asList(consol.readLine().split("\\s+"));

			classification=new Classification(eventType,valClassifier);
			s=consol.readLine("Fold no:");
			classification.StartCrossValidation(Integer.parseInt(s));

		}
		else if(choice.equals("15")) //NER cross validation
		{
			FileInputStream sampleDataIn = new FileInputStream("./ner_models/tweetLOC.train");

			ObjectStream<NameSample> sampleStream =new NameSampleDataStream(new PlainTextByLineStream(sampleDataIn.getChannel(), "UTF-8"));

			TokenNameFinderCrossValidator evaluator = new TokenNameFinderCrossValidator("en", 100, 15);
			evaluator.evaluate(sampleStream, 10);

			FMeasure result = evaluator.getFMeasure();

			System.out.println(result.toString());
		}
		else if(choice.equals("16"))
		{
			//mobile userda olayi kaydedelim latestOlay bilgileri olarak . mobil uygulama bundan okicak

			File fileKey;

			fileKey=new File("keychains/development.p12");
			System.out.println(System.currentTimeMillis());		try
			{

				List<PushedNotification> notifications = Push.alert("TEST",fileKey , "hakana", false, "324d4be9ceb8cf7228986cd8f9e6a77160e3ce4d7b3d0f5b647bcf25f6627032");
				for (PushedNotification notification : notifications) {
					if (notification.isSuccessful())
					{
						/* Apple accepted the notification and should deliver 

	it */  
						System.out.println("Push notification sent successfully to: " +notification.getDevice().getToken());
						/* Still need to query the Feedback Service regularly 

						 */  
					} else {
						System.out.println("Push GONDERMEDI");

						String invalidToken = notification.getDevice().getToken();
						/* Add code here to remove invalidToken from your 

	database */  

						/* Find out more about what the problem was */  
						Exception theProblem = notification.getException();
						theProblem.printStackTrace();

						/* If the problem was an error-response packet 

	returned by Apple, get it */  
						ResponsePacket theErrorResponse = notification.getResponse();
						if (theErrorResponse != null) {
							System.out.println(theErrorResponse.getMessage());
						}
					}
				}

			} catch (KeystoreException e) {
				/* A critical problem occurred while trying to use your keystore */  
				e.printStackTrace();

			} catch (CommunicationException e) {
				/* A critical communication error occurred while trying to contact 

	Apple servers */  
				e.printStackTrace();
			}

			/////NOTIFICATION TEST
		}
		else if(choice.equals("17"))
		{
			//////dosyadan lat lon okusun baska dosyaya aradaki mesafeyi bassin
			PrintWriter writer = new PrintWriter("gpsler_hesaplanan.txt", "UTF-8");

			BufferedReader br = new BufferedReader(new FileReader("gpsler.txt"));
			try {
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				while (line != null) {
					sb.append(line);
					//simdi once ; dan bol
					String delims = ";";
					String[] tokens = line.split(delims);
					//sonra ilk elemani , den bol ilkini olstur
					String delims1 = ",";
					String[] tokens0 = tokens[0].split(delims1);
					GeoLocation geoTweet=new GeoLocation(Double.valueOf(tokens0[0]).doubleValue(),Double.valueOf(tokens0[1]).doubleValue());
					//sonra ikinci elemani al olustur
					String[] tokens1 = tokens[1].split(delims1);
					GeoLocation geoMobile=new GeoLocation(Double.valueOf(tokens1[0]).doubleValue(), Double.valueOf(tokens1[1]).doubleValue());
					//sonra sonucu dosyaya yaz
					double 	valTemp=DistanceBetweenTwoGPS.distanceBetweenTwoGPS(geoTweet, geoMobile);
					System.out.println("hesap GPS notifier:"+valTemp);
					writer.println(valTemp);
					sb.append("\n");
					line = br.readLine();
				}
			} finally {
				br.close();
				writer.close();

			}
		}
		else if(choice.equals("18"))
		{
			eventType=consol.readLine("EventType (her kosulda gireceksin): ");
			String createdAt1=consol.readLine("created at 1:");
			String createdAt2=consol.readLine("created at 2:");

			eventType=consol.readLine("EventType (her kosulda gireceksin): ");
			ReadGPSFromEventWriteToFile rg=new ReadGPSFromEventWriteToFile(eventType,createdAt1,createdAt2);


		}

		//test durumunda asagidakileri ac		
		//								filterWords = Arrays.asList("quake","earthquake","temblor");
		//		filterWords = Arrays.asList("fire","smoke","burning");
		//
		//				indexName="test2112";
		//				eventType="earthquake";
		//		
		//		
		//				analysisType="";
		//				flagIndex=true;
		//				boolLiveFurtherAnalysis=false;
		//				flagReadFromDB=false;
		//				flagAnnotationIrrelevant=flagAnnotationNeutral=flagAnnotationRelevant=false;
		//				boolOnlyUpdateDB=false;
		//				boolCompleteIntervalAnalysis=false;
		//				boolAnnotation=false;
		//				boolDetectSituation=true;
		//				boolTestRead=false;//sadece streamreader calismasi icin
		//				valClassifier=1;

		//	

		//test olarak cokuyup calistirma icin asagiyida ac
		//		indexName="earthq5";
		//
		//		eventType="earthquake";
		//
		//		filterWords = Arrays.asList("quake","earthquake","temblor");
		//		analysisType="";
		//		flagIndex=true;
		//		boolLiveFurtherAnalysis=false;
		//		flagReadFromDB=false;
		//		flagAnnotationIrrelevant=flagAnnotationNeutral=flagAnnotationRelevant=false;
		//		boolOnlyUpdateDB=false;
		//		boolCompleteIntervalAnalysis=false;
		//		boolAnnotation=false;
		//		boolDetectSituation=true;
		//		boolTestRead=true;//sadece streamreader calismasi icin
		//		valClassifier=Integer.parseInt("1");

		////// training dosya hazirlatma icin sadece bu acik kalsin

		//				eventType="earthquake";
		//		
		//				filterWords = Arrays.asList("quake","earthquake","temblor");
		//		
		//				valClassifier=Integer.parseInt("1");
		//				Classification classification =new Classification(eventType,valClassifier);
		//				classification.prepareTraining(false);

		//		

		/////
		////crosss validation icin sadece bu acik kalsin


		//		valClassifier=Integer.parseInt("1");
		//		eventType="earthquake";
		//
		//		//		filterWords = Arrays.asList("fire","smoke","burning");
		//		//
		//		//		eventType="fire";
		//
		//		filterWords = Arrays.asList("quake","earthquake","temblor");
		//
		//
		//		classification=new Classification(eventType,valClassifier);
		//
		////		classification.PrepareTestAndTrainSetOnFile(10);//kac fold istiyorsan
		//
		//		classification.StartCrossValidation(10);


		////////

		//asagidaki hep acik kalacak yukariki console disi bazi olaylar disinda
		if(!choice.equals("10")&&!choice.equals("8")&&!choice.equals("7")&&!choice.equals("12")&&!choice.equals("13")&&!choice.equals("14")&&!choice.equals("15")&&!choice.equals("16")&&!choice.equals("17")&&!choice.equals("18"))
		{
			s=consol.readLine("Classification threshold:");
			new IndexManager(indexName,eventType,analysisType,flagIndex,flagAnnotationIrrelevant,flagAnnotationNeutral,flagAnnotationRelevant,flagReadFromDB,boolCompleteIntervalAnalysis,
					boolLiveFurtherAnalysis,boolOnlyUpdateDB,boolAnnotation,filterWords,annotationTime,official,boolDetectSituation,boolTestRead,valClassifier,Double.valueOf(s));
		}







	}

}
