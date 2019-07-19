package com.heb.pm.repository;

import com.heb.pm.entity.PackConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for pack configuration.
 *
 * @author m314029
 * @since 2.21.0
 */
public interface PackConfigurationRepository extends JpaRepository<PackConfiguration, String> {
}
