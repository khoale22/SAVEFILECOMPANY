package com.heb.pm.repository;

import com.heb.pm.entity.Audience;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository for AudienceRepository.
 *
 * @author s573181
 * @since 2.22.2
 */
public interface AudienceRepository extends JpaRepository<Audience, Long> {
}
