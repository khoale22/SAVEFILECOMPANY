package com.heb.pm.dictionary;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represents a key on the Vocabulary table.
 *
 * @author m314029
 * @since 2.7.0
 */
@Embeddable
public class VocabularyKey implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final int PRIME_NUMBER = 31;

	@Column(name = "word_txt")
	private String wordText;

	@Column(name = "cs_cd")
	private String caseCodeAttribute;

	public VocabularyKey() {
	}

	/**
	 * Constructor method.
	 * @param wordText
	 * @param caseCodeAttribute
	 */
	public VocabularyKey(String wordText, String caseCodeAttribute) {
		this.wordText = wordText;
		this.caseCodeAttribute = caseCodeAttribute;
	}

	/**
	 * Gets word text.
	 *
	 * @return the word text
	 */
	public String getWordText() {
		return wordText;
	}

	/**
	 * Sets word text.
	 *
	 * @param wordText the word text
	 */
	public void setWordText(String wordText) {
		this.wordText = wordText;
	}

	/**
	 * Gets case code attribute.
	 *
	 * @return the case code attribute
	 */
	public String getCaseCodeAttribute() {
		return caseCodeAttribute;
	}

	/**
	 * Sets case code attribute.
	 *
	 * @param caseCodeAttribute the case code attribute
	 */
	public void setCaseCodeAttribute(String caseCodeAttribute) {
		this.caseCodeAttribute = caseCodeAttribute;
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

		VocabularyKey that = (VocabularyKey) o;

		if (wordText != null ? !wordText.equals(that.wordText) : that.wordText != null) return false;
		return caseCodeAttribute != null ? caseCodeAttribute.equals(that.caseCodeAttribute) : that.caseCodeAttribute == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = wordText != null ? wordText.hashCode() : 0;
		result = PRIME_NUMBER * result + (caseCodeAttribute != null ? caseCodeAttribute.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "VocabularyKey{" +
				"wordText='" + wordText + '\'' +
				", caseCodeAttribute='" + caseCodeAttribute + '\'' +
				'}';
	}
}
