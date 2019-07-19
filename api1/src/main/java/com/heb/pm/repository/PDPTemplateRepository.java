package com.heb.pm.repository;

import com.heb.pm.entity.PDPTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for PDPTemplate.
 *
 * @author m314029
 * @since 2.6.0
 */
public interface PDPTemplateRepository extends JpaRepository<PDPTemplate, String> {
}
