package com.heb;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Since we have multiple entity managers, this annotation can be added to auto wired EntityManager properties
 * when the main entity manager is needed.
 *
 * @author vn55306
 * @since 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Qualifier("emfOracle")
public @interface CoreEntityManager {
}
