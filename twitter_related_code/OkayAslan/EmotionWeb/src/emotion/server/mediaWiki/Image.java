package emotion.server.mediaWiki;

public class Image
{
   private String source;
   private String width; 
   private String height;
   /**
    * @return the source
    */
   public String getSource()
   {
      return source;
   }
   /**
    * @return the width
    */
   public String getWidth()
   {
      return width;
   }
   /**
    * @return the height
    */
   public String getHeight()
   {
      return height;
   }
   /**
    * @param source the source to set
    */
   public void setSource(String source)
   {
      this.source = source;
   }
   /**
    * @param width the width to set
    */
   public void setWidth(String width)
   {
      this.width = width;
   }
   /**
    * @param height the height to set
    */
   public void setHeight(String height)
   {
      this.height = height;
   }
      
}
