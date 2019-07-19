package com.heb.pm.repository;

import com.heb.pm.entity.CodeDateType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for code date type.
 *
 * @author m314029
 * @since 2.21.0
 */
public interface CodeDateTypeRepository extends JpaRepository<CodeDateType, String> {
}
