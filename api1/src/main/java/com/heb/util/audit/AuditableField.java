package com.heb.util.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be placed on a field from a class that is to be displayed in the form of an audit.
 *
 * @author m314029
 * @since 2.7.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AuditableField {

	String NOT_APPLICABLE = "Not Applicable";

	/**
	 * The name of the audit field as it is displayed to a user.
	 *
	 * @return String representation of the auditable field.
	 */
	String displayName() default AuditableField.NOT_APPLICABLE;

	/**
	 * The name of the audit field as it is displayed to a user. This uses a getter function to get the specific
	 * attribute name to display to a user rather than a hard coded name like displayName(). This could be used in cases
	 * where a table has more than one type of attribute in the same column.
	 * (i.e PROD_DESC_TXT_AUD as 12 different description types)
	 *
	 * @return String representation of the auditable field.
	 */
	String displayNameMethod() default AuditableField.NOT_APPLICABLE;

	/**
	 * The method to call to get the display name for a code table to be displayed to a user. This would be used only
	 * when a value is saved in an audit table, but the user wants to see the code table full value
	 * (i.e. 'Code Table [1]' instead of just '1').
	 *
	 * @return String representation of the method to call to get the code table display name.
	 */
	String codeTableDisplayNameMethod() default AuditableField.NOT_APPLICABLE;

	/**
	 * The name of the filter to be applied to this field.  If the filter doesn't match what the service is checking for
	 * the field will be ignored.
 	 * @return String representation of the filter
	 */
	String[] filter() default AuditableField.NOT_APPLICABLE;
}
