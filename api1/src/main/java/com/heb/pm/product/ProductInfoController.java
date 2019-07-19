package com.heb.pm.product;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.ItemMaster;
import com.heb.pm.entity.ItemMasterKey;
import com.heb.pm.entity.ProductMaster;
import com.heb.pm.entity.User;
import com.heb.pm.user.UserService;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * REST endpoint for product information.
 *
 * @author s573181
 * @since 2.0.1
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL)
@AuthorizedResource(ResourceConstants.PRODUCT_BASIC_INFORMATION)
public class ProductInfoController {

   	private static final Logger logger = LoggerFactory.getLogger(ProductInfoController.class);

	private static final String PRODUCT_INFO_URL = "product/{productId}";
	private static final String ITEM_INFO_URL = "itemMaster";

    // Keys to user facing messages in the message resource bundle.
    private static final String DEFAULT_NO_PRODUCT_ID_MESSAGE = "Product ID cannot be null.";
    private static final String DEFAULT_PROD_ID_MESSAGE_KEY = "ProductInfoController.missingProductId";

    private static final String DEFAULT_NO_ITEM_ID_MESSAGE = "Item ID cannot be null.";
	private static final String DEFAULT_ITEM_ID_MESSAGE_KEY = "ProductInfoController.missingItemId";




	// Log Messages
    private static final String GET_PROD_INFO_BY_PROD_ID_MESSAGE =
            "User %s from IP %s has requested product info date for the following item code [%s]";
	private static final String GET_ITEM_INFO_BY_ITEM_ID_MESSAGE =
			"User %s from IP %s has requested item info date for the following item code [%s]";

	private LazyObjectResolver<ItemMaster> itemMasterLazyObjectResolver = new ItemMasterResolver();

	@Autowired
	private UserService userService;
	/**
	 * Resolves a ItemMaster object. It will load the following properties:
	 * 1 itemMasterKey
	 */
	private class ItemMasterResolver implements LazyObjectResolver<ItemMaster> {

		@Override
		public void fetch(ItemMaster itemMaster) {
			itemMaster.getKey().getItemCode();

			if (userService != null) {
				User user = userService.getUserById(itemMaster.getAddedUsrId());
				if (user != null) {
					itemMaster.setDisplayCreatedName(user.getDisplayName());
				} else {
					itemMaster.setDisplayCreatedName(itemMaster.getAddedUsrId());
				}
			}
		}
	}


    @Autowired private ProductInfoService productInfoService;
    @Autowired private UserInfo userInfo;
	@Autowired private NonEmptyParameterValidator parameterValidator;
	@Autowired ProductInfoResolver productInfoResolver = new ProductInfoResolver();

    /**
     * Gets prod info by prod id.
     *
     * @param productId the product id
     * @param request the request
     * @return the prod info by prod id
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductInfoController.PRODUCT_INFO_URL)
    public ProductMaster findProductByProductId(@PathVariable Long productId, HttpServletRequest request) {

		// productId is required.
		this.parameterValidator.validate(productId, ProductInfoController.DEFAULT_NO_PRODUCT_ID_MESSAGE,
				ProductInfoController.DEFAULT_PROD_ID_MESSAGE_KEY, request.getLocale());

        this.logGetProdInfo(request.getRemoteAddr(), productId, GET_PROD_INFO_BY_PROD_ID_MESSAGE);

		ProductMaster pm = this.productInfoService.findProductInfoByProdId(productId);
		this.productInfoResolver.fetch(pm);
        return pm;
    }

	/**
	 * Finds itemMasters based on the item codes
	 * @param itemMasterKey The item ID of the requested Item Master
	 * @param request HTTP request
	 * @return
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.POST, value = ProductInfoController.ITEM_INFO_URL)
	public ItemMaster findItemByKey(@RequestBody ItemMasterKey itemMasterKey, HttpServletRequest request) {
		this.parameterValidator.validate(itemMasterKey, ProductInfoController.DEFAULT_NO_ITEM_ID_MESSAGE,
				ProductInfoController.DEFAULT_ITEM_ID_MESSAGE_KEY, request.getLocale());
		this.logGetProdInfo(request.getRemoteAddr(), itemMasterKey.getItemCode(), GET_ITEM_INFO_BY_ITEM_ID_MESSAGE);
		ItemMaster im = this.productInfoService.findItemByItemId(itemMasterKey);
		this.itemMasterLazyObjectResolver.fetch(im);
		return im;
	}

	/**
	 * Sets the ProductInfoService for this object to use. This is primarily for testing.
	 *
	 * @param productInfoService The ProductInfoService for this object to use.
	 */
	public void setProductInfoService(ProductInfoService productInfoService) {
		this.productInfoService = productInfoService;
	}

	/**
	 * Sets the UserInfo for this class to use. This is primarily for testing.
	 *
	 * @param userInfo The UserInfo for this class to use.
	 */
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	/**
	 * Sets the NonEmptyParameterValidator for this class to use. This is primarily for testing.
	 *
	 * @param parameterValidator The NonEmptyParameterValidator for this class to use.
	 */
	public void setParameterValidator(NonEmptyParameterValidator parameterValidator) {
		this.parameterValidator = parameterValidator;
	}

    /**
     * Log's a user's request to get all records for a prodId.
     *
     * @param ip The IP address th user is logged in from.
     * @param prodId The item codes the user is searching for.
     */
    private void logGetProdInfo(String ip, long prodId, String logType){
       ProductInfoController.logger.info(String.format(logType, this.userInfo.getUserId(), ip, prodId));
    }

}
