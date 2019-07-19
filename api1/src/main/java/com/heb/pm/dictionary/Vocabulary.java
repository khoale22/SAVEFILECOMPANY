/*
 * Vocabulary
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.dictionary;

import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a vocabulary source that is a created (or imported) text that a user can, or cannot enter as a word, and
 * how the text should be cased (i.e. lowercase or uppercase).
 *
 * @author m314029
 * @since 2.7.0
 */
@Entity
@Table(name="vocabulary")
public class Vocabulary implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String VOCABULARY_SORT_FIELD = "key.wordText";

	@EmbeddedId
	private VocabularyKey key;

	@Column(name = "word_cd")
	private String wordCodeAttribute;

	@Column(name = "actv_sw")
	private Boolean active;

	@Column(name = "LST_UPDT_TS")
	private LocalDate lstUpdtTs;

	@Column(name = "LST_UPDT_UID")
	private String lstUpdtUid;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "word_cd", referencedColumnName = "word_cd", insertable = false, updatable = false)
	private WordCode wordCode;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cs_cd", referencedColumnName = "cs_cd", insertable = false, updatable = false)
	private CaseCode caseCode;

	/**
	 * Gets key.
	 *
	 * @return the key
	 */
	public VocabularyKey getKey() {
		return key;
	}

	/**
	 * Sets key.
	 *
	 * @param key the key
	 */
	public void setKey(VocabularyKey key) {
		this.key = key;
	}

	/**
	 * Gets whether or not the vocabulary record is active.
	 *
	 * @return whether or not the vocabulary record is active
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * Sets whether or not the vocabulary record is active..
	 *
	 * @param active whether or not the vocabulary record is active
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * Gets word code attribute.
	 *
	 * @return the word code attribute
	 */
	public String getWordCodeAttribute() {
		return wordCodeAttribute;
	}

	/**
	 * Sets word code attribute.
	 *
	 * @param wordCodeAttribute the word code attribute
	 */
	public void setWordCodeAttribute(String wordCodeAttribute) {
		this.wordCodeAttribute = wordCodeAttribute;
	}

	/**
	 * Gets word code. This is the code table for words.
	 *
	 * @return the word code
	 */
	public WordCode getWordCode() {
		return wordCode;
	}

	/**
	 * Sets word code.
	 *
	 * @param wordCode the word code
	 */
	public void setWordCode(WordCode wordCode) {
		this.wordCode = wordCode;
	}

	/**
	 * Gets case code. This is the code table for cases.
	 *
	 * @return the case code
	 */
	public CaseCode getCaseCode() {
		return caseCode;
	}

	/**
	 * Sets case code.
	 *
	 * @param caseCode the case code
	 */
	public void setCaseCode(CaseCode caseCode) {
		this.caseCode = caseCode;
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

		Vocabulary that = (Vocabulary) o;

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
		return "Vocabulary{" +
				"key=" + key +
				", wordCodeAttribute='" + wordCodeAttribute + '\'' +
				", active=" + active +
				", wordCode=" + wordCode +
				", caseCode=" + caseCode +
				'}';
	}

	/**
	 * Returns the default sort order for the Vocabulary table.
	 *
	 * @return The default sort order for the Vocabulary table.
	 */
	public static Sort getDefaultSort() {
		return  new Sort(
				new Sort.Order(Sort.Direction.ASC, Vocabulary.VOCABULARY_SORT_FIELD)
		);
	}
}
