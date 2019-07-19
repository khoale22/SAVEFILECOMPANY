/*
 * NutritionUpdatesController
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import com.heb.pm.HitsBase;
import com.heb.pm.entity.AlertStaging;
import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.product.ProductInfoResolver;
import com.heb.pm.productSearch.ProductSearchCriteria;
import com.heb.pm.productSearch.RawSearchCriteria;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * NutritionUpdatesController is used to handle requests associates to nutrition updates (alerts).
 *
 * @author vn40486
 * @since 2.11.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + NutritionUpdatesTaskController.NUTRITION_UPDATES_URL)
@AuthorizedResource(ResourceConstants.TASK_NUTRITION_UPDATES)
public class NutritionUpdatesTaskController {

    private static final Logger logger = LoggerFactory.getLogger(NutritionUpdatesTaskController.class);

    protected  static final String NUTRITION_UPDATES_URL = "/task/nutritionUpdates";

    /*Message Constants*/
    private static final String REJECT_SUCCESS_MESSAGE_KEY ="NutritionUpdates.reject.success";
    private static final String REJECT_ALL_SUCCESS_MESSAGE_KEY ="NutritionUpdates.reject.all.success";
    private static final String REJECT_ALL_FAILURE_MESSAGE_KEY ="NutritionUpdates.reject.all.failure";

    @Autowired
    private NutritionUpdatesTaskService nutritionUpdatesTaskService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private UserInfo userInfo;
    @Autowired ProductInfoResolver productInfoResolver;

    private static final String LOG_REQUEST_COUNT_REQUEST =
            "User %s from IP %s has submitted request to get count of all open nutrition updates.";
    private static final String LOG_REQUEST_GET_UPDATES =
            "User %s from IP %s has submitted request to get nutrition update alerts by: {includeCounts:%B, page:%d, " +
                    "pageSize:%d} ";
    private static final String LOG_REQUEST_GET_ALL_PRODUCTS =
    		"User %s from IP %s has submitted request to get all products of nutrition update task by: {includeCounts:%B, page:%d, " +
    				"pageSize:%d} ";
    private static final String REJECT_ALL_UPDATES_LOG_MESSAGE =
            "User %s from IP %s has submitted Reject All Nutrition Updates with following exclusions: [%s]";
    private static final String REJECT_UPDATES_LOG_MESSAGE =
            "User %s from IP %s has submitted Reject Nutrition Updates for Alerts: [%s]";
    private static final String LOG_REQUEST_EXPORT_CSV =
            "User %s from IP %s has submitted request to export as csv all tasks";

    private static final String GENERATE_TASK_DETAIL_REPORT = "generate task detail report error : %s";

    private static final String SEARCH_CRITERIA_FOR_NUTRITION_UPDATES = "storedSearchCriteriaForNutritionUpdates";

    private static final String SEARCH_LOG_MESSAGE =
            "User %s from IP %s searched for products with the following criteria: %s";
    private static final String SESSION_DOES_NOT_CONTAIN_SEARCH = "User session does not contain search criteria";
    private static final String WRONG_FORMAT = "Stored search criteria in wrong format";

    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "/nutritionUpdatesCount")
    public ModifiedEntity<Long> getActiveNutritionUpdatesCount(HttpServletRequest request) {
        this.logRequest(LOG_REQUEST_COUNT_REQUEST, this.userInfo.getUserId(), request.getRemoteAddr());
        ModifiedEntity<Long> modifiedEntity = new ModifiedEntity<Long>(this.nutritionUpdatesTaskService.getActiveNutritionUpdatesCount(), null);
        return modifiedEntity;
    }

    /**
     * Logs a user's request to search for products.
     *
     * @param ip The IP the user is logging in from.
     * @param rawSearchCriteria The user's search criteria.
     */
    private void logSearch(String ip, RawSearchCriteria rawSearchCriteria) {

        NutritionUpdatesTaskController.logger.info(String.format(NutritionUpdatesTaskController.SEARCH_LOG_MESSAGE,
                this.userInfo.getUserId(), ip, rawSearchCriteria));
    }

    /**
     * Search active nutrition updates.
     *
     * @param rawSearchCriteria
     * @param request
     * @param userSession
     * @return
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.POST, value = "/searchActiveNutritionUpdates")
    public PageableResult<AlertStaging> searchActiveNutritionUpdates(@RequestBody RawSearchCriteria rawSearchCriteria,
                                                                     HttpServletRequest request, HttpSession userSession) {

        this.logSearch(request.getRemoteAddr(), rawSearchCriteria);

        ProductSearchCriteria productSearchCriteria = rawSearchCriteria.toProductSearchCriteria();
        productSearchCriteria.setSessionId(this.userInfo.getUserId());

        // If this is the first search, save the parameters in the session in case they want to do an export.
        // We resisted putting stuff into the session, but the only mechanism for export we could put together
        // requires that the request be a GET, and the search object is to big to fit into a GET request, so this
        // keeps it in case they need to do an export.
        if (rawSearchCriteria.isFirstSearch() && rawSearchCriteria.isUseSession()) {
            if(userSession.getAttribute(NutritionUpdatesTaskController.SEARCH_CRITERIA_FOR_NUTRITION_UPDATES) != null){
                userSession.removeAttribute(NutritionUpdatesTaskController.SEARCH_CRITERIA_FOR_NUTRITION_UPDATES);
            }
            userSession.setAttribute(NutritionUpdatesTaskController.SEARCH_CRITERIA_FOR_NUTRITION_UPDATES, productSearchCriteria);
        }
       return this.nutritionUpdatesTaskService.searchActiveNutritionUpdates(productSearchCriteria,
                    rawSearchCriteria.getPage(),
                    rawSearchCriteria.getPageSize(), rawSearchCriteria.isFirstSearch());
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
        Object sessionObject = userSession.getAttribute(NutritionUpdatesTaskController.SEARCH_CRITERIA_FOR_NUTRITION_UPDATES);
        if (sessionObject == null) {
            throw new IllegalStateException(NutritionUpdatesTaskController.SESSION_DOES_NOT_CONTAIN_SEARCH);
        }
        if (!(sessionObject instanceof ProductSearchCriteria)) {
            throw new IllegalStateException(NutritionUpdatesTaskController.WRONG_FORMAT);
        }

        ProductSearchCriteria productSearchCriteria = (ProductSearchCriteria)sessionObject;
        // We only support hits for simple searches. Just to make the front end easier to manage, accept the
        // request, but return null.
        if (!productSearchCriteria.isSimpleSearch()) {
            return null;
        }

        productSearchCriteria.setSessionId(this.userInfo.getUserId());
        return this.nutritionUpdatesTaskService.getSearchHits(productSearchCriteria);
    }

    /**
     * Rejects nutrition updates excluding the one in the excludedAlertIds list.
     *
     * @param alertIds
     * @param rejectReason
     * @param request
     * @return
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.DELETE, value = "/rejectUpdates")
    public ModifiedEntity<String> rejectUpdates(
            @RequestParam(value = "alertIds", required = true) List<Integer> alertIds,
            @RequestParam(value = "rejectReason", required = true) String rejectReason,
            HttpServletRequest request) {
        this.logRequest(REJECT_UPDATES_LOG_MESSAGE, request.getRemoteAddr(), this.userInfo.getUserId(), alertIds);
        /**
         * Spring is normally converting single number request parameter to Long. Hence the following typecast.
         */
        if (alertIds != null && alertIds.size() == 1) {
            alertIds.set(0, NumberUtils.toInt(String.valueOf(alertIds.get(0))));
        }
        nutritionUpdatesTaskService.rejectUpdates(alertIds, rejectReason, this.userInfo.getUserId());
        String updateMessage = this.messageSource.getMessage(
                REJECT_SUCCESS_MESSAGE_KEY,
                null,
                REJECT_SUCCESS_MESSAGE_KEY, request.getLocale());
        return new ModifiedEntity<>(null, updateMessage);
    }

    /**
     * Rejects all nutrition updates excluding the one in the excludedAlertIds list.
     * @param massUpdateTaskRequest mass Update Task Request
     * @param request http request.
     * @return reject batch submit status.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = "/rejectAllUpdates")
    public ModifiedEntity<String> rejectAllUpdates(
            @RequestBody MassUpdateTaskRequest massUpdateTaskRequest, HttpServletRequest request) {
        this.logRequest(REJECT_ALL_UPDATES_LOG_MESSAGE, request.getRemoteAddr(), this.userInfo.getUserId(),massUpdateTaskRequest.toString());
        String status = nutritionUpdatesTaskService.rejectAllUpdates(massUpdateTaskRequest, this.userInfo.getUserId());
        //compose reply message.
        String replyMessageKey = status.equals(NutritionUpdatesTaskService.MESSAGE_BATCH_SUBMIT_SUCCESS) ?
                REJECT_ALL_SUCCESS_MESSAGE_KEY :  REJECT_ALL_FAILURE_MESSAGE_KEY;
        String updateMessage = this.messageSource.getMessage(replyMessageKey,null,replyMessageKey, request.getLocale());
        return new ModifiedEntity<>(null, updateMessage);
    }

    /**
     * Used to log the incoming requests.
     * @param logMessage message template to be logged.
     * @param arguments messsage arguments containing request specifics.
     */
    private void logRequest(String logMessage, Object... arguments) {
        NutritionUpdatesTaskController.logger.info(String.format(logMessage, arguments));
    }

    /**
     * Used to get a list of all product update tasks for alert type and assignee.
     * @param request
     * @return list of all tasks.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "/exportNutritionUpdatesToCsv")
    public void exportNutritionUpdatesToCsv(
            @RequestParam(value = "downloadId", required = false) String downloadId,
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession userSession) {
        this.logRequest(LOG_REQUEST_EXPORT_CSV, this.userInfo.getUserId(), request.getRemoteAddr(), downloadId);

        if (downloadId != null) {
            Cookie c = new Cookie(downloadId, downloadId);
            c.setPath("/");
            response.addCookie(c);
        }

        // Get the user's search parameters out of the session
        Object sessionObject = userSession.getAttribute(NutritionUpdatesTaskController.SEARCH_CRITERIA_FOR_NUTRITION_UPDATES);
        if (sessionObject == null) {
            throw new IllegalStateException(NutritionUpdatesTaskController.SESSION_DOES_NOT_CONTAIN_SEARCH);
        }
        if (!(sessionObject instanceof ProductSearchCriteria)) {
            throw new IllegalStateException(NutritionUpdatesTaskController.WRONG_FORMAT);
        }

        ProductSearchCriteria productSearchCriteria = (ProductSearchCriteria)sessionObject;
        productSearchCriteria.setSessionId(this.userInfo.getUserId());
        try {
            this.nutritionUpdatesTaskService.streamAllTaskDetails(response.getOutputStream(), productSearchCriteria);
        } catch (IOException e) {
            NutritionUpdatesTaskController.logger.error(String.format(GENERATE_TASK_DETAIL_REPORT, e.getMessage()));
            throw new StreamingExportException(e.getMessage(), e.getCause());
        }
    }
}
