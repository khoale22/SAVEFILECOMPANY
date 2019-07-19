/*
 *
 * NutrientsService.java
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */

package com.heb.pm.scaleManagement;

import com.heb.pm.entity.AlertStaging;
import com.heb.pm.CoreEntityManager;
import com.heb.pm.CoreTransactional;
import com.heb.pm.Hits;
import com.heb.pm.alert.AlertService;
import com.heb.pm.entity.*;
import com.heb.pm.jms.MediaMasterMessageSender;
import com.heb.pm.mediaMasterMessage.MenuLabel;
import com.heb.pm.repository.*;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import com.heb.util.upc.UpcUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class can be delegated for all searches based on Nutrients.
 *
 * @author m594201
 * @since 2.0.3
 */
@Service
public class NutrientsService {

	private static final String DESCRIPTION_REGEX = "%%%s%%";
	private static final int PAGE_SIZE = 20;

	// Constant for the max rounding rule boundary size.
	private static final int MAX_ROUNDING_RULE_SIZE = 9999;
	private static final int MIN_ROUNDING_RULE_SIZE = 0;
	private static final long MAX_NUTRIENT_STATEMENT = 9999999l;
	private static final long MAX_NUTRIENT_CODE = 999L;
	private static final int MIN_SERVINGS_PER_CONTAINER = 1;
	private static final int MAX_SERVINGS_PER_CONTAINER = 999;

	private static final String MAX_STATEMENT_SIZE_MESSAGE = "Statement value cannot be greater than 9999999.";
	private static final String MAX_NUTRIENT_CODE_SIZE_MESSAGE = "Nutrient code value cannot be greater than 999.";
	private static final String CALORIES_STRING = "CALORIES";
	private static final String MEDIA_MASTER_CALORIES_ATTRIBUTE = "CALORIES";
	private static final String MEDIA_MASTER_SERVING_SIZE_ATTRIBUTE = "SERVING SIZE";

	private static final String NUTRIENT_SEARCH_LOG_MESSAGE =
			"searching for Nutrients by the Nutrient Codes";
	private static final String NUTRIENT_DESCRIPTION_SEARCH_LOG_MESSAGE =
			"searching for Nutrients by the Nutrient Description";
	private static final String NUTRIENT_SEARCH_ALL_LOG_MESSAGE =
			"searching for Nutrients by the Nutrient Codes";
	private static final String UOM_SEARCH_LOG_MESSAGE =
			"searching for Nutrients by the UOM Code %d";
    private static final String UOM_STATEMENT_SEARCH_LOG_MESSAGE =
            "searching for Nutrients Statements by the UOM Code %d";
	private static final char UPDATE_MAINT_SW = 'C';
	private static final char ADD_MAINT_SW = 'A';
	private static final char DELETE_MAINT_SW = 'D';
	private static final String NUTRIENT_STATEMETN_DELETE_RECORD_TYPE = "NTRNT";

	// Error messages.
	private static final String FEDERAL_LABEL_ALREADY_EXISTS = "Federal Label Sequence: %.0f already exists.";
	private static final String QUANTITY_PRECISION_ERROR = "Quantity can only have one decimal place.";
	private static final String QUANTITY_MAX_ERROR = "Quantity max is 9999.";
	private static final String STATEMENT_EXISTS_ERROR = "Nutrient statement %d already exists";
	private static final String NUTRIENT_DOES_NOT_EXIST_ERROR = "Nutrient %d does not exist";
	private static final String NUTRIENT_EXISTS_ERROR = "Nutrient %d already exists";
	private static final String INVALID_STATEMENT_NUMBER_ERROR =
			"Nutrient statement number %d is invalid; it must be a positive number with fewer than six digits.";
	private static final String INVALID_SERVINGS_PER_CONTAINER_ERROR =
			"Servings per container %d is invalid; it must be a positive number with fewer than four digits.";
	private static final String NUTRIENT_PENDING_DELETE_ERROR =
			"Nutrient %d is scheduled for delete, but is still in the system. Please try again tomorrow";
	private static final String NUTRIENT_STATEMENT_IN_USE_ERROR = "Nutrient statement %d is still in use.";
	private static final String NUTRIENT_STATEMENT_NOT_FOUND_ERROR = "Nutrient statement %d does not exist.";
	private static final String NUTRIENT_STATEMENT_PENDING_DELETE_ERROR =
			"Nutrient statement %d is scheduled for delete, but is still pending in the system. Please try again tomorrow";
	private static final String NUTRIENT_ON_STATEMENT_ERROR = "Nutriend %d exists on statements and cannot be removed.";

	private static final String IMPERIAL_UOM_MISMATCH_ERROR = "Common measure cannot be set to a metric unit of measure";
	private static final String METRIC_UOM_MISMATCH_ERROR = "Metric measure cannot be set to a common unit of measure";
	private static final String STATE_MISMATCH_ERROR = "Common and metric measures must measure the same state";

	private static final String METRIC_REQUIRED_ERROR = "Nutrients must have a metric unit of measure";

	private static final Logger logger = LoggerFactory.getLogger(NutrientsService.class);

	private static final String PRODUCT_ID_AS_ALERT_KEY_CONVERSION = "%09d";

	private static final String ALERT_TYPE_PRODUCT_UPDATE = "PRUPD";

	@Autowired
	private NutrientRepository repository;

	@Autowired
	private NutrientStatementDetailRepository nutrientStatementDetailRepository;

	@Autowired
	private NutrientRoundingRuleRepository roundingRuleRepository;

	@Autowired
	private NutrientRepositoryWithCount repositoryWithCount;

	@Autowired
	private NutrientStatementHeaderRepository nutrientStatementHeaderRepository;

	@Autowired
	private ScaleManagementService scaleManagementService;

	@Autowired
	private IngredientNutrientStatementWorkRepository ingredientNutrientStatementWorkRepository;

	@Autowired
	private NutrientStatementHeaderRepositoryWithCount nutrientStatementHeaderRepositoryWithCount;

	@Autowired
	private MediaMasterMessageSender mediaMasterMessageSender;

	@Autowired
	private ScaleUpcRepository scaleUpcRepository;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	@CoreEntityManager
	private EntityManager entityManager;

	private String errorMessage;

	@Autowired
	private AlertService alertService;

	@Autowired
	private SellingUnitRepository sellingUnitRepository;

	@Autowired
	private NutrientStatementPanelHeaderRepository nutrientStatementPanelHeaderRepository;

	/**
	 * Find by nutrient code pageable result.
	 *
	 * @param nutrientCodes the nutrient codes
	 * @param includeCount  the include count
	 * @param page          the page
	 * @param pageSize      the page size
	 * @return the pageable result
	 */
	public PageableResult<Nutrient> findByNutrientCode(List<Long> nutrientCodes, boolean includeCount, int page,
													   int pageSize){

		Pageable nutrientRequest = new PageRequest(page, pageSize);

		NutrientsService.logger.debug(NutrientsService.NUTRIENT_SEARCH_LOG_MESSAGE);

		return includeCount ? this.searchNutrientCodeWithCounts(nutrientCodes, nutrientRequest) :
				this.searchNutrientCodeWithoutCounts(nutrientCodes, nutrientRequest);
	}

	/**
	 * Find by nutrient code pageable result.
	 * @param nutrientCodes the nutrient codes
	 * @param nutrientRequest The HTTP request that initiated this call
	 * @return the pageable result
	 */
	private PageableResult<Nutrient> searchNutrientCodeWithoutCounts(List<Long> nutrientCodes,
																	 Pageable nutrientRequest) {

		List<Nutrient> data = this.repository.findByNutrientCodeInAndMaintenanceSwitchNot(nutrientCodes,
				Nutrient.DELETE_MAINTENANCE_SW, nutrientRequest);

		NutrientsService.logger.debug(NutrientsService.NUTRIENT_SEARCH_LOG_MESSAGE);

		return new PageableResult<>(nutrientRequest.getPageNumber(), data);
	}

	/**
	 * Find by nutrient code with count pageable result.
	 *
	 * @param nutrientCodes the nutrient codes
	 * @param nutrientRequest The HTTP request that initiated this call
	 * @return the pageable result
	 */
	private PageableResult<Nutrient> searchNutrientCodeWithCounts(List<Long> nutrientCodes, Pageable nutrientRequest) {

		Page<Nutrient> data = this.repositoryWithCount.findByNutrientCodeInAndMaintenanceSwitchNot(nutrientCodes,
				Nutrient.DELETE_MAINTENANCE_SW, nutrientRequest);

		return new PageableResult<>(nutrientRequest.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Find by nutrient description pageable result.
	 *
	 * @param nutrientDescription the nutrient description
	 * @param includeCount        the include count
	 * @param page                the page
	 * @param pageSize            the page size
	 * @return the pageable result
	 */
	public PageableResult<Nutrient> findByNutrientDescription(List<String> nutrientDescription, boolean includeCount,
															  int page, int pageSize) {

		List<String> descriptions = nutrientDescription.stream().map(s -> String.format(DESCRIPTION_REGEX,
				s.toUpperCase())).collect(Collectors.toList());

		Pageable nutrientRequest = new PageRequest(page, pageSize);

		NutrientsService.logger.debug(NutrientsService.NUTRIENT_DESCRIPTION_SEARCH_LOG_MESSAGE);

		return includeCount ? this.searchDescriptionWithCounts(descriptions, nutrientRequest) :
				this.searchDescriptionWithoutCounts(descriptions, nutrientRequest);
	}

	/**
	 * Find by description pageable result.
	 *
	 * @param descriptions the nutrient codes
	 * @param nutrientRequest The HTTP request that initiated this call
	 * @return the pageable result
	 */
	private PageableResult<Nutrient> searchDescriptionWithoutCounts(List<String> descriptions,
																	Pageable nutrientRequest) {

		List<Nutrient> data = this.repository.findByNutrientDescriptionContains(descriptions, nutrientRequest);

		NutrientsService.logger.debug(NutrientsService.NUTRIENT_DESCRIPTION_SEARCH_LOG_MESSAGE);

		return new PageableResult<>(nutrientRequest.getPageNumber(), data);

	}

	/**
	 * Find by description pageable result.
	 *
	 * @param descriptions the nutrient codes
	 * @param nutrientRequest The HTTP request that initiated this call
	 * @return the pageable result
	 */
	private PageableResult<Nutrient> searchDescriptionWithCounts(List<String> descriptions, Pageable nutrientRequest) {

		Page<Nutrient> data = this.repositoryWithCount.findByNutrientDescriptionContains(descriptions, nutrientRequest);

		NutrientsService.logger.debug(NutrientsService.NUTRIENT_DESCRIPTION_SEARCH_LOG_MESSAGE);

		return new PageableResult<>(nutrientRequest.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Find all available nutrients.
	 *
	 * @param includeCount Whether or not to include counts of all available nutrients.
	 * @param page The page of nutrients wanted.
	 * @param pageSize The maximum amount of records to return.
	 * @return A pageable list of nutrients.
	 */
	public PageableResult<Nutrient> findAll(boolean includeCount, int page, int pageSize) {

		Pageable nutrientCodesRequest = new PageRequest(page, pageSize, Nutrient.getDefaultSort());

		NutrientsService.logger.debug(NutrientsService.NUTRIENT_SEARCH_ALL_LOG_MESSAGE);

		PageableResult<Nutrient> results = includeCount ? this.findAllWithCount(nutrientCodesRequest) :
				this.findAllWithoutCount(nutrientCodesRequest);
		return results;
	}

	/**
	 * Find all statement id pageable result.
	 *
	 * @param includeCount the include count
	 * @param page         the page
	 * @return the pageable result
	 */
	public PageableResult<NutrientStatementHeader> findAllStatementId(boolean includeCount, int page, int pageSize) {
		Pageable statementIdsRequest = new PageRequest(page, pageSize,
				NutrientStatementHeader.getDefaultSort());

		return this.orderPageBySequence(includeCount ? this.findAllStatementIdWithCount(statementIdsRequest) :
				this.findAllStatementIdWithoutCount(statementIdsRequest));
	}

	/**
	 * Find all nutrient Statement details via statement id without count.
	 *
	 * @param statementIdsRequest
	 * @return the pageable result
	 */
	private PageableResult<NutrientStatementHeader> findAllStatementIdWithoutCount(Pageable statementIdsRequest) {
		List<NutrientStatementHeader> data =
				this.nutrientStatementHeaderRepository.findByStatementMaintenanceSwitchNot(NutrientStatementHeader.DELETE_MAINT_SW, statementIdsRequest);

		return new PageableResult<>(statementIdsRequest.getPageNumber(), data);
	}

	/**
	 * Find all nutrient statement information based on statement id with count.
	 *
	 * @param statementIdsRequest
	 * @return the pagable result
	 */
	private PageableResult<NutrientStatementHeader> findAllStatementIdWithCount(Pageable statementIdsRequest) {

		Page<NutrientStatementHeader> data = this.nutrientStatementHeaderRepositoryWithCount.findByStatementMaintenanceSwitchNot(NutrientStatementHeader.DELETE_MAINT_SW, statementIdsRequest);

		return new PageableResult<>(statementIdsRequest.getPageNumber(), data.getTotalPages(), data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Find all without count pageable result.
	 *
	 * @param request the request
	 * @return the pageable result
	 */
	public PageableResult<Nutrient> findAllWithoutCount(Pageable request){

		List<Nutrient> data =
				this.repository.findByMaintenanceSwitchNot(Nutrient.DELETE_MAINTENANCE_SW, request);
		NutrientsService.logger.debug(NutrientsService.NUTRIENT_SEARCH_ALL_LOG_MESSAGE);

		return new PageableResult<>(request.getPageNumber(), data);
	}

	/**
	 * Search for all ScaleActionCode records with the count.
	 *
	 * @param request The scale action code request.
	 * @return A pageable Result that contains the pagenumber, total pages, total elements, and the
	 * list of scale action codes matching the action codes sent.
	 */
	private PageableResult<Nutrient> findAllWithCount(Pageable request) {

		Page<Nutrient> data = this.repositoryWithCount.findByMaintenanceSwitchNot(Nutrient.DELETE_MAINTENANCE_SW, request);

		return new PageableResult<>(request.getPageNumber(), data.getTotalPages(), data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Checks a nutrient to see if is valid.
	 *
	 * @param nutrient The nutrient to check.
	 * @throws IllegalArgumentException
	 */
	private void validateNutrient(Nutrient nutrient) {

		// Validate the federal label sequence. It's OK if it's zero, there can be multiple.
		if(nutrient.getFedLblSequence() != 0) {
			List<Nutrient> matchingSequencesList = this.repository.findByFedLblSequence(nutrient.getFedLblSequence());
			// Loop through the list, and throw an error if there is a nutrient that isn't this one
			// that has the same sequence.
			for (Nutrient nutrientToCheck : matchingSequencesList) {
				if (nutrientToCheck.getNutrientCode() != nutrient.getNutrientCode()) {
					throw new IllegalArgumentException(String.format(NutrientsService.FEDERAL_LABEL_ALREADY_EXISTS,
							nutrient.getFedLblSequence()));
				}
			}
		}

		// Make sure the unit of measure is metric.
		if (nutrient.getNutrientUom().getSystemOfMeasure() != NutrientUom.METRIC) {
			NutrientsService.logger.error(NutrientsService.METRIC_REQUIRED_ERROR);
			throw new IllegalArgumentException(NutrientsService.METRIC_REQUIRED_ERROR);
		}
	}

	/**
	 * Deletes a nutrient.
	 *
	 * @param nutrientCode The nutrient to delete.
	 */
	public void deleteNutrient(Long nutrientCode) {

		Nutrient scaleNutrientCode = this.repository.findOne(nutrientCode);
		if (scaleNutrientCode == null) {
			String message = String.format(NutrientsService.NUTRIENT_DOES_NOT_EXIST_ERROR,
					nutrientCode);
			NutrientsService.logger.error(message);
			throw new  IllegalArgumentException(message);
		}


		// Make sure there are no statements with that nutrient on it
		PageRequest pageRequest = new PageRequest(0, 1);

		List<NutrientStatementDetail> existingStatements =
				this.nutrientStatementDetailRepository.findNutrientLabelCodeByKeyNutrientLabelCode(nutrientCode, pageRequest);
		if (!existingStatements.isEmpty()) {
			String message = String.format(NutrientsService.NUTRIENT_ON_STATEMENT_ERROR,
					nutrientCode);
			NutrientsService.logger.error(message);
			throw new  IllegalArgumentException(message);
		}

		// If the code is an A, that means it hasn't been added to the scales system yet
		// so it can be deleted completely.
		if(scaleNutrientCode.getMaintenanceSwitch() == ADD_MAINT_SW) {
			this.repository.delete(scaleNutrientCode);
		} else {
			scaleNutrientCode.setMaintenanceSwitch(DELETE_MAINT_SW);
			this.repository.save(scaleNutrientCode);
		}
	}

	/**
	 * Update the data for a nutrient.
	 *
	 * @param nutrient The nutrient to update.
	 * @return The nutrient after it has been saved to the database.
	 */
	@CoreTransactional
	public Nutrient updateNutrient(Nutrient nutrient) {

		// Grab the existing nutrient to update.
		Nutrient originalNutrient = this.repository.findOne(nutrient.getNutrientCode());
		if (originalNutrient == null) {
			throw new IllegalArgumentException(String.format(NutrientsService.NUTRIENT_DOES_NOT_EXIST_ERROR,
					nutrient.getNutrientCode()));
		}

		// Validate the nutrient before save.
		this.validateNutrient(nutrient);

		// If we've made it here, just copy the new values to the old one, set the maintenance flags, and save.

		// Make the description all upper case.
		originalNutrient.setNutrientDescription(nutrient.getNutrientDescription().toUpperCase().trim());
		originalNutrient.setNutrientUom(nutrient.getNutrientUom());
		originalNutrient.setUomCode(nutrient.getNutrientUom().getNutrientUomCode());
		originalNutrient.setFedLblSequence(nutrient.getFedLblSequence());
		originalNutrient.setRecommendedDailyAmount(nutrient.getRecommendedDailyAmount());
		originalNutrient.setUsePercentDailyValue(nutrient.getUsePercentDailyValue());
		originalNutrient.setFederalRequired(nutrient.isFederalRequired());
		originalNutrient.setDefaultBehaviorOverrideRequired(nutrient.isDefaultBehaviorOverrideRequired());
		originalNutrient.setDefaultBehaviorOverrideSequence(nutrient.getDefaultBehaviorOverrideSequence());

		originalNutrient.setLstModifiedDate(LocalDate.now());

		this.setFederalFlags(originalNutrient);
		// If the sw is already A, that means it hasn't been added to the scales system yet
		// so the code needs to stay 'A'.
		if(originalNutrient.getMaintenanceSwitch() != NutrientsService.ADD_MAINT_SW) {
			originalNutrient.setMaintenanceSwitch(NutrientsService.UPDATE_MAINT_SW);
		}
		return this.repository.save(originalNutrient);
	}


	/**
	 * Adds a new nutrient to the database.
	 *
	 * @param addedNutrient The nutrient to add.
	 * @return The nutrient after it has been saved to the database.
	 */
	@CoreTransactional
	public Nutrient addNutrient(Nutrient addedNutrient){

		// See if the nutrient is already there.
		Nutrient nutrient = this.repository.findOne(addedNutrient.getNutrientCode());
		if(nutrient != null) {
			// If it's there and being set up as a delete, tell the user to wait.
			if (nutrient.getMaintenanceSwitch() == NutrientsService.DELETE_MAINT_SW) {
				throw new IllegalArgumentException(String.format(NutrientsService.NUTRIENT_PENDING_DELETE_ERROR,
						nutrient.getNutrientCode()));
			}
			// Otherwise, it's just there, so throw an error.
			throw new IllegalArgumentException(String.format(NutrientsService.NUTRIENT_EXISTS_ERROR, nutrient.getNutrientCode()));
		}

		// Validate the nutrient before save
		this.validateNutrient(addedNutrient);

		this.setFederalFlags(addedNutrient);

		// Make the description all upper-case
		addedNutrient.setNutrientDescription(addedNutrient.getNutrientDescription().toUpperCase().trim());
		addedNutrient.setMaintenanceSwitch(NutrientsService.ADD_MAINT_SW);
		addedNutrient.setLstModifiedDate(LocalDate.now());
		addedNutrient.setUomCode(addedNutrient.getNutrientUom().getNutrientUomCode());
		return this.repository.save(addedNutrient);
	}

	/**
	 * The federal and pdd required flags need to be set based on rules defined by having a federal label sequence.
	 * This function will update the flag based on those rules.
	 *
	 * @param nutrient The nutrient to update.
	 */
	private void setFederalFlags(Nutrient nutrient) {

		// If it has a value, these flags should be true.
		if (nutrient.getFedLblSequence() > 0.0) {
			nutrient.setFederalRequired(true);
			nutrient.setDefaultBehaviorOverrideRequired(true);
		} else {
			// Otherwise, they are false.
			nutrient.setFederalRequired(false);
			nutrient.setDefaultBehaviorOverrideRequired(false);
		}
	}

	/**
	 * Update nutrient statement data nutrient statement header. Note that this one works a little differently
	 * than other updates. It does not return an updated statement, the statement needs to be fetched after
	 * the save commits. This is because of the re-ordering of the nutrient details that happens on the fetch
	 * that cannot be done on the pull.
	 *
	 * @param nutrientStatementHeader the nutrient statement header
	 * @throws IllegalArgumentException Any error in validation will throw an IllegalArgumentException.
	 */
	@CoreTransactional
	public NutrientStatementHeader updateNutrientStatementData(NutrientStatementHeader nutrientStatementHeader) {

		// Update the units of measure to be the ones selected on the GUI.
		nutrientStatementHeader.setUomMetricCode(nutrientStatementHeader.getNutrientMetricUom().getNutrientUomCode());
		nutrientStatementHeader.setUomCommonCode(nutrientStatementHeader.getNutrientCommonUom().getNutrientUomCode());

		this.validateNutrientStatementHeader(nutrientStatementHeader);

		// Fetch the existing statement header from the DB.
		NutrientStatementHeader existingHeader =
				this.nutrientStatementHeaderRepository.findOne(nutrientStatementHeader.getNutrientStatementNumber());

		if (existingHeader == null) {
			String message = String.format(NutrientsService.NUTRIENT_STATEMENT_NOT_FOUND_ERROR,
					nutrientStatementHeader.getNutrientStatementNumber());
			NutrientsService.logger.error(message);
			throw new IllegalArgumentException(message);
		}

		try {
			// Send a message to Media Master if calories or serving size has changed.
			this.submitMediaMasterMessageOnCalorieAndServingSizeChange(nutrientStatementHeader,
					existingHeader.getNutrientStatementDetailList());
		} catch (Exception e) {
			// If there is a problem creating the task, just log the error and keep going. See PM-984.
			NutrientsService.logger.error(e.getMessage());
		}

		// Loop through the existing detail records, and see if they need to be updated.
		for (NutrientStatementDetail detail : existingHeader.getNutrientStatementDetailList()) {
			// Don't bother with the ones that are not required, they won't be in what comes from the front end.
			if (!detail.getNutrient().isFederalRequired()) {
				continue;
			}
			// Find it in the list that's passed in.
			int index = nutrientStatementHeader.getNutrientStatementDetailList().indexOf(detail);

			// If it's not there, just move on. The only way I can think of this happening is that if the
			// front end does not pass back everything.
			if (index == -1) {
				continue;
			}

			NutrientStatementDetail updatedDetail =
					nutrientStatementHeader.getNutrientStatementDetailList().get(index);

			this.validateNutrientValueRange(updatedDetail.getNutrientStatementQuantity());

			detail.setNutrientStatementQuantity(updatedDetail.getNutrientStatementQuantity());
			detail.setNutrientDailyValue(updatedDetail.getNutrientDailyValue());
			detail.setNutrientPDDQuantity(updatedDetail.getNutrientPDDQuantity());
			detail.setNutrientRoundingRequired(updatedDetail.isNutrientRoundingRequired());
			detail.setInAlternateEditMode(updatedDetail.isInAlternateEditMode());
		}

		// Loop through the detail records passed in and see if stuff needs to be added.
		for (NutrientStatementDetail updatedDetail : nutrientStatementHeader.getNutrientStatementDetailList()) {

			int index = existingHeader.getNutrientStatementDetailList().indexOf(updatedDetail);
			// if it's found, no need to do anything.
			if (index != -1) {
				continue;
			}

			// If we get here, it's in the new list, but not the old, so add it.
			Nutrient nutrient = this.repository.findOne(updatedDetail.getNutrient().getNutrientCode());
			NutrientStatementDetail newDetail = new NutrientStatementDetail();
			newDetail.setNutrient(nutrient);
			newDetail.setNutrientStatementHeader(existingHeader);
			newDetail.setKey(new NutrientStatementDetailsKey());
			newDetail.getKey().setNutrientStatementNumber(existingHeader.getNutrientStatementNumber());
			newDetail.getKey().setNutrientLabelCode(nutrient.getNutrientCode());
			newDetail.setNutrientStatementQuantity(updatedDetail.getNutrientStatementQuantity());
			newDetail.setNutrientDailyValue(updatedDetail.getNutrientDailyValue());
			newDetail.setNutrientPDDQuantity(updatedDetail.getNutrientPDDQuantity());
			newDetail.setNutrientRoundingRequired(updatedDetail.isNutrientRoundingRequired());
			newDetail.setInAlternateEditMode(updatedDetail.isInAlternateEditMode());
			existingHeader.getNutrientStatementDetailList().add(newDetail);
		}

		// Copy the values from the updated header to the one from the DB.
		existingHeader.setMeasureQuantity(nutrientStatementHeader.getMeasureQuantity());
		existingHeader.setMetricQuantity(nutrientStatementHeader.getMetricQuantity());
		existingHeader.setUomMetricCode(nutrientStatementHeader.getUomMetricCode());
		existingHeader.setUomCommonCode(nutrientStatementHeader.getUomCommonCode());
		existingHeader.setServingsPerContainer(nutrientStatementHeader.getServingsPerContainer());

		// If the maintenance switch is an A, that means it hasn't been added to the scales system yet
		// so the code needs to stay 'A'.
		if(existingHeader.getStatementMaintenanceSwitch() != ADD_MAINT_SW) {
			existingHeader.setStatementMaintenanceSwitch(UPDATE_MAINT_SW);
		}
		// Set the effective date
		existingHeader.setEffectiveDate(LocalDate.now());

		this.nutrientStatementHeaderRepository.save(existingHeader);

		// The save (and subsequent return) won't refresh the updated units of measure,
		// so set them from the object passed in.
		existingHeader.setNutrientCommonUom(nutrientStatementHeader.getNutrientCommonUom());
		existingHeader.setNutrientMetricUom(nutrientStatementHeader.getNutrientMetricUom());

		// If there's an error creating the task, just log the error but continue with the update.
		try {
			this.createProductUpdateAlertForUpdateNutrientStatement(existingHeader.getNutrientStatementNumber());
		} catch (Exception e) {
			NutrientsService.logger.error(e.getMessage());
		}

		return existingHeader;
	}

	/**
	 * This method calls submitMediaMasterMessage method to submit a JMS message to Media Master if:
	 * 1. media master has a nutrition statement, and
	 * 2. the media master nutrition statement number matches the updated nutrition statement number, and
	 * 3. calories or serving size has changed on a nutrient statement.
	 *
	 * @param nutrientStatementHeader The updated nutrient statement.
	 * @param oldDetails The old nutrient statement details.
	 */
	private void submitMediaMasterMessageOnCalorieAndServingSizeChange(NutrientStatementHeader nutrientStatementHeader,
												 List<NutrientStatementDetail> oldDetails) {
		NutrientStatementHeader mediaMasterHeader = this.findMediaMasterHeader(nutrientStatementHeader);

		// if media master header wasn't found, return null (do nothing)
		if(mediaMasterHeader == null){
			return;
		}
		// else if the updated statement number doesn't match media master statement number, return null (do nothing)
		else if(nutrientStatementHeader.getNutrientStatementNumber() != mediaMasterHeader.getNutrientStatementNumber()){
			return;
		}
		// if calories has changed, submit calorie message
		if(this.hasCaloriesChanged(nutrientStatementHeader, oldDetails)){
				this.submitMediaMasterMessage(
						mediaMasterHeader.getScaleUpc().getAssociateUpc().getSellingUnit().getProdId(),
						MEDIA_MASTER_CALORIES_ATTRIBUTE);
		}
		// if serving size changed, submit serving size message
		if(this.hasServingSizeChanged(nutrientStatementHeader)){
				this.submitMediaMasterMessage(
						mediaMasterHeader.getScaleUpc().getAssociateUpc().getSellingUnit().getProdId(),
						MEDIA_MASTER_SERVING_SIZE_ATTRIBUTE);
		}
	}

	/**
	 * Media Master system is product specific. Therefore, this method finds the nutrient statement used for media
	 * master attached to a given product by going through a series of table joins:
	 * NutrientStatementHeader -> ScaleUpc -> AssociateUpc -> SellingUnit -> ProductMaster -> SellingUnit(s)
	 * If the nutrient statement is not linked to a scale upc, or that scale upc is not linked to an associate upc,
	 * return null, else find the first selling unit attached to the product that has a nutrition statement, starting
	 * with the product primary. Once a nutrition statement is found, return it; Otherwise return null.
	 *
	 * @param nutrientStatementHeader The nutrient statement header being updated.
	 * @return The nutrient statement used by media master.
	 */
	private NutrientStatementHeader findMediaMasterHeader(NutrientStatementHeader nutrientStatementHeader) {
		// if there is no scale upc attached to the nutrient statement return null
		if(nutrientStatementHeader.getScaleUpc() == null){
			return null;
		}
		// else if there is no associate upc attached to the scale upc return null
		else if(nutrientStatementHeader.getScaleUpc().getAssociateUpc() == null){
			return null;
		}
		// Check dealing with PM-985
		if (nutrientStatementHeader.getScaleUpc().getAssociateUpc().getSellingUnit() == null ||
				nutrientStatementHeader.getScaleUpc().getAssociateUpc().getSellingUnit().getProductMaster() == null) {
			NutrientsService.logger.error(String.format("Error in UPC chain for %d",
					nutrientStatementHeader.getScaleUpc().getUpc()));
			return null;
		}

		// find the linked product
		ProductMaster linkedProduct = nutrientStatementHeader.getScaleUpc().getAssociateUpc()
				.getSellingUnit().getProductMaster();
		// if the product does not have show calories flag, return null
		if(!linkedProduct.getShowCalories()){
			return null;
		}
		// Check dealing with PM-985
		if (linkedProduct.getProductPrimarySellingUnit()== null) {
			NutrientsService.logger.error(String.format("Error in UPC chain for %d",
					nutrientStatementHeader.getScaleUpc().getUpc()));
			return null;
		}
		// find the product primary scale upc
		ScaleUpc mediaMasterScaleUpc = this.scaleManagementService.
				findOne(linkedProduct.getProductPrimarySellingUnit().getUpc());
		// if product primary scale upc exists and has nutrient data, return the product primary scale upc
		if(mediaMasterScaleUpc != null && mediaMasterScaleUpc.getNutrientStatementHeader() != null){
			return mediaMasterScaleUpc.getNutrientStatementHeader();
		}
		// else return the first scale upc the exists and has nutrient data
		for(SellingUnit sellingUnit : linkedProduct.getSellingUnits()){
			if(sellingUnit.getUpc() != linkedProduct.getProductPrimarySellingUnit().getUpc()){
				mediaMasterScaleUpc = this.scaleManagementService.
						findOne(sellingUnit.getUpc());
				if(mediaMasterScaleUpc != null && mediaMasterScaleUpc.getNutrientStatementHeader() != null) {
					return mediaMasterScaleUpc.getNutrientStatementHeader();
				}
			}
		}
		return null;
	}

	/**
	 * This method returns whether or not the serving size has changed on a nutrient statement header.
	 *
	 * @param nutrientStatementHeader The updated nutrient statement header.
	 * @return true if serving size changed; false otherwise
	 */
	private boolean hasServingSizeChanged(NutrientStatementHeader nutrientStatementHeader) {
		NutrientStatementHeader oldHeader = this.nutrientStatementHeaderRepository.
				findOne(nutrientStatementHeader.getNutrientStatementNumber());
		return oldHeader.getServingsPerContainer() != nutrientStatementHeader.getServingsPerContainer();
	}

	/**
	 * This method sends a MenuLabel to a MediaMasterMessageSender, so the message that calories or serving size
	 * has changed on a product can be sent to media master.
	 *
	 * @param productId The product id used by media master.
	 * @param attribute The attribute that changed (CALORIES or SERVING SIZE).
	 */
	private void submitMediaMasterMessage(long productId, String attribute) {
		MenuLabel menuLabel = new MenuLabel();
		menuLabel.setProdId(BigInteger.valueOf(productId));
		menuLabel.setAttributeNameText(attribute);
		this.mediaMasterMessageSender.sendStatusUpdate(menuLabel);
	}

	/**
	 * This method returns true if the calories for a nutrient statement have changed.
	 *
	 * @param nutrientStatementHeader Nutrient statement header being updated.
	 * @param oldDetails List of nutrient statement details before the update to compare values.
	 * @return true if calories changed; false otherwise.
	 */
	private boolean hasCaloriesChanged(NutrientStatementHeader nutrientStatementHeader,
													List<NutrientStatementDetail> oldDetails) {
		NutrientStatementDetail oldDetail = null;
		for(NutrientStatementDetail detail : oldDetails){
			if(detail.getNutrient().getNutrientDescription().trim().
					equalsIgnoreCase(NutrientsService.CALORIES_STRING)){
				oldDetail = detail;
				break;
			}
		}
		NutrientStatementDetail newDetail = null;
		for(NutrientStatementDetail detail : nutrientStatementHeader.getNutrientStatementDetailList()){
			if(detail.getNutrient().getNutrientDescription().trim().
					equalsIgnoreCase(NutrientsService.CALORIES_STRING)){
				newDetail = detail;
				break;
			}
		}

		if(oldDetail == null && newDetail == null){
			return false;
		} else if(oldDetail == null || newDetail == null){
			return true;
		} else return oldDetail.getNutrientStatementQuantity() != newDetail.getNutrientStatementQuantity();
	}

	/**
	 * For each pageable list passed in, it orders the details by their federal label sequence.
	 *
	 * @param pageableResult Pageable result of nutrient statement header.
	 * @return Pageable result of nutrient statement headers.
	 */
	private PageableResult<NutrientStatementHeader> orderPageBySequence(
			PageableResult<NutrientStatementHeader> pageableResult){

		for(NutrientStatementHeader statementHeader : pageableResult.getData()) {
			this.orderNutrientDetailsBySequence(statementHeader.getNutrientStatementDetailList());
		}
		return pageableResult;
	}

	/**
	 * Orders details by sequence and removes any non-required nutrients. This is done in-place so that the JPA
	 * objects remain intact.
	 *
	 * @param details List of nutrient statement details
	 */
	public void orderNutrientDetailsBySequence(List<NutrientStatementDetail> details){

		// Remove the nutrients that are not required.
		Iterator<NutrientStatementDetail> detailIterator = details.iterator();
		while (detailIterator.hasNext()) {
			NutrientStatementDetail detail = detailIterator.next();
			// If it's not required, remove it.
			if (!detail.getNutrient().isFederalRequired()) {
				// This is needed to keep Hibernate from deleting the record from the DB.
				this.entityManager.detach(detail);
				detailIterator.remove();
			}
		}

		// Sort the list. This uses bubble sort since it's straight-forward and these lists
		// are not large.
		int size = details.size();
		boolean swapped;
		do {
			swapped = false;
			for (int i = 1; i < size; i++) {
				Nutrient n1 = details.get(i - 1).getNutrient();
				Nutrient n2 = details.get(i).getNutrient();
				if (n1.getFedLblSequence() >
						n2.getFedLblSequence()) {
					Collections.rotate(details.subList(i - 1, i + 1), -1);
					swapped = true;
				}
			}
		} while (swapped);
	}

	/**
	 * Add a new nutrient statement.
	 *
	 * @param nutrientStatementHeader The nutrient statement header to add.
	 * @return The nutrient statement header after processing.
	 * @throws IllegalArgumentException Any exception in validating the nutrient statement before save.
	 */
	public NutrientStatementHeader addNutrientStatementData(NutrientStatementHeader nutrientStatementHeader)
			throws IllegalArgumentException {

		// Validate the nutrient statement number works. This will throw exceptions with whatever doesn't pass the rule.
		this.validateNewStatementNumber(nutrientStatementHeader.getNutrientStatementNumber());

		// Validate the rest of the header
		this.validateNutrientStatementHeader(nutrientStatementHeader);

		nutrientStatementHeader.setUomMetricCode(nutrientStatementHeader.getNutrientMetricUom().getNutrientUomCode());
		nutrientStatementHeader.setUomCommonCode(nutrientStatementHeader.getNutrientCommonUom().getNutrientUomCode());
		nutrientStatementHeader.setStatementMaintenanceSwitch(ADD_MAINT_SW);
		nutrientStatementHeader.setEffectiveDate(LocalDate.now());
		if(nutrientStatementHeader.getNutrientStatementDetailList() != null) {
			for (NutrientStatementDetail detail : nutrientStatementHeader.getNutrientStatementDetailList()) {

				if(detail.isNutrientRoundingRequired()) {
					this.applyRoundingRuleToNutrient(detail);
				}
				detail.getKey().setNutrientStatementNumber(nutrientStatementHeader.getNutrientStatementNumber());
				detail.getKey().setNutrientLabelCode(detail.getNutrient().getNutrientCode());
			}
			this.orderNutrientDetailsBySequence(nutrientStatementHeader.getNutrientStatementDetailList());
		}
		this.nutrientStatementHeaderRepository.save(nutrientStatementHeader);

		return nutrientStatementHeader;
	}

	/**
	 * Finds rounding rules by the nutrient code.
	 *
	 * @param nutrientCode the nutrient code.
	 * @return a List of rounding rules.
	 */
	public List<NutrientRoundingRule> findNutrientRoundingRulesByNutrientCode(int nutrientCode){
		return roundingRuleRepository.findByKeyNutrientCode(nutrientCode);
	}
	
	/**
	 * Find nutrient statement by nutrient code pageable result.
	 *
	 * @param nutrientCode the nutrient code
	 * @param ic           the ic
	 * @param ps           the ps
	 * @param page         the page
	 * @return the pageable result
	 */
	public PageableResult<NutrientStatementHeader> findNutrientStatementByNutrientCode(List<Long> nutrientCode,
																					   boolean ic, int ps, int page){

		Pageable nutrientCodesRequest = new PageRequest(page, ps, NutrientStatementHeader.getDefaultSort());

		return this.orderPageBySequence(ic ? this.findNutrientStatementByNutrientCodeWithCount(
				nutrientCode, nutrientCodesRequest) :
				this.findNutrientStatementByNutrientCodeWithoutCount(nutrientCode, nutrientCodesRequest));

	}

	/**
	 * Find nutrient statement by statement id pageable result.
	 *
	 * @param statementId the statement id
	 * @param ic          the ic
	 * @param ps          the ps
	 * @param pg          the pg
	 * @return the pageable result
	 */
	public PageableResult<NutrientStatementHeader> findNutrientStatementByStatementId(
			List<Long> statementId, boolean ic, int ps, int pg) {

		Pageable nutrientSatementsRequest = new PageRequest(pg, ps,	NutrientStatementHeader.getDefaultSort());

		return this.orderPageBySequence(ic ? this.findNutrientStatementByStatementIdWithCount(
				statementId, nutrientSatementsRequest) :
				this.findNutrientStatementByStatementIdWithoutCount(statementId, nutrientSatementsRequest));

	}

	/**
	 * Find nutrient statement information by id.
	 *
	 * @param statementId id that will search by.
	 * @param nutrientStatementRequest the request
	 * @return the pageable result

	 */
	private PageableResult<NutrientStatementHeader> findNutrientStatementByStatementIdWithCount(
			List<Long> statementId, Pageable nutrientStatementRequest) {

		Page<NutrientStatementHeader> data = this.nutrientStatementHeaderRepositoryWithCount.
				findByNutrientStatementNumberInAndStatementMaintenanceSwitchNot(statementId, NutrientStatementHeader.DELETE_MAINT_SW, nutrientStatementRequest);

		return new PageableResult<>(nutrientStatementRequest.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Find nutrient statement information by id.
	 *
	 * @param statementId id to search by
	 * @param nutrientStatementRequest the request
	 * @return the pageable result
	 */
	private PageableResult<NutrientStatementHeader> findNutrientStatementByStatementIdWithoutCount(
			List<Long> statementId, Pageable nutrientStatementRequest) {
		List<NutrientStatementHeader> data = this.nutrientStatementHeaderRepository.
				findByNutrientStatementNumberInAndStatementMaintenanceSwitchNot(statementId, NutrientStatementHeader.DELETE_MAINT_SW, nutrientStatementRequest);

		NutrientsService.logger.debug(NutrientsService.NUTRIENT_SEARCH_LOG_MESSAGE);

		return new PageableResult<>(nutrientStatementRequest.getPageNumber(), data);
	}

	/**
	 * Find nutrient statement information by nutrient code without count.
	 *
	 * @param nutrientCodes codes to search by
	 * @param nutrientCodesRequest the request
	 * @return the pageable result
	 */
	private PageableResult<NutrientStatementHeader> findNutrientStatementByNutrientCodeWithoutCount(
			List<Long> nutrientCodes, Pageable nutrientCodesRequest) {

		List<NutrientStatementHeader> data = this.nutrientStatementHeaderRepository.
				findDistinctByNutrientStatementDetailListKeyNutrientLabelCodeInAndStatementMaintenanceSwitchNot(nutrientCodes, NutrientStatementHeader.DELETE_MAINT_SW, nutrientCodesRequest);

		NutrientsService.logger.debug(NutrientsService.NUTRIENT_SEARCH_LOG_MESSAGE);

		return new PageableResult<>(nutrientCodesRequest.getPageNumber(), data);
	}

	/**
	 * Find nutrient statement details by nutrient code.
	 *
	 * @param nutrientCodes to search by
	 * @param nutrientCodesRequest the request
	 * @return the pageable result
	 */
	private PageableResult<NutrientStatementHeader> findNutrientStatementByNutrientCodeWithCount(
			List<Long> nutrientCodes, Pageable nutrientCodesRequest) {

		Page<NutrientStatementHeader> data = this.nutrientStatementHeaderRepositoryWithCount.
				findDistinctByNutrientStatementDetailListKeyNutrientLabelCodeInAndStatementMaintenanceSwitchNot(nutrientCodes, NutrientStatementHeader.DELETE_MAINT_SW, nutrientCodesRequest);


		return new PageableResult<>(nutrientCodesRequest.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
	}

	/**
	 * If the rules are valid, deletes the old rules and saves the new rules.
	 *
	 * @param newRules The rules to be added.
	 * @param oldRules The rules to be removed.
	 * @return A message containing the error message, if any.
	 */
	@CoreTransactional
	public String update(List<NutrientRoundingRule> newRules, List<NutrientRoundingRule> oldRules){
		if(this.isValidRoundingRules(newRules)){
			if(oldRules != null) {
				roundingRuleRepository.delete(oldRules);
			}
			roundingRuleRepository.save(newRules);
		}
		return errorMessage;
	}


	/**
	 * Checks to see if the rules have invalid parameters, are overlapping, or if there's a gap between rules.
	 *
	 * @param roundingRules The rounding rules the validate.
	 * @return whether or not the rules have invalid parameters, are overlapping, or if there's a gap between rules.
	 */
	private boolean isValidRoundingRules(List<NutrientRoundingRule> roundingRules){
		NutrientRoundingRule currentRule;
		NutrientRoundingRule nextRule;
		boolean isNoError = true;
		int lowestBoundCount = 0;
		int highestBoundCount = 0;
		for (int x = 0; x < roundingRules.size(); x++) {
			currentRule = roundingRules.get(x);
			if (roundingRules.get(x).getKey().getLowerBound() > roundingRules.get(x).getUpperBound()){
				this.setErrorMessage("Lower boundary cannot be greater than upper boundary. ");
			}
			// adds count of lowest boundary
			if(currentRule.getKey().getLowerBound() == MIN_ROUNDING_RULE_SIZE){
				lowestBoundCount++;
			}
			// adds count of the highest boundary
			if(currentRule.getUpperBound() == MAX_ROUNDING_RULE_SIZE){
				highestBoundCount++;
			}
			for(int y = x+1; y < roundingRules.size(); y++) {
				nextRule = roundingRules.get(y);

				// if two rules have the same lower bound, set message for the rules in conflict.
				if (currentRule.getKey().getLowerBound() == nextRule.getKey().getLowerBound()) {
					this.setBoundaryErrorMessage(currentRule,
							"Rule has same lower boundary as line: " + (y + 1) + ". ");
					this.setBoundaryErrorMessage(nextRule, "Rule has same lower boundary as line: " + (x + 1) + ". ");
					isNoError = false;
				}
				// if two rules have the same upper bound, set message for the rules in conflict.
				if (currentRule.getUpperBound() == nextRule.getUpperBound()) {
					this.setBoundaryErrorMessage(currentRule,
							"Rule has same upper lower boundary as line: " + (y + 1) + ". ");
					this.setBoundaryErrorMessage(nextRule,
							"Rule has same upper boundary as line: " + (x + 1) + ". ");
					isNoError = false;
				}
				// If there's boundary overlaps or a boundary within another boundary, set the boundary error
				// message for those rules that conflict.
				if (
					//(currentRule.key.lowerBound > nextRule.key.lowerBound &&
					//currentRule.key.lowerBound > nextRule.upperBound) ||
						(currentRule.getUpperBound() > nextRule.getKey().getLowerBound() &&
								currentRule.getUpperBound() < nextRule.getUpperBound())  ||
								(currentRule.getKey().getLowerBound() < nextRule.getKey().getLowerBound() &&
										currentRule.getUpperBound() > nextRule.getUpperBound())) {

					this.setBoundaryErrorMessage(currentRule, "Boundary conflict with line: " + (y + 1) + ". ");
					this.setBoundaryErrorMessage(nextRule, "Boundary conflict with line: " + (x + 1) + ". ");
					isNoError = false;
				}
			}
		}
		// if there's not exactly one of the MAX and MIN rounding rule value, set message.
		if(lowestBoundCount != 1){
			this.setErrorMessage("There must be exactly one lower boundary of 0. ");
			isNoError = false;
		}
		if(highestBoundCount != 1){
			this.setErrorMessage("There must be exactly one upper boundary of 9999. ");
			isNoError = false;
		}
		if(this.isGapBetweenRoundingRules(roundingRules)){
			isNoError = false;
		}
		return isNoError;
	}

	/**
	 * Checks for gaps in the rounding rules.
	 *
	 * @param roundingRules The rounding rules.
	 * @return Whether or not there's a gap or not.
	 */
	private boolean isGapBetweenRoundingRules(List<NutrientRoundingRule> roundingRules) {
		int currentUpperBound;
		int nextLowerBound;
		int closestBoundary;
		boolean isError = false;
		for (int x = 0; x < roundingRules.size(); x++) {
			currentUpperBound = roundingRules.get(x).getUpperBound();
			closestBoundary = 0;
			for(int y = 0; y < roundingRules.size(); y++) {
				if(x ==	 y){
					continue;
				}
				nextLowerBound = roundingRules.get(y).getKey().getLowerBound();
				if(currentUpperBound == nextLowerBound || currentUpperBound == MAX_ROUNDING_RULE_SIZE){
					break;
				} else {
					if(currentUpperBound < nextLowerBound &&
							(closestBoundary == 0 || nextLowerBound < closestBoundary)) {
						closestBoundary = nextLowerBound;
					}
				}
			}
			if(closestBoundary != 0){
				this.setErrorMessage("Gap between: " + currentUpperBound + " and: " + closestBoundary + ". ");
				isError = true;
			}
		}
		return isError;
	}

	/**
	 * Sets the boundary message to the rule if no messages already exist. Concats the message if one does exist.
	 *
	 * @param rule The rule to add the message to.
	 * @param message The message to add.
	 */
	private void setBoundaryErrorMessage(NutrientRoundingRule rule, String message){
		if(rule.getRoundingRulesError() == null){
			rule.setRoundingRulesError(message);
		} else {
			rule.setRoundingRulesError(rule.getRoundingRulesError().concat(message));
		}
	};

	/**
	 * If error message is null, the message sent is assigned to the error message, else the message is concatenated
	 * to the current error message.
	 *
	 * @param message An error message.
	 */
	private void setErrorMessage(String message){
		if(errorMessage == null){
			errorMessage = message;
		} else {
			errorMessage = errorMessage.concat(message);
		}
	}

	/**
	 * Returns the Hits count with match and non-match, along with non-match nutrient codes from the input list.
	 *
	 * @param nutrientCodeList The nutrient codes to search for.
	 * @return Hits for the nutrientCodeList.
	 */
	public Hits findHitsByNutrientCodeList(List<Long> nutrientCodeList) {
		List<Nutrient> scaleNutrientCodes = this.repositoryWithCount.findAll(nutrientCodeList);
		List<Long> hitNutrientCodes = scaleNutrientCodes.stream().map(Nutrient::getNutrientCode).collect(
				Collectors.toList());
		return Hits.calculateHits(nutrientCodeList, hitNutrientCodes);
	}

	/**
	 * Find nutrient statement hits by nutrient statement codes hits.
	 *
	 * @param codes the codes
	 * @return the hits
	 */
	public Hits findNutrientStatementHitsByNutrientStatementCodes(List<Long> codes){
		for(Long code : codes){
			if(code > MAX_NUTRIENT_STATEMENT){
				throw new IllegalArgumentException(MAX_STATEMENT_SIZE_MESSAGE);
			}
		}
		// Use new functions related to PM-985
		List<Long> statements =
				this.nutrientStatementHeaderRepository.findNutrientStatementNumberByNutrientStatementNumberIn(codes);
		Set<Long> hits = new HashSet<>();
		for(Long stmt : statements){
			if(codes.contains(stmt)){
				hits.add(stmt);
			}
		}
		return Hits.calculateHits(codes, new ArrayList<>(hits));
	}

	/**
	 * Find nutrient statement hits by nutrient codes hits.
	 *
	 * @param codes the codes
	 * @return the hits
	 */
	public Hits findNutrientStatementHitsByNutrientCodes(List<Long> codes) {
		for(Long code : codes){
			if(code > MAX_NUTRIENT_CODE){
				throw new IllegalArgumentException(MAX_NUTRIENT_CODE_SIZE_MESSAGE);
			}
		}
		// Use new functions related to PM-985
		List<Long> statements = this.nutrientStatementDetailRepository.
				findNutrientLabelCodeByKeyNutrientLabelCodeIn(codes);
		Set<Long> hits = new HashSet<>();

		for(Long stmt : statements) {
			if(codes.contains(stmt)) {
				hits.add(stmt);
			}
		}
		return Hits.calculateHits(codes, new ArrayList<>(hits));
	}

	/**
	 * Find by regular expression pageable result.
	 *
	 * @param searchString            the search string
	 * @param currentNutrientCodeList the current nutrient code list
	 * @return the pageable result
	 */
	public PageableResult<Nutrient> findByRegularExpression(String searchString, List<Long> currentNutrientCodeList) {

		List<Nutrient> nutrients;

		List<Long> doNotIncludeList = currentNutrientCodeList;
		long searchStringNumber = 0;

		Boolean isNumber;
		try {
			searchStringNumber = Long.valueOf(searchString);
			isNumber = true;
		} catch (NumberFormatException e){
			isNumber = false;
		}

		for(Nutrient nutrient : this.repository.findByFedLblSequence(0)){
			doNotIncludeList.add(nutrient.getNutrientCode());
		}

		Pageable nutrientRequest = new PageRequest(0, NutrientsService.PAGE_SIZE);


		if(searchString.length() <= 3 && isNumber){
			nutrients = this.repository.findByNutrientCodeAndNutrientCodeNotInOrNutrientDescriptionIgnoreCaseAndNutrientCodeNotIn(searchStringNumber, doNotIncludeList, searchString, doNotIncludeList, nutrientRequest);
		} else {
			nutrients = this.repository.findByNutrientDescriptionIgnoreCaseAndNutrientCodeNotIn(searchString, doNotIncludeList, nutrientRequest);

		}

		int remainingPageSize = 20 - nutrients.size();

		if(remainingPageSize > 0) {
			nutrientRequest = new PageRequest(0, remainingPageSize);
			if(searchString.length() <= 3 && isNumber){
				nutrients = this.repository.findByNutrientCodeContainingAndNutrientCodeNotInOrNutrientDescriptionContainingIgnoreCaseAndNutrientCodeNotIn(String.format(DESCRIPTION_REGEX, searchString), doNotIncludeList, nutrientRequest);
			} else {
				nutrients = this.repository.findByNutrientDescriptionContainingIgnoreCaseAndNutrientCodeNotIn(searchString, doNotIncludeList,nutrientRequest);

			}

		}
		return new PageableResult<>(0, new ArrayList<>(new LinkedHashSet<>(nutrients)));
	}

	/**
	 * Finds Mandated Nutrients not already on statement.
	 *
	 * @param statementNumber current statement.
	 * @return new mandated statement details.
	 */
	public List<NutrientStatementDetail> findMandatedNutrientsByStatementId(Long statementNumber) {
		List<NutrientStatementDetail> oldDetails = new ArrayList<>();
		if(statementNumber != null) {
			oldDetails = this.nutrientStatementDetailRepository.findByKeyNutrientStatementNumber(statementNumber);
		}
		List<Nutrient> nutrients = this.repository.findByFedLblSequenceNot(0);
		NutrientStatementDetail detail;
		List<NutrientStatementDetail> details = new ArrayList<>();
		boolean alreadyExists;
		for (Nutrient nutrient : nutrients) {
			alreadyExists = false;
			for (NutrientStatementDetail oldDetail : oldDetails) {
				if (oldDetail.getKey().getNutrientLabelCode() == nutrient.getNutrientCode()) {
					alreadyExists = true;
					break;
				}
			}
			if (!alreadyExists) {
				detail = new NutrientStatementDetail();
				detail.setKey(new NutrientStatementDetailsKey());
				detail.getKey().setNutrientLabelCode(nutrient.getNutrientCode());
				if (statementNumber != null) {
					detail.getKey().setNutrientStatementNumber(statementNumber);
				}
				detail.setNutrient(nutrient);
				details.add(detail);
			}
		}
		this.orderNutrientDetailsBySequence(details);
		return details;
	}

	/**
	 * Removes a nutrient statement.
	 *
	 * @param nutrientStatementNumber Current nutrient statement to be deleted.
	 */
	@CoreTransactional
	public void deleteNutrientStatement(List<Long> deptList, long nutrientStatementNumber) {

		// Grab the nutrients statement with that number.
		NutrientStatementHeader nutrientStatementHeader =
				this.nutrientStatementHeaderRepository.findOne(nutrientStatementNumber);
		if (nutrientStatementHeader == null) {
			throw new IllegalArgumentException(String.format(NutrientsService.NUTRIENT_STATEMENT_NOT_FOUND_ERROR,
					nutrientStatementNumber));
		}

		// See if this nutrient statement is in use still, and throw an error if it is.
		List<ScaleUpc> tiedUpcs = this.scaleUpcRepository.findByNutrientStatement(nutrientStatementNumber);
		if (!tiedUpcs.isEmpty()) {
			throw new IllegalArgumentException(String.format(NutrientsService.NUTRIENT_STATEMENT_IN_USE_ERROR,
					nutrientStatementNumber));
		}

		// If the code is an A, that means it hasn't been added to the scales system
		// yet so it can be deleted completely.
		if (nutrientStatementHeader.getStatementMaintenanceSwitch() == ADD_MAINT_SW) {
			this.nutrientStatementHeaderRepository.delete(nutrientStatementNumber);
		} else {
			int sequenceNumber = 1;

			List<IngredientNutrientStatementWork> departmentsToDelete = new ArrayList<>(deptList.size());

			for (long deptNumber : deptList) {
				IngredientNutrientStatementWork ingredientNutrientStatementWork = new IngredientNutrientStatementWork();
				IngredientNutrientStatementWorkKey ingredientNutrientStatementWorkKey = new IngredientNutrientStatementWorkKey();
				ingredientNutrientStatementWorkKey.setRecTypCode(NutrientsService.NUTRIENT_STATEMETN_DELETE_RECORD_TYPE);
				ingredientNutrientStatementWorkKey.setSequenceNumber(sequenceNumber);
				ingredientNutrientStatementWorkKey.setStatementNumber(nutrientStatementNumber);
				ingredientNutrientStatementWork.setDeptNumber(deptNumber);
				ingredientNutrientStatementWork.setKey(ingredientNutrientStatementWorkKey);
				ingredientNutrientStatementWork.setUserId(userInfo.getUserId());
				departmentsToDelete.add(ingredientNutrientStatementWork);
				sequenceNumber++;
			}
			nutrientStatementHeader.setStatementMaintenanceSwitch(DELETE_MAINT_SW);
			nutrientStatementHeader.setEffectiveDate(LocalDate.now());
			this.ingredientNutrientStatementWorkRepository.save(departmentsToDelete);
			this.nutrientStatementHeaderRepository.save(nutrientStatementHeader);
		}
	}

	/**
	 * Checks whether or not the statement number is available to add.
	 *
	 * @param nutrientStatementNumber the nutrient statement number to be adding.
	 * @return String that determines whether it is available or not.
	 */
	public String searchForAvailableNutrientStatement(Long nutrientStatementNumber) {
		NutrientStatementHeader nutrientStatementHeader;
		if(nutrientStatementNumber != null) {
			nutrientStatementHeader = this.nutrientStatementHeaderRepository.findOne(nutrientStatementNumber);
		} else {
			return "{\"data\" : \"Not available\"}";
		}
		if(nutrientStatementHeader == null){
			return "{\"data\" : \"Available\"}";
		} else if(nutrientStatementHeader.getStatementMaintenanceSwitch() == NutrientsService.DELETE_MAINT_SW){
			return "{\"data\" : \"Cannot add because this is already scheduled to be deleted.\"}";
		} else {
			return "{\"data\" : \"Not available\"}";
		}
	}

	/**
	 * Checks whether or not the nutrient code is available to add.
	 *
	 * @param nutrientCode the nutrient code to be adding.
	 * @return String that determines whether it is available or not.
	 */
	public String searchForAvailableNutrientCode(Long nutrientCode) {
		Nutrient nutrient;
		if(nutrientCode != null) {
			nutrient = this.repository.findOne(nutrientCode);
		} else {
			return "{\"data\" : \"Not available\"}";
		}
		if(nutrient == null || nutrient.getMaintenanceSwitch() == 'D'){
			return "{\"data\" : \"Available\"}";
		} else {
			return "{\"data\" : \"Not available\"}";
		}
	}

	/**
	 * Applies the rounding rules to a nutrient statement detail record. It will round the amount and, if appropriate,
	 * recalculate the percent daily value.
	 *
	 * @param nutrientStatementDetail The detail record to apply the rounding rule to.
	 * @return The modified nutrient statement detail record.
	 */
	public NutrientStatementDetail applyRoundingRuleToNutrient(NutrientStatementDetail nutrientStatementDetail) {

		long nutrientCode = nutrientStatementDetail.getKey().getNutrientLabelCode();
		List<NutrientRoundingRule> roundingRules = this.roundingRuleRepository.findByKeyNutrientCode((int)nutrientCode);
		for (NutrientRoundingRule rule : roundingRules){
			if(nutrientStatementDetail.getNutrientStatementQuantity() >= rule.getKey().getLowerBound() &&
					nutrientStatementDetail.getNutrientStatementQuantity() < rule.getUpperBound()){
				// Get the rounded amount
				double amount = this.getRoundedValueForNutrient(nutrientStatementDetail.getNutrientStatementQuantity(),
						rule.getIncrementQuantity());
				nutrientStatementDetail.setNutrientStatementQuantity(amount);

				// Set the percent daily value
				if (nutrientStatementDetail.getNutrient().getRecommendedDailyAmount() > 0) {
					nutrientStatementDetail.setNutrientDailyValue(this.getPercentDailyValue(amount,
							nutrientStatementDetail.getNutrient().getRecommendedDailyAmount()));
				}
				return nutrientStatementDetail;
			}
		}

		// if a rule was not found, send 0 as increment
		double amount = this.getRoundedValueForNutrient(nutrientStatementDetail.getNutrientStatementQuantity(), 0);
		nutrientStatementDetail.setNutrientStatementQuantity(amount);

		// Set the percent daily value
		if (nutrientStatementDetail.getNutrient().getRecommendedDailyAmount() > 0) {
			nutrientStatementDetail.setNutrientDailyValue(this.getPercentDailyValue(amount,
					nutrientStatementDetail.getNutrient().getRecommendedDailyAmount()));
		}
		return nutrientStatementDetail;
	}

	/**
	 * Helper method that takes in an actual quantity and an increment quantity to determine what should be done to the
	 * actual quantity based on the incremental value. There are four options:
	 * 1. increment quantity is 0 (return actual)
	 * 2. actual is divisible by increment (return actual)
	 * 3. actual is less than half-way between an increment (return the next lowest increment)
	 * 4. actual is at least half-way between an increment (return the next higher increment)
	 *
	 * @param actualNutrientStatementQuantity Actual quantity to compare increment value to.
	 * @param incrementQuantity Incremental value to evaluate the actual quantity on.
	 * @return The calculated value.
	 */
	private Double getRoundedValueForNutrient(double actualNutrientStatementQuantity, double incrementQuantity) {

		this.validateNutrientValueRange(actualNutrientStatementQuantity);

		if(incrementQuantity == 0){
			return actualNutrientStatementQuantity;
		}
		BigDecimal actual = BigDecimal.valueOf(actualNutrientStatementQuantity);
		BigDecimal increment = BigDecimal.valueOf(incrementQuantity);
		BigDecimal valueToReturn;
		BigDecimal moddedValue = actual.remainder(increment);
		if(moddedValue.equals(BigDecimal.ZERO)){
			valueToReturn = actual;
		} else if(moddedValue.compareTo(increment.divide(BigDecimal.valueOf(2), BigDecimal.ROUND_HALF_UP)) < 0){
			valueToReturn = actual.subtract(moddedValue);
		} else {
			valueToReturn = actual.add(increment.add(moddedValue.negate()));
		}
		return valueToReturn.doubleValue();
	}

	/**
	 * Does the most basic validation of a nutrient quantity. It cannot have a precision greater than one decimal
	 * place and cannot be greater than 9999.
	 *
	 * @param actualNutrientStatementQuantity The quantity to check.
	 */
	private void validateNutrientValueRange(double actualNutrientStatementQuantity) {

		BigDecimal actual = BigDecimal.valueOf(actualNutrientStatementQuantity);
		if(actual.scale() > 1){
			throw new IllegalArgumentException(QUANTITY_PRECISION_ERROR);
		} else if(actualNutrientStatementQuantity > 9999){
			throw new IllegalArgumentException(QUANTITY_MAX_ERROR);
		}

	}

	/**
	 * Calculates a the percent daily value a certain amount is for a nutrient.
	 *
	 * @param quantity The amount of a nutrient there is.
	 * @param percentDailyValue The recommended percent daily value.
	 * @return The calculated percent daily value.
	 */
	private long getPercentDailyValue(double quantity, long percentDailyValue) {
		return Math.round((quantity / Double.valueOf(percentDailyValue)) * 100);
	}

	/**
	 * Checks to see if a statement number is valid for a new statement.
	 *
	 * @param statementNumber The statement number to check.
	 * @throws IllegalArgumentException Any reason for the number to be ivalid will be thrown as an exception.
	 */
	private void validateNewStatementNumber(long statementNumber) throws IllegalArgumentException {

		// See if a statement already exists with that number.
		NutrientStatementHeader header = this.nutrientStatementHeaderRepository.findOne(statementNumber);
		if (header != null) {

			// If it does, but it's marked for delete, send one error message.
			if (header.getStatementMaintenanceSwitch() == NutrientsService.DELETE_MAINT_SW) {
				String message = String.format(NutrientsService.NUTRIENT_STATEMENT_PENDING_DELETE_ERROR,
						statementNumber);
				NutrientsService.logger.debug(message);
				throw new IllegalArgumentException();
			}
			else {
			// If it's just there, throw a different error.
				String message = String.format(NutrientsService.STATEMENT_EXISTS_ERROR, statementNumber);
				NutrientsService.logger.debug(message);
				throw new IllegalArgumentException(message);
			}
		}

		// Since these nutrient statements follow the same rules as a PLU (they have to be able to be
		// placed in the "PLU" part of the pre-digit two), it follows the same rules as a PLU.
		if (!UpcUtils.isPlu(statementNumber)) {
			String message = String.format(NutrientsService.INVALID_STATEMENT_NUMBER_ERROR, statementNumber);
			NutrientsService.logger.debug(message);
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Validates the header portion of a NutrientStatementHeader before saving.
	 *
	 * @param nutrientStatementHeader The NutrientStatementHeader to validate.
	 * @throws IllegalArgumentException Any error found will throw an IllegalArgumentException
	 */
	private void validateNutrientStatementHeader(NutrientStatementHeader nutrientStatementHeader)
			throws IllegalArgumentException {

		// Make sure the servings per container is between 1 and 999
		if (nutrientStatementHeader.getServingsPerContainer() < NutrientsService.MIN_SERVINGS_PER_CONTAINER ||
				nutrientStatementHeader.getServingsPerContainer() > NutrientsService.MAX_SERVINGS_PER_CONTAINER) {
			String message = String.format(
					NutrientsService.INVALID_SERVINGS_PER_CONTAINER_ERROR,
					nutrientStatementHeader.getServingsPerContainer());
			NutrientsService.logger.error(message);
			throw new IllegalArgumentException(message);
		}

		// Make sure the units of measure are set to the correct types of codes.
		if (nutrientStatementHeader.getNutrientCommonUom().getSystemOfMeasure() != NutrientUom.IMPERIAL) {
			NutrientsService.logger.error(NutrientsService.IMPERIAL_UOM_MISMATCH_ERROR);
			throw new IllegalArgumentException(NutrientsService.IMPERIAL_UOM_MISMATCH_ERROR);
		}

		if (nutrientStatementHeader.getNutrientMetricUom().getSystemOfMeasure() != NutrientUom.METRIC) {
			NutrientsService.logger.error(NutrientsService.METRIC_UOM_MISMATCH_ERROR);
			throw new IllegalArgumentException(NutrientsService.METRIC_UOM_MISMATCH_ERROR);
		}

		// The units of measure must have the same form.
		if (nutrientStatementHeader.getNutrientCommonUom().getForm() !=
				nutrientStatementHeader.getNutrientMetricUom().getForm()) {
			NutrientsService.logger.error(NutrientsService.STATE_MISMATCH_ERROR);
			throw new IllegalArgumentException(NutrientsService.STATE_MISMATCH_ERROR);
		}
	}

	/**
	 * Create alert requests when update nutrient statement.
	 * @param nutrientStatementNumber nutrient statement number.
	 */
	public void createProductUpdateAlertForUpdateNutrientStatement(long nutrientStatementNumber){
		List<ScaleUpc> tiedUpcs = this.scaleUpcRepository.findByNutrientStatement(nutrientStatementNumber);
		if (CollectionUtils.isNotEmpty(tiedUpcs)) {
			//one nutrition statement can only associate to one plu upc.
			Long upc = tiedUpcs.get(0).getUpc();
			//find the product of the upc
			SellingUnit sellingUnit = this.sellingUnitRepository.findOne(upc);

			String alertKey = String.format(NutrientsService.PRODUCT_ID_AS_ALERT_KEY_CONVERSION,sellingUnit.getProdId());
			AlertStaging alertStaging = this.alertService.findByAlertTypeCDAndAlertStatusCDAndAlertKey(
					ALERT_TYPE_PRODUCT_UPDATE, AlertStaging.AlertStatusCD.ACTIVE.getName(), alertKey);

			if (alertStaging != null && alertStaging.getAlertID() > 0){
				alertService.updateProductUpdateAlertForUpdateNutrient(alertStaging.getAlertID(), alertStaging.getAlertDataTxt());
			}else{
				alertService.createProductUpdateAlertForUpdateNutrient(alertKey, StringUtils.trim(sellingUnit
						.getProductMaster()
						.getClassCommodity
								().geteBMid()));

			}
		}
	}

	/**
	 * Find by nutrient code pageable result.
	 *
	 * @param uomCode the nutrient codes
	 * @param includeCount  the include count
	 * @param page          the page
	 * @param pageSize      the page size
	 * @return the pageable result
	 */
	public PageableResult<Nutrient> findByUomCode(long uomCode, boolean includeCount, int page,
													   int pageSize){

		Pageable uomRequest = new PageRequest(page, pageSize);

		NutrientsService.logger.info(String.format(UOM_SEARCH_LOG_MESSAGE, uomCode));

		return includeCount ? this.findByUomCodeWithCounts(uomCode, uomRequest) :
				this.findByUomCodeWithoutCounts(uomCode, uomRequest);
	}

	/**
	 * Find by uom code pageable result.
	 * @param uomCode the nutrient codes
	 * @param uomRequest The HTTP request that initiated this call
	 * @return the pageable result
	 */
	private PageableResult<Nutrient> findByUomCodeWithoutCounts(long uomCode,
																	 Pageable uomRequest) {
		List<Nutrient> data = this.repository.findByUomCode(uomCode, uomRequest);

		return new PageableResult<>(uomRequest.getPageNumber(), data);
	}

	/**
	 * Find by uom code with count pageable result.
	 *
	 * @param uomCode the uom codes
	 * @param uomRequest The HTTP request that initiated this call
	 * @return the pageable result
	 */
	private PageableResult<Nutrient> findByUomCodeWithCounts(long uomCode, Pageable uomRequest) {

		Page<Nutrient> data = this.repositoryWithCount.findByUomCode(uomCode, uomRequest);

		return new PageableResult<>(uomRequest.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
	}

    /**
     * Find by nutrient code pageable result.
     *
     * @param uomCode the nutrient codes
     * @param includeCount  the include count
     * @param page          the page
     * @param pageSize      the page size
     * @return the pageable result
     */
    public PageableResult<NutrientStatementHeader> findStatementByUomCode(long uomCode, boolean includeCount, int page,
                                                  int pageSize){
       Pageable uomRequest = new PageRequest(page, pageSize);

       NutrientsService.logger.info(String.format(UOM_STATEMENT_SEARCH_LOG_MESSAGE, uomCode));

       return includeCount ? this.findStatementByUomCodeWithCounts(uomCode, uomRequest) :
                this.findStatementByUomCodeWithoutCounts(uomCode, uomRequest);
    }

    /**
     * Find by uom code pageable result.
     * @param uomCode the nutrient codes
     * @param uomRequest The HTTP request that initiated this call
     * @return the pageable result
     */
    private PageableResult<NutrientStatementHeader> findStatementByUomCodeWithoutCounts(long uomCode,
                                                                Pageable uomRequest) {
		Page<NutrientStatementHeader> data = this.nutrientStatementHeaderRepository.findByUomMetricCodeIsOrUomCommonCodeIs(uomCode, uomCode, uomRequest);

        return new PageableResult<>(uomRequest.getPageNumber(), data);
    }

    /**
     * Find by uom code with count pageable result.
     *
     * @param uomCode the uom codes
     * @param uomRequest The HTTP request that initiated this call
     * @return the pageable result
     */
    private PageableResult<NutrientStatementHeader> findStatementByUomCodeWithCounts(long uomCode, Pageable uomRequest) {

		Page<NutrientStatementHeader> data =  this.nutrientStatementHeaderRepositoryWithCount.findByUomMetricCodeIsOrUomCommonCodeIs(uomCode, uomCode, uomRequest);

        return new PageableResult<>(uomRequest.getPageNumber(),
                data.getTotalPages(),
                data.getTotalElements(),
                data.getContent());
    }

	/**
	 * Check NLEA16 nutrient statement is exist.
	 * @param nutrientStatementNumber Nutrient statement number.
	 * @return check result.
	 */
	public Boolean isNLEA16NutrientStatementExists (Long nutrientStatementNumber){
		List<NutrientStatementPanelHeader> nutrientStatementPanelHeaders =
				nutrientStatementPanelHeaderRepository.findBySourceSystemReferenceIdAndStatementMaintenanceSwitchNotAndSourceSystemId(nutrientStatementNumber.toString(),
						NutrientStatementPanelHeader.ACTIVE_SW_Y,
						NutrientStatementPanelHeader.SRC_SYSTEM_ID_17);
		if (nutrientStatementPanelHeaders !=null && !nutrientStatementPanelHeaders.isEmpty()){
			return true;
		}
		return false;
	}
}
