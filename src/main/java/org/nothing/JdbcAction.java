/**
 * 
 */
package org.nothing;

import java.sql.Connection;
import java.sql.SQLException;

public interface JdbcAction<T> {
	T doWithConnection(Connection connection) throws SQLException;
}