/*
 *  CountryService
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.country;

import com.heb.pm.entity.Country;
import com.heb.pm.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Holds all business logic related to country information.
 *
 * @author s573181
 * @since 2.5.0
 */
@Service
public class CountryService {

	@Autowired
	private CountryRepository repository;

	/**
	 * Returns all countries ordered by country name.
	 *
	 * @return all countries ordered by country name.
	 */
	public List<Country> findAllOrderByCountryName(){
		return this.repository.findAllByOrderByCountryNameAsc();
	}
}
