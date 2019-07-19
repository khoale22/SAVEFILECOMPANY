/*
 * NutritionUpdatesTaskService
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;


import com.heb.pm.CoreEntityManager;
import com.heb.pm.Hits;
import com.heb.pm.HitsBase;
import com.heb.pm.productSearch.predicateBuilders.PredicateBuilderList;
import com.heb.pm.repository.*;
import com.heb.pm.CoreTransactional;
import com.heb.pm.entity.*;
import com.heb.pm.massUpdate.MassUpdateService;
import com.heb.pm.massUpdate.job.JobNotDefinedException;
import com.heb.pm.massUpdate.job.MassUpdateProductMap;
import com.heb.pm.product.ProductInfoResolver;
import com.heb.pm.productSearch.ProductSearchCriteria;
import com.heb.pm.ws.ApplicationAlertStagingServiceClient;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.jpa.PageableResult;
import com.heb.util.list.IntegerPopulator;
import com.heb.util.list.LongPopulator;
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

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This service is used to serve nutrition updates related queries.
 *
 * @author vn40486
 * @since 2.11.0
 */
@Service
public class NutritionUpdatesTaskService {

    private static final Logger logger = LoggerFactory.getLogger(NutritionUpdatesTaskService.class);

    @Autowired
    private JobLocator jobLocator;
    @Autowired
    private NutritionUpdatesTaskPredicateBuilder nutritionUpdatesTaskPredicateBuilder;

    @Autowired
    @Qualifier("asyncJobLauncher")
    private JobLauncher jobLauncher;

    @Value("${app.sourceSystemId}")
    private int sourceSystemId;

    private static final String USER_ROLE = "USER";
    private static final String TRANSACTION_ID_PARAMETER = "trackingId";

    public static final String TASK_DETAILS_EXPORT_HEADER = "UPC, Item Code, Channel, Product ID, Product Description, Size Text, Sub Commodity, BDM, Sub Department, Genesis Published Date, Dept, Last Modified, Last Modified By";
    private static final String TEXT_EXPORT_FORMAT = "\"%s\",";
    private static final String TEXT_EXPORT_FORMAT_LAST = "\"%s\"";
    private static final String NEWLINE_TEXT_EXPORT_FORMAT = "\n";
    public static final int PAGE_SIZE = 100;

    /**
     * The Product info repository without count.
     */
    @Autowired
    ProductInfoRepository productInfoRepository;
    @Autowired
    private CandidateWorkRequestRepository candidateWorkRequestRepository;
    @Autowired
    private TransactionTrackingRepository transactionTrackingRepository;
    @Autowired
    private ApplicationAlertStagingServiceClient applicationAlertStagingServiceClient;
    @Autowired
    private AlertStagingRepository alertStagingRepository;
    @Autowired
    private ProductInfoResolver productInfoResolver;
    @Autowired
    BdmRepository bdmRepository;
    @Autowired
    @CoreEntityManager
    private EntityManager entityManager;
    @Autowired
    private PredicateBuilderList predicateBuilderList;
    @Autowired
    private SearchCriteriaRepository searchCriteriaRepository;
    @Autowired
    private MassUpdateTaskMap massUpdateTaskMap;
    @Autowired
    private ItemMasterRepository itemMasterRepository;
    @Autowired
    private SellingUnitRepository sellingUnitRepository;

    private static final String NUTRITION_UPDATES_MASS_UPDATE_JOB_NAME = "I18X912";
    private static final String JOB_PARAMETER_REJECT_REASON = "rejectReason";
    private static final String JOB_PARAMETER_USER_ID = "userId";

    public static final String MESSAGE_BATCH_SUBMIT_SUCCESS = "SUCCESS";
    public static final String MESSAGE_BATCH_SUBMIT_FAILURE = "FAILURE";
    private static final String ERROR_NUTRITION_UPDATES_FAILURE = "Error with nutrition updates for alert: %s ";
    private static final String ERROR_BATCH_SUBMIT_FAILURE = "Error with nutrition updates mass update: %s ";


    // The default number of products to search for. This number
    // and fewer will have the maximum performance.
    private static final int DEFAULT_PRODUCT_ID_COUNT = 100;

    private static final String PRODUCT = "Product";
    private static final String PRODUCTS = "Products";
    private static final String UPC = "UPC";
    private static final String UPCS = "UPCs";
    private static final String ITEM_CODE = "Item Code";
    private static final String ITEM_CODES = "Item Codes";

    /**
     * Src system id 26 denoted "Pending Genesis product update approval"
     */
    private static int CANDIDATE_INTENT_NUTRITION_UPDATES = 26;

    /**
     * Src system id 26 denoted "Pending Genesis product update approval"
     */
    private static int CANDIDATE_SRC_NUTRITION_UPDATES = 26;

    /**
     * Used to get consistent size lists to query runners.
     */
    private LongPopulator longPopulator = new LongPopulator();
    private IntegerPopulator integerPopulator = new IntegerPopulator();


    /**
     * Used to get count of all active nutrition updates.
     *
     * @return count.
     */
    Long getActiveNutritionUpdatesCount() {
        return alertStagingRepository.countByAlertTypeCDAndAlertStatusCD(AlertStaging.AlertTypeCD.GENESIS_AP.getName(),
                AlertStaging.AlertStatusCD.ACTIVE.getName());
    }

    /**
     * Search active nutrition updates.
     *
     * @param searchCriteria holds the search criteria.
     * @param page           page requested.
     * @param pageSize       number of records per page.
     * @param firstSearch    identifies first fetch where total count of the records is required for pagination.
     * @return
     */
    @CoreTransactional
    public PageableResult<AlertStaging> searchActiveNutritionUpdates(ProductSearchCriteria searchCriteria,
                                                                     int page, int pageSize, boolean firstSearch) {
        this.validateSearchCriteria(searchCriteria);
        //1. Get nutrition updates by pagination.
        PageableResult<AlertStaging> pageableResult = handleFetchProducts(searchCriteria, firstSearch, page, pageSize);

        if (pageableResult.getData() != null && pageableResult.getData().iterator().hasNext()) {
            //2. Collect product ids from the Alerts.
            List<Long> productIds = new ArrayList<>();
            pageableResult.getData().forEach(alertStaging -> productIds.add(Long.valueOf(alertStaging.getAlertKey().trim())));
            //3. Send collected product-ids to master db to fetch their respective product info.
            this.longPopulator.populate(productIds, DEFAULT_PRODUCT_ID_COUNT);
            List<ProductMaster> productMasters = productInfoRepository.findAll(productIds);
            //4. Update Alerts with their respective ProductMaster.
            Map<Long, ProductMaster> productMasterMap = productMasters.stream().collect(
                    Collectors.toMap(productMaster -> productMaster.getProdId(), productMaster -> productMaster));
            pageableResult.getData().forEach(alertStaging -> alertStaging.setProductMaster(
                    productMasterMap.get(Long.valueOf(alertStaging.getAlertKey().trim()))));
            pageableResult.getData().forEach(alertStaging -> this.productInfoResolver.fetch(alertStaging.getProductMaster()));

        }

        return pageableResult;
    }

    /**
     * Fetches list of all products(alerts) tagged under a given task type. Includes building predicate and pagination
     * handling.
     *
     * @param searchCriteria holds the search criteria.
     * @param includeCounts  identifies first fetch where total count of the records is required for pagination.
     * @param page           page requested.
     * @param pageSize       number of records per page.
     * @return
     */
    private PageableResult<AlertStaging> handleFetchProducts(ProductSearchCriteria searchCriteria,
                                                             boolean includeCounts, int page, int pageSize) {
        // Get the objects needed to build the query.
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        // Builds the criteria for the main query
        CriteriaQuery<AlertStaging> queryBuilder = criteriaBuilder.createQuery(AlertStaging.class);
        // Select from product master.
        Root<AlertStaging> root = queryBuilder.from(AlertStaging.class);

        // Build the where clause
        Predicate predicate = this.nutritionUpdatesTaskPredicateBuilder.buildPredicate(
                root, queryBuilder, criteriaBuilder, searchCriteria);
        queryBuilder.where(predicate);

        /* Add the sort : Order by can slow down this query. Since user does not have any
        specific need about ordering the content displayed on the screen, Order By/Sort has been skipped.*/
        queryBuilder.orderBy(criteriaBuilder.asc(root.get(AlertStaging_.alertKey)));

        // Get the query
        TypedQuery<AlertStaging> tQuery = this.entityManager.createQuery(queryBuilder);

        // Sets the first row to grab and the maximum number to grab for pagination.
        tQuery.setFirstResult(page * pageSize).setMaxResults(pageSize);

        // Execute the query.
        List<AlertStaging> results = tQuery.getResultList();

        // If the user requested counts, build and execute that query.
        if (includeCounts) {
            long count;
            int pageCount;
            // It's a new query
            CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
            // Same from and where, just wrapping a count around it.
            countQuery.select(criteriaBuilder.count(countQuery.from(AlertStaging.class)));
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
     * Checks to make sure a user's search criteria is valid.
     *
     * @param searchCriteria The search criteria to check.
     */
    private void validateSearchCriteria(ProductSearchCriteria searchCriteria) {
        if (searchCriteria.getSessionId() == null) {
            throw new IllegalArgumentException("Session ID is required");
        }
    }

    /**
     * Loads the temp table with the user's search criteria.
     *
     * @param searchCriteria The user's search criteria.
     * @param firstSearch    Whether or not this is the first time the user has performed this search.
     */
    @CoreTransactional
    protected void loadTempTable(ProductSearchCriteria searchCriteria, boolean firstSearch) {
        // If this is the first search for a set of criteria, clear out any existing criteria from the temp table.
        // If there isn't a session ID for this request, then throw an error.
        if (firstSearch) {
            this.searchCriteriaRepository.deleteBySessionId(searchCriteria.getSessionId());
            // Flush the entity manager so that all the rows are actually removed.
            this.entityManager.flush();
        }

        // If this is a first search or there's no criteria for this session in the table, then loop through each
        // predicate builder and have it populate the temp table.
        if (firstSearch || this.searchCriteriaRepository.countBySessionId(searchCriteria.getSessionId()) <= 0) {
            this.predicateBuilderList.getPredicateBuilders().forEach((pb) -> pb.populateTempTable(searchCriteria));
        }

        // Flush the entity manager so that all the rows are in the temp table.
        this.entityManager.flush();
    }

    /**
     * For a simple search generates counts of matches and misses with a full list of what is missing.
     *
     * @param searchCriteria The user's search criteria
     * @return An object containing hit and miss counts and a list of misses.
     */
    @CoreTransactional
    public HitsBase<?> getSearchHits(ProductSearchCriteria searchCriteria) {

        this.validateSearchCriteria(searchCriteria);

        // Make sure that only a simple search was passed in.
        if (!searchCriteria.isSimpleSearch()) {
            throw new IllegalArgumentException("Only simple searches can be used to find hits");
        }

        this.loadTempTable(searchCriteria, true);

        // Depending on the type of search the user is doing, you need to use
        // a different query.

        if (searchCriteria.hasProductSearch()) {
            List<Long> productIds = this.productInfoRepository.findAllProductIdsForSearchNutritionUpdate(
                    AlertStaging.AlertTypeCD.GENESIS_AP.getName(),
                    AlertStaging.AlertStatusCD.ACTIVE.getName(),
                    searchCriteria.getSessionId());
            return Hits.from(searchCriteria.getProductIds(), productIds, PRODUCT, PRODUCTS);
        }

        if (searchCriteria.hasUpcSearch()) {
            List<Long> upcs = this.sellingUnitRepository.findAllUpcsForSearchNutritionUpdate(
                    AlertStaging.AlertTypeCD.GENESIS_AP.getName(),
                    AlertStaging.AlertStatusCD.ACTIVE.getName(),
                    searchCriteria.getSessionId());
            return Hits.from(searchCriteria.getUpcs(), upcs, UPC, UPCS);
        }

        if (searchCriteria.hasItemCodeSearch()) {
            List<Long> itemCodes = this.itemMasterRepository.findAllItemCodesForSearchNutritionUpdate(
                    AlertStaging.AlertTypeCD.GENESIS_AP.getName(),
                    AlertStaging.AlertStatusCD.ACTIVE.getName(),
                    searchCriteria.getSessionId());
            return Hits.from(searchCriteria.getItemCodes(), itemCodes, ITEM_CODE, ITEM_CODES);
        }

        return null;
    }

    /**
     * Used to reject set of alerts.
     *
     * @param alertIds     list of alerts to be rejected.
     * @param rejectReason reject reason.
     * @param userId       user ho submitted the reject request.
     */
    public void rejectUpdates(List<Integer> alertIds, String rejectReason, String userId) {
        integerPopulator.populate(alertIds, DEFAULT_PRODUCT_ID_COUNT);
        /* Prepare: Fetch all AlertStaging matching the selected Alert Ids. */
        List<AlertStaging> alertStagings = this.alertStagingRepository.findAll(alertIds);
        /* Prepare: Collect all product ids from the Alerts. */
        List<Long> alertProductIds = alertStagings.stream().map(AlertStaging::getAlertKey).map(String::trim)
                .map(Long::parseLong).collect(Collectors.toList());
        /* Prepare: Fetch all nutrition updates candidates associated to the products.*/
        List<CandidateWorkRequest> candidateWorkRequests = this.candidateWorkRequestRepository
                .findByIntentAndStatusAndSourceSystemAndProductIdIn(
                        CANDIDATE_INTENT_NUTRITION_UPDATES, CandidateStatus.PD_SETUP_STAT_CD_BATCH_UPLOAD,
                        CANDIDATE_SRC_NUTRITION_UPDATES, alertProductIds);
        /* Prepare: Set status of all related work request to status = REJECTED and add a status change reason record.*/
        candidateWorkRequests = NutritionUpdatesHelper
                .setCandidateAsRejectedWithComments(candidateWorkRequests, rejectReason, userId);
        /* Save:  this should update PS_WORK_RQST and insert a record into PS_CANDIDATE_STAT. */
        this.candidateWorkRequestRepository.save(candidateWorkRequests);
        /* Save: update ALERT records using webservice. */
        alertStagings.forEach(alertStaging -> {
            try {
                this.applicationAlertStagingServiceClient.updateAlert(alertStaging.getAlertID(),
                        AlertStaging.AlertStatusCD.CLOSE.getName());
            } catch (CheckedSoapException e) {
                logger.error(String.format(ERROR_NUTRITION_UPDATES_FAILURE, e.getMessage()));
            }
        });
    }

    /**
     * Used to Reject all the updates excluding the ones sent in the excludedAlertIds list.
     *
     * @param massUpdateTaskRequest data to find out item reject.
     * @param userId                user who submitted the reject request.
     * @return status of batch submit. success / failure.
     */
    public String rejectAllUpdates(MassUpdateTaskRequest massUpdateTaskRequest, String userId) {
        /* Create unique transaction id */
        TransactionTracker transaction = this.getTransaction(userId);
        /* Prepare : set job configuration */
        Job job = this.getMassUpdateJob();
        // The only parameter is the transaction ID.
        JobParametersBuilder parametersBuilder = new JobParametersBuilder();
        parametersBuilder.addLong(TRANSACTION_ID_PARAMETER, transaction.getTrackingId());
        parametersBuilder.addString(JOB_PARAMETER_USER_ID, userId);
        massUpdateTaskRequest.setUserId(userId);
        this.massUpdateTaskMap.add(transaction.getTrackingId(), massUpdateTaskRequest);

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
    private Job getMassUpdateJob() {
        try {
            return this.jobLocator.getJob(NUTRITION_UPDATES_MASS_UPDATE_JOB_NAME);
        } catch (NoSuchJobException e) {
            JobNotDefinedException je = new JobNotDefinedException(NUTRITION_UPDATES_MASS_UPDATE_JOB_NAME);
            throw je;
        }
    }

    /**
     * Saves an entry into the tracking table. This will be used to group all of the mass updates together as one unit.
     *
     * @return The object saved to the tracking table.
     */
    @CoreTransactional
    private TransactionTracker getTransaction(String userId) {
        TransactionTracker t = new TransactionTracker();
        t.setUserId(userId);
        t.setCreateDate(LocalDateTime.now());
        t.setSource(Integer.toString(this.sourceSystemId));
        t.setUserRole(USER_ROLE);
        return this.transactionTrackingRepository.save(t);
    }

    /**
     * Returns complete details of nutrition tasks
     *
     * @param outputStream
     */
    public void streamAllTaskDetails(ServletOutputStream outputStream, ProductSearchCriteria productSearchCriteria) {
        // Export the header to the file
        try {
            outputStream.println(TASK_DETAILS_EXPORT_HEADER);
        } catch (IOException e) {
            NutritionUpdatesTaskService.logger.error(e.getMessage());
            throw new StreamingExportException(e.getMessage(), e.getCause());
        }

        // stream 1st page
        PageableResult<AlertStaging> candidateListPage1 = this.searchActiveNutritionUpdates(productSearchCriteria, 0, PAGE_SIZE, true);
        int numberOfPages = candidateListPage1.getPageCount();

        this.streamTaskDetailsListPage(outputStream, candidateListPage1);

        // stream remaining pages
        if (numberOfPages > 1) {
            for (int currentPage = 1; currentPage < numberOfPages; currentPage++) {
                this.streamTaskDetailsListPage(outputStream, this.searchActiveNutritionUpdates(productSearchCriteria, currentPage, PAGE_SIZE, false));
            }
        }
    }

    /**
     * Stream the task details to a CSV output stream
     *
     * @param outputStream      the output stream
     * @param candidateListPage the candidate to output
     */
    private void streamTaskDetailsListPage(ServletOutputStream outputStream, PageableResult<AlertStaging> candidateListPage) {
        StringBuilder csv = new StringBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
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
                                csvLine.append(String.format(TEXT_EXPORT_FORMAT, formatter.format(candidate.getResponseByDate()))); // genesis published date
                                csvLine.append(String.format(TEXT_EXPORT_FORMAT, candidate.getProductMaster().getDepartmentCode() + candidate.getProductMaster().getSubDepartmentCode())); // dept
                                csvLine.append(String.format(TEXT_EXPORT_FORMAT, candidate.getProductMaster().getLastUpdateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))); // last modified
                                csvLine.append(String.format(TEXT_EXPORT_FORMAT_LAST, candidate.getProductMaster().getLastUpdateUserId().trim())); // last modified by
                                csvLine.append(String.format(NEWLINE_TEXT_EXPORT_FORMAT));
                                csv.append(csvLine);
                            });
                        }
                    });
                }
            });


            outputStream.print(csv.toString());

        } catch (IOException e) {
            NutritionUpdatesTaskService.logger.error(e.getMessage());
            throw new StreamingExportException(e.getMessage(), e.getCause());
        }
    }
}