package com.hellokoding.springboot.jpa;

public interface LazyObjectResolver<DataType> {

    /**
     * Performs the actual resolution of lazily loaded parameters.
     * @param d The object to resolve.
     */
    void fetch(DataType d);
}
