import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import net.sf.javaml.core.DenseInstance;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.util.Version;
import org.bson.types.ObjectId;

import weka.core.Debug.DBO;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

import cmu.arktweetnlp.Tagger;
import cmu.arktweetnlp.Tagger.TaggedToken;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;


//Tweetten cikarilan her ozellik burada toplanir 
//feature vector icin gerekli bilgileri toparlar

public class TextAnalytics
{
	//en son wordid kac generate edilmis
	private Integer valCurrentWordID; 
	private Tagger tagger;
	private String strModelFilename;
	private List<TaggedToken> taggedTokens;
	private TokenNameFinderModel modelLoc,modelTime;
	//kac tane ner detect edildi
	private Integer valDetectedNer;
	private StanfordCoreNLP pipeline;
	private StandardAnalyzer standardAnalyzer;
	//misal en fazla kac tane hashtag olabilir yada location tag cumlenin icinde
	//ona gore hepsinin sagi solundaki idler alinip feature vectore konulacak
	private Integer valMaxNumberOfSpecificFeatures=4; 
	//feature vectorde kullanilacak ozellikler
	private Integer valOrgTweetLength, valTweetLengthSemiStop;
	private Integer[] arrEmergencyWordPos;
	private Integer[] arrEmergencyWordID;
	private Integer[] arrHashtagWordPos;
	private Integer[] arrEmoticonWordPos;
	private Integer[] arrURLWordPos;
	private Integer[] arrMentionWordPos;
	private Integer[] arrPunctuationWordPos;
	private Integer[] arrLocationWordPos;
	private Integer[] valIsRT;
	private Integer[] arrTimeWordPos;
	private Integer[] valGPSAttached;
	//keywordlerin solunda saginda neler var onlari id si
	private Integer[] arrEmergencyWordLeft2ID;
	private Integer[] arrEmergencyWordLeft1ID;
	private Integer[] arrEmergencyWordRight2ID;
	private Integer[] arrEmergencyWordRight1ID;
	private Integer[] arrLocationWordLeft2ID;
	private Integer[] arrLocationWordLeft1ID;
	private Integer[] arrLocationWordRight2ID;
	private Integer[] arrLocationWordRight1ID;
	private Integer[] arrTimeWordLeft2ID;
	private Integer[] arrTimeWordLeft1ID;
	private Integer[] arrTimeWordRight2ID;
	private Integer[] arrTimeWordRight1ID;
	private Integer valCountStopWord; //cumlede kac stopword varmis
	private HashMap<String, Integer> hashMapWordID;
	private BasicDBObject doc;
	private Integer[] arrPos;
	private Integer[] arrHash;
	private List<String> stopWords;
	private CharArraySet stopSet;
	private Properties props;
	private List<String> lemmas;
	private StringBuilder sb;
	private String strLemmatized;
	private NameFinderME nameFinderLoc,nameFinderTime;

	public Integer getValCountStopWord() {
		return valCountStopWord;
	}

	public void setValCountStopWord(Integer valCountStopWord)
	{
		this.valCountStopWord = valCountStopWord;
	}

	public Integer[] getArrEmergencyWordID() {
		return arrEmergencyWordID;
	}

	public void setArrEmergencyWordID(Integer[] arrEmergencyWordID) {
		this.arrEmergencyWordID = arrEmergencyWordID;
	}
	public Integer getValOrgTweetLength() {
		return valOrgTweetLength;
	}

	public Integer getValTweetLengthSemiStop() {
		return valTweetLengthSemiStop;
	}

	public Integer[] getArrEmergencyWordPos() {
		return arrEmergencyWordPos;
	}

	public Integer[] getArrHashtagWordPos() {
		return arrHashtagWordPos;
	}

	public Integer[] getArrEmoticonWordPos() {
		return arrEmoticonWordPos;
	}

	public Integer[] getArrURLWordPos() {
		return arrURLWordPos;
	}

	public Integer[] getArrMentionWordPos() {
		return arrMentionWordPos;
	}

	public Integer[] getArrPunctuationWordPos() {
		return arrPunctuationWordPos;
	}

	public Integer[] getArrLocationWordPos() {
		return arrLocationWordPos;
	}

	public Integer[] getArrTimeWordPos() {
		return arrTimeWordPos;
	}

	public Integer[] getArrEmergencyWordLeft2ID() {
		return arrEmergencyWordLeft2ID;
	}

	public Integer[] getArrEmergencyWordLeft1ID() {
		return arrEmergencyWordLeft1ID;
	}

	public Integer[] getArrEmergencyWordRight2ID() {
		return arrEmergencyWordRight2ID;
	}

	public Integer[] getArrEmergencyWordRight1ID() {
		return arrEmergencyWordRight1ID;
	}

	public Integer[] getArrLocationWordLeft2ID() {
		return arrLocationWordLeft2ID;
	}

	public Integer[] getArrLocationWordLeft1ID() {
		return arrLocationWordLeft1ID;
	}

	public Integer[] getArrLocationWordRight2ID() {
		return arrLocationWordRight2ID;
	}

	public Integer[] getArrLocationWordRight1ID() {
		return arrLocationWordRight1ID;
	}

	public Integer[] getArrTimeWordLeft2ID() {
		return arrTimeWordLeft2ID;
	}

	public Integer[] getArrTimeWordLeft1ID() {
		return arrTimeWordLeft1ID;
	}

	public Integer[] getArrTimeWordRight2ID() {
		return arrTimeWordRight2ID;
	}

	public Integer[] getArrTimeWordRight1ID() {
		return arrTimeWordRight1ID;
	}


	public TextAnalytics() throws UnknownHostException, FileNotFoundException
	{
		tagger = new Tagger();

		strModelFilename = "/cmu/arktweetnlp/model.20120919";
		try
		{
			tagger.loadModel(strModelFilename);
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//hem lokasyon icin hemde time icin olan load edilmeli

		loadNER("./ner_models/tweetLOC.bin",0);
		loadNER("./ner_models/tweetLOC.bin",1);
		nameFinderLoc = new NameFinderME(modelLoc);
		nameFinderTime = new NameFinderME(modelTime);

		hashMapWordID=new HashMap<String,Integer>();

		DBConnect.connect();

		valCurrentWordID=getMaxWordIDFromDB();
		doc=new BasicDBObject();

		arrHash=new Integer[valMaxNumberOfSpecificFeatures];
		arrPos=new Integer[valMaxNumberOfSpecificFeatures];
		stopWords = new ArrayList<String>();
		stopSet = new CharArraySet(Version.LUCENE_36,stopWords.size(), false);
		props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma");

		pipeline = new StanfordCoreNLP(props);
		lemmas = new LinkedList<String>();
		strLemmatized="";


		try {
			// Open the file that is the first
			// command line parameter
			FileInputStream fstream = new FileInputStream("./stopwords.txt");
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null)
			{
				// Print the content on the console
				stopWords.add(strLine);
			}
			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		stopSet.addAll(stopWords);


		standardAnalyzer = new StandardAnalyzer(Version.LUCENE_36, stopSet);
		System.out.println("TEXT ANALYTICS HAZIR");
	}


	//butun wordidler fileda durur. ilk acilista bunlari rame alalim
	public void loadWordIdsFromFile() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("./wordIds.txt"));
		Integer cnt=0;
		try 
		{
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			String[] map;
			while (line != null)
			{
				//ilki en son wordID nosu
				if(cnt==0)
				{
					valCurrentWordID=Integer.parseInt(line);
					//System.out.println(valCurrentWordID);
				}
				else
				{
					//digerlerinin hepsi key:value
					line = br.readLine();
					if(line.equals("end"))
						break;
					map=line.split(" ");
					//	System.out.println(line);

					hashMapWordID.put(map[0], Integer.parseInt(map[1]));
				}
				cnt++;
			}
		}
		finally
		{
			br.close();
		}
	}
	//
	private Integer fetchWordIdFromDB(String token)
	{
		DBObject queryWordid=null;
		Integer val=0;
		queryWordid = QueryBuilder.start().and(
				QueryBuilder.start("wordid").is(token).get()
				).get();


		DBObject o=null;
		o = DBConnect.getCollWordId().findOne(queryWordid);

		if(o!=null)
		{
			val=(Integer)o.get("value");
		}
		else
		{
			val=valCurrentWordID++;
			InsertWordidToDB(token,val);
		}
		return val;

	}

	//file vs search time uzun dbye islenecek
	public void InsertWordidToDB(String token,Integer id) 
	{
		doc.clear();
		doc.put("wordid", token);
		doc.put("value", id);
		DBConnect.getCollWordId().insert(doc);
	}
	public void storeWordIdsToFile() throws IOException
	{

		File file = new File("./wordIds.txt");

		// if file doesnt exists, then create it
		if (!file.exists())
		{
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(valCurrentWordID.toString()+"\n");

		Set set = hashMapWordID.entrySet();
		Iterator i = set.iterator();
		// mapteki butun elemanlara bak ve gerekli degisiklikleri yap
		while(i.hasNext())
		{
			Map.Entry me = (Map.Entry)i.next();
			bw.write(me.getKey()+" "+me.getValue()+"\n");
		} 
		bw.write("end");
		bw.close();
	}
	//string e id dondurucez. eger mapte o id yoksa yeni uretilir dosyaya eklenir ve o dondurulur
	private Integer generateStringID(String token)
	{
		Integer id=hashMapWordID.get(token);

		if(id==null)
		{
			//numarasi yok 
			id=valCurrentWordID++;
			hashMapWordID.put(token, id);
		}
		return id;
	}


	//verilen textin sagindaki solundaki etc. word id lerini dondurur
	private Integer[] stringIDGivenPos(String text, Boolean left, Integer distance, Integer[] positions)
	{
		String[] txt=text.split(" ");
		int cnt=0;

		Boolean flag=false;
		int valRes;

		for (int i = 0; i < valMaxNumberOfSpecificFeatures; i++)
		{
			arrHash[i]=-1;
		}

		for (Integer pos : positions) 
		{
			if(pos<0)
				break;
			if(pos==null)
			{
				arrHash[cnt]=-1;
				cnt++;
			}
			else
			{
				if(left)
				{
					valRes=pos-distance;

					if(valRes>=0)
					{
						flag=true;
					}
				}
				else //right
				{
					valRes=pos+distance;
					if(valRes<txt.length)
					{
						flag=true;
					}
				}
				if(flag)
				{
					//hashcode double a yetmedi
					//arrHash[cnt]=generateStringID(txt[valRes]);
					arrHash[cnt]=fetchWordIdFromDB(txt[valRes]);
					cnt++;
				}
				flag=false;
			}
		}


		return arrHash;
	}

	//verilen array ile text i karsilastirarak. listenin icindeki kelimelerin verilen
	//textte hangi posizyonlarda bulundugunu dondurur
	private Integer[] stringPosOfListElements(String text, String[] elements)
	{
		int cnt=0,cnt1=0;
		String[] txt=text.split(" ");

		for (int i = 0; i < valMaxNumberOfSpecificFeatures; i++)
		{
			arrPos[i]=-1;
		}
		for (String str : txt)
		{
			for (String string : elements)
			{
				//sadece belirledigimiz kadar elemani eklesin
				if(str.equals(string)&&cnt1<valMaxNumberOfSpecificFeatures)
				{
					arrPos[cnt1]=cnt;
					cnt1++;
				}
			}
			cnt++;
		}
		return arrPos;
	}
	public void clear()
	{

		arrEmergencyWordPos=null;
		arrHashtagWordPos=null;
		arrLocationWordPos=null;
		arrMentionWordPos=null;
		arrEmoticonWordPos=null;
		arrURLWordPos=null;
		arrPunctuationWordPos=null;

		//solunda saginda ne var ise o kelimelerin IDleri

		arrEmergencyWordLeft1ID=null;
		arrEmergencyWordLeft2ID=null;
		arrEmergencyWordRight1ID=null;
		arrEmergencyWordRight2ID=null;

		arrLocationWordLeft1ID=null;
		arrLocationWordLeft2ID=null;
		arrLocationWordRight1ID=null;
		arrLocationWordRight2ID=null;

		arrEmergencyWordID=null;
		//		System.gc();
	}
	public List<Double> prepareTextAnalytics(String tweetText) throws IOException
	{
		//her ozelligi koy
		List<Double> listFeatures=new ArrayList<Double>();

		String strLocationTaggedText=combineTweetTextWithLocationAndTag(tweetText,detectLocationEntitiesFromText(tweetText));
		String taggedText=tagTweetTextReturnAsString(strLocationTaggedText);

		//TIME TAGLEMIYORUM
		//		String strTimeTaggedText=combineTweetTextWithTimeAndTag(taggedText,detectTimeEntitiesFromText(taggedText));
		//		String taggedText1=tagTweetTextReturnAsString(strTimeTaggedText);


		String filteredText=removeStopWords(taggedText);
		String lemmatizeText=textLemmatize(filteredText);
		lemmatizeText=detectNumbersInText(lemmatizeText);
		System.out.println("ORIGINAL: "+tweetText);

		System.out.println("LEMMATIZED FILTERED: "+lemmatizeText);
		valOrgTweetLength=tweetText.split(" ").length;
		listFeatures.add(valOrgTweetLength.doubleValue());
		valCountStopWord=valOrgTweetLength-removeStopWords(tweetText).split(" ").length;
		listFeatures.add(valCountStopWord.doubleValue());

		valTweetLengthSemiStop=filteredText.split(" ").length;
		listFeatures.add(valTweetLengthSemiStop.doubleValue());

		//tweetin icinde birden fazla event word olabilir. once verilen event wordler cumlede varmi. varsa pozisyonlari
		//ben 4 oldugunu varsayiyorum
		arrEmergencyWordPos=stringPosOfListElements(lemmatizeText, (String[])Main.filterWords.toArray()).clone();
		for (Integer val : arrEmergencyWordPos) {
			listFeatures.add(val.doubleValue());
		}
		for(int i=0;i<4-arrEmergencyWordPos.length;i++)
			listFeatures.add(Double.valueOf(-1));

//		arrHashtagWordPos=stringPosOfListElements(lemmatizeText, new String[]{"hashtag"}).clone();
//		for (Integer val : arrHashtagWordPos) {
//			listFeatures.add(val.doubleValue());
//		}
//		for(int i=0;i<4-arrHashtagWordPos.length;i++)
//			listFeatures.add(Double.valueOf(-1));

		arrLocationWordPos=stringPosOfListElements(lemmatizeText, new String[]{"location"}).clone();
		for (Integer val : arrLocationWordPos) {
			listFeatures.add(val.doubleValue());
		}
		for(int i=0;i<4-arrLocationWordPos.length;i++)
			listFeatures.add(Double.valueOf(-1));

//		arrMentionWordPos=stringPosOfListElements(lemmatizeText, new String[]{"mention"}).clone();
//		for (Integer val : arrMentionWordPos) {
//			listFeatures.add(val.doubleValue());
//		}
//		for(int i=0;i<4-arrMentionWordPos.length;i++)
//			listFeatures.add(Double.valueOf(-1));

//		arrEmoticonWordPos=stringPosOfListElements(lemmatizeText, new String[]{"emoticon"}).clone();
//		for (Integer val : arrEmoticonWordPos) {
//			listFeatures.add(val.doubleValue());
//		}
//		for(int i=0;i<4-arrEmoticonWordPos.length;i++)
//			listFeatures.add(Double.valueOf(-1));

//		arrURLWordPos=stringPosOfListElements(lemmatizeText, new String[]{"url"}).clone();
//		for (Integer val : arrURLWordPos) {
//			listFeatures.add(val.doubleValue());
//		}
//		for(int i=0;i<4-arrURLWordPos.length;i++)
//			listFeatures.add(Double.valueOf(-1));

//		arrPunctuationWordPos=stringPosOfListElements(lemmatizeText, new String[]{"punctuation"}).clone();
//		for (Integer val : arrPunctuationWordPos) {
//			listFeatures.add(val.doubleValue());
//		}
//		for(int i=0;i<4-arrPunctuationWordPos.length;i++)
//			listFeatures.add(Double.valueOf(-1));

		//solunda saginda ne var ise o kelimelerin IDleri

		arrEmergencyWordLeft1ID=stringIDGivenPos(lemmatizeText, true, 1, arrEmergencyWordPos).clone();
		for (Integer val : arrEmergencyWordLeft1ID) {
			listFeatures.add(val.doubleValue());
		}
		for(int i=0;i<4-arrEmergencyWordLeft1ID.length;i++)
			listFeatures.add(Double.valueOf(-1));

		arrEmergencyWordLeft2ID=stringIDGivenPos(lemmatizeText, true, 2, arrEmergencyWordPos).clone();
		for (Integer val : arrEmergencyWordLeft2ID) {
			listFeatures.add(val.doubleValue());
		}
		for(int i=0;i<4-arrEmergencyWordLeft2ID.length;i++)
			listFeatures.add(Double.valueOf(-1));

		arrEmergencyWordRight1ID=stringIDGivenPos(lemmatizeText, false, 1, arrEmergencyWordPos).clone();
		for (Integer val : arrEmergencyWordRight1ID) {
			listFeatures.add(val.doubleValue());
		}
		for(int i=0;i<4-arrEmergencyWordRight1ID.length;i++)
			listFeatures.add(Double.valueOf(-1));

		arrEmergencyWordRight2ID=stringIDGivenPos(lemmatizeText, false, 2, arrEmergencyWordPos).clone();
		for (Integer val : arrEmergencyWordRight2ID) {
			listFeatures.add(val.doubleValue());
		}
		for(int i=0;i<4-arrEmergencyWordRight2ID.length;i++)
			listFeatures.add(Double.valueOf(-1));

		arrLocationWordLeft1ID=stringIDGivenPos(lemmatizeText, true, 1, arrLocationWordPos).clone();
		for (Integer val : arrLocationWordLeft1ID) {
			listFeatures.add(val.doubleValue());
		}
		for(int i=0;i<4-arrLocationWordLeft1ID.length;i++)
			listFeatures.add(Double.valueOf(-1));

		arrLocationWordLeft2ID=stringIDGivenPos(lemmatizeText, true, 2, arrLocationWordPos).clone();
		for (Integer val : arrLocationWordLeft2ID) {
			listFeatures.add(val.doubleValue());
		}
		for(int i=0;i<4-arrLocationWordLeft2ID.length;i++)
			listFeatures.add(Double.valueOf(-1));

		arrLocationWordRight1ID=stringIDGivenPos(lemmatizeText, false, 1, arrLocationWordPos).clone();
		for (Integer val : arrLocationWordRight1ID) {
			listFeatures.add(val.doubleValue());
		}
		for(int i=0;i<4-arrLocationWordRight1ID.length;i++)
			listFeatures.add(Double.valueOf(-1));

		arrLocationWordRight2ID=stringIDGivenPos(lemmatizeText, false, 2, arrLocationWordPos).clone();
		for (Integer val : arrLocationWordRight2ID) {
			listFeatures.add(val.doubleValue());
		}
		for(int i=0;i<4-arrLocationWordRight2ID.length;i++)
			listFeatures.add(Double.valueOf(-1));

		arrEmergencyWordID=stringIDGivenPos(lemmatizeText, true, 0, arrEmergencyWordPos).clone();
		for (Integer val : arrEmergencyWordID) {
			listFeatures.add(val.doubleValue());
		}
		for(int i=0;i<4-arrEmergencyWordID.length;i++)
			listFeatures.add(Double.valueOf(-1));

		return listFeatures;
	}
	public static boolean isNumeric(String str)
	{
		return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}
	private String detectNumbersInText(String text)
	{
		String[] textArr=text.split(" ");
		String txt="";
		for (int i = 0; i < textArr.length; i++)
		{
			if(isNumeric(textArr[i]))
			{
				txt+="number"+" ";
			}
			else
			{
				txt+=textArr[i]+" ";
			}
		}
		return txt;

	}
	//hashtag, punctuation vs olarak taglanmis text ile bulunan locationlari cumlede taglar
	private String combineTweetTextWithLocationAndTag(String originalText,Span[] locationSpans)
	{

		String tweetText="";
		//out.println(originalText);

		String[] textArr=originalText.split(" ");

		for (String string : textArr) 
		{

			for (Span span : locationSpans)
			{

				textArr[span.getStart()]="location";
			}
		}


		for (int i = 0; i < textArr.length; i++)
		{
			tweetText+=textArr[i]+" ";
		}
		return tweetText;
	}
	private String combineTweetTextWithTimeAndTag(String originalText,Span[] timeSpans)
	{

		String tweetText="";
		//out.println(originalText);

		String[] textArr=originalText.split(" ");

		for (String string : textArr) 
		{

			for (Span span : timeSpans)
			{

				textArr[span.getStart()]="time";
			}
		}


		for (int i = 0; i < textArr.length; i++)
		{
			tweetText+=textArr[i]+" ";
		}
		return tweetText;
	}
	//texti verince nerleri detect etsin
	//http://opennlp.apache.org/documentation/1.5.2-incubating/manual/opennlp.html#tools.namefind.recognition
	private void loadNER(String modelFileName,Integer valType) throws FileNotFoundException
	{

		InputStream modelIn = new FileInputStream(modelFileName);

		try
		{
			if(valType==0)
				modelLoc = new TokenNameFinderModel(modelIn);
			else
				modelTime = new TokenNameFinderModel(modelIn);


		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			if (modelIn != null) {
				try
				{
					modelIn.close();
				}
				catch (IOException e) {
				}
			}
		}

	}


	//verilen textteki location entitiyleri sadece detect eder
	public Span[] detectLocationEntitiesFromText(String text) throws IOException
	{
		String sentence[] = text.split(" ");
		Span nameSpans[] = nameFinderLoc.find(sentence);
		//nameSpans icinde offset var kacinci kelimeden baslayip kacincida bittigini gosteren 0dan baslar
		for (Span span : nameSpans)
		{
			System.out.println(span.getStart());
		}
		valDetectedNer=nameSpans.length;
		nameFinderLoc.clearAdaptiveData();
		sentence=null;
		return nameSpans;
	}

	//verilen textteki location entitiyleri sadece detect eder
	public Span[] detectTimeEntitiesFromText(String text) throws IOException
	{
		String sentence[] = text.split(" ");
		Span nameSpans[] = nameFinderTime.find(sentence);
		//nameSpans icinde offset var kacinci kelimeden baslayip kacincida bittigini gosteren 0dan baslar
		for (Span span : nameSpans)
		{
			System.out.println(span.getStart());
		}
		valDetectedNer=nameSpans.length;
		nameFinderTime.clearAdaptiveData();
		sentence=null;
		return nameSpans;
	}

	private Integer getMaxWordIDFromDB()
	{
		DBObject queryWordid=null;
		Integer val=0;

		DBObject o;
		DBCursor cur = DBConnect.getCollWordId().find().sort(new BasicDBObject("value", -1)).limit(1);

		if (cur.hasNext())
		{
			o=cur.next();
			val=(Integer)o.get("value");
			val++;
		}
		else
		{
			val=0;
		}
		return val;


	}

	private String tagTweetTextReturnAsString(String text)
	{
		String taggedText="";
		taggedTokens=tagger.tokenizeAndTag(text);
		for (TaggedToken token : taggedTokens) {

			if (token.tag.equals("#")) {
				taggedText+="Hashtag ";


			} else if (token.tag.equals("U")) {
				taggedText+="URL ";


			} else if (token.tag.equals("E")) {
				taggedText+="Emoticon ";


			} else if (token.tag.equals(",")) {
				taggedText+="Punctuation ";
			} else if (token.tag.equals("@")) {
				taggedText+="Mention ";


			}
			else
			{
				taggedText+=token.token+ " ";
			}


		}
		taggedTokens.clear();
		taggedTokens=null;
		return taggedText;


	}
	public List<TaggedToken> tagTweetText(String text)
	{
		return tagger.tokenizeAndTag(text);

	}


	public String removeStopWords(String input) throws IOException
	{
		// tokenStream =new StopFilter(Version.LUCENE_36,tokenStream, stopSet);
		TokenStream tokenStream = standardAnalyzer.tokenStream("", new StringReader(input));
		sb = new StringBuilder();


		//		TokenStream tokenStream = new StandardTokenizer(Version.LUCENE_36,
		//				new StringReader(input));
		OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		String term="";
		while (tokenStream.incrementToken())
		{
			// int startOffset = offsetAttribute.startOffset();
			// int endOffset = offsetAttribute.endOffset();
			term = charTermAttribute.toString();
			if (sb.length() > 0) {
				sb.append(" ");
			}
			sb.append(term);
			term=null;
		}
		term= sb.toString();
		sb=null;
		//	System.out.println(input+"\n"+term);

		return term;
	}


	//kelimenin kokunu bul ve string olarka dondur
	private String textLemmatize(String documentText)
	{
		// create an empty Annotation just with the given text
		edu.stanford.nlp.pipeline.Annotation document = new edu.stanford.nlp.pipeline.Annotation(documentText);
		pipeline.annotate(document);
		// run all Annotators on this text
		strLemmatized="";
		// Iterate over all of the sentences found
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		for(CoreMap sentence: sentences) {
			// Iterate over all tokens in a sentence
			for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
				// Retrieve and add the lemma for each word into the
				// list of lemmas
				strLemmatized+=token.get(LemmaAnnotation.class)+" ";
				lemmas.add(token.get(LemmaAnnotation.class));
			}
		}
		lemmas.clear();
		sentences.clear();
		document=null;
		return strLemmatized;
	}

}
