package emotion.server.mediaWiki;

public class Item
{
   private String text;
   private String description;
   private String wikiUrl;
   private Image image;
   
   /**
    * @return the text
    */
   public String getText()
   {
      return text;
   }
   /**
    * @return the description
    */
   public String getDescription()
   {
      return description;
   }
   /**
    * @return the wikiUrl
    */
   public String getWikiUrl()
   {
      return wikiUrl;
   }
   /**
    * @param text the text to set
    */
   public void setText(String text)
   {
      this.text = text;
   }
   /**
    * @param description the description to set
    */
   public void setDescription(String description)
   {
      this.description = description;
   }
   /**
    * @param wikiUrl the wikiUrl to set
    */
   public void setWikiUrl(String wikiUrl)
   {
      this.wikiUrl = wikiUrl;
   }
   /**
    * @param image the image to set
    */
   public void setImage(Image image)
   {
      this.image = image;
   }
   /**
    * @return the image
    */
   public Image getImage()
   {
      return image;
   }
 }
