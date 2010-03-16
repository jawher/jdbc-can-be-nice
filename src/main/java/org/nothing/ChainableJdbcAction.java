package org.nothing;

/**
 * Augments a {@link JdbcAction} with chaining capabilities
 * 
 * @author Jawher
 * 
 * @param <T>
 *            the action's return type
 */
public interface ChainableJdbcAction<T> extends JdbcAction<T> {
	/**
	 * returns a new {@link ChainableJdbcAction} (so that it can be chained with
	 * other actions) that executes the current action <b>then</b> the argument
	 * <code>action</code> <b>and returns</b> this action's result</b>
	 * 
	 * @param action
	 *            a {@link JdbcAction} to chain with this action
	 * @return
	 */
	ChainableJdbcAction<T> then(JdbcAction<?> action);

	/**
	 * returns a new {@link ChainableJdbcAction} (so that it can be chained with
	 * other actions) that executes the current action <b>then</b> the argument
	 * <code>action</code> <b>and returns</b> the argument action's result</b>
	 * 
	 * @param <S>
	 *            the argument's action and the resulting action's return type
	 * @param action
	 *            a {@link JdbcAction} to chain with this action
	 * @return
	 */
	<S> ChainableJdbcAction<S> thenReturn(JdbcAction<S> action);
}
