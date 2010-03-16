/**
 * 
 */
package org.nothing;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps a row in a result set to a Java object :
 * 
 * <pre>
 * <code>
 * class PersonMapper implements RowMapper<Person> {
 * 		public Person mapRow(ResultSet resultSet, int row) throws SQLException {
 * 			Person p = new Person();
 * 			p.setId(rs.getLong("id"));
 * 			p.setName(rs.getString("name"));
 * 			return p;
 * 		}
 * 	}
 * </code>
 * </pre>
 * 
 * 
 * @author Jawher
 * 
 * @param <T>
 *            the Object type
 */
public interface RowMapper<T> {
	/**
	 * Maps a row in a result set to a Java object
	 * 
	 * @param resultSet
	 *            a resultset ready for consumption via
	 * @param row
	 *            the current row index of the resultset (start from 1)
	 * @return
	 * @throws SQLException
	 */
	T mapRow(ResultSet resultSet, int row) throws SQLException;
}