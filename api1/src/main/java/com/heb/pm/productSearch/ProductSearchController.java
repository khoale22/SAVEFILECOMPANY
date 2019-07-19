package com.heb.pm.productSearch;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.*;
import com.heb.pm.entity.ProductMaster;
import com.heb.pm.product.ProductInfoResolver;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * REST Controller to support the generic product search framework.
 *
 * @author d116773
 * @since 2.13.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + "/newSearch")
@AuthorizedResource(ResourceConstants.PRODUCT_BASIC_INFORMATION)
public class ProductSearchController {

	private static final Logger logger = LoggerFactory.getLogger(ProductSearchController.class);

	private static final String SIMPLE_SEARCH_ONLY_ERROR = "Only simple search can return hits";

	private static final String SEARCH_CRITERIA = "storedSearchCriteria";

	private static final String SEARCH_LOG_MESSAGE =
			"User %s from IP %s searched for products with the following criteria: %s";
	private static final String EXPORT_LOG_MESSAGE =
			"User %s from IP %s has requested an export of their most recent search.";

	@Autowired
	private ProductSearchService productSearchService;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private ProductInfoResolver productInfoResolver;

	/**
	 * Searches for products.
	 *
	 * @param rawSearchCriteria The search criteria passed from the front end.
	 * @param request The HTTP request that initiated this call.
	 * @return A pageable list of products that have been resolved to contain full data.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.POST)
	public PageableResult<ProductMaster> searchForProducts(@RequestBody RawSearchCriteria rawSearchCriteria,
														   HttpServletRequest request, HttpSession userSession) {

		this.logSearch(request.getRemoteAddr(), rawSearchCriteria);

		ProductSearchCriteria productSearchCriteria = rawSearchCriteria.toProductSearchCriteria();
		productSearchCriteria.setSessionId(userSession.getId());

		// If this is the first search, save the parameters in the session in case they want to do an export.
		// We resisted putting stuff into the session, but the only mechanism for export we could put together
		// requires that the request be a GET, and the search object is to big to fit into a GET request, so this
		// keeps it in case they need to do an export.
		if (rawSearchCriteria.isFirstSearch() && rawSearchCriteria.isUseSession()) {
			userSession.setAttribute(ProductSearchController.SEARCH_CRITERIA, productSearchCriteria);
		}

		PageableResult<ProductMaster> data = this.productSearchService.searchForProducts(productSearchCriteria,
				rawSearchCriteria.getPage(),
				rawSearchCriteria.getPageSize(), rawSearchCriteria.isFirstSearch());

		data.getData().forEach(this.productInfoResolver::fetch);
		return data;
	}

	/**
	 * Calculates which of the search parameters a user provided do and do not exist in the system. This function only
	 * works with a simple search. It will return null if the search type is not supported. This is intended to
	 * be called right after a user's initial search. Consequently, the search criteria are pulled from the user's
	 * session.
	 *
	 * @param userSession The user's server session.
	 * @return An object containing the counts of hits and misses and the list of misses from a user's search criteria
	 * or null if the search type is unsupported.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "/hits")
	public HitsBase<?> getHits(HttpSession userSession) {

		// Get the user's search parameters out of the session
		Object sessionObject = userSession.getAttribute(ProductSearchController.SEARCH_CRITERIA);
		if (sessionObject == null) {
			throw new IllegalStateException("User session does not contain search criteria");
		}
		if (!(sessionObject instanceof ProductSearchCriteria)) {
			throw new IllegalStateException("Stored search criteria in wrong format");
		}

		ProductSearchCriteria productSearchCriteria = (ProductSearchCriteria)sessionObject;
		// We only support hits for simple searches. Just to make the front end easier to manage, accept the
		// request, but return null.
		if (!productSearchCriteria.isSimpleSearch()) {
			return null;
		}

		productSearchCriteria.setSessionId(userSession.getId());
		return this.productSearchService.getSearchHits(productSearchCriteria);
	}

	/**
	 * Returns a full export of the user's last search.
	 *
	 * @param downloadId The ID to return back to the front ID for the download.
	 * @param additionalColumns selected export options or additional columns.
	 * @param request The HTTP Request that initiated this call.
	 * @param response The HTTP Response to write the export to.
	 * @param userSession The user's server session.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "exportToCsv", headers = "Accept=text/csv")
	public void exportProducts(@RequestParam(value="downloadId", required = false) String downloadId,
							   @RequestParam(value="additionalColumns", required = false) String additionalColumns,
							   HttpServletRequest request, HttpServletResponse response, HttpSession userSession) {

		this.logExport(request.getRemoteAddr());

		// Get the user's search parameters out of the session
		Object sessionObject = userSession.getAttribute(ProductSearchController.SEARCH_CRITERIA);
		if (sessionObject == null) {
			throw new IllegalStateException("User session does not contain search criteria");
		}
		if (!(sessionObject instanceof ProductSearchCriteria)) {
			throw new IllegalStateException("Stored search criteria in wrong format");
		}


		if (downloadId != null) {
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
		}
		List<String> additionalColumnsList = null;
		if(additionalColumns != null) {
			additionalColumnsList = Arrays.asList(additionalColumns.split(","));
		}
		ProductSearchCriteria productSearchCriteria = (ProductSearchCriteria)sessionObject;
		try {
			this.productSearchService.streamProductSearch(response.getWriter(), productSearchCriteria, additionalColumnsList);
		} catch (IOException e) {
			ProductSearchController.logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Logs a user's request to search for products.
	 *
	 * @param ip The IP the user is logging in from.
	 * @param rawSearchCriteria The user's search criteria.
	 */
	private void logSearch(String ip, RawSearchCriteria rawSearchCriteria) {

		ProductSearchController.logger.info(String.format(ProductSearchController.SEARCH_LOG_MESSAGE,
				this.userInfo.getUserId(), ip, rawSearchCriteria));
	}

	/**
	 * Logs a user's request to export a list of prouducts.
	 *
	 * @param ip The IP the user is logging in from.
	 */
	private void logExport(String ip) {

		ProductSearchController.logger.info(String.format(ProductSearchController.EXPORT_LOG_MESSAGE,
				this.userInfo.getUserId(), ip));
	}
}
