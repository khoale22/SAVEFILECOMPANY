/*
 *  ProductCategoryService
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.productCategory;
import com.heb.pm.entity.MarketConsumerEventType;
import com.heb.pm.entity.ProductCategory;
import com.heb.pm.entity.ProductCategoryRole;
import com.heb.pm.repository.MarketConsumerEventTypeRepository;
import com.heb.pm.repository.ProductCategoryRepository;
import com.heb.pm.repository.ProductCategoryRoleRepository;
import com.heb.pm.ws.CodeTableManagementServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * This holds all of the business logic for Product Category.
 *
 * @author vn70529
 * @since 2.12.0
 */
@Service
public class ProductCategoryService {

	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@Autowired
	private MarketConsumerEventTypeRepository marketConsumerEventTypeRepository;

	@Autowired
	private ProductCategoryRoleRepository productCategoryRoleRepository;

	@Autowired
	private CodeTableManagementServiceClient codeTableManagementServiceClient;

	/**
	 * Get list of Product Categories.
	 *
	 * @return List of Product Categories.
	 */
	public List<ProductCategory> findAllProductCategories() {
		return this.productCategoryRepository.findAllByOrderByProductCategoryIdAsc();
	}

	/**
	 * Get list of Market Consumer Event Types.
	 *
	 * @return List of Market Consumer Event Types.
	 */
	public List<MarketConsumerEventType> findAllMarketConsumerEventTypes() {
		return this.marketConsumerEventTypeRepository.findAll();
	}

	/**
	 * Get list of Product Category Roles.
	 *
	 * @return List of Product Category Roles.
	 */
	public List<ProductCategoryRole> findAllProductCategoryRoles() {
		return this.productCategoryRoleRepository.findAll();
	}

	/**
	 * Call webservice to add new Product Categories
	 *
	 * @param listOfProductCategories The list of product categories to add new
	 */
	public void addProductCategories(List<ProductCategory> listOfProductCategories) {
		this.codeTableManagementServiceClient.updateProductCategories(listOfProductCategories,
				CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_ADD.getValue());
	}

	/**
	 * Call webservice to update Product Categories
	 *
	 * @param listOfProductCategories The list of product categories to update
	 */
	public void updateProductCategories(List<ProductCategory> listOfProductCategories) {
		this.codeTableManagementServiceClient.updateProductCategories(listOfProductCategories,
				CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_UPDATE.getValue());
	}

	/**
	 * Call webservice to delete Product Categories
	 *
	 * @param listOfProductCategories The list of product categories to delete
	 */
	public void deleteProductCategories(List<ProductCategory> listOfProductCategories) {
		this.codeTableManagementServiceClient.updateProductCategories(listOfProductCategories,
				CodeTableManagementServiceClient.ACTION_CODE.ACTION_CD_DELETE.getValue());
	}
}