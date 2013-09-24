package emotion.server.util;

public class SystemPrinter
{
   public static void printOperation(String string)
   {
      System.out.println("");
      System.out.println("....................................................................");
      System.out.println(string);
      System.out.println("....................................................................");
      System.out.println("");
   }
   
   public static void printException(String string)
   {
      System.out.println("***");
      System.out.println(string); 
      System.out.println("***");
   }   
   
}
