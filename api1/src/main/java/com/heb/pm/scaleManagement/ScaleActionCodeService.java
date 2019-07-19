/*
 *  ScaleActionCodeService
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.scaleManagement;

import com.heb.pm.Hits;
import com.heb.pm.repository.*;
import com.heb.pm.entity.ScaleActionCode;
import com.heb.pm.entity.ScaleUpc;
import com.heb.pm.repository.ScaleUpcRepositoryWithCount;
import com.heb.util.jpa.PageableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Maintains all business logic related to scale action codes.
 *
 * @author s573181
 * @since 2.0.8
 */
@Service
public class ScaleActionCodeService {

	private static final String DESCRIPTION_REGEX = "%%%s%%";

	private static final int PAGE_SIZE = 10;
	private static final String MAX_ACTION_CODE_SIZE_ERROR =
			"The graphics code value can't be larger than 99999.";

	private static final int MAX_ACTION_CODE_SIZE = 99999;
	@Autowired
	private ScaleActionCodeRepositoryWithCount repositoryWithCount;

	@Autowired
	private ScaleActionCodeRepository repository;

	@Autowired
	private ScaleUpcRepositoryWithCount scaleUpcRepositoryWithCount;

	@Autowired
	private ScaleUpcRepository scaleUpcRepository;

	/**
	 * Search for ScaleActionCode records based on action codes with the count.
	 *
	 * @param actionCodes The action codes.
	 * @param request The scale action code request.
	 * @return A pageable Result that contains the pagenumber, total pages, total elements, and the
	 * list of scale action codes matching the action codes sent.
	 */
	private PageableResult<ScaleActionCode> findByActionCodeWithCount(List<Long> actionCodes, Pageable request) {
		Page<ScaleActionCode> data = this.repositoryWithCount.findByActionCodeIn(actionCodes, request);

		return new PageableResult<>(request.getPageNumber(), data.getTotalPages(), data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Search for ScaleActionCode records based on action codes without the count.
	 *
	 * @param actionCodes The action codes.
	 * @param request The scale action code request.
	 * @return a list of scale action codes matching the action codes sent.
	 */
	private PageableResult<ScaleActionCode> findByActionCodeWithoutCount(List<Long> actionCodes, Pageable request) {
		List<ScaleActionCode> data =
				this.repository.findByActionCodeIn(actionCodes, request);

		return new PageableResult<>(request.getPageNumber(), data);
	}

	/**
	 * Search for ScaleActionCode records based on action codes.
	 *
	 * @param actionCodes The action codes.
	 * @param includeCount Tells the ScaleActionCodeRepository whether it needs to get the count or not.
	 * @param page The current page.
	 * @return a list of scale action codes matching the action codes sent.
	 */
	public PageableResult<ScaleActionCode> findByActionCodes (List<Long> actionCodes, boolean
			includeCount, int page){
	    for(Long code : actionCodes){
			if(code > ScaleActionCodeService.MAX_ACTION_CODE_SIZE){
				throw new IllegalArgumentException(MAX_ACTION_CODE_SIZE_ERROR);
			}
		}

		Pageable actionCodesRequest = new PageRequest(page, ScaleActionCodeService.PAGE_SIZE,
				ScaleActionCode.getDefaultSort());

		List<Long> actionCodeList = actionCodes.stream().collect(Collectors.toList());

		PageableResult<ScaleActionCode> results = includeCount ? this.findByActionCodeWithCount(actionCodeList,
				actionCodesRequest) :
				this.findByActionCodeWithoutCount(actionCodeList, actionCodesRequest);
		results.getData().forEach(this::resolveCounts);
		return results;
	}

	/**
	 * Search for all ScaleActionCode records with the count.
	 *
	 * @param request The scale action code request.
	 * @return A pageable Result that contains the pagenumber, total pages, total elements, and the
	 * list of scale action codes matching the action codes sent.
	 */
	private PageableResult<ScaleActionCode> findAllWithCount(Pageable request) {
		Page<ScaleActionCode> data = this.repositoryWithCount.findAll(request);

		return new PageableResult<>(request.getPageNumber(), data.getTotalPages(), data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Search for all ScaleActionCode records without the count.
	 *
	 * @param request The scale action code request.
	 * @return a list of scale action codes matching the action codes sent.
	 */
	private PageableResult<ScaleActionCode> findAllWithoutCount(Pageable request) {
		Page<ScaleActionCode> data =
				this.repository.findAll(request);

		return new PageableResult<>(request.getPageNumber(), data.getContent());
	}

	/**
	 * Search for ScaleActionCode records based on action codes.
	 *
	 * @param includeCount Tells the ScaleActionCodeRepository whether it needs to get the count or not.
	 * @param page The current page.
	 * @return a list of scale action codes matching the action codes sent.
	 */
	public PageableResult<ScaleActionCode> findAll (boolean
			includeCount, int page){

		Pageable actionCodesRequest = new PageRequest(page, ScaleActionCodeService.PAGE_SIZE,
				ScaleActionCode.getDefaultSort());


		PageableResult<ScaleActionCode> results = includeCount ? this.findAllWithCount(actionCodesRequest) :
				this.findAllWithoutCount(actionCodesRequest);
		results.getData().forEach(this::resolveCounts);
		return results;
	}

	/**
	 * Search for ScaleActionCode records based on the description with the count.
	 *
	 * @param description The action code's description.
	 * @param request The scale action code request.
	 * @return A pageable Result that contains the pagenumber, total pages, total elements, and the
	 * list of scale action codes matching the action codes sent.
	 */
	private PageableResult<ScaleActionCode> findByActionCodeDescriptionWithCount(String description, Pageable request) {
		Page<ScaleActionCode> data = this.repositoryWithCount.findByDescriptionContains(description, request);

		return new PageableResult<>(request.getPageNumber(), data.getTotalPages(),
				data.getTotalElements(), data.getContent());
	}

	/**
	 * Search for ScaleActionCode records based on the description without the count.
	 *
	 * @param description The action code's description.
	 * @param request The scale action code request.
	 * @return A pageable Result that contains the pagenumber, total pages, total elements, and the
	 * list of scale action codes matching the action codes sent.
	 */
	private PageableResult<ScaleActionCode> findByActionCodeDescriptionWithoutCount(String description, Pageable
			request) {
		List<ScaleActionCode> data =
				this.repository.findByDescriptionContains(description, request);
		return new PageableResult<>(request.getPageNumber(), data);

	}

	/**
	 * Search for ScaleActionCode records based on action codes.
	 *
	 * @param description The action code's description.
	 * @param includeCount Tells the ScaleActionCodeRepository whether it needs to get the count or not.
	 * @param page The current page.
	 * @return a list of scale action codes matching the description sent.
	 */
	public PageableResult<ScaleActionCode> findByActionCodeDescription (String description, boolean
			includeCount, int page){
		Pageable actionCodesRequest = new PageRequest(page, ScaleActionCodeService.PAGE_SIZE,
				ScaleActionCode.getDefaultSort());

		PageableResult<ScaleActionCode> results = includeCount ? this.findByActionCodeDescriptionWithCount(
				String.format(DESCRIPTION_REGEX, description.toUpperCase()), actionCodesRequest) :
				this.findByActionCodeDescriptionWithoutCount(String.format(DESCRIPTION_REGEX,
						description.toUpperCase()), actionCodesRequest);
		results.getData().forEach(this::resolveCounts);
		return results;
	}

	/**
	 * Search for ScaleUpc records based on an action code with the count.
	 *
	 * @param actionCode The action code.
	 * @param request The scale action code request.
	 * @return A pageable Result that contains the pagenumber, total pages, total elements, and the
	 * list of scale upcs matching the action code sent.
	 */
	private PageableResult<ScaleUpc> findPlusByActionCodeWithCount(Long actionCode, Pageable request){
		Page<ScaleUpc> data = this.scaleUpcRepositoryWithCount.findByActionCode(actionCode, request);
		return new PageableResult<>(request.getPageNumber(), data.getTotalPages(), data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Search for ScaleUpc records based on action code without the count.
	 *
	 * @param actionCode The action code.
	 * @param request The scale action code request.
	 * @return a list of scale action codes matching the action codes sent.
	 */
	private PageableResult<ScaleUpc> findPlusByActionCodeWithoutCount(Long actionCode,
																	  Pageable request){
		List<ScaleUpc> data = this.scaleUpcRepository.findByActionCode(actionCode, request);
		return new PageableResult<>(request.getPageNumber(), data);
	}

	/**
	 * Search for ScaleUpc records based on action codes.
	 *
	 * @param actionCode The action code.
	 * @param includeCount Tells the ScaleActionCodeRepository whether it needs to get the count or not.
	 * @param page The current page.
	 * @return a list of scale upcs matching the action codes sent.
	 */
	public PageableResult<ScaleUpc> findPlusByActionCode(Long actionCode, boolean includeCount, int page){
		Pageable actionCodesRequest = new PageRequest(page, ScaleActionCodeService.PAGE_SIZE,
				ScaleUpc.getDefaultSort());
		return includeCount ? this.findPlusByActionCodeWithCount(actionCode,
				actionCodesRequest) : this.findPlusByActionCodeWithoutCount(actionCode, actionCodesRequest);
	}

	/**
	 * Returns the Hits count with match and non-match, along with non-match action codes from the input list.
	 *
	 * @param actionCodeList The action codes to search for.
	 * @return Hits for the actionCodeList.
	 */
	public Hits findHitsByActionCodeList(List<Long> actionCodeList) {
		List<Long> actionCodes = actionCodeList.stream().collect(Collectors.toList());
		List<ScaleActionCode> scaleActionCodes = this.repositoryWithCount.findAll(actionCodes);
		List<Long> hitActionCodes = scaleActionCodes.stream().map(ScaleActionCode::getActionCode).collect(
				Collectors.toList());
		return Hits.calculateHits(actionCodeList, hitActionCodes);
	}

	/**
	 * Updates the description of the ScaleActionCode.
	 *
	 * @param scaleActionCode The ScaleActionCode.
	 * @return The updated scale action code.
	 */
	public ScaleActionCode update(ScaleActionCode scaleActionCode){
		scaleActionCode.setDescription(scaleActionCode.getDescription().trim().toUpperCase());
		ScaleActionCode returnScaleActionCode = this.repository.save(scaleActionCode);
		this.resolveCounts(returnScaleActionCode);
		return returnScaleActionCode;
	}

	/**
	 * Adds a new scale action code with the current max action cd plus 1.
	 *
	 * @param description The description to be added to the new ScaleActionCode.
	 * @return The new ScaleActionCode.
	 */
	public ScaleActionCode add(Long actionCode, String description){
		if(actionCode > 99999){
			throw new IllegalArgumentException("Action code must be less than or equal to 99999.");
		}
		if(this.repository.findOne(actionCode) == null) {
			ScaleActionCode scaleActionCode = new ScaleActionCode();
			scaleActionCode.setActionCode(actionCode);
			scaleActionCode.setDescription(description.trim().toUpperCase());
			return this.repository.save(scaleActionCode);
		} else {
			throw new IllegalArgumentException("This Action Code already exists.");
		}
	}

	/**
	 * Deletes the ScaleActionCode.
	 *
	 * @param actionCode The scaleActionCode actionCode to be deleted.
	 */
	public void delete(Long actionCode){
		ScaleActionCode scaleActionCode = this.repository.findOne(actionCode);
		if(scaleActionCode != null) {
			this.repository.delete(scaleActionCode);
		}
	}

	/**
	 * This will take a ScaleActionCode and populate the counts of products that have the action code.
	 *
	 * @param actionCode The ScaleActionCode to populate.
	 */
	private void resolveCounts(ScaleActionCode actionCode){
		actionCode.setCountOfActionCodeUpcs(this.scaleUpcRepositoryWithCount.countByActionCode(actionCode.getActionCode()));
	}

}
