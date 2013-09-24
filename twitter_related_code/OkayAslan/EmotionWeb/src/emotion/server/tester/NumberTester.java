package emotion.server.tester;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NumberTester
{
   private static NumberFormat numberFormatter = new DecimalFormat("#,###,###.##"); 

   /**
    * @param args
    */
   public static void main(String[] args)
   {
      double a = 48451211.63585;
      System.out.println(numberFormatter.format(a));
   }

}
