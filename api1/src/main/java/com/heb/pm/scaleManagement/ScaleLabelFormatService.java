/*
 * ScaleLabelFormatService
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
import com.heb.pm.entity.ScaleLabelFormat;
import com.heb.pm.entity.ScaleUpc;
import com.heb.pm.repository.ScaleLabelFormatRepository;
import com.heb.pm.repository.ScaleLabelFormatRepositoryWithCounts;
import com.heb.pm.repository.ScaleUpcRepository;
import com.heb.pm.repository.ScaleUpcRepositoryWithCount;
import com.heb.util.jpa.PageableResult;
import com.heb.util.list.LongPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for searching, adding, and modifying Scale Label Formats.
 *
 * @author d116773
 * @since 2.0.8
 */
@Service
public class ScaleLabelFormatService {

	private static final String DESCRIPTION_SEARCH_FORMAT = "%%%s%%";

	private static final int DEFAULT_SEARCH_CODE_COUNT = 10;
	private static final int UNUSED_FORMAT_CODE = -1;
	private static final String MAX_LABEL_CODE_SIZE_ERROR =
			"The label code value can't be larger than 99999.";
	private static final int MAX_LABEL_CODE_SIZE = 99999;
	@Autowired
	private ScaleLabelFormatRepository scaleLabelFormatRepository;

	@Autowired
	private ScaleLabelFormatRepositoryWithCounts scaleLableFormatRepositoryWithCounts;

	@Autowired
	private ScaleUpcRepositoryWithCount scaleUpcRepositoryWithCount;

	@Autowired
	private ScaleUpcRepository scaleUpcRepository;

	private LongPopulator longPopulator = new LongPopulator(ScaleLabelFormatService.UNUSED_FORMAT_CODE);

	/**
	 * Returns a page of ScaleUpcs that have a specific label format code in its column one.
	 *
	 * @param formatCode The label format code to look for.
	 * @param includeCounts Whether or not to include counts in the return.
	 * @param page The page to return.
	 * @param pageSize The maximum number of records to return.
	 * @return A page of ScaleUpcs.
	 */
	@CoreTransactional
	public PageableResult<ScaleUpc> findUpcsByFormatCodeOne(Long formatCode,
															boolean includeCounts, int page, int pageSize) {

		PageRequest pageRequest = new PageRequest(page, pageSize, ScaleUpc.getDefaultSort());

		PageableResult<ScaleUpc> results;

		if (includeCounts) {
			Page<ScaleUpc> data = this.scaleUpcRepositoryWithCount.findByLabelFormatOne(formatCode, pageRequest);
			results = new PageableResult<>(page, data.getTotalPages(), data.getTotalElements(),
					data.getContent());
		} else {
			List<ScaleUpc> data = this.scaleUpcRepository.findByLabelFormatOne(formatCode, pageRequest);
			results = new PageableResult<>(page, data);
		}
		return results;
	}

	/**
	 * Returns a page of ScaleUpcs that have a specific label format code in its column two.
	 *
	 * @param formatCode The ID to search for.
	 * @param includeCounts Whether or not to include counts in the result.
	 * @param page The page of data to return.
	 * @param pageSize The maximum number of records to return.
	 * @return A PageableResult with Scale Upc records.
	 */
	@CoreTransactional
	public PageableResult<ScaleUpc> findUpcsByFormatCodeTwo(Long formatCode,
															boolean includeCounts, int page, int pageSize) {

		PageRequest pageRequest = new PageRequest(page, pageSize, ScaleUpc.getDefaultSort());

		PageableResult<ScaleUpc> results;

		if (includeCounts) {
			Page<ScaleUpc> data = this.scaleUpcRepositoryWithCount.findByLabelFormatTwo(formatCode, pageRequest);
			results = new PageableResult<>(page, data.getTotalPages(), data.getTotalElements(),
					data.getContent());
		} else {
			List<ScaleUpc> data = this.scaleUpcRepository.findByLabelFormatTwo(formatCode, pageRequest);
			results = new PageableResult<>(page, data);
		}
		return results;
	}

	/**
	 * Returns a pageable list of all label formats.
	 *
	 * @param includeCounts Whether or not to include counts in the result.
	 * @param page The page of data to return.
	 * @param pageSize The maximum number of records to return.
	 * @return A PageableResult with ScaleLabelFormat records.
	 */
	@CoreTransactional
	public PageableResult<ScaleLabelFormat> findAllLabelFormats(boolean includeCounts, int page, int pageSize) {

		PageRequest pageRequest = new PageRequest(page, pageSize, ScaleLabelFormat.getDefaultSort());

		PageableResult<ScaleLabelFormat> results;

		if (includeCounts) {
			Page<ScaleLabelFormat> data = this.scaleLableFormatRepositoryWithCounts.findAll(pageRequest);
			results = new PageableResult<>(page, data.getTotalPages(),data.getTotalElements(),
					data.getContent());
		} else {
			List<ScaleLabelFormat> data = this.scaleLabelFormatRepository.findAllByPage(pageRequest);
			results = new PageableResult<>(page, data);
		}
		results.getData().forEach(this::resolveCounts);
		return results;
	}

	/**
	 * Searches for a page of label formats by description.
	 *
	 * @param description The description to search for.
	 * @param includeCounts Whether or not to include counts in the result.
	 * @param page The page of data to return.
	 * @param pageSize The maximum number of records to return.
	 * @return A PageableResult with ScaleLabelFormat records.
	 */
	@CoreTransactional
	public PageableResult<ScaleLabelFormat> findByDescription(String description, boolean includeCounts, int page,
															  int pageSize) {

		PageRequest pageRequest = new PageRequest(page, pageSize, ScaleLabelFormat.getDefaultSort());
		String searchString = String.format(ScaleLabelFormatService.DESCRIPTION_SEARCH_FORMAT, description).toUpperCase();

		PageableResult<ScaleLabelFormat> results;

		if (includeCounts) {
			Page<ScaleLabelFormat> data =
					this.scaleLableFormatRepositoryWithCounts.findByDescriptionContains(searchString, pageRequest);
			results = new PageableResult<>(page, data.getTotalPages(),data.getTotalElements(),
					data.getContent());
		} else {
			List<ScaleLabelFormat> data =
					this.scaleLabelFormatRepository.findByDescriptionContains(searchString, pageRequest);
			results = new PageableResult<>(page, data);
		}
		results.getData().forEach(this::resolveCounts);
		return results;
	}

	/**
	 * Searches for a label format by code.
	 *
	 * @param formatCode The ID to search for.
	 * @return A ScaleLabelFormat with that ID.
	 */
	@CoreTransactional
	public PageableResult<ScaleLabelFormat> findByFormatCode(List<Long> formatCode, boolean includeCounts, int page,
															 int pageSize) {
		for(Long code : formatCode){
			if(code > ScaleLabelFormatService.MAX_LABEL_CODE_SIZE){
				throw new IllegalArgumentException(ScaleLabelFormatService.MAX_LABEL_CODE_SIZE_ERROR);
			}
		}
		PageRequest pageRequest = new PageRequest(page, pageSize, ScaleLabelFormat.getDefaultSort());
		this.longPopulator.populate(formatCode, ScaleLabelFormatService.DEFAULT_SEARCH_CODE_COUNT);

		PageableResult<ScaleLabelFormat> results;

		if (includeCounts) {
			Page<ScaleLabelFormat> data =
					this.scaleLableFormatRepositoryWithCounts.findDistinctByFormatCodeIn(formatCode, pageRequest);
			results	= new PageableResult<>(page, data.getTotalPages(), data.getTotalElements(), data.getContent());
		} else {
			List<ScaleLabelFormat> data =
					this.scaleLabelFormatRepository.findDistinctByFormatCodeIn(formatCode, pageRequest);
			results = new PageableResult<>(page, data);
		}
		results.getData().forEach(this::resolveCounts);
		return results;
	}

	/**
	 * Returns the Hits count with match and non-match, along with non-match format codes from the input list.
	 *
	 * @param labelFormatCodeList The format codes to search for.
	 * @return Hits for the labelFormatCodeList.
	 */
	public Hits findHitsByLabelFormatCodeList(List<Long> labelFormatCodeList) {
		List<Long> formatCodes = labelFormatCodeList.stream().collect(Collectors.toList());
		List<ScaleLabelFormat> scaleLabelFormats = this.scaleLableFormatRepositoryWithCounts.findAll(formatCodes);
		List<Long> hitLabelFormatCodes = scaleLabelFormats.stream().map(ScaleLabelFormat::getFormatCode).collect(
				Collectors.toList());
		return Hits.calculateHits(labelFormatCodeList, hitLabelFormatCodes);
	}

	/**
	 * Updates the description of the ScaleLabelFormat.
	 *
	 * @param scaleLabelFormatCode The ScaleLabelFormat.
	 * @return The updated scale format code.
	 */
	public ScaleLabelFormat update(ScaleLabelFormat scaleLabelFormatCode){

		// Pull the one out of the DB to update otherwise it'll try to do an insert.
		ScaleLabelFormat formatToUpdate = this.scaleLabelFormatRepository.findOne(scaleLabelFormatCode.getFormatCode());
		if (formatToUpdate == null) {
			throw new IllegalArgumentException(
					String.format("Label format code %d does not exist", scaleLabelFormatCode.getFormatCode()));
		}
		// Copy the description as passed in but trim it and turn it to upper case.
		formatToUpdate.setDescription(scaleLabelFormatCode.getDescription().trim().toUpperCase());

		return this.scaleLabelFormatRepository.save(formatToUpdate);
	}

	/**
	 * Adds a new scale format code with the current max format code plus 1.
	 *
	 * @param description The description to be added to the new ScaleLabelFormatCode.
	 * @return The new ScaleLabelFormatCode.
	 */
	public ScaleLabelFormat add(Long formatCode, String description){
		if(this.scaleLabelFormatRepository.findOne(formatCode) == null) {
			ScaleLabelFormat scaleLabelFormatCode = new ScaleLabelFormat();
			scaleLabelFormatCode.setFormatCode(formatCode);
			scaleLabelFormatCode.setDescription(description.trim().toUpperCase());
			return this.scaleLabelFormatRepository.save(scaleLabelFormatCode);
		} else {
			throw new IllegalArgumentException("This Label Format Code already exists.");
		}
	}

	/**
	 * Deletes the ScaleLabelFormatCode.
	 *
	 * @param formatCode The scaleLabelFormatCode formatCode to be deleted.
	 */
	public void delete(Long formatCode){
		ScaleLabelFormat scaleLabelFormat = this.scaleLabelFormatRepository.findOne(formatCode);
		if(scaleLabelFormat != null) {
			this.scaleLabelFormatRepository.delete(scaleLabelFormat);
		}
	}

	/**
	 * This will take a ScaleLabelFormat and populate the counts of products that have
	 * each label format in both the label 1 foramt and label 2 format fields.
	 *
	 * @param format The ScaleLabelFormats to populate.
	 */
	private void resolveCounts(ScaleLabelFormat format) {
		int countOfFirst = this.scaleUpcRepositoryWithCount.countByLabelFormatOne(format.getFormatCode());
		int coutnOfSecond = this.scaleUpcRepositoryWithCount.countByLabelFormatTwo(format.getFormatCode());
		format.setCountOfLabelFormatOneUpcs(countOfFirst);
		format.setCountOfLabelFormatTwoUpcs(coutnOfSecond);
	}
}
