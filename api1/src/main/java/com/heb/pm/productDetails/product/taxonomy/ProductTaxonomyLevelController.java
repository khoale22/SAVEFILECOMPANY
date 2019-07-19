package com.heb.pm.productDetails.product.taxonomy;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.GenericEntityRelationship;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Rest endpoint for product taxonomy levels.
 *
 * @author m314029
 * @since 2.18.4
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ProductTaxonomyLevelController.CLASS_ROOT_URL)
@AuthorizedResource(ResourceConstants.PRODUCT_TAXONOMY_LEVEL)
public class ProductTaxonomyLevelController {

	private static final Logger logger = LoggerFactory.getLogger(ProductTaxonomyLevelController.class);
	protected static final String CLASS_ROOT_URL = "/productTaxonomy/level";

	// urls
	private static final String FIND_ALL_URL = "findAll";

	// logs
	private static final String FIND_ALL_LOG_MESSAGE = "User %s from IP %s has requested all taxonomy levels " +
			"for product id: %d.";

	// error messages
	private static final String NO_PRODUCT_ID_MESSAGE = "Product ID cannot be null.";
	private static final String NO_PROD_ID_MESSAGE_KEY = "ProductTaxonomyLevelController.missingProductId";

	@Autowired
	private NonEmptyParameterValidator parameterValidator;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private ProductTaxonomyLevelService service;

	/**
	 * Find all product taxonomy levels given a product id and hierarchy context code.
	 *
	 * @param productId The product id being used to search for all parents
	 * @param request The HTTP request that initiated this call.
	 * @return A list of relationships.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ProductTaxonomyLevelController.FIND_ALL_URL)
	public List<GenericEntityRelationship> findAll(
			@RequestParam Long productId, HttpServletRequest request) {
		this.parameterValidator.validate(productId,
				NO_PRODUCT_ID_MESSAGE, NO_PROD_ID_MESSAGE_KEY, request.getLocale());
		ProductTaxonomyLevelController.logger.info(String.format(FIND_ALL_LOG_MESSAGE, this.userInfo
				.getUserId(), request.getRemoteAddr(), productId));
		return this.service.findAll(productId);
	}
}
