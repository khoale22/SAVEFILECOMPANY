package com.heb.pm.repository;

import com.heb.pm.entity.PizzaPortion;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * repository for pizza portion.
 *
 * @author s769046
 * @since 2.8.0
 */

public interface PizzaPortionRepository extends JpaRepository<PizzaPortion, String> {
}
