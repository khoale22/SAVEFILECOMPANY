package com.heb.pm.repository;

import com.heb.pm.entity.SourceSystem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A repository to query the src_system table in the database
 * @author s769046
 * @version 2.21.0
 */
public interface SourceSystemRepository extends JpaRepository<SourceSystem, Long> {



}
