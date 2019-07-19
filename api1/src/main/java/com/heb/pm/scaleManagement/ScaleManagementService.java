/*
 * UpcPluMaintenanceService
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.scaleManagement;

import com.heb.pm.CoreTransactional;
import com.heb.pm.Hits;
import com.heb.pm.entity.IngredientStatementHeader;
import com.heb.pm.entity.NutrientStatementHeader;
import com.heb.pm.entity.ScaleUpc;
import com.heb.pm.repository.*;
import com.heb.scaleMaintenance.model.ScaleMaintenanceProduct;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Holds all business logic related to scale upc.
 *
 * @author m314029
 * @since 2.0.8
 */
@Service
public class ScaleManagementService {
	private static final Logger logger = LoggerFactory.getLogger(ScaleManagementService.class);

	private static final String DESCRIPTION_REGEX = "%%%s%%";
	private static final char UPDATE_MAINT_FUNCTION = 'C';
	private static final char ADD_MAINT_FUNCTION = 'D';
	private static final int PAGE_SIZE = 100;

	private static final String SCALE_MANAGEMENT_CODE_EXPORT_HEADER =
			"\"PLU\",\"UPC\",\"Product Description Line 1\",\"Product Description Line 2\",\"Product Description Line 3\",\"Product Description Line 4\",\"Ingredient Statement Number\",\"Nutrient Statement Number\",\"Effective Date\"";
	private static final String TEXT_EXPORT_FORMAT = "\"%s\",";
	private static final String NEWLINE_TEXT_EXPORT_FORMAT = "\n";

	// string code constants that are returned from the front end
	private static final String ACTION_CODE = "action code";
	private static final String FORMAT_CODE_ONE = "1";
	private static final String FORMAT_CODE_TWO = "2";
	private static final String GRAPHICS_CODE = "graphics code";
	private static final String INGREDIENT_STATEMENT = "ingredient statement";

	@Autowired
	private ScaleUpcRepositoryWithCount repositoryWithCount;

	@Autowired
	private ScaleUpcRepository repository;

	@Autowired
	private ScaleGraphicsCodeRepository graphicsCodeRepository;

	@Autowired
	private ScaleActionCodeRepository scaleActionCodeRepository;

	@Autowired
	private ScaleLabelFormatRepository scaleLabelFormatRepository;

	@Autowired
	private NutrientStatementHeaderRepository nutrientStatementHeaderRepository;

	@Autowired
	private IngredientStatementHeaderRepository ingredientStatementHeaderRepository;

	@Autowired
	private ScaleGraphicsCodeService scaleGraphicsCodeService;

	@Autowired
	private ScaleActionCodeService scaleActionCodeService;

	@Autowired
	private ScaleLabelFormatService scaleLabelFormatService;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private ScaleMaintenanceConverter scaleMaintenanceConverter;

	// These are the values that are set when a UPC does not have an ingredient statement attached to it.
	private static final long[] EMPTY_SCALE_INGREDIENT_STATEMENT_NUMBERS = {0, 9999};

	/**
	 * Searches for scale upc records based on a list of PLUs.
	 *
	 * @param pluList The list of PLUs to search for.
	 * @param includeCount Tells the repository whether it needs to get the count or not.
	 * @param page Current page.
	 * @return A list of scale upc records matching the sent list of PLUs.
	 */
	public PageableResult<ScaleUpc> findByPlu(List<Long> pluList, boolean includeCount, int page){
		Pageable scaleUpcRequest = new PageRequest(page, ScaleManagementService.PAGE_SIZE, ScaleUpc.getDefaultSort());
		List<Long> upcList = pluList.stream().map(this::convertPluToUpc).collect(Collectors.toList());

		PageableResult<ScaleUpc> results = includeCount ? this.findByPluWithCount(upcList, scaleUpcRequest) :
				this.findByPluWithoutCount(upcList, scaleUpcRequest);
		for(ScaleUpc result : results.getData()){
			result.setEnglishDescriptionOne(result.getEnglishDescriptionOne().trim());
			result.setEnglishDescriptionTwo(result.getEnglishDescriptionTwo().trim());
			result.setEnglishDescriptionThree(result.getEnglishDescriptionThree().trim());
			result.setEnglishDescriptionFour(result.getEnglishDescriptionFour().trim());
			result.setSpanishDescriptionOne(result.getSpanishDescriptionOne().trim());
			result.setSpanishDescriptionTwo(result.getSpanishDescriptionTwo().trim());
			result.setSpanishDescriptionThree(result.getSpanishDescriptionThree().trim());
			result.setSpanishDescriptionFour(result.getSpanishDescriptionFour().trim());

		}
		return results;
	}

	/**
	 * Searches for scale upc records base a list of upcs with the count.
	 *
	 * @param upcList List of upcs to search for.
	 * @param scaleUpcRequest The Scale Upc Request.
	 * @return A pageable Result that contains the pagenumber, total pages, total elements, and the content.
	 */
	private PageableResult<ScaleUpc> findByPluWithCount(List<Long> upcList, Pageable scaleUpcRequest) {
		Page<ScaleUpc> data = this.repositoryWithCount.findByUpcIn(upcList, scaleUpcRequest);

		return new PageableResult<>(scaleUpcRequest.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Searches for scale upc records base a list of upcs without the count.
	 *
	 * @param upcList List of Upcs to search for.
	 * @param scaleUpcRequest The Scale Upc Request.
	 * @return A pageable result that contains the page number and the data.
	 */
	private PageableResult<ScaleUpc> findByPluWithoutCount(List<Long> upcList, Pageable scaleUpcRequest) {
		List<ScaleUpc> data = this.repository.findByUpcIn(upcList, scaleUpcRequest);

		return new PageableResult<>(scaleUpcRequest.getPageNumber(), data);
	}

	/**
	 * Searches for scale upc records based on a wildcard description search.
	 *
	 * @param description The description to search for.
	 * @param includeCount Tells the repository whether it needs to get the count or not.
	 * @param page Current page.
	 * @return A list of scale upc records matching the wildcard description.
	 */
	public PageableResult<ScaleUpc> findByDescription(String description, boolean includeCount, int page ) {
		Pageable scaleUpcRequest = new PageRequest(page, ScaleManagementService.PAGE_SIZE, ScaleUpc.getDefaultSort());

		return includeCount ? this.findByDescriptionWithCount(String.format(DESCRIPTION_REGEX,
				description.toUpperCase()), scaleUpcRequest) : this.findByDescriptionWithoutCount(
				String.format(DESCRIPTION_REGEX, description.toUpperCase()), scaleUpcRequest);
	}

	/**
	 * Searches for scale upc by description with the count
	 *
	 * @param formattedString The formatted description to be searched for.
	 * @param scaleUpcRequest The Scale Upc Request.
	 * @return A Pageable Result that contains the page number, total pages, total elements, content.
	 */
	private PageableResult<ScaleUpc> findByDescriptionWithCount(String formattedString, Pageable scaleUpcRequest) {
		Page<ScaleUpc> data = this.repositoryWithCount.
				findByEnglishDescriptionOneContainsOrEnglishDescriptionTwoContainsOrEnglishDescriptionThreeContainsOrEnglishDescriptionFourContains(formattedString, formattedString,
						formattedString, formattedString, scaleUpcRequest);

		return new PageableResult<>(scaleUpcRequest.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Searches for scale upc by description without the count.
	 *
	 * @param formattedString The formatted description to be searched for.
	 * @param scaleUpcRequest The Scale Upc Request.
	 * @return The Pageable Result that contains the page number and the data.
	 */
	private PageableResult<ScaleUpc> findByDescriptionWithoutCount(String formattedString, Pageable scaleUpcRequest) {
		List<ScaleUpc> data = this.repository.
                findByEnglishDescriptionOneContainsOrEnglishDescriptionTwoContainsOrEnglishDescriptionThreeContainsOrEnglishDescriptionFourContains(formattedString, formattedString,
						formattedString, formattedString, scaleUpcRequest);

		return new PageableResult<>(scaleUpcRequest.getPageNumber(), data);
	}

	/**
	 * Return a plu converted into a upc.
	 *
	 * @param plu The plu to transform.
	 * @return A plu converted into a upc.
	 */
	private long convertPluToUpc(Long plu){
		return plu * 100000 + 20000000000L;
	}

	/**
	 * Returns the Hits count with match and non-match, along with non-match plus from the input list.
	 *
	 * @param pluList The list of PLUs to search for.
	 * @return Hits for the pluList.
	 */
	public Hits findHitsByPluList(List<Long> pluList) {
		List<Long> upcList = pluList.stream().map(this::convertPluToUpc).collect(Collectors.toList());
		List<ScaleUpc> scaleUpcList = this.repositoryWithCount.findAll(upcList);
		List<Long> hitPluList = scaleUpcList.stream().map(ScaleUpc::getPlu).collect(Collectors.toList());
		return Hits.calculateHits(pluList, hitPluList);
	}

	private static final String UPC_NOT_FOUND_ERROR = "UPC %d not found";
	private static final String NUTRIENT_STATEMENT_NOT_FOUND_ERROR = "Nutrient statement %d not found";
	private static final String INGREDIENT_STATEMENT_NOT_FOUND_ERROR = "Ingredient statement %d not found";
	private static final String LABEL_FORMAT_CODE_NOT_FOUND_ERROR = "%d is not a valid label format code";
	private static final String ACTION_CODE_NOT_FOUNT_ERROR = "%d is not a valid action code";
	private static final String GRAPHICS_CODE_NOT_FOUND_ERROR = "%d is not a valid graphics code";

	/**
	 * Updates the scale upc.
	 *
	 * @param scaleUpc The ScaleUpc
	 * @return The updated scale upc.
	 */
	@CoreTransactional
	public ScaleUpc update(ScaleUpc scaleUpc) {

		// Get the scale UPC they want to update from the DB.
		ScaleUpc originalScaleUpc = this.repository.findOne(scaleUpc.getUpc());
		if (originalScaleUpc == null) {
			throw new IllegalArgumentException(String.format(ScaleManagementService.UPC_NOT_FOUND_ERROR,
					scaleUpc.getUpc()));
		}

		// Check to make sure the various codes are valid.
		if(this.scaleLabelFormatRepository.findOne(scaleUpc.getLabelFormatOne()) == null) {
			throw new IllegalArgumentException(String.format(ScaleManagementService.LABEL_FORMAT_CODE_NOT_FOUND_ERROR,
					scaleUpc.getLabelFormatOne()));
		}

		if(this.scaleLabelFormatRepository.findOne(scaleUpc.getLabelFormatTwo()) == null){
			throw new IllegalArgumentException(String.format(ScaleManagementService.LABEL_FORMAT_CODE_NOT_FOUND_ERROR,
					scaleUpc.getLabelFormatTwo()));
		}

		if(this.scaleActionCodeRepository.findOne(scaleUpc.getActionCode()) == null){
			throw new IllegalArgumentException(String.format(ScaleManagementService.ACTION_CODE_NOT_FOUNT_ERROR,
					scaleUpc.getActionCode()));
		}

		if(this.graphicsCodeRepository.findOne(scaleUpc.getGraphicsCode()) == null){
			throw new IllegalArgumentException(String.format(ScaleManagementService.GRAPHICS_CODE_NOT_FOUND_ERROR,
					scaleUpc.getGraphicsCode()));
		}

		// Validate the nutrient statement.
		NutrientStatementHeader nutrientStatementHeader =
				this.validateNutrientStatementHeader(scaleUpc.getPlu(), scaleUpc.getNutrientStatement());

		// If it's not already pending maintenance, then update it.
		if(originalScaleUpc.getNutrientStatement() != scaleUpc.getNutrientStatement() &&
				nutrientStatementHeader != null &&
				nutrientStatementHeader.getStatementMaintenanceSwitch() != ADD_MAINT_FUNCTION) {

				nutrientStatementHeader.setStatementMaintenanceSwitch(UPDATE_MAINT_FUNCTION);
				nutrientStatementHeader.setEffectiveDate(LocalDate.now());
				this.nutrientStatementHeaderRepository.save(nutrientStatementHeader);
		}

		IngredientStatementHeader ingredientStatementHeader =
				this.validateIngredientStatement(scaleUpc.getIngredientStatement());

		// Validate the ingredient statement.
		if(originalScaleUpc.getIngredientStatement() != scaleUpc.getIngredientStatement()
				&& ingredientStatementHeader != null) {

			ingredientStatementHeader.setMaintenanceSwitch(true);
			ingredientStatementHeader.setMaintenanceDate(LocalDate.now());
			// If the code is an A,
			// that means it hasn't been added to the scales system yet so the code needs to stay 'A'.
			if(ingredientStatementHeader.getMaintenanceCode() != ADD_MAINT_FUNCTION) {
				ingredientStatementHeader.setMaintenanceCode(UPDATE_MAINT_FUNCTION);
			}
			this.ingredientStatementHeaderRepository.save(ingredientStatementHeader);

			IngredientStatementEvent ingredientStatementEvent = new IngredientStatementEvent(this,scaleUpc.getUpc(),  null, this.userInfo.getUserId());
			applicationEventPublisher.publishEvent(ingredientStatementEvent);
		}

		// Copy the values from the updated ScaleUpc to the one we just pulled from the DB.
		originalScaleUpc.setShelfLifeDays(scaleUpc.getShelfLifeDays());
		originalScaleUpc.setFreezeByDays(scaleUpc.getFreezeByDays());
		originalScaleUpc.setEatByDays(scaleUpc.getEatByDays());
		originalScaleUpc.setActionCode(scaleUpc.getActionCode());
		originalScaleUpc.setGraphicsCode(scaleUpc.getGraphicsCode());
		originalScaleUpc.setLabelFormatOne(scaleUpc.getLabelFormatOne());
		originalScaleUpc.setLabelFormatTwo(scaleUpc.getLabelFormatTwo());
		originalScaleUpc.setGrade(scaleUpc.getGrade());
		originalScaleUpc.setServiceCounterTare(scaleUpc.getServiceCounterTare());
		originalScaleUpc.setIngredientStatement(scaleUpc.getIngredientStatement());
		originalScaleUpc.setNutrientStatement(scaleUpc.getNutrientStatement());
		originalScaleUpc.setPrePackTare(scaleUpc.getPrePackTare());
		originalScaleUpc.setNetWeight(scaleUpc.getNetWeight());
		originalScaleUpc.setForceTare(scaleUpc.isForceTare());
		originalScaleUpc.setPriceOverride(scaleUpc.isPriceOverride());

		originalScaleUpc.setEnglishDescriptionOne(scaleUpc.getEnglishDescriptionOne().trim().toUpperCase());
		originalScaleUpc.setEnglishDescriptionTwo(scaleUpc.getEnglishDescriptionTwo().trim().toUpperCase());
		originalScaleUpc.setEnglishDescriptionThree(scaleUpc.getEnglishDescriptionThree().trim().toUpperCase());
		originalScaleUpc.setEnglishDescriptionFour(scaleUpc.getEnglishDescriptionFour().trim().toUpperCase());
		originalScaleUpc.setSpanishDescriptionOne(scaleUpc.getSpanishDescriptionOne().trim().toUpperCase());
		originalScaleUpc.setSpanishDescriptionTwo(scaleUpc.getSpanishDescriptionTwo().trim().toUpperCase());
		originalScaleUpc.setSpanishDescriptionThree(scaleUpc.getSpanishDescriptionThree().trim().toUpperCase());
		originalScaleUpc.setSpanishDescriptionFour(scaleUpc.getSpanishDescriptionFour().trim().toUpperCase());

		// Set the flags to send the data through the maintenance job.
		originalScaleUpc.setMaintFunction(ScaleManagementService.UPDATE_MAINT_FUNCTION);
		originalScaleUpc.setStripFlag(true);
		originalScaleUpc.setEffectiveDate(LocalDate.now());
		return this.repository.save(originalScaleUpc);
	}

	/**
	 * Maintenance the scale upc.
	 *
	 * @param scaleUpc The ScaleUpc
	 * @return The updated scale upc.
	 */
	@CoreTransactional
	public ScaleUpc sendMaintenance(ScaleUpc scaleUpc) {
		ScaleUpc originalScaleUpc = this.repository.findOne(scaleUpc.getUpc());
		if (originalScaleUpc == null) {
			throw new IllegalArgumentException(String.format(ScaleManagementService.UPC_NOT_FOUND_ERROR,
					scaleUpc.getUpc()));
		}
		originalScaleUpc.setMaintFunction(UPDATE_MAINT_FUNCTION);
		originalScaleUpc.setEffectiveDate(LocalDate.now());
		originalScaleUpc.setStripFlag(true);
		List<ScaleUpc> scaleUpcs = new ArrayList<>();
		scaleUpcs.add(originalScaleUpc);
		PLUMaintenanceEvent pluMaintenanceEvent = new PLUMaintenanceEvent(this, scaleUpcs,  this.userInfo.getUserId());
		applicationEventPublisher.publishEvent(pluMaintenanceEvent);
		return this.repository.save(originalScaleUpc);
	}
	/**
	 * Validates a nutrient statement number for a given PLU.
	 *
	 * @param plu The PLU the user is trying to set the statement number for.
	 * @param nutrientStatementNumber The statement number they are trying to set.
	 * @return The nutrient statement from the DB so that it may be further processed. This will
	 * be null if it is one of the fake nutrient statements.
	 */
	private NutrientStatementHeader validateNutrientStatementHeader(long plu, long nutrientStatementNumber) {

		// If it's one of the fake numbers, then it's OK.
		for (long fakeNutrientStatementNumber : IngredientStatementHeader.EMPTY_SCALE_NUTRIENT_STATEMENT_NUMBERS) {
			if (fakeNutrientStatementNumber == nutrientStatementNumber) {
				return null;
			}
		}

		// The nutrient statement number must match the PLU.
		if (plu != nutrientStatementNumber) {
			throw new IllegalArgumentException("Nutrition statement number must match PLU");
		}

		// If it matches, then go get the statement.
		NutrientStatementHeader nutrientStatementHeader =
				this.nutrientStatementHeaderRepository.findOne(nutrientStatementNumber);

		// Make sure they're actually trying to set it to a nutrient statement that exists.
		if (nutrientStatementHeader == null) {
			throw new IllegalArgumentException(String.format(
					ScaleManagementService.NUTRIENT_STATEMENT_NOT_FOUND_ERROR,
					nutrientStatementNumber));
		}

		// It's valid, so return the nutrient statement header
		return nutrientStatementHeader;
	}

	/**
	 * Validates an ingredient statement number.
	 *
	 * @param ingredientStatementNumber The ingredient statement number.
	 * @return The ingredient statement header from the DB so that it may be further processed. This will
	 * be null if it is one of those fake ingredient statements.
	 */
	private IngredientStatementHeader validateIngredientStatement(long ingredientStatementNumber) {

		// If it's one of the fake ones, then it's OK.
		for (long fakeIngredientStatementNumber : ScaleManagementService.EMPTY_SCALE_INGREDIENT_STATEMENT_NUMBERS) {
			if (fakeIngredientStatementNumber == ingredientStatementNumber) {
				return null;
			}
		}

		// Make sure it exists.
		IngredientStatementHeader ingredientStatementHeader
				= this.ingredientStatementHeaderRepository.findOne(ingredientStatementNumber);

		if (ingredientStatementHeader == null) {
			throw new IllegalArgumentException(String.format(
					ScaleManagementService.INGREDIENT_STATEMENT_NOT_FOUND_ERROR,
					ingredientStatementNumber));
		}

		return ingredientStatementHeader;
	}
	/**
	 * Bulk updates a list of 100 upc's
	 * @param scaleManagementBulkUpdate Bulk update that contains list of scale upc's,
	 * the attribute to be updated and the value
	 * @return updated list of scale upc's
	 */
	@CoreTransactional
	public List<ScaleUpc> bulkUpdate(ScaleManagementBulkUpdate scaleManagementBulkUpdate){

		// If there's nothing to update, just return.
		if (scaleManagementBulkUpdate.getScaleUpcs().isEmpty()) {
			return scaleManagementBulkUpdate.getScaleUpcs();
		}

		// If they are updating action code then make sure the code is valid before
		// bothering to go ahead.
		if (scaleManagementBulkUpdate.getAttribute().equals(
				ScaleManagementBulkUpdate.BulkUpdateAttribute.ACTION_CODE)) {
			if(this.scaleActionCodeRepository.findOne(Long.parseLong(scaleManagementBulkUpdate.getValue())) == null) {
				throw new IllegalArgumentException(String.format(ScaleManagementService.ACTION_CODE_NOT_FOUNT_ERROR,
						Long.parseLong(scaleManagementBulkUpdate.getValue())));
			}
		}

		// If they are updating graphics code then make sure the code is valid before
		// bothering to go ahead.
		if (scaleManagementBulkUpdate.getAttribute().equals(
				ScaleManagementBulkUpdate.BulkUpdateAttribute.GRAPHICS_CODE)) {
			if(this.graphicsCodeRepository.findOne(Long.parseLong(scaleManagementBulkUpdate.getValue())) == null){
				throw new IllegalArgumentException(String.format(ScaleManagementService.GRAPHICS_CODE_NOT_FOUND_ERROR,
						Long.parseLong(scaleManagementBulkUpdate.getValue())));
			}
		}

		// If they are updating ingredient statement number then make sure the code is valid before
		// bothering to go ahead.
		if (scaleManagementBulkUpdate.getAttribute().equals(
				ScaleManagementBulkUpdate.BulkUpdateAttribute.INGREDIENT_STATEMENT_NUMBER)) {
			this.validateIngredientStatement(Long.parseLong(scaleManagementBulkUpdate.getValue()));
		}

		// Will hold the list to update
		List<ScaleUpc> upcsToUpdate = new ArrayList<>();

		for(ScaleUpc scaleUpc : scaleManagementBulkUpdate.getScaleUpcs()) {

			// Grab the scale UPC from the DB, this is the one we'll update.
			ScaleUpc originalScaleUpc = this.repository.findOne(scaleUpc.getUpc());
			if (originalScaleUpc == null) {
				throw new IllegalArgumentException(String.format(ScaleManagementService.UPC_NOT_FOUND_ERROR,
						scaleUpc.getUpc()));
			}
			upcsToUpdate.add(originalScaleUpc);

			switch (scaleManagementBulkUpdate.getAttribute()) {
				case SHELF_LIFE_DAYS: {
					originalScaleUpc.setShelfLifeDays(Integer.parseInt(scaleManagementBulkUpdate.getValue()));
					break;
				}
				case FREEZE_BY_DAYS: {
					originalScaleUpc.setFreezeByDays(Integer.parseInt(scaleManagementBulkUpdate.getValue()));
					break;
				}
				case SERVICE_COUNTER_TARE: {
					originalScaleUpc.setServiceCounterTare(Double.parseDouble(scaleManagementBulkUpdate.getValue()));
					break;
				}
				case EAT_BY_DAYS: {
					originalScaleUpc.setEatByDays(Integer.parseInt(scaleManagementBulkUpdate.getValue()));
					break;
				}
				case PREPACK_TARE: {
					originalScaleUpc.setPrePackTare(Double.parseDouble(scaleManagementBulkUpdate.getValue()));
					break;
				}
				case GRAPHICS_CODE: {
					originalScaleUpc.setGraphicsCode(Long.parseLong(scaleManagementBulkUpdate.getValue()));
					break;
				}
				case ACTION_CODE: {
					originalScaleUpc.setActionCode(Long.parseLong(scaleManagementBulkUpdate.getValue()));
					break;
				}
				case FORCE_TARE: {
					originalScaleUpc.setForceTare(Boolean.valueOf(scaleManagementBulkUpdate.getValue()));
					break;
				}
				case ENGLISH_LINE_1: {
					originalScaleUpc.setEnglishDescriptionOne(
							scaleManagementBulkUpdate.getValue().trim().toUpperCase());
					break;
				}
				case ENGLISH_LINE_2: {
					originalScaleUpc.setEnglishDescriptionTwo(
							scaleManagementBulkUpdate.getValue().trim().toUpperCase());
					break;
				}
				case ENGLISH_LINE_3: {
					originalScaleUpc.setEnglishDescriptionThree(
							scaleManagementBulkUpdate.getValue().trim().toUpperCase());
					break;
				}
				case ENGLISH_LINE_4: {
					originalScaleUpc.setEnglishDescriptionFour(
							scaleManagementBulkUpdate.getValue().trim().toUpperCase());
					break;
				}
				case INGREDIENT_STATEMENT_NUMBER: {
					originalScaleUpc.setIngredientStatement(Long.parseLong(scaleManagementBulkUpdate.getValue()));
					break;
				}
			}

			if (originalScaleUpc.getMaintFunction() != ADD_MAINT_FUNCTION) {
				originalScaleUpc.setMaintFunction(UPDATE_MAINT_FUNCTION);
			}
			originalScaleUpc.setEffectiveDate(LocalDate.now());
			originalScaleUpc.setStripFlag(true);
		}
		if(scaleManagementBulkUpdate.getAttribute().equals(ScaleManagementBulkUpdate.BulkUpdateAttribute.MAINTENANCE)){
			PLUMaintenanceEvent pluMaintenanceEvent = new PLUMaintenanceEvent(this, upcsToUpdate,  this.userInfo.getUserId());
			applicationEventPublisher.publishEvent(pluMaintenanceEvent);
		}
		return this.repository.save(upcsToUpdate);
	}

	/**
	 * This method returns the scale upc with the upc matching given upc.
	 *
	 * @param upc The UPC to search for a scale upc on.
	 * @return The scale upc found.
	 */
	public ScaleUpc findOne(long upc) {
		return this.repository.findOne(upc);
	}

	/**
	 * Streams a CSV of an code for a particular code type. This includes all UPCs & PLUs the code is part of,
	 * and all the descriptions, ingredient statement number, nutrient statement number, & effective date
	 * tied to each PLU.
	 * @param outputStream The output stream to write the CSV to.
	 * @param requestedCodeType    The type of code they are looking for
	 * @param requestedCode  The code the user requested data for.
	 * @param includeCounts include count of pages boolean.
	 * @param page          page number.
	 * @param pageSize      The number of records being asked for.
	 */
	public void streamScaleManagementCode(ServletOutputStream outputStream, String requestedCodeType, Long requestedCode,
										  boolean includeCounts, int page, int pageSize) {
		// Print out the header and the delegate the details.
		try {
			outputStream.println(SCALE_MANAGEMENT_CODE_EXPORT_HEADER);
		} catch (IOException e) {
			ScaleManagementService.logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
		this.streamSpecificScaleManagementCode(outputStream, requestedCodeType,  requestedCode, includeCounts, page, pageSize);

	}

	/**
	 * Gets the all the records for particular code type
	 * @param outputStream The output stream to write the CSV to.
	 * @param requestedCodeType    The type of code they are looking for
	 * @param requestedCode The code the user requested data for.
	 * @param includeCounts include count of pages boolean.
	 * @param page          page nummber.
	 * @param pageSize      The number of records being asked for.
	 */
	public void streamSpecificScaleManagementCode(ServletOutputStream outputStream, String requestedCodeType,
												 Long requestedCode, boolean includeCounts, int page, int pageSize){

		PageableResult<ScaleUpc> results = null;
		int totalNumberPages = 0;

		switch (requestedCodeType){
			case ACTION_CODE: {
				results = this.scaleActionCodeService.findPlusByActionCode(requestedCode, includeCounts, page);
				//streaming first page of info
                this.streamScaleManagementCodeDetails(outputStream, results.getData());
                //saving total page count because we lose this count once includeCounts is false
                totalNumberPages = results.getPageCount();
                //loop through the rest of the pages
				for (int i = results.getPage() +1; i < totalNumberPages; i++){
                    results = this.scaleActionCodeService.findPlusByActionCode(requestedCode, false, i);
                    this.streamScaleManagementCodeDetails(outputStream, results.getData());
                }
				break;
			}
			case FORMAT_CODE_ONE: {
				results = this.scaleLabelFormatService.findUpcsByFormatCodeOne(requestedCode, includeCounts, page, pageSize);
                this.streamScaleManagementCodeDetails(outputStream, results.getData());

                totalNumberPages = results.getPageCount();
                for (int i = results.getPage() +1; i < totalNumberPages; i++){
                    results = this.scaleLabelFormatService.findUpcsByFormatCodeOne(requestedCode, false, i, pageSize);
                    this.streamScaleManagementCodeDetails(outputStream, results.getData());
                }
				break;
			}
			case FORMAT_CODE_TWO: {
				results = this.scaleLabelFormatService.findUpcsByFormatCodeTwo(requestedCode, includeCounts, page, pageSize);
                this.streamScaleManagementCodeDetails(outputStream, results.getData());

                totalNumberPages = results.getPageCount();
                for (int i = results.getPage() +1; i < totalNumberPages; i++){
                    results = this.scaleLabelFormatService.findUpcsByFormatCodeTwo(requestedCode, false, i, pageSize);
                    this.streamScaleManagementCodeDetails(outputStream, results.getData());
                }
				break;
			}
			case GRAPHICS_CODE: {
				results = this.scaleGraphicsCodeService.findByGraphicsCode(requestedCode, includeCounts, page, pageSize);
                this.streamScaleManagementCodeDetails(outputStream, results.getData());

                totalNumberPages = results.getPageCount();
                for (int i = results.getPage() +1; i < totalNumberPages; i++){
                    results = this.scaleGraphicsCodeService.findByGraphicsCode(requestedCode, false, i, pageSize);
                    this.streamScaleManagementCodeDetails(outputStream, results.getData());
                }
				break;
			}
		}
	}
	/**
	 * Performs the actual work of streaming the code information to a CSV.
     * @param outputStream The output stream to write the CSV to.
     * @param results    One page of information
     */
	public void streamScaleManagementCodeDetails(ServletOutputStream outputStream, Iterable<ScaleUpc> results){
		StringBuilder graphicCodeStatementBuilder = new StringBuilder();

		try {
			for (ScaleUpc currRecord : results) {

				graphicCodeStatementBuilder.append(String.format(TEXT_EXPORT_FORMAT, currRecord.getPlu()));
				graphicCodeStatementBuilder.append(String.format(TEXT_EXPORT_FORMAT, currRecord.getUpc()));

				graphicCodeStatementBuilder.append(String.format(TEXT_EXPORT_FORMAT, currRecord.getEnglishDescriptionOne().trim()));
				graphicCodeStatementBuilder.append(String.format(TEXT_EXPORT_FORMAT, currRecord.getEnglishDescriptionTwo().trim()));
				graphicCodeStatementBuilder.append(String.format(TEXT_EXPORT_FORMAT, currRecord.getEnglishDescriptionThree()));
				graphicCodeStatementBuilder.append(String.format(TEXT_EXPORT_FORMAT, currRecord.getEnglishDescriptionFour()));

				graphicCodeStatementBuilder.append(String.format(TEXT_EXPORT_FORMAT, currRecord.getIngredientStatement()));
				graphicCodeStatementBuilder.append(String.format(TEXT_EXPORT_FORMAT, currRecord.getNutrientStatement()));
				graphicCodeStatementBuilder.append(String.format(TEXT_EXPORT_FORMAT, currRecord.getEffectiveDate()));

				graphicCodeStatementBuilder.append(NEWLINE_TEXT_EXPORT_FORMAT);
			}
			outputStream.print(graphicCodeStatementBuilder.toString());
		} catch (IOException e) {
			ScaleManagementService.logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Finds all scale upcs by a list of upcs.
	 *
	 * @param upcs List of upcs to retrieve data for.
	 * @return List of scale upcs matching the given upcs.
	 */
	public List<ScaleUpc> findByUpcs(List<Long> upcs) {
		return this.repository.findAll(upcs);
	}

	/**
	 * Converts a list of upcs into a list of scale maintenance product.
	 *
	 * @param upcs Upcs to look up.
	 * @return List of scale maintenance products matching the given upcs.
	 */
	public List<ScaleMaintenanceProduct> convertUpcsToScaleMaintenanceProduct(List<Long> upcs) {
		List<ScaleUpc> scaleUpcs = this.findByUpcs(upcs);
		return this.scaleMaintenanceConverter.convertScaleUpcsToScaleMaintenanceProducts(scaleUpcs);
	}
}
