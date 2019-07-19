package com.heb.gdsn;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * REST controller for GDSN vendor subscriptions.
 *
 * @author d116773
 * @since 2.3.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + VendorSubscriptionController.VENDOR_SUBSCRIPTION_URL)
@AuthorizedResource(ResourceConstants.GDSN_VENDOR_SUBSCRIPTION)
public class VendorSubscriptionController {

	private static final Logger logger = LoggerFactory.getLogger(VendorSubscriptionController.class);

	protected static final String VENDOR_SUBSCRIPTION_URL = "/gdsn/vendorSubscription";

	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_PAGE_SIZE = 50;
	private static final boolean DEFAULT_INCLUDE_COUNTS = true;

	private static final String LOG_ALL_MESSAGE =
			"User %s from IP %s has requested a list of all GDSN vendor subscriptions";
	private static final String LOG_FIND_BY_GLN_MESSAGE =
			"User %s from IP %s has requested a list of all GDSN vendor subscriptions for GLN %s";
	private static final String LOG_FIND_BY_VENDOR_NAME_MESSAGE =
			"User %s from IP %s has requested a list of all GDSN vendor subscriptions for vendors '%s'";
	private static final String LOG_SAVE_VENDOR_MESSAGE =
			"User %s from IP %s has requested to save the vendor %s";

	private static final String DEFAULT_ADD_SUCCESS_MESSAGE = "Vendor successfully added";
	private static final String ADD_SUCCESS_MESSAGE_KEY = "VendorSubscriptionController.addSuccessful";

	private VendorSubscriptionResolver resolver = new VendorSubscriptionResolver();

	@Autowired
	private VendorSubscriptionService vendorSubscriptionService;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private MessageSource messageSource;

	/**
	 * Saves a vendor subscription record.
	 *
	 * @param vendorSubscription The vendor subscription to save.
	 * @param request The HTTP request that initiated this call.
	 * @return A ModifiedEntity with the saved request and a message.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.PUT)
	@GdsnTransactional
	public ModifiedEntity<VendorSubscription> saveVendorSubscription(@RequestBody VendorSubscription vendorSubscription,
																	 HttpServletRequest request) {

		this.logSaveVendor(request.getRemoteAddr(), vendorSubscription);

		VendorSubscription vs = this.vendorSubscriptionService.addSubscription(vendorSubscription, request.getLocale());

		String updateMessage = this.messageSource.getMessage(VendorSubscriptionController.ADD_SUCCESS_MESSAGE_KEY,
				null, VendorSubscriptionController.DEFAULT_ADD_SUCCESS_MESSAGE, request.getLocale());
		return new ModifiedEntity<>(vs, updateMessage);
	}

	/**
	 * Returns a page of records of which vendors are set up to get GDSN data from.
	 *
	 * @param page The page being requested.
	 * @param pageSize The number of records being requested.
	 * @param request The HTTP request that initiated this call.
	 *
	 * @return A list of vendor subscription records.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET)
	@GdsnTransactional
	public PageableResult<VendorSubscription> findAll(@RequestParam(value="page", required = false) Integer page,
													  @RequestParam(value="pageSize", required = false)
													  Integer pageSize,
													  @RequestParam(value="includeCounts", required = false)
													  Boolean includeCounts,
													  HttpServletRequest request) {

		this.logFindAll(request.getRemoteAddr());

		int p = page == null ? VendorSubscriptionController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? VendorSubscriptionController.DEFAULT_PAGE_SIZE : pageSize;

		PageableResult<VendorSubscription> result = this.vendorSubscriptionService.findAll(p, ps);
		result.getData().forEach(this.resolver::fetch);
		return result;
	}

	/**
	 * Returns a page of records of which vendors are set up to get GDSN data from filtering by GLN.
	 *
	 * @param page The page being requested.
	 * @param pageSize The number of records being requested.
	 * @param includeCounts Whether or not to include record counts in the response.
	 * @param gln The GLN to search for.
	 * @param request The HTTP request that initiated this call.
	 *
	 * @return A list of vendor subscription records.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="/gln")
	@GdsnTransactional
	public PageableResult<VendorSubscription> findByGln(@RequestParam(value="page", required = false) Integer page,
														@RequestParam(value="pageSize", required = false)
														Integer pageSize,
														@RequestParam(value="includeCounts", required = false)
														Boolean includeCounts,
														@RequestParam(value="gln") String gln,
														HttpServletRequest request) {

		this.logFindByGln(request.getRemoteAddr(), gln);

		int p = page == null ? VendorSubscriptionController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? VendorSubscriptionController.DEFAULT_PAGE_SIZE : pageSize;
		boolean ic = includeCounts == null ? VendorSubscriptionController.DEFAULT_INCLUDE_COUNTS : includeCounts;

		PageableResult<VendorSubscription> result =  this.vendorSubscriptionService.findByGln(p, ps, ic, gln);
		result.getData().forEach(this.resolver::fetch);
		return result;
	}

	/**
	 * Returns a page of records of which vendors are set up to get GDSN data from filtering by vendor name.
	 *
	 * @param page The page being requested.
	 * @param pageSize The number of records being requested.
	 * @param includeCounts Whether or not to include record counts in the response.
	 * @param vendorName The GLN to search for.
	 * @param request The HTTP request that initiated this call.
	 *
	 * @return A list of vendor subscription records.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="/vendorName")
	@GdsnTransactional
	public PageableResult<VendorSubscription> findByVendorName(@RequestParam(value="page", required = false) Integer page,
													  @RequestParam(value="pageSize", required = false)
													  Integer pageSize,
													  @RequestParam(value="includeCounts", required = false)
													  Boolean includeCounts,
													  @RequestParam(value="vendorName") String vendorName,
													  HttpServletRequest request) {

		this.logFindByVendorName(request.getRemoteAddr(), vendorName);

		int p = page == null ? VendorSubscriptionController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? VendorSubscriptionController.DEFAULT_PAGE_SIZE : pageSize;
		boolean ic = includeCounts == null ? VendorSubscriptionController.DEFAULT_INCLUDE_COUNTS : includeCounts;

		PageableResult<VendorSubscription> result =
				this.vendorSubscriptionService.findByVendorName(p, ps, ic, vendorName);
		result.getData().forEach(this.resolver::fetch);
		return result;
	}

	/**
	 * Logs a user's request of all vendor subscription records.
	 *
	 * @param ip The IP address the request is coming from.
	 */
	private void logFindAll(String ip) {
		VendorSubscriptionController.logger.info(
				String.format(VendorSubscriptionController.LOG_ALL_MESSAGE, this.userInfo.getUserId(), ip)
		);
	}

	/**
	 * Logs a user's request of all vendor subscription records for a GLN.
	 *
	 * @param ip The IP address the request is coming from.
	 * @param gln The GLN the user is searching from.
	 */
	private void logFindByGln(String ip, String gln) {
		VendorSubscriptionController.logger.info(
				String.format(VendorSubscriptionController.LOG_FIND_BY_GLN_MESSAGE, this.userInfo.getUserId(), ip, gln)
		);
	}

	/**
	 * Logs a user's request of all vendor subscription records.
	 *
	 * @param ip The IP address the request is coming from.
	 * @param vendorName The name of the vendor the user is looking for.
	 */
	private void logFindByVendorName(String ip, String vendorName) {
		VendorSubscriptionController.logger.info(
				String.format(VendorSubscriptionController.LOG_FIND_BY_VENDOR_NAME_MESSAGE, this.userInfo.getUserId(),
						ip, vendorName)
		);
	}


	/**
	 * Logs a user's request to add a vendor subscription.
	 *
	 * @param ip The IP address the request is coming from.
	 * @param vendorSubscription The vendor subscription they are trying to save.
	 */
	private void logSaveVendor(String ip, VendorSubscription  vendorSubscription) {
		VendorSubscriptionController.logger.info(
				String.format(VendorSubscriptionController.LOG_SAVE_VENDOR_MESSAGE, this.userInfo.getUserId(),
						ip, vendorSubscription)
		);
	}
}
