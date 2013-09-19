package util;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class TimeUtil {
	
	public static String getCurrentTime() {
		return  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
	
	/**
	 * Preventing construction
	 */
	private TimeUtil(){
		throw new AssertionError();
	}
}
