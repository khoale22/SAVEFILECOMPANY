package com.heb.pm.repository;

import com.heb.pm.entity.SheetPanSize;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * repository for sheet pan size.
 *
 * @author s769046
 * @since 2.8.0
 */

public interface SheetPanSizeRepository extends JpaRepository<SheetPanSize, String> {
}
