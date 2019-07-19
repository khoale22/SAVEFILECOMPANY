/*
 * ProductDiscontinueExceptionType
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */


package com.heb.pm.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the types of product discontinue rules are available.
 *
 * @author m314029
 * @since 2.0.2
 */
public class ProductDiscontinueExceptionType {

	public static final ProductDiscontinueExceptionType ALL;
	public static final ProductDiscontinueExceptionType VENDOR;
	public static final ProductDiscontinueExceptionType DEPT;
	public static final ProductDiscontinueExceptionType SBT;
	public static final ProductDiscontinueExceptionType UPC;
	public static final ProductDiscontinueExceptionType CLASS;
	public static final ProductDiscontinueExceptionType COMMODITY;
	public static final ProductDiscontinueExceptionType SUBCOMMODITY;
	public static final List<ProductDiscontinueExceptionType> allTypes;

	// Sets up the static variables that represent the types the application supports.
	static {
		ALL = new ProductDiscontinueExceptionType("All", 0);
		VENDOR = new ProductDiscontinueExceptionType("Vendor", 7);
		DEPT = new ProductDiscontinueExceptionType("Dept", 3);
		SBT = new ProductDiscontinueExceptionType("SBT", 1);
		UPC = new ProductDiscontinueExceptionType("UPC", 8);
		CLASS = new ProductDiscontinueExceptionType("Class", 4);
		COMMODITY = new ProductDiscontinueExceptionType("Commodity", 5);
		SUBCOMMODITY = new ProductDiscontinueExceptionType("Sub-commodity", 6);

		allTypes = new ArrayList<>(8);
		allTypes.add(ALL);
		allTypes.add(VENDOR);
		allTypes.add(DEPT);
		allTypes.add(SBT);
		allTypes.add(UPC);
		allTypes.add(CLASS);
		allTypes.add(COMMODITY);
		allTypes.add(SUBCOMMODITY);
	}

	private String type;
	private int priority;

	/**
	 * Constructs a new ProductDiscontinueExceptionType.
	 *
	 * @param type The description of the type.
	 * @param priority The type's priority.
	 */
	private ProductDiscontinueExceptionType(String type, int priority) {
		this.type = type;
		this.priority = priority;
	}

	/**
	 * Returns the description of the type.
	 *
	 * @return The description of the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Returns the priority of the type.
	 *
	 * @return The priority of the type.
	 */
	public int getPriority() {
		return priority;
	}


	/**
	 * Returns a ProductDiscontinueExceptionType that whose value matches a string. If one is not found,
	 * the function throws an IllegalArgumentException.
	 *
	 * @param type The String to try to match on.
	 * @return A ProductDiscontinueExceptionType whose value matches the string.
	 */
	public static ProductDiscontinueExceptionType fromString(String type) {

		String trimmedType = type.trim();

		if (trimmedType.equalsIgnoreCase(ALL.getType())) return ALL;
		if (trimmedType.equalsIgnoreCase(VENDOR.getType())) return VENDOR;
		if (trimmedType.equalsIgnoreCase(DEPT.getType())) return DEPT;
		if (trimmedType.equalsIgnoreCase(SBT.getType())) return SBT;
		if (trimmedType.equalsIgnoreCase(UPC.getType())) return UPC;
		if (trimmedType.equalsIgnoreCase(CLASS.getType())) return CLASS;
		if (trimmedType.equalsIgnoreCase(COMMODITY.getType())) return COMMODITY;
		if (trimmedType.equalsIgnoreCase(SUBCOMMODITY.getType())) return SUBCOMMODITY;

		throw new IllegalArgumentException(type + " is not defined as a ProductDiscontinueExceptionType.");
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return this.type;
	}
}
