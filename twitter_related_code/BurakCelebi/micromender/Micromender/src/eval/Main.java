package eval;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sun.jersey.core.impl.provider.entity.XMLJAXBElementProvider.App;

import util.AppContextUtil;

public class Main {

	private static final String NOT_WORTH_RECOMMENDED_TERM_VARIATION = "NWR-TV";
	private static final String NOT_WORTH_RECOMMENDED_CONTENT = "NWR-C";
	private static final String NOT_WORTH_RECOMMENDED = "NWR";
	private static final String RECOMMENDED = "R";
	private static final String TITLE_PCT = "\\%";
	private static final String NL = "\n";
	private static final String SEP = " & ";
	private static Dao dao = AppContextUtil.getEvalDao();
	
	static DecimalFormat df = new DecimalFormat("#.##");
	
	public static void main(String[] args) {
		String[] eval = {
				
		};
		
		// insertEvalDetail(eval);
		// updateEvalDetail(eval);
		
		//printTesterAges();
		//printTesterGender();
		//printTesterEdu();
		//printTesterStu();
		//printTesterProf();
		

		//printBroadCategories();
		// printTesterQueryCounts();

		//printEvalSummary();

		//printBroadCategoryResults();
		
		//printSpecificQueries();
		//printSpecificQueriesResult();
		
		//printIterImpact();
		
		
		//printQueryExpImpact();
		//printNoQueryExpansions();
		
		
		// printAppendixResultAll();
		
		//int searchId = 424, evalId = 197;
		
		//printAppendixTopRelConcepts(searchId);
		//printAppendixRelConcepts(searchId);
		
		// printAppendixResult(263);
	}


	private static void printAppendixRelConcepts(int searchId) {
		System.out.println( 
				dao.findRelatedConcepts(searchId)
		);
	}

	private static void printAppendixTopRelConcepts(int searchId) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("tag", "tag");
		map.put("weight", "weight");
		
		List<Map<String, Object>> queryTabularRecords = dao.findTopRelatedConcepts(searchId);
		
		System.out.println(
			new LatexTabular(
				"\\TopRelatedConcepts~for ``''", 
				"t:topRelConcepts-"+searchId, 
				queryTabularRecords, map, false, null
			)
		);
	}

	private static void printAppendixResultAll() {
		
		List<Integer> userIds = dao.findEvalUsers();
		
		for (Integer userId: userIds) {
			
			System.out.println("\\section{User " + userId + "}\n");
			
			Map<String, String> map = new LinkedHashMap<String, String>();
			
			map.put("User ID", "user_id");
			map.put("Query ID", "id");
			map.put("Query", "query");
			
			LatexTabular userQueriesTabular = new LatexTabular(
					"Queries of User " + userId, 
					"t:EvalUser-"+userId, 
					dao.findEvalUserQueries(userId), map, false, "h"
					);
			
			System.out.println(userQueriesTabular);
			
			for (Map<String, Object> rsMap : userQueriesTabular.getRecords()) {
					
				int queryId = (Integer)rsMap.get("id");
				
				System.out.println("\\begin{landscape}\n");
				// System.out.println("\\subsection{Query " + queryId + "}\n");
				printAppendixResult(queryId);
				System.out.println("\\end{landscape}\n\n");
			}
		}
	}


	private static void printAppendixResult(int evalUserQueryId) {
		
		Map<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("\\SN", "screen_name");
		map.put("\\Rec", "r");
		map.put("\\Ev", "eval");
		map.put("Score", "new_score");
		map.put("\\CBS", "score");
		
		map.put("\\Sos", "socialness_own_norm");
		map.put("\\Sor", "socialness_rts_norm");
		map.put("\\Fds", "feedness_own_norm");
		map.put("\\Fdr", "feedness_rts_norm");
		map.put("\\Hts", "hashtags_own_norm");
		map.put("\\Htr", "hashtags_rts_norm");
		map.put("\\Res", "rating_own_norm");
		map.put("\\Rer", "rating_rts_norm");
		map.put("\\Tvs", "term_variation_own_norm");
		map.put("\\Tvr", "term_variation_rts_norm");
		map.put("\\ReR", "retweet_ratio_new");
		
		List<Map<String, Object>> queryTabularRecords = dao.findUserQueryResult(evalUserQueryId);
		
		System.out.println(
			new LatexTabular(
				"Evaluation Result for " + evalUserQueryId, 
				"t:EvalResult-"+evalUserQueryId, 
				queryTabularRecords, map, false, "h"
			)
		);
	}


	private static void printNoQueryExpansions() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("Query", "query");
		map.put("Broad Category", "broad_category");
		
		System.out.println(new LatexTabular("Queries Could not Expanded", "t:NoQueryExpansion", dao.findNoQueryExpansions(), map, false));
		
	}


	private static void printSpecificQueriesResult() {
		 
		List<Map<String, Object>> specificQueries = dao.findSpecificQueries();
		
		List<Integer> evalQueryIdList = new ArrayList<Integer>(specificQueries.size());
		for (Map<String, Object> map : specificQueries) {
			evalQueryIdList.add((Integer)map.get("id"));
		}
		
		for (Integer integer : evalQueryIdList) {
			System.out.println(integer);
		}
		
		printEvalSummary(evalQueryIdList);
		
	}


	private static void printSpecificQueries() {

		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("Query", "query");
		map.put("Broad Category", "broad_category");
		map.put("Expansion Grade", "expansion_grade");
		
		System.out.println(new LatexTabular("Specific Queries", "t:specificQueries", dao.findSpecificQueries(), map, false));
		
	}


	private static void printBroadCategories() {
		
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("Broad Category", "broad_category");
		map.put("Count", "query_count");
		map.put(TITLE_PCT, "pct");
		
		System.out.println(new LatexTabular("Broad Categories", "t:broadCategories", dao.findBroadCategories(), map, true));
	}


	private static void printIterImpact() {
		
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("Iteration", "iter");
		map.put("Count", "count");
		
		System.out.println(new LatexTabular("Impact of Iteration", "t:iterImpact", dao.findIterationImpact(), map, false));
	}


	private static void printQueryExpImpact() {
		
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("QE Grade", "expansion_grade");
		map.put("S", "satisfied");
		map.put("PS", "partially_satisfied");
		map.put("NS", "not_satisfied");
		map.put("NA", "not_applicable");
		// map.put("Total", "total");
		
		System.out.println(new LatexTabular("Impact of Query Expansion", "t:queryExpansionImpact", dao.findQueryExpansionImpact(), map, false));
	}


	private static void printBroadCategoryResults() {
		
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("", "broad_category");
		map.put("R", "sum_rec");
		
		map.put("S", "sum_rec_satisfied");
		map.put("P S", "pct_rec_satisfied");
		
		map.put("PS", "sum_rec_partially_satisfied");
		map.put("P PS", "pct_rec_partially_satisfied");
		
		map.put("NS", "sum_rec_not_satisfied");
		map.put("P NS", "pct_rec_not_satisfied");
		
		map.put("N/A", "sum_rec_not_applicable");
		map.put("P N/A", "pct_rec_not_applicable");
		
		System.out.println(new LatexTabular("Broad Category Results", "t:broadCategoryResults", dao.findBroadCategoryResults(), map, true));
		
		System.out.println("\\textbf{Broad Category} & & $\\#$ & \\% & $\\#$ & \\% & $\\#$ & \\% & $\\#$ & \\%\n\\myLine");
	}

	private static void printTesterQueryCounts() {

		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("User ID", "user_id");
		map.put("Number of Queries", "query_count");
		map.put("Number of Query Categories", "query_category_count");

		System.out.println(new LatexTabular("Test Users Queries", "t:testUserQueries", dao.findTestUserQueryCounts(), map, true));
	}
	
	private static void printTesterAges() {

		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("Age Interval", "age_interval");
		map.put("Count", "count");
		map.put(TITLE_PCT, "pct");

		System.out.println(new LatexTabular("Test Users Ages", "t:testUsersAges", dao.findTestUserAges(), map, false));
	}
	
	private static void printTesterGender() {

		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("Gender", "gender");
		map.put("Count", "count");
		map.put(TITLE_PCT, "pct");
		
		System.out.println(new LatexTabular("Test Users Genders", "t:testUsersGender", dao.findTestUserGender(), map, false));
	}
	
	private static void printTesterEdu() {

		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("Education Level", "edu_level");
		map.put("Count", "count");
		map.put(TITLE_PCT, "pct");

		System.out.println(new LatexTabular("Test Users' Education Levels", "t:testUsersEdu", dao.findTestUserEdu(), map, false));
	}

	private static void printTesterStu() {

		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("Student Status", "is_student");
		map.put("Count", "count");
		map.put(TITLE_PCT, "pct");
		
		System.out.println(new LatexTabular("Test Users Students", "t:testUsersStu", dao.findTestUserIsStudent(), map, false));
	}

	private static void printTesterProf() {

		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("Profession", "profession");
		map.put("Count", "count");
		map.put(TITLE_PCT, "pct");
		

		System.out.println(new LatexTabular("Test Users Professions", "t:testUsersProf", dao.findTestUserProf(), map, true));
	}


	private static void updateEvalDetail(String[] eval) {

		for (String evalRow : eval) {
			
			String[] splitted = evalRow.split(",");
			
			int id = Integer.valueOf(splitted[0]);
			System.out.println("id=" + id);
			
			String broadCategory = splitted[1];
			dao.updateEvalDetail(id, broadCategory);
		}
	}


	private static void insertEvalDetail(String[] eval) {
		for (String evalRow : eval) {
			String[] splitted = evalRow.split(",");
			
			
			int id = Integer.valueOf(splitted[0]);
			System.out.println(splitted[1] + " - " + id);
			
			
			String types = splitted[2];
			String typeDetails = splitted[3];
			String expansionGrade = splitted[4];
			
			dao.insertEvalDetail(id, types, typeDetails, expansionGrade);

			//System.out.println(dao.findTopRelatedKeywords(id));
			//System.out.println("");
			
		}
	}
	
	
	public static void printEvalSummary() {
		printEvalSummary(null);
	}
	
	private static void printEvalSummary(List<Integer> evalQueryIdList) {

		Eval[] evals = {Eval.NOT_SATISFIED, Eval.NOT_APPLICABLE, Eval.PARTIALLY_SATISFIED, Eval.SATISFIED};
		NotRecommendedReason[] reasons = {NotRecommendedReason.ALL, NotRecommendedReason.CONTENT_BASED_SCORE, NotRecommendedReason.TERM_VARIATION};
		
		StringBuilder sb = new StringBuilder("& \\textbf{Total} & \\multicolumn{2}{|c|}{\\textbf{" + 
												RECOMMENDED + "}} & \\multicolumn{2}{|c|}{\\textbf{" +
												NOT_WORTH_RECOMMENDED + "}}" 
		/*
												NOT_WORTH_RECOMMENDED_CONTENT + "}} & \\multicolumn{2}{|c|}{\\textbf{" + 
												NOT_WORTH_RECOMMENDED_TERM_VARIATION  + "}}\n\\myLine\n"
												*/
		+ "\n\\myLine\n");
		/*
		for (Eval eval : evals) {
			
			sb.append(eval.toString()).append(", ").append(dao.count(eval));
			
			sb.append(", ").append( dao.countRecommended(eval) );
			
			for (NotRecommendedReason notRecommendedReason : reasons) {
				sb.append(", ").append( dao.countNotRecommended(eval,notRecommendedReason) );
			}
			
			sb.append("\n");
		}
		*/
		readResult(evalQueryIdList);
		
		sb.append("\\textbf{Evaluation} & & $\\#$ & \\% & $\\#$ & \\%\n\\myLine\n");
		
		sb.append("Satisfied").append(SEP)
		  .append(Result.satisfied).append(SEP)
		  .append(Result.satisfied_Rec_Total).append(SEP).append(divide(Result.satisfied_Rec_Total,Result.satisfied)).append(SEP)
		  .append(Result.satisfied_NotRec_Total).append(SEP).append(divide(Result.satisfied_NotRec_Total,Result.satisfied)).append(NL);
		  /*
		  .append(Result.satisfied_NotRec_CB).append(SEP).append(divide(Result.satisfied_NotRec_CB,Result.satisfied)).append(SEP)
		  .append(Result.satisfied_NotRec_TV).append(SEP).append(divide(Result.satisfied_NotRec_TV,Result.satisfied)).append(NL);
		  */
		sb.append("\\myLine\n");
		
		sb.append("Partially Satisfied").append(SEP)
		  .append(Result.partiallySatisfied).append(SEP)
		  .append(Result.partiallySatisfied_Rec_Total).append(SEP).append(divide(Result.partiallySatisfied_Rec_Total,Result.partiallySatisfied)).append(SEP)
		  .append(Result.partiallySatisfied_NotRec_Total).append(SEP).append(divide(Result.partiallySatisfied_NotRec_Total,Result.partiallySatisfied)).append(NL);
		  /*
		  .append(Result.partiallySatisfied_NotRec_CB).append(SEP).append(divide(Result.partiallySatisfied_NotRec_CB,Result.partiallySatisfied)).append(SEP)
		  .append(Result.partiallySatisfied_NotRec_TV).append(SEP).append(divide(Result.partiallySatisfied_NotRec_TV,Result.partiallySatisfied)).append(NL);
		  */
		sb.append("\\myLine\n");
		
		sb.append("Not Satisfied").append(SEP)
		  .append(Result.notSatisfied).append(SEP)
		  .append(Result.notSatisfied_Rec_Total).append(SEP).append(divide(Result.notSatisfied_Rec_Total,Result.notSatisfied)).append(SEP)
		  .append(Result.notSatisfied_NotRec_Total).append(SEP).append(divide(Result.notSatisfied_NotRec_Total,Result.notSatisfied)).append(NL);
		  /*
		  .append(Result.notSatisfied_NotRec_CB).append(SEP).append(divide(Result.notSatisfied_NotRec_CB,Result.notSatisfied)).append(SEP)
		  .append(Result.notSatisfied_NotRec_TV).append(SEP).append(divide(Result.notSatisfied_NotRec_TV,Result.notSatisfied)).append(NL);
		  */
		sb.append("\\myLine\n");
		
		sb.append("Not Applicable").append(SEP)
		  .append(Result.notApplicable).append(SEP)
		  .append(Result.notApplicable_Rec_Total).append(SEP).append(divide(Result.notApplicable_Rec_Total,Result.notApplicable)).append(SEP)
		  .append(Result.notApplicable_NotRec_Total).append(SEP).append(divide(Result.notApplicable_NotRec_Total,Result.notApplicable)).append(NL);
		  /*
		  .append(Result.notApplicable_NotRec_CB).append(SEP).append(divide(Result.notApplicable_NotRec_CB,Result.notApplicable)).append(SEP)
		  .append(Result.notApplicable_NotRec_TV).append(SEP).append(divide(Result.notApplicable_NotRec_TV,Result.notApplicable)).append(NL);
		  */
		sb.append("\\myLine\n");
		
		int totalEval = Result.satisfied + Result.partiallySatisfied + Result.notSatisfied + Result.notApplicable;
		int totalEvalRecommended = Result.satisfied_Rec_Total + Result.partiallySatisfied_Rec_Total + Result.notSatisfied_Rec_Total + Result.notApplicable_Rec_Total;
		int totalEvalNotWorthRecommending = Result.satisfied_NotRec_Total + Result.partiallySatisfied_NotRec_Total + Result.notSatisfied_NotRec_Total + Result.notApplicable_NotRec_Total;  
		
		sb.append("Total").append(SEP)
		 .append(totalEval).append(SEP)
		  .append(totalEvalRecommended).append(SEP)
		  	.append(divide(totalEvalRecommended, totalEval)).append(SEP)
		  .append(totalEvalNotWorthRecommending).append(SEP)
		  	.append(divide(totalEvalNotWorthRecommending, totalEval)).append(NL);
		
		
		System.out.println(sb.toString());
		
		sb = new StringBuilder("& \\textbf{S} & \\textbf{PS} & \\textbf{NS} & \\textbf{NA}\n");

		sb.append("Recommended").append(SEP)
		  .append(divide(Result.satisfied_Rec_Total,Result.totalRecommended())).append(SEP)
		  .append(divide(Result.partiallySatisfied_Rec_Total,Result.totalRecommended())).append(SEP)
		  .append(divide(Result.notSatisfied_Rec_Total,Result.totalRecommended())).append(SEP)
		  .append(divide(Result.notApplicable_Rec_Total,Result.totalRecommended())).append(NL);
		sb.append("\\myLine\n");
		
		sb.append("Not Worth Recommended").append(SEP)
		  .append(divide(Result.satisfied_NotRec_Total,Result.totalNotRecommended())).append(SEP)  
		  .append(divide(Result.partiallySatisfied_NotRec_Total,Result.totalNotRecommended())).append(SEP)
		  .append(divide(Result.notSatisfied_NotRec_Total,Result.totalNotRecommended())).append(SEP)
		  .append(divide(Result.notApplicable_NotRec_Total,Result.totalNotRecommended())).append(NL);
		sb.append("\\myLine\n");
		
		sb.append("Not Worth Recommended - Content").append(SEP)
		  .append(divide(Result.satisfied_NotRec_CB,Result.totalNotRecommendedCB())).append(SEP)
		  .append(divide(Result.partiallySatisfied_NotRec_CB,Result.totalNotRecommendedCB())).append(SEP)
		  .append(divide(Result.notSatisfied_NotRec_CB,Result.totalNotRecommendedCB())).append(SEP)
		  .append(divide(Result.notApplicable_NotRec_CB,Result.totalNotRecommendedCB())).append(NL);
		sb.append("\\myLine\n");
		
		sb.append("Not Worth Recommended - Term Variation").append(SEP)
		  .append(divide(Result.satisfied_NotRec_TV,Result.totalNotRecommendedTV())).append(SEP)
		  .append(divide(Result.partiallySatisfied_NotRec_TV,Result.totalNotRecommendedTV())).append(SEP)
		  .append(divide(Result.notSatisfied_NotRec_TV,Result.totalNotRecommendedTV())).append(SEP)
		  .append(divide(Result.notApplicable_NotRec_TV,Result.totalNotRecommendedTV())).append(NL);
		sb.append("\\myLine\n");
				
		System.out.println(sb.toString());
		
		/*
		System.out.println("recommended:");
		
		System.out.println("not satisfied: " + 
				divide(Result.notSatisfied_Rec_Total,Result.totalRecommended())
		);
		System.out.println("not applicable: " + 
				divide(Result.notApplicable_Rec_Total,Result.totalRecommended())
		);
		System.out.println("partially satisfied: " + 
				divide(Result.partiallySatisfied_Rec_Total,Result.totalRecommended())
		);
		System.out.println("satisfied: " + 
				divide(Result.satisfied_Rec_Total,Result.totalRecommended())
		);
		
	
		System.out.println("-----------------------------");
		
		System.out.println("not recommended:");
		
		System.out.println("not satisfied: " + 
				divide(Result.notSatisfied_NotRec_Total,Result.totalNotRecommended())
		);
		System.out.println("not applicable: " + 
				divide(Result.notApplicable_NotRec_Total,Result.totalNotRecommended())
		);
		System.out.println("partially satisfied: " + 
				divide(Result.partiallySatisfied_NotRec_Total,Result.totalNotRecommended())
		);
		System.out.println("satisfied: " + 
				divide(Result.satisfied_NotRec_Total,Result.totalNotRecommended())
		);
		
System.out.println("-----------------------------");
		
		System.out.println("not recommended - content:");
		
		System.out.println("not satisfied: " + 
				divide(Result.notSatisfied_NotRec_CB,Result.totalNotRecommendedCB())
		);
		System.out.println("not applicable: " + 
				divide(Result.notApplicable_NotRec_CB,Result.totalNotRecommendedCB())
		);
		System.out.println("partially satisfied: " + 
				divide(Result.partiallySatisfied_NotRec_CB,Result.totalNotRecommendedCB())
		);
		System.out.println("satisfied: " + 
				divide(Result.satisfied_NotRec_CB,Result.totalNotRecommendedCB())
		);
		
		System.out.println("-----------------------------");
		
		System.out.println("not recommended - tv:");
		
		System.out.println("not satisfied: " + 
				divide(Result.notSatisfied_NotRec_TV,Result.totalNotRecommendedTV())
		);
		System.out.println("not applicable: " + 
				divide(Result.notApplicable_NotRec_TV,Result.totalNotRecommendedTV())
		);
		System.out.println("partially satisfied: " + 
				divide(Result.partiallySatisfied_NotRec_TV,Result.totalNotRecommendedTV())
		);
		System.out.println("satisfied: " + 
				divide(Result.satisfied_NotRec_TV,Result.totalNotRecommendedTV())
		);
		
		*/
		
		
		/*
		for (int i = 0; i < Result.totalTable.length; i++) {
			
			for (int j = 0; i < Result.totalTable[i].length; j++) {
				
				totalTable[i]
				
			}
		}
		*/
		
		/*
		System.out.println( dao.countRecommended(Eval.ANY) );
		
		System.out.println( dao.count(Eval.NOSATISFIED) );
		
		System.out.println( dao.countRecommended(Eval.SATISFIED) );
		System.out.println( dao.countNotRecommended(Eval.SATISFIED, NotRecommendedReason.ALL) );
		System.out.println( dao.countNotRecommended(Eval.SATISFIED, NotRecommendedReason.TERM_VARIATION) );
		System.out.println( dao.countNotRecommended(Eval.SATISFIED, NotRecommendedReason.CONTENT_BASED_SCORE) );
		*/
			
	}

	public static String divide(double d1, double d2) {
		return d2==0 ? "0" : df.format(d1/d2);
	}
	
	private static void readResult(List<Integer> evalQueryIdList) {
		
		Result.notSatisfied = dao.count(Eval.NOT_SATISFIED, evalQueryIdList);
		Result.notApplicable = dao.count(Eval.NOT_APPLICABLE, evalQueryIdList);
		Result.partiallySatisfied = dao.count(Eval.PARTIALLY_SATISFIED, evalQueryIdList);
		Result.satisfied = dao.count(Eval.SATISFIED, evalQueryIdList);

		Result.notSatisfied_NotRec_CB = dao.countNotRecommended(Eval.NOT_SATISFIED, NotRecommendedReason.CONTENT_BASED_SCORE, evalQueryIdList);
		Result.notApplicable_NotRec_CB = dao.countNotRecommended(Eval.NOT_APPLICABLE, NotRecommendedReason.CONTENT_BASED_SCORE, evalQueryIdList);
		Result.partiallySatisfied_NotRec_CB = dao.countNotRecommended(Eval.PARTIALLY_SATISFIED, NotRecommendedReason.CONTENT_BASED_SCORE, evalQueryIdList);
		Result.satisfied_NotRec_CB = dao.countNotRecommended(Eval.SATISFIED, NotRecommendedReason.CONTENT_BASED_SCORE, evalQueryIdList);

		Result.notSatisfied_NotRec_TV = dao.countNotRecommended(Eval.NOT_SATISFIED, NotRecommendedReason.TERM_VARIATION, evalQueryIdList);
		Result.notApplicable_NotRec_TV = dao.countNotRecommended(Eval.NOT_APPLICABLE, NotRecommendedReason.TERM_VARIATION, evalQueryIdList);
		Result.partiallySatisfied_NotRec_TV = dao.countNotRecommended(Eval.PARTIALLY_SATISFIED, NotRecommendedReason.TERM_VARIATION, evalQueryIdList);
		Result.satisfied_NotRec_TV = dao.countNotRecommended(Eval.SATISFIED, NotRecommendedReason.TERM_VARIATION, evalQueryIdList);

		Result.notSatisfied_NotRec_Total = dao.countNotRecommended(Eval.NOT_SATISFIED, NotRecommendedReason.ALL, evalQueryIdList);
		Result.notApplicable_NotRec_Total = dao.countNotRecommended(Eval.NOT_APPLICABLE, NotRecommendedReason.ALL, evalQueryIdList);
		Result.partiallySatisfied_NotRec_Total = dao.countNotRecommended(Eval.PARTIALLY_SATISFIED, NotRecommendedReason.ALL, evalQueryIdList);
		Result.satisfied_NotRec_Total = dao.countNotRecommended(Eval.SATISFIED, NotRecommendedReason.ALL, evalQueryIdList);

		Result.notSatisfied_Rec_Total = dao.countRecommended(Eval.NOT_SATISFIED, evalQueryIdList);
		Result.notApplicable_Rec_Total = dao.countRecommended(Eval.NOT_APPLICABLE, evalQueryIdList);
		Result.partiallySatisfied_Rec_Total = dao.countRecommended(Eval.PARTIALLY_SATISFIED, evalQueryIdList);
		Result.satisfied_Rec_Total = dao.countRecommended(Eval.SATISFIED, evalQueryIdList);
	}
}
