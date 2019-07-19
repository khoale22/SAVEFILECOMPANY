package com.heb.pm.dictionary;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represents a key on the Suggestion table.
 *
 * @author m314029
 * @since 2.7.0
 */
@Embeddable
public class SuggestionKey implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final int PRIME_NUMBER = 31;

	@Column(name = "word_txt")
	private String wordText;

	@Column(name = "corr_txt")
	private String correctedText;

	/**
	 * Gets word text. This represents a word that needs to be corrected.
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
	 * Gets corrected text. This represents the text to correct a word value to.
	 *
	 * @return the corrected text
	 */
	public String getCorrectedText() {
		return correctedText;
	}

	/**
	 * Sets corrected text.
	 *
	 * @param correctedText the corrected text
	 */
	public void setCorrectedText(String correctedText) {
		this.correctedText = correctedText;
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

		SuggestionKey that = (SuggestionKey) o;

		if (wordText != null ? !wordText.equals(that.wordText) : that.wordText != null) return false;
		return correctedText != null ? correctedText.equals(that.correctedText) : that.correctedText == null;
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
		result = SuggestionKey.PRIME_NUMBER * result + (correctedText != null ? correctedText.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "SuggestionKey{" +
				"wordText='" + wordText + '\'' +
				", correctedText='" + correctedText + '\'' +
				'}';
	}
}
