/**
 * 
 */
package org.nothing;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Represents, well, a JDBC action such as a query or an update operation, but
 * can also be any snippet of JDBC related code
 * 
 * @author Jawher
 * 
 * @param <T>
 *            the action's result type
 */
public interface JdbcAction<T> {
	/**
	 * Implement this method to interact with a JDBC connection
	 * 
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	T doWithConnection(Connection connection) throws SQLException;
}