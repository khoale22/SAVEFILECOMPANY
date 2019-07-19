package com.heb.pm.scaleManagement;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.Nutrient;
import com.heb.pm.entity.NutrientStatementHeader;
import com.heb.pm.entity.NutrientUom;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * REST endpoint for nutrient uom.
 *
 * @author vn18422
 * @since 2.16.0
 */
@RestController
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + NutrientsUomController.NUTRIENTS_URL)
@AuthorizedResource(ResourceConstants.SCALE_MANAGEMENT_NUTRIENTS)
public class NutrientsUomController {

	/**
	 * The constant NUTRIENTS_URL.
	 */
	protected static final String NUTRIENTS_URL = "/scaleManagement/nutrientUomCode";

	private static final Logger logger = LoggerFactory.getLogger(NutrientsUomController.class);

	private static final String FIND_BY_UOM_CODE_MESSAGE = "User %s from IP %s has requested nutrient data for the following UOM Code: [%s]";
    private static final String FIND_STATEMENT_BY_UOM_CODE_MESSAGE = "User %s from IP %s has requested nutrient statement data for the following UOM Code: [%s]";	private static final String NUTRIENT_UOM_REGEX_URL = "nutrientUomIndex";
	private static final String SEARCH_BY_PATTERN_LOG_MESSAGE = "User %s from IP %s searched for Nutrient Uom Code with the pattern '%s'";
	private static final String NO_SEARCH_STRING_DEFAULT_ERROR_MESSAGE = "Must have a pattern to search for.";
	private static final String NO_SEARCH_STRING_ERROR_KEY = "NutrientUomController.missingSearchString";
	private static final String MESSAGE_KEY_NUTRIENT_UOM_CODE = "NutrientUomController.missingNutrientUomCode";
	private static final String DEFAULT_ERROR_MESSAGE = "Required Search parameter is missing.";
	private static final String MESSAGE_KEY_NUTRIENT_UOM_DES = "NutrientUomController.missingDescription";
	private static final String DEFAULT_ADD_SUCCESS_MESSAGE ="Nutrient Uom Code: %s added successfully.";
	private static final String ADD_SUCCESS_MESSAGE_KEY ="NutrientUomController.addSuccessful";
	private static final String DEFAULT_DELETE_SUCCESS_MESSAGE ="Nutrient Uom Code: %s staged for delete successfully.";
	private static final String DELETE_SUCCESS_MESSAGE_KEY ="NutrientUomController.deleteSuccessful";
	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE ="Nutrient Uom Code: %s updated successfully.";
	private static final String UPDATE_SUCCESS_MESSAGE_KEY ="NutrientUomController.updateSuccessful";
	private static final String NO_MEASURE_SYSTEM_DEFAULT_ERROR_MESSAGE = "Must have a measure system defined.";
	private static final String NO_MEASURE_SYSTEM_ERROR_KEY = "NutrientUomController.missingMeasureSystem";
	private static final String NUTRIENT_UOM_SEARCH_BY_PATTERN_LOG_MESSAGE = "User %s from IP %s searched for Scale Nutrient Uom Code with the pattern '%s'";
    private static final String ADD_BY_PATTERN_LOG_MESSAGE = "User %s from IP %s added a new Scale Nutrient Uom Code with the pattern '%s'";
	private static final String UPDATE_BY_PATTERN_LOG_MESSAGE =	"User %s from IP %s updated a Scale Nutrient Uom Code with the pattern '%s'";
	private static final String DELETE_BY_PATTERN_LOG_MESSAGE =	"User %s from IP %s deleted a Scale Nutrient Uom Code with the pattern '%s'";
	private static final String LOG_PATTERN_SEARCH_BY_DEFAULT =	"ALL";
	private static final String NUTRIENT_UOM_SEARCH_BY_PATTERN_AND_FORM_LOG_MESSAGE =
			"User %s from IP %s searched for nutrient units of measure with the pattern %s for " +
					"the measurement system %s and the form %s";

	// Defaults for searches
	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_PAGE_SIZE = 15;

    // Defaults related to sorting.
    private static final NutrientUomService.SortColumn DEFAULT_SORT_COLUMN = NutrientUomService.SortColumn.SCALE_NUTRIENT_UOM_CD;
    private static final NutrientUomService.SortDirection DEFAULT_SORT_DIRECTION = NutrientUomService.SortDirection.ASC;

    @Autowired
	private NonEmptyParameterValidator parameterValidator;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private NutrientUomService nutrientUomService;

	@Autowired
	private NutrientsService nutrientsService;

	@Autowired
	private MessageSource messageSource;

	/**
	 * Find by regular expression pageable result.
	 *
	 * @param nutrientUomCodes The search string
	 * @param includeCounts To include counts
	 * @param page The page of data to return.
	 * @param pageSize The maximum number of results to return.
	 * @param request The HTTP request that initiated this call.
	 * @return A pageable list of units of measure.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "code")
	@ResponseBody
	public PageableResult<NutrientUom> findBynutrientUomCode(
            @RequestParam("includeCounts") Boolean includeCounts,
            @RequestParam("nutrientUomCodes") List<Long>  nutrientUomCodes,
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "pageSize", required = true) Integer pageSize,
            @RequestParam(value = "sortColumn", required = false)    NutrientUomService.SortColumn sortColumn,
            @RequestParam(value = "sortDirection", required = false) NutrientUomService.SortDirection sortDirection,
            HttpServletRequest request) {

		// Search string is required
		this.parameterValidator.validate(nutrientUomCodes, NutrientsUomController.NO_SEARCH_STRING_DEFAULT_ERROR_MESSAGE,
				NutrientsUomController.NO_SEARCH_STRING_ERROR_KEY, request.getLocale());

		// Measure system is required
		this.parameterValidator.validate(nutrientUomCodes, NutrientsUomController.NO_MEASURE_SYSTEM_DEFAULT_ERROR_MESSAGE,
				NutrientsUomController.NO_MEASURE_SYSTEM_ERROR_KEY, request.getLocale());

		// Set defaults if page and page size are not passed in.
        boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
        int pg = page == null ? NutrientsUomController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? NutrientsUomController.DEFAULT_PAGE_SIZE : pageSize;


        NutrientUomService.SortColumn sc =
				sortColumn == null ? NutrientsUomController.DEFAULT_SORT_COLUMN : sortColumn;
        NutrientUomService.SortDirection sd =
				sortDirection == null ? NutrientsUomController.DEFAULT_SORT_DIRECTION : sortDirection;
		return nutrientUomService.findByNutrientUomCode(nutrientUomCodes, ic, pg, ps, sc, sd );
	}

	/**
	 * Used to fetch a list of Scale Nutrient Uom Code data with support for server side pagination and sorting.
	 *
	 * @param page The page of data to pull.
	 * @param pageSize The number of records being asked for.
	 * @param sortColumn The column to sort the data on.
	 * @param sortDirection The direction to sort on.
	 * @param request The HTTP request that initiated this call.
	 * @return	An iterable collection of  Scale Nutrient Uom Code.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="all")
	public PageableResult<NutrientUom> allNutrientUomCode(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sortColumn", required = false) NutrientUomService.SortColumn sortColumn,
			@RequestParam(value = "sortDirection", required = false) NutrientUomService.SortDirection sortDirection,
			HttpServletRequest request) {
 			this.log(request.getRemoteAddr(), NUTRIENT_UOM_SEARCH_BY_PATTERN_LOG_MESSAGE, 		LOG_PATTERN_SEARCH_BY_DEFAULT);
		int pg = page == null ? NutrientsUomController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? NutrientsUomController.DEFAULT_PAGE_SIZE : pageSize;
		NutrientUomService.SortColumn sc =
				sortColumn == null ? NutrientsUomController.DEFAULT_SORT_COLUMN : sortColumn;
		NutrientUomService.SortDirection sd =
				sortDirection == null ? NutrientsUomController.DEFAULT_SORT_DIRECTION : sortDirection;
		return nutrientUomService.findAll(pg, ps, sc, sd);
	}

	/**
	 * Used to fetch a list of Scale Nutrient Uom Code data with support for server side pagination and sorting.
	 *
	 * @param includeCounts	Whether or not to include total record and page counts.
	 * @param page The page of data to pull.
	 * @param pageSize The number of records being asked for.
	 * @param sortColumn The column to sort the data on.
	 * @param sortDirection The direction to sort on.
	 * @param nutrientUomCodeDescription Scale Nutrient Uom Code description.
	 * @param request The HTTP request that initiated this call.
	 * @return	An iterable collection of  Scale Nutrient Uom Code.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "findNutrientUomByDescription")
	public PageableResult<NutrientUom> findByNutrientUomCodeDescription(
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "pageSize", required = true) Integer pageSize,
			@RequestParam(value = "sortColumn", required = false) NutrientUomService.SortColumn sortColumn,
			@RequestParam(value = "sortDirection", required = false) NutrientUomService.SortDirection sortDirection,
			@RequestParam(value = "description", required = true) String nutrientUomCodeDescription,
			HttpServletRequest request) {
		this.log(request.getRemoteAddr(), SEARCH_BY_PATTERN_LOG_MESSAGE, LOG_PATTERN_SEARCH_BY_DEFAULT);
		this.parameterValidator.validate(nutrientUomCodeDescription, DEFAULT_ERROR_MESSAGE,
				MESSAGE_KEY_NUTRIENT_UOM_DES, request.getLocale());
		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? NutrientsUomController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? NutrientsUomController.DEFAULT_PAGE_SIZE : pageSize;
		NutrientUomService.SortColumn sc = sortColumn == null ? NutrientsUomController.DEFAULT_SORT_COLUMN : sortColumn;
		NutrientUomService.SortDirection sd = sortDirection == null ? NutrientsUomController.DEFAULT_SORT_DIRECTION : sortDirection;
		return nutrientUomService.findByNutrientUomDescription(ic, pg, ps, sc, sd, nutrientUomCodeDescription);
		/*results.getData().forEach(this::resolveCounts);*/
		//return results;
	}
	/**
	 * Adds new Scale Nutrient Uom Code to the repository.
	 *
	 * @param uomCode The new nutrient to be added.
	 * @param request	The HTTP request that initiated this call.
	 * @return	The newly added nutrient with nutrient master id.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = "add")
	public ModifiedEntity<NutrientUom> add(@RequestBody NutrientUom uomCode,
										   HttpServletRequest request) {
		this.log(request.getRemoteAddr(), ADD_BY_PATTERN_LOG_MESSAGE, uomCode);
		NutrientUom nutrientUomCode = nutrientUomService.add(uomCode);

		String updateMessage = this.messageSource.getMessage(NutrientsUomController.ADD_SUCCESS_MESSAGE_KEY,
				new Object[]{nutrientUomCode.getNutrientUomCode()},
				NutrientsUomController.DEFAULT_ADD_SUCCESS_MESSAGE, request.getLocale());
		return new ModifiedEntity<>(nutrientUomCode, updateMessage);
	}

	/**
	 * Updates Scale Nutrient Uom Code in the repository.
	 *
	 * @param nutrientUomCode The new nutrient to be added.
	 * @param request	The HTTP request that initiated this call.
	 * @return	The newly added nutrient with nutrient master id.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.PUT , value = "update")
	public ModifiedEntity<NutrientUom> update(@RequestBody NutrientUom nutrientUomCode,
											  HttpServletRequest request) {
		this.log(request.getRemoteAddr(), UPDATE_BY_PATTERN_LOG_MESSAGE, nutrientUomCode);
		NutrientUom updatedNutrientUomCode = nutrientUomService.update(nutrientUomCode);
		String updateMessage = this.messageSource.getMessage(NutrientsUomController.UPDATE_SUCCESS_MESSAGE_KEY,
				new Object[]{updatedNutrientUomCode.getNutrientUomCode()},
				NutrientsUomController.DEFAULT_UPDATE_SUCCESS_MESSAGE, request.getLocale());
		return new ModifiedEntity<>(updatedNutrientUomCode, updateMessage);
	}

	/**
	 * Used to delete a particular Scale Nutrient Uom Code from the repository.
	 * @param nutrientUomCode The id of the Scale Nutrient Uom Code that is to be deleted.
	 * @param request The HTTP request that initiated this call.
	 * @return Returns copy of the Scale Nutrient Uom Code that was deleted.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST , value = "delete")
    public ModifiedEntity<NutrientUom> delete(@RequestBody NutrientUom nutrientUomCode,
                                       HttpServletRequest request) {
	this.log(request.getRemoteAddr(), DELETE_BY_PATTERN_LOG_MESSAGE, nutrientUomCode);
        NutrientUom deleteNutrientUomCode = nutrientUomService.delete(nutrientUomCode);
		String updateMessage = this.messageSource.getMessage(NutrientsUomController.DELETE_SUCCESS_MESSAGE_KEY,
				new Object[]{deleteNutrientUomCode.getNutrientUomCode()}, NutrientsUomController.DEFAULT_DELETE_SUCCESS_MESSAGE,
				request.getLocale());
		return new ModifiedEntity<>(deleteNutrientUomCode, updateMessage);
	}

	/**
	 * Logs the incoming requests from the user for  Scale Nutrient Uom Code.
	 *
	 * @param ip The IP address of the user searching for Nutrient Uom Codes.
	 * @param pattern The patters of the log message that explains action requested by the user.
	 * @param requestParam The info the user requested to be acted upon.
	 *
	 */
	private void log(String ip, String pattern, Object requestParam) {
		NutrientsUomController
				.logger.info(
				String.format(pattern,
						this.userInfo.getUserId(), ip, requestParam)
		);
	}

	/**
	 * Used to fetch a list of Scale Nutrients by UoM Code.
	 *
	 * @param uomCode The UoM code being used as the search key..
	 * @param page The page of data to pull.
	 * @param pageSize The number of records being asked for.
	 * @param includeCounts	Whether or not to include total record and page counts.
	 * @param request The HTTP request that initiated this call.
	 * @return	An iterable collection of  Scale Nutrients.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "associatedNutrients")
	public PageableResult<Nutrient> findNutrientsByUomCode(
			@RequestParam("uomCode") long uomCode,
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "pageSize", required = true) Integer pageSize,
			HttpServletRequest request){
		this.logFindByUomCode(request.getRemoteAddr(), uomCode);
		return this.nutrientsService.findByUomCode(uomCode, includeCounts, page, pageSize);
	}

	/**
	 * Find by regular expression pageable result.
	 *
	 * @param searchString The search string
	 * @param measureSystem The measurement system to search for (imperial or metric).
	 * @param form The form the product is in (solid or liquid).
	 * @param page The page of data to return.
	 * @param pageSize The maximum number of results to return.
	 * @param request The HTTP request that initiated this call.
	 * @return A pageable list of units of measure.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = NutrientsUomController.NUTRIENT_UOM_REGEX_URL)
	@ResponseBody
	public PageableResult<NutrientUom> findByRegularExpression(
			@RequestParam("searchString") String searchString,
			@RequestParam("measureSystem") String measureSystem,
			@RequestParam(value = "form", required = false) String form,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			HttpServletRequest request) {

		// Search string is required
		this.parameterValidator.validate(searchString, NutrientsUomController.NO_SEARCH_STRING_DEFAULT_ERROR_MESSAGE,
				NutrientsUomController.NO_SEARCH_STRING_ERROR_KEY, request.getLocale());

		// Measure system is required
		this.parameterValidator.validate(searchString, NutrientsUomController.NO_MEASURE_SYSTEM_DEFAULT_ERROR_MESSAGE,
				NutrientsUomController.NO_MEASURE_SYSTEM_ERROR_KEY, request.getLocale());

		// Set defaults for page and page size
		// Set defaults if page and page size are not passed in.
		int pg = page == null ? NutrientsUomController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? NutrientsUomController.DEFAULT_PAGE_SIZE : pageSize;

		// form may or may not be present, so call the appropriate function.
		if (form == null || form.isEmpty()) {
			this.logUnitOfMeasureSearch(request.getRemoteAddr(), searchString, measureSystem);
			return this.nutrientUomService.findByRegularExpression(searchString, measureSystem, pg, ps);
		} else {
			this.logUnitOfMeasureSearch(request.getRemoteAddr(), searchString, measureSystem, form);
			return this.nutrientUomService.findByRegularExpression(searchString, measureSystem, form, pg, ps);
		}

	}

	/**
	 * Logs the incoming requests from the user for Scale Nutrient by Uom Code.
	 *
	 * @param ip The IP address of the user searching for Nutrient Uom Codes.
	 * @param uomCode The patters of the log message that explains action requested by the user.
	 */
	private void logFindByUomCode(String ip, long uomCode) {
		NutrientsUomController.logger.info(
				String.format(NutrientsUomController.FIND_BY_UOM_CODE_MESSAGE,
						this.userInfo.getUserId(), ip, uomCode));
	}

	/**
	 * Used to fetch a list of Scale Nutrient Statements by UoM Code.
	 *
	 * @param uomCode The UoM code being used as the search key..
	 * @param page The page of data to pull.
	 * @param pageSize The number of records being asked for.
	 * @param includeCounts	Whether or not to include total record and page counts.
	 * @param request The HTTP request that initiated this call.
	 * @return	An iterable collection of  Scale Nutrients.
	 */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "associatedStatements")
    public PageableResult<NutrientStatementHeader> findNutrientStatementsByUomCode(
            @RequestParam("uomCode") long uomCode,
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
            @RequestParam(value = "pageSize", required = true) Integer pageSize,
            HttpServletRequest request){
        this.logFindNutrientStatementByUomCode(request.getRemoteAddr(), uomCode);
        return this.nutrientsService.findStatementByUomCode(uomCode, includeCounts, page, pageSize);
    }

    /**
     * Logs the incoming requests from the user for Scale Nutrient by Uom Code.
     *
     * @param ip The IP address of the user searching for Nutrient Uom Codes.
     * @param uomCode The pattersM of the log message that explains action requested by the user.
     */
    private void logFindNutrientStatementByUomCode(String ip, long uomCode) {
        NutrientsUomController.logger.info(
                String.format(NutrientsUomController.FIND_STATEMENT_BY_UOM_CODE_MESSAGE,
                        this.userInfo.getUserId(), ip, uomCode));
    }

	/**
	 * Logs a user request to search for units of measure
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param searchString The string the user is using to search sub-departments for.
	 * @param measureSystem The measurement system the user is searching for.
	 */
	private void logUnitOfMeasureSearch(String ip, String searchString, String measureSystem) {
		NutrientsUomController.logger.info(
				String.format(NutrientsUomController.NUTRIENT_UOM_SEARCH_BY_PATTERN_LOG_MESSAGE,
						this.userInfo.getUserId(), ip, searchString, measureSystem));
	}

	/**
	 * Logs a user request to search for units of measure
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param searchString The string the user is using to search sub-departments for.
	 * @param measureSystem The measurement system the user is searching for.
	 * @param form The form of the unit the user is searching for.
	 */
	private void logUnitOfMeasureSearch(String ip, String searchString, String measureSystem, String form) {
		NutrientsUomController.logger.info(
				String.format(NutrientsUomController.NUTRIENT_UOM_SEARCH_BY_PATTERN_AND_FORM_LOG_MESSAGE,
						this.userInfo.getUserId(), ip, searchString, measureSystem, form));
	}

	/**
	 * finds the next NutrientUomCode to be used
	 *
	 * @return The next NutrientUomCode to be used
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "getNextNutrientUomCode")
	public ModifiedEntity<Long> getNextNutrientUomCode(HttpServletRequest request){
		return new ModifiedEntity<>(this.nutrientUomService.getNextNutrientUomCode(), StringUtils.EMPTY);
	}
}
