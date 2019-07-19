/*
 *  ProductGroupService
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productGroup;

import com.heb.pm.CoreEntityManager;
import com.heb.pm.entity.*;
import com.heb.pm.repository.CustomerProductGroupRepository;
import com.heb.pm.repository.GenericEntityRelationshipRepository;
import com.heb.pm.repository.GenericEntityRepository;
import com.heb.util.jpa.PageableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This service will implement any function on product group screen.
 *
 * @author vn87351
 * @updated vn86116
 * @since 2.15.0
 */
@Service
public class ProductGroupSearchService {

	@CoreEntityManager
	@Autowired
	private EntityManager entityManager;

	@Autowired
	private GenericEntityRelationshipRepository genericEntityRelationshipRepository;

	@Autowired
	private CustomerProductGroupRepository customerProductGroupRepository;

	@Autowired
	private GenericEntityRepository genericEntityRepository;

	private CustomerProductGroupResolver customerProductGroupResolver = new CustomerProductGroupResolver();

	private static final String JPA_CRITERIA_WILDCARD = "%";

	private static final int MAX_ELEMENT = 1000;

	private static final String ENTY_TYPE_CODE_CUSTH = "CUSTH";

	private static final String KEY_IS_LOWEST_LEVEL = "isLowestLevel";

	private static final int NUMBER_0 = 0;

	private static final int NUMBER_1 = 1;

	/**
	 * Method to search for product group.
	 *
	 * @param page the number of page.
	 * @param pageSize the number elements per page.
	 * @param productGroupId the product group id.
	 * @param productGroupName the product group name.
	 * @param productGroupType the product group type.
	 * @param firstSearch      the variable used to know is first search or not.
	 * @return PageableResult<CustomerProductGroup> the result search.
	 */
	public PageableResult<CustomerProductGroup> findProductGroup(int page, int pageSize, Long productGroupId, String productGroupName, String productGroupType, Long customerHierarchyId, boolean firstSearch) {
		// Get the objects needed to build the query.
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		// Builds the criteria for the main query.
		CriteriaQuery<CustomerProductGroup> queryBuilder = criteriaBuilder.createQuery(CustomerProductGroup.class);
		// Select from customer product group.
		Root<CustomerProductGroup> pmRoot = queryBuilder.from(CustomerProductGroup.class);
		// Build query.
		queryBuilder.select(pmRoot);
		queryBuilder.orderBy(criteriaBuilder.asc(pmRoot.get(CustomerProductGroup_.custProductGroupId)));
		if (productGroupId != null || productGroupName != null || productGroupType != null || customerHierarchyId != null) {
			if (createPredicate(criteriaBuilder, productGroupId, productGroupName, customerHierarchyId, productGroupType, pmRoot) != null){
				queryBuilder.where(createPredicate(criteriaBuilder, productGroupId, productGroupName, customerHierarchyId, productGroupType, pmRoot));
			}
			else {
				long count = 0;
				return new PageableResult<>(page, 0, count, null);
			}
		}
			// Execute the query.
			TypedQuery<CustomerProductGroup> pmTQuery = this.entityManager.createQuery(queryBuilder);
			pmTQuery.setFirstResult(page * pageSize).setMaxResults(pageSize);
			List<CustomerProductGroup> results = pmTQuery.getResultList();
			if (!results.isEmpty()) {
				setLowestEntity(results);
				customerProductGroupResolver.fetch(results);
			}
			if (firstSearch) {
				// It's a new query to count total element when is first search.
				CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
				countQuery.select(criteriaBuilder.count(countQuery.from(CustomerProductGroup.class)));
				if (productGroupId != null || productGroupName != null || productGroupType != null || customerHierarchyId != null) {
					countQuery.where(createPredicate(criteriaBuilder, productGroupId, productGroupName, customerHierarchyId, productGroupType, pmRoot));
				}
				// Run the query
				TypedQuery<Long> countTQuery = this.entityManager.createQuery(countQuery);
				long count = countTQuery.getSingleResult();
				return new PageableResult<>(page, 0, count, results);
			} else {
				return new PageableResult<>(page, results);
			}
	}

	/**
	 * Method to set lowest entity for customer product group.
	 *
	 * @param customerProductGroups list of customer product Group.
	 */
	private void setLowestEntity(List<CustomerProductGroup> customerProductGroups){
		for (CustomerProductGroup cust : customerProductGroups) {
			GenericEntity genericEntity = this.genericEntityRepository.findTop1ByDisplayNumberAndType(cust.getCustProductGroupId(), GenericEntity.EntyType.PGRP.getName());
			if (genericEntity != null) {
				cust.setGenericEntity(genericEntity);
				GenericEntityRelationship rlsh = genericEntityRelationshipRepository.findTop1ByKeyChildEntityIdAndHierarchyContextAndDefaultParent(
						cust.getGenericEntity().getId(), HierarchyContext.HierarchyContextCode.CUST.getName(), true);
				if (rlsh != null) {
					if(rlsh.getGenericParentEntity().getDisplayNumber()>0){
						rlsh.getGenericParentEntity().setHierarchyPathDisplay(rlsh.getParentDescription().getLongDescription());
					}else{
						rlsh.getGenericParentEntity().setHierarchyPathDisplay(rlsh.getGenericParentEntity().getDisplayText());
					}
					cust.setLowestEntity(rlsh.getGenericParentEntity());
				}
			}
		}
	}

	/**
	 * Find product group ids.
	 *
	 * @param productGroupId    the product group id.
	 * @param productGroupName  the product group name.
	 * @param productGroupType  the product group type.
	 * @return list of product group ids.
	 */
	public List<Long> findProductGroupIds(Long productGroupId, String productGroupName, String productGroupType, Long customerHierarchyId) {
		// Get the objects needed to build the query.
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		// Builds the criteria for the main query
		CriteriaQuery<CustomerProductGroup> queryBuilder = criteriaBuilder.createQuery(CustomerProductGroup.class);
		Root<CustomerProductGroup> pmRoot = queryBuilder.from(CustomerProductGroup.class);
		// Select id from customer product group.
		queryBuilder.select(pmRoot.get("custProductGroupId"));
		Predicate predicate = createPredicate(criteriaBuilder, productGroupId, productGroupName, customerHierarchyId, productGroupType, pmRoot);
		if (predicate != null){
			queryBuilder.where(predicate);
		}
		queryBuilder.orderBy(criteriaBuilder.asc(pmRoot.get(CustomerProductGroup_.custProductGroupId)));
		Query query = this.entityManager.createQuery(queryBuilder);
		return query.getResultList();
	}

	/**
	 * Create Predicate for query.
	 *
	 * @param criteriaBuilder  the criteria builder
	 * @param productGroupId   the product group id.
	 * @param productGroupName the product group name.
	 * @param productGroupType the product group type.
	 * @param pmRoot           the Customer Product Group Root.
	 * @return predicate for query.
	 */
	private Predicate createPredicate(CriteriaBuilder criteriaBuilder, Long productGroupId, String productGroupName, Long customerHierarchyId, String productGroupType, Root<CustomerProductGroup> pmRoot) {
		Predicate predicate = null;
		if (productGroupId != null && productGroupId > 0) {
			predicate = criteriaBuilder.equal(pmRoot.get(CustomerProductGroup_.custProductGroupId), productGroupId);
		}
		if (productGroupName != null) {
			Expression<String> literal = criteriaBuilder.literal(JPA_CRITERIA_WILDCARD.concat(productGroupName.toUpperCase()).concat(JPA_CRITERIA_WILDCARD));
			predicate = (criteriaBuilder.like(criteriaBuilder.upper(pmRoot.get(CustomerProductGroup_.custProductGroupName)), literal));
		} else if (productGroupType != null) {
			predicate = criteriaBuilder.equal(pmRoot.get(CustomerProductGroup_.productGroupTypeCode),
					productGroupType);
		} else if (customerHierarchyId != null) {
			List<Long> parentEntityIds = new ArrayList<>();
			if (genericEntityRepository.findOne(customerHierarchyId) != null) {
				parentEntityIds = genericEntityRepository.findGenericEntityIdsByDisplayName(genericEntityRepository.findOne(customerHierarchyId).getDisplayText());
			}
			if (parentEntityIds != null && !parentEntityIds.isEmpty()) {
				List<Long> childrenEntityIds =
						this.genericEntityRelationshipRepository.findChildEntityIdsByKeyParentEntityIds
								(parentEntityIds);
				//Check Collection in Predicate can't over 1000 records.
				if (!childrenEntityIds.isEmpty() && childrenEntityIds.size() <= MAX_ELEMENT) {
					List<Long> displayNumbers =
							this.genericEntityRepository.findDisplayNumberByIds(childrenEntityIds);
					if (!displayNumbers.isEmpty()) {
						predicate = pmRoot.get(CustomerProductGroup_.custProductGroupId).in(displayNumbers);
					}
				}
				//Handle predicate when childrenEntityIds Collection over 1000 records.
				else if (!childrenEntityIds.isEmpty()) {
					List<List<Long>> listDisplayNumbers = new ArrayList<>();
					for (int i = NUMBER_0; i <= childrenEntityIds.size() / MAX_ELEMENT; i++) {
						if (i < childrenEntityIds.size() / MAX_ELEMENT) {
							listDisplayNumbers.add(this.genericEntityRepository.findDisplayNumberByIds(childrenEntityIds.subList(i * MAX_ELEMENT, i * MAX_ELEMENT + MAX_ELEMENT)));
						} else {
							listDisplayNumbers.add(this.genericEntityRepository.findDisplayNumberByIds(childrenEntityIds.subList(i * MAX_ELEMENT, childrenEntityIds.size())));
						}
					}
					if (!listDisplayNumbers.isEmpty()) {
						predicate = pmRoot.get(CustomerProductGroup_.custProductGroupId).in(listDisplayNumbers.get(0));
						for (int i = NUMBER_1; i < listDisplayNumbers.size(); i++) {
							predicate = criteriaBuilder.or(predicate, pmRoot.get(CustomerProductGroup_.custProductGroupId).in(listDisplayNumbers.get(i)));
						}
					}
				}
			}
		}
			return predicate;
		}

	/**
	 * Find Product Group By Product group id.
	 *
	 * @param productGroupId id of product group
	 * @return CustomerProductGroup object
	 */
	public CustomerProductGroup findProductGroupById(Long productGroupId){
		return customerProductGroupRepository.findOne(productGroupId);
	}

	/**
	 * Method to check lowest level.
	 *
	 * @param lowestLevelId the lowest level id.
	 * @return result check.
	 */
	public Map<String, Boolean> checkLowestLevel (Long lowestLevelId) {
		Boolean result = false;
		if (lowestLevelId != null){
			if (!genericEntityRelationshipRepository.findChildEntityIdsByKeyParentEntityId(lowestLevelId).isEmpty()) {
				List<Long> childEntityIds = genericEntityRelationshipRepository.findChildEntityIdsByKeyParentEntityId(lowestLevelId);
				CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
				CriteriaQuery<GenericEntity> queryBuilder = criteriaBuilder.createQuery(GenericEntity.class);
				Root<GenericEntity> pmRoot = queryBuilder.from(GenericEntity.class);
				queryBuilder.select(pmRoot);
				Expression<String> literal = criteriaBuilder.literal(ENTY_TYPE_CODE_CUSTH);
				//Check Collection in Predicate can't over 1000 records.
				if (childEntityIds.size()<=MAX_ELEMENT) {
					queryBuilder.where(criteriaBuilder.and(pmRoot.get(GenericEntity_.id).in(childEntityIds)), criteriaBuilder.notEqual(pmRoot.get(GenericEntity_.type), literal));
					TypedQuery<GenericEntity> countTQuery = this.entityManager.createQuery(queryBuilder);
					if (countTQuery.getResultList().size() > NUMBER_0) {
						result = true;
					}
				}
				//Handle predicate when childEntityIds Collection over 1000 records.
				else {
					List<List<Long>> listchildEntityIds = new ArrayList<>();
					for (int i = NUMBER_0;i<=childEntityIds.size()/MAX_ELEMENT ;i++){
						if(i<childEntityIds.size()/MAX_ELEMENT ) {
							listchildEntityIds.add(this.genericEntityRepository.findDisplayNumberByIds(childEntityIds.subList(i * MAX_ELEMENT , i * MAX_ELEMENT  + MAX_ELEMENT )));
						}
						else {
							listchildEntityIds.add(this.genericEntityRepository.findDisplayNumberByIds(childEntityIds.subList(i*MAX_ELEMENT ,childEntityIds.size())));
						}
					}
					Predicate predicate = pmRoot.get(GenericEntity_.id).in(listchildEntityIds.get(NUMBER_0));
					for (int i = NUMBER_1;i<listchildEntityIds.size();i++) {
						predicate = criteriaBuilder.or(predicate,pmRoot.get(GenericEntity_.id).in(listchildEntityIds.get(i)));
					}
					queryBuilder.where(predicate , criteriaBuilder.notEqual(pmRoot.get(GenericEntity_.type), literal));
					TypedQuery<GenericEntity> countTQuery = this.entityManager.createQuery(queryBuilder);
					if (countTQuery.getResultList().size() > NUMBER_0) {
						result = true;
					}
				}
			}
		}
		return Collections.singletonMap(KEY_IS_LOWEST_LEVEL, result);
	}

	/**
	 * Returns a customer product group matching the id given.
	 *
	 * @param customerProductGroupId Customer product group id to look for.
	 * @return CustomerProductGroup matching the given id.
	 */
	public CustomerProductGroup findCustomerProductGroupById(Long customerProductGroupId) {
			return this.customerProductGroupRepository.findOne(customerProductGroupId);
	}

	/**
	 * Returns a list of all customer product groups not in a given list of customer product group ids.
	 *
	 * @param excludedCustomerProductGroupIds Customer product group ids to not include in repo call.
	 * @return All customer product groups that do not match the given list of excluded ids.
	 */
	public List<CustomerProductGroup> findCustomerProductGroupsNotIn(List<Long> excludedCustomerProductGroupIds) {
		if(excludedCustomerProductGroupIds != null && !excludedCustomerProductGroupIds.isEmpty()) {
			return this.customerProductGroupRepository.findByCustProductGroupIdNotInOrderByCustProductGroupId(excludedCustomerProductGroupIds);
		} else {
			Sort customerProductGroupIdSort = new Sort(new Sort.Order(Sort.Direction.ASC, "custProductGroupId"));
			return this.customerProductGroupRepository.findAll(customerProductGroupIdSort);
		}
	}
}
