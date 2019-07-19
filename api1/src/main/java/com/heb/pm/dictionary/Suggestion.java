package com.heb.pm.dictionary;

import org.springframework.data.domain.Sort;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a suggestion for a Vocabulary record. It contains the original text, and the suggestion value text.
 * This would be used if the Vocabulary record has a 'fix' case code, but also when the user wants to see all available
 * suggestions for a word.
 *
 * @author m314029
 * @since 2.7.0
 */
@Entity
@Table(name="suggestion")
public class Suggestion implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String SUGGESTION_SORT_FIELD = "key.wordText";

	@EmbeddedId
	private SuggestionKey key;

	@Column(name = "actv_sw")
	private Boolean active;

	@Column(name = "LST_UPDT_TS")
	private LocalDate lstUpdtTs;

	@Column(name = "LST_UPDT_UID")
	private String lstUpdtUid;

	/**
	 * Gets key.
	 *
	 * @return the key
	 */
	public SuggestionKey getKey() {
		return key;
	}

	/**
	 * Sets key.
	 *
	 * @param key the key
	 */
	public void setKey(SuggestionKey key) {
		this.key = key;
	}

	/**
	 * Gets whether or not this suggestion is active or not.
	 *
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * Sets active.
	 *
	 * @param active the active
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * Sets the lstUpdtTs
	 */
	public LocalDate getLstUpdtTs() {
		return lstUpdtTs;
	}

	/**
	 * @return Gets the value of lstUpdtTs and returns lstUpdtTs
	 */
	public void setLstUpdtTs(LocalDate lstUpdtTs) {
		this.lstUpdtTs = lstUpdtTs;
	}

	/**
	 * Sets the lstUpdtUid
	 */
	public String getLstUpdtUid() {
		return lstUpdtUid;
	}

	/**
	 * @return Gets the value of lstUpdtUid and returns lstUpdtUid
	 */
	public void setLstUpdtUid(String lstUpdtUid) {
		this.lstUpdtUid = lstUpdtUid;
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

		Suggestion that = (Suggestion) o;

		return key != null ? key.equals(that.key) : that.key == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "Suggestion{" +
				"key=" + key +
				", active=" + active +
				'}';
	}

    /**
     * Returns the default sort order for the Suggestion table.
     *
     * @return The default sort order for the Suggestion table.
     */
    public static Sort getDefaultSort() {
        return new Sort(
                new Sort.Order(Sort.Direction.ASC, Suggestion.SUGGESTION_SORT_FIELD)
		);
    }
}
