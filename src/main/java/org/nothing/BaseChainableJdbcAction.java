package org.nothing;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * A base class you can inherit from that handles the chaining logic defined in
 * {@link ChainableJdbcAction}
 * 
 * @author Jawher
 * 
 * @param <T>
 */
public abstract class BaseChainableJdbcAction<T> implements
		ChainableJdbcAction<T> {

	public <S> ChainableJdbcAction<S> thenReturn(final JdbcAction<S> action) {
		return new BaseChainableJdbcAction<S>() {

			public S doWithConnection(Connection connection)
					throws SQLException {
				BaseChainableJdbcAction.this.doWithConnection(connection);
				return action.doWithConnection(connection);
			}
		};
	}

	public ChainableJdbcAction<T> then(final JdbcAction<?> action) {
		return new BaseChainableJdbcAction<T>() {

			public T doWithConnection(Connection connection)
					throws SQLException {
				T res = BaseChainableJdbcAction.this
						.doWithConnection(connection);
				action.doWithConnection(connection);
				return res;
			}
		};
	}

}
