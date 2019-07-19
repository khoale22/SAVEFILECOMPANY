/*
 * ProductUpdatesTaskService
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import com.heb.pm.entity.AlertStaging;
import com.heb.pm.repository.AlertStagingRepository;
import com.heb.pm.repository.AlertStagingRepositoryWithCount;
import com.heb.pm.CoreTransactional;
import com.heb.pm.customHierarchy.CustomerHierarchyService;
import com.heb.pm.entity.*;
import com.heb.pm.massUpdate.job.JobNotDefinedException;
import com.heb.pm.entity.HierarchyContext;
import com.heb.pm.entity.ItemMasterKey;
import com.heb.pm.entity.ProductMaster;
import com.heb.pm.entity.User;
import com.heb.pm.repository.BdmRepository;
import com.heb.pm.repository.ProductInfoRepository;
import com.heb.pm.repository.TransactionTrackingRepository;
import com.heb.pm.user.UserService;
import com.heb.pm.ws.ApplicationAlertStagingServiceClient;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.util.DateUtils;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.jpa.PageableResult;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import com.heb.pm.CoreEntityManager;

/**
 * This service is used to serve product updates related queries.
 *
 * @author vn40486
 * @since 2.15.0
 */
@Service
public class ProductUpdatesTaskService {

    private static final Logger logger = LoggerFactory.getLogger(ProductUpdatesTaskService.class);

    // The default number of products to search for. This number and fewer will have the maximum performance.
    private static final int DEFAULT_PRODUCT_ID_COUNT = 100;
    public static final int MAX_UPDATE_REASON_COUNT = 11;
    private static final int PAGEABLE_FIRST_PAGE = 0;
    private static final int PAGEABLE_MAX_PER_REQUEST_LIMIT = 1000;
	public static final String DATE_YYYY_MM_DD = "yyyy-MM-dd";
	private static final String PRODUCT_UPDATES_MASS_UPDATE_JOB_NAME = "I18X916";
	private static final String USER_ROLE = "USER";
	private static final String TRANSACTION_ID_PARAMETER = "trackingId";
	public static final String MESSAGE_BATCH_SUBMIT_SUCCESS = "SUCCESS";
	public static final String MESSAGE_BATCH_SUBMIT_FAILURE = "FAILURE";
	private static final String ERROR_PRODUCT_UPDATES_FAILURE = "Error with product updates for alert: %s ";
	private static final String ERROR_BATCH_SUBMIT_FAILURE = "Error with product updates mass update: %s ";
	private static final String ERROR_RESPONSE = "Error with close alert: %s ";
	private static final String JOB_PARAMETER_USER_ID = "userId";

    // Log Messages
    private static final String UNABLE_TO_RESOLVE_MESSAGE =
            "Unable to resolve record with product ID: %s";
    private static final String EXPORT_EXCEL_PAGE_MESSAGE =
            "Exporting page %s of %s";
    private static final String DONE_EXPORT_EXCEL_MESSAGE =
            "Export completed";

    @Autowired
    private AlertStagingRepository alertStagingRepository;
    @Autowired
    private AlertStagingRepositoryWithCount alertStagingRepositoryWithCount;
    @Autowired
    ProductInfoRepository productInfoRepository;
    @Autowired
    CustomerHierarchyService customerHierarchyService;
    @Autowired
    UserService userService;
    @Autowired
    //@DB2EntityManager
    @CoreEntityManager
    private EntityManager entityManager;
    @Autowired
    private ProductUpdatesTaskPredicateBuilder predicateBuilder;
    @Autowired
    BdmRepository bdmRepository;
    @Autowired
    private ProductMasterResolver productMasterResolver;
	@Autowired
	private JobLocator jobLocator;
	@Autowired
	@Qualifier("asyncJobLauncher")
	private JobLauncher jobLauncher;

	@Value("${app.sourceSystemId}")
	private int sourceSystemId;

	@Autowired
	private TransactionTrackingRepository transactionTrackingRepository;
	@Autowired
	private MassUpdateTaskMap massUpdateTaskMap;
	@Autowired
	private ApplicationAlertStagingServiceClient applicationAlertStagingServiceClient;

    public static final String TASK_DETAILS_EXPORT_HEADER = "UPC, Item Code, Channel, Product ID, Product Description, Size Text, Sub Commodity, BDM, Sub Department, Last Modified, Last Modified By, Notes";
    private static final String TEXT_EXPORT_FORMAT = "\"%s\",";
    private static final String NEWLINE_TEXT_EXPORT_FORMAT = "\n";

    /**
     * Used to get consistent size lists to query runners.
     */
    private LongPopulator longPopulator = new LongPopulator();

    /**
     * Used to get count of all active product updates alerts.
     * @return count.
     */
    Long getActiveProductUpdatesCount() {
        return alertStagingRepository.countAlertByAlertTypeAndAlertStatus(
                AlertStaging.AlertTypeCD.PRODUCT_UPDATES.getName(),AlertStaging.AlertStatusCD.ACTIVE.getName());
    }

    /**
     * Used to get a list of all users/assignee of the products listed under an alert type.
     * @param alertType alert Type.
     * @return list of all users of a task.
     */
    List<User> getAssignedToUsers(String alertType) {
        Pageable pageable = new PageRequest(PAGEABLE_FIRST_PAGE, PAGEABLE_MAX_PER_REQUEST_LIMIT);
        List<String> assignedToUserIds = this.alertStagingRepository.findAssigneeByAlertType(
                alertType,AlertStaging.AlertStatusCD.ACTIVE.getName(),pageable);
        List<User> users = this.userService.getUserById(assignedToUserIds);
        Collections.sort(users, Comparator.comparing(User::getFullName));
        return users;
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
     * @return
     */
    PageableResult<AlertStaging> getProducts(
            String alertType, String assignee, List<Long> attributes, String showOnSite, String salesChannels, boolean includeCounts, int page, int pageSize) {
        //1.Get all alerts(products).
        PageableResult<AlertStaging> pageableResult = handleFetchProducts(alertType, AlertStaging.AlertStatusCD.ACTIVE.getName(),
                assignee, attributes, showOnSite, salesChannels, includeCounts, page, pageSize);
        if (pageableResult.getData() != null) {
        	pageableResult.getData().forEach(alertStaging -> {
        		Long productId = Long.valueOf(alertStaging.getAlertKey().trim());
        		ProductMaster productMaster = productInfoRepository.findOne(productId);
        		if (productMaster == null) {
        			ProductUpdatesTaskService.logger.error(String.format(UNABLE_TO_RESOLVE_MESSAGE, alertStaging.getAlertKey().trim()));
        		} else {
        			this.resolveResults(productMaster);
        			this.productMasterResolver.fetch(productMaster);
        			alertStaging.setProductMaster(productMaster);
        		}
        	});
        }
        return pageableResult;
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
     * @return
     */
    PageableResult<AlertStaging> getAllProducts(
    		String alertType, String assignee, List<Long> attributes, String showOnSite, String salesChannels, boolean includeCounts, int page, int pageSize) {
    	//1.Get all alerts(products).
    	PageableResult<AlertStaging> pageableResult = handleFetchProducts(alertType, AlertStaging.AlertStatusCD.ACTIVE.getName(),
    			assignee, attributes, showOnSite, salesChannels, includeCounts, page, pageSize);
    	if (pageableResult.getData() != null && pageableResult.getData().iterator().hasNext()) {
    		//2. Collect product ids from the Alerts.
    		List<Long> productIds = new ArrayList<>();
    		pageableResult.getData().forEach(alertStaging -> productIds.add(Long.valueOf(alertStaging.getAlertKey().trim())));
    		//3. Send collected product-ids to master db to fetch their respective product info.
    		this.longPopulator.populate(productIds, DEFAULT_PRODUCT_ID_COUNT);
    		List<ProductMaster> productMasters = productInfoRepository.findAll(productIds);
    		//4. Update Alerts with their respective ProductMaster.
    		productMasters.stream().forEach(productMaster -> this.resolveResults(productMaster));
    		Map<Long,ProductMaster> productMasterMap = productMasters.stream()
    				.collect(Collectors.toMap(productMaster -> productMaster.getProdId(), productMaster -> productMaster));
    		pageableResult.getData().forEach(alertStaging -> {
    			ProductMaster productMaster = productMasterMap.get(Long.valueOf(alertStaging.getAlertKey().trim()));
    			if (productMaster == null) {
    				ProductUpdatesTaskService.logger.error(String.format(UNABLE_TO_RESOLVE_MESSAGE, alertStaging.getAlertKey().trim()));
    			} else {
    				alertStaging.setProductMaster(productMaster);
    			}
    		});
    		pageableResult.getData().forEach(alertStaging ->  {
    			if(alertStaging.getProductMaster() != null){
    				alertStaging.getProductMaster().getProdId();
    			}
    		});
    	}
    	return pageableResult;
    }

    /**
     * Fetches list of all products(alerts) tagged under a given task type. Inlcudes building predicate and pagination
     * handling.
     *
     * @param alertType     alert type like PRUPD/PRRVW/NWPRD
     * @param assignee      alert assigned to user.
     * @param attributes    list of update reasons, where each reason is identified by it's attribute code.
     * @param showOnSite    show on site Y/N.
     * @param salesChannels list of sales channels filter.
     * @param includeCounts identifies first fetch where total count of the records is required for pagination.
     * @param page          page requested.
     * @param pageSize      number of records per page.
     * @return PageableResult<AlertStaging>
     */
    private PageableResult<AlertStaging> handleFetchProducts(
            String alertType, String alertStatus, String assignee,List<Long> attributes, String showOnSite, String salesChannels,
            boolean includeCounts, int page, int pageSize) {
		logger.info(alertType+"::"+ alertStatus+"::"+ assignee+"::"+attributes+"::"+ showOnSite+"::"+
		 includeCounts+"::"+  page+"::"+  pageSize);
        // Get the objects needed to build the query.
        CriteriaBuilder criteriaBuilder =  this.entityManager.getCriteriaBuilder();
        // Builds the criteria for the main query
        CriteriaQuery<AlertStaging> queryBuilder = criteriaBuilder.createQuery(AlertStaging.class);
        // Select from product master.
        Root<AlertStaging> root = queryBuilder.from(AlertStaging.class);

        // Build the where clause
        Predicate predicate = this.predicateBuilder.buildPredicate(
                root, queryBuilder, criteriaBuilder, alertType, alertStatus, assignee, attributes, showOnSite, salesChannels);

        queryBuilder.where(predicate);

        /* Add the sort : Order by can slow down this query as it has search by LIKE. Since user does not have any
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
     * Helper functon to resolve the details of a task starting from work request to it's associated product details.
     * @param productMaster ProductMaster.
     */
    private void resolveResults(ProductMaster productMaster) {
        try {
            productMaster.getProductBrand().getDisplayName();
            List<ProductFullfilmentChanel> productFullfilmentChanels = new ArrayList<>();
            for (ProductFullfilmentChanel productFullfilmentChanel: productMaster.getProductFullfilmentChanels()){
                if(DateUtils.isGreaterThanOrEqualToday(productFullfilmentChanel.getExpirationDate().toString(),ProductFullfilmentChanel.DATE_FORMAT)){
                    productFullfilmentChanels.add(productFullfilmentChanel);
                }
            }
            productMaster.setProductFullfilmentChanels(productFullfilmentChanels);
            productMaster.getProductOnlines().isEmpty();
            productMaster.getMasterDataExtensionAttributes().isEmpty();
            productMaster.setPrimaryCustomerHierarchy(this.customerHierarchyService.getPrimaryCustomerHierarchy(
                    productMaster.getProdId(), HierarchyContext.HierarchyContextCode.CUST.getName()));
        } catch (Exception e) {
            ProductUpdatesTaskService.logger.error(String.format(UNABLE_TO_RESOLVE_MESSAGE, productMaster.getProdId()));
        }
    }

    /**
     * Returns complete details of a task with list of products added under it by assignee and alertType
     *
     * @param outputStream  the ServletOutputStream
     * @param alertType     alert Type
     * @param assignee      assignee
     * @param attributes    list of update reasons, where each reason is indetified by it's attribute code.
     * @param showOnSite    show on site Y/N.
     * @param salesChannels list of sales channels filter.
     */
    public void streamAllTaskDetails(ServletOutputStream outputStream, String alertType, String assignee, List<Long> attributes, String showOnSite, String salesChannels) {
        try {
            // Export the header to the file
            outputStream.println(TASK_DETAILS_EXPORT_HEADER);

            // stream 1st page
            PageableResult<AlertStaging> candidateListPage1 = this.getProducts(alertType, assignee, attributes, showOnSite, salesChannels, true, 0, 100);
            int numberOfPages = candidateListPage1.getPageCount();

            ProductUpdatesTaskService.logger.info(String.format(EXPORT_EXCEL_PAGE_MESSAGE, 1, numberOfPages));
            this.streamTaskDetailsListPage(outputStream, candidateListPage1);

            // stream remaining pages
            if(numberOfPages > 1) {
                for (int currentPage = 1; currentPage < numberOfPages; currentPage++) {
                    ProductUpdatesTaskService.logger.info(String.format(EXPORT_EXCEL_PAGE_MESSAGE, currentPage + 1, numberOfPages));
                    this.streamTaskDetailsListPage(outputStream, this.getProducts(alertType, assignee, attributes, showOnSite, salesChannels, false, currentPage, 100));
                }
            }
            ProductUpdatesTaskService.logger.info(String.format(DONE_EXPORT_EXCEL_MESSAGE));
        } catch (IOException e) {
            ProductUpdatesTaskService.logger.error(e.getMessage());
            throw new StreamingExportException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Stream the task details to a CSV output stream
     * @param outputStream the output stream
     * @param candidateListPage the candidate to output
     */
    private void streamTaskDetailsListPage(ServletOutputStream outputStream, PageableResult<AlertStaging> candidateListPage) throws IOException {
        StringBuilder csv = new StringBuilder();

        // UPC, Item Code, Channel, Product ID, Product Description, Size Text, Sub Commodity, BDM, Sub Department, Last Modified, Last Modified By";
        candidateListPage.getData().forEach(candidate -> {

            if (candidate.getProductMaster() != null && candidate.getProductMaster().getProdItems() != null && candidate.getProductMaster().getProdItems().size() > 0) {
                candidate.getProductMaster().getProdItems().forEach(item -> {
                    if (item.getItemMaster() != null && item.getItemMaster().getPrimaryUpc() != null && item.getItemMaster().getPrimaryUpc().getAssociateUpcs() != null && item.getItemMaster().getPrimaryUpc().getAssociateUpcs().size() > 0) {
                        item.getItemMaster().getPrimaryUpc().getAssociateUpcs().forEach(upc -> {
                            try {
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
                                csvLine.append(String.format(TEXT_EXPORT_FORMAT, candidate.getProductMaster().getLastUpdateUserId().trim())); // last modified by
                                csvLine.append(String.format(TEXT_EXPORT_FORMAT, "")); // notes
                                csvLine.append(String.format(NEWLINE_TEXT_EXPORT_FORMAT));
                                csv.append(csvLine);
                            } catch (Exception e) {
                                streamErrorLine(csv, candidate);
                            }
                        });
                    }
                });
            }
        });

        outputStream.print(csv.toString());
    }

    private void streamErrorLine(StringBuilder csv, AlertStaging candidate) {
        StringBuilder csvLine = new StringBuilder();

        csvLine.append(String.format(TEXT_EXPORT_FORMAT, "")); // upc
        csvLine.append(String.format(TEXT_EXPORT_FORMAT, "")); // item code
        csvLine.append(String.format(TEXT_EXPORT_FORMAT, "")); // channel
        if (candidate != null && candidate.getProductMaster() != null && candidate.getProductMaster().getProdId() != null) {
            csvLine.append(String.format(TEXT_EXPORT_FORMAT, candidate.getProductMaster().getProdId())); // product ID
        } else {
            csvLine.append(String.format(TEXT_EXPORT_FORMAT, "")); // product ID
        }

        if (candidate != null && candidate.getProductMaster() != null && candidate.getProductMaster().getDescription() != null) {
            csvLine.append(String.format(TEXT_EXPORT_FORMAT, candidate.getProductMaster().getDescription())); // product description
        } else {
            csvLine.append(String.format(TEXT_EXPORT_FORMAT, "")); // product description
        }

        csvLine.append(String.format(TEXT_EXPORT_FORMAT, "")); // size text
        csvLine.append(String.format(TEXT_EXPORT_FORMAT, "")); // sub commodity
        csvLine.append(String.format(TEXT_EXPORT_FORMAT, "")); // BDM
        csvLine.append(String.format(TEXT_EXPORT_FORMAT, "")); // sub department
        csvLine.append(String.format(TEXT_EXPORT_FORMAT, "")); // last modified
        csvLine.append(String.format(TEXT_EXPORT_FORMAT, "")); // last modified by
        csvLine.append(String.format(TEXT_EXPORT_FORMAT, "Error retrieving product data")); // notes

        csvLine.append(String.format(NEWLINE_TEXT_EXPORT_FORMAT));

        csv.append(csvLine);
    }
	/**
	 * Returns a reference to the mass update job.
	 *
	 * @return A reference to the mass update job.
	 */
	private Job getMassUpdateJob() {
		try {
			return this.jobLocator.getJob(PRODUCT_UPDATES_MASS_UPDATE_JOB_NAME);
		} catch (NoSuchJobException e) {
			JobNotDefinedException je = new JobNotDefinedException(PRODUCT_UPDATES_MASS_UPDATE_JOB_NAME);
			throw je;
		}
	}

	/**
	 * Saves an entry into the tracking table. This will be used to group all of the mass updates together as one unit.
	 *
	 * @return The object saved to the tracking table.
	 */
	@CoreTransactional
	private TransactionTracker getTransaction(MassUpdateTaskRequest massUpdateTaskRequest) {
		TransactionTracker t = new TransactionTracker();
		t.setUserId(massUpdateTaskRequest.getUserId());
		t.setCreateDate(LocalDateTime.now());
		t.setSource(Integer.toString(this.sourceSystemId));
		t.setUserRole(USER_ROLE);
		t.setFileDes(massUpdateTaskRequest.getDescription());
		if(massUpdateTaskRequest.getActionType()==MassUpdateTaskRequest.ACTION_TYPE_SAVE)
			t.setFileNm(TransactionTracker.FileNameCode.PRODUCT_ATTRIBUTES.getName());
		else if (massUpdateTaskRequest.getActionType()==MassUpdateTaskRequest.ACTION_TYPE_PUBLISH_PRODUCT){
			t.setFileNm(TransactionTracker.FileNameCode.PUBLISH_PRODUCT.getName());
		}else{
			t.setFileNm(TransactionTracker.FileNameCode.PRODUCT_UPDATE.getName());
		}
		t.setTrxStatCd(TransactionTracker.STAT_CODE_NOT_COMPLETE);
		return this.transactionTrackingRepository.save(t);
	}

	/**
	 * submit request to run batch async
	 * @param massUpdateTaskRequest
	 * @return
	 */
	public Long submit(MassUpdateTaskRequest massUpdateTaskRequest) {
        /* Create unique transaction id */
		TransactionTracker transaction = this.getTransaction(massUpdateTaskRequest);
        /* Prepare : set job configuration */
		Job job = this.getMassUpdateJob();
		// The only parameter is the transaction ID.
		JobParametersBuilder parametersBuilder = new JobParametersBuilder();
		parametersBuilder.addLong(TRANSACTION_ID_PARAMETER, transaction.getTrackingId());
		parametersBuilder.addString(JOB_PARAMETER_USER_ID, massUpdateTaskRequest.getUserId());
		this.massUpdateTaskMap.add(transaction.getTrackingId(),massUpdateTaskRequest);
		try {
			// Kick off the job. This is asynchronous, so we won't have the status of the job after the call.
			this.jobLauncher.run(job, parametersBuilder.toJobParameters());
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
				JobParametersInvalidException e) {
			logger.error(String.format(ERROR_BATCH_SUBMIT_FAILURE, e.getMessage()));
			return 0L;
		}
		return transaction.getTrackingId();
	}

	/**
	 * Remove alert not select all. real time process
	 * @param massUpdateTaskRequest
	 */
	public void removeAlertNotSelectAll(MassUpdateTaskRequest massUpdateTaskRequest){
		if(massUpdateTaskRequest.getSelectedAlertStaging()!=null ){
			massUpdateTaskRequest.getSelectedAlertStaging().forEach(alert -> {
				removeAlert(alert,massUpdateTaskRequest.getUserId());
			});
		}
	}

	/**
	 * Call webservice to remove alert
	 * @param alertStaging
	 * @param userId
	 */
	public void removeAlert(AlertStaging alertStaging,String userId){
		try {
			applicationAlertStagingServiceClient.updateAlert(alertStaging.getAlertID(),AlertStaging.AlertStatusCD.CLOSE.getName());
			applicationAlertStagingServiceClient.deleteAlertRecipient(alertStaging.getAlertID(), userId);
		}
		catch (CheckedSoapException ex){
			logger.error(String.format(ERROR_RESPONSE, ex.getMessage()));
		}
	}
}