/*
 * AssociatedUpcRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;/*
  * Created by m314029 on 8/5/2016.
 */

import com.heb.pm.entity.AssociatedUpc;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository to retrieve information about an associated upc.
 *
 * @author m314029
 * @since 2.0.4
 */
public interface AssociatedUpcRepository extends JpaRepository<AssociatedUpc, Long> {
}
