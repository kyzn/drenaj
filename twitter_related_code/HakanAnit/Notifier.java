import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.text.StrSubstitutor;
import org.bson.types.ObjectId;

import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.Device;
import javapns.notification.PushedNotification;
import javapns.notification.ResponsePacket;
import twitter4j.GeoLocation;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;


public class Notifier implements Runnable
{
	private DBCursor cursor,cursor1,cursor2,cursor3,cursor4,cursor5;
	private DBObject query,o,o1,o2,o3,o4,o5,o6,o7,o8,o9,o10;
	private Integer valNotifiyParameter=15*60; //olaydan kac sn sonra notify icin atilsin
	private Integer valSleepInterval=2*60; //sn 
	private Boolean boolDistribution=false; //developemnt suan
	private static final int DISTANCE_THRESHOLD = 1000; //1000m bu km cinsindne

	private double valUserScore,valUserScoreMaxForSituation,valMaxUserScore,valUserScoreTotalPerSituation;
	private double valNotificationThreshold=1; //artik bu contribution sayisi oldu 3 msaj topladiysa haber verecek
	private double valFalsePositiveRatioClassifier=0.1;
	private double valUserTrustForContributions=1.0;
	private double valFollowerCnt,valFollowingCnt,valUnitContributionPerEvent,valMaxUnitContributionPerEvent;
	private Integer valOfficialCntUser;


	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		while(true)
		{
			System.out.println("NOTIFIER RUNNING");
			//dbden situation main, notified not exists olaylari cek
			query=new BasicDBObject();
			query = QueryBuilder.start().and(
					QueryBuilder.start("main").is("1").get(),
					QueryBuilder.start("notified").exists(false).get()
					).get();

			cursor =DBConnect.getCollSituation().find(query).sort(new BasicDBObject("timestamp", -1)).limit(5);
			while (cursor.hasNext())
			{
				//olayin zamani +5dk>=simdiki zaman ise noification gonderilecek
				System.out.println("NOTIFY ettir");

				o=cursor.next();
				Integer valSittime=(Integer)o.get("timestamp");
				Integer valNow=Long.valueOf((System.currentTimeMillis()/1000)).intValue();
				System.out.println(valSittime+" VALSITTIME "+valNow+" "+valNotifiyParameter);
				//notify icin uygun bu olay. update et olayi
				if(valSittime+valNotifiyParameter>=valNow)
				{
					System.out.println("NOTIFIER uygun var");

					//bu entry yu update et notified=1 yap,
					DBObject query1 = new BasicDBObject();
					query1 = QueryBuilder.start().and(
							QueryBuilder.start("situationid").is((Integer)o.get("situationid")).get(),
							QueryBuilder.start("main").is("1").get()
							).get();
					//					DBObject newObject =  DBConnect.getCollSituation().find(query1).toArray().get(0);
					//					newObject.put("notified","1");
					//					DBConnect.getCollSituation().findAndModify(query1, newObject);
					//butun mobileUserdan kullanicilari cek. lat long al. olayin
					cursor1 =DBConnect.getCollMobileUser().find();
					List<String> listDeviceTokens=new ArrayList<String>();
					double valTemp=0,valTemp1=0;
					Boolean boolDeviceAdded;
					while (cursor1.hasNext())
					{
						boolDeviceAdded=false;
						//olayin TweetLat ve longu ile GPSlat longu hesaplanip karar verilecek
						o1=cursor1.next();
						//InterestCount var ise hepsinin lat long alinmali ve gerekirse adam listeye eklenmeli mesj icin

						String strGPSLat=(String)o1.get("GPSLat");
						String strGPSLong=(String)o1.get("GPSLong");
						//						System.out.println(strGPSLat+" "+strGPSLong);
						String strTweetLat=(String)o.get("TweetLat");
						String strTweetLong=(String)o.get("TweetLong");
						//						System.out.println(strTweetLat+" "+strTweetLong);

						GeoLocation geoTweet=new GeoLocation(Double.valueOf(strTweetLat).doubleValue(), 

								Double.valueOf(strTweetLong).doubleValue());
						GeoLocation geoMobile=new GeoLocation(Double.valueOf(strGPSLat).doubleValue(), 

								Double.valueOf(strGPSLong).doubleValue());

						valTemp=DistanceBetweenTwoGPS.distanceBetweenTwoGPS(geoTweet, geoMobile);
						System.out.println("hesap GPS notifier:"+valTemp);

						if(valTemp<=DISTANCE_THRESHOLD) 
						{
							//butun mobile devicelari al o lokasyona yakin

							listDeviceTokens.add((String)o1.get("DeviceToken"));
							boolDeviceAdded=true;
						}
						//adamin ilgi noktalarinda var mi. zaten eklenmisse gerek yok buraya girmesine ondan bool var
						if(o1.get("InterestCount")!=null&&!boolDeviceAdded)
						{

							String strInterestCount=(String)o1.get("InterestCount");
							for (Integer i = 0; i < Integer.parseInt(strInterestCount); i++)
							{
								String strInterestLat=(String)o1.get("InterestLat"+i.toString());
								String strInterestLong=(String)o1.get("InterestLong"+i.toString());
								GeoLocation geoInterest=new GeoLocation(Double.valueOf(strInterestLat).doubleValue(), Double.valueOf(strInterestLong).doubleValue());
								valTemp1=DistanceBetweenTwoGPS.distanceBetweenTwoGPS(geoTweet, geoInterest);
								//								

								System.out.println("interest lat "+strInterestLat+" inter long "+strInterestLong+" tlat "+strTweetLat+" tlong "+strTweetLong);

								//								

								System.out.println("hesap GPS notifier1:"+valTemp1);

								if(valTemp1<=DISTANCE_THRESHOLD) 
								{
									//butun mobile devicelari al o lokasyona yakin

									listDeviceTokens.add((String)o1.get("DeviceToken"));
									boolDeviceAdded=true;
									//1 tane eklesin yoksa devicetoken tekrari olur
									break; 
								}

							}
						}

					}
					//YENI sekilde Notifikasyon gonderilsin mi
					//once gerekli mesajlari alalim o situationa ait
					DBObject query4 = new BasicDBObject();
					query4 = QueryBuilder.start().and(
							QueryBuilder.start("situationid").is((Integer)o.get("situationid")).get()
							).get();
					cursor2 =DBConnect.getCollSituation().find(query4);
					DBObject query5 = new BasicDBObject();
					//olaya ait tweetlwlerin alabilirisn
					System.out.println("BURDA1"+(Integer)o.get("situationid"));
					valUserScoreTotalPerSituation=0.0;
					while (cursor2.hasNext())
					{
						o4=cursor2.next();
						//simdi tweetid fieldini al situation tablosundan
						ObjectId objTweetId=(ObjectId)o4.get("tweetid");
						//bu tweete ait olan kullanici ile ilgili gerekli bilgileri al
						System.out.println("tweetidBUMU "+objTweetId.toString());
						query5= QueryBuilder.start().and(
								QueryBuilder.start("_id").is(objTweetId).get()
								).get();
						cursor3 =DBConnect.getCollEvent().find(query5);
						//tweeti aldin herseyi alabilirsin hakkinda

						DBObject tweetobj = DBConnect.getCollEvent().findOne(query5);
						BasicDBList tweetlist;
						tweetlist=(BasicDBList)tweetobj.get("tweet");
						String strUsername="";
						if(!tweetlist.equals(null))
						{

							strUsername=(String)((BasicDBObject)tweetlist.get(0)).get("userName");

						}
						valOfficialCntUser=0;

						while (cursor3.hasNext())
						{
							o5=cursor3.next();
							//tweet ile ilgili herseyi alabilirsin
							valFollowerCnt=Double.valueOf((Integer)((BasicDBObject)tweetlist.get(0)).get("followerCount"));

							valFollowingCnt=Double.valueOf((Integer)((BasicDBObject)tweetlist.get(0)).get("followingCount"));
							//twwet id ye ait username i al 
							//userTrustta fieldi trustInfo=1 olan ve usernamei tweetid olani al
							//bunda officialCountUser varsa kacmis onu al yoksa update et gelmisse


							//situation icin official arrive etmis mi
							query1 = new BasicDBObject();
							query1 = QueryBuilder.start().and(
									QueryBuilder.start("situationid").is((Integer)o.get

											("situationid")).get(),
											QueryBuilder.start("main").is("1").get()
									).get();
							cursor5 =DBConnect.getCollSituation().find(query1);
							o7=cursor5.next();
							Integer valOfficialArrived=0;
							if(cursor5.count()==0||cursor5==null)
								valOfficialArrived=0;
							else
							{
								if(o7.get("officialArrived")!=null)
									valOfficialArrived=1;
								else 
									valOfficialArrived=0;
							}
							ObjectId objUserTrustId = null;

							DBObject query6 = new BasicDBObject();
							query6 = QueryBuilder.start().and(
									QueryBuilder.start("trustInfo").is(1).get(),
									QueryBuilder.start("userName").is(strUsername).get()
									).get();
							cursor4 =DBConnect.getCollUserTrust().find(query6);
							if(cursor4.count()==0||cursor4==null)
							{
								//yeni entry yarat
								//trust infosu su, usernamei bu, officialCntUseri bu diye
								BasicDBObject doc=new BasicDBObject();
								doc.put("trustInfo", 1);
								doc.put("userName",strUsername);	
								doc.put("officialCntUser",valOfficialArrived);
								//query1 de officialArrived mi bu event icin onu al
								valOfficialCntUser=valOfficialArrived;
								DBConnect.getCollUserTrust().insert(doc);
								objUserTrustId=(ObjectId)doc.get("_id");

							}
							else
							{
								while (cursor4.hasNext())
								{
									o6=cursor4.next();
									//official count useri varsa al arttir kac olacakca yoksa yeni yarat
									//simdi update etmek gerekli official countu almaliyiz once.
									//officialArrived kadar eklemeliyiz
									valOfficialCntUser=(Integer)o6.get("officialCntUser");
									valOfficialCntUser+=valOfficialArrived;
									objUserTrustId=(ObjectId)o6.get("_id");
								}
								//update et 
								BasicDBObject queryTemp = new BasicDBObject();
								queryTemp.put("_id",objUserTrustId);
								DBObject newObject2 =  DBConnect.getCollUserTrust().find(queryTemp).toArray().get(0);
								newObject2.put("officialCntUser",valOfficialCntUser);
								DBConnect.getCollUserTrust().findAndModify(queryTemp, newObject2);

							}

							//max kullanici puani 
							DBObject queryTemp = new BasicDBObject();
							queryTemp = QueryBuilder.start().and(
									QueryBuilder.start("maxUserScore").exists(true).get(),
									QueryBuilder.start("maxNotify").exists(true).get()
									).get();
							DBCursor cur = DBConnect.getCollUserTrust().find(queryTemp);
							if(cur!=null&&cur.count()!=0)
								o8=cur.next();
							if(cur.count()==0||cur==null)
							{
								valMaxUserScore=(double)0.0;
								BasicDBObject doc1=new BasicDBObject();
								doc1.put("maxUserScore", valMaxUserScore);
								doc1.put("maxNotify",(double)0.0);
								DBConnect.getCollUserTrust().insert(doc1);
							}
							else
							{

								if(o8.get("maxUserScore").getClass().equals(Integer.class))
									valMaxUserScore=(Integer)o8.get("maxUserScore");
								else
								{
									System.out.println("HATA NEYMIS:"+o8.get("maxUserScore")+o8.get("maxUserScore").getClass());
									valMaxUserScore=(Double)o8.get("maxUserScore");
								}




							}
							//							System.out.println("VAL NOTIFY followercnt:"+valFollowerCnt);
							//
							//							System.out.println("VAL NOTIFY folloiwing:"+valFollowingCnt);
							//							System.out.println("VAL NOTIFY official cnt user:"+valOfficialCntUser);
							//							System.out.println("VAL NOTIFY user trust for contributions:"+valUserTrustForContributions);
							//							System.out.println("VAL NOTIFY max user score:"+valMaxUserScore);

							double val1;
							if(valFollowingCnt==0)
								val1=valFollowerCnt;
							else
								val1=(double)(valFollowerCnt/valFollowingCnt);

							double val2=(double)(valOfficialCntUser);
							double val3=(double)((val1+val2)*valUserTrustForContributions);
							double val4;	
							if(val3>valMaxUserScore)
							{
								val4=val3;
							}
							else
							{
								val4=valMaxUserScore;
							}
							valUserScore=(double)(val3/val4);
							//eğer kullanıcı puanı elimizdeki maximumdan büyükse o valMaxUser olarak dbde update edilmeli değilse sorun yok
							if(val3>valMaxUserScore)
							{
								valMaxUserScore=val3;

								DBObject queryTemp1 = new BasicDBObject();
								queryTemp1 = QueryBuilder.start().and(
										QueryBuilder.start("maxUserScore").exists(true).get(),
										QueryBuilder.start("maxNotify").exists(true).get()
										).get();
								DBObject newObject2 =  DBConnect.getCollUserTrust().find(queryTemp1).toArray().get(0);
								newObject2.put("maxUserScore",val3);
								newObject2.put("maxNotify",(double)0.0);
								DBConnect.getCollUserTrust().findAndModify(queryTemp1, newObject2);

							}

							BasicDBObject queryTemp1 = new BasicDBObject();
							queryTemp1.put("_id",objUserTrustId);
							DBObject newObject2 =  DBConnect.getCollUserTrust().find(queryTemp1).toArray().get(0);
							newObject2.put("score",valUserScore);
							DBConnect.getCollUserTrust().findAndModify(queryTemp1, newObject2);
							valUserScoreTotalPerSituation+=valUserScore;
						}
					}
					//verilen olay icin main 1 olan situatioda score fieldini guncelle
					//valuserscoretotalper situation ile
					DBObject queryTempN = new BasicDBObject();
					queryTempN = QueryBuilder.start().and(
							QueryBuilder.start("main").is("1").get(),
							QueryBuilder.start("situationid").is((Integer)o.get("situationid")).get()
							).get();
					DBObject newObjectN =  DBConnect.getCollSituation().find(queryTempN).toArray().get(0);
					newObjectN.put("score",valUserScoreTotalPerSituation);
					DBConnect.getCollSituation().findAndModify(queryTempN, newObjectN);


					//olay icin t surede gelen mesaj sayisi
					valUnitContributionPerEvent=(double)cursor2.count();
					Double valNotify=0.0;
					System.out.println("CURSOR2 cnt"+cursor2.count());
					if(cursor2.count()>0)
					{
						//herhangi bir olay icin t surede gelen en cok mesaj sayisi
						DBObject queryTemp = new BasicDBObject();
						queryTemp = QueryBuilder.start().and(
								QueryBuilder.start("main").is("1").get(),
								QueryBuilder.start("notified").is("1").get()
								).get();

						DBCursor cur =DBConnect.getCollSituation().find(queryTemp).sort(new BasicDBObject("timestamp", 1));
						//butun bu olaylarin icinde gez ve max mesaj sayisina sahip olani bul
						Double valMaxScore=-1.0,valTemp2=0.0;
						Integer valMaxTweet=-1;

						while (cur.hasNext())
						{
							valTemp2=0.0;
							o9=cur.next();
							//situation idsini al
							Integer valSitid=(Integer)o9.get("situationid");
							queryTemp = QueryBuilder.start().and(
									QueryBuilder.start("situationid").is(valSitid).get()
									).get();

							DBCursor cur1 =DBConnect.getCollSituation().find(queryTemp);
							//kac tane olayoldugunu al
							if(cur1.count()>valMaxTweet)
								valMaxTweet=cur1.count();
							//simdi herolay icin kullanici puanlarini toplamaluyir
							queryTemp = QueryBuilder.start().and(
									QueryBuilder.start("situationid").is(valSitid).get(),
									QueryBuilder.start("main").is("1").get()
									).get();

							DBCursor cur2 =DBConnect.getCollSituation().find(queryTemp);
							o10=cur2.next();
							//kullanici puan toplami buluuuyor situation main=1de
							//							System.out.println("Sitid in here:"+valSitid);
							valTemp2=(Double)o10.get("score");
							//update den sonra hemen hazir olmayabiliyor HATA
							if(valTemp2==null)
							{
								valTemp2=0.0;

							}
							if(valTemp2>valMaxScore)
								valMaxScore=valTemp2;
						}
						valMaxUnitContributionPerEvent=(double)valMaxTweet;
						valMaxUserScore=valMaxScore;

						//						System.out.println("VAL NOTIFY followercnt:"+valFollowerCnt);
						//
						//						System.out.println("VAL NOTIFY folloiwing:"+valFollowingCnt);
						//						System.out.println("VAL NOTIFY official cnt user:"+valOfficialCntUser);
						//						System.out.println("VAL NOTIFY user trust for contributions:"+valUserTrustForContributions);
						//						System.out.println("VAL NOTIFY max user score:"+valMaxUserScore);
						//						System.out.println("VAL NOTIFY unit contr. per event:"+valUnitContributionPerEvent);
						//
						//						System.out.println("VAL NOTIFY max unit cont. per er event:"+valMaxUnitContributionPerEvent);
						//						System.out.println("VAL NOTIFY valUserScoreTotalPerSituation:"+valUserScoreTotalPerSituation);
						//						System.out.println("VAL NOTIFY valMaxUserScore:"+valMaxUserScore);
						//						System.out.println("VAL NOTIFY valFalsePositiveRatioClassifier:"+valFalsePositiveRatioClassifier);


						double val1=(double)(valUnitContributionPerEvent/valMaxUnitContributionPerEvent);
						double val2=(double)(valUserScoreTotalPerSituation/valMaxUserScore);
						double val3=(double)(val1+val2)*(double)(1-valFalsePositiveRatioClassifier);


						DBObject queryTemp2 = new BasicDBObject();
						queryTemp2 = QueryBuilder.start().and(
								QueryBuilder.start("maxUserScore").exists(true).get(),
								QueryBuilder.start("maxNotify").exists(true).get()
								).get();
						DBCursor cur2 = DBConnect.getCollUserTrust().find(queryTemp2);
						o8=cur2.next();
						double valMaxNotify=(Double)o8.get("maxNotify");
						if(val3>valMaxNotify)
						{
							valMaxNotify=val3;
							DBObject queryTemp1 = new BasicDBObject();
							queryTemp1 = QueryBuilder.start().and(
									QueryBuilder.start("maxUserScore").exists(true).get(),
									QueryBuilder.start("maxNotify").exists(true).get()
									).get();
							DBObject newObject2 =  DBConnect.getCollUserTrust().find(queryTemp1).toArray().get(0);

							newObject2.put("maxNotify",valMaxNotify);
							DBConnect.getCollUserTrust().findAndModify(queryTemp1, newObject2);

						}
						double val4=(double)(val3/valMaxNotify);
						valNotify=val4;

						System.out.println("VAL NOTIFY SCORE:"+valNotify);
					}
					DBObject queryTempN1 = new BasicDBObject();
					queryTempN1 = QueryBuilder.start().and(
							QueryBuilder.start("main").is("1").get(),
							QueryBuilder.start("situationid").is((Integer)o.get("situationid")).get()
							).get();
					DBObject newObjectN1 =  DBConnect.getCollSituation().find(queryTempN1).toArray().get(0);
					newObjectN1.put("notify",valNotify);
					DBConnect.getCollSituation().findAndModify(queryTempN1, newObjectN1);

					//bu kullaniciya Push not gonderilecek
					//situationlardan en buyuk score toplami olani al onu 100 e oranlicaz
					//main =1 olanda total score var. zaten main=1 olanin icinde donuyoruz
					//sadece score u al
					if(valUnitContributionPerEvent>=valNotificationThreshold)
					{
						System.out.println("GONDERME GIRDI");
						DBObject newObject =  DBConnect.getCollSituation().find(query1).toArray().get(0);
						newObject.put("notified","1");
						DBConnect.getCollSituation().findAndModify(query1, newObject);
						//mesaj gonderilme kismi
						//ESKI sekilde BASLA
						//						DBObject query2 = new BasicDBObject();
						//						query2 = QueryBuilder.start().and(
						//								QueryBuilder.start("main").is("1").get()
						//								).get();
						//						DBCursor cur = DBConnect.getCollSituation().find(query2).sort(new BasicDBObject("score", -1)).limit(1);
						//
						//						o2=cur.next();
						//						Integer valHighscore=(Integer)o2.get("score");
						//						Integer valCurrentSituationscore=(Integer)o.get("score");
						//
						//						//ussekite ikisi bos olabilir o yuzden sifir olsun exception halinde
						//						Double valPercentageConfidence=0.0;
						//						try
						{
							//							valPercentageConfidence=Double.valueOf((valCurrentSituationscore/valHighscore)*100);
							//
							//						} catch (Exception e) {
							//							// TODO: handle exception
							//						}
							//ESKI sekilde BITIS

							//	if(valNotify>valNotificationThreshold) //degistiriyorum sadece contribution sayisina bagli olacak
							if(!listDeviceTokens.isEmpty())
							{



								//push mesaji olustur ve hepsine yolla
								String strEventType=(String)o.get("eventType");
								String strTweetCountry=(String)o.get("TweetCountry");
								String strTweetCity=(String)o.get("TweetCity");
								String strTweetState=(String)o.get("TweetState");
								String strTweetTown=(String)o.get("TweetTown");
								String strTweetStreet=(String)o.get("TweetStreet");
								//Integer valTimestamp=(Integer)o.get("timestamp"); //simdiki zamani gonder


								//						System.out.println("TIMESTAPM"+expiry.toString());

								//dbyi update et notification icin

								//mobile userda olayi kaydedelim latestOlay bilgileri olarak . mobil uygulama bundan okicak

								//long epoch = Long.parseLong(valTimestamp.toString());
								long epoch=System.currentTimeMillis();
								//								Date expiry = new Date( epoch * 1000 );
								Date expiry = new Date( epoch  );

								//							System.out.println("TIMESTAPM"+expiry.toString());

								String strTimestamp= expiry.toString();


								Double valTemp3=(Double)(valNotify*100);
								String strMessage= expiry.toString()+" "+strEventType+"!"+" Str:"+strTweetStreet+" Town: "+strTweetTown+" State: "+strTweetState+" City: "+strTweetCity+" Country: "+strTweetCountry;
								//						System.out.println("giden mesaj"+strMessage);
								File fileKey;
								if(!boolDistribution)
									fileKey=new File("keychains/development.p12");
								else
									fileKey=new File("keychains/production.p12");
								try
								{

									List<PushedNotification> notifications = Push.alert

											(strMessage,fileKey , "hakana", boolDistribution, listDeviceTokens);
									System.out.println("notificatsyon gonderildimi kacina "+listDeviceTokens.get(0).toString());
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



								//butun device tokenlari gez ve latest confidence update et
								for (String strToken : listDeviceTokens)
								{

									//bu device token a ait olanin id yi al

									DBObject query3 = new BasicDBObject();
									query3 = QueryBuilder.start().and(
											QueryBuilder.start("DeviceToken").is(strToken).get()
											).get();
									DBCursor cur2 = DBConnect.getCollMobileUser().find(query3);
									while(cur2.hasNext())
									{
										o3=cur2.next();
										BasicDBObject queryMobile = new BasicDBObject();
										queryMobile.put("_id", (ObjectId)o3.get("_id"));
										DBObject newObjectMobile =  DBConnect.getCollMobileUser().find(queryMobile).toArray().get(0);
										newObjectMobile.put("latestEventType",strEventType);
										newObjectMobile.put("latestStreet",strTweetStreet);
										newObjectMobile.put("latestTown",strTweetTown);
										newObjectMobile.put("latestState",strTweetState);
										newObjectMobile.put("latestCity",strTweetCity);
										newObjectMobile.put("latestCountry",strTweetCountry);
										newObjectMobile.put("latestTimestamp",strTimestamp);
										newObjectMobile.put("latestLat",(String)o.get("TweetLat"));
										newObjectMobile.put("latestLong",(String)o.get("TweetLong"));
										newObjectMobile.put("latestConfidence",valTemp3.toString());
										DBConnect.getCollMobileUser().findAndModify(queryMobile, newObjectMobile);

									}
								}
								System.out.println("GONDERMECIKTI");


								fileKey=null;
								//						query2=null;
								listDeviceTokens.clear();
							}

						}
						listDeviceTokens=null;

						query1=null;


					}

				}

				query=null;

			}
			try {
				Thread.sleep(valSleepInterval*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
