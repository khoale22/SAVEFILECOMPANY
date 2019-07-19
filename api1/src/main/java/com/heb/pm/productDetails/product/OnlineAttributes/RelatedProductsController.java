/*
 *  RelatedProductsController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.product.OnlineAttributes;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.ProductRelationship;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Represents Online Attributes RelatedProducts Controller
 *
 * @author a786878
 * @since 2.14.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + RelatedProductsController.ONLINE_URL)
@AuthorizedResource(ResourceConstants.ONLINE_ATTRIBUTES)
public class RelatedProductsController {

	private static final Logger logger = LoggerFactory.getLogger(RelatedProductsController.class);

	private static final String LOG_RELATED_PRODUCTS_BY_PRODUCT_ID = "User %s from IP %s has requested related products for product id: %s";

	private static final String LOG_RELATED_PRODUCTS_DELETE_REQUEST = "User %s from address %s has requested to delete the " +
			"online attributes (related products) of product with id %s and related product id %s";

	private static final String LOG_RELATED_PRODUCTS_ADD_REQUEST = "User %s from address %s has requested to add the " +
			"online attributes (related products) of product with id %s and related product id %s";

	protected static final String ONLINE_URL = "/onlineInfo";

	protected static final String GET_RELATED_PRODUCTS = "/getRelatedProducts";
	protected static final String SAVE_RELATED_PRODUCTS = "/saveRelatedProducts";

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private RelatedProductsService service;

	private ProductRelationshipResolver productRelationshipResolver = new ProductRelationshipResolver();

	/**
	 * Gets product relationships.
	 *
	 * @param prodId  the prod id
	 * @param request the request
	 * @return the product relationships
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = RelatedProductsController.GET_RELATED_PRODUCTS)
	public List<ProductRelationship> getRelatedProducts(@RequestParam(value="prodId") Long prodId, HttpServletRequest request) {

		this.logGetMrtAuditInformation(request.getRemoteAddr(), prodId);

		List<ProductRelationship> relationships = this.service.getRelatedProducts(prodId);
        relationships.forEach(this.productRelationshipResolver::fetch);

		return relationships;
	}

	/**
	 * Save product relationships updates and deletes
	 *
	 * @param relatedProducts  the related products to be deleted
	 * @param request the request
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = RelatedProductsController.SAVE_RELATED_PRODUCTS)
	public void saveRelatedProducts(@RequestBody List<ProductRelationship> relatedProducts, HttpServletRequest request) {

		relatedProducts.stream().forEach((product) -> {
			if (product.getActionCode().equals(RelatedProductsService.ACTION_CODE_DELETE)) {
				logDeleteProduct(request.getRemoteAddr(), product.getKey().getProductId(), product.getKey().getRelatedProductId());
			} else if (product.getActionCode().equals(RelatedProductsService.ACTION_CODE_ADD)) {
				logAddProduct(request.getRemoteAddr(), product.getKey().getProductId(), product.getKey().getRelatedProductId());
			}
		});

		this.service.saveRelatedProducts(relatedProducts);

		return;
	}

	/**
	 * Logs related product to be deleted.
	 *
	 * @param ip The user's ip.
	 * @param prodId the primary product
     * @param relatedProdId the related product ID to be deleted
	 */
	private void logDeleteProduct(String ip, Long prodId, Long relatedProdId) {
		RelatedProductsController.logger.info(
				String.format(RelatedProductsController.LOG_RELATED_PRODUCTS_DELETE_REQUEST, this.userInfo.getUserId(), ip, prodId, relatedProdId)
		);
	}

	/**
	 * Logs related product to be added.
	 *
	 * @param ip The user's ip.
	 * @param prodId the primary product
	 * @param relatedProdId the related product ID to be added
	 */
	private void logAddProduct(String ip, Long prodId, Long relatedProdId) {
		RelatedProductsController.logger.info(
				String.format(RelatedProductsController.LOG_RELATED_PRODUCTS_ADD_REQUEST, this.userInfo.getUserId(), ip, prodId, relatedProdId)
		);
	}

	/**
	 * Logs get related products information by product ID.
	 *
	 * @param ip The user's ip.
	 * @param prodId The product ID being searched on.
	 */
	private void logGetMrtAuditInformation(String ip, Long prodId) {
		RelatedProductsController.logger.info(
				String.format(RelatedProductsController.LOG_RELATED_PRODUCTS_BY_PRODUCT_ID, this.userInfo.getUserId(), ip, prodId)
		);
	}

	/**
	 * Resolver fetch product master data for Product relationship object.
	 */
	private class ProductRelationshipResolver implements LazyObjectResolver<ProductRelationship> {

		/**
		 * Resolves a product relationship object to fetch related master from lazy JPQL mapping
		 *
		 * @param productRelationship the product relationship to fetch product master for.
		 */
		@Override
		public void fetch(ProductRelationship productRelationship) {
            if (productRelationship.getRelatedProduct() != null) {
                productRelationship.getRelatedProduct().getProdId();
            }
            if (productRelationship.getRelatedProduct().getGoodsProduct() != null) {
                productRelationship.getRelatedProduct().getGoodsProduct().isSellableProduct();
            }
        }
    }
}
