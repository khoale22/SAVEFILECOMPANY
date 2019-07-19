package com.heb.pm.repository;

import com.heb.pm.entity.PrimaryMerchandisingLocation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * repository for primary merchandising location.
 *
 * @author s769046
 * @since 2.8.0
 */

public interface PrimaryMerchandisingLocationRepository extends JpaRepository<PrimaryMerchandisingLocation, String> {
}
