package com.heb.pm.repository;

import com.heb.pm.entity.CandidateProductMaster;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by thanhtran on 8/30/2017.
 */
@org.springframework.transaction.annotation.Transactional(readOnly = true)
public interface CandidateProductMasterRepository extends JpaRepository<CandidateProductMaster, Long> {

	/**
	 * Get the of product masters by ps work ids.
	 *
	 * @param psWorkRqstIds the list of ps work ids.
	 * @return the list of ps product masters.
	 */
	@Query(value = "select psProductMaster from CandidateProductMaster psProductMaster join fetch psProductMaster.candidateWorkRequest where psProductMaster.candidateWorkRequest.workRequestId in :psWorkRqstIds")
	List<CandidateProductMaster> findByCandidateWorkRequest(@Param("psWorkRqstIds")  List<Integer> psWorkRqstIds);
}
