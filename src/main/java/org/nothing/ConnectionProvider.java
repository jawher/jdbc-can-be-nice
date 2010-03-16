/**
 * 
 */
package org.nothing;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Absrtacts on how a connection is obtained
 * 
 * @author Jawher
 * 
 */
public interface ConnectionProvider {
	/**
	 * Creates and returns a JDBC connection
	 * 
	 * @return
	 * @throws SQLException
	 */
	Connection get() throws SQLException;
}