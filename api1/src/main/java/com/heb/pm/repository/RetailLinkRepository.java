package com.heb.pm.repository;

import com.heb.pm.entity.RetailLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for upc link.
 *
 * @author s573181
 * @since 2.23.0
 */
public interface RetailLinkRepository extends JpaRepository<RetailLink, Long> {
}
