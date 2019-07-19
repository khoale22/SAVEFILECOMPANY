package com.heb.pm.dictionary;

import org.springframework.data.domain.Sort;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents a spelling case code for a Vocabulary record. This means what the user has defined needs to be done on
 * this word before it can be considered 'valid'. Case values are:
 * articl: Part of speech is an Article
 * cap1: Capitalize the first letter
 * conjs: Coordinating conjunctions are lower cased
 * lower: Lower case all letters
 * preps: Preps word length>=4 r capitalized, shorter aren't
 * upper: Capitalize all letters
 * mixed: mixed
 *
 * @author m314029
 * @since 2.7.0
 */
@Entity
@Table(name="case_cd")
public class CaseCode implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String CASE_CODE_SORT_FIELD = "id";

	@Id
	@Column(name = "cs_cd")
	private String id;

	@Column(name = "cs_abb")
	private String abbreviation;

	@Column(name = "cs_des")
	private String description;

	// possible case code values
	public enum CodeValues {
		MIXED("mixed"),
		UPPER_CASE("upper"),
		CAPITALIZE_FIRST_LETTER("cap1"),
		LOWER_CASE_CODE("lower"),
		PREPOSITION("preps"),
		ARTICLE("artcl"),
		CONJUNCTION("conjs");

		private String name;

		CodeValues(String name) {
			this.name = name;
		}

		/**
		 * Gets name.
		 *
		 * @return the name
		 */
		public String getName() {
			return this.name;
		}
	}

	/**
	 * Gets id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets id.
	 *
	 * @param id the id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets abbreviation.
	 *
	 * @return the abbreviation
	 */
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * Sets abbreviation.
	 *
	 * @param abbreviation the abbreviation
	 */
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	/**
	 * Gets description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets description.
	 *
	 * @param description the description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Compares another object to this one. The key is the only thing used to determine equality.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CaseCode caseCode = (CaseCode) o;

		return id != null ? id.equals(caseCode.id) : caseCode.id == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "CaseCode{" +
				"id='" + id + '\'' +
				", abbreviation='" + abbreviation + '\'' +
				", description='" + description + '\'' +
				'}';
	}

	/**
	 * Returns the default sort order for the case code table.
	 *
	 * @return The default sort order for the case code table.
	 */
	public static Sort getDefaultSort() {
		return new Sort(
				new Sort.Order(Sort.Direction.ASC, CaseCode.CASE_CODE_SORT_FIELD)
		);
	}
}
