/*
 * PublicationAuditRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.PublicationAudit;
import com.heb.pm.entity.PublicationAuditKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for PublicationAudit.
 *
 * @author vn70516
 * @since 2.14.0
 */
public interface PublicationAuditRepository extends JpaRepository<PublicationAudit, PublicationAuditKey> {

    PublicationAudit findTop1ByKeyItemProductIdOrderByKeyPublishDateDesc(Long itemProductId);
}
