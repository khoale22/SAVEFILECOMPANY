package com.heb.scaleMaintenance.entity;

import java.util.function.Function;

/**
 * Interface to add map and save methods to an entity to make them more useful in functional programming.
 *
 * @author d116773
 * @since 2.17.8
 */
public interface TopLevelEntity<T> {

	<R> R map(Function<? super T, ? extends R> mapper);

	T save(Function<? super T, ? extends T> saver);
}
