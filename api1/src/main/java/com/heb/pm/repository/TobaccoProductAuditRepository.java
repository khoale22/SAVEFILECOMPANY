package com.heb.pm.repository;

import com.heb.pm.entity.TobaccoProductAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TobaccoProductAuditRepository extends JpaRepository<TobaccoProductAudit, Long> {
	/**
	 * Retrieves all records of changes on an Tobacco product audit attributes
	 * @param prodId - product ID you are searching for
	 * @return - TobaccoProductAudit's changes
	 */
	List<TobaccoProductAudit> findByKeyProdIdOrderByKeyChangedOn(@Param("id") Long prodId);
}
