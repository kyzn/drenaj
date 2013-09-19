package eval;

enum Eval {
	
	NOT_APPLICABLE(-2),
	NOT_SATISFIED(0),
	PARTIALLY_SATISFIED(1),
	SATISFIED(2),
	
	ANY(100);
	
	private int val;
	
	private Eval(int val) {
		this.val = val;
	}
	
	public int getVal(){
		return val;
	}

}
