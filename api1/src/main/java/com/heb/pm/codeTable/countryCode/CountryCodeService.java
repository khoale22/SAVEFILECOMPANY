/*
 *  CountryCodeService
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.pm.codeTable.countryCode;

import com.heb.pm.entity.Country;
import com.heb.pm.repository.CountryRepository;
import com.heb.pm.ws.CodeTableManagementServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Holds all business logic related to country code information.
 *
 * @author vn70529
 * @since 2.12.0
 */
@Service
public class CountryCodeService {

	@Autowired
	private CountryRepository repository;
	@Autowired
	private CodeTableManagementServiceClient serviceClient;
	/**
	 * Returns the list of countries ordered by country id.
	 *
	 * @return the countries ordered by country id.
	 */
	public List<Country> findAllByOrderByCountryIdAsc() {
		int removeCountryId = 0;
		return this.repository.findByCountryIdGreaterThanOrderByCountryIdAsc(removeCountryId);
	}
	/**
	 * Add new the list of countries.
	 *
	 * @param countries The list of countries to add new.
	 */
	public void addCountryCodes(List<Country> countries) {
		this.serviceClient.updateCountryCode(countries,  CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_ADD.getValue());
	}
	/**
	 * Update information for the list of countries.
	 *
	 * @param countries The list of countries to edit.
	 */
	public void updateCountryCodes(List<Country> countries) {
		this.serviceClient.updateCountryCode(countries, CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_UPDATE.getValue());
	}
	/**
	 * Delete the list of countries.
	 *
	 * @param countries The list of countries to delete.
	 */
	public void deleteCountryCodes(List<Country> countries) {
		this.serviceClient.updateCountryCode(countries, CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_DELETE.getValue());
	}
}
