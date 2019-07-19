/*
 * DictionaryVO
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.dictionary;

import com.heb.util.list.ListFormatter;

import java.io.Serializable;
import java.util.List;

/**
 * Represents the dictionary rules flattened out in a way that more directly represents the business
 * understanding of the business rules. This object will make it easier to send data back and forth with the
 * front end.
 *
 * @author vn70516
 * @since 2.7.0
 */
public class DictionaryVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Vocabulary> vocabularyListUpdate;
	private List<Vocabulary> vocabularyListDelete;

	private List<Suggestion> suggestionListUpdate;
	private List<Suggestion> suggestionListDelete;

	/**
	 * Sets the vocabularyListUpdate
	 */
	public List<Vocabulary> getVocabularyListUpdate() {
		return vocabularyListUpdate;
	}

	/**
	 * @return Gets the value of vocabularyListUpdate and returns vocabularyListUpdate
	 */
	public void setVocabularyListUpdate(List<Vocabulary> vocabularyListUpdate) {
		this.vocabularyListUpdate = vocabularyListUpdate;
	}

	/**
	 * Sets the vocabularyListDelete
	 */
	public List<Vocabulary> getVocabularyListDelete() {
		return vocabularyListDelete;
	}

	/**
	 * @return Gets the value of vocabularyListDelete and returns vocabularyListDelete
	 */
	public void setVocabularyListDelete(List<Vocabulary> vocabularyListDelete) {
		this.vocabularyListDelete = vocabularyListDelete;
	}

	/**
	 * Sets the suggestionListUpdate
	 */
	public List<Suggestion> getSuggestionListUpdate() {
		return suggestionListUpdate;
	}

	/**
	 * @return Gets the value of suggestionListUpdate and returns suggestionListUpdate
	 */
	public void setSuggestionListUpdate(List<Suggestion> suggestionListUpdate) {
		this.suggestionListUpdate = suggestionListUpdate;
	}

	/**
	 * Sets the suggestionListDelete
	 */
	public List<Suggestion> getSuggestionListDelete() {
		return suggestionListDelete;
	}

	/**
	 * @return Gets the value of suggestionListDelete and returns suggestionListDelete
	 */
	public void setSuggestionListDelete(List<Suggestion> suggestionListDelete) {
		this.suggestionListDelete = suggestionListDelete;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "DictionaryVO{" +
				"vocabularyListUpdate=" + ListFormatter.formatAsString(vocabularyListUpdate) +
				", vocabularyListDelete=" + ListFormatter.formatAsString(vocabularyListDelete) +
				", suggestionListUpdate=" + ListFormatter.formatAsString(suggestionListUpdate) +
				", suggestionListDelete=" + ListFormatter.formatAsString(suggestionListDelete) +
				'}';
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
		if (!(o instanceof DictionaryVO)) return false;

		DictionaryVO that = (DictionaryVO) o;

		if (!getVocabularyListUpdate().equals(that.getVocabularyListUpdate())) return false;
		if (!getVocabularyListDelete().equals(that.getVocabularyListDelete())) return false;
		if (!getSuggestionListUpdate().equals(that.getSuggestionListUpdate())) return false;
		return getSuggestionListDelete().equals(that.getSuggestionListDelete());
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = getVocabularyListUpdate().hashCode();
		result = 31 * result + getVocabularyListDelete().hashCode();
		result = 31 * result + getSuggestionListUpdate().hashCode();
		result = 31 * result + getSuggestionListDelete().hashCode();
		return result;
	}
}
