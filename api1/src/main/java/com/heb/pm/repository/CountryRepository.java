/*
 *  CountryService
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository to retrieve information about countries.
 *
 * @author s573181
 * @since 2.5.0
 */
public interface CountryRepository extends JpaRepository<Country, Integer> {
	/**
	 * Returns a list of all countries ordered by country name.
	 *
	 * @return a list of all countries ordered by country name.
	 */
	List<Country> findAllByOrderByCountryNameAsc();

	/**
	 * Returns the list of countries that have country Id greater than the country Id provided and ordered by country id.
	 * In the database, there are some countries that have country id equals to zero.
	 * But the app just uses the country that has country id greater than zero,
	 * so we should follow to implement the case to only get the list of countries that have country id greater than 0.
	 *
	 * @param countryId the country id needs to filter the list of countries.
	 * @return  a list of countries ordered by country id.
	 */
	List<Country> findByCountryIdGreaterThanOrderByCountryIdAsc(int countryId);
}
