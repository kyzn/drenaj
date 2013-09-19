package util;

public final class MathUtil {
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static double divideZeroSafe (int x, int y) {
		return divideZeroSafe((double)x, y);
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static double divideZeroSafe (double x, int y) {
		return y==0 ? 0 : x / y;
	}

	/**
	 * 
	 * @param val
	 * @return
	 */
	public static Double nanSafeDouble(Double val) {
		return nullAndNanSafeDouble(val, null);
	}
	
	/**
	 * 
	 * @param val
	 * @return
	 */
	public static Double nullAndNanSafeDouble(Double val, Double defaultVal) {
		return val==null || Double.isNaN(val) ? defaultVal : val;
	}

	/**
	 * 
	 * @param val
	 * @return
	 */
	public static double nullAndNanSafeDouble(Double val) {
		return nullAndNanSafeDouble(val, 0.0);
	}
	
	/**
	 * Preventing construction
	 */
	private MathUtil () {
		throw new AssertionError();
	}
}