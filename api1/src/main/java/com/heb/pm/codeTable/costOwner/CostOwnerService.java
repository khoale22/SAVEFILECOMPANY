/*
 *  CostOwnerService
 *
 *  Copyright (c) 2019 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.costOwner;

import com.heb.pm.entity.CostOwner;
import com.heb.pm.repository.CostOwnerRepository;
import com.heb.pm.repository.CostOwnerRepositoryWithCount;
import com.heb.pm.repository.SellingUnitRepository;
import com.heb.util.jpa.PageableResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all business logic related to code table cost owner.
 *
 * @author vn70529
 * @since 2.41.0
 **/
@Service
public class CostOwnerService {

	private static final String CST_OWN_USED_IN_CODE_TABLE_ERROR_MESSAGE = "Unable to delete - In relationship code table";
	private static final String ITEM_TIED_ERROR_MESSAGE = "Unable to delete - Items tied";
	private static final String CST_OWN_USED_IN_CODE_TABLE_AND_ITEM_ERROR_MESSAGE = "Unable to delete - In relationship code table and items tied";
	public static final String COST_OWNER_EXIST_ERROR_MESSAGE = "Cost Owner already exists.";
	private static final String ALREADY_EXIST_ERROR_MESSAGE = "%s already exists.";

	@Autowired
	private CostOwnerRepository costOwnerRepository;

	@Autowired
	private CostOwnerRepositoryWithCount costOwnerRepositoryWithCount;

	@Autowired
	private SellingUnitRepository sellingUnitRepository;

	/**
	 * Get all records of CST_OWN TABLE table by pagination.
	 *
	 * @param id           The id of cost owner.
	 * @param name         The name of cost owner.
	 * @param page         The page number.
	 * @param pageSize     The page size.
	 * @param includeCount The flag checking include count or not.
	 * @return the page of cost owners.
	 */
	public PageableResult<CostOwner> findCostOwners(String id, String name,
	                                                int page, int pageSize, boolean includeCount) {

		Pageable pageRequest = new PageRequest(page, pageSize, CostOwner.getDefaultSort());
		PageableResult<CostOwner> results;
		if (includeCount) {
			Page<CostOwner> costOwners = this.findCostOwnersWithCount(id, name, pageRequest);
			results = new PageableResult<>(pageRequest.getPageNumber(), costOwners.getTotalPages(),
					costOwners.getTotalElements(), costOwners.getContent());
		} else {
			List<CostOwner> costOwners = this.findCostOwnersWithoutCount(id, name, pageRequest);
			results = new PageableResult<>(pageRequest.getPageNumber(), costOwners);
		}
		return results;
	}

	/**
	 * Add new cost owners.
	 *
	 * @param costOwners The cost owner to add.
	 * @return the newly added cost owners.
	 */
	public List<CostOwner> addCostOwners(List<CostOwner> costOwners) {

		List<CostOwner> newCostOwners = new ArrayList<>();
		if (CollectionUtils.isEmpty(costOwners)) {
			return newCostOwners;
		}

		Integer maxId = this.costOwnerRepository.findTop1ByOrderByCostOwnerIdDesc().getCostOwnerId();
		for (CostOwner costOwner : costOwners) {
			costOwner.setTopToTopId(CostOwner.DEFAULT_T2T_ID);
			costOwner.setCostOwnerId(++maxId);
			newCostOwners.add(costOwner);
		}

		return this.costOwnerRepository.save(newCostOwners);
	}

	/**
	 * Save an updated cost owner. Throw exception if cost owner name is already in use.
	 *
	 * @param costOwner The cost owner to be updated.
	 * @return the updated cost owner.
	 */
	public CostOwner updateCostOwner(CostOwner costOwner) {

		List<CostOwner> costOwners =
				this.costOwnerRepository.findByTrimmedCostOwnerNameIgnoreCase(costOwner.getCostOwnerName());
		if (CollectionUtils.isNotEmpty(costOwners)) {
			// In case DB has duplicate cost owner names before, need to loop all
			for (CostOwner duplicateCostOwner : costOwners) {
				if (!duplicateCostOwner.getCostOwnerId().equals(costOwner.getCostOwnerId())) {
					throw new IllegalArgumentException(String.format(ALREADY_EXIST_ERROR_MESSAGE, costOwner.getCostOwnerName()));
				}
			}
		}
		return this.costOwnerRepository.save(costOwner);
	}

	/**
	 * Delete cost owner.
	 *
	 * @param costOwnerId the id of the CostOwner to delete.
	 */
	public void deleteCostOwner(Integer costOwnerId) {
		this.validateCostOwnerBeforeDelete(costOwnerId);
		this.costOwnerRepository.delete(costOwnerId);
	}

	/**
	 * Find cost owners with pagination.
	 *
	 * @param id          The cost owner's id to find.
	 * @param name        The cost owner's name to find.
	 * @param pageRequest The page request for pagination.
	 * @return the page of cost owners.
	 */
	private List<CostOwner> findCostOwnersWithoutCount(String id, String name, Pageable pageRequest) {

		List<CostOwner> costOwners;
		if (StringUtils.isEmpty(id) && StringUtils.isEmpty(name)) {
			// Get cost owners.
			costOwners = this.costOwnerRepository.findAllBy(pageRequest);
		} else if (!StringUtils.isEmpty(id) && StringUtils.isEmpty(name)) {
			// Get cost owners filtered by cost owner id.
			costOwners = this.costOwnerRepository.findByCostOwnerId(id, pageRequest);
		} else if (StringUtils.isEmpty(id) && !StringUtils.isEmpty(name)) {
			// Get cost owners filtered by cost owner name.
			costOwners = this.costOwnerRepository.findByCostOwnerNameIgnoreCaseContaining(name, pageRequest);
		} else {
			// Get cost owners filtered by cost owner id and cost owner name.
			costOwners = this.costOwnerRepository.findAllByCostOwnerIdAndCostOwnerName(id, name, pageRequest);
		}
		return costOwners;
	}

	/**
	 * Find cost owners with pagination include count.
	 *
	 * @param id          The cost owner's id to find.
	 * @param name        The cost owner's name to find.
	 * @param pageRequest The page request for pagination.
	 * @return the page of cost owners.
	 */
	private Page<CostOwner> findCostOwnersWithCount(String id, String name, Pageable pageRequest) {
		Page<CostOwner> costOwners;
		if (StringUtils.isEmpty(id) && StringUtils.isEmpty(name)) {
			// Get cost owners.
			costOwners = this.costOwnerRepositoryWithCount.findAll(pageRequest);
		} else if (!StringUtils.isEmpty(id) && StringUtils.isEmpty(name)) {
			// Get cost owners filtered by cost owner id.
			costOwners = this.costOwnerRepositoryWithCount.findByCostOwnerId(id, pageRequest);
		} else if (StringUtils.isEmpty(id) && !StringUtils.isEmpty(name)) {
			// Get cost owners filtered by cost owner name.
			costOwners = this.costOwnerRepositoryWithCount.findByCostOwnerNameIgnoreCaseContaining(name, pageRequest);
		} else {
			// Get cost owners filtered by cost owner id and cost owner name.
			costOwners = this.costOwnerRepositoryWithCount.findAllByCostOwnerIdAndCostOwnerName(id, name, pageRequest);
		}
		return costOwners;
	}

	/**
	 * Validate CostOwner is existed or not.
	 *
	 * @param costOwners the list of added CostOwners.
	 * @return the list of existed CostOwners.
	 */
	public List<CostOwner> validateCostOwners(List<CostOwner> costOwners) {
		List<CostOwner> invalidCostOwners = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(costOwners)) {
			for (CostOwner costOwner : costOwners) {
				List<CostOwner> costOwnerList =
						this.costOwnerRepository.findByTrimmedCostOwnerNameIgnoreCase(costOwner.getCostOwnerName());
				if (CollectionUtils.isNotEmpty(costOwnerList)) {
					invalidCostOwners.add(costOwner);
				}
			}
		}
		return invalidCostOwners;
	}

	/**
	 * Validate CostOwner before deleting.
	 *
	 * @param costOwnerId The ID of cost owner.
	 */
	private void validateCostOwnerBeforeDelete(Integer costOwnerId) {

		CostOwner costOwner = costOwnerRepository.getOne(costOwnerId);
		List<String> errorList = new ArrayList<>();
		// Find if cost owner in the Cost Owner - Top 2 Top table or in PROD_BRND_CST_OWN.
		if (!costOwner.getProductBrandCostOwners().isEmpty() || costOwner.getTopToTopId() > 0) {
			errorList.add(CST_OWN_USED_IN_CODE_TABLE_ERROR_MESSAGE);
		}
		// Find if cost owner tied to UPCs in PROD_SCN_CODES.
		if (sellingUnitRepository.countSellingUnitByCostOwnerCostOwnerId(costOwnerId) > 0) {
			errorList.add(ITEM_TIED_ERROR_MESSAGE);
		}

		if (!errorList.isEmpty()) {
			if (errorList.size() == 2) {
				throw new IllegalArgumentException(CST_OWN_USED_IN_CODE_TABLE_AND_ITEM_ERROR_MESSAGE);
			}
			throw new IllegalArgumentException(errorList.get(0));
		}
	}
}
