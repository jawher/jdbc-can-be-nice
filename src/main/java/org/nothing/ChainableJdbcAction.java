package org.nothing;

public interface ChainableJdbcAction<T> extends JdbcAction<T> {
	ChainableJdbcAction<T> andThen(JdbcAction<?> action);

	<S> ChainableJdbcAction<S> andReturn(JdbcAction<S> action);
}
