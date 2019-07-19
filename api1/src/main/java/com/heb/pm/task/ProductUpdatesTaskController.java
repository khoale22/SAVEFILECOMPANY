/*
 * ProductUpdatesTaskController
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import com.heb.pm.entity.AlertStaging;
import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.User;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * ProductUpdatesTaskController is used to handle requests associates to product updates (alerts).
 *
 * @author vn40486
 * @since 2.15.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ProductUpdatesTaskController.PRODUCT_UPDATES_URL)
@AuthorizedResource(ResourceConstants.TASK_PRODUCT_UPDATES)
public class ProductUpdatesTaskController {

    private static final Logger logger = LoggerFactory.getLogger(ProductUpdatesTaskController.class);

    protected  static final String PRODUCT_UPDATES_URL = "/task/productUpdates";
	private static final String MESSAGE_KEY_UPDATE_TASK_SUCCESS = "ProductUpdateTaskController.task.update.success";
	private static final String MESSAGE_KEY_DELETE_ALERT_SUBMIT_SUCCESS ="ProductUpdateController.alert.delete.submit.success";
	private static final String MESSAGE_KEY_DELETE_ALERT_SUCCESS = "ProductUpdateController.alert.delete.success";

    @Autowired
    private ProductUpdatesTaskService productUpdatesTaskService;
    @Autowired
    private UserInfo userInfo;
	@Autowired
	private MessageSource messageSource;

	private static final String LOG_REQUEST_SAVE_REQUEST =
			"User %s from IP %s has submitted request to save product updates %s.";
	private static final String LOG_REQUEST_ASSIGN_BDM_REQUEST =
			"User %s from IP %s has submitted request to assign BDM on product updates %s.";
	private static final String LOG_REQUEST_ASSIGN_EBM_REQUEST =
			"User %s from IP %s has submitted request to assign eBM on product updates %s.";
	private static final String LOG_REQUEST_PUBLISH_REQUEST =
			"User %s from IP %s has submitted request to publish on product updates %s.";
    private static final String LOG_REQUEST_COUNT_REQUEST =
            "User %s from IP %s has submitted request to get count of all open product updates.";
    private static final String LOG_REQUEST_GET_UPDATES =
            "User %s from IP %s has submitted request to get product updates by: {alertType:%s, assignee:%s, " +
                    "attributes:%s, showOnSite%s, includeCounts:%B, page:%d, pageSize:%d} ";
    private static final String LOG_REQUEST_GET_ALL_PRODUCTS =
    		"User %s from IP %s has submitted request to get all product updates by: {alertType:%s, assignee:%s, " +
    				"attributes:%s, showOnSite%s, includeCounts:%B, page:%d, pageSize:%d} ";
    private static final String LOG_REQUEST_GET_PRODUCTS_ASSIGNEE =
            "User %s from IP %s has submitted request to get all assignee of Task Type: %s";
    private static final String LOG_REQUEST_EXPORT_CSV =
            "User %s from IP %s has submitted request to export as csv all tasks with alertType: '%s', assignee: '%s', and downloadId: '%s'";
	private static final String LOG_REQUEST_DELETE_REQUEST =
			"User %s from IP %s has submitted request to delete alerts on product updates %s.";
    private static final String GENERATE_TASK_DETAIL_REPORT = "generate task detail report error : %s";

    /**
     * Returns count of active product updates.
     * @param request
     * @return product updates count.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "/count")
    public ModifiedEntity<Long> getActiveProductUpdatesCount(HttpServletRequest request) {
        this.logRequest(LOG_REQUEST_COUNT_REQUEST, this.userInfo.getUserId(), request.getRemoteAddr());
        ModifiedEntity<Long> modifiedEntity = new ModifiedEntity<Long>(
                this.productUpdatesTaskService.getActiveProductUpdatesCount(), null);
        return modifiedEntity;
    }

    /**
     * Fetches list of all products(alerts) tagged under a given task type (PRUPD/PRRVW/NWPRD) and filtered by the other
     * conditions paased in as the input argumets to this method.
     * @param alertType alert type like PRUPD/PRRVW/NWPRD
     * @param assignee alert assigned to user.
     * @param attributes list of update reasons, where each reason is indetified by it's attribute code.
     * @param showOnSite show on site Y/N.
	 * @param salesChannels list of sales channels filter.
	 * @param includeCounts identifies first fetch where total count of the records is required for pagination.
     * @param page page requested.
     * @param pageSize number of records per page.
     * @return list of alerts (products) of the matchign alert type.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "/products")
    PageableResult<AlertStaging> getProducts(
            @RequestParam(value = "alertType", required = true) String alertType,
            @RequestParam(value = "assignee", required = false) String assignee,
            @RequestParam(value = "attributes", required = false) List<Long> attributes,
            @RequestParam(value = "showOnSite", required = false) String showOnSite,
			@RequestParam(value = "salesChannelsFilter", required = false) String salesChannels,
			@RequestParam(value = "includeCounts", required = true) Boolean includeCounts,
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "pageSize", required = true) Integer pageSize,
            HttpServletRequest request) {
        this.logRequest(LOG_REQUEST_GET_UPDATES, this.userInfo.getUserId(), request.getRemoteAddr(), alertType,
                assignee, attributes, showOnSite, includeCounts, page, pageSize);
        return this.productUpdatesTaskService.getProducts(alertType, assignee, attributes, showOnSite, salesChannels, includeCounts, page, pageSize);
    }

    /**
     * Fetches list of all products(alerts) tagged under a given task type (PRUPD/PRRVW/NWPRD) and filtered by the other
     * conditions paased in as the input argumets to this method.
     * @param alertType alert type like PRUPD/PRRVW/NWPRD
     * @param assignee alert assigned to user.
     * @param attributes list of update reasons, where each reason is indetified by it's attribute code.
     * @param showOnSite show on site Y/N.
	 * @param salesChannels list of sales channels filter.
	 * @param includeCounts identifies first fetch where total count of the records is required for pagination.
     * @param page page requested.
     * @param pageSize number of records per page.
     * @return list of alerts (products) of the matchign alert type.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "/getAllProducts")
    PageableResult<AlertStaging> getAllProducts(
    		@RequestParam(value = "alertType", required = true) String alertType,
    		@RequestParam(value = "assignee", required = false) String assignee,
    		@RequestParam(value = "attributes", required = false) List<Long> attributes,
    		@RequestParam(value = "showOnSite", required = false) String showOnSite,
			@RequestParam(value = "salesChannelsFilter", required = false) String salesChannels,
			@RequestParam(value = "includeCounts", required = true) Boolean includeCounts,
    		@RequestParam(value = "page", required = true) Integer page,
    		@RequestParam(value = "pageSize", required = true) Integer pageSize,
    		HttpServletRequest request) {
    	this.logRequest(LOG_REQUEST_GET_ALL_PRODUCTS, this.userInfo.getUserId(), request.getRemoteAddr(), alertType,
    			assignee, attributes, showOnSite, includeCounts, page, pageSize);
    	return this.productUpdatesTaskService.getAllProducts(alertType, assignee, attributes, showOnSite, salesChannels, includeCounts, page, pageSize);
    }

    /**
     * Used to get a list of all users/assignee of the products listed under a task.
     * @param alertType alert Type.
     * @param request
     * @return list of all users of a task.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "/products/assignee/{alertType}")
    List<User> getAssignedToUsers(@PathVariable(value = "alertType") String alertType, HttpServletRequest request) {
        this.logRequest(LOG_REQUEST_GET_PRODUCTS_ASSIGNEE, this.userInfo.getUserId(), request.getRemoteAddr(),  alertType);
        return this.productUpdatesTaskService.getAssignedToUsers(alertType);
    }

    /**
     * Used to log the incoming requests.
     * @param logMessage message template to be logged.
     * @param arguments messsage arguments containing request specifics.
     */
    private void logRequest(String logMessage, Object... arguments) {
        ProductUpdatesTaskController.logger.info(String.format(logMessage, arguments));
    }

    /**
     * Used to get a list of all product update tasks for alert type and assignee.
     * @param alertType alert Type
     * @param assignee assignee
	 * @param attributes list of update reasons, where each reason is indetified by it's attribute code.
	 * @param showOnSite show on site Y/N.
	 * @param salesChannels list of sales channels filter.
	 * @param request the HttpServletRequest.
	 * @param response the HttpServletResponse.
     * @return list of all tasks.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "/exportProductUpdatesToCsv")
    public void exportProductUpdatesToCsv(
            @RequestParam(value = "downloadId", required = false) String downloadId,
            @RequestParam(value = "alertType") String alertType,
            @RequestParam(value = "assignee") String assignee,
            @RequestParam(value = "attributes", required = false) List<Long> attributes,
            @RequestParam(value = "showOnSite", required = false) String showOnSite,
			@RequestParam(value = "salesChannelsFilter", required = false) String salesChannels,
			HttpServletRequest request,
            HttpServletResponse response) {
        this.logRequest(LOG_REQUEST_EXPORT_CSV, this.userInfo.getUserId(), request.getRemoteAddr(), alertType, assignee, downloadId);

        if (downloadId != null) {
            Cookie c = new Cookie(downloadId, downloadId);
            c.setPath("/");
            response.addCookie(c);
        }

        try {
            this.productUpdatesTaskService.streamAllTaskDetails(response.getOutputStream(), alertType, assignee.toUpperCase(), attributes, showOnSite, salesChannels);
        } catch (IOException e) {
			ProductUpdatesTaskController.logger.error(String.format(GENERATE_TASK_DETAIL_REPORT, e.getMessage()));
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
    }

	/**
	 * Save data for mass fill product update
	 * @param massUpdateTaskRequest mass Update Task Request
	 * @param request the request
	 * @return massUpdateTaskRequest
	 */
	@EditPermission
	@RequestMapping(value = "/saveData",method = RequestMethod.POST)
	public ResponseEntity saveData(@RequestBody MassUpdateTaskRequest massUpdateTaskRequest, HttpServletRequest request) {
		this.logRequest(LOG_REQUEST_SAVE_REQUEST, this.userInfo.getUserId(), request.getRemoteAddr(), massUpdateTaskRequest.toString());
		massUpdateTaskRequest.setUserId(userInfo.getUserId());
		Long trackingId = productUpdatesTaskService.submit(massUpdateTaskRequest);
		return  generateResponse(trackingId,MESSAGE_KEY_UPDATE_TASK_SUCCESS, request.getLocale(), HttpStatus.OK);
		//new ModifiedEntity<>(trackingId.toString(),MESSAGE_KEY_UPDATE_TASK_SUCCESS);
	}

	/**
	 * Used to assign product to BDM
	 * @param massUpdateTaskRequest
	 * @param request
	 * @return ResponseEntity
	 */
	@EditPermission
	@RequestMapping(value = "/assignToBDM",method = RequestMethod.POST)
	public ResponseEntity assignToBDM(@RequestBody MassUpdateTaskRequest massUpdateTaskRequest, HttpServletRequest request) {
		this.logRequest(LOG_REQUEST_ASSIGN_BDM_REQUEST, this.userInfo.getUserId(), request.getRemoteAddr(), massUpdateTaskRequest.toString());
		massUpdateTaskRequest.setUserId(userInfo.getUserId());
		Long trackingId = productUpdatesTaskService.submit(massUpdateTaskRequest);
		return  generateResponse(trackingId,MESSAGE_KEY_UPDATE_TASK_SUCCESS, request.getLocale(), HttpStatus.OK);
	}

	/**
	 * Used to assign product to eBM
	 * @param massUpdateTaskRequest
	 * @param request
	 * @return ResponseEntity
	 */
	@EditPermission
	@RequestMapping(value = "/assignToEBM",method = RequestMethod.POST)
	public ResponseEntity assignToEBM(@RequestBody MassUpdateTaskRequest massUpdateTaskRequest, HttpServletRequest request) {
		this.logRequest(LOG_REQUEST_ASSIGN_EBM_REQUEST, this.userInfo.getUserId(), request.getRemoteAddr(), massUpdateTaskRequest.toString());
		massUpdateTaskRequest.setUserId(userInfo.getUserId());
		Long trackingId = productUpdatesTaskService.submit(massUpdateTaskRequest);
		return  generateResponse(trackingId,MESSAGE_KEY_UPDATE_TASK_SUCCESS, request.getLocale(), HttpStatus.OK);
	}

	/**
	 * Used to publish product
	 * @param massUpdateTaskRequest
	 * @param request
	 * @return
	 */
	@EditPermission
	@RequestMapping(value = "/publishProduct",method = RequestMethod.POST)
	public ResponseEntity publishProduct(@RequestBody MassUpdateTaskRequest massUpdateTaskRequest, HttpServletRequest request) {
		this.logRequest(LOG_REQUEST_PUBLISH_REQUEST, this.userInfo.getUserId(), request.getRemoteAddr(), massUpdateTaskRequest.toString());
		massUpdateTaskRequest.setUserId(userInfo.getUserId());
		Long trackingId = productUpdatesTaskService.submit(massUpdateTaskRequest);
		return  generateResponse(trackingId,MESSAGE_KEY_UPDATE_TASK_SUCCESS, request.getLocale(), HttpStatus.OK);
	}

	/**
	 *
	 * @param massUpdateTaskRequest
	 * @param request
	 * @return ResponseEntity
	 */
	@EditPermission
	@RequestMapping(value = "/deleteAlerts",method = RequestMethod.POST)
	public ResponseEntity deleteAlerts(@RequestBody MassUpdateTaskRequest massUpdateTaskRequest, HttpServletRequest request) {
		this.logRequest(LOG_REQUEST_DELETE_REQUEST, this.userInfo.getUserId(), request.getRemoteAddr(), massUpdateTaskRequest.toString());
		massUpdateTaskRequest.setUserId(userInfo.getUserId());
		if(massUpdateTaskRequest.isSelectAll()){
			Long trackingId =productUpdatesTaskService.submit(massUpdateTaskRequest);
			return generateResponse(trackingId,MESSAGE_KEY_DELETE_ALERT_SUBMIT_SUCCESS, request.getLocale(), HttpStatus.OK);
		}else{
			productUpdatesTaskService.removeAlertNotSelectAll(massUpdateTaskRequest);
			return generateResponse(null,MESSAGE_KEY_DELETE_ALERT_SUCCESS, request.getLocale(), HttpStatus.OK);
		}
	}
	/**
	 * Generates a ResponseEntity to be sent back to controller caller.
	 * @param data response data.
	 * @param messageKey key of the message(messsage_*.properties) to be sent.
	 * @param locale requested user locale.
	 * @param status HTTP response status.
	 * @param <T>
	 * @return
	 */
	private <T> ResponseEntity<ModifiedEntity> generateResponse(final T data, final String messageKey,
																final Locale locale, final HttpStatus status){
		ModifiedEntity<T> modifiedEntity = new ModifiedEntity<T>(data,getMessage(messageKey,locale));
		return new ResponseEntity<ModifiedEntity>(modifiedEntity,status);
	}
	/**
	 * Used to get message by key from the message properties file.
	 * @param messageKey
	 * @param locale
	 * @return
	 */
	private String getMessage(final String messageKey, final Locale locale) {
		return this.messageSource.getMessage(messageKey,null,null, locale);
	}
}
