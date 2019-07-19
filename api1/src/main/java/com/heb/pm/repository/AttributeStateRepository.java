package com.heb.pm.repository;

import com.heb.pm.entity.AttributeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository for AttributeStateRepository.
 *
 * @author s573181
 * @since 2.22.2
 */
public interface AttributeStateRepository extends JpaRepository<AttributeStatus, String> {
}
