/*
 * CandidateWorkRequestRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.CandidateWorkRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author d116773
 * @since 2.12.0
 */
public interface CandidateWorkRequestRepository extends JpaRepository<CandidateWorkRequest, Long> {

    /**
     * Find an Candidate Work Request by intntId, pdSetupStatCd and prodId.
     *
     * @param intent    - The intnt Id
     * @param status    - The product setup status code.1
     * @param productId - The product id.
     * @return CandidateWorkRequest
     * @author vn73545
     */
	List<CandidateWorkRequest> findByIntentAndStatusAndProductIdOrderByCreateDateDesc(Integer intent, String status, Long productId);
	/**
	 * Find an Candidate Work Request by intntId, prodId.
	 * @param intntId - The intnt Id
	 * @param productIds _ The product setup status code.1
	 * @return
	 */
	List<CandidateWorkRequest >findByIntentAndProductIdIn(Integer intntId, List<Long> productIds);
	/**
	 * Find an Candidate Work Request by intntId, prodId.
	 * @param intntId - The intnt Id
	 * @param productIds _ The product setup status code.
	 * @param status - Status code of 104 for DELETED
	 * @return
	 */
	List<CandidateWorkRequest >findByIntentAndProductIdInAndStatusNot(Integer intntId, List<Long> productIds, String status);
    /**
	 * find page tracking with tracking id
	 * @param trackingId the tracking id
	 * @param pageable page tracking status
	 * @return page tracking status
	 * @author vn87351
	 */

	List<CandidateWorkRequest> findByTransactionTrackingTrackingId(Long trackingId, Pageable pageable);


	/**
	 * find list tracking with tracking id
	 * @param trxTrkgId the tracking id
	 * @return list tracking status
	 * @author vn87351
	 */
	int countByTransactionTracking_trackingId(Long trxTrkgId);
	/**
	 * find list tracking with tracking id
	 * @param trackingId the tracking id
	 * @return list tracking status
	 * @author vn87351
	 */
	List<CandidateWorkRequest> findByTransactionTrackingTrackingId(Long trackingId);

	/**
	 * User to fetch work requests filtered by intent , request status, source of the request and the product id.
	 * @param intent type of update or candidate request.
	 * @param status status of the request.
	 * @param sourceSystem source of the request.
	 * @param productId product id.
	 * @return
	 */
	List<CandidateWorkRequest> findByIntentAndStatusAndSourceSystemAndProductId(@Param("intent")Integer intent,
																				@Param("status")String status,
																				@Param("sourceSystem")Integer sourceSystem,
																				@Param("productId")Long productId);
	/**
	 * User to fetch work requests filtered by intent , request status, source of the request and by multiple product
	 * ids.
	 * @param intent type of update or candidate request.
	 * @param status status of the request.
	 * @param sourceSystem source of the request.
	 * @param productId product id.
	 * @return
	 */
	List<CandidateWorkRequest> findByIntentAndStatusAndSourceSystemAndProductIdIn(@Param("intent")Integer intent,
																				  @Param("status")String status,
																				  @Param("sourceSystem")Integer sourceSystem,
																				  @Param("productId")List<Long> productId);

	/**
	 * User to fetch work requests filtered by intent , request status, source of the request and by multiple UPC
	 * ids.
	 * @param intent type of update or candidate request.
	 * @param status status of the request.
	 * @param upcs list upc.
	 * @return the list of CandidateWorkRequest.
	 */
	List<CandidateWorkRequest> findByIntentAndStatusNotInAndUpcIn(@Param("intent")Integer intent,
																				  @Param("status")List<String> status,
																				  @Param("upc")List<Long> upcs);

	/**
	 * Verifies if a candidate for particular product with given status already exist under the given transaction. Used
	 * in cases like add a product to a Task(transaction) to avoid creating duplicate record for same product.
	 *
	 * @param trackingId transaction tracking Id.
	 * @param productId product id.
	 * @param status candidate status.
	 * @return TRUE if candidate exists else FALSE.
	 */
	long countByTrackingIdAndProductIdAndStatus(Long trackingId, Long productId, String status);

	/**
	 * Get list of work requests by tracking id and status, order by product id.
	 * @param trackingId tracking id or batch id.
	 * @param status candidate status.
	 * @param pageable pagination request.
	 * @return list of work requests.
	 */
	List<CandidateWorkRequest> findByTrackingIdAndStatusOrderByProductIdAsc(Long trackingId, String status, Pageable pageable);
	/**
	 * Used to get list of candidates of a transaction filtered by status of the products(candidates).
	 *
	 * @param trackingId tracking id or transaction id.
	 * @param status status of the candidate.
	 * @param pageable pagination request.
	 * @return list of work requests.
	 */
	@Query(value = "SELECT w FROM CandidateWorkRequest w WHERE w.trackingId=:trackingId " +
			" AND  w.status=:status ORDER BY w.productId asc")
	List<CandidateWorkRequest> findByTrackingIdAndStatus(@Param("trackingId")Long trackingId,
														 @Param("status")String status, Pageable pageable);
	/**
	 * Used to get list of candidates of a transaction filtered by asignee of the products(candidates).
	 *
	 * @param trackingId tracking id or transaction id.
	 * @param lastUpdateUserId candidate assignee - user id in all uppercase.
	 * @param status status of the candidate.
	 * @param pageable pagination request.
	 * @return list of work requests.
	 */
	@Query(value = "SELECT w FROM CandidateWorkRequest w WHERE w.trackingId=:trackingId " +
			" AND UPPER(w.lastUpdateUserId)=:lastUpdateUserId AND w.status=:status ORDER BY w.productId asc")
	List<CandidateWorkRequest> findByTrackingIdAndLastUpdateUserIdAndStatus(@Param("trackingId")Long trackingId,
																			@Param("lastUpdateUserId")String lastUpdateUserId,
																			@Param("status")String status, Pageable pageable);

	/**
	 * Returns list of candidates identified by the tracking id and product ids input.
	 * @param trackingId tracking id.
	 * @param status status of the candidate.
	 * @param productId list of productIds to be searched for.
	 * @return list of canidates or work requests.
	 */
	List<CandidateWorkRequest> findByTrackingIdAndStatusAndProductIdIn(Long trackingId, String status, List<Long> productId);

	/**
	 * Used to fetch the list of all assignee of the products listed under a transaction id.
	 * @param trackingId tracking id.
	 * @param status status of the candidate.
	 * @return list of user ids.
	 */
	@Query(value = "SELECT DISTINCT lastUpdateUserId from CandidateWorkRequest WHERE trackingId=:trackingId AND status=:status")
	List<String> findByTrackingIdAndStatus(@Param("trackingId")Long trackingId, @Param("status")String status);/**
	 * Get count of all CandidateWorkRequests.
	 *
	 * @param trackingId tracking id or transaction id.
	 * @param lastUpdateUserId candidate assignee - user id in all uppercase.
	 * @param status status of the candidate.
	 * @return  returns count of CandidateWorkRequests matching the input filters.
	 */
	Long countByTrackingIdAndLastUpdateUserIdAndStatus(@Param("trackingId")Long trackingId,
													   @Param("lastUpdateUserId")String lastUpdateUserId,
													   @Param("status")String status);
	/**
	 * Get count of all CandidateWorkRequests.
	 *
	 * @param trackingId tracking id or transaction id.
	 * @param status status of the candidate.
	 * @return  returns count of CandidateWorkRequests matching the input filters.
	 */
	Long countByTrackingIdAndStatus(@Param("trackingId")Long trackingId,
									@Param("status")String status);
	/**
	 * Find a CandidateWorkRequest by trackingId and status productId.
	 * @param trackingId the tracking id.
	 * @param assigneeId the assignee id.
	 * @param status the status
	 * @param productId the product id.
	 * @return the CandidateWorkRequest.
	 */
	CandidateWorkRequest findOneByTrackingIdAndLastUpdateUserIdAndStatusAndProductId(@Param("trackingId")Long trackingId,
																					 @Param("lastUpdateUserId") String assigneeId,
																					 @Param("status")String status,
																					 @Param("productId")Long productId);

}