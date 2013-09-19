package util;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Properties;

public class MicromenderConfigurator {
	
	private static final String PROPERTY_FILE = "micromender.properties";
	private static Properties property;
	
	public static final String OAUTH_CONSUMER_KEY        = "oauth.consumerKey",
                               OAUTH_CONSUMER_SECRET     = "oauth.consumerSecret",
                               OAUTH_ACCESS_TOKEN        = "oauth.accessToken",
                               OAUTH_ACCESS_TOKEN_SECRET = "oauth.accessTokenSecret",
                               USER                      = "user",
                               PASSWORD                  = "password",
                               PROXIES_CVS               = "proxies_cvs",
                               STOPWORDS = "stopwords";
	
	public static final String DELIMETER_COMMA = ",";
	
	public static String getProperty(String propertyName) {
		
		if(propertyName==null){
			throw new MissingResourceException("Cannot find property for null value", MicromenderConfigurator.class.getName(), propertyName);
		}
		
		String property = getPropertiesFile().getProperty(propertyName);
		if(property== null){
			throw new MissingResourceException("Can not find property: "+ propertyName, MicromenderConfigurator.class.getName(), propertyName);
		}
		return property.trim();
	}
	
	/**
	 * @param propertyName
	 * @param defaultValue dosyada @param propertyName bulunamaz ise bu de�eri doner.
	 * */
	public static String getProperty(String propertyName, String defaultValue) {
		
		if(propertyName==null){
			throw new MissingResourceException("Cannot find property for null value", MicromenderConfigurator.class.getName(), propertyName);
		}
		
		String property = getPropertiesFile().getProperty(propertyName);
		if(property== null){
			return defaultValue;
		}
		return property.trim();
	}

	/**
	 * 
	 * @param propertyName
	 * @param delimeterRegex
	 * @return
	 */
	public static String[] getPropertyWithDelimeterAsStringArray(String propertyName, String delimeterRegex) {
		
		String propValue = getProperty(propertyName);
		return propValue.equals("") ? null : propValue.split(delimeterRegex);
	}

	/**
	 * 
	 * @param propertyName
	 * @param delimeterRegex
	 * @return
	 */
	public static List<String> getPropertyWithDelimeterAsStringList(String propertyName, String delimeterRegex) {
		
		String[] propArr = getPropertyWithDelimeterAsStringArray(propertyName, delimeterRegex);
		return propArr==null ? null : Arrays.asList(propArr);
	}
	
	/**
	 * 
	 * @param propertyName
	 * @return
	 */
	public static int getPropertyAsInt(String propertyName) {
		return Integer.valueOf(getProperty(propertyName));
	}
	
	/**
	 * 
	 * @param propertyName
	 * @return
	 */
	public static double getPropertyAsDouble(String propertyName) {
		return Double.valueOf(getProperty(propertyName));
	}
	
	/**
	 * 
	 * @param propertyName
	 * @return
	 */
	public static float getPropertyAsFloat(String propertyName) {
		return Float.valueOf(getProperty(propertyName));
	}
	
	private static Properties getPropertiesFile() {
		if (property == null) {
			property = new Properties();
			try {
				InputStream is = MicromenderConfigurator.class.getClassLoader().getResourceAsStream(PROPERTY_FILE);
				property.load(is);
			} catch (Exception e) {
				throw new RuntimeException("Error initializing the property file " + PROPERTY_FILE + ": " + e.toString(),e);
			}
		}
		return property;
	}
	
	/**
	 * Preventing construction
	 */
	private MicromenderConfigurator() {
		throw new AssertionError();
	}
}
