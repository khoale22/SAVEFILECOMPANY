/*
 *  CodeDateService
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.product.SpecialAttributes;

import com.heb.pm.CoreEntityManager;
import com.heb.pm.CoreTransactional;
import com.heb.pm.audit.AuditService;
import com.heb.pm.entity.GoodsProduct;
import com.heb.pm.entity.ProductMaster;
import com.heb.pm.product.ProductInfoResolver;
import com.heb.pm.repository.ProductInfoRepository;
import com.heb.pm.ws.ProductManagementServiceClient;
import com.heb.util.audit.AuditRecord;
import com.heb.xmlns.ei.goodsprod.GoodsProd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * This holds all of the business logic for the Code Date page.
 *
 * @author l730832
 * @since 2.12.0
 */
@Service
public class CodeDateService {

	@Autowired
	private ProductManagementServiceClient productManagementServiceClient;

	@Autowired
	private ProductInfoRepository productInfoRepository;

	@Autowired
	private ProductInfoResolver productInfoResolver = new ProductInfoResolver();

	@Autowired
	private AuditService auditService;

	@Autowired
	@CoreEntityManager
	private EntityManager entityManager;

	/**
	 * This saves code date changes and pulls an updated product master from the repository.
	 * @param productMaster updated product master
	 * @return updated product master
	 */
	@CoreTransactional
	public ProductMaster saveCodeDateChanges(ProductMaster productMaster) {

		// Get the existing product master to be able to run through the rules
		ProductMaster originalProductMaster = this.productInfoRepository.findOne(productMaster.getProdId());

		// For all the values to check, if they're not set in the object passed in, pull them from the goods product
		// table from the database.
		boolean isCodeDated = productMaster.getGoodsProduct().getCodeDate() == null ?
				originalProductMaster.getGoodsProduct().getCodeDate() : productMaster.getGoodsProduct().getCodeDate();
		long maxShelfLifeDays = productMaster.getGoodsProduct().getMaxShelfLifeDays() == null ?
				originalProductMaster.getGoodsProduct().getMaxShelfLifeDays() : productMaster.getGoodsProduct().getMaxShelfLifeDays();
		long inboundSpecDays = productMaster.getGoodsProduct().getInboundSpecificationDays() == null ?
				originalProductMaster.getGoodsProduct().getInboundSpecificationDays() : productMaster.getGoodsProduct().getInboundSpecificationDays();
		long warehouseReactionDays = productMaster.getGoodsProduct().getWarehouseReactionDays() == null ?
				originalProductMaster.getGoodsProduct().getWarehouseReactionDays() : productMaster.getGoodsProduct().getWarehouseReactionDays();
		long guaranteeToStoreDays = productMaster.getGoodsProduct().getGuaranteeToStoreDays() == null ?
				originalProductMaster.getGoodsProduct().getGuaranteeToStoreDays() : productMaster.getGoodsProduct().getGuaranteeToStoreDays();
		this.productManagementServiceClient.updateCodeDate(productMaster);
		this.entityManager.refresh(originalProductMaster);
		this.entityManager.refresh(originalProductMaster.getGoodsProduct());
		return originalProductMaster;
	}

	/**
	 * This returns a list of code date audits based on the prodId.
	 * @param prodId way to uniquely ID the set of code date audits requested
	 * @return a list of all the changes made to an product's code date audits.
	 */
	List<AuditRecord> getCodeDateAuditInformation(Long prodId) {
		return this.auditService.getCodeDateAuditInformation(prodId);
	}

}
