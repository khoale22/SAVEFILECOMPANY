package com.heb.pm.repository;

import com.heb.pm.entity.TransactionTracker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author vn87351
 * @since 2.12.0
 */
public interface TransactionTrackingRepositoryWithCount extends JpaRepository<TransactionTracker, Long>,TransactionTrackingRepositoryCommon {
}
