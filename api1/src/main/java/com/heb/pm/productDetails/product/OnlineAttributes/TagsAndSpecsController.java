package com.heb.pm.productDetails.product.OnlineAttributes;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.EntityAttribute;
import com.heb.pm.entity.EntityAttributeCode;
import com.heb.pm.entity.TagsAndSpecsAttribute;
import com.heb.pm.entity.TagsAndSpecsAttributeUpdate;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * This controller is used to get and update dynamic attributes associated with tags and specs attributes
 * @author s753601
 * @version 2.14.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + TagsAndSpecsController.ONLINE_ATTRIBUTES)
@AuthorizedResource(ResourceConstants.ONLINE_ATTRIBUTES_TIER_PRICING)
public class TagsAndSpecsController {

	private static final Logger logger = LoggerFactory.getLogger(TierPricingController.class);

	protected static final String ONLINE_ATTRIBUTES = "/onlineAttributes";
	protected static final String GET_TAGS_AND_SPECS_CHANGES="/getTagsAndSpecs";
	protected static final String GET_TAGS_AND_SPECS_ATTRIBUTES="/getTagsAndSpecsAttributeOptions";
	protected static final String GET_TAGS_AND_SPECS_VALUES="/getTagsAndSpecsValues";
	protected static final String SAVE_TAGS_AND_SPECS_CHANGES="/updateTagsAndSpecs";
	private static final String TAGS_AND_SPECS_REQUEST="User %s from address %s has requested the Tags and Specs attributes for product: %s";
	private static final String TAGS_AND_SPECS_ATTRIBUTE_REQUEST="User %s from address %s has requested the Tags and Specs attributes options";
	private static final String TAGS_AND_SPECS_VALUES_REQUEST="User %s from address %s has requested the Tags and Specs attributes value options";
	private static final String TAGS_AND_SPECS_UPDATE_REQUEST="User %s from address %s has requested to update the " +
			"dynamics attributes (tags and specs) of product with id %s ";
	private static final String TAGS_AND_SPECS_UPDATE_COMPLETION="User %s from address %s has completed an update  to the " +
			"dynamic attributes (tags and specs) of product with id %s";
	private static final String UPDATE_SUCCESS_MESSAGE ="OnlineAttributesTagsAndSpecsController.updateSuccessful";
	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE = "Tags and Specs List for product: %s updated successfully.";

	private LazyObjectResolver<EntityAttribute> resolver = new EntityAttributeResolver();

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private TagsAndSpecsService service;



	private class EntityAttributeResolver implements LazyObjectResolver<EntityAttribute> {

		/**
		 * Currently this fetch is getting:
		 * 1. the key
		 * 2. the Image type
		 * @param
		 */

		@Override
		public void fetch(EntityAttribute entityAttribute) {
			if(entityAttribute.getKey() != null){
				entityAttribute.getKey().getAttributeId();
			}
			if(entityAttribute.getAttribute() != null){
				entityAttribute.getAttribute().getAttributeId();
			}
		}
	}

	/**
	 * This method will return all of the tags and specs attribute currently assigned to a product
	 * @param prodId the product identificaiton code
	 * @param request a request
	 * @return
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = GET_TAGS_AND_SPECS_CHANGES)
	public List<TagsAndSpecsAttribute> getTagsAndSpecs(@RequestParam Long prodId, HttpServletRequest request){
		TagsAndSpecsController.logger.info(String.format(TAGS_AND_SPECS_REQUEST, this.userInfo.getUserId(), request.getRemoteAddr(), prodId.toString()));
		List<TagsAndSpecsAttribute> results = this.service.getTagsAndSpecs(prodId);
		return results;
	}

	/**
	 * This method will return all of the tags and specs attribute currently assigned to a product
	 * @param request a request
	 * @return
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = GET_TAGS_AND_SPECS_ATTRIBUTES)
	public List<EntityAttribute> getTagsAndSpecsAttribute( HttpServletRequest request){
		TagsAndSpecsController.logger.info(String.format(TAGS_AND_SPECS_ATTRIBUTE_REQUEST, this.userInfo.getUserId(), request.getRemoteAddr()));
		List<EntityAttribute> tagsAndSpecsOptions = this.service.getTagsAndSpecsOptions();
		tagsAndSpecsOptions.forEach(this.resolver::fetch);
		return tagsAndSpecsOptions;
	}

	/**
	 * This method will return all of the tags and specs attribute currently assigned to a product
	 * @param request a request
	 * @return
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = GET_TAGS_AND_SPECS_VALUES)
	public List<EntityAttributeCode> getTagsAndSpecsValues(HttpServletRequest request){
		TagsAndSpecsController.logger.info(String.format(TAGS_AND_SPECS_VALUES_REQUEST, this.userInfo.getUserId(), request.getRemoteAddr()));
		List<EntityAttributeCode> tagsAndSpecsOptions = this.service.getTagsAndSpecsValues();
		return tagsAndSpecsOptions;
	}

	/**
	 * This method will return all of the tags and specs attribute currently assigned to a product
	 * @param request a request
	 * @return
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = SAVE_TAGS_AND_SPECS_CHANGES)
	public ModifiedEntity<List<TagsAndSpecsAttribute>> updateTagsAndSpece(@RequestBody TagsAndSpecsAttributeUpdate updates, HttpServletRequest request){
		TagsAndSpecsController.logger.info(String.format(TAGS_AND_SPECS_UPDATE_REQUEST, this.userInfo.getUserId(), request.getRemoteAddr(), updates.getProdId()));
		this.service.updateTagsAndSpecs(updates, this.userInfo.getUserId());
		List<TagsAndSpecsAttribute> toReturn = this.service.getTagsAndSpecs(updates.getProdId());
		String updateMessage = this.messageSource.getMessage(
				TagsAndSpecsController.UPDATE_SUCCESS_MESSAGE,
				new Object[]{updates.getProdId()}, String.format(TagsAndSpecsController.DEFAULT_UPDATE_SUCCESS_MESSAGE, updates.getProdId()), request.getLocale());


		TagsAndSpecsController.logger.info(String.format(TAGS_AND_SPECS_UPDATE_COMPLETION, this.userInfo.getUserId(), request.getRemoteAddr(), updates.getProdId()));
		return new ModifiedEntity<>(toReturn, updateMessage);
	}
}
