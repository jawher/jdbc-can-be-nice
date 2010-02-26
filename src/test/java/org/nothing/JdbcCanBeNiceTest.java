package org.nothing;

import static junit.framework.Assert.*;
import static org.nothing.JdbcCanBeNice.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.Test;
import static org.mockito.Mockito.*;

public class JdbcCanBeNiceTest {

	private ConnectionProvider createConnectionProvider() {
		ConnectionProvider connectionProvider = mock(ConnectionProvider.class);
		try {
			Connection connection = mock(Connection.class);
			when(connectionProvider.get()).thenReturn(connection);
		} catch (SQLException e1) {
			fail("Shouldn't happen");
		}
		return connectionProvider;
	}

	@Test
	public void testCachingConnectionProvider() {
		ConnectionProvider connectionProvider = createConnectionProvider();

		ConnectionProvider cachingConnectionProvider = cachingConnectionProvider(connectionProvider);

		try {
			cachingConnectionProvider.get();
			cachingConnectionProvider.get();
			verify(connectionProvider, times(1)).get();
		} catch (SQLException e) {
			fail("Shouldn't happen");
		}
	}

	@Test
	public void testDoWithConnection() {
		ConnectionProvider connectionProvider = createConnectionProvider();
		ConnectionProvider cachingConnectionProvider = cachingConnectionProvider(connectionProvider);

		JdbcAction<Object> jdbcAction = mock(JdbcAction.class);

		try {
			when(jdbcAction.doWithConnection(cachingConnectionProvider.get()))
					.thenReturn("jdbc.can.be.nice");
			Object res = doWithConnection(jdbcAction, cachingConnectionProvider);
			verify(connectionProvider, times(1)).get();
			verify(jdbcAction, times(1)).doWithConnection(
					cachingConnectionProvider.get());
			assertEquals("jdbc.can.be.nice", res);
		} catch (SQLException e) {
			fail("Shouldn't happen");
		}
	}

	@Test
	public void testSqlTxSuccess() {
		final Connection connection = mock(Connection.class);
		ConnectionProvider connectionProvider = new ConnectionProvider() {

			public Connection get() throws SQLException {
				return connection;
			}
		};

		JdbcAction<Object> sucessfullJdbcAction = mock(JdbcAction.class);
		try {
			when(sucessfullJdbcAction.doWithConnection(connection)).thenReturn(
					"jdbc.can.be.nice");
		} catch (SQLException e) {
			fail("Shouldn't happen");
		}

		try {
			ChainableJdbcAction<Object> txJdbcAction = sqlTx(sucessfullJdbcAction);
			Object res = doWithConnection(txJdbcAction, connectionProvider);
			verify(sucessfullJdbcAction, times(1)).doWithConnection(connection);
			verify(connection, atLeast(1)).setAutoCommit(false);
			verify(connection).commit();
			verify(connection, never()).rollback();
			assertEquals("jdbc.can.be.nice", res);
		} catch (SQLException e) {
			fail("Shouldn't happen");
		}
	}

	@Test
	public void testSqlTxFailing() {
		final Connection connection = mock(Connection.class);
		ConnectionProvider connectionProvider = new ConnectionProvider() {

			public Connection get() throws SQLException {
				return connection;
			}
		};

		JdbcAction<Object> failingJdbcAction = mock(JdbcAction.class);
		try {
			when(failingJdbcAction.doWithConnection(connection)).thenThrow(
					new RuntimeException());
		} catch (SQLException e) {
			fail("Shouldn't happen");
		}

		try {
			ChainableJdbcAction<Object> txJdbcAction = sqlTx(failingJdbcAction);
			try {
				doWithConnection(txJdbcAction, connectionProvider);
			} catch (RuntimeException e) {
				// expected
			}
			verify(failingJdbcAction, times(1)).doWithConnection(connection);
			verify(connection, atLeast(1)).setAutoCommit(false);
			verify(connection, never()).commit();
			verify(connection).rollback();
		} catch (SQLException e) {
			fail("Shouldn't happen");
		}
	}

	@Test
	public void testSqlUpdate() {
		final Connection connection = mock(Connection.class);
		ConnectionProvider connectionProvider = new ConnectionProvider() {

			public Connection get() throws SQLException {
				return connection;
			}
		};

		PreparedStatement preparedStatement = mock(PreparedStatement.class);
		String sql = "jdbc.can.be.nice";

		try {
			when(connection.prepareStatement(sql))
					.thenReturn(preparedStatement);
			ChainableJdbcAction<Integer> action = sqlUpdate(sql, 1, true,
					"string");
			doWithConnection(action, connectionProvider);

			verify(connection).prepareStatement(sql);
			verify(preparedStatement).setObject(1, 1);
			verify(preparedStatement).setObject(2, true);
			verify(preparedStatement).setObject(3, "string");
			verify(preparedStatement).executeUpdate();
		} catch (SQLException e) {
			fail("Shouldn't happen");
		}
	}

	@Test
	public void testDontThrowSqlException() {
		final Connection connection = mock(Connection.class);
		ConnectionProvider connectionProvider = new ConnectionProvider() {

			public Connection get() throws SQLException {
				return connection;
			}
		};

		JdbcAction<Object> action = mock(JdbcAction.class);
		try {
			when(action.doWithConnection(connection)).thenThrow(
					new SQLException());
		} catch (SQLException e) {
			fail("Shouldn't happen");
		}
		ChainableJdbcAction<Object> dontThrowSqlExceptionAction = dontThrowSqlException(action);

		// This hould not throw an SQL Exception
		doWithConnection(dontThrowSqlExceptionAction, connectionProvider);

		try {
			// check that the original action was called
			verify(action).doWithConnection(connection);
		} catch (SQLException e) {
			fail("Shouldn't happen");
		}
	}
}
