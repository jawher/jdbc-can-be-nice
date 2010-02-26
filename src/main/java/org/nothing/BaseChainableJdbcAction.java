package org.nothing;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseChainableJdbcAction<T> implements
		ChainableJdbcAction<T> {

	public <S> ChainableJdbcAction<S> andReturn(final JdbcAction<S> action) {
		return new BaseChainableJdbcAction<S>() {

			public S doWithConnection(Connection connection)
					throws SQLException {
				BaseChainableJdbcAction.this.doWithConnection(connection);
				return action.doWithConnection(connection);
			}
		};
	}

	public ChainableJdbcAction<T> andThen(final JdbcAction<?> action) {
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
