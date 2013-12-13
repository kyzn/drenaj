import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

import org.bson.types.ObjectId;

import twitter4j.GeoLocation;



//Tweetleri pass ederken threadler arasi tweet hakkinda bilgi paylasmak icin
//or: tweet uzunlugu, 1.kelime 2.kelime vs ne., gps datasi varmi, 
public class TweetStat
{
	private Integer tweetLengthFiltered;
	private Integer tweetLengthSemiFiltered;
	private Integer tweetLength; //kelime sayisi
	private boolean isGpsAttached,isRT; //gps bilgisi atanmismi
	private String originalTweet,userName,classifiedAs; //zamani, tweet
	
	private int tweetTime;
	private ArrayList<String> tokens,tokensFiltered;
	private GeoLocation geoLocation;
	private Queue<String> emoticonQueue,punctuationQueue,hashQueue;
	private String filteredTweet,outGPS,outAbb,outAddress,outCity,outTown,outCountry,outSarcasticLocation,
	outFakeLocation,inGPS, inAbb,inAddress,inCity,inTown,inCountry,inSarcasticLocation,inFakeLocation,organization,dueToAnotherEvent,timeEndInfo,timeStartInfo ;
	private ObjectId dbTweetID; //db de ki bu tweet nereye eklenmis
	
	
	public String getClassifiedAs() {
		return classifiedAs;
	}
	public void setClassifiedAs(String classifiedAs) {
		this.classifiedAs = classifiedAs;
	}
	public ObjectId getDbTweetID() {
		return dbTweetID;
	}
	public void setDbTweetID(ObjectId dbTweetID) {
		this.dbTweetID = dbTweetID;
	}
	public TweetStat(Integer _tweetLength, boolean _gpsAttached, int _tweetTime, String _originalTweet,
			boolean _isRT,GeoLocation _geoLocation, String _userName)
	{
		tweetLength=_tweetLength;
		isGpsAttached=_gpsAttached;
		tweetTime=_tweetTime;
		tokens=new ArrayList<String>();
		tokensFiltered=new ArrayList<String>();

		originalTweet=_originalTweet;
		geoLocation=_geoLocation;
		isRT=_isRT;
		userName=_userName;
		emoticonQueue=new LinkedList<String>();
		punctuationQueue=new LinkedList<String>();
		hashQueue=new LinkedList<String>();
	}
	public TweetStat()
	{
		
		tweetLength=0;
		isGpsAttached=false;
		isRT=false;
		originalTweet="";
		userName="";
		tweetTime=0;
		tokens=new ArrayList<String>();
		tokensFiltered=new ArrayList<String>();
		
		emoticonQueue=new LinkedList<String>();
		punctuationQueue=new LinkedList<String>();
		hashQueue=new LinkedList<String>();

		geoLocation=null;
	}
	
	
	public String getOutGPS() {
		return outGPS;
	}
	public void setOutGPS(String outGPS) {
		this.outGPS = outGPS;
	}
	public String getOutAbb() {
		return outAbb;
	}
	public void setOutAbb(String outAbb) {
		this.outAbb = outAbb;
	}
	public String getOutAddress() {
		return outAddress;
	}
	public void setOutAddress(String outAddress) {
		this.outAddress = outAddress;
	}
	public String getOutCity() {
		return outCity;
	}
	public void setOutCity(String outCity) {
		this.outCity = outCity;
	}
	public String getOutTown() {
		return outTown;
	}
	public void setOutTown(String outTown) {
		this.outTown = outTown;
	}
	public String getOutCountry() {
		return outCountry;
	}
	public void setOutCountry(String outCountry) {
		this.outCountry = outCountry;
	}
	public String getOutSarcasticLocation() {
		return outSarcasticLocation;
	}
	public void setOutSarcasticLocation(String outSarcasticLocation) {
		this.outSarcasticLocation = outSarcasticLocation;
	}
	public String getOutFakeLocation() {
		return outFakeLocation;
	}
	public void setOutFakeLocation(String outFakeLocation) {
		this.outFakeLocation = outFakeLocation;
	}
	public String getInGPS() {
		return inGPS;
	}
	public void setInGPS(String inGPS) {
		this.inGPS = inGPS;
	}
	public String getInAbb() {
		return inAbb;
	}
	public void setInAbb(String inAbb) {
		this.inAbb = inAbb;
	}
	public String getInAddress() {
		return inAddress;
	}
	public void setInAddress(String inAddress) {
		this.inAddress = inAddress;
	}
	public String getInCity() {
		return inCity;
	}
	public void setInCity(String inCity) {
		this.inCity = inCity;
	}
	public String getInTown() {
		return inTown;
	}
	public void setInTown(String inTown) {
		this.inTown = inTown;
	}
	public String getInCountry() {
		return inCountry;
	}
	public void setInCountry(String inCountry) {
		this.inCountry = inCountry;
	}
	public String getInSarcasticLocation() {
		return inSarcasticLocation;
	}
	public void setInSarcasticLocation(String inSarcasticLocation) {
		this.inSarcasticLocation = inSarcasticLocation;
	}
	public String getInFakeLocation() {
		return inFakeLocation;
	}
	public void setInFakeLocation(String inFakeLocation) {
		this.inFakeLocation = inFakeLocation;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getDueToAnotherEvent() {
		return dueToAnotherEvent;
	}
	public void setDueToAnotherEvent(String dueToAnotherEvent) {
		this.dueToAnotherEvent = dueToAnotherEvent;
	}
	public String getTimeEndInfo() {
		return timeEndInfo;
	}
	public void setTimeEndInfo(String timeEndInfo) {
		this.timeEndInfo = timeEndInfo;
	}
	public String getTimeStartInfo() {
		return timeStartInfo;
	}
	public void setTimeStartInfo(String timeStartInfo) {
		this.timeStartInfo = timeStartInfo;
	}


	
	public void clear()
	{
		inTown=null;
		tweetLength=null; //kelime sayisi
		
		originalTweet=null;
		userName=null; //zamani, tweet
		
	
		geoLocation=null;
		filteredTweet=null;
		outGPS=null;
		outAbb=null;
		outAddress=null;
		outCity=null;
		outTown=null;
		outCountry=null;
		outSarcasticLocation=null;
		outFakeLocation=null;
		inGPS=null;
		inAbb=null;
		inAddress=null;
		inCity=null;
		inTown=null;
		inCountry=null;
		inSarcasticLocation=null;
		inFakeLocation=null;
		organization=null;
	dueToAnotherEvent=null;
	timeEndInfo=null;
		timeStartInfo=null ;
		
		
		
		tokensFiltered.clear();
		tokens.clear();
		emoticonQueue.clear();
		punctuationQueue.clear();
		hashQueue.clear();
	}
	
	public String GetEmoticonQueuePoll() {
		return emoticonQueue.poll();
	}
	public void AddEmoticonQueue(String s) {
		emoticonQueue.add(s);
	}
	public void AddPunctuationQueue(String s) {
		
			punctuationQueue.add(s);
	}
	public String GetPunctuationQueuePoll() {
		return punctuationQueue.poll();
	}
	public void AddHashQueue(String s) {
		hashQueue.add(s);
	}
	public String GetHashQueuePoll() {
		return hashQueue.poll();
	}
	public String GetUserName() {
		return userName;
	}
	public void SetUserName(String userName) {
		this.userName = userName;
	}
	public GeoLocation GetGeoLocation() {
		return geoLocation;
	}
	public void SetGeoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
	}
	public String GetIsRT() {
		return (isRT?"1":"0");
	}
	public boolean IsRT() {
		return isRT;
	}
	public void SetRT(boolean isRT) {
		this.isRT = isRT;
	}
	public void AddToken(String _token)
	{
		tokens.add(_token);
	}
	public void AddStringToToken(String[] _token)
	{
		for (int i = 0; i < _token.length; i++) 
		{
			tokensFiltered.add(_token[i]);
		}
	}
	public String GetCombinedTokens()
	{
		String s="";
		for (int i = 0; i < tokens.size(); i++) {
			
			s=s+tokens.get(i)+" ";			
		}
		return s;
		
	}
	public ArrayList<String> GetTokensFiltered()
	{
		return tokensFiltered;
	}
	public ArrayList<String> GetTokens()
	{
		return tokens;
	}
	public Integer GetTweetLength() {
		return tweetLength;
	}
	public void SetTweetLength(int tweetLength) {
		this.tweetLength = tweetLength;
	}
	public String GetGpsAttached() {
		return (isGpsAttached?"1":"0");
	}
	public boolean IsGpsAttached() {
		return isGpsAttached;
	}
	public void SetGpsAttached(boolean gpsAttached) {
		this.isGpsAttached = gpsAttached;
	}
	public long GetTweetTime() {
		return tweetTime;
	}
	public void SetTweetTime(int tweetTime) {
		this.tweetTime = tweetTime;
	}
	public String GetOriginalTweet() {
		return originalTweet;
	}
	public void SetOriginalTweet(String originalTweet) {
		this.originalTweet = originalTweet;
	}
	public String GetFilteredTweet() {
		return filteredTweet;
	}
	public void SetFilteredTweet(String filteredTweet) {
		this.filteredTweet = filteredTweet;
	}
	public Integer GetFilteredTweetLength() {
		return tweetLengthFiltered;
	}
	public void SetFilteredTweetLength(int tweetLengthFiltered) {
		this.tweetLengthFiltered = tweetLengthFiltered;
	}
	
	public Integer GetSemiFilteredTweetLength() {
		return tweetLengthSemiFiltered;
	}
	public void SetSemiFilteredTweetLength(int tweetLengthSemiFiltered) {
		this.tweetLengthSemiFiltered = tweetLengthSemiFiltered;
	}

}
