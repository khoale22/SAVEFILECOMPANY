/*
 * Views
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.util.json;

/**
 * This class can be used by objects that will be serialized over JSON to make JSON views. There is a lot of material
 * out there about how they can be used, but the most straightforward I've found is
 * <a href="https://spring.io/blog/2014/12/02/latest-jackson-integration-improvements-in-spring">here</a>.
 *
 * @author d116773
 * @since 2.0.1
 */
public class Views {

	/**
	 * Interface for attributes that should be included when sending summary data back to the front end.
	 */
	public interface Thin{}

	/**
	 * Interface for attributes that should be included when sending detailed data back to the front end.
	 */
	public interface Thick extends Thin{}
}
