/*
 *  IngredientStatementService
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.scaleManagement;

import com.heb.pm.CoreEntityManager;
import com.heb.pm.CoreTransactional;
import com.heb.pm.Hits;
import com.heb.pm.entity.*;
import com.heb.pm.repository.*;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import com.heb.util.list.LongPopulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Holds all business logic related for ingredient statements.
 * @author s573181
 * @since 2.2.0
 */
@Service
public class IngredientStatementService {

	private static final Logger logger = LoggerFactory.getLogger(IngredientStatementService.class);

	//@Value("${db2.region}")
	//db2oracle change vn00907
	@Value("${oracle.region}")
	private String databaseSchema;

	@Autowired
	private IngredientStatementHeaderRepository repository;

	@Autowired
	private IngredientStatementDetailRepository statementDetailRepository;

	@Autowired
	private IngredientStatementHeaderRepositoryWithCount repositoryWithCount;

	@Autowired
	@CoreEntityManager
	EntityManager entityManager;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private IngredientNutrientStatementWorkRepository ingredientNutrientStatementWorkRepository;

	@Autowired
	private IngredientRepository ingredientRepository;

	@Autowired
	private ScaleUpcRepository scaleUpcRepository;

	@Autowired
	private ScaleUpcRepositoryWithCount scaleUpcRepositoryWithCount;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	// Used to get consistent size lists to query runners.
	private LongPopulator longPopulator = new LongPopulator();

	private static final Long MAX_INGREDIENT_STATEMENT = 9999999L;
	private static final Double MAX_PERCENTAGE = 999.0;
	private static final int MAX_STATEMENT_SIZE = 9999999;
	private static final int MIN_INGREDIENT_STATEMENT = 0;
	private static final int DEFAULT_PAGE_SIZE = 100;
	private static final int DEFAULT_POPULATION_COUNT = 1000;

	private static final char ADD_CODE = 'A';
	private static final char UPDATE_CODE = 'C';
	private static final char DELETE_CODE = 'D';
	private static final String INGREDIENT_STATEMENT_DELETE_RECORD_TYPE = "INGRD";
	private static final String INGREDIENT_STATEMENT_EXPORT_HEADER = "\"Statement Number\",\"Ingredient Codes\",\"Ingredients\"";
	private static final String INGREDIENT_STATEMENT_EXPORT_FORMAT = "%s,\"%s\",\"%s\"";

	//db2oracle change vn00907 removed CLOB
	private static final String ORDERED_LIST_SEARCH_SQL =  "select pd_ingrd_stmt_no from ( " +
			"select pd_ingrd_stmt_no,  replace(replace(replace(cast(XMLSERIALIZE(CONTENT XMLAGG(XMLELEMENT(NAME \"x\",rtrim(pd_ingrd_cd)  )order by pd_ingrd_pct desc ) " +
			") as varchar(4000)),  '</x><x>', '|') , '<x>', '') , '</x>', '|') as ingrdcodes " +
			"from %s.sl_ingrd_stmt_dtl a " +
			"group by pd_ingrd_stmt_no ) a where ingrdcodes like :searchRegularExpression";
	private static final String ORDERED_LIST_PARAMETER_NAME = "searchRegularExpression";

	// Error messages
	private static final String INVALID_STATEMENT_NUMBER_ERROR = "Ingredient statements must be between 0 and 9999999.";
	private static final String INGREDIENT_STATEMENT_IN_USE_ERROR = "Ingredient statement %d is still in use.";
	private static final String INGREDIENT_STATEMENT_DOES_NOT_EXIST_ERROR = "Ingredient statement %d does not exist.";
	private static final String INGREDIENT_STATEMENT_INVALID_ERROR = "Ingredient statements 0 and 9999999 are reserved";

	/**
	 * Returns a pageable list of all ingredient statements.
	 *
	 * @param includeCounts Whether or not to include counts in the result.
	 * @param page The page of data to return.
	 * @param pageSize The maximum number of records to return.
	 * @return A PageableResult with IngredientStatementHeader records.
	 */
	public PageableResult<IngredientStatementHeader> findAll(boolean includeCounts, int page, int pageSize){

		PageRequest pageRequest = new PageRequest(page, pageSize, IngredientStatementHeader.getDefaultSort());

		PageableResult<IngredientStatementHeader> results;

		if(includeCounts){
			Page<IngredientStatementHeader> data = this.repositoryWithCount.findByMaintenanceCodeNot(IngredientStatementHeader.DELETE_CODE, pageRequest);
			results = new PageableResult<>(page, data.getTotalPages(),
					data.getTotalElements(), data.getContent());
		} else {
			List<IngredientStatementHeader> data = this.repository.findByMaintenanceCodeNot(IngredientStatementHeader.DELETE_CODE, pageRequest);
			results = new PageableResult<>(page, data);
		}
		return results;
	}

	/**
	 * Returns a pageable list of all ingredient statements.
	 *
	 * @param ingredientStatements a List of IngredientStatement codes.
	 * @param includeCounts Whether or not to include counts in the result.
	 * @param page The page of data to return.
	 * @param pageSize The maximum number of records to return.
	 * @return A PageableResult with IngredientStatementHeader records.
	 */
	public PageableResult<IngredientStatementHeader> findByIngredientStatement(
			List<Long> ingredientStatements, boolean includeCounts, int page, int pageSize){
		this.validateMaxLengthForIngredientStatementCodes(ingredientStatements);
		PageRequest pageRequest = new PageRequest(page, pageSize);
		PageableResult<IngredientStatementHeader> results;
		if(includeCounts){
			Page<IngredientStatementHeader> data = this.repositoryWithCount.findByStatementNumberInAndMaintenanceCodeNot(
					ingredientStatements, IngredientStatementHeader.DELETE_CODE, pageRequest);
			results = new PageableResult<>(page, data.getTotalPages(),
					data.getTotalElements(), data.getContent());
		} else {
			List<IngredientStatementHeader> data = this.repository.findByStatementNumberInAndMaintenanceCodeNot(
					ingredientStatements, IngredientStatementHeader.DELETE_CODE, pageRequest);
			results = new PageableResult<>(page, data);
		}
		return results;
	}

	/**
	 * Returns a pageable list of ingredient statements based on the ingredient description.
	 *
	 * @param ingredientDescription the ingredient description.
	 * @param includeCounts Whether or not to include counts in the result.
	 * @param page The page of data to return.
	 * @param pageSize The maximum number of records to return.
	 * @return A PageableResult with IngredientStatementHeader records.
	 */
	public PageableResult<IngredientStatementHeader> findByDescription(
			String ingredientDescription, boolean includeCounts, int page, int pageSize) {

		PageRequest pageRequest = new PageRequest(page, pageSize, IngredientStatementHeader.getDefaultSort());
		PageableResult<IngredientStatementHeader> results;
		if(includeCounts){
			Page<IngredientStatementHeader> data = this.repositoryWithCount.
					findDistinctByIngredientStatementDetailsIngredientIngredientDescriptionIgnoreCaseContainingAndMaintenanceCodeNot(
							ingredientDescription, IngredientStatementHeader.DELETE_CODE, pageRequest);
			results = new PageableResult<>(page, data.getTotalPages(), data.getTotalElements(), data.getContent());
		} else {
			List<IngredientStatementHeader> data = this.repository.
					findDistinctByIngredientStatementDetailsIngredientIngredientDescriptionIgnoreCaseContainingAndMaintenanceCodeNot(
							ingredientDescription, IngredientStatementHeader.DELETE_CODE, pageRequest);
			results = new PageableResult<>(page, data);
		}
		return results;
	}

	/**
	 * Returns a pageable list of ingredient statements searched by a list of ingredient codes.
	 *
	 * @param ingredientCodes a List of Ingredient Codes.
	 * @param includeCounts Whether or not to include counts in the result.
	 * @param page The page of data to return.
	 * @param pageSize The maximum number of records to return.
	 * @return A PageableResult with IngredientStatementHeader records.
	 */
	public PageableResult<IngredientStatementHeader> findByIngredientCode(
			List<String> ingredientCodes, boolean includeCounts, int page, int pageSize){

		this.validateMaxLengthForIngredientCodes(ingredientCodes);
		PageRequest pageRequest = new PageRequest(page, pageSize, IngredientStatementHeader.getDefaultSort());
		PageableResult<IngredientStatementHeader> results;
		if(includeCounts){
			
			Page<IngredientStatementHeader> data =
					this.repositoryWithCount.findByIngredientCodesMatchingAll(ingredientCodes, ingredientCodes.size(),
							IngredientStatementHeader.DELETE_CODE, pageRequest);
			
			results = new PageableResult<>(page, data.getTotalPages(),
					data.getTotalElements(), data.getContent());
			
			
		} else {
			List<IngredientStatementHeader> data =
					this.repository.findByIngredientCodesMatchingAll(ingredientCodes, ingredientCodes.size(),
							IngredientStatementHeader.DELETE_CODE, pageRequest);
			results = new PageableResult<>(page, data);
		}
		return results;
	}

	/**
	 * Returns a pageable list of ingredient statements searched by an ordered list of ingredientCodes
	 *
	 * @param ingredientCodes a list of ingredient codes.
	 * @param includeCounts Whether or not to include counts in the result.
	 * @param page The page of data to return.
	 * @param pageSize The maximum number of records to return.
	 * @return A PageableResult with IngredientStatementHeader records.
	 */
	public PageableResult<IngredientStatementHeader> findByOrderedIngredientCodes(
			List<String> ingredientCodes, boolean includeCounts, int page, int pageSize){

		this.validateMaxLengthForIngredientCodes(ingredientCodes);

		List<Long> convertedIngredientCodes =
				this.findIngredientStatementNumberByOrderedIngredientCodes(ingredientCodes, true);

		// Now that you have the list, go and fetch the details for those ingredient statements.
		PageRequest pageRequest = new PageRequest(page, pageSize);
		PageableResult<IngredientStatementHeader> results;

		if(includeCounts){
			Page<IngredientStatementHeader> data =
					this.repositoryWithCount.findByStatementNumberInAndMaintenanceCodeNot(convertedIngredientCodes,
							IngredientStatementHeader.DELETE_CODE, pageRequest);
			results = new PageableResult<>(page, data.getTotalPages(),
					data.getTotalElements(), data.getContent());
		} else {
			List<IngredientStatementHeader> data =
					this.repository.findByStatementNumberInAndMaintenanceCodeNot(convertedIngredientCodes,
							IngredientStatementHeader.DELETE_CODE, pageRequest);
			results = new PageableResult<>(page, data);
		}
		return results;
	}

	/**
	 * Adds a new IngredientStatementHeader with the current highest statement number plus 1 and percentage starting
	 * from the MAX_PERCENTAGE (999.9), -1 per ingredient.
	 *
	 * @param header The IngredientStatementHeader to be added.
	 * @return The IngredientStatementHeader to be added with the current highest statement number plus 1
	 */
	@CoreTransactional
	public IngredientStatementHeader add(IngredientStatementHeader header) {

		// If it's one of the fake numbers, then throw an error.
		for (long fakeNutrientStatementNumber : IngredientStatementHeader.EMPTY_SCALE_NUTRIENT_STATEMENT_NUMBERS) {
			if (fakeNutrientStatementNumber == header.getStatementNumber()) {
				throw new IllegalArgumentException(IngredientStatementService.INGREDIENT_STATEMENT_INVALID_ERROR);
			}
		}
		// If it's too big or small, throw an error.
		if (header.getStatementNumber() < IngredientStatementService.MIN_INGREDIENT_STATEMENT ||
				header.getStatementNumber() > IngredientStatementService.MAX_INGREDIENT_STATEMENT) {
			throw new IllegalArgumentException(INVALID_STATEMENT_NUMBER_ERROR);
		}

		header.setMaintenanceCode(ADD_CODE);
		header.setMaintenanceDate(LocalDate.now());
		if(header.getIngredientStatementDetails() != null) {

			header.setMaintenanceSwitch(true);
			double percentage = MAX_PERCENTAGE;
			this.checkForDuplications(header.getIngredientStatementDetails());
			List<IngredientStatementDetail> details = new ArrayList<>();

			// This will rebuild the list as a fully populated objects with the percentage set
			// so that they will be in the proper order. It will clean out the header's current
			// list and add them all back.
			for (IngredientStatementDetail detail : header.getIngredientStatementDetails()) {
				detail.getKey().setStatementNumber(header.getStatementNumber());
				detail.getKey().setIngredientCode(detail.getIngredient().getIngredientCode());
				// Add the details about the ingredient.
				detail.setIngredient(this.ingredientRepository.findOne(detail.getIngredient().getIngredientCode()));
				detail.setIngredientPercentage(percentage--);
				details.add(detail);
			}
			header.getIngredientStatementDetails().removeAll(header.getIngredientStatementDetails());
			header.getIngredientStatementDetails().addAll(details);
		}

		IngredientStatementHeader toReturn = this.repository.save(header);

		return toReturn;
	}

	/**
	 * Deletes the ingredientStatement code provided.
	 *
	 * @param ingredientStatement The ingredientStatement code to be deleted.
	 */
	@CoreTransactional
	public void delete(List<Long> deptList, long ingredientStatement) {

		IngredientStatementHeader ingredientStatementHeader = this.repository.findOne(ingredientStatement);
		if (ingredientStatementHeader == null) {
			String message = String.format(IngredientStatementService.INGREDIENT_STATEMENT_DOES_NOT_EXIST_ERROR,
					ingredientStatement);
			IngredientStatementService.logger.debug(message);
			throw new IllegalArgumentException(message);
		}

		// See if this ingredient statement is tied to any UPCs.
		// Since this requires a page request, and we only care if there are records or not,
		// just grab the first page of 1 record.
		PageRequest pageRequest = new PageRequest(0, 1);
		List<ScaleUpc> tiedUpcs = this.scaleUpcRepository.findByIngredientStatement(ingredientStatement, pageRequest);
		if (!tiedUpcs.isEmpty()) {
			String message = String.format(IngredientStatementService.INGREDIENT_STATEMENT_IN_USE_ERROR,
					ingredientStatement);
			IngredientStatementService.logger.debug(message);
			throw new IllegalArgumentException(message);
		}

		// If it was just added, then we can just remove this from the DB.
		if(ingredientStatementHeader.getMaintenanceCode() == IngredientStatementService.ADD_CODE){
			this.repository.delete(ingredientStatementHeader);
		} else {
			int sequenceNumber = 1;
			List<IngredientNutrientStatementWork> departmentsToDelete = new ArrayList<>(deptList.size());

			for(long deptNumber : deptList) {
				IngredientNutrientStatementWork ingredientNutrientStatementWork = new IngredientNutrientStatementWork();
				IngredientNutrientStatementWorkKey ingredientNutrientStatementWorkKey =
						new IngredientNutrientStatementWorkKey();
				ingredientNutrientStatementWorkKey.setRecTypCode(
						IngredientStatementService.INGREDIENT_STATEMENT_DELETE_RECORD_TYPE);
				ingredientNutrientStatementWorkKey.setSequenceNumber(sequenceNumber);
				ingredientNutrientStatementWorkKey.setStatementNumber(ingredientStatement);
				ingredientNutrientStatementWork.setDeptNumber(deptNumber);
				ingredientNutrientStatementWork.setKey(ingredientNutrientStatementWorkKey);
				ingredientNutrientStatementWork.setUserId(userInfo.getUserId());
				departmentsToDelete.add(ingredientNutrientStatementWork);
				sequenceNumber++;
			}
			// Though we're just setting the ingredient statement to be deleted, we need to clear out the
			// ingredients tied to the statement or the maintenance job will fail.
			ingredientStatementHeader.getIngredientStatementDetails().clear();

			// Mark the statement as to-be deleted.
			ingredientStatementHeader.setMaintenanceCode(IngredientStatementService.DELETE_CODE);
			ingredientStatementHeader.setMaintenanceDate(LocalDate.now());
			ingredientStatementHeader.setMaintenanceSwitch(true);
			this.ingredientNutrientStatementWorkRepository.save(departmentsToDelete);
			this.repository.save(ingredientStatementHeader);
		}
	}

	/**
	 * Updates the value of the IngredientStatementHeader passed.
	 *
	 * @param statement The updated IngredientStatementHeader.
	 * @return The updated IngredientStatementHeader.
	 */
	@CoreTransactional
	public IngredientStatementHeader update(IngredientStatementHeader statement){

		// make sure there are not already duplicates
		this.checkForDuplications(statement.getIngredientStatementDetails());

		// Grab the existing statement from the DB.
		IngredientStatementHeader originalStatement = this.repository.findOne(statement.getStatementNumber());
		if (originalStatement == null) {
			String message = String.format(IngredientStatementService.INGREDIENT_STATEMENT_DOES_NOT_EXIST_ERROR,
					statement.getStatementNumber());
			IngredientStatementService.logger.debug(message);
			throw new IllegalArgumentException(message);
		}

		// If the statement is already set for add, don't update the maintenance flags.
		if(originalStatement.getMaintenanceCode() != IngredientStatementService.ADD_CODE) {
			originalStatement.setMaintenanceCode(IngredientStatementService.UPDATE_CODE);
			originalStatement.setMaintenanceSwitch(true);
			// Ignore the user's maintenance date, and set it to today.
			originalStatement.setMaintenanceDate(LocalDate.now());
		}

		// Delete any current ingredients on the statement.
		if (!originalStatement.getIngredientStatementDetails().isEmpty()) {
			originalStatement.getIngredientStatementDetails().clear();
			this.entityManager.flush();
		}

		// Copy all the ingredients from the passed in statement.
		double currentPercentage = IngredientStatementService.MAX_PERCENTAGE;
		for (IngredientStatementDetail detail : statement.getIngredientStatementDetails()) {
			String codeToSearchFor = String.format("%-5s", detail.getIngredient().getIngredientCode());
			Ingredient i = this.ingredientRepository.findOne(codeToSearchFor);
			IngredientStatementDetail newDetail = new IngredientStatementDetail();
			newDetail.setKey(new IngredientStatementDetailsKey());
			newDetail.getKey().setIngredientCode(i.getIngredientCode());
			newDetail.getKey().setStatementNumber(originalStatement.getStatementNumber());
			newDetail.setIngredientPercentage(currentPercentage--);
			newDetail.setIngredientStatementHeader(originalStatement);
			newDetail.setIngredient(i);
			originalStatement.getIngredientStatementDetails().add(newDetail);
		}


		originalStatement = this.repository.save(originalStatement);

		List<Long> ingredientStatementNumberChangeList = new ArrayList<>();
		ingredientStatementNumberChangeList.add(originalStatement.getStatementNumber());
		IngredientStatementEvent ingredientStatementEvent = new IngredientStatementEvent(this,null, ingredientStatementNumberChangeList, this.userInfo.getUserId());
		applicationEventPublisher.publishEvent(ingredientStatementEvent);

		return originalStatement;
	}

	/**
	 * Finds the hits when searching for ingredient statements by statement codes.
	 *
	 * @param ingredientStatementCodes The ingredient statement codes.
	 * @return The hits when searching for ingredient statements by statement codes.
	 */
	public Hits findHitsByIngredientStatementCodes(List<Long> ingredientStatementCodes){
		this.validateMaxLengthForIngredientStatementCodes(ingredientStatementCodes);
		List<Long> statementCodes = ingredientStatementCodes.stream().collect(Collectors.toList());
        List<IngredientStatementHeader> page = this.repository.findByStatementNumberInAndMaintenanceCodeNot(statementCodes,IngredientStatementHeader.DELETE_CODE);
        List<Long> hitStatementCodes = page.stream().map(IngredientStatementHeader::getStatementNumber).collect(
				Collectors.toList());
		return  Hits.calculateHits(ingredientStatementCodes, hitStatementCodes);
	}

	/**
	 * Finds the next available statement number greater than or equal to the number entered.
	 *
	 * @param statementNumber The statement number to look for.
	 * @return The next available statement number.
	 */
	public List<Long> findNextAvailableStatementNumber(Long statementNumber){
		boolean isFound = false;
		if(statementNumber == 0){
			statementNumber++;
		}
		long currentStatementNumber = statementNumber;
		List<Long> statementNumbers;
		while(!isFound) {
			if(currentStatementNumber > MAX_STATEMENT_SIZE - 1){
				throw new IllegalArgumentException("Reached max ingredient statement number.");
			}
			statementNumbers = this.repository.findFirst100ByStatementNumberGreaterThanEqualOrderByStatementNumber(currentStatementNumber).
					stream().map(IngredientStatementHeader::getStatementNumber).collect(Collectors.toList());
			if (statementNumbers.isEmpty()){
				break;
			}
			for (long number : statementNumbers) {
				if (number != currentStatementNumber) {
					isFound = true;
					break;
				} else {
					currentStatementNumber++;
				}
			}
		}
		List<Long> statement = new ArrayList<>();
		statement.add(currentStatementNumber);
		return statement;
	}

	/**
	 * This method converts the list of ingredient statement details to a list of ingredient codes and checks if there
	 * are any exact matches already in existence.
	 *
	 * @param ingredientDetails the candidate to be added to the database.
	 */
	private void checkForDuplications(List<IngredientStatementDetail> ingredientDetails){
		List<String> ingredientCodes = new ArrayList<>();
		for (IngredientStatementDetail detail: ingredientDetails) {
			ingredientCodes.add(detail.getIngredient().getIngredientCode());
		}
		List<Long> remainingHeaders = this.findIngredientStatementNumberByOrderedIngredientCodes(ingredientCodes, false);
		if(remainingHeaders.size()>0){
			throw new IllegalArgumentException(String.format("Ingredient statement numbers: %s already exists with ingredient codes in the order of: %s.", remainingHeaders, ingredientCodes));
		}
	}

	/**
	 * Returns a new ingredient statement and adds supplied ingredient codes to it. This is used to create a new
	 * statement based off of searched for ingredients.
	 *
	 * @param ingredientCodes The ingredient codes to add to the new statement.
	 * @return A new ingredient statement header with the requested ingredients already added.
	 */
	public IngredientStatementHeader createNewHeaderWithCodes(List<String> ingredientCodes) {

		double currentPercentage = IngredientStatementService.MAX_PERCENTAGE;

		IngredientStatementHeader header = new IngredientStatementHeader();
		header.setIngredientStatementDetails(new ArrayList<>());
		for (String ingredientCode : ingredientCodes) {
			Ingredient i = this.ingredientRepository.findOne(ingredientCode);
			if (i == null) {
				continue;
			}
			IngredientStatementDetail detail = new IngredientStatementDetail();
			detail.setKey(new IngredientStatementDetailsKey());
			detail.getKey().setIngredientCode(i.getIngredientCode());
			detail.setIngredient(i);
			detail.setIngredientStatementHeader(header);
			detail.setIngredientPercentage(currentPercentage--);
			header.getIngredientStatementDetails().add(detail);
		}

		return header;
	}

	/**
	 * Searches for scale upcs by ingredient statment number.
	 *
	 * @param ingredientStatementNumber The ingredient statement number to be searched for.
	 * @param includeCounts Include count of pages.
	 * @param page The page of data you're looking for.
	 * @return A Pageable Result that contains the page number, total pages, total elements, content.
	 */
	public PageableResult<ScaleUpc> findByIngredientStatementNumber(Long ingredientStatementNumber,
																	boolean includeCounts, int page, int pageSize) {

		Pageable pageRequest = new PageRequest(page, pageSize, ScaleUpc.getDefaultSort());

		PageableResult<ScaleUpc> results;
		if (includeCounts) {
			Page<ScaleUpc> data = this.scaleUpcRepositoryWithCount.findByIngredientStatement(ingredientStatementNumber,
					pageRequest);
			results = new PageableResult<>(pageRequest.getPageNumber(), data.getTotalPages(),data.getTotalElements(),
					data.getContent());
		} else {
			List<ScaleUpc> data =
					this.scaleUpcRepository.findByIngredientStatement(ingredientStatementNumber, pageRequest);
			results = new PageableResult<>(pageRequest.getPageNumber(), data);
		}

		return results;
	}

	/**
	 * This method returns all ingredient statement header's ingredient statement numbers that contain the given list of
	 * ingredient codes in the same order. If the parameter 'matchAll' is true, return all ingredient statements with
	 * ingredients codes in the same order (no matter if there are more ingredient codes present). If the parameter
	 * 'matchAll' is false, return ONLY ingredient statements that match the ingredient codes in the same order and do
	 * not have any other ingredients tied to it.
	 *
	 * @param ingredientCodes The list of ingredient codes to compare against.
	 * @param matchAll Whether other ingredients can exist besides given ingredient codes.
	 * @return The list of ingredient statement numbers from the ingredient statement headers found matching the request.
	 */
	private List<Long> findIngredientStatementNumberByOrderedIngredientCodes(List<String> ingredientCodes, boolean matchAll){

		// First, find a list of ingredient statements that have the requested codes in the exact order
		// they are requested. Build a regex of the ingredient codes that appends a delimiter to the regex if the
		// ingredient code is not the last in the list
		StringBuilder regexBuilder = new StringBuilder();
		ingredientCodes.forEach((ic) ->
				regexBuilder.append(ic).append(IngredientStatementHeaderRepository.INGREDIENT_LIST_DELIMETER));

		// if match all, add regex wildcard, otherwise you want an exact match (no wildcard)
		if(matchAll) {
			regexBuilder.append(IngredientStatementHeaderRepository.REGEX_MATCH_ALL);
		}

		// Didn't want to do it this way, but could not figure out how to replace the schema in the SQL
		// in the repository. This will create a native query to search for an ordered list of
		// ingredients on a statement.
		String sql = String.format(IngredientStatementService.ORDERED_LIST_SEARCH_SQL,
				this.databaseSchema);
		IngredientStatementService.logger.debug(sql);
		Query q = entityManager.createNativeQuery(sql);
		q.setParameter(IngredientStatementService.ORDERED_LIST_PARAMETER_NAME, regexBuilder.toString());
		List matchingIngredientCodes = q.getResultList();

		// The list that comes back may be BigDecimals, so convert it to a list of Longs. This is a Spring JPA thing.
		List<Long> convertedIngredientCodes = new ArrayList<>(matchingIngredientCodes.size());
		for (Object o : matchingIngredientCodes) {
			if (o instanceof BigDecimal) {
				convertedIngredientCodes.add(((BigDecimal) o).longValue());
			}
			if (o instanceof Long) {
				convertedIngredientCodes.add((Long)o);
			}
			// I haven't seen them be anything else, so not sure what to do if it is. Ignoring it for now.
		}
		return convertedIngredientCodes;
	}

	/**
	 * This method checks if any of the given ingredient codes are longer than max length allowed. Throws exception
	 * if an ingredient code is too long.
	 *
	 * @param ingredientCodes Ingredient codes to validate.
	 */
	private void validateMaxLengthForIngredientCodes(List<String> ingredientCodes) {
		for(String code : ingredientCodes) {
			Long l = Long.valueOf(code);
			if(l > MAX_INGREDIENT_STATEMENT || l < MIN_INGREDIENT_STATEMENT){
				throw new IllegalArgumentException(INVALID_STATEMENT_NUMBER_ERROR);
			}
		}
	}

	/**
	 * This method checks if any of the given ingredient statement codes are longer than max length allowed. Throws
	 * exception if an ingredient statement code is too long.
	 *
	 * @param ingredientStatementCodes Ingredient statement codes to validate.
	 */
	private void validateMaxLengthForIngredientStatementCodes(List<Long> ingredientStatementCodes) {
		for(Long code : ingredientStatementCodes){
			if(code > MAX_INGREDIENT_STATEMENT || code < MIN_INGREDIENT_STATEMENT){
				throw new IllegalArgumentException(INVALID_STATEMENT_NUMBER_ERROR);
			}
		}
	}

	/**
	 * Outputs the value of a description-based search to a CSV file
	 * @param outputStream The output stream in charge of writing to the CSV file
	 * @param description The description used in the search
	 */
	public void streamStatementByDescription(ServletOutputStream outputStream, String description) {
		streamIngredientStatementHeader(outputStream);

		//Find the data and stream the data of from the first page and count the total number of pages returned by the search.
		PageableResult<IngredientStatementHeader> statementListPage1 = this.findByDescription(description, true, 0, DEFAULT_PAGE_SIZE);
		int numberOfPages = statementListPage1.getPageCount();
		this.streamStatementListPage(outputStream, statementListPage1);

		//If more than one page, loop through and stream all other pages.
		if (numberOfPages > 1) {
			for (int currentPage = 1; currentPage < numberOfPages; currentPage++) {
				this.streamStatementListPage(outputStream, this.findByDescription(description, false, currentPage, DEFAULT_PAGE_SIZE));
			}
		}
	}

	/**
	 * Outputs the value of a statement code-based search to a CSV file
	 * @param outputStream The output stream in charge of writing to the CSV file
	 * @param ingredientStatements The list of statement code used in the search
	 */
	public void streamStatementByStatementCode(ServletOutputStream outputStream, List<Long> ingredientStatements) {
		streamIngredientStatementHeader(outputStream);

		//Find the data and stream the data of from the first page and count the total number of pages returned by the search.
		this.longPopulator.populate(ingredientStatements, DEFAULT_POPULATION_COUNT);
		PageableResult<IngredientStatementHeader> statementListPage1 = this.findByIngredientStatement(ingredientStatements, true, 0, DEFAULT_PAGE_SIZE);
		int numberOfPages = statementListPage1.getPageCount();
		this.streamStatementListPage(outputStream, statementListPage1);

		for (int currentPage = 1; currentPage < numberOfPages; currentPage++) {
			this.streamStatementListPage(outputStream, this.findByIngredientStatement(ingredientStatements, false, currentPage, DEFAULT_PAGE_SIZE));
		}
	}

	/**
	 * Outputs the value of a ingredient code-based search to a CSV file
	 * @param outputStream The output stream in charge of writing to the CSV file
	 * @param ingredientCodes The list of ingredient codes used in the search
	 */
	public void streamStatementByIngredientCode(ServletOutputStream outputStream, List<String> ingredientCodes) {
		streamIngredientStatementHeader(outputStream);

		//Find the data and stream the data of from the first page and count the total number of pages returned by the search.
		PageableResult<IngredientStatementHeader> statementListPage1 = this.findByIngredientCode(ingredientCodes, true, 0, DEFAULT_PAGE_SIZE);
		int numberOfPages = statementListPage1.getPageCount();
		this.streamStatementListPage(outputStream, statementListPage1);

		//If more than one page, loop through and stream all other pages.
		if (numberOfPages > 1) {
			for (int currentPage = 1; currentPage < numberOfPages; currentPage++) {
				this.streamStatementListPage(outputStream, this.findByIngredientCode(ingredientCodes, false, currentPage, DEFAULT_PAGE_SIZE));
			}
		}
	}

	/**
	 * Outputs the value of a search based on ordered ingredient codes to a CSV file
	 * @param outputStream The output stream in charge of writing to the CSV file
	 * @param orderedCodes The list of ordered ingredient codes used in the search
	 */
	public void streamStatementByOrderedCodes(ServletOutputStream outputStream, List<String> orderedCodes) {
		streamIngredientStatementHeader(outputStream);

		//Find the data and stream the data of from the first page and count the total number of pages returned by the search.
		PageableResult<IngredientStatementHeader> statementListPage1 = this.findByOrderedIngredientCodes(orderedCodes, true, 0, DEFAULT_PAGE_SIZE);
		int numberOfPages = statementListPage1.getPageCount();
		this.streamStatementListPage(outputStream, statementListPage1);

		for (int currentPage = 1; currentPage < numberOfPages; currentPage++) {
			this.streamStatementListPage(outputStream, this.findByOrderedIngredientCodes(orderedCodes, false, currentPage, DEFAULT_PAGE_SIZE));
		}
	}

	/**
	 * Outputs all ingredient statements to a CSV file
	 * @param outputStream The output stream in charge of writing to the CSV file
	 */
	public void streamAllStatements(ServletOutputStream outputStream) {
		streamIngredientStatementHeader(outputStream);

		//Find the data and stream the data of from the first page and count the total number of pages returned by the search.
		PageableResult<IngredientStatementHeader> statementListPage1 = this.findAll(true, 0, DEFAULT_PAGE_SIZE);
		int numberOfPages = statementListPage1.getPageCount();
		this.streamStatementListPage(outputStream, statementListPage1);

		for (int currentPage = 1; currentPage < numberOfPages; currentPage++) {
			this.streamStatementListPage(outputStream, this.findAll(false, currentPage, DEFAULT_PAGE_SIZE));
		}
	}

	/**
	 * Streams the header to the CSV file,
	 * @param outputStream The output stream in charge of writing to the CSV file
	 */
	public void streamIngredientStatementHeader(ServletOutputStream outputStream){
		//Export the header to the file
		try {
			outputStream.println(INGREDIENT_STATEMENT_EXPORT_HEADER);
		} catch (IOException e) {
			IngredientStatementService.logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Breaks a PageableResult page down into individual entries and calls for them to be streamed to the CSV file.
	 * @param outputStream The output stream in charge of writing to the CSV file
	 * @param page The page of the current search to be output.
	 */
	public void streamStatementListPage(ServletOutputStream outputStream, PageableResult<IngredientStatementHeader> page){
		Iterable<IngredientStatementHeader> page1Iterable = page.getData();
		for (IngredientStatementHeader statement : page1Iterable) {
			this.streamSingleStatement(outputStream, statement);
		}
	}

	/**
	 * Streams an individual IngredientStatement to the CSV file
	 * @param outputStream The output stream in charge of writing to the CSV file
	 * @param statement The individual statement to be streamed to the CSV file
	 */
	private void streamSingleStatement(ServletOutputStream outputStream, IngredientStatementHeader statement){
		//Create a list of Ingredients from the IngredientStatementDetails
		List<IngredientStatementDetail> detailList = statement.getIngredientStatementDetails();
		StringBuilder ingredientCodeBuilder = new StringBuilder();


		for(IngredientStatementDetail detail: detailList){
			ingredientCodeBuilder.append(detail.getIngredient().getDisplayName()).append(" ");
		}

		try {
			// Write out the ingredient to the stream.
			outputStream.println(
					String.format(INGREDIENT_STATEMENT_EXPORT_FORMAT, statement.getStatementNumber(),
							ingredientCodeBuilder, statement.getIngredientsText()));
		} catch (IOException e) {
			IngredientStatementService.logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}
}
