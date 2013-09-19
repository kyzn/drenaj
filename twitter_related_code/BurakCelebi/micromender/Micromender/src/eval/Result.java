package eval;

class Result {

	static int 
	  
	  notSatisfied,
	  notApplicable,
	  partiallySatisfied,
	  satisfied,

	  notSatisfied_NotRec_CB,
	  notApplicable_NotRec_CB,
	  partiallySatisfied_NotRec_CB,
	  satisfied_NotRec_CB,

	  notSatisfied_NotRec_TV,
	  notApplicable_NotRec_TV,
	  partiallySatisfied_NotRec_TV,
	  satisfied_NotRec_TV,

	  notSatisfied_NotRec_Total,
	  notApplicable_NotRec_Total,
	  partiallySatisfied_NotRec_Total,
	  satisfied_NotRec_Total,
	  
	  notSatisfied_Rec_Total,
	  notApplicable_Rec_Total,
	  partiallySatisfied_Rec_Total,
	  satisfied_Rec_Total
	  
	  ;
	
	static int totalRecommended() {
		return Result.notSatisfied_Rec_Total
	         + Result.notApplicable_Rec_Total
	         + Result.partiallySatisfied_Rec_Total
	         + Result.satisfied_Rec_Total;
	}
	
	static int totalNotRecommended() {
		return Result.notSatisfied_NotRec_Total
	         + Result.notApplicable_NotRec_Total
	         + Result.partiallySatisfied_NotRec_Total
	         + Result.satisfied_NotRec_Total;
	}
	
	static int totalNotRecommendedCB() {
		return Result.notSatisfied_NotRec_CB
	         + Result.notApplicable_NotRec_CB
	         + Result.partiallySatisfied_NotRec_CB
	         + Result.satisfied_NotRec_CB;
	}
	
	static int totalNotRecommendedTV() {
		return Result.notSatisfied_NotRec_TV
	         + Result.notApplicable_NotRec_TV
	         + Result.partiallySatisfied_NotRec_TV
	         + Result.satisfied_NotRec_TV;
	}
	
	/*
	public static int[] totalEval = {notSatisfied,notApplicable,partiallySatisfied,satisfied};
	
	public static int[] totalNotRecCb = {notSatisfied_NotRec_CB, notApplicable_NotRec_CB, partiallySatisfied_NotRec_CB, satisfied_NotRec_CB};
	
	public static int[] totalNotRecTv = {notSatisfied_NotRec_TV,notApplicable_NotRec_TV,partiallySatisfied_NotRec_TV,satisfied_NotRec_TV};
	
	public static int[] totalNotRecTotal = {notSatisfied_NotRec_Total,notApplicable_NotRec_Total,partiallySatisfied_NotRec_Total,satisfied_NotRec_Total};
	
	public static int[] totalRecTotal = {notSatisfied_Rec_Total,notApplicable_Rec_Total,partiallySatisfied_Rec_Total,satisfied_Rec_Total};
	
	public static int[][] totalTable = {totalEval,totalNotRecCb,totalNotRecTv,totalNotRecTotal,totalRecTotal};
	*/
}
