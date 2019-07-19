/*
 * Store
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.pm.entity;

import com.heb.util.ws.MessageField;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * Represents a store.
 *
 * @author d116773
 * @since 2.0.1
 */
@Document(indexName="retail-loc", type="store")
public class Store implements Serializable{

	private static final long serialVersionUID = 1L;

	private static final int FOUR_BYTES = 32;

	@Id
	@MessageField(sourceField = "LOCNBR")
	private long storeNumber;

	@MessageField(sourceField = "LOCNM")
	private String name;

	/**
	 * Returns the store's corporate number.
	 *
	 * @return The store's corporate number.
	 */
	public long getStoreNumber() {
		return storeNumber;
	}

	/**
	 * Sets the store's corporate number.
	 *
	 * @param storeNumber The store's corporate number.
	 */
	public void setStoreNumber(long storeNumber) {
		this.storeNumber = storeNumber;
	}

	/**
	 * Returns the store's name.
	 *
	 * @return The store's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the store's name.
	 *
	 * @param name The store's name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Reutns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "Store{" +
				"storeNumber=" + storeNumber +
				", name='" + name + '\'' +
				'}';
	}

	/**
	 * Compares another object to this one. Equality is based on store number.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Store)) return false;

		Store store = (Store) o;

		return storeNumber == store.storeNumber;

	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will have different hashes.
	 *
	 * @return The hash code for this object.
	 */

	@Override
	public int hashCode() {
		return (int) (storeNumber ^ (storeNumber >>> Store.FOUR_BYTES));
	}
}
