package com.heb.util.ws;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be placed on fields that you want to convert into from a generated web-service object.
 * Created by d116773 on 4/19/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@SuppressWarnings("rawtypes")
public @interface MessageField {

	/**
	 * The name of the property on the generated web-service object.
	 * @return
	 */
	String sourceField();

	/**
	 * The type of object to convert to. This is ignored except when the
	 * target field is a List. In that case, this is used to determine
	 * what kind of object to convert to. That type will also be scanned for
	 * this annotation to do a conversion.
	 * @return
	 */
	Class innerType() default Object.class;

}
