package labeler;

import java.util.ArrayList;
import java.util.List;

import tokenizer.Label;
import tokenizer.Token;
import util.Util;

public class HashTagLabeler implements Labeler {

	@Override
	public void init() throws Exception {
	}

	@Override
	public List<Token> label(List<Token> tokens) {

		List<Token> labeledTokens = new ArrayList<Token>();
		
		for (Token token : tokens) {
			
			List<String> hashtags = Util.extractor.extractHashtags(token.getValue());
			
			if (hashtags.size() > 0) {
				token.setLabel(Label.HASHTAG);
				token.setValue(hashtags.get(0));
			}
			
			/*
			if(token.getValue().startsWith("#")) {
				token.setLabel(Label.HASHTAG);
			}
			*/
			
			labeledTokens.add(token);
		}
		return labeledTokens;
	}

}
