/*
*  FactoryService

*  Copyright (c) 2016 HEB
*  All rights reserved.
*
*  This software is the confidential and proprietary information of HEB.
*/
package com.heb.pm.productDetails.casePack;

import com.heb.pm.entity.Factory;
import com.heb.pm.repository.FactoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Holds all business logic related to factories.
 *
 * @author s573181
 * @since 2.6.0
 */
@Service
public class FactoryService {

	@Autowired
	private FactoryRepository repository;

	/**
	 * Returns a list of all factories.
	 * 
	 * @return a list of all factories.
	 */
	public List<Factory> findAll(){
		return this.repository.findAll();
	}
}
