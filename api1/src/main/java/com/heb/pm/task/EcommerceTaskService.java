/*
 * EcommerceTaskService
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;


import com.heb.pm.CoreEntityManager;
import com.heb.pm.CoreTransactional;
import com.heb.pm.alert.AlertCommentService;
import com.heb.pm.alert.AlertUserResponseService;
import com.heb.pm.customHierarchy.CustomerHierarchyService;
import com.heb.pm.entity.*;
import com.heb.pm.massUpdate.job.JobExecutionException;
import com.heb.pm.massUpdate.job.JobNotDefinedException;
import com.heb.pm.massUpdate.job.MassUpdateProductMap;
import com.heb.pm.productDetails.product.eCommerceView.ProductECommerceViewService;
import com.heb.pm.productSearch.ProductSearchCriteria;
import com.heb.pm.repository.*;
import com.heb.pm.user.UserService;
import com.heb.pm.ws.ApplicationAlertStagingServiceClient;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.util.DateUtils;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.jpa.PageableResult;
import com.heb.util.list.IntegerPopulator;
import com.heb.util.list.LongPopulator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.heb.pm.product.ProductInfoResolver;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This service is used to serve eCommerce task related queries.
 *
 * @author vn40486
 * @since 2.14.0
 */
@Service
public class EcommerceTaskService {
    private static final Logger logger = LoggerFactory.getLogger(EcommerceTaskService.class);

    private static final String MASS_UPDATE_ADD_PRODUCTS_JOB_NAME = "I18X913";
    private static final String MASS_UPDATE_REMOVE_PRODUCTS_JOB_NAME = "I18X914";
    private static final String MASS_UPDATE_ECOMMERCE_TASK_JOB_NAME = "I18X915";
    private static final String ASSIGN_TO_BDM_ECOMMERCE_TASK_JOB_NAME = "I18X917";
    private static final String ASSIGN_TO_EBM_ECOMMERCE_TASK_JOB_NAME = "I18X918";
    private static final String JOB_PARAMETER_USER_ID = "userId";
    private static final String JOB_PARAMETER_TRANSACTION_ID = "transactionId";
    private static final String JOB_PARAMETER_ASSIGNEE_ID = "assigneeId";
    private static final String JOB_PARAMETER_SHOW_ON_SITE = "showOnSite";
    private static final String JOB_PARAMETER_SALES_CHANNELS = "salesChannels";
    private static final String JOB_PARAMETER_ALERT_ID = "alertId";
    private static final String JOB_PARAMETER_ACTION_TYPE = "actionType";
    private static final String USER_ROLE = "USER";
    /**
     * A random number used to to work around the Spring batch's "A job instance already exists and is complete for
     * parameters={..} " exception.
     */
    private static final String JOB_PARAMETER_IDENTIFIER = "time";

    // The default number of parameters to search for. This number and fewer will have the maximum performance.
    private static final int DEFAULT_PRODUCT_ID_COUNT = 100;

    public static final String MESSAGE_BATCH_SUBMIT_SUCCESS = "SUCCESS";
    public static final String MESSAGE_BATCH_SUBMIT_FAILURE = "FAILURE";
    private static final String ERROR_BATCH_SUBMIT_FAILURE = "Error with add products batch submit: %s ";

    public static final String TASK_DETAILS_EXPORT_HEADER = "UPC, Item Code, Channel, Product ID, Product Description, Size Text, Sub Commodity, BDM, Sub Department, Last Modified, Last Modified By";
    private static final String TEXT_EXPORT_FORMAT = "\"%s\",";
    private static final String TEXT_EXPORT_FORMAT_LAST = "\"%s\"";
    private static final String NEWLINE_TEXT_EXPORT_FORMAT = "\n";
    private static final String ERROR_MASS_UPDATE_SUBMIT_FAILURE = "Unable to execute job %s: %s ";

    /**
     * The Product info repository without count.
     */
    @Autowired
    private ApplicationAlertStagingServiceClient alertServiceClient;
    @Autowired
    private AlertStagingRepository alertStagingRepository;
    @Autowired
    private AlertStagingRepositoryWithCount alertStagingRepositoryWithCount;
    @Autowired
    private TransactionTrackingRepository transactionTrackingRepository;
    @Autowired
    private CandidateWorkRequestRepository candidateWorkRequestRepository;
    @Autowired
    private CandidateWorkRequestRepositoryWithCounts candidateWorkRequestRepositoryWithCounts;
    @Autowired
    ProductInfoRepository productInfoRepository;
    @Autowired
    CandidateCommentsRepository candidateCommentsRepository;
    @Autowired
    CustomerHierarchyService customerHierarchyService;
    @Autowired
    AlertCommentService alertCommentService;
    @Autowired
    AlertUserResponseService alertUserResponseService;
    @Autowired
    EcommerceTaskManagementServiceClient taskManagementServiceClient;
    @Autowired
    UserService userService;
    @Autowired
    EcommerceTaskPredicateBuilder predicateBuilder;
    @Autowired
    private MassUpdateProductMap massUpdateProductMap;
    @Autowired
    private JobLocator jobLocator;
    @Autowired
    @Qualifier("asyncJobLauncher")
    private JobLauncher jobLauncher;
    @Autowired
    private ProductInfoResolver productInfoResolver;
    @Autowired
    BdmRepository bdmRepository;
    @Autowired
    @CoreEntityManager
    private EntityManager entityManager;
    @Autowired
    private MassUpdateTaskMap ecommerceTaskMap;
    @Value("${app.sourceSystemId}")
    private int sourceSystemId;
    @Autowired
    private ProductECommerceViewService productECommerceViewService;

    public enum LogicAttributeCode {
        BRAND_LOGIC_ATTRIBUTE_ID(1672L),
        DISPLAY_NAME_LOGIC_ATTRIBUTE_ID(1664L),
        SIZE_LOGIC_ATTRIBUTE_ID(1667L);
        private Long value;

        LogicAttributeCode(Long value) {
            this.value = value;
        }

        public Long getValue() {
            return this.value;
        }

        public static ProductECommerceViewService.LogicAttributeCode getAttribute(Long attributeId) {
            for (ProductECommerceViewService.LogicAttributeCode attribute : ProductECommerceViewService.LogicAttributeCode.values()) {
                if (attribute.getValue().equals(attributeId)) {
                    return attribute;
                }
            }
            return null;
        }
    }
    /**
     * Used to get consistent size lists to query runners.
     */
    private LongPopulator longPopulator = new LongPopulator();
    private IntegerPopulator integerPopulator = new IntegerPopulator();

    /**
     * Used to get count of all open/active ecommerce tasks by user id.
     * @return count of open task.
     */
    Long getActiveTaskCountByUser(String userId) {
        return alertStagingRepository.countByUser(AlertStaging.AlertTypeCD.ECOM_TASK.getName(),
                AlertStaging.AlertStatusCD.ACTIVE.getName(), userId);
    }

    /**
     * Fetches all open/active ecommerce tasks based on the filter condition input arguments.
     *
     * @param includeCounts to fetch pagination or count info; true or false.
     * @param page          page number.
     * @param pageSize      number of records in a page.
     * @return list of AlertStanding with/without pagination info.
     */
    PageableResult<AlertStaging> getAllActiveTasks(boolean includeCounts, int page, int pageSize, String assignee) {
        PageableResult<AlertStaging> pageableResult = null;
        Pageable pageable = new PageRequest(page, pageSize);
        if (includeCounts) {
            Page<AlertStaging> result = alertStagingRepositoryWithCount.findAlertsByAlertTypeAndStatusAndAssignee(
                    AlertStaging.AlertTypeCD.ECOM_TASK.getName(), AlertStaging.AlertStatusCD.ACTIVE.getName(), assignee, pageable);
            pageableResult = new PageableResult<>(page, result.getTotalPages(), result.getTotalElements(), result.getContent());
        } else {
            List<AlertStaging> result = alertStagingRepository.findAlertsByAlertTypeAndStatusAndAssignee(
                    AlertStaging.AlertTypeCD.ECOM_TASK.getName(), AlertStaging.AlertStatusCD.ACTIVE.getName(), assignee, pageable);
            pageableResult = new PageableResult<>(pageable.getPageNumber(), result);
        }
        return pageableResult;
    }
    /**
     * Fetches all closed ecommerce tasks based on the filter condition input arguments.
     *
     * @param includeCounts to fetch pagination or count info; true or false.
     * @param page          page number.
     * @param pageSize      number of records in a page.
     * @return list of AlertStanding with/without pagination info.
     */
    PageableResult<AlertStaging> getAllClosedTasks(boolean includeCounts, int page, int pageSize, String assignee) {
        PageableResult<AlertStaging> pageableResult = null;
        Pageable pageable = new PageRequest(page, pageSize);
        if (includeCounts) {
            Page<AlertStaging> result = alertStagingRepositoryWithCount.findAlertsByAlertTypeAndStatusAndAssignee(
                    AlertStaging.AlertTypeCD.ECOM_TASK.getName(), AlertStaging.AlertStatusCD.CLOSE.getName(), assignee, pageable);
            pageableResult = new PageableResult<>(page, result.getTotalPages(), result.getTotalElements(), result.getContent());
        } else {
            List<AlertStaging> result = alertStagingRepository.findAlertsByAlertTypeAndStatusAndAssignee(
                    AlertStaging.AlertTypeCD.ECOM_TASK.getName(), AlertStaging.AlertStatusCD.CLOSE.getName(), assignee, pageable);
            pageableResult = new PageableResult<>(pageable.getPageNumber(), result);
        }
        return pageableResult;
    }

    /**
     * Fetches all ecommerce tasks irrespective of task status based on the filter condition input arguments.
     *
     * @param includeCounts to fetch pagination or count info; true or false.
     * @param page          page number.
     * @param pageSize      number of records in a page.
     * @param assignee      task /alert assigned to user id.
     * @return list of AlertStanding with/without pagination info.
     */
    PageableResult<AlertStaging> getAllTasks(boolean includeCounts, int page, int pageSize, String assignee) {
        PageableResult<AlertStaging> pageableResult = null;
        Pageable pageable = new PageRequest(page, pageSize);
        if (includeCounts) {
            Page<AlertStaging> result = alertStagingRepositoryWithCount.findAllAlertsByAssignee(
                    AlertStaging.AlertTypeCD.ECOM_TASK.getName(), assignee, pageable);
            pageableResult = new PageableResult<>(page, result.getTotalPages(), result.getTotalElements(), result.getContent());
        } else {
            List<AlertStaging> result = alertStagingRepository.findAllAlertsByAssignee(
                    AlertStaging.AlertTypeCD.ECOM_TASK.getName(), assignee, pageable);
            pageableResult = new PageableResult<>(pageable.getPageNumber(), result);
        }
        return pageableResult;
    }

    /**
     * Fetches and returns high level task info by task id (alert id)
     * @param alertId task / alert id.
     * @return task info.
     */
    AlertStaging getTaskInfo(int alertId) {
        AlertStaging alertStaging = alertStagingRepository.findOne(alertId);
        if(alertStaging != null && alertStaging.getAlertDataTxt() != null) {
            alertStaging.setAlertDataTxt(alertStaging.getAlertDataTxt().trim());
            User user = userService.getUserById(alertStaging.getDelegatedByUserID().trim());
            if(user != null){
                alertStaging.setCreatedByFullName(user.getDisplayName());
            }
        }
        return alertStaging;
    }

    /**
     * Returns complete details of a task with list of products added under it filtered by  the supplied pagination
     * condition.
     *
     * @param trackingId tracking id of an alert/task.
     * @param includeCounts include pagination counts info. TRUE/FALSE.
     * @param page selected page number.
     * @param pageSize page size; number of records to be fetched per page.
     * @return list of candidate work request (products) tagged under the given tracking id or task id.
     */
    PageableResult<CandidateWorkRequest> getTaskDetail(long trackingId, boolean includeCounts, int page, int pageSize) {
        PageableResult<CandidateWorkRequest> pageableResult = null;
        Pageable pageable = new PageRequest(page, pageSize);
        if (includeCounts) {
            Page<CandidateWorkRequest> result = candidateWorkRequestRepositoryWithCounts
                    .findByTrackingIdAndStatusOrderByProductIdAsc(trackingId, CandidateStatus.PD_SETUP_STAT_CD_BATCH_UPLOAD, pageable);
            pageableResult = new PageableResult<>(page, result.getTotalPages(), result.getTotalElements(), result.getContent());
        } else {
            List<CandidateWorkRequest> result = candidateWorkRequestRepository.findByTrackingIdAndStatusOrderByProductIdAsc(
                    trackingId, CandidateStatus.PD_SETUP_STAT_CD_BATCH_UPLOAD, pageable);
            pageableResult = new PageableResult<>(pageable.getPageNumber(), result);
        }
        if (pageableResult.getData() != null) {
            pageableResult.getData().forEach(candidateWorkRequest -> resolveResults(candidateWorkRequest));
        }
        return pageableResult;
    }

    /**
     * Returns complete details of a task with list of products added under it filtered by  the supplied pagination
     * condition.
     *
     * @param trackingId tracking id of an alert/task.
     * @param includeCounts include pagination counts info. TRUE/FALSE.
     * @param assignee products assigned to user.
     * @param page selected page number.
     * @param pageSize page size; number of records to be fetched per page.
     * @return list of candidate work request (products) tagged under the given tracking id or task id.
     */
    PageableResult<CandidateWorkRequest> getTaskDetail(long trackingId, String assignee, boolean includeCounts, int page, int pageSize) {
        PageableResult<CandidateWorkRequest> pageableResult = null;
        Pageable pageable = new PageRequest(page, pageSize);
        if (includeCounts) {
            Page<CandidateWorkRequest> result = candidateWorkRequestRepositoryWithCounts
                    .findByTrackingIdAndLastUpdateUserIdAndStatus(trackingId, assignee.toUpperCase(),
                            CandidateStatus.PD_SETUP_STAT_CD_BATCH_UPLOAD, pageable);
            pageableResult = new PageableResult<>(page, result.getTotalPages(), result.getTotalElements(), result.getContent());
        } else {
            List<CandidateWorkRequest> result = candidateWorkRequestRepository.findByTrackingIdAndLastUpdateUserIdAndStatus(
                    trackingId, assignee.toUpperCase(), CandidateStatus.PD_SETUP_STAT_CD_BATCH_UPLOAD, pageable);
            pageableResult = new PageableResult<>(pageable.getPageNumber(), result);
        }
        if (pageableResult.getData() != null) {
            pageableResult.getData().forEach(candidateWorkRequest -> resolveResults(candidateWorkRequest));
        }
        return pageableResult;
    }

    /**
     * Returns complete details of a task with list of products added under it filtered by  the supplied pagination
     * condition.
     *
     * @param trackingId    tracking id of an alert/task.
     * @param includeCounts include pagination counts info. TRUE/FALSE.
     * @param assignee      products assigned to user.
     * @param showOnSite    show on site Y/N.
     * @param salesChannels list of sales channels filter.
     * @param page          selected page number.
     * @param pageSize      page size; number of records to be fetched per page.
     * @return list of candidate work request (products) tagged under the given tracking id or task id.
     */
    PageableResult<CandidateWorkRequest> getTaskDetail(long trackingId, String assignee, String showOnSite,
                                                       String salesChannels, boolean includeCounts, int page, int pageSize) {
        PageableResult<CandidateWorkRequest> pageableResult = handleFetchProducts(trackingId, assignee, showOnSite, salesChannels,
                includeCounts, page, pageSize);
        if (pageableResult.getData() != null) {
            pageableResult.getData().forEach(candidateWorkRequest -> {
                resolveResults(candidateWorkRequest);
                //Correct data for brand, displayname, size by priority source system
                //get brand
                ECommerceViewAttributePriorities brand = this.productECommerceViewService
                        .getECommerceDataSystemByLogicAttributeAndUpc(LogicAttributeCode
                        .BRAND_LOGIC_ATTRIBUTE_ID.value, candidateWorkRequest.getProductId(), candidateWorkRequest
                        .getProductMaster().getProductPrimaryScanCodeId());
                if(brand != null) {
                    candidateWorkRequest.setBrand(brand.getContent());
                }
                //get display name
                ECommerceViewAttributePriorities displayName = this.productECommerceViewService
                        .getECommerceDataSystemByLogicAttributeAndUpc(LogicAttributeCode
                            .DISPLAY_NAME_LOGIC_ATTRIBUTE_ID.value, candidateWorkRequest.getProductId(), candidateWorkRequest
                        .getProductMaster().getProductPrimaryScanCodeId());
                if(displayName != null) {
                    candidateWorkRequest.setDisplayName(displayName.getContent());
                }
                //get size
                ECommerceViewAttributePriorities size = this.productECommerceViewService
                        .getECommerceDataSystemByLogicAttributeAndUpc(LogicAttributeCode
                            .SIZE_LOGIC_ATTRIBUTE_ID.value, candidateWorkRequest.getProductId(), candidateWorkRequest
                        .getProductMaster().getProductPrimaryScanCodeId());
                if(size != null) {
                    candidateWorkRequest.setSize(size.getContent());
                }
            });
        }
        return pageableResult;
    }

    /**
     * Returns complete details of a task with list of products id added under it filtered by  the supplied pagination
     * condition.
     *
     * @param trackingId    tracking id of an alert/task.
     * @param includeCounts include pagination counts info. TRUE/FALSE.
     * @param assignee      products assigned to user.
     * @param showOnSite    show on site Y/N.
     * @param salesChannels list of sales channels filter.
     * @param page          selected page number.
     * @param pageSize      page size; number of records to be fetched per page.
     * @return list of candidate work request (products id and not detail product) tagged under the given tracking id or task id.
     */
    PageableResult<CandidateWorkRequest> getTaskProductsInfo(long trackingId, String assignee, String showOnSite,
                                                             String salesChannels, boolean includeCounts, int page, int pageSize) {
        PageableResult<CandidateWorkRequest> pageableResult = handleFetchProducts(trackingId, assignee, showOnSite, salesChannels,
                includeCounts, page, pageSize);
        if (pageableResult.getData() != null) {
        	pageableResult.getData().forEach(candidateWorkRequest -> {
        		if(candidateWorkRequest.getProductMaster() != null){
        			candidateWorkRequest.getProductMaster().getProdId();
        		}
        	});
        }
        return pageableResult;
    }

    /**
     * Returns only the task related candidates with list of products added under it filtered by  the supplied pagination
     * condition. This result does not include additional details of the products like brand, display name etc.
     *
     * @param trackingId    tracking id of an alert/task.
     * @param includeCounts include pagination counts info. TRUE/FALSE.
     * @param assignee      products assigned to user.
     * @param showOnSite    show on site Y/N.
     * @param salesChannels list of sales channels filter.
     * @param page          selected page number.
     * @param pageSize      page size; number of records to be fetched per page.
     * @return list of candidate work request (products) tagged under the given tracking id or task id.
     */
    PageableResult<CandidateWorkRequest> getTaskProducts(long trackingId, String assignee, String showOnSite,
                                                         String salesChannels, boolean includeCounts, int page, int pageSize) {
        PageableResult<CandidateWorkRequest> pageableResult = handleFetchProducts(trackingId, assignee, showOnSite, salesChannels,
                includeCounts, page, pageSize);
        return pageableResult;
    }

    /**
     * Returns complete details of a task with list of products added under it by assignee.
     *
     * @param outputStream  the ServletOutputStream.
     * @param trackingId    tracking id of an alert/task.
     * @param assignee      the assignee
     * @param showOnSite    show on site Y/N.
     * @param salesChannels list of sales channels filter.
     * @return list of candidate work request (products) tagged under the given tracking id or task id and assignee.
     */
    void streamAllTaskDetails(ServletOutputStream outputStream, long trackingId, String assignee, String showOnSite, String salesChannels) {

        // Export the header to the file
        try {
            outputStream.println(TASK_DETAILS_EXPORT_HEADER);
        } catch (IOException e) {
            EcommerceTaskService.logger.error(e.getMessage());
            throw new StreamingExportException(e.getMessage(), e.getCause());
        }

        PageableResult<CandidateWorkRequest> candidateListPage1 = this.getTaskDetail(trackingId, assignee, showOnSite, salesChannels, true, 0, 100);

        int numberOfPages = candidateListPage1.getPageCount();

        this.streamTaskDetailsListPage(outputStream, candidateListPage1);

        if(numberOfPages > 1) {
            for (int currentPage = 1; currentPage < numberOfPages; currentPage++) {
                this.streamTaskDetailsListPage(outputStream, this.getTaskDetail(trackingId, assignee, showOnSite, salesChannels,false, currentPage, 100));
            }
        }
    }

    /**
     * Stream the task details to a CSV output stream
     * @param outputStream the output stream
     * @param candidateListPage the candidate to output
     */
    private void streamTaskDetailsListPage(ServletOutputStream outputStream, PageableResult<CandidateWorkRequest> candidateListPage) {
        StringBuilder csv = new StringBuilder();

        try {
            // UPC, Item Code, Channel, Product ID, Product Description, Size Text, Sub Commodity, BDM, Sub Department, Last Modified, Last Modified By";
            candidateListPage.getData().forEach(candidate -> {
                if (candidate.getProductMaster() != null && candidate.getProductMaster().getProdItems() != null && candidate.getProductMaster().getProdItems().size() > 0) {
                    candidate.getProductMaster().getProdItems().forEach(item -> {
                        if (item.getItemMaster() != null && item.getItemMaster().getPrimaryUpc() != null && item.getItemMaster().getPrimaryUpc().getAssociateUpcs() != null && item.getItemMaster().getPrimaryUpc().getAssociateUpcs().size() > 0) {
                            item.getItemMaster().getPrimaryUpc().getAssociateUpcs().forEach(upc -> {
                                StringBuilder csvLine = new StringBuilder();
                                csvLine.append(String.format(TEXT_EXPORT_FORMAT, upc.getUpc())); // upc
                                csvLine.append(String.format(TEXT_EXPORT_FORMAT, item.getKey().getItemCode())); // item code
                                csvLine.append(String.format(TEXT_EXPORT_FORMAT, item.getKey().getItemType().equals(ItemMasterKey.WAREHOUSE) ? "WHS" : "DSD")); // channel
                                csvLine.append(String.format(TEXT_EXPORT_FORMAT, candidate.getProductMaster().getProdId())); // product ID
                                csvLine.append(String.format(TEXT_EXPORT_FORMAT, CSVUtils.formatCsvData(candidate.getProductMaster().getDescription()))); // product description
                                csvLine.append(String.format(TEXT_EXPORT_FORMAT, CSVUtils.formatCsvData(candidate.getProductMaster().getProductSizeText()))); // size text
                                if (candidate.getProductMaster().getSubCommodity() != null) {
                                    csvLine.append(String.format(TEXT_EXPORT_FORMAT, CSVUtils.formatCsvData(candidate.getProductMaster().getSubCommodity().getDisplayName()))); // sub commodity
                                } else {
                                    csvLine.append(String.format(TEXT_EXPORT_FORMAT, "Unknown"));
                                }
                                csvLine.append(String.format(TEXT_EXPORT_FORMAT, CSVUtils.formatCsvData(bdmRepository.findOne(candidate.getProductMaster().getBdmCode()).getDisplayName()))); // BDM
                                csvLine.append(String.format(TEXT_EXPORT_FORMAT, CSVUtils.formatCsvData(candidate.getProductMaster().getSubDepartment().getDisplayName()))); // sub department
                                csvLine.append(String.format(TEXT_EXPORT_FORMAT, candidate.getProductMaster().getLastUpdateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))); // last modified
                                csvLine.append(String.format(TEXT_EXPORT_FORMAT_LAST, candidate.getProductMaster().getLastUpdateUserId().trim())); // last modified by
                                csvLine.append(String.format(NEWLINE_TEXT_EXPORT_FORMAT));
                                csv.append(csvLine);
                            });
                        }
                    });
                }
            });


            outputStream.println(csv.toString());

        } catch (IOException e) {
            EcommerceTaskService.logger.error(e.getMessage());
            throw new StreamingExportException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Fetches list of notes or comments tagged under a give work request id. As normally not more than 10 comments are
     * found, pagination has be skipped purposely.
     * @param workRequestId work reques id (of a selected product)
     * @return list of comments or notes.
     */
    List<CandidateComments> getProductComments(Long workRequestId) {
        return this.candidateCommentsRepository.findByKeyWorkRequestIdOrderByTimeDesc(workRequestId);
    }

    /**
     * Used to add/create a new product notes into the comments table.
     * @param comment comment to be saved.
     * @return created comment record.
     */
    CandidateComments addProductNotes(CandidateComments comment) {
        int nextSequenceNumber = getNextSequenceNumber(comment.getKey().getWorkRequestId());
        comment.getKey().setSequenceNumber(nextSequenceNumber);
        comment.setTime(LocalDateTime.now());
        return this.candidateCommentsRepository.save(comment);
    }

    /**
     * Update a product notes into the comments table.
     * @param comment comment to be updated.
     * @return created comment record.
     */
    CandidateComments updateProductNotes(CandidateComments comment) {
        CandidateComments newCandidateComments = candidateCommentsRepository.findOne(comment.getKey());
        newCandidateComments.setComments(comment.getComments());
        newCandidateComments.setVendorVisibility(comment.getVendorVisibility());
        newCandidateComments.setTime(LocalDateTime.now());
        return this.candidateCommentsRepository.save(newCandidateComments);
    }

    /**
     * Delete a product notes into the comments table.
     * @param comment comment to be deleted.
     * @return created comment record.
     */
    void deleteProductNotes(CandidateComments comment) {
        this.candidateCommentsRepository.delete(comment);
    }

    /**
     * Fetch max sequence number of a work request's comments.
     * @param workRequestId work request.
     * @return next sequence value to be used for adding new comment.
     */
    private int getNextSequenceNumber(Long workRequestId) {
        Integer maxSequenceNumber = this.candidateCommentsRepository.getMaxSequenceNumber(workRequestId);
        return (maxSequenceNumber != null) ? ++maxSequenceNumber : 1;
    }

    /**
     * Used to create a new ecommerce task for the supplied task name. It involves 2 parts. First creates a transaction
     * record and then secondly creates an Alert(task) in the alert table using the created tracking id as the alert Key.
     * @param taskName name of task to be created.
     * @param userId task creator id.
     * @return alert or task id of the created task.
     */
    BigInteger createTask(String taskName, String userId) {
        TransactionTracker transactionTracker = createNewTaskTransactionTracker(taskName, userId);
        this.transactionTrackingRepository.save(transactionTracker);
        BigInteger taskId = null;
        if(transactionTracker.getTrackingId() != null && transactionTracker.getTrackingId() > 0) {
            taskId  = this.alertServiceClient.insertAlertForEcommerceTask(transactionTracker.getTrackingId(), taskName,
                    userId);
        }
        return taskId;
    }

    /**
     * Delete a Task in alert staging
     * @param alertStaging alert or task info.
     * @throws CheckedSoapException throws exception in case of any issues in executing this request.
     */
    void deleteTask(AlertStaging alertStaging) throws CheckedSoapException {
        alertStaging.setAlertStatusCD(AlertStaging.AlertStatusCD.CLOSE.getName());
        alertStaging.setAlertHideSw(AlertStaging.ALERT_HIDDEN_SWITCH_YES);
        this.taskManagementServiceClient.updateTask(alertStaging);
    }
    /**
     * Handles adding list of products to the task.
     * @param productSearchCriteria search criteria for products to be added to the task.
     * @return success/failure of batch submit request.
     */
    String addProducts(ProductSearchCriteria productSearchCriteria, String userId) {
        return submitAddProductsBatch(productSearchCriteria, userId);
    }

    /**
     * Handles delete selected list of products from a task.
     * @param trackingId tracking or transaction id of the task.
     * @param productIds list of products referenced work requests to be deleted.
     * @return delete products request status.
     */
    void deleteProducts(Long trackingId, List<Long> productIds, String userId) {
        this.longPopulator.populate(productIds, DEFAULT_PRODUCT_ID_COUNT);
        List<CandidateWorkRequest> candidateWorkRequests =
                this.candidateWorkRequestRepository.findByTrackingIdAndStatusAndProductIdIn(trackingId,
                        CandidateStatus.PD_SETUP_STAT_CD_BATCH_UPLOAD, productIds);
        this.candidateWorkRequestRepository.delete(candidateWorkRequests);
    }

    /**
     * Handles delete of all products from a task in batch mode.
     *
     * @param trackingId         tracking id.
     * @param assignee           the assignee
     * @param showOnSite         show on site Y/N.
     * @param salesChannels      list of sales channels filter.
     * @param excludedProductIds products to be excluded while removing all products in a batch mode.
     * @return status of batch submit success/failure.
     */
    String deleteAllProducts(Long trackingId, String assignee, String showOnSite, String salesChannels, List<Long> excludedProductIds, String userId) {
        ProductSearchCriteria productSearchCriteria = new ProductSearchCriteria();
        productSearchCriteria.setTrackingId(trackingId);
        productSearchCriteria.setExcludedProducts(excludedProductIds);
        return submitRemoveProductsBatch(productSearchCriteria, assignee, showOnSite, salesChannels, userId);
    }

    /**
     * Used to get a list of all users/assignee of the products listed under a task.
     * @param trackingId tracking id.
     * @return list of all users of a task.
     */
    List<User> getAssignedToUsers(Long trackingId) {
        List<String> assignedToUserIds = this.candidateWorkRequestRepository.findByTrackingIdAndStatus(
                trackingId, CandidateStatus.PD_SETUP_STAT_CD_BATCH_UPLOAD);
        List<User> users = this.userService.getUserById(assignedToUserIds);
        Collections.sort(users, Comparator.comparing(User::getFullName));
        return users;
    }

    /**
     * Used to fetch list of alert/task comments/notes of the given task/alert id.
     * @param taskId alert or task id.
     * @return list of user notes or comments.
     */
    List<AlertComment> getTaskNotes(Integer taskId) {
        return this.alertCommentService.findByAlertId(taskId);
    }

    /**
     * Used to add/create a new product notes into the comments table.
     * @param comment comment to be saved.
     * @return created comment record.
     */
    AlertComment addTaskNotes(AlertComment comment) {
        return this.alertCommentService.insertComment(comment);
    }

    /**
     * Update a product notes into the comments table.
     * @param comment comment to be updated.
     * @return created comment record.
     */
    AlertComment updateTaskNotes(AlertComment comment) {
       return this.alertCommentService.updateComment(comment);
    }

    /**
     * Delete a product notes into the comments table.
     * @param comment comment to be deleted.
     * @return created comment record.
     */
    void deleteTaskNotes(AlertComment comment) {
        this.alertCommentService.deleteComment(comment);
    }

    /**
     * Handling executing high level update for a give task. This involves updating the task description and status change
     * in the trx_tracking table and as well as the related record in the alert stagin table. Then also create or update
     * an alert user response for the same.
     *
     * @param alertStaging alert or task info.
     * @throws CheckedSoapException throws exception in case of any issues in executing this request.
     */
    void updateTask(AlertStaging alertStaging) throws CheckedSoapException {
        //1.a update transaction tracking table.
        this.updateTransactionDescription(alertStaging);
        //1.a update alert staging table.
        this.taskManagementServiceClient.updateTask(alertStaging);
        //2.a Get & Insert/Update alert user response
        AlertUserResponse alertUserResponse = this.alertUserResponseService.findOne(alertStaging.getAlertID(), alertStaging.getAlertStatusUserId());
        if(alertUserResponse != null) {//2.b just update if record already exists.
            this.updateAlertUserResponse(alertUserResponse, alertStaging);
        } else {//2.b insert if record doesn't exists.
            this.taskManagementServiceClient.insertAlertUserResp(alertStaging);
        }
    }

    /**
     * Fetches list of all CandidateWorkRequest. included building predicate and pagination
     * handling.
     *
     * @param trackingId    tracking id.
     * @param assignee      products assigned to user.
     * @param showOnSite    show on site Y/N.
     * @param salesChannels list of sales channels filter.
     * @param includeCounts identifies first fetch where total count of the records is required for pagination.
     * @param page          page requested.
     * @param pageSize      number of records per page.
     * @return PageableResult<AlertStaging>
     */
    private PageableResult<CandidateWorkRequest> handleFetchProducts(Long trackingId, String assignee, String showOnSite,
                                                                     String salesChannels, boolean includeCounts, int page, int pageSize) {

        // Get the objects needed to build the query.
        CriteriaBuilder criteriaBuilder =  this.entityManager.getCriteriaBuilder();
        // Builds the criteria for the main query
        CriteriaQuery<CandidateWorkRequest> queryBuilder = criteriaBuilder.createQuery(CandidateWorkRequest.class);
        // Select from product master.
        Root<CandidateWorkRequest> root = queryBuilder.from(CandidateWorkRequest.class);

        // Build the where clause
        Predicate predicate = this.predicateBuilder.buildPredicate(
                root, queryBuilder, criteriaBuilder, trackingId, assignee, showOnSite, salesChannels);

        queryBuilder.where(predicate);

        /* Add the sort : Order by can slow down this query as it has search by LIKE. Since user does not have any
        specific need about ordering the content displayed on the screen, Order By/Sort has been skipped.*/
        queryBuilder.orderBy(criteriaBuilder.asc(root.get(CandidateWorkRequest_.productId)));

        // Get the query
        TypedQuery<CandidateWorkRequest> tQuery = this.entityManager.createQuery(queryBuilder);

        // Sets the first row to grab and the maximum number to grab for pagination.
        tQuery.setFirstResult(page * pageSize).setMaxResults(pageSize);

        // Execute the query.
        List<CandidateWorkRequest> results = tQuery.getResultList();

        // If the user requested counts, build and execute that query.
        if (includeCounts) {
        	long count;
        	int pageCount;

        	// It's a new query
        	CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        	// Same from and where, just wrapping a count around it.
        	countQuery.select(criteriaBuilder.count(countQuery.from(CandidateWorkRequest.class)));
        	countQuery.where(predicate);

        	// Run the query
        	TypedQuery<Long> countTQuery = this.entityManager.createQuery(countQuery);

        	count = countTQuery.getSingleResult();

        	// Calculate how many pages of data there are.
        	pageCount = (int) count / pageSize;
        	pageCount += (int) count % pageSize == 0 ? 0 : 1;
        	return new PageableResult<>(page, pageCount, count, results);
        } else {
            return new PageableResult<>(page, results);
        }
    }

    /**
     * Handles preparing and updating a description of the task in the transaction tracking table.
     * @param alertStaging
     */
    private void updateTransactionDescription(AlertStaging alertStaging) {
        TransactionTracker transactionTracker = this.transactionTrackingRepository.getOne(Long.valueOf(alertStaging.getAlertKey().trim()));
        transactionTracker.setFileDes(alertStaging.getAlertDataTxt().trim());
        this.transactionTrackingRepository.save(transactionTracker);
    }

    /**
     * Handles updating user response for the given alert. Updates the user response with default status EMPTY, in case
     * the status of the task is Active. And CLOSE if the alert status is CLOSD.
     * @param alertUserResponse
     * @param alertStaging
     * @throws CheckedSoapException
     */
    private void updateAlertUserResponse(AlertUserResponse alertUserResponse, final AlertStaging alertStaging) throws CheckedSoapException {
        if(alertStaging.getAlertStatusCD().equals(AlertStaging.AlertStatusCD.ACTIVE)) {
            alertUserResponse.setAlertResolutionCode(AlertUserResponse.RESOLUTION_CODE_DEFAULT);
        } else if (alertStaging.getAlertStatusCD().equals(AlertStaging.AlertStatusCD.CLOSE)) {
            alertUserResponse.setAlertResolutionCode(AlertUserResponse.RESOLUTION_CODE_CLOSE);
        }
        this.taskManagementServiceClient.updateAlertUserResp(alertUserResponse);
    }

    /**
     * Helper function to create a new transaction tracker record.
     * @param taskName name of task.
     * @param userId id of the task creator.
     * @return a transaction tracker record.
     */
    private TransactionTracker createNewTaskTransactionTracker(String taskName, String userId) {
        TransactionTracker transactionTracker = new TransactionTracker();
        transactionTracker.setFileDes(taskName);
        transactionTracker.setUserId(userId);
        transactionTracker.setSource(String.valueOf(SourceSystem.SOURCE_SYSTEM_PRODUCT_MAINTENANCE));
        transactionTracker.setUserRole(TransactionTracker.ROLE_CODE_USER);
        transactionTracker.setFileNm(TransactionTracker.FILE_NAME_ATTRIBUTE_TASKS);
        transactionTracker.setTrxStatCd(TransactionTracker.STAT_CODE_NOT_COMPLETE);
        transactionTracker.setCreateDate(LocalDateTime.now());
        return transactionTracker;
    }

    /**
     * Returns a candidate work request for a given product id to be added under ecommerce task.
     * @param productId product id.
     * @param trackingId tracking id of the task.
     * @param userId user who adds the product to the task..
     * @param sourceSystemId source system id.
     * @return candidate work request.
     */
    protected CandidateWorkRequest createAddProductsCandidate(Long productId, Long trackingId, String userId,
                                                            int sourceSystemId) {
        CandidateWorkRequest candidateWorkRequest = new CandidateWorkRequest();
        candidateWorkRequest.setIntent(CandidateWorkRequest.INTNT_ID_MODIFY_EXISTING_PRODUCT);
        candidateWorkRequest.setCreateDate(LocalDateTime.now());
        candidateWorkRequest.setUserId(userId.toUpperCase());
        candidateWorkRequest.setLastUpdateDate(LocalDateTime.now());
        candidateWorkRequest.setStatus(CandidateWorkRequest.StatusCode.IN_PROGRESS.getName());
        candidateWorkRequest.setStatusChangeReason(CandidateWorkRequest.STAT_CHG_RSN_ID_WRKG);
        candidateWorkRequest.setSourceSystem(sourceSystemId);
        candidateWorkRequest.setReadyToActivate(Boolean.TRUE);
        candidateWorkRequest.setLastUpdateUserId(userId.toUpperCase());
        candidateWorkRequest.setTrackingId(trackingId);
        candidateWorkRequest.setProductId(productId);
        candidateWorkRequest.setDelegatedByUserId(userId);
        candidateWorkRequest.setDelegatedTime(LocalDateTime.now());
        return candidateWorkRequest;
    }

    /**
     * Helper functon to resolve the details of a task starting from work request to it's associated product details.
     * @param candidateWorkRequest candidate work request.
     */
    private void resolveResults(CandidateWorkRequest candidateWorkRequest) {
       // this.productInfoResolver.fetch(candidateWorkRequest.getProductMaster());
        candidateWorkRequest.getProductMaster().getProductBrand().getDisplayName();
        List<ProductFullfilmentChanel> productFullfilmentChanels = new ArrayList<>();
        for (ProductFullfilmentChanel productFullfilmentChanel: candidateWorkRequest.getProductMaster().getProductFullfilmentChanels()){
            if(DateUtils.isGreaterThanOrEqualToday(productFullfilmentChanel.getExpirationDate().toString(),ProductFullfilmentChanel.DATE_FORMAT)){
                productFullfilmentChanels.add(productFullfilmentChanel);
            }
        }
        candidateWorkRequest.getProductMaster().setProductFullfilmentChanels(productFullfilmentChanels);
        candidateWorkRequest.getProductMaster().getProductOnlines().isEmpty();
        candidateWorkRequest.getProductMaster().getMasterDataExtensionAttributes().isEmpty();
        candidateWorkRequest.getProductMaster().setPrimaryCustomerHierarchy(
                this.customerHierarchyService.getPrimaryCustomerHierarchy(candidateWorkRequest.getProductId(), "CUST"));
    }


    /**
     * Prepares and submits the add products request in batch mode.
     * @param productSearchCriteria object containing product search/filter conditions and product to be excluded (if any).
     * @param userId user who submitted the request.
     * @return status of batch submit. success / failure.
     */
    private String submitAddProductsBatch(ProductSearchCriteria productSearchCriteria, String userId) {
        JobParametersBuilder parametersBuilder = new JobParametersBuilder();
        parametersBuilder.addLong(JOB_PARAMETER_TRANSACTION_ID, productSearchCriteria.getTrackingId());
        parametersBuilder.addString(JOB_PARAMETER_USER_ID, userId);
        parametersBuilder.addLong(JOB_PARAMETER_IDENTIFIER, System.currentTimeMillis());
        this.massUpdateProductMap.add(productSearchCriteria.getTrackingId(), productSearchCriteria);
        Job job = this.getMassUpdateJob(MASS_UPDATE_ADD_PRODUCTS_JOB_NAME);
        try {
            // Kick off the job. This is asynchronous, so we won't have the status of the job after the call.
            this.jobLauncher.run(job, parametersBuilder.toJobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                JobParametersInvalidException e) {
            logger.error(String.format(ERROR_BATCH_SUBMIT_FAILURE, e.getMessage()));
            return MESSAGE_BATCH_SUBMIT_FAILURE;
        }
        return MESSAGE_BATCH_SUBMIT_SUCCESS;
    }

    /**
     * Prepares and submits the add products request in batch mode.
     *
     * @param productSearchCriteria object containing product search/filter conditions and product to be excluded (if any).
     * @param assignee              alert assigned to user.
     * @param showOnSite            show on site Y/N.
     * @param salesChannels         list of sales channels filter.
     * @param userId                user who submitted the request.
     * @return status of batch submit. success / failure.
     */
    private String submitRemoveProductsBatch(ProductSearchCriteria productSearchCriteria, String assignee, String showOnSite, String salesChannels, String userId) {
        JobParametersBuilder parametersBuilder = new JobParametersBuilder();
        parametersBuilder.addLong(JOB_PARAMETER_TRANSACTION_ID, productSearchCriteria.getTrackingId());
        parametersBuilder.addString(JOB_PARAMETER_ASSIGNEE_ID, assignee);
        parametersBuilder.addString(JOB_PARAMETER_SHOW_ON_SITE, showOnSite);
        parametersBuilder.addString(JOB_PARAMETER_SALES_CHANNELS, salesChannels);
        parametersBuilder.addString(JOB_PARAMETER_USER_ID, userId);
        parametersBuilder.addLong(JOB_PARAMETER_IDENTIFIER, System.currentTimeMillis());
        this.massUpdateProductMap.add(productSearchCriteria.getTrackingId(), productSearchCriteria);
        Job job = this.getMassUpdateJob(MASS_UPDATE_REMOVE_PRODUCTS_JOB_NAME);
        try {
            // Kick off the job. This is asynchronous, so we won't have the status of the job after the call.
            this.jobLauncher.run(job, parametersBuilder.toJobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                JobParametersInvalidException e) {
            logger.error(String.format(ERROR_BATCH_SUBMIT_FAILURE, e.getMessage()));
            return MESSAGE_BATCH_SUBMIT_FAILURE;
        }
        return MESSAGE_BATCH_SUBMIT_SUCCESS;
    }

    /**
     * Returns a reference to the mass update job.
     *
     * @return A reference to the mass update job.
     */
    private Job getMassUpdateJob(String jobName) {
        try {
            return this.jobLocator.getJob(jobName);
        } catch (NoSuchJobException e) {
            JobNotDefinedException je = new JobNotDefinedException(MASS_UPDATE_ADD_PRODUCTS_JOB_NAME);
            throw je;
        }
    }
    /**
     * Update mass fill.
     *
     * @param userId the current id.
     * @param massUpdateTaskRequest the massUpdateTaskRequest.
     * @return the message.
     */
    public Long updateMassFillToProduct(String userId, MassUpdateTaskRequest massUpdateTaskRequest){
        Long trackingId = getTransaction(userId, massUpdateTaskRequest.getDescription(),massUpdateTaskRequest).getTrackingId();
        JobParametersBuilder parametersBuilder = new JobParametersBuilder();
        parametersBuilder.addLong(JOB_PARAMETER_TRANSACTION_ID, trackingId);
        parametersBuilder.addString(JOB_PARAMETER_USER_ID, userId);
        parametersBuilder.addString(JOB_PARAMETER_ASSIGNEE_ID, massUpdateTaskRequest.getAssigneeId());
        this.ecommerceTaskMap.add(trackingId, massUpdateTaskRequest);
        Job job = this.getMassUpdateJob(MASS_UPDATE_ECOMMERCE_TASK_JOB_NAME);
        try {
            // Kick off the job. This is asynchronous, so we won't have the status of the job after the call.
            this.jobLauncher.run(job, parametersBuilder.toJobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                JobParametersInvalidException e) {
            logger.error(String.format(ERROR_MASS_UPDATE_SUBMIT_FAILURE, MASS_UPDATE_ECOMMERCE_TASK_JOB_NAME, e.getMessage()));
            throw new JobExecutionException(String.format(ERROR_MASS_UPDATE_SUBMIT_FAILURE,
                    MASS_UPDATE_ECOMMERCE_TASK_JOB_NAME, e.getMessage()), e.getCause());
        }
        return trackingId;
    }
    /**
     * Saves an entry into the tracking table. This will be used to group all of the mass updates together as one unit.
     *
     * @return The object saved to the tracking table.
     */
    @CoreTransactional
    TransactionTracker getTransaction(String userId, String description,MassUpdateTaskRequest massUpdateTaskRequest) {
        TransactionTracker t = new TransactionTracker();
        t.setUserId(userId);
        t.setCreateDate(LocalDateTime.now());
        t.setSource(Integer.toString(this.sourceSystemId));
        t.setUserRole(USER_ROLE);
        t.setFileDes(description);
       if (massUpdateTaskRequest.getActionType()==MassUpdateTaskRequest.ACTION_TYPE_PUBLISH_PRODUCT){
            t.setFileNm(TransactionTracker.FileNameCode.PUBLISH_PRODUCT.getName());
            t.setTrxCntlNbr(Long.valueOf(massUpdateTaskRequest.getAlertId()));
        }
        else {
           t.setFileNm(TransactionTracker.FileNameCode.PRODUCT_ATTRIBUTES.getName());
       }
        t.setTrxStatCd(TransactionTracker.STAT_CODE_NOT_COMPLETE);
        return this.transactionTrackingRepository.save(t);
    }
    /**
     * Used to get count of all eCommerce task updates.
     * @return count.
     */
    Long getEcommerceTaskUpdatesCount(Long trackingId, String assignee) {
        Long totalNoOfRecords = null;
        if(assignee != null && StringUtils.isNotBlank(assignee)) {
            totalNoOfRecords = candidateWorkRequestRepository
                    .countByTrackingIdAndLastUpdateUserIdAndStatus(trackingId, assignee.toUpperCase(),
                            CandidateStatus.PD_SETUP_STAT_CD_BATCH_UPLOAD);
        }else{
            totalNoOfRecords = candidateWorkRequestRepository
                    .countByTrackingIdAndStatus(trackingId,
                            CandidateStatus.PD_SETUP_STAT_CD_BATCH_UPLOAD);
        }
        return totalNoOfRecords;
    }
    /**
     * Assign to bdm.
     * @param userId the user id.
     * @param massUpdateTaskRequest the massUpdateTaskRequest
     */
    public Long assignToBdm(String userId, MassUpdateTaskRequest massUpdateTaskRequest){
        JobParametersBuilder parametersBuilder = new JobParametersBuilder();
        parametersBuilder.addLong(JOB_PARAMETER_TRANSACTION_ID, massUpdateTaskRequest.getTrackingId());
        parametersBuilder.addString(JOB_PARAMETER_USER_ID, userId);
        parametersBuilder.addLong(JOB_PARAMETER_ALERT_ID, Long.valueOf(massUpdateTaskRequest.getAlertId()));
        parametersBuilder.addLong(JOB_PARAMETER_IDENTIFIER, System.currentTimeMillis());
        this.ecommerceTaskMap.add(massUpdateTaskRequest.getTrackingId(), massUpdateTaskRequest);
        Job job = this.getMassUpdateJob(ASSIGN_TO_BDM_ECOMMERCE_TASK_JOB_NAME);
        try {
            // Kick off the job. This is asynchronous, so we won't have the status of the job after the call.
            this.jobLauncher.run(job, parametersBuilder.toJobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                JobParametersInvalidException e) {
            logger.error(String.format(ERROR_MASS_UPDATE_SUBMIT_FAILURE, ASSIGN_TO_BDM_ECOMMERCE_TASK_JOB_NAME, e.getMessage()));
            throw new JobExecutionException(String.format(ERROR_MASS_UPDATE_SUBMIT_FAILURE,
                    ASSIGN_TO_BDM_ECOMMERCE_TASK_JOB_NAME, e.getMessage()), e.getCause());
        }
        return  massUpdateTaskRequest.getTrackingId();
    }
    /**
     * Assign to ebm.
     * @param userId the user id.
     * @param massUpdateTaskRequest the massUpdateTaskRequest
     */
    public Long assignToEbm(String userId, MassUpdateTaskRequest massUpdateTaskRequest){
        JobParametersBuilder parametersBuilder = new JobParametersBuilder();
        parametersBuilder.addLong(JOB_PARAMETER_TRANSACTION_ID, massUpdateTaskRequest.getTrackingId());
        parametersBuilder.addString(JOB_PARAMETER_USER_ID, userId);
        parametersBuilder.addLong(JOB_PARAMETER_ALERT_ID, Long.valueOf(massUpdateTaskRequest.getAlertId()));
        parametersBuilder.addLong(JOB_PARAMETER_IDENTIFIER, System.currentTimeMillis());
        this.ecommerceTaskMap.add(massUpdateTaskRequest.getTrackingId(), massUpdateTaskRequest);
        Job job = this.getMassUpdateJob(ASSIGN_TO_EBM_ECOMMERCE_TASK_JOB_NAME);
        try {
            // Kick off the job. This is asynchronous, so we won't have the status of the job after the call.
            this.jobLauncher.run(job, parametersBuilder.toJobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                JobParametersInvalidException e) {
            logger.error(String.format(ERROR_MASS_UPDATE_SUBMIT_FAILURE, ASSIGN_TO_EBM_ECOMMERCE_TASK_JOB_NAME, e.getMessage()));
            throw new JobExecutionException(String.format(ERROR_MASS_UPDATE_SUBMIT_FAILURE,
                    ASSIGN_TO_EBM_ECOMMERCE_TASK_JOB_NAME, e.getMessage()), e.getCause());
        }
        return  massUpdateTaskRequest.getTrackingId();
    }

    /**
     * Submit request to run batch async
     *
     * @param massUpdateTaskRequest the mass update request.
     * @return tracking id.
     */
    public Long submit(MassUpdateTaskRequest massUpdateTaskRequest) {
        TransactionTracker transaction = this.getTransaction(massUpdateTaskRequest.getUserId(),massUpdateTaskRequest.getDescription(),massUpdateTaskRequest);
        JobParametersBuilder parametersBuilder = new JobParametersBuilder();
        parametersBuilder.addLong(JOB_PARAMETER_TRANSACTION_ID, transaction.getTrackingId());
        parametersBuilder.addString(JOB_PARAMETER_USER_ID, massUpdateTaskRequest.getUserId());
        parametersBuilder.addString(JOB_PARAMETER_ACTION_TYPE,String.valueOf(massUpdateTaskRequest.getActionType()));
        this.ecommerceTaskMap.add(transaction.getTrackingId(), massUpdateTaskRequest);
        Job job = this.getMassUpdateJob(MASS_UPDATE_ECOMMERCE_TASK_JOB_NAME);
        try {
            // Kick off the job. This is asynchronous, so we won't have the status of the job after the call.
            this.jobLauncher.run(job, parametersBuilder.toJobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                JobParametersInvalidException e) {
            logger.error(String.format(ERROR_MASS_UPDATE_SUBMIT_FAILURE, ASSIGN_TO_EBM_ECOMMERCE_TASK_JOB_NAME, e.getMessage()));
            throw new JobExecutionException(String.format(ERROR_MASS_UPDATE_SUBMIT_FAILURE,
                    ASSIGN_TO_EBM_ECOMMERCE_TASK_JOB_NAME, e.getMessage()), e.getCause());
        }
        return  transaction.getTrackingId();
    }
}