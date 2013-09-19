package util;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class JdbcUtil {
	
	public static Long getNonZeroLongNullable(ResultSet rs, int rowNum) throws SQLException {
		long val = rs.getLong(rowNum);
		return val == 0 ? null : Long.valueOf(val);
	}
	
	/**
	 * 
	 * @param rs
	 * @param rowNum
	 * @return
	 * @throws SQLException
	 */
	public static Double getNullableDouble(ResultSet rs, int columnIndex) throws SQLException {
		Object val = rs.getObject(columnIndex);
		return val==null ? null : (Double)val;
	}
	
	/**
	 * 
	 * @param rs
	 * @param columnName
	 * @return
	 * @throws SQLException
	 */
	public static Double getNullableDouble(ResultSet rs, String columnName) throws SQLException {
		Object val = rs.getObject(columnName);
		return val==null ? null : (Double)val;
	}
	
	/**
	 * Preventing construction
	 */
	private JdbcUtil(){
		throw new AssertionError();
	}
	
}
