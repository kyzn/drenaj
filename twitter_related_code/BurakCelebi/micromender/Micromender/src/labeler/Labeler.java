package labeler;

import java.util.List;

import tokenizer.Token;

public interface Labeler {

  public void init() throws Exception;
  
  public List<Token> label(List<Token> tokens);
}
