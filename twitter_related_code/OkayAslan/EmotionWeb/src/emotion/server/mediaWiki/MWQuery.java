package emotion.server.mediaWiki;

public class MWQuery
{
   private MWSearch mwSearch;
   private transient String searchinfo;

   /**
    * @param mwSearch the mwSearch to set
    */
   public void setMwSearch(MWSearch mwSearch)
   {
      this.mwSearch = mwSearch;
   }

   /**
    * @return the mwSearch
    */
   public MWSearch getMwSearch()
   {
      return mwSearch;
   }

   /**
    * @param searchinfo the searchinfo to set
    */
   public void setSearchinfo(String searchinfo)
   {
      this.searchinfo = searchinfo;
   }

   /**
    * @return the searchinfo
    */
   public String getSearchinfo()
   {
      return searchinfo;
   }
}
