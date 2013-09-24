package emotion.server.mediaWiki;

import java.util.ArrayList;
import java.util.List;

public class Section
{
   private List<Item> items = new ArrayList<Item>();

   /**
    * @param items the items to set
    */
   public void setItems(List<Item> items)
   {
      this.items = items;
   }

   /**
    * @return the items
    */
   public List<Item> getItems()
   {
      return items;
   }


}
