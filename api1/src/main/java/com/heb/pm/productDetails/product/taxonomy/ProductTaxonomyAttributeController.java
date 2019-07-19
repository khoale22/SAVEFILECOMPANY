package com.heb.pm.productDetails.product.taxonomy;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.User;
import com.heb.pm.repository.LdapSearchRepository;
import com.heb.pm.ws.EmailServiceClient;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Rest endpoint for product taxonomy.
 *
 * @author m314029
 * @since 2.18.4
 */
@RestController()
@RequestMapping(ProductTaxonomyAttributeController.ROOT_URL)
@AuthorizedResource(ResourceConstants.PRODUCT_TAXONOMY_ATTRIBUTES)
public class ProductTaxonomyAttributeController {

	private static final Logger logger = LoggerFactory.getLogger(ProductTaxonomyAttributeController.class);
	protected static final String ROOT_URL = ApiConstants.BASE_APPLICATION_URL + "/productTaxonomy/attribute";

	//urls
	private static final String GET_BY_PRODUCT_ID_AND_PRODUCT_TYPE_URL = "getByProductIdAndProductType";
	private static final String GET_ATTRIBUTE_VALUES_URL = "getAttributeValues";
	private static final String UPDATE_URL = "update";
	private static final String REQUEST_URL = "request-new";

	// logs
	private static final String GET_BY_PRODUCT_ID_AND_PRODUCT_TYPE_LOG_MESSAGE = "User %s from IP %s has requested " +
			"attributes for product id: %d and product type: %s.";
	private static final String GET_PRODUCT_ATTRIBUTES_VALUES =
			"User %s from IP %s has requested the product attributes for attribute with id: %d.";
	private static final String GET_ATTRIBUTES_VALUES_UPDATE =
			"User %s from IP %s has requested updates for the product attributes for product id: %d and product type: %s.";
	private static final String ATTRIBUTE_UPDATE_COMPLETE =
			"User %s from IP %s has completed updates for the product attributes for product id: %d and product type: %s.";
	private static final String REQUEST_NEW_VALUES =
			"User %s from IP %s has requested new attribute values for attribute with id: %d and name: %s.";
    private static final String REQUEST_NEW_VALUES_ERROR =
            "Error sending email for requesting new attribute values with id: %d and name: %s. Error: %s.";

	// error messages
	private static final String NO_PRODUCT_ID_MESSAGE = "Product ID cannot be null.";
	private static final String NO_PROD_ID_MESSAGE_KEY = "ProductTaxonomyAttributeController.missingProductId";
	private static final String NO_PRODUCT_TYPE_MESSAGE = "Product type cannot be null.";
	private static final String NO_PROD_TYPE_MESSAGE_KEY = "ProductTaxonomyAttributeController.missingProductType";
	private static final String NO_ATTRIBUTE_ID_MESSAGE = "Attribute ID cannot be null.";
	private static final String NO_ATTRIBUTE_ID_MESSAGE_KEY = "ProductTaxonomyAttributeController.missingAttributeId";

	// email
    public static final String DO_NOT_REPLY_HEB_COM = "do-not-reply@heb.com";
    public static final String SUBJECT_REQUEST_FOR_NEW_ATTRIBUTE_VALUES = "New Attribute Values Request";
	public static final String REQUESTED_FULL_NAME = "Requested Full name: ";
    public static final String REQUESTED_USER_ID = "Requested User ID: ";
    public static final String REQUESTED_EMAIL = "Requested Email ID: ";
    public static final String REQUESTED_PHONE = "Requested Phone #: ";
    public static final String ATTRIBUTE_ID = "Attribute id: ";
    public static final String ATTRIBUTE_NAME = "Attribute name: ";
    public static final String ATTRIBUTE_VALUES = "Attribute values: ";
    public static final String NOTE = "Note: ";
    public static final String MESSAGE_SUCCESSFULLY_SENT_EMAIL_REQUEST_FOR_NEW_ATTRIBUTE_VALUES = "{ \"message\" : \"Successfully sent email request for new attribute values\"}";
    public static final String MESSAGE_UNABLE_TO_SEND_EMAIL_REQUEST_FOR_NEW_ATTRIBUTE_VALUES = "{ \"message\" : \"Unable to send email request for new attribute values\"}";

	@Autowired
	private NonEmptyParameterValidator parameterValidator;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private ProductTaxonomyAttributeService service;

	@Autowired
	LdapSearchRepository ldapSearchRepository;

    @Autowired
    private EmailServiceClient emailService;

    @Value("${email.toAddress}")
    private String toAddress;

	/**
	 * Gets product attributes by product id and product type.
	 *
	 * @param productId Product id to search for.
	 * @param productType Product type to search for.
	 * @param request The http request.
	 * @return List of product attributes matching the given product id and product type.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ProductTaxonomyAttributeController.GET_BY_PRODUCT_ID_AND_PRODUCT_TYPE_URL)
	public List<ProductAttribute> getByProductIdAndProductType(
			@RequestParam Long productId, @RequestParam String productType, HttpServletRequest request) {
		this.parameterValidator.validate(productId,
				NO_PRODUCT_ID_MESSAGE, NO_PROD_ID_MESSAGE_KEY, request.getLocale());
		this.parameterValidator.validate(productType,
				NO_PRODUCT_TYPE_MESSAGE, NO_PROD_TYPE_MESSAGE_KEY, request.getLocale());
		ProductTaxonomyAttributeController.logger.info(
				String.format(GET_BY_PRODUCT_ID_AND_PRODUCT_TYPE_LOG_MESSAGE, this.userInfo
						.getUserId(), request.getRemoteAddr(), productId, productType));
		return this.service.getByProductIdAndProductType(productId, productType);
	}


	/**
	 * Get Product attribute values.
	 *
	 * @param request the request.
	 * @return the attribute values.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = GET_ATTRIBUTE_VALUES_URL)
	public  List<ProductAttributeValue> getProductAttributeValues(@RequestParam Long attributeId, HttpServletRequest request){
		this.parameterValidator.validate(attributeId,
				NO_ATTRIBUTE_ID_MESSAGE, NO_ATTRIBUTE_ID_MESSAGE_KEY, request.getLocale());
		ProductTaxonomyAttributeController.logger.info(
				String.format(GET_PRODUCT_ATTRIBUTES_VALUES, this.userInfo.getUserId(), request.getRemoteAddr(), attributeId));
		return this.service.getProductAttributeValues(attributeId);
	}

	/**
	 * Update Product attribute values.
	 *
	 * @param request the request.
	 * @return the attribute values.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = UPDATE_URL)
	public void update(@RequestBody ProductAttributeHeader productAttributeHeader, HttpServletRequest request){
		this.parameterValidator.validate(productAttributeHeader.getProductId(),
				NO_PRODUCT_ID_MESSAGE, NO_PROD_ID_MESSAGE_KEY, request.getLocale());
		this.parameterValidator.validate(productAttributeHeader.getProductType(),
				NO_PRODUCT_TYPE_MESSAGE, NO_PROD_TYPE_MESSAGE_KEY, request.getLocale());
		ProductTaxonomyAttributeController.logger.info(
				String.format(GET_ATTRIBUTES_VALUES_UPDATE, this.userInfo.getUserId(),
				request.getRemoteAddr(), productAttributeHeader.getProductId(), productAttributeHeader.getProductType()));
		this.service.update(productAttributeHeader);
		ProductTaxonomyAttributeController.logger.info(String.format(ATTRIBUTE_UPDATE_COMPLETE, this.userInfo.getUserId(),
				request.getRemoteAddr(), productAttributeHeader.getProductId(), productAttributeHeader.getProductType()));
	}

    /**
     * Send request for new attribute values.
     *
     * @param productAttribute attribute the new values are for
     * @param request the request
     * @return response
     */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = REQUEST_URL)
	public ResponseEntity requestNewAttributeValuesForAttribute(@RequestBody ProductAttribute productAttribute, HttpServletRequest request) {
		ProductTaxonomyAttributeController.logger.info(String.format(REQUEST_NEW_VALUES, this.userInfo.getUserId(),
				request.getRemoteAddr(), productAttribute.getAttributeId(), productAttribute.getAttributeName()));
		try {
			String body = createMessageBody(productAttribute);

            emailService.sendMail(
            		DO_NOT_REPLY_HEB_COM, toAddress, null,
                    SUBJECT_REQUEST_FOR_NEW_ATTRIBUTE_VALUES,
                    body);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(MESSAGE_SUCCESSFULLY_SENT_EMAIL_REQUEST_FOR_NEW_ATTRIBUTE_VALUES);

        } catch (Exception e) {
            ProductTaxonomyAttributeController.logger.error(String.format(REQUEST_NEW_VALUES_ERROR, productAttribute.getAttributeId(), productAttribute.getAttributeName(), e.toString()));

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MESSAGE_UNABLE_TO_SEND_EMAIL_REQUEST_FOR_NEW_ATTRIBUTE_VALUES);
        }
	}

	/**
	 * Create the email message body
	 *
	 * @param productAttribute attribute the new values are for
	 * @return message body
	 * @throws Exception
	 */
	private String createMessageBody(@RequestBody ProductAttribute productAttribute) throws Exception {
		ArrayList<String> users = new ArrayList<>();
		users.add(this.userInfo.getUserId());

		List<User> userDetails = ldapSearchRepository.getUserList(users);
		User user = userDetails.get(0);

		StringBuilder body = new StringBuilder();

		body.append(REQUESTED_USER_ID + this.userInfo.getUserId() + "\n");

		if (user.getMail() != null) {
		    body.append(REQUESTED_EMAIL + user.getMail() + "\n");
        }

        if (user.getFullName() != null) {
            body.append( REQUESTED_FULL_NAME + user.getFullName() + "\n");
        }

        if (user.getTelephoneNumber() != null) {
            body.append( REQUESTED_PHONE + user.getTelephoneNumber() + "\n");
        }
        body.append("\n");

        body.append(ATTRIBUTE_ID + productAttribute.getAttributeId() + "\n");
        body.append(ATTRIBUTE_NAME + productAttribute.getAttributeName() + "\n");
        body.append(ATTRIBUTE_VALUES + productAttribute.getNewValues() + "\n");
        body.append("\n");
        
        body.append(NOTE + productAttribute.getNote());

		return body.toString();
	}
}
