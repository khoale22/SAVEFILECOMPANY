package com.heb.pm.repository;

import com.heb.pm.entity.MasterPackMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for master pack material.
 *
 * @author m314029
 * @since 2.21.0
 */
public interface MasterPackMaterialRepository extends JpaRepository<MasterPackMaterial, String> {
}
