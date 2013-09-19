package labeler;

import java.util.LinkedList;
import java.util.List;

import tokenizer.Token;

public class MultiLabeler implements Labeler {

	private List<Labeler> labelers;

	public MultiLabeler(List<Labeler> labelers) throws Exception {
		this.labelers = labelers;
		init();
	}

	@Override
	public void init() throws Exception {
		for (Labeler labeler : labelers) {
			labeler.init();
		}
	}

	@Override
	public List<Token> label(final List<Token> tokens) {

		List<Token> labeledTokens = new LinkedList<Token>();
		labeledTokens.addAll(tokens);

		for (Labeler labeler : labelers) {
			labeledTokens = labeler.label(labeledTokens);
		}
		return labeledTokens;
	}
}
