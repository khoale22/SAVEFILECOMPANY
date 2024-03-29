package com.heb.pm.repository;

import com.heb.pm.entity.SheetSize;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * repository for sheet size.
 *
 * @author s769046
 * @since 2.8.0
 */

public interface SheetSizeRepository extends JpaRepository<SheetSize, String> {
}
