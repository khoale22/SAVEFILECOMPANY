package com.heb.pm.repository;

import com.heb.pm.entity.RegulatoryOversight;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * repository for regulatory oversight.
 *
 * @author s769046
 * @since 2.8.0
 */

public interface RegulatoryOversightRepository extends JpaRepository<RegulatoryOversight, String> {
}
