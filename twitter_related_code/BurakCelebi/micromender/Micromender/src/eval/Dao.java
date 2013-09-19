package eval;

import java.util.List;
import java.util.Map;

/**
 * Interface for Data Access Object.
 */
public interface Dao {

	public int count(Eval eval, List<Integer> evalQueryIdList);
	public int countRecommended(Eval eval, List<Integer> evalQueryIdList);
	public int countNotRecommended(Eval eval, NotRecommendedReason reason, List<Integer> evalQueryIdList);
	
	/*
	public int count(Eval eval);
	public int countRecommended(Eval eval);
	public int countNotRecommended(Eval eval, NotRecommendedReason reason);
	*/
	
	public void insertEvalDetail(int id, String types, String typeDetails, String expansionGrade);
	public String findTopRelatedKeywords(int evalId);
	public void updateEvalDetail(int id, String broadCategory);
	
	
	public List<Map<String, Object>> findTestUserAges();
	public List<Map<String, Object>> findTestUserGender();
	public List<Map<String, Object>> findTestUserEdu();
	public List<Map<String, Object>> findTestUserIsStudent();
	public List<Map<String, Object>> findTestUserProf();

	public List<Map<String, Object>> findTestUserQueryCounts();
	
	public List<Map<String, Object>> findBroadCategories();
	
	public List<Map<String, Object>> findBroadCategoryResults();
	
	
	public  List<Map<String, Object>>  findQueryExpansionImpact();
	
	public  List<Map<String, Object>>  findIterationImpact();
	public List<Map<String, Object>> findSpecificQueries();
	public List<Map<String, Object>> findNoQueryExpansions();
	
	public List<Map<String, Object>> findUserQueryResult(int evalUserQueryId);
	
	public List<Integer> findEvalUsers();
	
	public List<Map<String, Object>> findEvalUserQueries(int userId);
	
	public List<Map<String, Object>> findTopRelatedConcepts(int searchId);
	public String findRelatedConcepts(int searchId);
	
}
