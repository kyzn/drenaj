package emotion.server.mediaWiki;

public class SearchSuggestion
{
   private String query;
   private Section section;

   /**
    * @return the query
    */
   public String getQuery()
   {
      return query;
   }
   /**
    * @return the section
    */
   public Section getSection()
   {
      return section;
   }
   /**
    * @param query
    *           the query to set
    */
   public void setQuery(String query)
   {
      this.query = query;
   }
   /**
    * @param section
    *           the section to set
    */
   public void setSection(Section section)
   {
      this.section = section;
   }

}
