package com.heb.scaleMaintenance.endpoint;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ResourceConstants;
import com.heb.scaleMaintenance.ScaleMaintenanceConstants;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceAuthorizeRetail;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceTracking;
import com.heb.scaleMaintenance.entity.ScaleMaintenanceTransmit;
import com.heb.scaleMaintenance.model.ScaleMaintenance;
import com.heb.scaleMaintenance.model.ScaleMaintenanceNutrientStatement;
import com.heb.scaleMaintenance.service.*;
import com.heb.scaleMaintenance.utils.EPlumApiUtils;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Rest endpoint for scale maintenance transactions.
 *
 * @author m314029
 * @since 2.17.8
 */
@RestController()
@RequestMapping(ScaleMaintenanceConstants.BASE_SCALE_MAINTENANCE_URL + CheckStatusEndPoint.BASE_CHECK_STATUS_URL)
@AuthorizedResource(ResourceConstants.SCALE_MAINTENANCE_CHECK_STATUS)
public class CheckStatusEndPoint {

	private static final Logger logger = LoggerFactory.getLogger(CheckStatusEndPoint.class);

	// urls
	static final String BASE_CHECK_STATUS_URL = "/checkStatus";
	private static final String TRANSACTION_ID_PARAMETER = "/{transactionId}";
	private static final String FIND_ALL_TRANSACTIONS_URL = "/findAllTransactions";
	private static final String FIND_ALL_TRANSMITS_URL = "/findAllTransmits";
	private static final String FIND_ALL_RETAILS_URL = "/findAllRetails";
	private static final String EXPORT_ALL_RETAILS_BY_STORE_URL = "/exportAllRetailsByStore";
	private static final String FIND_ALL_SCALE_MAINTENANCE_BY_STORE_AND_TRANSACTION_ID_URL = "/findAllScaleMaintenanceByStoreAndTransactionId";
	private static final String FIND_SERVING_SIZE_DESCRIPTION_BY_NUTRIENT_STATEMENT_URL = "/findServingSizeDescriptionByNutrientStatement";

	// log messages
	private static final String FIND_TRACKING_LOG =
			"User %s from IP %s requested to get scale maintenance tracking " +
					"with paging info (page: %d , page size: %d).";
	private static final String FIND_TRACKING_BY_ID_LOG =
			"User %s from IP %s requested to get scale maintenance tracking with tracking id : %d.";
	private static final String NUTRIENT_STATEMENT_BY_NUTRIENT_CODE_EXPORT ="User %s from IP %s has requested to " +
			"export to excel all retails for the following store id: %d and transaction id: %s.";
	private static final String NUTRIENT_STATEMENT_INFO_BY_NUTRIENT_CODE ="User %s from IP %s has requested  " +
			"all retail information for the following store id: %d and transaction id: %s.";
	private static final String SERVING_SIZE_DESCRIPTION_BY_NUTRIENT_STATEMENT ="User %s from IP %s has requested  " +
			"serving size description by nutrient statement: %s.";
	// error messages
	private static final String TRANSACTION_ID_NOT_FOUND_ERROR = "Transaction id required to search for.";
	private static final String TRANSACTION_ID_NOT_FOUND_ERROR_KEY ="CheckStatusEndPoint.missingTransactionId";
	private static final String STORE_NUMBER_NOT_FOUND_ERROR = "Store Number required to search for.";
	private static final String STORE_NUMBER_NOT_FOUND_ERROR_KEY ="CheckStatusEndPoint.missingStore";

	@Autowired
	private NonEmptyParameterValidator parameterValidator;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private ScaleMaintenanceTrackingService scaleMaintenanceTrackingService;

	@Autowired
	private ScaleMaintenanceTransmitService scaleMaintenanceTransmitService;

	@Autowired
	private ScaleMaintenanceAuthorizeRetailService scaleMaintenanceAuthorizeRetailService;

	@Autowired
	private ScaleMaintenanceService scaleMaintenanceService;

	@Autowired
	private EPlumApiUtils ePlumApiUtils;

	/**
	 * Gets the scale maintenance tracking matching the given transaction id.
	 *
	 * @param transactionId Transaction id to look for.
	 * @param request The HTTP request that initiated this call.
	 * @return Scale maintenance tracking matching the transaction id.
	 */
	@RequestMapping(method = RequestMethod.GET,  value = TRANSACTION_ID_PARAMETER)
	public ScaleMaintenanceTracking findTransactionById(
			@PathVariable Long transactionId, HttpServletRequest request) {

		this.parameterValidator.validate(
				transactionId, TRANSACTION_ID_NOT_FOUND_ERROR, TRANSACTION_ID_NOT_FOUND_ERROR_KEY, request.getLocale());

		CheckStatusEndPoint.logger.info(String.format(FIND_TRACKING_BY_ID_LOG,
				this.userInfo.getUserId(), request.getRemoteAddr(), transactionId));
		return this.scaleMaintenanceTrackingService.findByTransactionId(transactionId);
	}

	/**
	 * Finds the requested page of all scale maintenance tracking.
	 *
	 * @param page The page being requested.
	 * @param pageSize The number of records being requested.
	 * @param includeCount Whether or not to include record count in the response.
	 * @param request The HTTP request that initiated this call.
	 * @return Page of scale maintenance tracking based on given parameters.
	 */
	@RequestMapping(method = RequestMethod.GET, value = FIND_ALL_TRANSACTIONS_URL)
	public PageableResult<ScaleMaintenanceTracking> findTransactions(
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "pageSize") Integer pageSize,
			@RequestParam(value = "includeCount") Boolean includeCount,
			HttpServletRequest request) {

		CheckStatusEndPoint.logger.info(String.format(FIND_TRACKING_LOG,
				this.userInfo.getUserId(), request.getRemoteAddr(), page, pageSize));
		return this.scaleMaintenanceTrackingService.findAllByCreatedTime(page, pageSize, includeCount);
	}

	/**
	 * Finds the requested page of all scale maintenance transmits.
	 *
	 * @param page The page being requested.
	 * @param pageSize The number of records being requested.
	 * @param includeCount Whether or not to include record count in the response.
	 * @param request The HTTP request that initiated this call.
	 * @return Page of scale maintenance tracking based on given parameters.
	 */
	@RequestMapping(method = RequestMethod.GET, value = FIND_ALL_TRANSMITS_URL)
	public PageableResult<ScaleMaintenanceTransmit> findTransmits(
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "pageSize") Integer pageSize,
			@RequestParam(value = "includeCount") Boolean includeCount,
			@RequestParam(value = "transactionId") Long transactionId,
			HttpServletRequest request) {
		this.parameterValidator.validate(
				transactionId, TRANSACTION_ID_NOT_FOUND_ERROR, TRANSACTION_ID_NOT_FOUND_ERROR_KEY, request.getLocale());

		CheckStatusEndPoint.logger.info(String.format(FIND_TRACKING_LOG,
				this.userInfo.getUserId(), request.getRemoteAddr(), page, pageSize));
		return this.scaleMaintenanceTransmitService.
				findAllByTransactionIdOrderedByStore(page, pageSize, includeCount, transactionId);
	}

	/**
	 *
	 * @param page The page being requested.
	 * @param pageSize The number of records being requested.
	 * @param includeCount Whether or not to include record count in the response.
	 * @param request The HTTP request that initiated this call.
	 * @return Page of scale maintenance retail based on given parameters.
	 */

	@RequestMapping(method = RequestMethod.GET, value = FIND_ALL_RETAILS_URL)
	public PageableResult<ScaleMaintenanceAuthorizeRetail> findRetail(
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "pageSize") Integer pageSize,
			@RequestParam(value = "includeCount") Boolean includeCount,
			@RequestParam(value = "transactionId") Long transactionId,
			@RequestParam(value = "store") Integer store,
			HttpServletRequest request) {
		this.parameterValidator.validate(
				transactionId, TRANSACTION_ID_NOT_FOUND_ERROR, TRANSACTION_ID_NOT_FOUND_ERROR_KEY, request.getLocale());
		this.parameterValidator.validate(
				store, STORE_NUMBER_NOT_FOUND_ERROR, STORE_NUMBER_NOT_FOUND_ERROR_KEY, request.getLocale());


		CheckStatusEndPoint.logger.info(String.format(FIND_TRACKING_LOG,
				this.userInfo.getUserId(), request.getRemoteAddr(), page, pageSize));
		return this.scaleMaintenanceAuthorizeRetailService.findAllByTransactionIdAndStore(
				page, pageSize, includeCount, transactionId, store);
	}

	/**
	 * Calls excel export for scale retails by store.
	 *
	 * @param store the store.
	 * @param totalRecordCount the total record count.
	 * @param transactionId the transaction id.
	 * @param downloadId the download id.
	 * @param request The HTTP request that initiated this call.
	 * @param response The HTTP response.
	 */
	@ViewPermission
			@RequestMapping(method = RequestMethod.GET, value = EXPORT_ALL_RETAILS_BY_STORE_URL, headers = "Accept=text/csv")
	public void exportAllRetailsByStore(@RequestParam(name = "store") Integer store,
										@RequestParam(name = "totalRecordCount") int totalRecordCount,
										@RequestParam(value = "transactionId") Long transactionId,
										@RequestParam(value = "downloadId") String downloadId,
										HttpServletRequest request, HttpServletResponse response) {
		CheckStatusEndPoint.logger.info(String.format(NUTRIENT_STATEMENT_BY_NUTRIENT_CODE_EXPORT,
				this.userInfo.getUserId(), request.getRemoteAddr(), store, transactionId));
		if (downloadId != null) {
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
		}
		try {
			this.scaleMaintenanceAuthorizeRetailService.exportCheckStatusRetailCsv(
					response.getOutputStream(), store, transactionId, totalRecordCount);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}
	/**
	 * Calls excel export for scale retails by store.
	 *  @param store the store.
	 * @param transactionId the transaction id.
	 * @param request The HTTP request that initiated this call.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = FIND_ALL_SCALE_MAINTENANCE_BY_STORE_AND_TRANSACTION_ID_URL)
	public PageableResult<ScaleMaintenance> findAllScaleMaintenanceByStoreAndTransactionId(@RequestParam(value = "page") Integer page,
																			  @RequestParam(value = "pageSize") Integer pageSize,
																			  @RequestParam(value = "includeCount") Boolean includeCount,
																			  @RequestParam(name = "store") Integer store,
																			  @RequestParam(value = "transactionId") Long transactionId,
																			  HttpServletRequest request) {
		CheckStatusEndPoint.logger.info(String.format(NUTRIENT_STATEMENT_INFO_BY_NUTRIENT_CODE,
				this.userInfo.getUserId(), request.getRemoteAddr(), store, transactionId));
		return this.scaleMaintenanceService.findAllScaleMaintenanceByStoreAndTransactionId(page, pageSize,includeCount,store,transactionId);
	}

	/**
	 * Returns the serving size description by the nutrient statement object.
	 *
	 * @param nutrientStatement the nutrient statement.
	 * @param request The HTTP request that initiated this call.
	 * @return the serving size description by the nutrient statement object.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.POST, value = FIND_SERVING_SIZE_DESCRIPTION_BY_NUTRIENT_STATEMENT_URL)
	public ModifiedEntity<String> findServingSizeDescriptionByNutrientStatement(@RequestBody ScaleMaintenanceNutrientStatement nutrientStatement, HttpServletRequest request) {

		CheckStatusEndPoint.logger.info(String.format(SERVING_SIZE_DESCRIPTION_BY_NUTRIENT_STATEMENT,
				this.userInfo.getUserId(), request.getRemoteAddr(), nutrientStatement));

		return new ModifiedEntity<>(ePlumApiUtils.getEPlumServingSizeFromScaleMaintenanceNutrient(nutrientStatement), "Successful.");
	}
}
