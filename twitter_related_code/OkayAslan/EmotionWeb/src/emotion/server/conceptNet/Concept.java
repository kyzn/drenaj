package emotion.server.conceptNet;

public class Concept
{
   private String text;
   private String canonical_name;
   private String resource_uri;
   private String id;
   
   /**
    * @return the text
    */
   public String getText()
   {
      return text;
   }
   /**
    * @return the canonical_name
    */
   public String getCanonical_name()
   {
      return canonical_name;
   }

   /**
    * @return the resource_uri
    */
   public String getResource_uri()
   {
      return resource_uri;
   }
   /**
    * @param text the text to set
    */
   public void setText(String text)
   {
      this.text = text;
   }
   /**
    * @param canonicalName the canonical_name to set
    */
   public void setCanonical_name(String canonicalName)
   {
      canonical_name = canonicalName;
   }

   /**
    * @param resourceUri the resource_uri to set
    */
   public void setResource_uri(String resourceUri)
   {
      resource_uri = resourceUri;
   }
   /**
    * @param id the id to set
    */
   public void setId(String id)
   {
      this.id = id;
   }
   /**
    * @return the id
    */
   public String getId()
   {
      return id;
   }
}
