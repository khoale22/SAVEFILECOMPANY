package com.heb.pm.productHierarchy;

import com.heb.pm.CoreEntityManager;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.*;
import com.heb.pm.index.DocumentWrapperUtil;
import com.heb.pm.repository.SubCommodityIndexRepository;
import com.heb.pm.repository.SubCommodityRepository;
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

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.ArrayList;

/**
 * Holds all business functions related to sub-commodities.
 *
 * @author d116773
 * @since 2.0.2
 */
@Service
public class SubCommodityService {

	private static final Logger logger = LoggerFactory.getLogger(SubCommodityService.class);

	private static final String SUB_COMMODITY_REGULAR_EXPRESSION = "*%s*";
	private static final String SUB_COMMODITY_SEARCH_LOG_MESSAGE =
			"searching for sub-commodities by the regular expression '%s'";
	private static final String USER_EDITABLE_COMMODITY_LOG_MESSAGE =
			"User %s is authorized to update sub-commodity: %s.";
	private static final String JPA_CRITERIA_WILDCARD = "%";
	
	@CoreEntityManager
	@Autowired
	private EntityManager entityManager;

	@Autowired
	private SubCommodityIndexRepository indexRepository;

	@Autowired
	private SubCommodityRepository subCommodityRepository;

	@Autowired
	private ProductHierarchyUtils productHierarchyUtils;

	@Autowired
	private ProductHierarchyManagementServiceClient productHierarchyManagementServiceClient;

	@Autowired
	private UserInfo userInfo;

	/**
	 * Searches for a list of sub-commodities by a regular expression. This is a wildcard search, meaning that
	 * anything partially matching the text passed in will be returned.
	 *
	 * @param searchString The text to search for sub-commodities by.
	 * @param page The page to look for.
	 * @param pageSize The maximum size for the page.
	 * @return A PageableResult with classes matching the search criteria.
	 */
	public PageableResult<SubCommodity> findByRegularExpression(String searchString, int page, int pageSize) {

		String regex = String.format(SubCommodityService.SUB_COMMODITY_REGULAR_EXPRESSION, searchString.toUpperCase());

		SubCommodityService.logger.debug(String.format(SubCommodityService.SUB_COMMODITY_SEARCH_LOG_MESSAGE, regex));

		Page<SubCommodityDocument> scd = this.indexRepository.findByRegularExpression(regex,
				new PageRequest(page, pageSize));

		List<SubCommodity> subCommodities = new ArrayList<>(scd.getSize());
		DocumentWrapperUtil.toDataCollection(scd, subCommodities);

		return new PageableResult<>(page, scd.getTotalPages(), scd.getTotalElements(), subCommodities);
	}

	/**
	 * Searches for a specific sub-commodity.
	 *
	 * @param subCommodityId The ID of the sub-commodity to look for.
	 * @return A SubCommodity with the ID requested.
	 */
	public SubCommodity  findSubCommodity(int subCommodityId) {
		SubCommodityDocument sc = this.indexRepository.findOne(Integer.toString(subCommodityId));
		return DocumentWrapperUtil.toData(sc);
	}

	/**
	 * Sets the SubCommodityIndexRepository for the object to use. This is mainly for testing.
	 *
	 * @param indexRepository The SubCommodityIndexRepository for the object to use.
	 */
	public void setIndexRepository(SubCommodityIndexRepository indexRepository) {
		this.indexRepository = indexRepository;
	}

	/**
	 * Finds sub-commodities by page.
	 *
	 * @param pageRequest the page request
	 * @return page of sub-commodity defined by page request.
	 */
	public Page<SubCommodity> findAllSubCommoditiesByPage(PageRequest pageRequest) {
		Page<SubCommodity> subCommodityPage =
				this.subCommodityRepository.findAll(pageRequest);
		for(SubCommodity subCommodity : subCommodityPage.getContent()){
			subCommodity.getCommodityMaster().getKey().getCommodityCode();
			subCommodity.getCommodityMaster().getItemClassMaster().getItemClassCode();
			this.productHierarchyUtils.extrapolateSubDepartmentOfItemClass(
					subCommodity.getCommodityMaster().getItemClassMaster());
		}
		return subCommodityPage;
	}

	/**
	 * This method calls the product hierarchy management service client to update a subCommodity.
	 *
	 * @param subCommodity SubCommodity to update.
	 */
	public void update(SubCommodity subCommodity) {
		try {
			this.productHierarchyManagementServiceClient.
					updateSubCommodity(this.getUserEditableSubCommodity(subCommodity));
		} catch (Exception e){
			throw  new SoapException(e.getMessage());
		}
	}

	/**
	 * Searches for a specific sub-commodity by key through the sub-commodity repository.
	 *
	 * @param key The key of the sub-commodity searched for.
	 * @return A sub-commodity with the key requested. Will return null if not found.
	 */
	public SubCommodity findOne(SubCommodityKey key) {
		return this.subCommodityRepository.findOne(key);
	}

	/**
	 * This method takes in the updatable subCommodity from the front end, and returns the subCommodity with the fields
	 * the user has access to edit from that updatable subCommodity, so only the fields the user has access to edit will
	 * be sent to the webservice for updating.
	 *
	 * @param subCommodity SubCommodity to update sent from the front end.
	 * @return SubCommodity the user has editable access for.
	 */
	private SubCommodity getUserEditableSubCommodity(SubCommodity subCommodity) {
		SubCommodity userEditableSubCommodity = new SubCommodity();
		userEditableSubCommodity.setKey(new SubCommodityKey(subCommodity.getKey()));
		if(subCommodity.getName() != null && !StringUtils.EMPTY.equals(subCommodity.getName().trim()) &&
				this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_HIERARCHY_SUB_COMMODITY_DESCRIPTION)) {
			userEditableSubCommodity.setName(subCommodity.getName());
		}
		if(subCommodity.getProductCategoryId() != null &&
				this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_HIERARCHY_SUB_COMMODITY_PRODUCT_CATEGORY_ID)) {
			userEditableSubCommodity.setProductCategoryId(subCommodity.getProductCategoryId());
		}
		if(subCommodity.getSubCommodityActive() != null &&
				this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_HIERARCHY_SUB_COMMODITY_ACTIVE)) {
			userEditableSubCommodity.setSubCommodityActive(subCommodity.getSubCommodityActive());
		}
		if(subCommodity.getImsCommodityCode() != null &&
				this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_HIERARCHY_SUB_COMMODITY_IMS_CODE_COMMODITY)) {
			userEditableSubCommodity.setImsCommodityCode(subCommodity.getImsCommodityCode());
		}
		if(subCommodity.getImsSubCommodityCode() != null &&
				this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_HIERARCHY_SUB_COMMODITY_IMS_CODE_COMMODITY)) {
			userEditableSubCommodity.setImsSubCommodityCode(subCommodity.getImsSubCommodityCode());
		}
		if(subCommodity.getFoodStampEligible() != null &&
				this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_HIERARCHY_SUB_COMMODITY_FOOD_STAMP_ELIGIBLE)) {
			userEditableSubCommodity.setFoodStampEligible(subCommodity.getFoodStampEligible());
		}
		if(subCommodity.getTaxEligible() != null &&
				this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_HIERARCHY_SUB_COMMODITY_TAXABLE)) {
			userEditableSubCommodity.setTaxEligible(subCommodity.getTaxEligible());
		}
		if(subCommodity.getTaxCategoryCode() != null &&
				this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_HIERARCHY_SUB_COMMODITY_TAXABLE_TAX_CATEGORY)) {
			userEditableSubCommodity.setTaxCategoryCode(subCommodity.getTaxCategoryCode().isEmpty() ? " " : subCommodity.getTaxCategoryCode());
		}
		if(subCommodity.getNonTaxCategoryCode() != null &&
				this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_HIERARCHY_SUB_COMMODITY_NON_TAXABLE_TAX_CATEGORY)) {
			userEditableSubCommodity.setNonTaxCategoryCode(subCommodity.getNonTaxCategoryCode().isEmpty() ? " " : subCommodity.getNonTaxCategoryCode());
		}
		this.logUserEditableSubCommodity(userEditableSubCommodity);
		return userEditableSubCommodity;
	}

	/**
	 * Logger for the subCommodity the user has permissions to edit.
	 *
	 * @param userEditableSubCommodity The subCommodity the user has edit permissions for.
	 */
	private void logUserEditableSubCommodity(SubCommodity userEditableSubCommodity) {
		SubCommodityService.logger.info(
				String.format(SubCommodityService.USER_EDITABLE_COMMODITY_LOG_MESSAGE,
						this.userInfo.getUserId(), userEditableSubCommodity.toString())
		);
	}

	/**
	 * Finds all sub commodities sharing the given commodity code.
	 *
	 * @param commodityCode Commodity code to search for.
	 * @return List of sub commodities matching search.
	 */
	public List<SubCommodity> findByCommodity(Integer commodityCode) {
		return this.subCommodityRepository.findByKeyCommodityCode(commodityCode);
	}
	
	/**
	 * Finds sub-commodities by page.
	 *
	 * @param page The page to look for.
	 * @param pageSize The maximum size for the page.
	 * @return A list of sub commodities.
	 */
	public List<SubCommodity> findAllSubCommoditiesByPageRequest(int page, int pageSize) {
		Page<SubCommodity> subCommodityPage = this.subCommodityRepository.findAll(new PageRequest(page, pageSize));
		return subCommodityPage.getContent();
	}
	
	/**
	 * Find sub commodities by search text.
	 * @param searchText the text to find by
	 * @param page page number
	 * @param pageSize the page size
	 * @return List of sub commodities matching search.
	 */
	public PageableResult<SubCommodity> findSubCommoditiesBySearchText(String searchText, int page, int pageSize){
		PageableResult<SubCommodity> results;
        // Get the objects needed to build the query.
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		// Builds the criteria for the main query.
		CriteriaQuery<SubCommodity> queryBuilder = criteriaBuilder.createQuery(SubCommodity.class);
		// Select from product brand
		Root<SubCommodity> pmRoot = queryBuilder.from(SubCommodity.class);
        // Build query.
		queryBuilder.select(pmRoot);

		Expression<String> literal = criteriaBuilder.literal(JPA_CRITERIA_WILDCARD.concat(searchText.toUpperCase()).concat(JPA_CRITERIA_WILDCARD));
		queryBuilder.where(criteriaBuilder.or(
		        criteriaBuilder.like(criteriaBuilder.upper(pmRoot.get(SubCommodity_.name)),literal),
                criteriaBuilder.like(criteriaBuilder.upper(pmRoot.get(SubCommodity_.subCommodityCode).as(String.class)),literal)
                ));
		queryBuilder.orderBy(criteriaBuilder.desc(criteriaBuilder.selectCase()
				.when(criteriaBuilder.like(criteriaBuilder.upper(criteriaBuilder.trim(pmRoot.get(SubCommodity_.name))),
                        searchText.toUpperCase().concat(JPA_CRITERIA_WILDCARD)),1)
                .when(criteriaBuilder.equal(criteriaBuilder.trim(pmRoot.get(SubCommodity_.subCommodityCode).as(String.class)), searchText.toUpperCase()),1)
				.otherwise(0)),criteriaBuilder.asc(pmRoot.get(SubCommodity_.name)));
		TypedQuery<SubCommodity> pmTQuery = this.entityManager.createQuery(queryBuilder);
		pmTQuery.setFirstResult(page).setMaxResults(pageSize);
		List<SubCommodity> listSubCommodity = pmTQuery.getResultList();
		results = new PageableResult<>(page, listSubCommodity);
		return results;
	}
}
