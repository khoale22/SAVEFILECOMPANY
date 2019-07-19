package com.heb.pm.repository;

import com.heb.pm.entity.SubCommodityAudit;
import com.heb.pm.entity.SubCommodityAuditKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for Product Scan Code Extent Audit.
 *
 * @author vn70529
 * @since 2.17.9
 */
public interface SubCommodityAuditRepository extends JpaRepository<SubCommodityAudit, SubCommodityAuditKey> {

    List<SubCommodityAudit> findByKey_SubCommodityCodeOrderByLstUpdtTsDesc(Integer subCommodityCode);

}
