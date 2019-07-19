package com.heb.pm.repository;

import com.heb.pm.entity.GenericEntityDescription;
import com.heb.pm.entity.GenericEntityDescriptionKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository to retrieve information about generic entity relationships.
 *
 * @author vn55306
 * @since 2.12.0
 */
public interface GenericEntityDescriptionRespository extends JpaRepository<GenericEntityDescription,GenericEntityDescriptionKey>{
}
