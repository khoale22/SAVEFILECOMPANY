package com.heb.pm.productDetails.product.SpecialAttributes;

import com.heb.pm.entity.CustomShippingMethod;
import com.heb.pm.entity.ProductRestrictions;
import com.heb.pm.entity.ProductShippingException;
import com.heb.pm.repository.CustomShippingMethodRepository;
import com.heb.pm.repository.ProductRestrictionsRepository;
import com.heb.pm.repository.ProductShippingExceptionRepository;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.pm.ws.ProductHierarchyServiceClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m594201 on 11/16/2017.
 */
@Service
public class RestrictionService {

	@Autowired
	private ProductShippingExceptionRepository productShippingExceptionRepository;

    @Autowired
	private ProductRestrictionsRepository productRestrictionsRepository;

    @Autowired
	private CustomShippingMethodRepository customShippingMethodRepository;
    
    @Autowired
	private ProductHierarchyServiceClient productHierarchyServiceClient;

	private static final String SHIPPING_RESTRICTION_CODE="9";

	/**
	 * Gets shipping restrictions.
	 *
	 * @return the shipping restrictions
	 */
	public List<ProductRestrictions> getShippingRestrictions() {

		return this.productRestrictionsRepository.findAll();
	}

	/**
	 * Gets product shipping restrictions by product id.
	 *
	 * @param prodId the prod id
	 * @return the product shipping restrictions by product id
	 */
	public List<ProductRestrictions> getProductShippingRestrictionsByProductId(Long prodId) {
		return this.productRestrictionsRepository.findByKeyProdIdAndRestrictionRestrictionGroupCode(prodId, SHIPPING_RESTRICTION_CODE);
	}

	/**
	 * Get list of sales restrictions by sub commodity.
	 *
	 * @param subcomcd The sub commodity code.
	 * @return the list of sales restrictions.
	 */
	public List<String> getSalesRestrictionsBySubCommodity(String subcomcd) {
		List<String> listReturn = null;
		try {
			listReturn = this.productHierarchyServiceClient.getSalesRestrictionsBySubCommodity(subcomcd);
		} catch (CheckedSoapException e) {
			listReturn = new ArrayList<>();
		}
		return listReturn;
	}

	/**
	 * Get list of state warning by sub commodity.
	 *
	 * @param subcomcd The sub commodity code.
	 * @return the list of state warning.
	 */
	public List<String> getStateWarningBySubcommodity(String subcomcd){
		List<String> listReturn = null;
		try {
			listReturn = this.productHierarchyServiceClient.getStateWarningBySubcommodity(subcomcd);
		} catch (CheckedSoapException e) {
			listReturn = new ArrayList<>();
		}
		return listReturn;
	}

	/**
	 * Gets shipping method exceptions by product id.
	 *
	 * @param prodId the prod id
	 * @return the shipping method exceptions by product id
	 */
	public List<ProductShippingException> getShippingMethodExceptionsByProductId(Long prodId) {
		return this.productShippingExceptionRepository.findByKeyProdId(prodId);
	}

	/**
	 * Find all shipping method exceptions list.
	 *
	 * @return the list
	 */
	public List<CustomShippingMethod> findAllShippingMethodExceptions() {
		return this.customShippingMethodRepository.findAll();
	}
}
