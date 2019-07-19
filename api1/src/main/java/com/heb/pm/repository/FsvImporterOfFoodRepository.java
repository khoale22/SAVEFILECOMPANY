package com.heb.pm.repository;

import com.heb.pm.entity.FsvImporterOfFood;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * repository for importer of food.
 *
 * @author s769046
 * @since 2.8.0
 */

public interface FsvImporterOfFoodRepository extends JpaRepository<FsvImporterOfFood, String> {
}
