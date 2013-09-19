package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {
	
	public final static Logger logger = LoggerFactory.getLogger("micromender");

	/**
	 * Preventing construction
	 */
	private LogUtil() {
		throw new AssertionError();
	}
}
