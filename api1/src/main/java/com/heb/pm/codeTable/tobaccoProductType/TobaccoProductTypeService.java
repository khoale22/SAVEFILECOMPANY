/*
 *  TobaccoProductTypeService
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.pm.codeTable.tobaccoProductType;

import com.heb.pm.entity.TobaccoProductType;
import com.heb.pm.repository.TobaccoProductTypeRepository;
import com.heb.pm.ws.CodeTableManagementServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * This is the service for the Tobacco Product Type.
 * @author vn75469
 * @version 2.16.0
 */
@Service
public class TobaccoProductTypeService {
	@Autowired
	private TobaccoProductTypeRepository tobaccoProductTypeRepository;

	@Autowired
	private CodeTableManagementServiceClient codeTableManagementServiceClient;

	/**
	 * Returns all records in table Tobacco Product Types Code.
	 *
	 * @return all records in table Tobacco Product Types Code.
	 */
	public List<TobaccoProductType> getTobaccoProductTypes(){
		return this.tobaccoProductTypeRepository.findAll();
	}

	/**
	 * Call webservice to update Tobacco Product Type tax rate.
	 *
	 * @param tobaccoProductTypes list of Tobacco Product Type to update tax rate.
	 */
	public void updateTobaccoProductType(List<TobaccoProductType> tobaccoProductTypes) {
		this.codeTableManagementServiceClient.updateTobaccoProductType(tobaccoProductTypes);
	}
}

