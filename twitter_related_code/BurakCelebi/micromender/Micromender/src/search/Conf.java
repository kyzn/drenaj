package search;

/**
 * TODO 1. Bir tablodan yönetilebilir hale getirilmeli (mesela t_conf).
 * TODO 2. Her bir tavsiye istediği (t_search.id), mevcutta set edilmiş konfigürasyonla iliskili hale gelecek sekilde veritabanina yansitilabilmeli.   
 */
public class Conf {

	public static final int TWITTER_SEARCH_PAGE_SIZE = 10;
	
	public static final int EXTENDED_USERS_ITER = 5; 
	public static final int EXTENDED_USERS_REF_USERS = 5;
	public static final int EXTENDED_USERS_USERS_PER_ITER = 5;

	static final int USER_MAX_TWEETS = 50;
	
	public static final int MAX_TOP_COOCCURING_TAGS = 10;
	
	static double MIN_BEST_CONTENT_BASED_SCORE = 0.1;
	
	public static boolean LOG_ENRICHMENT = true; 

	/*
	private int maxUsersForInitialIter;
	
	private int twitterSearchPageSize;

	private int extendedUsersRefUsers;
	private int extendedUsersIteration;
	private int extendedUsersPerIteration;
	
	public int getMaxUsersForInitialIter() {
		return maxUsersForInitialIter;
	}

	public void setMaxUsersForInitialIter(int maxUsersForInitialIter) {
		this.maxUsersForInitialIter = maxUsersForInitialIter;
	}

	public int getTwitterSearchPageSize() {
		return twitterSearchPageSize;
	}

	public void setTwitterSearchPageSize(int twitterSearchPageSize) {
		this.twitterSearchPageSize = twitterSearchPageSize;
	}

	public int getExtendedUsersRefUsers() {
		return extendedUsersRefUsers;
	}

	public void setExtendedUsersRefUsers(int extendedUsersRefUsers) {
		this.extendedUsersRefUsers = extendedUsersRefUsers;
	}

	public int getExtendedUsersIteration() {
		return extendedUsersIteration;
	}

	public void setExtendedUsersIteration(int extendedUsersIteration) {
		this.extendedUsersIteration = extendedUsersIteration;
	}

	public int getExtendedUsersPerIteration() {
		return extendedUsersPerIteration;
	}

	public void setExtendedUsersPerIteration(int extendedUsersPerIteration) {
		this.extendedUsersPerIteration = extendedUsersPerIteration;
	}
	*/

	/**
	 * @deprecated
	 */
	static int MAX_USERS_INITIAL_ITER = 0; //0:not-limited
	
	/**
	 * Preventing construction
	 */
	private Conf() {
		throw new AssertionError();
	}
}