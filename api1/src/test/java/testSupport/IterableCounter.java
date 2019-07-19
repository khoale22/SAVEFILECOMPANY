/*
 *  IterableCounter.java
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package testSupport;

/**
 * Class to simplify counting the number of objects in a collection.
 *
 * Created by d116773 on 5/10/2016.
 */
public class IterableCounter {

	private long count;

	/**
	 * Adds to the count of objects in the collection.
	 *
	 * @param o The object to add to the count.
	 */
	public void add(Object o) {
		this.count++;
	}

	/**
	 * Returns the count so far of the collection.
	 *
	 * @return The count so far of the collection.
	 */
	public long getCount() {
		return this.count;
	}

	/**
	 * Sets the count back to zero.
	 */
	public void reset() {
		this.count = 0;
	}
}
