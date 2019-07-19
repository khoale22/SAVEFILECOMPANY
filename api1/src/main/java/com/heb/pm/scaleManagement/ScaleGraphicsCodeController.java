package com.heb.pm.scaleManagement;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.Hits;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.ScaleGraphicsCode;
import com.heb.pm.entity.ScaleUpc;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * ScaleGraphicsCodeController.
 *
 * @author vn40486
 * @since 2.1.0
 */
@RestController
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ScaleGraphicsCodeController.GRAPHIC_CODE_URL)
@AuthorizedResource(ResourceConstants.SCALE_MANAGEMENT_GRAPHICS_CODE)
public class ScaleGraphicsCodeController {

	private static final Logger logger = LoggerFactory.getLogger(ScaleGraphicsCodeController.class);

	static final String GRAPHIC_CODE_URL = "/scaleManagement/graphicsCode";

	private static final String SEARCH_BY_PATTERN_LOG_MESSAGE =
			"User %s from IP %s searched for Scale Graphic Code with the pattern '%s'";
	private static final String ADD_BY_PATTERN_LOG_MESSAGE =
			"User %s from IP %s added a new Scale Graphic Code with the pattern '%s'";
	private static final String UPDATE_BY_PATTERN_LOG_MESSAGE =
			"User %s from IP %s updated a Scale Graphic Code with the pattern '%s'";
	private static final String DELETE_BY_PATTERN_LOG_MESSAGE =
			"User %s from IP %s deleted a Scale Graphic Code with the pattern '%s'";
	private static final String LOG_PATTERN_SEARCH_BY_DEFAULT =	"ALL";

	private static final String DEFAULT_ERROR_MESSAGE = "Required Search parameter is missing.";
	private static final String MESSAGE_KEY_GRAPHICS_CODE = "ScaleGraphicsCodeController.missingGraphicsCode";
	private static final String MESSAGE_KEY_GRAPHICS_DES = "ScaleGraphicsCodeController.missingDescription";
	private static final String DEFAULT_ADD_SUCCESS_MESSAGE ="Graphics Code: %s added successfully.";
	private static final String ADD_SUCCESS_MESSAGE_KEY ="ScaleGraphicsCodeController.addSuccessful";
	private static final String DEFAULT_DELETE_SUCCESS_MESSAGE ="Graphics Code: %s deleted successfully.";
	private static final String DELETE_SUCCESS_MESSAGE_KEY ="ScaleGraphicsCodeController.deleteSuccessful";
	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE ="Graphics Code: %s updated successfully.";
	private static final String UPDATE_SUCCESS_MESSAGE_KEY ="ScaleGraphicsCodeController.updateSuccessful";

	// Defaults related to paging.
	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_PAGE_SIZE = 100;

	// Defaults related to sorting.
	private static final ScaleGraphicsCodeService.SortColumn DEFAULT_SORT_COLUMN =
			ScaleGraphicsCodeService.SortColumn.SCALE_GRAPHICS_CD;
	private static final ScaleGraphicsCodeService.SortDirection DEFAULT_SORT_DIRECTION =
			ScaleGraphicsCodeService.SortDirection.ASC;

	@Autowired
	private ScaleGraphicsCodeService scaleGraphicsCodeService;

	@Autowired
	private UserInfo userInfo;

	@Autowired private
	NonEmptyParameterValidator parameterValidator;

	@Autowired
	private MessageSource messageSource;

	/**
	 * Used to fetch a list of Scale Graphic Code data with support for server side pagination and sorting.
	 *
	 * @param includeCounts	Whether or not to include total record and page counts.
	 * @param page The page of data to pull.
	 * @param pageSize The number of records being asked for.
	 * @param sortColumn The column to sort the data on.
	 * @param sortDirection The direction to sort on.
	 * @param request The HTTP request that initiated this call.
	 * @return	An iterable collection of  Scale Graphic Code.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="findAllGraphicsCodes")
	public PageableResult<ScaleGraphicsCode> findAll(
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sortColumn", required = false) ScaleGraphicsCodeService.SortColumn sortColumn,
			@RequestParam(value = "sortDirection", required = false)
			ScaleGraphicsCodeService.SortDirection sortDirection,
			HttpServletRequest request) {
		this.log(request.getRemoteAddr(), SEARCH_BY_PATTERN_LOG_MESSAGE, LOG_PATTERN_SEARCH_BY_DEFAULT);
		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? ScaleGraphicsCodeController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? ScaleGraphicsCodeController.DEFAULT_PAGE_SIZE : pageSize;
		ScaleGraphicsCodeService.SortColumn sc =
				sortColumn == null ? ScaleGraphicsCodeController.DEFAULT_SORT_COLUMN : sortColumn;
		ScaleGraphicsCodeService.SortDirection sd =
				sortDirection == null ? ScaleGraphicsCodeController.DEFAULT_SORT_DIRECTION : sortDirection;
		PageableResult<ScaleGraphicsCode> results = scaleGraphicsCodeService.findAll(ic, pg, ps, sc, sd);
		results.getData().forEach(this::resolveCounts);
		return results;
	}


	/**
	 * Used to fetch a list of Scale Graphic Code data with support for server side pagination and sorting.
	 *
	 * @param includeCounts	Whether or not to include total record and page counts.
	 * @param page The page of data to pull.
	 * @param pageSize The number of records being asked for.
	 * @param sortColumn The column to sort the data on.
	 * @param sortDirection The direction to sort on.
	 * @param graphicsCodes graphic codes.
	 * @param request The HTTP request that initiated this call.
	 * @return	An iterable collection of  Scale Graphic Code.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "graphicsCodes")
	public PageableResult<ScaleGraphicsCode> findByScaleGraphicCode(
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sortColumn", required = false) ScaleGraphicsCodeService.SortColumn sortColumn,
			@RequestParam(value = "sortDirection", required = false)
			ScaleGraphicsCodeService.SortDirection sortDirection,
			@RequestParam(value = "graphicsCodes", required = true) List<Long> graphicsCodes,
			HttpServletRequest request) {
		this.log(request.getRemoteAddr(), SEARCH_BY_PATTERN_LOG_MESSAGE, LOG_PATTERN_SEARCH_BY_DEFAULT);
		this.parameterValidator.validate(graphicsCodes, DEFAULT_ERROR_MESSAGE,
				MESSAGE_KEY_GRAPHICS_CODE, request.getLocale());
		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? ScaleGraphicsCodeController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? ScaleGraphicsCodeController.DEFAULT_PAGE_SIZE : pageSize;
		ScaleGraphicsCodeService.SortColumn sc =
				sortColumn == null ? ScaleGraphicsCodeController.DEFAULT_SORT_COLUMN : sortColumn;
		ScaleGraphicsCodeService.SortDirection sd =
				sortDirection == null ? ScaleGraphicsCodeController.DEFAULT_SORT_DIRECTION : sortDirection;
		PageableResult<ScaleGraphicsCode> results = scaleGraphicsCodeService.findByScaleGraphicsCode(ic, pg, ps, sc,
				sd, graphicsCodes);
		results.getData().forEach(this::resolveCounts);
		return results;
	}

	/**
	 * Used to fetch a list of Scale Graphic Code data with support for server side pagination and sorting.
	 *
	 * @param includeCounts	Whether or not to include total record and page counts.
	 * @param page The page of data to pull.
	 * @param pageSize The number of records being asked for.
	 * @param sortColumn The column to sort the data on.
	 * @param sortDirection The direction to sort on.
	 * @param graphicsCodeDescription Scale Graphic Code description.
	 * @param request The HTTP request that initiated this call.
	 * @return	An iterable collection of  Scale Graphic Code.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "graphicsCodeDescription")
	public PageableResult<ScaleGraphicsCode> findByScaleGraphicsCodeDescription(
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "sortColumn", required = false) ScaleGraphicsCodeService.SortColumn sortColumn,
			@RequestParam(value = "sortDirection", required = false)
			ScaleGraphicsCodeService.SortDirection sortDirection,
			@RequestParam(value = "description", required = true) String graphicsCodeDescription,
			HttpServletRequest request) {
		this.log(request.getRemoteAddr(), SEARCH_BY_PATTERN_LOG_MESSAGE, LOG_PATTERN_SEARCH_BY_DEFAULT);
		this.parameterValidator.validate(graphicsCodeDescription, DEFAULT_ERROR_MESSAGE,
				MESSAGE_KEY_GRAPHICS_DES, request.getLocale());
		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? ScaleGraphicsCodeController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? ScaleGraphicsCodeController.DEFAULT_PAGE_SIZE : pageSize;
		ScaleGraphicsCodeService.SortColumn sc =
				sortColumn == null ? ScaleGraphicsCodeController.DEFAULT_SORT_COLUMN : sortColumn;
		ScaleGraphicsCodeService.SortDirection sd =
				sortDirection == null ? ScaleGraphicsCodeController.DEFAULT_SORT_DIRECTION : sortDirection;
		PageableResult<ScaleGraphicsCode> results = scaleGraphicsCodeService.findByScaleGraphicsCodeDescription(ic, pg,
				ps, sc, sd, graphicsCodeDescription);
		results.getData().forEach(this::resolveCounts);
		return results;
	}

	/**
	 * Adds new Scale Graphic Code to the repository.
	 *
	 * @param scaleGraphicsCode The new nutrient to be added.
	 * @param request	The HTTP request that initiated this call.
	 * @return	The newly added nutrient with nutrient master id.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = "addGraphicsCode")
	public ModifiedEntity<ScaleGraphicsCode> add(@RequestBody ScaleGraphicsCode scaleGraphicsCode,
												 HttpServletRequest request) {
		this.log(request.getRemoteAddr(), ADD_BY_PATTERN_LOG_MESSAGE, scaleGraphicsCode);
		ScaleGraphicsCode graphicsCode = scaleGraphicsCodeService.add(scaleGraphicsCode);

		String updateMessage = this.messageSource.getMessage(ScaleGraphicsCodeController.ADD_SUCCESS_MESSAGE_KEY,
				new Object[]{graphicsCode.getScaleGraphicsCode()},
				ScaleGraphicsCodeController.DEFAULT_ADD_SUCCESS_MESSAGE, request.getLocale());
		return new ModifiedEntity<>(graphicsCode, updateMessage);
	}

	/**
	 * Used to update the modified Scale Graphic Code info to the repository.
	 *
	 * @param scaleGraphicsCode The modified nutrient info.
	 * @param request	The HTTP request that initiated this call.
	 * @return	Returns copy of the updated nutrient.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.PUT)
	public ModifiedEntity<ScaleGraphicsCode> update(@RequestBody ScaleGraphicsCode scaleGraphicsCode,
													HttpServletRequest request) {
		this.log(request.getRemoteAddr(), UPDATE_BY_PATTERN_LOG_MESSAGE, scaleGraphicsCode);
		ScaleGraphicsCode graphicsCode = scaleGraphicsCodeService.update(scaleGraphicsCode);
		String updateMessage = this.messageSource.getMessage(ScaleGraphicsCodeController.UPDATE_SUCCESS_MESSAGE_KEY,
				new Object[]{scaleGraphicsCode.getScaleGraphicsCode()},
				ScaleGraphicsCodeController.DEFAULT_UPDATE_SUCCESS_MESSAGE, request.getLocale());
		return new ModifiedEntity<>(graphicsCode, updateMessage);
	}

	/**
	 * Used to delete a particular Scale Graphic Code from the repository.
	 *
	 * @param scaleGraphicsCode The id of the Scale Graphic Code that is to be deleted.
	 * @param request The HTTP request that initiated this call.
	 * @return Returns copy of the Scale Graphic Code that was deleted.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.DELETE)
	public ModifiedEntity<Long> delete(@RequestParam("scaleGraphicsCode") Long scaleGraphicsCode,
									   HttpServletRequest request) {
		this.log(request.getRemoteAddr(), DELETE_BY_PATTERN_LOG_MESSAGE, scaleGraphicsCode);
		scaleGraphicsCodeService.delete(scaleGraphicsCode);
		String updateMessage = this.messageSource.getMessage(ScaleGraphicsCodeController.DELETE_SUCCESS_MESSAGE_KEY,
				new Object[]{scaleGraphicsCode}, ScaleGraphicsCodeController.DEFAULT_DELETE_SUCCESS_MESSAGE,
				request.getLocale());

		return new ModifiedEntity<>(scaleGraphicsCode, updateMessage);
	}


	/**
	 * Returns a list of scale upc data matching a description.
	 *
	 * @param graphicsCode The graphics code to look for data about.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of scale upc data.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "scaleUpc")
	public PageableResult<ScaleUpc> findScaleUpcByGraphicsCode(
			@RequestParam(value = "graphicsCode", required = true) Long graphicsCode,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			HttpServletRequest request){
		this.parameterValidator.validate(graphicsCode, DEFAULT_ERROR_MESSAGE,
				MESSAGE_KEY_GRAPHICS_CODE, request.getLocale());
		this.log(request.getRemoteAddr(), "Get UPC by graphics code", graphicsCode);

		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? DEFAULT_PAGE : page;

		return this.scaleGraphicsCodeService.findByGraphicsCode(graphicsCode, ic, pg, pageSize);
	}

	/**
	 * Returns the Hits count with match and non-match, along with non-match plus from the input list.
	 *
	 * @param graphicsCodes The graphics codes searched for.
	 * @param request The HTTP request that initiated this call.
	 * @return Hits result with Not found UPCs List
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="hits")
	public Hits findHitsByGraphicsCodes(@RequestParam("graphicsCodes") List<Long> graphicsCodes,
										HttpServletRequest request) {
		this.parameterValidator.validate(graphicsCodes, DEFAULT_ERROR_MESSAGE,
				MESSAGE_KEY_GRAPHICS_CODE, request.getLocale());
		this.log(request.getRemoteAddr(), "Find hits by graphics codes", graphicsCodes);
		return this.scaleGraphicsCodeService.findHitsByGraphicsCodes(graphicsCodes);
	}

	/**
	 * This will take a ScaleGraphicsCode and populate the counts of upcs that has this associated with this
	 * scale graphics.
	 *
	 * @param scaleGraphicsCode scale graphics to populate with the count of upc.
	 */
	private void resolveCounts(ScaleGraphicsCode scaleGraphicsCode) {
		int scaleScanCodeCount = this.scaleGraphicsCodeService.getCountByGraphicsCode(
				scaleGraphicsCode.getScaleGraphicsCode());
		scaleGraphicsCode.setScaleScanCodeCount(scaleScanCodeCount);
	}

	/**
	 * Logs the incoming requests from the user for  Scale Graphic Code.
	 *
	 * @param ip The IP address of the user searching for bdms.
	 * @param pattern The patters of the log message that explains action requested by the user.
	 * @param requestParam The info the user requested to be acted upon.
	 *
	 */
	private void log(String ip, String pattern, Object requestParam) {
		ScaleGraphicsCodeController
				.logger.info(
				String.format(pattern,
						this.userInfo.getUserId(), ip, requestParam)
		);
	}

}
