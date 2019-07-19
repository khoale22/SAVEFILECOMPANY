package com.heb.pm;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Since we have multiple entity managers, this annotation can be added to auto wired EntityManager properties
 * when the main entity manager is needed.
 *
 * @author d116773
 * @since 2.3.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
//db2ToOraclecHanges by vn76717
@Qualifier("emfOracle")
public @interface CoreEntityManager {
}
