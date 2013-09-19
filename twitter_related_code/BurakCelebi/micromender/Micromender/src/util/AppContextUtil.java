package util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import search.Dao;
import search.DaoImpl;
import search.SearchService;
import search.SearchServiceImpl;
import semantic.SemanticDao;
import semantic.SemanticDaoImpl;
import semantic.SemanticService;

public final class AppContextUtil {

	private static ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
	
	/**
	 * 
	 * @return
	 */
	public static SearchService getSearchService() {
		return new SearchServiceImpl(getDao());
	}
	
	/**
	 * 
	 * @return
	 */
	public static SemanticService getSemanticService() {
		return new SemanticService(getSemanticDao());
	}
	
	/**
	 * 
	 * @return
	 */
	public static Dao getDao() {
		return (DaoImpl)getBean("dao");
	}
	
	/**
	 * 
	 * @return
	 */
	public static SemanticDao getSemanticDao() {
		return (SemanticDaoImpl)getBean("semanticDao");
	}

	/**
	 * 
	 * @param beanId
	 * @return
	 */
	private static Object getBean(String beanId) {
		return applicationContext.getBean(beanId);
	}
	
	/**
	 * Preventing construction
	 */
	private AppContextUtil(){
		throw new AssertionError();
	}

	public static eval.Dao getEvalDao() {
		return (eval.DaoImpl)getBean("evalDao");
	}
}
