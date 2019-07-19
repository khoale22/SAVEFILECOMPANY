package com.heb.pm.productDetails.product.taxonomy;

import javax.persistence.Transient;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Model that represents a value for a product attribute.
 *
 * @author m314029
 * @since 2.18.4
 */
public class ProductAttributeValue implements Serializable {
	private static final long serialVersionUID = 7389321180982386849L;

	private static final String CODE_TYPE = "CODE";
	private static final String STRING_TYPE = "STRING";
	private static final String NUMBER_TYPE = "NUMBER";
	private static final String TIME_STAMP_TYPE = "TIMESTAMP";
	private static final String DATE_TYPE = "DATE";

	private String type;
	private String text;
	private Double number;
	private LocalDateTime timestamp;
	private LocalDate date;
	private Long code;
	private Integer sequenceNumber;
	private Long sourceSystem;

	@Transient
	private String action;

	public ProductAttributeValue() {
	}

	public ProductAttributeValue(String type, String text, Double number, LocalDateTime timestamp, LocalDate date, Long code, Integer sequenceNumber, Long sourceSystem, String action) {
		this.type = type;
		this.text = text;
		this.number = number;
		this.timestamp = timestamp;
		this.date = date;
		this.code = code;
		this.sequenceNumber = sequenceNumber;
		this.sourceSystem = sourceSystem;
		this.action = action;
	}

	// constructor for code type
	public ProductAttributeValue(Long code, String text, Integer sequenceNumber, Integer sourceSystem) {
		this.code = code;
		this.text = text;
		this.number = null;
		this.timestamp = null;
		this.date = null;
		this.type = CODE_TYPE;
		this.sequenceNumber = sequenceNumber;
		this.sourceSystem = (long)sourceSystem;
	}

	// constructor for string type
	public ProductAttributeValue(String text, Integer sequenceNumber, Integer sourceSystem) {
		this.code = null;
		this.text = text;
		this.number = null;
		this.timestamp = null;
		this.date = null;
		this.type = STRING_TYPE;
		this.sequenceNumber = sequenceNumber;
		this.sourceSystem = (long)sourceSystem;
	}

	// constructor for number type
	public ProductAttributeValue(Double number, Integer sequenceNumber, Integer sourceSystem) {
		this.code = null;
		this.text = null;
		this.number = number;
		this.timestamp = null;
		this.date = null;
		this.type = NUMBER_TYPE;
		this.sequenceNumber = sequenceNumber;
		this.sourceSystem = (long)sourceSystem;
	}

	// constructor for date type
	public ProductAttributeValue(LocalDate date, Integer sequenceNumber, Integer sourceSystem) {
		this.code = null;
		this.text = null;
		this.number = null;
		this.timestamp = null;
		this.date = date;
		this.type = DATE_TYPE;
		this.sequenceNumber = sequenceNumber;
		this.sourceSystem = (long)sourceSystem;
	}

	// constructor for time stamp type
	public ProductAttributeValue(LocalDateTime timestamp, Integer sequenceNumber, Integer sourceSystem) {
		this.code = null;
		this.text = null;
		this.number = null;
		this.timestamp = timestamp;
		this.date = null;
		this.type = TIME_STAMP_TYPE;
		this.sequenceNumber = sequenceNumber;
		this.sourceSystem = (long)sourceSystem;
	}

	public ProductAttributeValue(Long code, String text) {
		this.code = code;
		this.text = text;
		this.number = null;
		this.timestamp = null;
		this.date = null;
		this.type = CODE_TYPE;
		this.sequenceNumber = null;
		this.sourceSystem = null;
	}

	/**
	 * Returns the class as it should be displayed on the GUI.
	 *
	 * @return A String representation of the class as it is meant to be displayed on the GUI.
	 */
	public String getDisplayName() {
		switch (this.type) {
			case CODE_TYPE:
				return this.text;
			case STRING_TYPE:
				return this.text;
			case NUMBER_TYPE:
				return this.number == null ? null : this.number.toString();
			case DATE_TYPE:
				return this.date == null ? null : this.date.toString();
			case TIME_STAMP_TYPE:
				return this.timestamp == null ? null : this.timestamp.toString();
			default:
				return this.text;
		}
	}
	/**
	 * Returns Type.
	 *
	 * @return The Type.
	 **/
	public String getType() {
		return type;
	}

	/**
	 * Returns Text.
	 *
	 * @return The Text.
	 **/
	public String getText() {
		return text;
	}

	/**
	 * Returns Number.
	 *
	 * @return The Number.
	 **/
	public Double getNumber() {
		return number;
	}

	/**
	 * Returns Timestamp.
	 *
	 * @return The Timestamp.
	 **/
	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	/**
	 * Returns Date.
	 *
	 * @return The Date.
	 **/
	public LocalDate getDate() {
		return date;
	}

	/**
	 * Returns Code.
	 *
	 * @return The Code.
	 **/
	public Long getCode() {
		return code;
	}

	/**
	 * Sets the Type.
	 *
	 * @param type The Type.
	 **/
	public ProductAttributeValue setType(String type) {
		this.type = type;
		return this;
	}

	/**
	 * Sets the Text.
	 *
	 * @param text The Text.
	 **/
	public ProductAttributeValue setText(String text) {
		this.text = text;
		return this;
	}

	/**
	 * Sets the Number.
	 *
	 * @param number The Number.
	 **/
	public ProductAttributeValue setNumber(Double number) {
		this.number = number;
		return this;
	}

	/**
	 * Sets the Timestamp.
	 *
	 * @param timestamp The Timestamp.
	 **/
	public ProductAttributeValue setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	/**
	 * Sets the Date.
	 *
	 * @param date The Date.
	 **/
	public ProductAttributeValue setDate(LocalDate date) {
		this.date = date;
		return this;
	}

	/**
	 * Sets the Code.
	 *
	 * @param code The Code.
	 **/
	public ProductAttributeValue setCode(Long code) {
		this.code = code;
		return this;
	}

	/**
	 * Returns Action.
	 *
	 * @return The Action.
	 **/
	public String getAction() {
		return action;
	}

	/**
	 * Sets the Action.
	 *
	 * @param action The Action.
	 **/
	public ProductAttributeValue setAction(String action) {
		this.action = action;
		return this;
	}

	/**
	 * Returns SequenceNumber.
	 *
	 * @return The SequenceNumber.
	 **/
	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * Sets the SequenceNumber.
	 *
	 * @param sequenceNumber The SequenceNumber.
	 **/
	public ProductAttributeValue setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
		return this;
	}

	/**
	 * Returns SourceSystem.
	 *
	 * @return The SourceSystem.
	 **/
	public Long getSourceSystem() {
		return sourceSystem;
	}

	/**
	 * Sets the SourceSystem.
	 *
	 * @param sourceSystem The SourceSystem.
	 **/
	public ProductAttributeValue setSourceSystem(Long sourceSystem) {
		this.sourceSystem = sourceSystem;
		return this;
	}

	/**
	 * Compares another object to this one. This is a deep compare.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ProductAttributeValue that = (ProductAttributeValue) o;
		return Objects.equals(type, that.type) &&
				Objects.equals(text, that.text) &&
				Objects.equals(number, that.number) &&
				Objects.equals(timestamp, that.timestamp) &&
				Objects.equals(date, that.date) &&
				Objects.equals(code, that.code) &&
				Objects.equals(sequenceNumber, that.sequenceNumber) &&
				Objects.equals(sourceSystem, that.sourceSystem);
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {

		return Objects.hash(type, text, number, timestamp, date, code, sequenceNumber, sourceSystem);
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductAttributeValue{" +
				"type='" + type + '\'' +
				", text='" + text + '\'' +
				", number=" + number +
				", timestamp=" + timestamp +
				", date=" + date +
				", code=" + code +
				", sequenceNumber=" + sequenceNumber +
				", sourceSystem=" + sourceSystem +
				", action='" + action + '\'' +
				'}';
	}
}
