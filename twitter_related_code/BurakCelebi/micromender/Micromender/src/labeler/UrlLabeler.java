package labeler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import tokenizer.Token;
import tokenizer.Label;
import util.Util;

public class UrlLabeler implements Labeler {

	private static final String URL_REGEX = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	private static Pattern pattern;
	
	@Override
	public void init() throws Exception {
		pattern = Pattern.compile(URL_REGEX, Pattern.CASE_INSENSITIVE);
	}

	@Override
	public List<Token> label(List<Token> tokens) {

		List<Token> labeledTokens = new ArrayList<Token>();

		for (Token token : tokens) {
			
			List<String> urls = Util.extractor.extractURLs(token.getValue());
			
			if(urls.size()>0) {
				token.setLabel(Label.URL);
				token.setValue(urls.get(0));
			}
			
			/*
			boolean isUrl = pattern.matcher(token.getValue()).matches();
			if(isUrl) {
				token.setLabel(Label.URL);
			}
			*/
			
			labeledTokens.add(token);
		}
		return labeledTokens;
	}

}
