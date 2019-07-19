/*
 *  CheckStatusService
 *  Copyright (c) 2017 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.batchUpload.checkstatus;

import com.heb.pm.CoreEntityManager;
import com.heb.pm.batchUpload.util.BatchUploadStatus;
import com.heb.pm.batchUpload.util.BatchUploadStatusDetail;
import com.heb.pm.entity.*;
import com.heb.pm.repository.*;
import com.heb.pm.user.UserService;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.jpa.PageableResult;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * check status service.
 *
 * @author vn87351
 * @since 2.12.0
 */
@Service
public class CheckStatusService{

	/**
	 * log message
	 */
	private static final Logger logger = LoggerFactory.getLogger(CheckStatusService.class);
	//Batch Upload status
	public static final String RESULT_ACTIVATED = "Activated";
	public static final String RESULT_ACTIVE_FAILED = "Activation Failed";
	public static final String RESULT_BATCH_UPLOAD = "BATCH UPLOAD DATA";
	private static final String SUMMARY_DETAIL="Success: %d of %d Failure: %d of %d";
	private static final String DEFAULT_DATE_FORMAT = "MM-dd-yyyy hh-mm-ss";
	private static final String REPORT_DATE_FORMAT = "MM/dd/yy hh:mm";
	private static final String GENERATE_TRACKING_REPORT_ERROR = "generate tracking detail report error : %s";
	private static final String COUNT_TRACKING_KEY = "countTracking";

	// Defaults related to paging.
	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_PAGE_SIZE = 50;
	private static final int MAX_SIZE_EXPORT = 30000;
	@Autowired
	private TransactionTrackingRepository transactionTrackingRepository;
	@Autowired
	private CandidateWorkRequestRepository candidateWorkRequestRepository;
	@Autowired
	private CandidateWorkRequestRepositoryWithCounts candidateWorkRequestRepositoryWithCounts;

	@Autowired
	private ProductMasterRepository productMasterRepository;

	@Autowired
	UserService userService;

	@Autowired
	@CoreEntityManager
	private EntityManager entityManager;

	@Autowired
	CheckStatusPredicateBuilder predicateBuilder;

	/**
	 * get tracking detail.using pagging to get data.
	 * @param trackingId the tracking id
	 * @return pageable
	 * @author vn87351
	 */
	public PageableResult<BatchUploadStatusDetail> getTrackingDetail(long trackingId, Pageable request) {
		List<CandidateWorkRequest> data = candidateWorkRequestRepository
				.findByTransactionTrackingTrackingId(trackingId, request);
		return new PageableResult<>(request.getPageNumber(), convertViewDetail(data));
	}

	/**
	 * get tracking detail.using pagging to get data.
	 *
	 * @param trackingId the tracking id
	 * @return pageable
	 * @author vn87351
	 */
	public PageableResult<BatchUploadStatusDetail> getTrackingDetailWithCount(long trackingId, PageRequest pgRequest) {
		Page<CandidateWorkRequest> data = candidateWorkRequestRepositoryWithCounts
				.findByTransactionTrackingTrackingId(trackingId, pgRequest);
		return new PageableResult(pgRequest.getPageNumber(), data.getTotalPages(),
				data.getTotalElements(), convertViewDetail(data.getContent()));
	}

	/**
	 * get tracking detail for export csv. get all not pagging
	 * @param trackingId the tracking id
	 * @return List object
	 * @author vn87351
	 */
	public List<BatchUploadStatusDetail> getTrackingDetailAll(long trackingId){
		return convertViewDetail(candidateWorkRequestRepository.findByTransactionTrackingTrackingId(trackingId));
	}

	/**
	 * Convert to view detail tracking.
	 *
	 * @param candidateWorkRequests list work request
	 * @return List<BatchUploadStatusDetail>
	 */
	private List<BatchUploadStatusDetail> convertViewDetail(List<CandidateWorkRequest> candidateWorkRequests){
		List<BatchUploadStatusDetail> batchUploadStatusDetails = new ArrayList<>();
		candidateWorkRequests.forEach(obj->{
			batchUploadStatusDetails.add(convertViewDetail(obj));
		});
		return batchUploadStatusDetails;
	}

	/**
	 * Convert candidate work request to check status detail
	 *
	 * @param candidateWorkRequest CandidateWorkRequest
	 * @return BatchUploadStatusDetail
	 */
	@Transactional
	public BatchUploadStatusDetail convertViewDetail(CandidateWorkRequest candidateWorkRequest) {
		BatchUploadStatusDetail obj = new BatchUploadStatusDetail();
		obj.setProductId(0L);
		ProductMaster productMaster = null;
		try {
			if (candidateWorkRequest.getProductId() == null) {
				if (candidateWorkRequest.getUpc() != null && candidateWorkRequest.getUpc() > 0) {
					productMaster = productMasterRepository.findProductMasterByUpc(candidateWorkRequest.getUpc());
					if (null != productMaster) {
						obj.setProductId(productMaster.getProdId());
					}
				}
			} else {
				obj.setProductId(candidateWorkRequest.getProductId());
				if (candidateWorkRequest.getProductMaster() != null) {
					productMaster = candidateWorkRequest.getProductMaster();
				}
			}
			if (productMaster != null) {
				obj.setProductDescription(productMaster.getDescription());
				obj.setPrimaryUpc(productMaster.getProductPrimaryScanCodeId());
				obj.setSize(productMaster.getProductSizeText());
			}
			obj.setUpdateResult(getStatusDescription(candidateWorkRequest.getStatus()));

			if (candidateWorkRequest.getCandidateStatuses() != null && candidateWorkRequest.getCandidateStatuses().size() > 0) {
				obj.setErrorMessage(StringUtils.trimToEmpty(candidateWorkRequest.getCandidateStatuses().get(0).getCommentText()));
			} else {
				obj.setErrorMessage(BatchUploadStatusDetail.ERROR_MESSAGE_DEFAULT);
			}
			if (candidateWorkRequest.getUpc() == null) {
				obj.setUpc(0L);
			} else {
				obj.setUpc(candidateWorkRequest.getUpc());
			}
		} catch (Exception e) {
			CheckStatusService.logger.error(String.format(GENERATE_TRACKING_REPORT_ERROR, e.getMessage()));
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
		return obj;
	}

	/**
	 * get tracking info by id
	 * @param trackingId the tracking id
	 * @return page check status
	 * @author vn87351
	 */
	public PageableResult<BatchUploadStatus> getTrackingById(long trackingId){
		logger.info("service get list tracking by Id");
		TransactionTracker transactionTracking	= transactionTrackingRepository
				.findOne(trackingId);
		transactionTracking.getCandidateWorkRequest().size();
		List<TransactionTracker> tmpLst = new ArrayList<>();
		tmpLst.add(transactionTracking);
		return new PageableResult(0, getListCheckStatus(tmpLst, false));
	}

	/**
	 * Get list of tracking info with count.
	 *
	 * @param pageRequest the page request.
	 * @param trackingId  the tracking id.
	 * @param attribute   the attribute of tracking.
	 * @param description the description of tracking.
	 * @param startDate   the start date tracking.
	 * @param endDate     the end date tracking.
	 * @param userId      the user created tracking.
	 * @return PageableResult<BatchUploadStatus>
	 */
	public PageableResult<BatchUploadStatus> getListTrackingWithCount(PageRequest pageRequest, String trackingId,
																	  String attribute, String description, String userId, String startDate, String endDate) {
		PageableResult<TransactionTracker> data = this.handleFetchTracking(trackingId,
				attribute, description, startDate, endDate , userId, true, pageRequest.getPageNumber(), pageRequest.getPageSize());

		return new PageableResult(pageRequest.getPageNumber(), data.getPageCount(), data.getRecordCount(),
				getListCheckStatus(Lists.newArrayList(data.getData().iterator()), false));
	}

	/**
	 * Get list of tracking info.
	 *
	 * @param pageRequest the page request.
	 * @param trackingId  the tracking id.
	 * @param attribute   the attribute of tracking.
	 * @param description the description of tracking.
	 * @param startDate   the start date tracking.
	 * @param endDate     the end date tracking.
	 * @param userId      the user created tracking.
	 * @return PageableResult<BatchUploadStatus>
	 */
	public PageableResult<BatchUploadStatus> getListTracking(PageRequest pageRequest, String trackingId,
															 String attribute, String description, String userId, String startDate, String endDate) {
		PageableResult<TransactionTracker> data = this.handleFetchTracking(trackingId,
				attribute, description, startDate, endDate, userId, false, pageRequest.getPageNumber(), pageRequest.getPageSize());

		return new PageableResult(pageRequest.getPageNumber(), getListCheckStatus(Lists.newArrayList(data.getData().iterator()), false));
	}

	/**
	 * generate list resource default to get tracking info
	 *
	 * @return list resource id
	 * @author vn87351
	 */
	public static List<Integer> getListOfDefaultResource() {
		List<Integer> lstSource = new ArrayList<>();
		lstSource.add(SourceSystem.SOURCE_SYSTEM_PRODUCT_MAINTENANCE);
		lstSource.add(SourceSystem.SOURCE_SYSTEM_GS1);
		lstSource.add(SourceSystem.SOURCE_SYSTEM_BLOSSOM);
		return lstSource;
	}

	/**
	 * generate list file name default to get tracking info
	 *
	 * @return list file name
	 * @author vn87351
	 */
	public static List<String> getListOfDefaultFileName() {
		List<String> lstType = new ArrayList<>();
		lstType.add(TransactionTracker.FileNameCode.PRODUCT_NEW.getName());
		lstType.add(TransactionTracker.FileNameCode.PRODUCT_UPDATE.getName());
		lstType.add(TransactionTracker.FileNameCode.PRODUCT_WRITE.getName());
		lstType.add(TransactionTracker.FileNameCode.NEW_IMAGE.getName());
		lstType.add(TransactionTracker.FileNameCode.TASKS.getName());
		return lstType;
	}

	/**
	 * Get List BatchUploadStatus.
	 *
	 * @param transactionTrackers List<TransactionTracking>
	 * @param isReportDetails     the flag for report data.
	 * @return List<BatchUploadStatus>
	 * @author vn87351
	 */
	public List<BatchUploadStatus> getListCheckStatus(List<TransactionTracker> transactionTrackers, boolean isReportDetails) {
		List<BatchUploadStatus> batchUploadStatuses = new ArrayList<>();
		for (TransactionTracker trxTracking : transactionTrackers) {
			batchUploadStatuses.add(getCheckStatus(trxTracking, isReportDetails));
		}
		return batchUploadStatuses;
	}

	/**
	 * Get batch upload status.
	 *
	 * @param trxTracking     the TransactionTracker.
	 * @param isReportDetails the flag for report data.
	 * @return BatchUploadStatus
	 */
	public BatchUploadStatus getCheckStatus(TransactionTracker trxTracking, boolean isReportDetails) {
		BatchUploadStatus batchUploadStatus;
		batchUploadStatus = new BatchUploadStatus();
		batchUploadStatus.setRequestId(trxTracking.getTrackingId());
		if (trxTracking.getCreateDate() != null) {
			DateTimeFormatter fomat;
			if (isReportDetails) {
				fomat = DateTimeFormatter.ofPattern(REPORT_DATE_FORMAT);
			} else {
				fomat = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
			}
			batchUploadStatus.setDateTime(trxTracking.getCreateDate().format(fomat));
		} else {
			batchUploadStatus.setDateTime(BatchUploadStatus.DATE_DEFAULT);
		}

		batchUploadStatus.setUserId(trxTracking.getUserId());
		batchUploadStatus.setUpdateDescription(trxTracking.getFileDes());
		batchUploadStatus.setAttributeSelected(trxTracking.getFileNm());
		if (!isReportDetails) {
			if (null != trxTracking.getTrxStatCd() &&
					TransactionTracker.STAT_CODE_COMPLETE.equals(trxTracking.getTrxStatCd().trim())) {
				batchUploadStatus.setStatus(BatchUploadStatus.STATUS_COMPLETED);
			} else {
				batchUploadStatus.setStatus(BatchUploadStatus.STATUS_IN_PROGRESS);
			}
			if (BatchUploadStatus.STATUS_COMPLETED.equalsIgnoreCase(batchUploadStatus.getStatus())) {
				batchUploadStatus.setResult(getSummaryTrackingProcess(trxTracking.getTrackingId()));
			} else {
				batchUploadStatus.setResult(TransactionTracker.SUMMARY_UNKNOWN);
			}
		}
		if (null != trxTracking.getFileNm() && (trxTracking.getFileNm().trim().equalsIgnoreCase(TransactionTracker
				.FileNameCode
				.IMAGE_CONTENT_TYPE
				.getName()) ||
				trxTracking.getFileNm().trim().equalsIgnoreCase(TransactionTracker.FileNameCode.IMAGE_ATTRIBUTES
						.getName()))) {
			batchUploadStatus.setImageUpload(true);
		}
		return batchUploadStatus;
	}

	/**
	 * get summary info for batch upload request
	 *
	 * @return
	 */
	private String getSummaryTrackingProcess(long trackingId) {
		long totalRecords = candidateWorkRequestRepository
				.countByTransactionTracking_trackingId(trackingId);
		long totalSuccessRecords = candidateWorkRequestRepository
				.countByTrackingIdAndStatus(trackingId,
						CandidateWorkRequest.StatusCode.SUCCESS.getName());
		return String.format(SUMMARY_DETAIL, totalSuccessRecords, totalRecords, totalRecords - totalSuccessRecords, totalRecords);
	}

	/**
	 * Get status description of CandidateWorkRequest.
	 *
	 * @param candidateStatusCode the candidateStatusCode.
	 * @return String
	 */
	private String getStatusDescription(String candidateStatusCode) {
		String status = RESULT_BATCH_UPLOAD;
		if (CandidateWorkRequest.StatusCode.SUCCESS.getName().equals(candidateStatusCode)) {
			status = RESULT_ACTIVATED;
		} else if (CandidateWorkRequest.StatusCode.FAILURE.getName().equals(candidateStatusCode)) {
			status = RESULT_ACTIVE_FAILED;
		}
		return status;
	}

	/**
	 * Get list of user created tracking.
	 *
	 * @return List<String>
	 */
	public List<String> getUserList() {
		return this.transactionTrackingRepository.findAllUserCreatedTracking(getListOfDefaultResource(), getListOfDefaultFileName());
	}

	/**
	 * Export list of tracking.
	 *
	 * @param response    the HttpServletResponse.
	 * @param requestId   the request id.
	 * @param attribute   the attribute of tracking.
	 * @param description the description of tracking.
	 * @param startDate   the start date tracking.
	 * @param endDate     the end date tracking.
	 * @param userId      the user created tracking.
	 * @param downloadId  the download id.
	 */
	public void generateTrackingReport(HttpServletResponse response, String requestId,
									   String attribute, String description, String startDate, String endDate, String userId, String downloadId) {
		if (downloadId != null) {
			response.setHeader("Cache-Control", "no-cache, no-store");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
		}
		try {
			Map<String, Integer> countTracking = new HashMap<>();
			countTracking.put(COUNT_TRACKING_KEY, 0);
			ServletOutputStream servletOutputStream = response.getOutputStream();
			//print header for tracking export
			servletOutputStream.println(CheckStatusReportGenerator.TRACKING_REPORT_CSV_HEADING);

			//create tracking info report for first page
			PageableResult<TransactionTracker> transactionTrackerPagea1 = this.handleFetchTracking(requestId,
					attribute, description, startDate, endDate, userId, true, DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
			this.createTrackingReportCsv(countTracking, servletOutputStream, transactionTrackerPagea1);

			//loop export tracking when have many page tracking
			int numberOfPages = transactionTrackerPagea1.getPageCount();
			if (numberOfPages > 1) {
				for (int currentPage = 1; currentPage < numberOfPages; currentPage++) {
					//stop export when num row export greater than max size export
					if (countTracking.get(COUNT_TRACKING_KEY) >= MAX_SIZE_EXPORT) break;
					//create tracking info report
					this.createTrackingReportCsv(countTracking, servletOutputStream, this.handleFetchTracking(requestId,
							attribute, description, startDate, endDate, userId, false, currentPage, DEFAULT_PAGE_SIZE));
				}
			}
		} catch (Exception e) {
			CheckStatusService.logger.error(String.format(GENERATE_TRACKING_REPORT_ERROR, e.getMessage()));
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Create tracking report csv.
	 *
	 * @param countTracking       the count list of tracking reported.
	 * @param outputStream        the ServletOutputStream.
	 * @param transactionTrackers the list of TransactionTracker report.
	 */
	@Transactional
	public void createTrackingReportCsv(Map<String, Integer> countTracking, ServletOutputStream outputStream, PageableResult<TransactionTracker> transactionTrackers) {
		try {
			for (TransactionTracker transactionTracker : transactionTrackers.getData()) {
				BatchUploadStatus batchUploadStatus = getCheckStatus(transactionTracker, true);
				//count all work request of tracking
				int countWR = candidateWorkRequestRepository.countByTransactionTracking_trackingId(
						transactionTracker.getTrackingId());
				for (int i = 0; i < countWR; i += DEFAULT_PAGE_SIZE) {
					//set page and page size to get data export
					Pageable page = new PageRequest(i / DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE);
					outputStream.print(processPagingExport(countTracking, transactionTracker, page, batchUploadStatus));
					//stop export when num row export greater than max size export
					if (countTracking.get(COUNT_TRACKING_KEY) >= MAX_SIZE_EXPORT) break;
				}
				//stop export when num row export greater than max size export
				if (countTracking.get(COUNT_TRACKING_KEY) >= MAX_SIZE_EXPORT) break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Process export tracking detail information.
	 *
	 * @param countTracking      the count list of tracking reported.
	 * @param transactionTracker the TransactionTracker.
	 * @param page               the page report.
	 * @param batchUploadStatus  the BatchUploadStatus.
	 * @return a CSV string with tracking detail information.
	 */
	@Transactional
	public String processPagingExport(Map<String, Integer> countTracking, TransactionTracker transactionTracker, Pageable page, BatchUploadStatus batchUploadStatus) {
		StringBuilder csvReport = new StringBuilder();
		Page<CandidateWorkRequest> pageRs =
				candidateWorkRequestRepositoryWithCounts.findByTransactionTrackingTrackingId(
						transactionTracker.getTrackingId(), page);
		for (CandidateWorkRequest candidateWorkRequest : pageRs) {
			csvReport.append(
					CheckStatusReportGenerator.createTrackingCsv(batchUploadStatus, convertViewDetail(candidateWorkRequest)));
			countTracking.put(COUNT_TRACKING_KEY, countTracking.get(COUNT_TRACKING_KEY) + 1);
			//stop export when num row export greater than max size export
			if (countTracking.get(COUNT_TRACKING_KEY) >= MAX_SIZE_EXPORT) break;
		}
		entityManager.clear();
		return csvReport.toString();
	}

	/**
	 * Fetch tracking base on criteria search
	 *
	 * @param requestId     the request id.
	 * @param attribute     the attribute of tracking.
	 * @param description   the description of tracking.
	 * @param startDate     the start date tracking.
	 * @param endDate       the end date tracking.
	 * @param userId        the user created tracking.
	 * @param page          the page number
	 * @param pageSize      the size of page
	 * @param includeCounts request with count or not.
	 * @return PageableResult<TransactionTracker>
	 */
	private PageableResult<TransactionTracker> handleFetchTracking(String requestId, String attribute,
																   String description, String startDate, String endDate, String userId, boolean includeCounts, int page, int pageSize) {

		// Get the objects needed to build the query.
		CriteriaBuilder criteriaBuilder =  this.entityManager.getCriteriaBuilder();
		// Builds the criteria for the main query
		CriteriaQuery<TransactionTracker> queryBuilder = criteriaBuilder.createQuery(TransactionTracker.class);
		// Select from product master.
		Root<TransactionTracker> root = queryBuilder.from(TransactionTracker.class);

		// Build the where clause
		Predicate predicate = this.predicateBuilder.buildPredicate(
				root, queryBuilder, criteriaBuilder, requestId, attribute, description, startDate, endDate, userId);

		queryBuilder.where(predicate);

        /* Add the sort : Order by can slow down this query as it has search by LIKE. Since user does not have any
        specific need about ordering the content displayed on the screen, Order By/Sort has been skipped.*/
		queryBuilder.orderBy(criteriaBuilder.desc(root.get(TransactionTracker_.createDate)));

		// Get the query
		TypedQuery<TransactionTracker> tQuery = this.entityManager.createQuery(queryBuilder);

		// Sets the first row to grab and the maximum number to grab for pagination.
		tQuery.setFirstResult(page * pageSize).setMaxResults(pageSize);

		// Execute the query.
		List<TransactionTracker> results = tQuery.getResultList();

		// If the user requested counts, build and execute that query.
		if (includeCounts) {
			long count;
			int pageCount;

			// It's a new query
			CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
			// Same from and where, just wrapping a count around it.
			countQuery.select(criteriaBuilder.count(countQuery.from(TransactionTracker.class)));
			countQuery.where(predicate);

			// Run the query
			TypedQuery<Long> countTQuery = this.entityManager.createQuery(countQuery);

			count = countTQuery.getSingleResult();

			// Calculate how many pages of data there are.
			pageCount = (int) count / pageSize;
			pageCount += (int) count % pageSize == 0 ? 0 : 1;
			return new PageableResult<>(page, pageCount, count, results);
		}
		return new PageableResult<>(page, results);
	}

	/**
	 * Count item for export data.
	 *
	 * @param requestId   the request id.
	 * @param attribute   the attribute of tracking.
	 * @param description the description of tracking.
	 * @param startDate   the start date tracking.
	 * @param endDate     the end date tracking.
	 * @param userId      the user created tracking.
	 * @return Long
	 */
	public Long countItemExportQuery(String requestId, String attribute,
									 String description, String startDate, String endDate, String userId) {
		// Get the objects needed to build the query.
		CriteriaBuilder criteriaBuilder =  this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> queryBuilder = criteriaBuilder.createQuery(Long.class);
		Root<TransactionTracker> root = queryBuilder.from(TransactionTracker.class);
		Join<TransactionTracker, CandidateWorkRequest> join = root.join(TransactionTracker_.candidateWorkRequest,JoinType.INNER);
		queryBuilder.select(criteriaBuilder.count(join));
		// Build the where clause
		Predicate predicate = this.predicateBuilder.buildPredicate(
				root, queryBuilder, criteriaBuilder, requestId, attribute, description, startDate, endDate, userId);
		// join CandidateWorkRequest with TransactionTracker
		Predicate pJoin=criteriaBuilder.equal(join.get(CandidateWorkRequest_.trackingId), root.get(TransactionTracker_.trackingId));

		queryBuilder.where(predicate,pJoin);
		// Run the query
		TypedQuery<Long> countTQuery = this.entityManager.createQuery(queryBuilder);
		return countTQuery.getSingleResult();
	}
}
