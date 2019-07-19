package com.heb.pm.repository;

import com.heb.pm.entity.SalesChannel;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository for the sales channel code table
 * @author s753601
 * @version 2.13.0
 */
public interface SalesChannelRepository extends JpaRepository<SalesChannel, String> {
}
