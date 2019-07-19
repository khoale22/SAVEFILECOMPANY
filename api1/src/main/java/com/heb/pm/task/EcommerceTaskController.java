/*
 * EcommerceTaskController
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import com.heb.pm.entity.AlertComment;
import com.heb.pm.entity.AlertStaging;
import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.CandidateComments;
import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.entity.User;
import com.heb.pm.productSearch.ProductSearchCriteria;
import com.heb.pm.productSearch.RawSearchCriteria;
import com.heb.pm.ws.CheckedSoapException;
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
import java.math.BigInteger;
import java.util.List;
import java.util.Locale;

/**
 * EcommerceTaskController is used to handle requests associates to eCommerce tasks (alerts).
 *
 * @author vn40486
 * @since 2.14.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + EcommerceTaskController.ECOMMERCE_TASK_URL)
@AuthorizedResource(ResourceConstants.TASK_ECOMMERCE)
public class EcommerceTaskController {
    private static final Logger logger = LoggerFactory.getLogger(EcommerceTaskController.class);

    /**
     * Ecommerce task base url.
     */
    protected  static final String ECOMMERCE_TASK_URL = "/task/ecommerceTask";
    private static final String EXPORT_TASK_DETAIL_TO_CSV_URL = "/exportTaskDetailToCsv";
    private static final String UPDATE_MASS_FILL_TO_PRODUCT = "/updateMassFillToProduct";
    private static final String ASSIGN_TO_BDM = "/assignToBdm";
    private static final String ASSIGN_TO_EBM = "/assignToEbm";
    /*Message Constants*/
    private static final String MESSAGE_KEY_ADD_PRODUCTS_SUBMIT_SUCCESS = "EcommerceTaskController.products.add.submit.success";
    private static final String MESSAGE_KEY_ADD_PRODUCTS_SUBMIT_FAILURE = "EcommerceTaskController.products.add.submit.failure";
    private static final String MESSAGE_KEY_DELETE_PRODUCTS_SUBMIT_SUCCESS ="EcommerceTaskController.products.delete.submit.success";
    private static final String MESSAGE_KEY_DELETE_PRODUCTS_SUBMIT_FAILURE = "EcommerceTaskController.products.delete.submit.failure";
    private static final String MESSAGE_KEY_DELETE_PRODUCTS_SUCCESS = "EcommerceTaskController.products.delete.success";
    private static final String MESSAGE_KEY_UPDATE_TASK_SUCCESS = "EcommerceTaskController.task.update.success";
    private static final String MESSAGE_KEY_UPDATE_TASK_FAILURE = "EcommerceTaskController.task.update.fail";
    private static final String MESSAGE_KEY_DELETE_TASK_SUCCESS = "EcommerceTaskController.task.delete.success";
    private static final String MESSAGE_KEY_DELETE_TASK_FAILURE = "EcommerceTaskController.task.delete.fail";

    /**
     * Log constants.
     */
    private static final String LOG_REQUEST_COUNT_REQUEST =
            "User %s from IP %s has submitted request to get count of all open ecommerce tasks.";
    private static final String LOG_REQUEST_GET_TASK =
            "User %s from IP %s has submitted request to get ecommerce tasks by: {includeCounts:%B, page:%d, " +
                    "pageSize:%d} ";
    private static final String LOG_REQUEST_GET_TASK_INFO =
            "User %s from IP %s has submitted request to get ecommerce task info by taskId:%d";
    private static final String LOG_REQUEST_UPDATE_TASK =
            "User %s from IP %s has submitted request to update task info for task: %s";
    private static final String LOG_REQUEST_GET_TASK_DETAIL =
            "User %s from IP %s has submitted request to get ecommerce task detail by {trackingId:%d, assignee:%s, " +
                    "showOnSite:%s, includeCounts:%B, page:%d, pageSize:%d}";
    private static final String LOG_REQUEST_GET_TASK_PRODUCT =
            "User %s from IP %s has submitted request to get ecommerce task product by {trackingId:%d, assignee:%s, " +
                    "showOnSite:%s, includeCounts:%B, page:%d, pageSize:%d}";
    private static final String LOG_REQUEST_GET_PRODUCT_NOTES =
            "User %s from IP %s has submitted request to get product notes by workId:%d";
    private static final String LOG_REQUEST_ADD_PRODUCT_NOTES =
            "User %s from IP %s has submitted request to add a new product notes : %s";
    private static final String LOG_REQUEST_UPDATE_PRODUCT_NOTE =
            "User %s from IP %s has submitted request to update a product notes : %s";
    private static final String LOG_REQUEST_DELETE_PRODUCT_NOTE =
            "User %s from IP %s has submitted request to delete a product notes : %s";
    private static final String LOG_REQUEST_CREATE_NEW_TASK =
            "User %s from IP %s has submitted request to create a new task by name : %s";
    private static final String LOG_REQUEST_ADD_PRODUCTS =
            "User %s from IP %s has submitted request to add products with criteria : %s";
    private static final String LOG_REQUEST_DELETE_PRODUCTS =
            "User %s from IP %s has submitted request to delete task products with : trackingId=%d , products=%s";
    private static final String LOG_REQUEST_DELETE_ALL_PRODUCTS =
            "User %s from IP %s has submitted request to delete all task products with : trackingId=%d , excluded-products=%s";
    private static final String LOG_REQUEST_GET_PRODUCTS_ASSIGNEE =
            "User %s from IP %s has submitted request to get all products assignee by : trackingId=%d";
    private static final String LOG_REQUEST_GET_TASK_NOTES =
            "User %s from IP %s has submitted request to get all task notes by : task id=%d";
    private static final String LOG_REQUEST_ADD_TASK_NOTE =
            "User %s from IP %s has submitted request to add a new task note : %s";
    private static final String LOG_REQUEST_UPDATE_TASK_NOTE =
            "User %s from IP %s has submitted request to update a task note : %s";
    private static final String LOG_REQUEST_DELETE_TASK_NOTE =
            "User %s from IP %s has submitted request to delete a task note : %s";
    private static final String EXPORT_TASK_DETAIL =
            "User %s from IP %s requested to export all eCommerce task details with : trackingId=%d , assignee=%s , showOnSite=%s";
    private static final String GENERATE_TASK_DETAIL_REPORT = "generate task detail report error : %s";
    private static final String LOG_REQUEST_UPDATE_TASK_MASS_FILL =
            "User %s from IP %s has submitted request to update task mass fill.";
    private static final String MESSAGE_MASS_UPDATE_SUBMIT_SUCCESS = "Successfully Updated.";
    private static final String MESSAGE_ASSIGN_SUBMIT_SUCCESS = "Successfully Assigned.";
    private static final String LOG_REQUEST_PUBLISH_REQUEST =
            "User %s from IP %s has submitted request to assign publish on eCommerce view task %s.";
    private static final String MESSAGE_KEY_DELETE_COMMENT_SUCCESS = "Note deleted successfully!!";
    private static final String MESSAGE_KEY_UPDATE_COMMENT_SUCCESS = "Note updated successfully!!";
    private static final String ACTIVE_TASKS = "Active";
    private static final String ACTIVE_AND_CLOSED_TASKS = "ActiveAndClosed";
    @Autowired
    private EcommerceTaskService ecommerceTaskService;
    @Autowired
    private UserInfo userInfo;
    @Autowired
    private MessageSource messageSource;

    /**
     * Used to get the count of all active ecommerce task by the signed in user id.
     * @param request
     * @return count of open task by user.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "/ecommerceTaskCount")
    public ModifiedEntity<Long> getActiveEcommerceTaskCount(HttpServletRequest request) {
        this.logRequest(LOG_REQUEST_COUNT_REQUEST, this.userInfo.getUserId(), request.getRemoteAddr() , null);
        ModifiedEntity<Long> modifiedEntity = new ModifiedEntity<Long>(
                this.ecommerceTaskService.getActiveTaskCountByUser(this.userInfo.getUserId()), null);
        return modifiedEntity;
    }

    /**
     * Returns nutrition updates alerts filtered by page size.
     * @param includeCounts include pagination counts info.
     * @param page page number.
     * @param pageSize page size.
     * @param taskStatus task Status.
     * @return list of alert staging results.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "/tasks")
    PageableResult<AlertStaging> getTasks(
            @RequestParam(value = "includeCounts", required = true) Boolean includeCounts,
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "pageSize", required = true) Integer pageSize,
            @RequestParam(value = "taskStatus", required = true) String taskStatus,
            HttpServletRequest request) {
        this.logRequest(LOG_REQUEST_GET_TASK, this.userInfo.getUserId(), request.getRemoteAddr(), includeCounts, page,
                pageSize);
        if (taskStatus.equals(ACTIVE_AND_CLOSED_TASKS)) {
            return ecommerceTaskService.getAllTasks(includeCounts, page, pageSize, this.userInfo.getUserId());
        }else if(taskStatus.equals(ACTIVE_TASKS)){
            return ecommerceTaskService.getAllActiveTasks(includeCounts, page, pageSize, this.userInfo.getUserId());
        }
        return ecommerceTaskService.getAllClosedTasks(includeCounts, page, pageSize, this.userInfo.getUserId());
    }

    /**
     * Fetches high level task info that contains the task name , description and most importantly the tracking id
     * which will be used to fetch further details.
     * @param taskId alert id or task id.
     * @param request http request.
     * @return alert or task info.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "/taskInfo")
    AlertStaging getTaskInfo(@RequestParam(value = "taskId", required = true) int taskId,
                                          HttpServletRequest request) {
        this.logRequest(LOG_REQUEST_GET_TASK_INFO, this.userInfo.getUserId(), request.getRemoteAddr(),  taskId);
        return ecommerceTaskService.getTaskInfo(taskId);
    }

    /**
     * Handles upating a task's high level info like  task description and task status active/closed.
     * @param alertStaging task info with modified task description and status.
     * @param taskId task or alert id.
     * @param request
     * @return confirmation/status on update request.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.PUT, value = "/{taskId}")
    ResponseEntity<ModifiedEntity> updateTask(@RequestBody AlertStaging alertStaging,
                                                 @PathVariable("taskId") Integer taskId, HttpServletRequest request) {
        this.logRequest(LOG_REQUEST_UPDATE_TASK, this.userInfo.getUserId(), request.getRemoteAddr(), alertStaging);
        alertStaging.setAlertStatusUserId(userInfo.getUserId().toUpperCase());
        try {
            this.ecommerceTaskService.updateTask(alertStaging);
        } catch (CheckedSoapException e) {
            logger.error(getMessage(MESSAGE_KEY_UPDATE_TASK_FAILURE,request.getLocale()),e.getMessage());
            return generateResponse(null, MESSAGE_KEY_UPDATE_TASK_FAILURE, request.getLocale(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return generateResponse(null, MESSAGE_KEY_UPDATE_TASK_SUCCESS, request.getLocale(), HttpStatus.OK);
    }

    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = "/delete")
    ResponseEntity<ModifiedEntity> deleteTask(@RequestBody AlertStaging alertStaging,
                                              HttpServletRequest request) throws CheckedSoapException {
        this.logRequest(LOG_REQUEST_UPDATE_TASK, this.userInfo.getUserId(), request.getRemoteAddr(), alertStaging);
        alertStaging.setAlertStatusUserId(userInfo.getUserId().toUpperCase());
        try {
            this.ecommerceTaskService.deleteTask(alertStaging);
        } catch (CheckedSoapException e) {
            logger.error(getMessage(MESSAGE_KEY_DELETE_TASK_FAILURE,request.getLocale()),e.getMessage());
            return generateResponse(null, MESSAGE_KEY_DELETE_TASK_FAILURE, request.getLocale(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return generateResponse(null, MESSAGE_KEY_DELETE_TASK_SUCCESS, request.getLocale(), HttpStatus.OK);
    }

    /**
     * Fetches list of products tied to a task based on the pagination criteria.
     *
     * @param trackingId    tracking number of the selected task.
     * @param assignee      user id to whom certain products under the task is assigned to.
     * @param showOnSite    show on site Y/N.
     * @param salesChannels list of sales channels filter.
     * @param includeCounts include pagination counts info.
     * @param page          page number.
     * @param pageSize      page size.
     * @param request       HttpServletRequest
     * @return paginated list of candidate work request referenced products.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "/taskDetail")
    PageableResult<CandidateWorkRequest> getTaskDetail(
            @RequestParam(value = "trackingId", required = true) Long trackingId,
            @RequestParam(value = "assignee", required = false) String assignee,
            @RequestParam(value = "showOnSite", required = false) String showOnSite,
            @RequestParam(value = "salesChannelsFilter", required = false) String salesChannels,
            @RequestParam(value = "includeCounts", required = true) Boolean includeCounts,
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "pageSize", required = true) Integer pageSize,
            HttpServletRequest request) {
        this.logRequest(LOG_REQUEST_GET_TASK_DETAIL, this.userInfo.getUserId(), request.getRemoteAddr(), trackingId,
                assignee, showOnSite, includeCounts, page, pageSize);
        return this.ecommerceTaskService.getTaskDetail(trackingId, assignee, showOnSite, salesChannels, includeCounts, page, pageSize);
    }

    /**
     * Fetches list of products id tied to a task based on the pagination criteria.
     * And We are not get all detail product
     *
     * @param trackingId    tracking number of the selected task.
     * @param assignee      user id to whom certain products under the task is assigned to.
     * @param showOnSite    show on site Y/N.
     * @param salesChannels list of sales channels filter.
     * @param includeCounts include pagination counts info.
     * @param page          page number.
     * @param pageSize      page size.
     * @param request       HttpServletRequest
     * @return paginated list of candidate work request referenced products.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "/taskProducts")
    PageableResult<CandidateWorkRequest> getTaskProducts  (
            @RequestParam(value = "trackingId", required = true) Long trackingId,
            @RequestParam(value = "assignee", required = false) String assignee,
            @RequestParam(value = "showOnSite", required = false) String showOnSite,
            @RequestParam(value = "salesChannelsFilter", required = false) String salesChannels,
            @RequestParam(value = "includeCounts", required = true) Boolean includeCounts,
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "pageSize", required = true) Integer pageSize,
            HttpServletRequest request) {
        this.logRequest(LOG_REQUEST_GET_TASK_PRODUCT, this.userInfo.getUserId(), request.getRemoteAddr(), trackingId,
                assignee, showOnSite, includeCounts, page, pageSize);
        return this.ecommerceTaskService.getTaskProductsInfo(trackingId, assignee, showOnSite, salesChannels, includeCounts, page, pageSize);
    }

    /**
     * Used to get the list of comments or notes pertaining to a given work request referenced product.
     * @param workRequestId work request id.
     * @param request
     * @return list of notes or comments of a work request.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "/productNotes/{workRequestId}")
    List<CandidateComments> getProductNotes(@PathVariable Long workRequestId, HttpServletRequest request) {
        this.logRequest(LOG_REQUEST_GET_PRODUCT_NOTES, this.userInfo.getUserId(), request.getRemoteAddr(),  workRequestId);
        return this.ecommerceTaskService.getProductComments(workRequestId);
    }

    /**
     * Used to add new comments or notes to a work request.
     * @param comment new comment to saved.
     * @param request
     * @return saved comment or notes record.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = "/productNotes/add")
    CandidateComments addProductNotes(@RequestBody CandidateComments comment, HttpServletRequest request) {
        this.logRequest(LOG_REQUEST_ADD_PRODUCT_NOTES, this.userInfo.getUserId(), request.getRemoteAddr(),  comment);
        comment.setUserId(userInfo.getUserId());
        return this.ecommerceTaskService.addProductNotes(comment);
    }

    /**
     * Update comments or notes to a work request.
     * @param comments comment to update.
     * @param request
     * @return saved comment or notes record.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = "/productNotes/update")
    ModifiedEntity<CandidateComments> updateProductNotes(@RequestBody CandidateComments comments,
                                                         HttpServletRequest request) {
        this.logRequest(LOG_REQUEST_UPDATE_PRODUCT_NOTE, this.userInfo.getUserId(), request.getRemoteAddr(),  comments);
        comments.setUserId(userInfo.getUserId());
        comments = this.ecommerceTaskService.updateProductNotes(comments);
        String updateMessage = this.messageSource.getMessage(EcommerceTaskController.MESSAGE_KEY_UPDATE_COMMENT_SUCCESS,
                new Object[]{comments}, EcommerceTaskController.MESSAGE_KEY_UPDATE_COMMENT_SUCCESS, request.getLocale());
        return new ModifiedEntity<>(comments, updateMessage);
    }

    /**
     * Delete comments or notes to a work request.
     * @param comment comment to delete.
     * @param request
     * @return saved comment or notes record.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = "/productNotes/delete")
    ModifiedEntity<CandidateComments> deleteProductNotes(@RequestBody CandidateComments comment, HttpServletRequest request) {
        this.logRequest(LOG_REQUEST_DELETE_PRODUCT_NOTE, this.userInfo.getUserId(), request.getRemoteAddr(),  comment);
        comment.setUserId(userInfo.getUserId());
        this.ecommerceTaskService.deleteProductNotes(comment);
        String updateMessage = this.messageSource.getMessage(EcommerceTaskController.MESSAGE_KEY_DELETE_COMMENT_SUCCESS,
                new Object[]{comment}, EcommerceTaskController.MESSAGE_KEY_DELETE_COMMENT_SUCCESS, request.getLocale());
        return new ModifiedEntity<>(comment, updateMessage);
    }

    /**
     * Used to create a new ecommerce task based on the task name supplied.
     * @param taskName name of the task to be created.
     * @param request
     * @return the created task number.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResponseEntity<ModifiedEntity> createAlertForEcommerceTask(@RequestBody String taskName, HttpServletRequest request){
        this.logRequest(LOG_REQUEST_CREATE_NEW_TASK, this.userInfo.getUserId(), request.getRemoteAddr(),  taskName);
        BigInteger taskId = this.ecommerceTaskService.createTask(taskName, this.userInfo.getUserId());
        return this.generateResponse(taskId, null, request.getLocale(), HttpStatus.OK);
    }

    /**
     * Handles adding list of products to the task.
     * @param rawSearchCriteria search criteria for products to be added to the task.
     * @param request
     * @return success/failure of batch submit request.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = "/products/add")
    ResponseEntity<ModifiedEntity> addProducts(@RequestBody RawSearchCriteria rawSearchCriteria, HttpServletRequest request) {
        this.logRequest(LOG_REQUEST_ADD_PRODUCTS, this.userInfo.getUserId(), request.getRemoteAddr(),  rawSearchCriteria);
        ProductSearchCriteria productSearchCriteria = rawSearchCriteria.toProductSearchCriteria();
        String submitStatus = this.ecommerceTaskService.addProducts(productSearchCriteria, userInfo.getUserId());
        if(submitStatus.equals(EcommerceTaskService.MESSAGE_BATCH_SUBMIT_SUCCESS)) {
            return this.generateResponse(null, MESSAGE_KEY_ADD_PRODUCTS_SUBMIT_SUCCESS, request.getLocale(), HttpStatus.OK);
        }
        return this.generateResponse(null, MESSAGE_KEY_ADD_PRODUCTS_SUBMIT_FAILURE, request.getLocale(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles delete selected list of products from a task.
     * @param trackingId tracking or transaction id of the task.
     * @param productIds list of products referenced work requests to be deleted.
     * @param request
     * @return delete products request status.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.DELETE, value = "/products/delete")
    ResponseEntity<ModifiedEntity> deleteProducts(
            @RequestParam(value = "trackingId", required = true) Long trackingId,
            @RequestParam(value = "productIds", required = true) List<Long> productIds, HttpServletRequest request) {
        this.logRequest(LOG_REQUEST_DELETE_PRODUCTS, this.userInfo.getUserId(), request.getRemoteAddr(),  trackingId, productIds);
        this.ecommerceTaskService.deleteProducts(trackingId, productIds, userInfo.getUserId());
        return this.generateResponse(null, MESSAGE_KEY_DELETE_PRODUCTS_SUCCESS, request.getLocale(), HttpStatus.OK);
    }

    /**
     * Handles delete of all products from a task in batch mode.
     *
     * @param trackingId         tracking id.
     * @param assignee           assignee
     * @param showOnSite         show on site Y/N.
     * @param salesChannels      list of sales channels filter.
     * @param excludedProductIds the list product id excluded.
     * @param request            the HttpServletRequest
     * @return status of batch submit success/failure.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.DELETE, value = "/products/deleteAll")
    ResponseEntity<ModifiedEntity> deleteAllProducts(
            @RequestParam(value = "trackingId", required = true) Long trackingId,
            @RequestParam(value = "assignee", required = false) String assignee,
            @RequestParam(value = "showOnSite", required = false) String showOnSite,
            @RequestParam(value = "salesChannelsFilter", required = false) String salesChannels,
            @RequestParam(value = "productIds", required = false) List<Long> excludedProductIds,HttpServletRequest request) {
        this.logRequest(LOG_REQUEST_DELETE_ALL_PRODUCTS, this.userInfo.getUserId(), request.getRemoteAddr(),  trackingId, excludedProductIds);
        String submitStatus = this.ecommerceTaskService.deleteAllProducts(trackingId, assignee, showOnSite, salesChannels, excludedProductIds, userInfo.getUserId());

        if(submitStatus.equals(EcommerceTaskService.MESSAGE_BATCH_SUBMIT_SUCCESS)) {
            return generateResponse(null, MESSAGE_KEY_DELETE_PRODUCTS_SUBMIT_SUCCESS, request.getLocale(), HttpStatus.OK);
        }
        return generateResponse(null, MESSAGE_KEY_DELETE_PRODUCTS_SUBMIT_FAILURE, request.getLocale(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * Used to get a list of all users/assignee of the products listed under a task.
     * @param trackingId tracking id.
     * @param request
     * @return list of all users of a task.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "/products/assignee/{trackingId}")
    List<User> getAssignedToUsers(@PathVariable(value = "trackingId") Long trackingId, HttpServletRequest request) {
        this.logRequest(LOG_REQUEST_GET_PRODUCTS_ASSIGNEE, this.userInfo.getUserId(), request.getRemoteAddr(),  trackingId);
        return this.ecommerceTaskService.getAssignedToUsers(trackingId);
    }

    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "/notes/{taskId}")
    List<AlertComment> getTaskNotes(@PathVariable(value = "taskId") Integer taskId, HttpServletRequest request) {
        this.logRequest(LOG_REQUEST_GET_TASK_NOTES, this.userInfo.getUserId(), request.getRemoteAddr(),  taskId);
        return this.ecommerceTaskService.getTaskNotes(taskId);
    }

    /**
     * Used to add new comments or notes to a task.
     * @param comment new comment to saved.
     * @param request
     * @return saved comment or notes record.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = "/notes/add")
    AlertComment addTaskNotes(@RequestBody AlertComment comment, HttpServletRequest request) {
        this.logRequest(LOG_REQUEST_ADD_TASK_NOTE, this.userInfo.getUserId(), request.getRemoteAddr(),  comment);
        comment.setCreateUserID(userInfo.getUserId());
        return this.ecommerceTaskService.addTaskNotes(comment);
    }

    /**
     * Update comments or notes to a task.
     * @param comment new comment to saved.
     * @param request
     * @return saved comment or notes record.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = "/notes/update")
    ModifiedEntity<AlertComment> updateTaskNotes(@RequestBody AlertComment comment, HttpServletRequest request) {
        this.logRequest(LOG_REQUEST_UPDATE_TASK_NOTE, this.userInfo.getUserId(), request.getRemoteAddr(),  comment);
        comment.setCreateUserID(userInfo.getUserId());
        comment = this.ecommerceTaskService.updateTaskNotes(comment);
        String updateMessage = this.messageSource.getMessage(EcommerceTaskController.MESSAGE_KEY_UPDATE_COMMENT_SUCCESS,
                new Object[]{comment}, EcommerceTaskController.MESSAGE_KEY_UPDATE_COMMENT_SUCCESS, request.getLocale());
        return new ModifiedEntity<>(comment, updateMessage);
    }

    /**
     * Delete comments or notes to a task.
     * @param comment new comment to saved.
     * @param request
     * @return saved comment or notes record.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = "/notes/delete")
    ModifiedEntity<AlertComment> deleteTaskNotes(@RequestBody AlertComment comment,
                                                 HttpServletRequest request) {
        this.logRequest(LOG_REQUEST_DELETE_TASK_NOTE, this.userInfo.getUserId(), request.getRemoteAddr(),  comment);
        comment.setCreateUserID(userInfo.getUserId());
        this.ecommerceTaskService.deleteTaskNotes(comment);
        String updateMessage = this.messageSource.getMessage(EcommerceTaskController.MESSAGE_KEY_DELETE_COMMENT_SUCCESS,
                new Object[]{comment}, EcommerceTaskController.MESSAGE_KEY_DELETE_COMMENT_SUCCESS, request.getLocale());
        return new ModifiedEntity<>(comment, updateMessage);
    }

    /**
     * Used to log the incoming requests.
     * @param logMessage message template to be logged.
     * @param arguments messsage arguments containing request specifics.
     */
    private void logRequest(String logMessage, Object... arguments) {
        EcommerceTaskController.logger.info(String.format(logMessage, arguments));
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
     * Calls excel export for tracking detail.
     *
     * @param trackingId    the tracking id to get detail
     * @param request       The HTTP request that initiated this call.
     * @param response      The HTTP response.
     * @param assignee      assignee
     * @param showOnSite    show on site Y/N.
     * @param salesChannels list of sales channels filter.
     * @author vn87351
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = EXPORT_TASK_DETAIL_TO_CSV_URL, headers = "Accept=text/csv")
    public void streamAllTaskDetails(@RequestParam(value = "trackingId", required = true) Long trackingId,
                                     @RequestParam(value = "downloadId", required = false) String downloadId,
                                     @RequestParam(value = "assignee", required = true) String assignee,
                                     @RequestParam(value = "showOnSite", required = false) String showOnSite,
                                     @RequestParam(value = "salesChannelsFilter", required = false) String salesChannels,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {

        logRequest(EXPORT_TASK_DETAIL, this.userInfo.getUserId(), request.getRemoteAddr() , trackingId, assignee, showOnSite);

        if (downloadId != null) {
            Cookie c = new Cookie(downloadId, downloadId);
            c.setPath("/");
            response.addCookie(c);
        }

        try {
            this.ecommerceTaskService.streamAllTaskDetails(response.getOutputStream(), trackingId, assignee.toUpperCase(), showOnSite, salesChannels);
        } catch (IOException e) {
            EcommerceTaskController.logger.error(String.format(GENERATE_TASK_DETAIL_REPORT,e.getMessage()));
            throw new StreamingExportException(e.getMessage(), e.getCause());
        }
    }
    /**
     * Update mass fill to selected product of eCommerce task.
     *
     * @param massUpdateTaskRequest the massUpdateTaskRequest.
     * @param request
     * @return the ModifiedEntity object that holds tracking id and message.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = UPDATE_MASS_FILL_TO_PRODUCT)
    public ModifiedEntity updateMassFillToProduct(@RequestBody MassUpdateTaskRequest massUpdateTaskRequest, HttpServletRequest request) {
        this.logRequest(LOG_REQUEST_UPDATE_TASK_MASS_FILL, this.userInfo.getUserId(), request.getRemoteAddr());
        ModifiedEntity<Long> modifiedEntity = new ModifiedEntity<>(this.ecommerceTaskService.updateMassFillToProduct(userInfo.getUserId(), massUpdateTaskRequest),MESSAGE_MASS_UPDATE_SUBMIT_SUCCESS);
        return modifiedEntity;
    }
    /**
     * Assign task to bdm for selected product of eCommerce task.
     *
     * @param massUpdateTaskRequest the MassUpdateTaskRequest.
     * @param request
     * @return the ModifiedEntity object that holds tracking id and message.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = ASSIGN_TO_BDM)
    public ModifiedEntity assignToBdm(@RequestBody MassUpdateTaskRequest massUpdateTaskRequest, HttpServletRequest request) {
        this.logRequest(LOG_REQUEST_UPDATE_TASK_MASS_FILL, this.userInfo.getUserId(), request.getRemoteAddr());
        ModifiedEntity<Long> modifiedEntity = new ModifiedEntity<>(this.ecommerceTaskService.assignToBdm(userInfo.getUserId(), massUpdateTaskRequest),MESSAGE_ASSIGN_SUBMIT_SUCCESS);
        return modifiedEntity;
    }
    /**
     * Assign task to ebm for selected product of eCommerce task.
     *
     * @param massUpdateTaskRequest the MassUpdateTaskRequest.
     * @param request
     * @return the ModifiedEntity object that holds tracking id and message.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = ASSIGN_TO_EBM)
    public ModifiedEntity assignToEbm(@RequestBody MassUpdateTaskRequest massUpdateTaskRequest, HttpServletRequest request) {
        this.logRequest(LOG_REQUEST_UPDATE_TASK_MASS_FILL, this.userInfo.getUserId(), request.getRemoteAddr());
        ModifiedEntity<Long> modifiedEntity = new ModifiedEntity<>(this.ecommerceTaskService.assignToEbm(userInfo.getUserId(), massUpdateTaskRequest),MESSAGE_ASSIGN_SUBMIT_SUCCESS);
        return modifiedEntity;
    }

    /**
     * Publish selected product of eCommerce task.
     *
     * @param massUpdateTaskRequest the MassUpdateTaskRequest.
     * @param request the http request.
     * @return tracking Id.
     */
    @EditPermission
    @RequestMapping(value = "/publishProduct",method = RequestMethod.POST)
    public ModifiedEntity publishProduct(@RequestBody MassUpdateTaskRequest massUpdateTaskRequest, HttpServletRequest request) {
        this.logRequest(LOG_REQUEST_PUBLISH_REQUEST, this.userInfo.getUserId(), request.getRemoteAddr(), massUpdateTaskRequest.toString());
        massUpdateTaskRequest.setUserId(userInfo.getUserId());
        Long trackingId = ecommerceTaskService.submit(massUpdateTaskRequest);
        return new ModifiedEntity<>(trackingId.toString(),MESSAGE_KEY_UPDATE_TASK_SUCCESS);
    }
}