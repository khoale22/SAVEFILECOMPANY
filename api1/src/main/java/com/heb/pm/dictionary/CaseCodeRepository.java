package com.heb.pm.dictionary;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for case code entity.
 *
 * @author vn70516
 * @since 2.7.0
 */
public interface CaseCodeRepository extends JpaRepository<CaseCode, String> {
}
