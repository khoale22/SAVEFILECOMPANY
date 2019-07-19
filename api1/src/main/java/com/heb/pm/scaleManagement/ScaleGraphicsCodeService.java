/*
 * ScaleGraphicsCodeService
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.scaleManagement;

import com.heb.pm.Hits;
import com.heb.pm.entity.ScaleGraphicsCode;
import com.heb.pm.entity.ScaleUpc;
import com.heb.pm.repository.ScaleGraphicsCodeRepository;
import com.heb.pm.repository.ScaleGraphicsCodeRepositoryWithCounts;
import com.heb.pm.repository.ScaleUpcRepositoryWithCount;
import com.heb.pm.repository.ScaleUpcRepository;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Provides GET and PUT service operations for Scale Graphic Code.
 *
 * @author vn40486
 * @since 2.1.0
 */
@Service
public class ScaleGraphicsCodeService {
	private static final Logger logger = LoggerFactory.getLogger(ScaleGraphicsCodeService.class);

	private static final String LOG_MSG_SEARCH_SCALE_GRAPHICS_CD_ALL =
			"Searching for All Graphics Code";
	private static final String LOG_MSG_SEARCH_SCALE_GRAPHICS_CD_BY_CD =
			"Searching for Graphics Code by list of codes ";
	private static final String LOG_MSG_SEARCH_SCALE_GRAPHICS_CD_BY_DESC =
			"Searching for Graphics Code by description ";
	private static final String LOG_MSG_SEARCH_SCALE_GRAPHICS_CD_HITS =
			"Delete Scale Graphics Code Code";
	private static final String LOG_MSG_SCALE_GRAPHICS_CD_ADD =
			"Add Scale Graphics Code Code";
	private static final String LOG_MSG_SCALE_GRAPHICS_CD_UPDATE =
			"Update Scale Graphics Code Code";
	private static final String LOG_MSG_SCALE_GRAPHICS_CD_DELETE =
			"Delete Scale Graphics Code Code";
	private static final String MAX_GRAPHICS_CODE_SIZE_ERROR =
			"The graphics code value can't be larger than 99999.";
	private static final int MAX_GRAPHICS_CODE_SIZE = 99999;

	@Autowired
	private ScaleGraphicsCodeRepository scaleGraphicsCodeRepository;

	@Autowired
	private ScaleGraphicsCodeRepositoryWithCounts scaleGraphicsCodeRepositoryWithCounts;

	@Autowired
	private ScaleUpcRepositoryWithCount scaleUpcRepositoryWithCount;

	@Autowired
	private ScaleUpcRepository scaleUpcRepository;

	/**
	 * Defines columns that are available to sort on.
	 */
	public enum SortColumn {
		SCALE_GRAPHICS_CD,
		SCALE_GRAPHICS_CD_DESC
	}

	/**
	 * Defines directions to sort on.
	 */
	public enum SortDirection {
		ASC,
		DESC
	}


	/**
	 * Used to fetch a list of Graphics Code data from the repository with support for server side pagination and
	 * sorting.
	 *
	 * @param includeCounts Whether or not to include total record and page counts.
	 * @param page          The page of data to pull.
	 * @param pageSize      The number of records being asked for.
	 * @param sortColumn    The column to sort the data on.
	 * @param sortDirection The direction to sort on.
	 * @return An iterable collection of  Scale Graphic Code.
	 */
	PageableResult<ScaleGraphicsCode> findAll(boolean includeCounts, int page, int pageSize,
											  SortColumn sortColumn, SortDirection sortDirection) {
		ScaleGraphicsCodeService.logger.debug(String.format(ScaleGraphicsCodeService.LOG_MSG_SEARCH_SCALE_GRAPHICS_CD_ALL));
		Pageable pageRequest = new PageRequest(page, pageSize, this.toSort(sortColumn, sortDirection));
		Page<ScaleGraphicsCode> data = scaleGraphicsCodeRepositoryWithCounts.findAll(pageRequest);

		PageableResult<ScaleGraphicsCode> results = new PageableResult<>(pageRequest.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
		results.getData().forEach(this::resolveTrailingSpaces);
		return results;
	}

	/**
	 * Used to fetch a list of Graphics Codes by Graphics Code ids from the repository with support for
	 * server side pagination and sorting.
	 *
	 * @param includeCounts      Whether or not to include total record and page counts.
	 * @param page               The page of data to pull.
	 * @param pageSize           The number of records being asked for.
	 * @param sortColumn         The column to sort the data on.
	 * @param sortDirection      The direction to sort on.
	 * @param scaleGraphicsCodes list of Graphics Codes.
	 * @return An iterable collection of  Scale Graphic Code.
	 */
	PageableResult<ScaleGraphicsCode> findByScaleGraphicsCode(boolean includeCounts, int page, int pageSize,
															  SortColumn sortColumn, SortDirection sortDirection,
															  List<Long> scaleGraphicsCodes) {
		ScaleGraphicsCodeService.logger.debug(String.format(ScaleGraphicsCodeService.LOG_MSG_SEARCH_SCALE_GRAPHICS_CD_BY_CD, scaleGraphicsCodes));
		for (Long graphicsCode : scaleGraphicsCodes) {
			if (graphicsCode > ScaleGraphicsCodeService.MAX_GRAPHICS_CODE_SIZE) {
				throw new IllegalArgumentException(ScaleGraphicsCodeService.MAX_GRAPHICS_CODE_SIZE_ERROR);
			}
		}
		Pageable pageRequest = new PageRequest(page, pageSize, this.toSort(sortColumn, sortDirection));
		PageableResult<ScaleGraphicsCode> results;
		if (includeCounts) {
			Page<ScaleGraphicsCode> data = scaleGraphicsCodeRepositoryWithCounts.findByScaleGraphicsCodeIn(scaleGraphicsCodes, pageRequest);
			results = new PageableResult<>(pageRequest.getPageNumber(),
					data.getTotalPages(),
					data.getTotalElements(),
					data.getContent());
		} else {
			List<ScaleGraphicsCode> data =
					this.scaleGraphicsCodeRepository.findByScaleGraphicsCodeIn(scaleGraphicsCodes, pageRequest);
			results = new PageableResult<>(pageRequest.getPageNumber(), data);
		}
		results.getData().forEach(this::resolveTrailingSpaces);
		return results;
	}

	/**
	 * Used to fetch a list of Graphics Code by findByScaleGraphicsCodeDescription from the repository with support
	 * for server side pagination and sorting.
	 *
	 * @param includeCounts                Whether or not to include total record and page counts.
	 * @param page                         The page of data to pull.
	 * @param pageSize                     The number of records being asked for.
	 * @param sortColumn                   The column to sort the data on.
	 * @param sortDirection                The direction to sort on.
	 * @param scaleGraphicsCodeDescription scale graphics description.
	 * @return An iterable collection of  Scale Graphic Code.
	 */
	PageableResult<ScaleGraphicsCode> findByScaleGraphicsCodeDescription(boolean includeCounts, int page, int pageSize,
																		 SortColumn sortColumn, SortDirection sortDirection,
																		 String scaleGraphicsCodeDescription) {
		ScaleGraphicsCodeService.logger.debug(
				String.format(ScaleGraphicsCodeService.LOG_MSG_SEARCH_SCALE_GRAPHICS_CD_BY_DESC,
						scaleGraphicsCodeDescription));
		Pageable pageRequest = new PageRequest(page, pageSize, this.toSort(sortColumn, sortDirection));
		PageableResult<ScaleGraphicsCode> results;
		if (includeCounts) {
			Page<ScaleGraphicsCode> data = scaleGraphicsCodeRepositoryWithCounts.findByScaleGraphicsCodeDescriptionIgnoreCaseContaining(scaleGraphicsCodeDescription,
					pageRequest);

			results = new PageableResult<>(pageRequest.getPageNumber(),
					data.getTotalPages(),
					data.getTotalElements(),
					data.getContent());
		} else {
			List<ScaleGraphicsCode> data = this.scaleGraphicsCodeRepository
					.findByScaleGraphicsCodeDescriptionIgnoreCaseContaining(scaleGraphicsCodeDescription, pageRequest);
			results = new PageableResult<>(pageRequest.getPageNumber(), data);
		}
		results.getData().forEach(this::resolveTrailingSpaces);
		return results;
	}

	/**
	 * Returns the Hits count with match and non-match, along with non-match plus from the input list.
	 *
	 * @param graphicsCodes The list of graphics codes to search for.
	 * @return Hits for the graphics codes.
	 */
	public Hits findHitsByGraphicsCodes(List<Long> graphicsCodes) {
		ScaleGraphicsCodeService.logger.debug(
				String.format(ScaleGraphicsCodeService.LOG_MSG_SEARCH_SCALE_GRAPHICS_CD_HITS, graphicsCodes));
		List<Long> hitsGraphicCodesList = this.scaleGraphicsCodeRepository.findScaleGraphicsCodes(graphicsCodes);
		return Hits.calculateHits(graphicsCodes, hitsGraphicCodesList);
	}


	/**
	 * Utility function to take a column and direction as defined by the enums in this class and convert it into
	 * a Spring Sort.
	 *
	 * @param sortColumn The column to sort on. If null, the default sort of ScaleGraphicsCode is used.
	 * @param direction  The direction to sort. If null, ascending is assumed.
	 * @return A Spring Sort object.
	 */
	private Sort toSort(SortColumn sortColumn, SortDirection direction) {
		Sort sort;
		if (sortColumn == null) {
			sort = ScaleGraphicsCode.getDefaultSort();
		} else {
			Sort.Direction sortDirection = this.toDirection(direction);
			switch (sortColumn) {
				case SCALE_GRAPHICS_CD:
					sort = new Sort(new Sort.Order(sortDirection, ScaleGraphicsCode.SORT_FIELD_SCALE_GRAPHICS_CD));
					break;
				case SCALE_GRAPHICS_CD_DESC:
					sort = new Sort(new Sort.Order(sortDirection, ScaleGraphicsCode.SORT_FIELD_SCALE_GRAPHICS_CD_DES));
					break;
				default:
					sort = new Sort(new Sort.Order(sortDirection, ScaleGraphicsCode.SORT_FIELD_SCALE_GRAPHICS_CD));
			}

		}
		return sort;
	}


	/**
	 * Add scale graphics code.
	 *
	 * @param scaleGraphicsCode the scale graphics code
	 * @return the scale graphics code
	 */
	public ScaleGraphicsCode add(ScaleGraphicsCode scaleGraphicsCode) {
		ScaleGraphicsCodeService.logger.debug(
				String.format(ScaleGraphicsCodeService.LOG_MSG_SCALE_GRAPHICS_CD_ADD, scaleGraphicsCode));

		if (scaleGraphicsCode.getScaleGraphicsCode() > ScaleGraphicsCodeService.MAX_GRAPHICS_CODE_SIZE) {
			throw new IllegalArgumentException(ScaleGraphicsCodeService.MAX_GRAPHICS_CODE_SIZE_ERROR);
		}
		if (this.scaleGraphicsCodeRepository.findOne(scaleGraphicsCode.getScaleGraphicsCode()) == null) {
			return scaleGraphicsCodeRepository.save(scaleGraphicsCode);
		} else {
			throw new IllegalArgumentException("This Graphics Code already exists.");
		}
	}

	/**
	 * Update scale graphics code.
	 *
	 * @param scaleGraphicsCode the scale graphics code
	 * @return the scale graphics code
	 */
	public ScaleGraphicsCode update(ScaleGraphicsCode scaleGraphicsCode) {
		ScaleGraphicsCodeService.logger.debug(
				String.format(ScaleGraphicsCodeService.LOG_MSG_SCALE_GRAPHICS_CD_UPDATE, scaleGraphicsCode));
		if (scaleGraphicsCode.getScaleGraphicsCode() > ScaleGraphicsCodeService.MAX_GRAPHICS_CODE_SIZE) {
			throw new IllegalArgumentException(ScaleGraphicsCodeService.MAX_GRAPHICS_CODE_SIZE_ERROR);
		}
		return scaleGraphicsCodeRepository.save(scaleGraphicsCode);
	}

	/**
	 * Delete.
	 *
	 * @param scaleGraphicsCode the scale graphics code
	 */
	public void delete(Long scaleGraphicsCode) {
		ScaleGraphicsCodeService.logger.debug(
				String.format(ScaleGraphicsCodeService.LOG_MSG_SCALE_GRAPHICS_CD_DELETE, scaleGraphicsCode));
		scaleGraphicsCodeRepository.delete(scaleGraphicsCode);
	}

	/**
	 * Searches for scale upc by graphics code.
	 *
	 * @param graphicsCode  The graphics Code to be searched for.
	 * @param includeCounts include count of pages boolean.
	 * @param page          page nummber.
	 * @return A Pageable Result that contains the page number, total pages, total elements, content.
	 */
	public PageableResult<ScaleUpc> findByGraphicsCode(Long graphicsCode, boolean includeCounts, int page, int pageSize) {

		if (graphicsCode > ScaleGraphicsCodeService.MAX_GRAPHICS_CODE_SIZE) {
			throw new IllegalArgumentException(ScaleGraphicsCodeService.MAX_GRAPHICS_CODE_SIZE_ERROR);
		}
		Pageable pageRequest = new PageRequest(page, pageSize, ScaleUpc.getDefaultSort());

		PageableResult<ScaleUpc> results;
		if (includeCounts) {
			Page<ScaleUpc> data = this.scaleUpcRepositoryWithCount.findByGraphicsCode(graphicsCode, pageRequest);
			results = new PageableResult<>(pageRequest.getPageNumber(), data.getTotalPages(), data.getTotalElements(),
					data.getContent());
		} else {
			List<ScaleUpc> data =
					this.scaleUpcRepository.findByGraphicsCode(graphicsCode, pageRequest);
			results = new PageableResult<>(pageRequest.getPageNumber(), data);
		}

		return results;
	}

	/**
	 * Get count of scale UPCs by grapics code.
	 * @param graphicsCode graphics code to be matched.
	 * @return count of scale upcs
	 */
	public int getCountByGraphicsCode(Long graphicsCode) {
		return this.scaleUpcRepositoryWithCount.countByGraphicsCode(graphicsCode);
	}

	/**
	 * Utility function to convert a sort direction as defined in the enum in this class to the one defined in Spring's
	 * Sort class.
	 *
	 * @param direction The direction to sort in. If null, ascending is assumed.
	 * @return The Spring Sort Direction to use.
	 */
	private Sort.Direction toDirection(SortDirection direction) {
		if (direction == null) {
			return Sort.Direction.ASC;
		}
		if (direction.equals(SortDirection.DESC)) {
			return Sort.Direction.DESC;
		}
		return Sort.Direction.ASC;
	}

	/**
	 * This will take a ScaleGraphicsCode and remove the trailing spaces.
	 *
	 * @param scaleGraphicsCode The ScaleGraphicsCode to update.
	 */
	private void resolveTrailingSpaces(ScaleGraphicsCode scaleGraphicsCode) {
		scaleGraphicsCode.setScaleGraphicsCodeDescription(scaleGraphicsCode.getScaleGraphicsCodeDescription() != null
				? scaleGraphicsCode.getScaleGraphicsCodeDescription().trim() : null);
	}

}