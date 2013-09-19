package labeler;

import java.util.ArrayList;
import java.util.List;

import tokenizer.Token;
import tokenizer.Label;
import util.Util;

public class MentionLabeler implements Labeler {

	@Override
	public void init() throws Exception {
	}

	@Override
	public List<Token> label(List<Token> tokens) {

		List<Token> labeledTokens = new ArrayList<Token>();

		for (Token token : tokens) {
		
			List<String> mentions = Util.extractor.extractMentionedScreennames(token.getValue());
			
			if(mentions.size()>0) {
				token.setLabel(Label.MENTION);
				token.setValue(mentions.get(0));
			}
			
			/*
			if(token.getValue().startsWith("@")) {
				token.setLabel(Label.MENTION);
			}
			*/
			
			labeledTokens.add(token);
		}
		return labeledTokens;
	}

}
