package emotion.shared;

import java.io.Serializable;
import java.util.Date;

public class EmoAnalysisDO  implements Serializable
{
   /**
    * 
    */
   private static final long serialVersionUID = -564989952470253776L;
   
   public String keyword;
   public Date startDate;
   public Date endDate;
   public boolean semanticAnalysis;
   
   /**
    * @return the keyword
    */
   public String getKeyword()
   {
      return keyword;
   }
   /**
    * @param keyword the keyword to set
    */
   public void setKeyword(String keyword)
   {
      this.keyword = keyword;
   }
   /**
    * @return the startDate
    */
   public Date getStartDate()
   {
      return startDate;
   }
   /**
    * @param startDate the startDate to set
    */
   public void setStartDate(Date startDate)
   {
      this.startDate = startDate;
   }
   /**
    * @return the semanticAnalysis
    */
   public boolean isSemanticAnalysis()
   {
      return semanticAnalysis;
   }
   /**
    * @param semanticAnalysis the semanticAnalysis to set
    */
   public void setSemanticAnalysis(boolean semanticAnalysis)
   {
      this.semanticAnalysis = semanticAnalysis;
   }
   /**
    * @return the endDate
    */
   public Date getEndDate()
   {
      return endDate;
   }
   /**
    * @param endDate the endDate to set
    */
   public void setEndDate(Date endDate)
   {
      this.endDate = endDate;
   }
   
   
}
