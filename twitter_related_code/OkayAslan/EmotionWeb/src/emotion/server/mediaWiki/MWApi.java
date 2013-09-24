package emotion.server.mediaWiki;

public class MWApi
{
   private MWQuery mwQuery;
   private transient String queryCont;

   /**
    * @param mwQuery the mwQuery to set
    */
   public void setMwQuery(MWQuery mwQuery)
   {
      this.mwQuery = mwQuery;
   }

   /**
    * @return the mwQuery
    */
   public MWQuery getMwQuery()
   {
      return mwQuery;
   }

   /**
    * @param queryCont the queryCont to set
    */
   public void setQueryCont(String queryCont)
   {
      this.queryCont = queryCont;
   }

   /**
    * @return the queryCont
    */
   public String getQueryCont()
   {
      return queryCont;
   }
   
}  
