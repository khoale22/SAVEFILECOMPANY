package com.heb.pm.ws;

/**
 * This represents a field within an entity that has changed. The fact that the field has changed is put on the service
 * layer, so the webservice layer can just build the requests from these changes.
 *
 * @author m314029
 * @version 2.14.0
 */
public class UpdatableField {

	// Default Constructor
	public UpdatableField(){}

	// Copy Constructor
	public UpdatableField(String field, Object value){
		this.field = field;
		this.value = value;
	}
	private String field;
	private Object value;

	/**
	 * This is the string representation of the field that was updated.
	 *
	 * @return The field that was updated.
	 */
	public String getField() {
		return field;
	}

	/**
	 * Set the field that was updated.
	 *
	 * @param field The field that was updated.
	 */
	public void setField(String field) {
		this.field = field;
	}

	/**
	 * This is the value of the field that was updated.
	 *
	 * @return The updated value.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Set the updated value.
	 *
	 * @param value The updated value.
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "UpdatableField{" +
				"field='" + field + '\'' +
				", value=" + value +
				'}';
	}
}
