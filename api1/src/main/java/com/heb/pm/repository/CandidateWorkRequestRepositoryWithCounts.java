package com.heb.pm.repository;

import com.heb.pm.entity.CandidateWorkRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author vn87351
 * @since 2.12.0
 */
public interface CandidateWorkRequestRepositoryWithCounts extends JpaRepository<CandidateWorkRequest, Long> {
    /**
	 * find page tracking with tracking id
	 * @param trackingId the tracking id
	 * @param pageable page tracking status
	 * @return page tracking status
	 * @author vn87351
	 */
	Page<CandidateWorkRequest> findByTransactionTrackingTrackingId(Long trackingId, Pageable pageable);

	/**
	 * Get list of work requests by tracking id and status, order by product id.
	 * @param trackingId tracking id or batch id.
	 * @param status candidate status.
	 * @param pageable pagination request.
	 * @return list of work requests.
	 */
	Page<CandidateWorkRequest> findByTrackingIdAndStatusOrderByProductIdAsc(Long trackingId, String status, Pageable pageable);

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
	Page<CandidateWorkRequest> findByTrackingIdAndLastUpdateUserIdAndStatus(@Param("trackingId")Long trackingId,
																			@Param("lastUpdateUserId")String lastUpdateUserId,
																			@Param("status")String status, Pageable pageable);
}