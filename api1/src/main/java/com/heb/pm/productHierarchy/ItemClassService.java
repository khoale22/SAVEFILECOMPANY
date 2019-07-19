package com.heb.pm.productHierarchy;

import com.heb.pm.CoreTransactional;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.ItemClass;
import com.heb.pm.index.DocumentWrapperUtil;
import com.heb.pm.repository.ItemClassIndexRepository;
import com.heb.pm.repository.ItemClassRepository;
import com.heb.pm.ws.ProductHierarchyManagementServiceClient;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import com.heb.util.ws.SoapException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all business logic related for item class.
 *
 * @author m314029
 * @since 2.6.0
 */
@Service
public class ItemClassService {

	private static final Logger logger = LoggerFactory.getLogger(ItemClassService.class);
	private static final String ITEM_CLASS_REGULAR_EXPRESSION = "*%s*";
	private static final String ITEM_CLASS_SEARCH_LOG_MESSAGE =
			"searching for item-classes by the regular expression '%s'";
	private static final String USER_EDITABLE_ITEM_CLASS_LOG_MESSAGE =
			"User %s is authorized to update item class: %s.";

	@Autowired
	private ItemClassRepository itemClassRepository;

	@Autowired
	private ProductHierarchyUtils productHierarchyUtils;

	@Autowired
	private ItemClassIndexRepository itemClassIndexRepository;

	@Autowired
	private ProductHierarchyManagementServiceClient productHierarchyManagementServiceClient;

	@Autowired
	private UserInfo userInfo;

	/**
	 * Finds item-classes by page.
	 *
	 * @param pageRequest the page request
	 * @return page of item-classes defined by page request.
	 */
	public Page<ItemClass> findAllByPage(PageRequest pageRequest) {
		Page<ItemClass> subDepartments = this.itemClassRepository.findAll(pageRequest);
		this.productHierarchyUtils.extrapolateSubDepartmentOfItemClassList(subDepartments.getContent());
		return subDepartments;
	}

	/**
	 * Searches for a list of item-classes by a regular expression. This is a wildcard search, meaning that
	 * anything partially matching the text passed in will be returned.
	 *
	 * @param searchString The text to search for classes by.
	 * @param page The page to look for.
	 * @param pageSize The maximum size for the page.
	 * @return A PageableResult with item-classes matching the search criteria.
	 */
	public PageableResult<ItemClass> findItemClassesByRegularExpression(String searchString, int page, int pageSize) {

		String regexString = String.format(ItemClassService.ITEM_CLASS_REGULAR_EXPRESSION,
				searchString.toUpperCase());

		ItemClassService.logger.debug(String.format(ItemClassService.ITEM_CLASS_SEARCH_LOG_MESSAGE, regexString));

		Page<ItemClassDocument> ccp = this.itemClassIndexRepository.findByRegularExpression(regexString,
				new PageRequest(page, pageSize));

		List<ItemClass> itemClasses = new ArrayList<>(ccp.getSize());
		DocumentWrapperUtil.toDataCollection(ccp, itemClasses);

		return new PageableResult<>(page, ccp.getTotalPages(), ccp.getTotalElements(), itemClasses);
	}

	/**
	 * Searches for a specific item-class through the item-class index.
	 *
	 * @param itemClassCode The ID of the item-class searched for.
	 * @return An item-class with the ID requested. Will return null if not found.
	 */
	public ItemClass findOne(String itemClassCode){
		ItemClassDocument itemClassDocument = this.itemClassIndexRepository.findOne(itemClassCode);
		return DocumentWrapperUtil.toData(itemClassDocument);
	}

	/**
	 * Sets the ItemClassIndexRepository for this object to use. This is used for testing.
	 *
	 * @param itemClassIndexRepository The ItemClassIndexRepository for this object to use
	 */
	public void setItemClassIndexRepository(ItemClassIndexRepository itemClassIndexRepository) {
		this.itemClassIndexRepository = itemClassIndexRepository;
	}

	/**
	 * This method calls the product hierarchy management service client to update an item class.
	 *
	 * @param itemClass Item class to update.
	 */
	@CoreTransactional
	public void update(ItemClass itemClass) {
		try {
			this.productHierarchyManagementServiceClient.updateItemClass(this.getUserEditableItemClass(itemClass));
		} catch (Exception e){
			throw  new SoapException(e.getMessage());
		}
	}

	/**
	 * This method takes in the updatable item class from the front end, and returns the item class with the fields
	 * the user has access to edit from that updatable item class, so only the fields the user has access to edit will
	 * be sent to the webservice for updating.
	 *
	 * @param itemClass Item class to update sent from the front end.
	 * @return Item class the user has editable access for.
	 */
	private ItemClass getUserEditableItemClass(ItemClass itemClass) {
		ItemClass userEditableItemClass = new ItemClass();
		userEditableItemClass.setItemClassCode(itemClass.getItemClassCode());
		if(itemClass.getBillCostEligible() != null &&
				this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_HIERARCHY_ITEM_CLASS_BILL_COST))
			userEditableItemClass.setBillCostEligible(itemClass.getBillCostEligible());
		if(itemClass.getGenericClass() != null &&
				this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_HIERARCHY_ITEM_CLASS_GENERIC_CLASS))
			userEditableItemClass.setGenericClass(itemClass.getGenericClass());
		if(!StringUtils.EMPTY.equals(itemClass.getItemClassDescription().trim()) &&
				this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_HIERARCHY_ITEM_CLASS_DESCRIPTION)) {
			userEditableItemClass.setItemClassDescription(itemClass.getItemClassDescription());
		}
		if(itemClass.getMerchantTypeCode() != null &&
				this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_HIERARCHY_ITEM_CLASS_MERCHANT_TYPE)) {
			userEditableItemClass.setMerchantTypeCode(itemClass.getMerchantTypeCode());
		}
		if(itemClass.getScanDepartment() != null &&
				this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_HIERARCHY_ITEM_CLASS_PSS_DEPARTMENT)) {
			userEditableItemClass.setScanDepartment(itemClass.getScanDepartment());
		}
		this.logUserEditableItemClass(userEditableItemClass);
		return userEditableItemClass;
	}

	/**
	 * Logger for the item class the user has permissions to edit.
	 *
	 * @param userEditableItemClass The item class the user has edit permissions for.
	 */
	private void logUserEditableItemClass(ItemClass userEditableItemClass) {
		ItemClassService.logger.info(
				String.format(ItemClassService.USER_EDITABLE_ITEM_CLASS_LOG_MESSAGE,
						this.userInfo.getUserId(), userEditableItemClass.toString())
		);
	}

	/**
	 * Searches for a specific item-class through the item-class repository.
	 *
	 * @param itemClassCode The ID of the item-class searched for.
	 * @return An item-class with the ID requested. Will return null if not found.
	 */
	@CoreTransactional
	public ItemClass findOneByItemClassCode(Integer itemClassCode) {
		return this.itemClassRepository.findOne(itemClassCode);
	}
}
