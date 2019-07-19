package com.heb.pm.repository;

import com.heb.pm.entity.ProductMasterAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * JPA repository for the ProductMasterAudit entity.
 *
 */
public interface ProductMasterAuditRepository extends JpaRepository<ProductMasterAudit, Long>{
    /**
     * Retrieves all records of changes on an product master attributes
     * @param prodId - product ID you are searching for
     * @return - ProductMasterAudit's changes
     */
    List< ProductMasterAudit> findByKeyProdIdOrderByKeyChangedOn(@Param("id") Long prodId);
}
