package com.heb.pm.scaleManagement;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.reports.ReportController;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * REST controller for reports out of the scale system.
 *
 * @author d116773
 * @since 2.13.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ReportsController.REPORT_URL)
@AuthorizedResource(ResourceConstants.SCALE_MANAGEMENT_REPORTS)
public class ReportsController {

	private static final Logger logger = LoggerFactory.getLogger(ReportsController.class);

	protected static final String REPORT_URL = "/scaleReports";

	@Autowired
	private ReportsService ingredientReportService;

	@Autowired
	private UserInfo userInfo;

	/**
	 * Streams a report of UPCs whose ingredient statements match a supplied pattern.
	 *
	 * @param ingredientPattern The pattern to match in the ingredient statement.
	 * @param downloadId The ID to put as a cookie in the response so the front the end can track it.
	 * @param request The HTTP request that initiated this report.
	 * @param response The HTTP response to write the report to.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "ingredient")
	public void streamIngredientReport(@RequestParam(value="ingredient") String ingredientPattern,
									   @RequestParam(value="downloadId", required = false)String downloadId,
									   HttpServletRequest request, HttpServletResponse response) {

		// Log the request.
		ReportsController.logger.info(
				String.format("User %s from IP %s has requested an export of ingredient statements containing '%s'",
						this.userInfo.getUserId(), request.getRemoteAddr(), ingredientPattern));

		// Validate the user has access to view the ingredients report.
		if (!this.userInfo.canUserViewResource(ResourceConstants.SCALE_MANAGEMENT_INGREDIENT_REPORT)){
			throw new AccessDeniedException(String.format("User %s does not have access to view the scale ingredient report", this.userInfo.getUserId()));
		}

		// Write the download ID to a cookie.
		if (downloadId != null) {
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
		}

		// Delegate the creation of the report to a service.
		try {
			this.ingredientReportService.streamIngredientReport(response.getOutputStream(), ingredientPattern);
			ReportsController.logger.info("ingredient report complete");
		} catch (IOException e) {
			ReportsController.logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Streams a report of nutrient statements that do not contain a corresponding 2016 statement .
	 * @param request The HTTP request that initiated this report.
	 * @param response The HTTP response to write the report to.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "NLEA2016")
	public void streamNLEA2016Report( @RequestParam(value="downloadId", required = false)String downloadId,
									   HttpServletRequest request,
									   HttpServletResponse response){
		ReportsController.logger.info(
				String.format("User %s from IP %s has requested an export of NLEA2016",
						this.userInfo.getUserId(), request.getRemoteAddr()));
	// Write the download ID to a cookie.
		if (downloadId != null) {
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
		}
		// Delegate the creation of the report to a service.
		try {
			this.ingredientReportService.streamNLEA2016Report(response.getOutputStream());
			ReportsController.logger.info("NLEA2016 report complete");
		} catch (IOException e) {
			ReportsController.logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}
	
    //	Start - Changes are done for Scale Load by Department by Paranthaman,Dhayanithi
	/**
	 * Streams a report of nutrient statements that do not contain a corresponding IngredientsNotInStatements statement .
	 * @param request The HTTP request that initiated this report.
	 * @param response The HTTP response to write the report to.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "ingredientsNotInStatements")
	public void streamIngredientsNotInStatementsReport( @RequestParam(value="downloadId", required = false)String downloadId,
									   HttpServletRequest request,
									   HttpServletResponse response){
		ReportsController.logger.info(
				String.format("User %s from IP %s has requested an export of ingredientsNotInStatements",
						this.userInfo.getUserId(), request.getRemoteAddr()));
	// Write the download ID to a cookie.
		if (downloadId != null) {
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
		}
		// Delegate the creation of the report to a service.
		try {
			this.ingredientReportService.streamIngredientsNotInStatementsReport(response.getOutputStream());
			ReportsController.logger.info("IngredientsNotInStatements report complete");
		} catch (IOException e) {
			ReportsController.logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}
    //	End - Changes are done for Scale Load by Department by Paranthaman,Dhayanithi

}
