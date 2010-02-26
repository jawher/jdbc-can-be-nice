/**
 * 
 */
package org.nothing;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<T> {
	T mapRow(ResultSet resultSet, int row) throws SQLException;
}