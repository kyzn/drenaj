import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;


//ayri bir threadde butun situationlari kontrol eder. main 1 olanlarda official arrived mi bakar.
// eger official arrived ise butun tweet idlerinden user name leri cikartir. o userlarin var olan puanlarini 1 arttirir.

public class Trust implements Runnable
{

	public Trust()
	{

	}
	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		
		while(true)
		{
			
			System.out.println("trussstt");
			//situationda main=1 ve officialArrived=1 olanlarin situationidlerini al
			DBObject query = new BasicDBObject();
			query = QueryBuilder.start().and(
					QueryBuilder.start("main").is("1").get(),
					QueryBuilder.start("officialArrived").is("1").get()
					).get();
			DBObject o;
			DBCursor cur = DBConnect.getCollSituation().find(query);
			Integer valSitId;

			while (cur.hasNext())
			{
				o=cur.next();
				valSitId=(Integer)o.get("situationid");
				//her situation id si ayni olan icin tweet idleri al
				DBObject query1 = new BasicDBObject();
				query1 = QueryBuilder.start().and(
						QueryBuilder.start("situationid").is(valSitId.intValue()).get()
						).get();
				DBObject o1;
				DBCursor cur1 = DBConnect.getCollSituation().find(query1);
				ObjectId objTweetid;

				while (cur1.hasNext())
				{
					o1=cur1.next();
					objTweetid=(ObjectId)o1.get("tweetid");

					//tweetidlere ait userName leri al

					DBObject query2 = new BasicDBObject();
					query2 = QueryBuilder.start().and(
							QueryBuilder.start("_id").is(objTweetid).get()
							).get();
					DBObject o2;
					BasicDBList dblistTweet;
					DBCursor cur2 = DBConnect.getCollEvent().find(query2);
					String strUsername="";
					if (cur2.hasNext())
					{
						o2=cur2.next();
						dblistTweet=(BasicDBList)o2.get("tweet");
						//System.out.println("trustUsername:"+objTweetid.toString());
						strUsername=((BasicDBObject)dblistTweet.get(0)).get("userName").toString();
						//trust tablosuna git bak user situation id icin ve tweetid si icin bu tabloya eklenmismi. adam olay iicn birkac mesaj atmis olabilir her mesaji eklensin
						//trust tablosunda score, sitid, ve tweetid var
						//score hep en son halini tutar
						DBObject query3 = new BasicDBObject();
						query3 = QueryBuilder.start().and(
								QueryBuilder.start("situationid").is(valSitId).get(),
								QueryBuilder.start("tweetid").is(objTweetid).get()

								).get();
						DBObject o3;
						DBCursor cur3 = DBConnect.getCollUserTrust().find(query3);
						//hic eleman yoksa yeni eklenecek bu trust

						if (!cur3.hasNext())
						{
							//trust tablosunda userName,score, sitid, ve tweetid oalacak
							//o situation ve tweetid icin adama puan verilemmis ver.
							//once adamin en son puanini cek butun
							DBObject query4 = new BasicDBObject();
							query4 = QueryBuilder.start().and(
									QueryBuilder.start("userName").is(strUsername).get(),
									QueryBuilder.start("situationid").is(valSitId).get()

									).get();
							DBCursor cur4 = DBConnect.getCollUserTrust().find(query4).sort(new BasicDBObject("score", -1)).limit(1);
							Integer valUserscore;
							if (cur4.hasNext())
							{
								o3=cur4.next();
								valUserscore=(Integer)o3.get("score");
								valUserscore++;
							}
							else
							{ 
								valUserscore=1;
							}
							//ekle dbye adama olay icin puan verilmemis
							DBConnect.insertToUserTrust(objTweetid, strUsername, valSitId, valUserscore);

						}	
						//olayin genel puanini guncelle main=1 olanda puan olacak
						//yani  situation id si tutanlarin scorelarini topla
						Integer valTotalScore=0;

						DBObject query5 = new BasicDBObject();
						query5 = QueryBuilder.start().and(
								QueryBuilder.start("situationid").is(valSitId.intValue()).get()
								).get();
						DBObject o4;
						DBCursor cur5= DBConnect.getCollUserTrust().find(query5);

						while (cur5.hasNext())
						{
							o4=cur5.next();
							valTotalScore+=(Integer)o4.get("score");
						}
						//System.out.println("total score:"+valTotalScore);
						//main olani ve sitid si olanda update et score u
						//ESKI SISTMDE
//						DBObject query6 = new BasicDBObject();
//						query6 = QueryBuilder.start().and(
//								QueryBuilder.start("_id").is((ObjectId)o.get("_id")).get(),
//								QueryBuilder.start("situationid").is(valSitId).get(),
//								QueryBuilder.start("main").is("1").get()
//								).get();
//						DBObject newObject =  DBConnect.getCollSituation().find(query6).toArray().get(0);
//						newObject.put("score",valTotalScore.intValue());
//						DBConnect.getCollSituation().findAndModify(query6, newObject);
					}			
				}

			}
			try {
				Thread.sleep(1000*30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
