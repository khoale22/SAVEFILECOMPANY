package com.heb.pm.productDetails;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.ProductMaster;
import com.heb.pm.entity.RecalledProduct;
import com.heb.pm.entity.User;
import com.heb.pm.product.ProductInfoResolver;
import com.heb.pm.product.ProductInfoService;
import com.heb.pm.user.UserService;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * This is the controller for Product Details.
 *
 * @author m594201
 * @since 2.13.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ProductDetailController.PRODUCT_DETAIL_URL)
@AuthorizedResource(ResourceConstants.PRODUCT_BASIC_INFORMATION)
public class ProductDetailController {

	private static final Logger logger = LoggerFactory.getLogger(ProductDetailController.class);
	/**
	 * The constant PRODUCT_DETAIL_URL.
	 */
	protected static final String PRODUCT_DETAIL_URL = "/productDetail";

	/**
	 * The constant GET_PRODUCT_RECALL_DATA.
	 */
	protected static final String GET_PRODUCT_RECALL_DATA = "/getProductRecallData";
	protected static final String GET_UPDATED_PRODUCT = "/getUpdatedProduct";

	private static final String GET_UPDATED_PRODUCT_BY_PRODUCT_ID_LOG_MESSAGE =
			"User %s from IP %s has requested a updated product information for product id: [%d].";
	@Autowired
	private ProductDetailService productDetailService;

	@Autowired
	private ProductInfoService productInfoService;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private UserService userService;

	@Autowired
    private ProductInfoResolver productInfoResolver;

	/**
	 * Gets product recall data for the productDetail Header banner.  Calls a web service and consolidates the data into one returned object.
	 *
	 * @param prodId  the prod id
	 * @param request the request
	 * @return the product recall data
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = GET_PRODUCT_RECALL_DATA)
	public RecalledProduct getProductRecallData(@RequestParam(value = "prodId") Long prodId, HttpServletRequest request) {

		return 	this.productDetailService.getProductRecallData(prodId);
	}

	/**
	 * Gets product recall data for the productDetail Header banner.  Calls a web service and consolidates the data into one returned object.
	 *
	 * @param prodId  the prod id
	 * @param request the request
	 * @return the product recall data
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = GET_UPDATED_PRODUCT)
	public ProductMaster getUpdatedProduct(@RequestParam(value = "prodId") Long prodId, HttpServletRequest request) {

		this.logGetUpdatedProduct(request.getRemoteAddr(), prodId);
		ProductMaster toReturn = this.productInfoService.findProductInfoByProdId(prodId);
		this.productInfoResolver.fetch(toReturn);
		return toReturn;
	}

	/**
	 * Log's a user's request to get updated product by product id.
	 * @param ip The IP address the user is logged in from.
	 * @param prodId The product id to search on.
	 */
	private void logGetUpdatedProduct(String ip, Long prodId){
		ProductDetailController.logger.info(String.format(
				ProductDetailController.GET_UPDATED_PRODUCT_BY_PRODUCT_ID_LOG_MESSAGE,
				this.userInfo.getUserId(), ip, prodId));
	}
}
