package org.nothing;

public interface ChainableJdbcAction<T> extends JdbcAction<T> {
	ChainableJdbcAction<T> then(JdbcAction<?> action);

	<S> ChainableJdbcAction<S> thenReturn(JdbcAction<S> action);
}
