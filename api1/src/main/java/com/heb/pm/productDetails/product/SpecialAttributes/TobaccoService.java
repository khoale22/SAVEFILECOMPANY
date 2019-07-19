package com.heb.pm.productDetails.product.SpecialAttributes;

import com.heb.pm.audit.AuditService;
import com.heb.pm.entity.ProductMaster;
import com.heb.pm.entity.TobaccoProduct;
import com.heb.pm.entity.TobaccoProductType;
import com.heb.pm.product.ProductInfoResolver;
import com.heb.pm.repository.ProductInfoRepository;
import com.heb.pm.repository.TobaccoProductRepository;
import com.heb.pm.repository.TobaccoProductTypeRepository;
import com.heb.pm.ws.ProductManagementServiceClient;
import com.heb.util.audit.AuditRecord;
import com.heb.util.jpa.LazyObjectResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This is the service for the Tobacco Product tab
 * @author s753601
 * @version 2.13.0
 */
@Service
public class TobaccoService {

	@Autowired
	private AuditService auditService;

	@Autowired
	private TobaccoProductTypeRepository tobaccoProductTypeRepository;

	@Autowired
	private TobaccoProductRepository tobaccoProductRepository;

	@Autowired
	private ProductInfoRepository productInfoRepository;

	@Autowired
	private ProductManagementServiceClient productManagementServiceClient;

	private LazyObjectResolver<ProductMaster> productMasterResolver = new ProductInfoResolver();

	/**
	 * Returns all of the values in the TobaccoType Code Table
	 * @return
	 */
	public List<TobaccoProductType> getTobaccoProductTypes(){
		return this.tobaccoProductTypeRepository.findAll();
	}

	/**
	 * Returns the tobacco product with the product id
	 * @return
	 */
	public TobaccoProduct getTobaccoProduct(Long prodId){
		return this.tobaccoProductRepository.findByProdId(prodId);
	}

	/**
	 * This saves code date changes and pulls an updated product master from the repository.
	 * @param productMaster updated product master
	 * @return updated product master
	 */
	public ProductMaster saveTobaccoChanges(ProductMaster productMaster) {
		this.productManagementServiceClient.updateTobacco(productMaster);
		ProductMaster updatedProductMaster = this.productInfoRepository.findOne(productMaster.getProdId());
		this.productMasterResolver.fetch(updatedProductMaster);
		return updatedProductMaster;
	}

	/**
	 * This returns a list of tobacco audits based on the prodId.
	 * @param prodId way to uniquely ID the set of tobacco audits requested
	 * @return a list of all the changes made to an product's tobacco audits.
	 */
	List<AuditRecord> getTobaccoAuditInformation(Long prodId) {
		return this.auditService.getTobaccoAuditInformation(prodId);
	}
}
