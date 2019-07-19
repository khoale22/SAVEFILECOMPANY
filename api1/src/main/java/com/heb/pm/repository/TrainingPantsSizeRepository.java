package com.heb.pm.repository;

import com.heb.pm.entity.TrainingPantsSize;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * repository for training pants size.
 *
 * @author s769046
 * @since 2.8.0
 */

public interface TrainingPantsSizeRepository extends JpaRepository<TrainingPantsSize, String> {
}
