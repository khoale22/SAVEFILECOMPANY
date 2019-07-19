package com.heb.scaleMaintenance.model;

import java.util.function.Function;

/**
 * Interface to add map and save methods to a model object to make them more useful in functional programming.
 *
 * @author d116773
 * @since 1.0.0
 */
public interface TopLevelModel<T> {

	<R> R map(Function<? super T, ? extends R> mapper);

	T validate(Function<? super T, ? extends T> validator);
}
