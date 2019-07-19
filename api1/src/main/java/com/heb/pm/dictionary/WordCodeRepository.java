package com.heb.pm.dictionary;


import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for word code entity.
 *
 * @author vn70516
 * @since 2.7.0
 */
public interface WordCodeRepository extends JpaRepository<WordCode, String> {
}
