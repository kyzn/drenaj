package emotion.shared;

import emotion.server.mediaWiki.Item;
import emotion.server.mediaWiki.MWTitle;

public class EmoTweetResultData
{
   private Item item;
   private MWTitle mwTitle;
   private String title;
   private int numberOfOccurence;
   private int distinc;
   private float ratio;
   
   /**
    * @return the numberOfOccurence
    */
   public int getNumberOfOccurence()
   {
      return numberOfOccurence;
   }

   /**
    * @param numberOfOccurence the numberOfOccurence to set
    */
   public void setNumberOfOccurence(int numberOfOccurence)
   {
      this.numberOfOccurence = numberOfOccurence;
   }

   /**
    * @param item the item to set
    */
   public void setItem(Item item)
   {
      this.item = item;
   }

   /**
    * @return the item
    */
   public Item getItem()
   {
      return item;
   }

   /**
    * @param mwTitle the mwTitle to set
    */
   public void setMwTitle(MWTitle mwTitle)
   {
      this.mwTitle = mwTitle;
   }

   /**
    * @return the mwTitle
    */
   public MWTitle getMwTitle()
   {
      return mwTitle;
   }

   /**
    * @param ratio the ratio to set
    */
   public void setRatio(float ratio)
   {
      this.ratio = ratio;
   }

   /**
    * @return the ratio
    */
   public float getRatio()
   {
      return ratio;
   }

   /**
    * @param distinc the distinc to set
    */
   public void setDistinc(int distinc)
   {
      this.distinc = distinc;
   }

   /**
    * @return the distinc
    */
   public int getDistinc()
   {
      return distinc;
   }

   /**
    * @param title the title to set
    */
   public void setTitle(String title)
   {
      this.title = title;
   }

   /**
    * @return the title
    */
   public String getTitle()
   {
      return title;
   }
   
}
