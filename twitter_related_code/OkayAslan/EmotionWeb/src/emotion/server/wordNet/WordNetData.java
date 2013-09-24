package emotion.server.wordNet;

public class WordNetData
{
   private String word;
   private String plainForm;
   private boolean justNoun;
   private boolean noun;
   private boolean verb;
   private boolean adverb;
   private boolean adjective;
   
   /**
    * @return the word
    */
   public String getWord()
   {
      return word;
   }
   /**
    * @return the plainForm
    */
   public String getPlainForm()
   {
      return plainForm;
   }
   /**
    * @return the noun
    */
   public boolean isNoun()
   {
      return noun;
   }
   /**
    * @return the verb
    */
   public boolean isVerb()
   {
      return verb;
   }
   /**
    * @return the adverb
    */
   public boolean isAdverb()
   {
      return adverb;
   }
   /**
    * @return the adjective
    */
   public boolean isAdjective()
   {
      return adjective;
   }
   /**
    * @param word the word to set
    */
   public void setWord(String word)
   {
      this.word = word;
   }
   /**
    * @param plainForm the plainForm to set
    */
   public void setPlainForm(String plainForm)
   {
      this.plainForm = plainForm;
   }
   /**
    * @param noun the noun to set
    */
   public void setNoun(boolean noun)
   {
      this.noun = noun;
   }
   /**
    * @param verb the verb to set
    */
   public void setVerb(boolean verb)
   {
      this.verb = verb;
   }
   /**
    * @param adverb the adverb to set
    */
   public void setAdverb(boolean adverb)
   {
      this.adverb = adverb;
   }
   /**
    * @param adjective the adjective to set
    */
   public void setAdjective(boolean adjective)
   {
      this.adjective = adjective;
   }
   
   public String toString()
   {
      StringBuffer strBuff = new StringBuffer();
      strBuff.append("word : "+ getWord());
      strBuff.append("  ");
      strBuff.append("plainForm : "+ getPlainForm());
      strBuff.append(isNoun()?" noun ":"");
      strBuff.append(isVerb()?" verb ":"");
      strBuff.append(isAdjective()?" adjective ":"");
      strBuff.append(isAdverb()?" adverb ":"");
      
      return strBuff.toString();
   }
   /**
    * @param justNoun the justNoun to set
    */
   public void setJustNoun(boolean justNoun)
   {
      this.justNoun = justNoun;
   }
   /**
    * @return the justNoun
    */
   public boolean isJustNoun()
   {
      return justNoun;
   }
   
}
