package emotion.server.mediaWiki;

import java.util.ArrayList;
import java.util.List;

public class MWSearch
{
   private List<MWTitle> mwTitles = new ArrayList<MWTitle>();

   /**
    * @param mwTitles the mwTitles to set
    */
   public void setMwTitles(List<MWTitle> mwTitles)
   {
      this.mwTitles = mwTitles;
   }

   /**
    * @return the mwTitles
    */
   public List<MWTitle> getMwTitles()
   {
      return mwTitles;
   }
}
