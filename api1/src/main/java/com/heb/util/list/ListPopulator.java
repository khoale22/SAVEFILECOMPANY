/*
 * com.heb.util.list.ListPopulator
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.util.list;

import java.io.Serializable;
import java.util.List;
import java.util.function.Supplier;

/**
 * Can be used to populate an collection to a consistent size. Generally, this is
 * so you can use in-lists.
 *
 * @author d116773
 * @param <DataType> The type that is used to define the list that is being filled in.
 */
public class ListPopulator<DataType extends Serializable> {

	private Supplier<? extends DataType> objectCreator;

	/**
	 * Creates a new ListPopulator.
	 *
	 * @param objectCreator A Supplier that can be used to create objects that will be stored in the list.
	 *                      For example String::new will create new Strings.
	 */
	public ListPopulator(Supplier<? extends DataType> objectCreator) {
		this.objectCreator = objectCreator;
	}

	/**
	 * Adds objects to the list to fill it out to at least count size. If the list is null, the list will remain null.
	 * If the list is already larger than count, it is not modified.
	 *
	 * @param list The list to add objects to.
	 * @param count The size to which you wish to increase the list.
	 */
	public void populate(List<? super DataType> list, int count) {
		if (list == null) {
			return;
		}

		if (list.size() >= count) {
			return;
		}

		int toAdd = count - list.size();

		for (int i = 0; i < toAdd; i++) {
			list.add(this.objectCreator.get());
		}
	}
}
