package com.heb.pm.scaleManagement;

import com.heb.pm.entity.NutrientUom;
import com.heb.pm.index.DocumentWrapperUtil;
import com.heb.pm.repository.NutritionUomIndexRepository;
import com.heb.pm.repository.ScaleNutrientUomCodeRepository;
import com.heb.pm.repository.ScaleNutrientUomCodeRepositoryWithCounts;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import com.heb.util.list.LongPopulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Holds business logic related to NutrientUom Code Maintenance.
 *
 * @author vn18422
 * @since 2.16.0
 */
@Service
public class NutrientUomService {

	private static final Logger logger = LoggerFactory.getLogger(NutrientUomService.class);

	private static final String LOG_MSG_SEARCH_NUTRIENT_UOM_CD_ALL =
			"Searching for All nutrient uom Code";
	private static final String LOG_MSG_SEARCH_NUTRIENT_UOM_CD_BY_CD =
			"Searching for nutrient uom Code by list of codes ";
	private static final String LOG_MSG_SEARCH_NUTRIENT_UOM_CD_BY_DESC =
			"Searching for nutrient uom Code by description ";
	private static final String LOG_MSG_SCALE_NUTRIENT_UOM_CD_ADD =
			"Add Scale nutrient uom Code Code";
	private static final String LOG_MSG_NUTRIENT_UOM_CD_UPDATE =
			"Update Scale nutrient uom Code Code";
	private static final String LOG_MSG_SCALE_NUTRIENT_UOM_CD_DELETE =
			"Delete Scale nutrient uom Code Code";
	private static final String MAX_NUTRIENT_UOM_CODE_SIZE_ERROR =
			"The nutrient uom code value can't be larger than 99999.";
	private static final String NUTRIENT_UOM_SEARCH_LOG_MESSAGE =
			"Searching for nutrient uom by the regular expression '%s'";
	private static final int MAX_NUTRIENT_UOM_CODE_SIZE = 999;
    private static final int DEFAULT_NUTRIENT_UOM_COUNT = 100;

    // Used to get consistent size lists to query runners.
    private LongPopulator longPopulator = new LongPopulator();

    @Autowired
	private ScaleNutrientUomCodeRepositoryWithCounts scaleNutrientUomCodeRepositoryWithCounts;
    @Autowired
    private ScaleNutrientUomCodeRepository scaleNutrientUomCodeRepository;
	@Autowired
	private NutritionUomIndexRepository nutritionUomIndexRepository;
    @Autowired
    private UserInfo userInfo;
    public enum SortColumn {
        SCALE_NUTRIENT_UOM_CD,
        SCALE_NUTRIENT_UOM_CD_DES
    }

    /**
     * Defines directions to sort on.
     */
    public enum SortDirection {
        ASC,
        DESC
    }
	/**
	 * Searches for a list of nutrient Uom Codes by a regular expression. It will limit the results to a particular
	 * system of measure and form.
	 *
	 * @param nutrientUomCodes The text to search for sub-departments by.
	 * @param includeCounts The measurement system to limit results to.
	 * @param page The page to look for.
	 * @param pageSize The maximum size for the page.
	 * @return A PageableResult with sub-departments matching the search criteria.
	 */
	public PageableResult<NutrientUom> findByNutrientUomCode( List<Long> nutrientUomCodes, boolean includeCounts,
                                                             int page, int pageSize, SortColumn sortColumn, SortDirection sortDirection) {

        NutrientUomService.logger.debug(String.format(NutrientUomService.LOG_MSG_SEARCH_NUTRIENT_UOM_CD_BY_CD, nutrientUomCodes));


        for(Long nutrientUomCode : nutrientUomCodes){
            if(nutrientUomCode > NutrientUomService.MAX_NUTRIENT_UOM_CODE_SIZE){
                throw new IllegalArgumentException(NutrientUomService.MAX_NUTRIENT_UOM_CODE_SIZE_ERROR);
            }
        }
		Pageable pageRequest = new PageRequest(page, pageSize, this.toSort(sortColumn, sortDirection));
		PageableResult<NutrientUom> results;

		//Populate nutrientUomCodes(List<Long>) to 100 elements via longPopulator
        this.longPopulator.populate(nutrientUomCodes, NutrientUomService.DEFAULT_NUTRIENT_UOM_COUNT);

		if (includeCounts) {
			Page<NutrientUom> data =  scaleNutrientUomCodeRepositoryWithCounts.findByNutrientUomCodeIn(nutrientUomCodes, pageRequest);
			results = new PageableResult<>(pageRequest.getPageNumber(),
					data.getTotalPages(),
					data.getTotalElements(),
					data.getContent());
		} else {
			List<NutrientUom> data =
					this.scaleNutrientUomCodeRepository.findByNutrientUomCodeIn(nutrientUomCodes, pageRequest);
			results = new PageableResult<>(pageRequest.getPageNumber(), data);
		}
		results.getData().forEach(this::resolveTrailingSpaces);
		return results;

	}

    /**
     * Used to fetch a list of Nutrient Uom Code data from the repository with support for server side pagination and
     * sorting.
     *
     * @param page          The page of data to pull.
     * @param pageSize      The number of records being asked for.
     * @param sortColumn    The column to sort the data on.
     * @param sortDirection The direction to sort on.
     * @return An iterable collection of  Nutrient Uom Code.
     */
    PageableResult<NutrientUom> findAll(int page, int pageSize,
                                        SortColumn sortColumn, SortDirection sortDirection) {
        NutrientUomService.logger.debug(NutrientUomService.LOG_MSG_SEARCH_NUTRIENT_UOM_CD_ALL);
        Pageable pageRequest = new PageRequest(page, pageSize, this.toSort(sortColumn, sortDirection));
        Page<NutrientUom> data = scaleNutrientUomCodeRepositoryWithCounts.findAll(pageRequest);

        PageableResult<NutrientUom> results = new PageableResult<>(pageRequest.getPageNumber(),
                data.getTotalPages(),
                data.getTotalElements(),
                data.getContent());
        results.getData().forEach(this::resolveTrailingSpaces);
        return results;
    }
    /**
     * Used to fetch a list of Nutrient Uom Code by findByScaleNutrientUomCodeDescription from the repository with support
     * for server side pagination and sorting.
     *
     * @param includeCounts                Whether or not to include total record and page counts.
     * @param page                         The page of data to pull.
     * @param pageSize                     The number of records being asked for.
     * @param sortColumn                   The column to sort the data on.
     * @param sortDirection                The direction to sort on.
     * @param nutrientUomDescription       Nutrient Uom Code description.
     * @return An iterable collection of  Scale Nutrient Uom Code.
     */
    PageableResult<NutrientUom> findByNutrientUomDescription(boolean includeCounts, int page, int pageSize,
                                                             SortColumn sortColumn, SortDirection sortDirection,
                                                             String nutrientUomDescription) {
        NutrientUomService.logger.debug(
                String.format(NutrientUomService.LOG_MSG_SEARCH_NUTRIENT_UOM_CD_BY_DESC, nutrientUomDescription));
        Pageable pageRequest = new PageRequest(page, pageSize, this.toSort(sortColumn, sortDirection));
        PageableResult<NutrientUom> results;

        if (includeCounts) {
            Page<NutrientUom> data = scaleNutrientUomCodeRepositoryWithCounts.findByNutrientUomDescriptionIgnoreCaseContaining(nutrientUomDescription,
                    pageRequest);

            results = new PageableResult<>(pageRequest.getPageNumber(),
                    data.getTotalPages(),
                    data.getTotalElements(),
                    data.getContent());
        } else {
            List<NutrientUom> data = this.scaleNutrientUomCodeRepository
                    .findByNutrientUomDescriptionIgnoreCaseContaining(nutrientUomDescription, pageRequest);
            results = new PageableResult<>(pageRequest.getPageNumber(), data);
        }
        results.getData().forEach(this::resolveTrailingSpaces);
        return results;
    }
    /**
     * Add Nutrient Uom Code code.
     *
     * @param nutrientUomCode the Nutrient Uom code
     * @return the Nutrient Uom  code
     */
    public NutrientUom add(NutrientUom nutrientUomCode) {
        NutrientUomService.logger.debug(
                String.format(NutrientUomService.LOG_MSG_SCALE_NUTRIENT_UOM_CD_ADD, nutrientUomCode));

        if(nutrientUomCode.getNutrientUomCode() > NutrientUomService.MAX_NUTRIENT_UOM_CODE_SIZE){
            throw new IllegalArgumentException(NutrientUomService.MAX_NUTRIENT_UOM_CODE_SIZE_ERROR);
        }
        if(this.scaleNutrientUomCodeRepository.findOne(nutrientUomCode.getNutrientUomCode()) == null) {
            nutrientUomCode.setMaintenanceFunction(nutrientUomCode.ADD);
            nutrientUomCode.setMaintenanceDate(LocalDate.now());
            if (nutrientUomCode.getExtendedDescription() == null)
            {
               nutrientUomCode.setExtendedDescription(" ");
            }
            nutrientUomCode.setCreateDate(LocalDateTime.now());
            nutrientUomCode.setCreateUserId(userInfo.getUserId());
            nutrientUomCode.setLastUpdateDate(LocalDateTime.now());
            nutrientUomCode.setLastUpdateUserId(userInfo.getUserId());

            return scaleNutrientUomCodeRepository.save(nutrientUomCode);
        } else {
            throw new IllegalArgumentException("This Nutrient Uom Code already exists.");
        }
    }
    /**
     * Update  Nutrient Uom Code code.
     *
     * @param nutrientUomCode the Nutrient Uom code
     * @return the Nutrient Uom code
     */
    public NutrientUom update(NutrientUom nutrientUomCode) {
        NutrientUomService.logger.debug(
                String.format(NutrientUomService.LOG_MSG_NUTRIENT_UOM_CD_UPDATE, nutrientUomCode));
        if(nutrientUomCode.getNutrientUomCode() > NutrientUomService.MAX_NUTRIENT_UOM_CODE_SIZE){
            throw new IllegalArgumentException(NutrientUomService.MAX_NUTRIENT_UOM_CODE_SIZE_ERROR);
        }
        nutrientUomCode.setMaintenanceDate(LocalDate.now());
        nutrientUomCode.setMaintenanceFunction(nutrientUomCode.CHANGE);
        nutrientUomCode.setLastUpdateDate(LocalDateTime.now());
        nutrientUomCode.setLastUpdateUserId(userInfo.getUserId());
        return scaleNutrientUomCodeRepository.save(nutrientUomCode);
    }

    /**
     * Delete. Nutrient Uom Codes - Data is staged for delete to be picked up by scale batch process.
     *
     * @param deleteNutrientUom  Nutrient Uom Code
     */
    public NutrientUom delete(NutrientUom deleteNutrientUom) {
        NutrientUomService.logger.debug(
                String.format(NutrientUomService.LOG_MSG_SCALE_NUTRIENT_UOM_CD_DELETE, deleteNutrientUom));
        deleteNutrientUom.setMaintenanceDate(LocalDate.now());
        deleteNutrientUom.setMaintenanceFunction(deleteNutrientUom.DELETE);
        deleteNutrientUom.setLastUpdateDate(LocalDateTime.now());
        deleteNutrientUom.setLastUpdateUserId(userInfo.getUserId());
        return scaleNutrientUomCodeRepository.save(deleteNutrientUom);
    }


    /**
     * Utility function to take a column and direction as defined by the enums in this class and convert it into
     * a Spring Sort.
     *
     * @param sortColumn The column to sort on. If null, the default sort of Nutrient Uom Code is used.
     * @param direction  The direction to sort. If null, ascending is assumed.
     * @return A Spring Sort object.
     */
    private Sort toSort(SortColumn sortColumn, SortDirection direction) {
        Sort sort;
        if (sortColumn == null) {
            sort = NutrientUom.getDefaultSort();
        } else {
            Sort.Direction sortDirection = this.toDirection(direction);
            switch (sortColumn) {
                case SCALE_NUTRIENT_UOM_CD:
                    sort = new Sort(new Sort.Order(sortDirection, NutrientUom.SORT_FIELD_SCALE_NUTRIENT_UOM_CD));
                    break;
                case SCALE_NUTRIENT_UOM_CD_DES:
                    sort = new Sort(new Sort.Order(sortDirection, NutrientUom.SORT_FIELD_SCALE_NUTRIENT_UOM_CD_DES));
                    break;
                default:
                    sort = new Sort(new Sort.Order(sortDirection, NutrientUom.SORT_FIELD_SCALE_NUTRIENT_UOM_CD));
            }

        }
        return sort;
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
     * This will take a Nutrient Uom Code and remove the trailing spaces.
     *
     * @param nutrientUom The Nutrient Uom Code to update.
     */
    private void resolveTrailingSpaces(NutrientUom nutrientUom){
        nutrientUom.setNutrientUomDescription(nutrientUom.getNutrientUomDescription() != null
                ? nutrientUom.getNutrientUomDescription().trim() : null);
    }

    /**
	 * Searches for a list of sub-departments by a regular expression. This is a wildcard search, meaning that
	 * anything partially matching the text passed in will be returned. It will limit the results to a particular
	 * system of measure.
	 *
	 * @param searchString The text to search for sub-departments by.
	 * @param measureSystem The measurement system to limit results to.
	 * @param page The page to look for.
	 * @param pageSize The maximum size for the page.
	 * @return A PageableResult with sub-departments matching the search criteria.
	 */
	public PageableResult<NutrientUom> findByRegularExpression(String searchString, String measureSystem,
															   int page, int pageSize) {

		NutrientUomService.logger.debug(String.format(NutrientUomService.NUTRIENT_UOM_SEARCH_LOG_MESSAGE,
				searchString));

		Page<NutrientUomDocument> sdp = this.nutritionUomIndexRepository.findByFilterAndMeasureSystem(searchString,
				measureSystem, new PageRequest(page, pageSize, NutrientUomDocument.getDefaultSort()));

		List<NutrientUom> unitsOfMeasure = new ArrayList<>();
		DocumentWrapperUtil.toDataCollection(sdp, unitsOfMeasure);

		return new PageableResult<>(page, sdp.getTotalPages(), sdp.getTotalElements(), unitsOfMeasure);
	}

	/**
	 * Searches for a list of sub-departments by a regular expression. This is a wildcard search, meaning that
	 * anything partially matching the text passed in will be returned. It will limit the results to a particular
	 * system of measure and form.
	 *
	 * @param searchString The text to search for sub-departments by.
	 * @param measureSystem The measurement system to limit results to.
	 * @param form The form the measure is for (solid or liquid).
	 * @param page The page to look for.
	 * @param pageSize The maximum size for the page.
	 * @return A PageableResult with sub-departments matching the search criteria.
	 */
	public PageableResult<NutrientUom> findByRegularExpression(String searchString, String measureSystem, String form,
															   int page, int pageSize) {

		NutrientUomService.logger.debug(String.format(NutrientUomService.NUTRIENT_UOM_SEARCH_LOG_MESSAGE,
				searchString));

		Page<NutrientUomDocument> sdp =
				this.nutritionUomIndexRepository.findByFilterAndMeasureSystemAndForm(searchString, measureSystem,
						form, new PageRequest(page, pageSize, NutrientUomDocument.getDefaultSort()));

		List<NutrientUom> unitsOfMeasure = new ArrayList<>();
		DocumentWrapperUtil.toDataCollection(sdp, unitsOfMeasure);

		return new PageableResult<>(page, sdp.getTotalPages(), sdp.getTotalElements(), unitsOfMeasure);
	}

	/**
	 *  Searches for the max of NutrientUomCode
	 * @return max of NutrientUomCode
	 */
	public Long getMaxNutrientUomCode(){
		return this.scaleNutrientUomCodeRepository.findMaxNutrientUomCode();
	}

	/**
	 * generates the next NutrientUomCode to be used
	 * @return The next NutrientUomCode to be used
	 */
	public Long getNextNutrientUomCode(){
		Long maxNutrientUomCode = this.getMaxNutrientUomCode();
		if(maxNutrientUomCode >= MAX_NUTRIENT_UOM_CODE_SIZE){
			return this.getNextAvailableNutrientUomCode();
		}else{
			return maxNutrientUomCode + 1;
		}

	}

	/**
	 * Searches for the next available NutrientUomCode
	 * @return The next available NutrientUomCode
	 */
	public Long getNextAvailableNutrientUomCode(){
		long currentNutrientUomCode = 1; // codes start at 1
		boolean isFound = false;
		List<Long> nutrientUomCodes;
		while(!isFound) {
			if(currentNutrientUomCode > MAX_NUTRIENT_UOM_CODE_SIZE){
				throw new IllegalArgumentException("Reached max nutrient UOM code.");
			}
			nutrientUomCodes = this.scaleNutrientUomCodeRepository.findFirst100ByNutrientUomCodeGreaterThanEqualOrderByNutrientUomCode(currentNutrientUomCode).
					stream().map(NutrientUom::getNutrientUomCode).collect(Collectors.toList());
			if (nutrientUomCodes.isEmpty()){
				break;
			}
			for (long number : nutrientUomCodes) {
				if (number != currentNutrientUomCode) {
					isFound = true;
					break;
				} else {
					currentNutrientUomCode++;
				}
			}
		}
		return currentNutrientUomCode;
	}
}
